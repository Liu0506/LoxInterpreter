package com.cheng.lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cheng.lox.TokenType.*;

// 导入所有TokenType枚举值，方便直接使用

/**
 * 词法分析器
 * 负责将源代码字符串转换为Token序列
 */
class Scanner {
    private static final Map<String, TokenType> keywords;

    // 关键字映射
    static {
        keywords = new HashMap<>();
        // keywords.put("and", AND);
        // keywords.put("or", OR);
        keywords.put("else", ELSE);
        keywords.put("false", FALSE);
        keywords.put("for", FOR);
        keywords.put("fun", FUN);
        keywords.put("if", IF);
        keywords.put("null", NULL);
        keywords.put("or", OR);
        keywords.put("print", PRINT);
        keywords.put("return", RETURN);
        keywords.put("super", SUPER);
        keywords.put("this", THIS);
        keywords.put("true", TRUE);
        keywords.put("var", VAR);
        keywords.put("while", WHILE);
    }

    /**
     * 源代码字符串
     */
    private final String source;
    /**
     * 生成的Token列表
     */
    private final List<Token> tokens = new ArrayList<>();
    /**
     * 当前正在扫描的Token的起始位置
     */
    private int start = 0;
    /**
     * 当前扫描位置
     */
    private int current = 0;
    /**
     * 当前行号
     */
    private int line = 1;

    /**
     * 构造函数
     *
     * @param source 源代码字符串
     */
    Scanner(String source) {
        this.source = source;
    }

    /**
     * 扫描所有Token
     *
     * @return Token列表
     */
    List<Token> scanTokens() {
        // 循环扫描直到文件结束
        while (!isAtEnd()) {
            // 重置起始位置，准备扫描下一个Token
            start = current;
            scanToken();
        }

        // 添加文件结束符Token
        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    /**
     * 扫描单个Token
     */
    private void scanToken() {
        // 读取当前字符并前进
        char c = advance();
        // 根据字符类型进行处理
        switch (c) {
            // 单字符直接判断
            case '(':
                addToken(LEFT_PAREN);
                break;
            case ')':
                addToken(RIGHT_PAREN);
                break;
            case '{':
                addToken(LEFT_BRACE);
                break;
            case '}':
                addToken(RIGHT_BRACE);
                break;
            case ',':
                addToken(COMMA);
                break;
            case '.':
                addToken(DOT);
                break;
            case '-':
                addToken(MINUS);
                break;
            case '+':
                addToken(PLUS);
                break;
            case ';':
                addToken(SEMICOLON);
                break;
            case '*':
                addToken(STAR);
                break;
            case '?':
                addToken(QUESTION);
                break;
            case ':':
                addToken(COLON);
                break;

            // 一个或两个字符判断
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '&':
                if (match('&')) {
                    addToken(AND);
                } else {
                    Lox.error(line, "Unexpected character: '&'");
                }
                break;
            case '|':
                if (match('|')) {
                    addToken(OR);
                } else {
                    Lox.error(line, "Unexpected character: '|'");
                }
                break;

            case '/':
                if (peek() == '/' || peek() == '*') {
                    // 注释符号
                    comments();
                } else {
                    addToken(SLASH);
                }
                break;

            // 忽略空白符
            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                line++;
                break;

            // 字符串字面量
            case '"':
            case '`':
                // case '\'':
                string(c);
                break;

            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    Lox.error(line, "Unexpected character." + c);
                }
                break;
        }
    }

    /**
     * 前进到下一个字符
     *
     * @return 字符
     */
    private char advance() {
        return source.charAt(current++);
    }

    /**
     * 查看下一个字符（不消耗字符）
     *
     * @return 字符
     */
    private char peek() {
        if (current >= source.length()) return '\0';
        return source.charAt(current);
    }

    /**
     * 查看下个字符（不消耗字符）
     *
     * @return 字符
     */
    private char peek(int offset) {
        int pos = offset - 1;
        if (current + pos >= source.length()) return '\0';
        return source.charAt(current + pos);
    }

    /**
     * 添加Token（无字面量值）
     *
     * @param type Token类型
     */
    private void addToken(TokenType type) {
        addToken(type, null);
    }

    /**
     * 添加Token（带字面量值）
     *
     * @param type    Token类型
     * @param literal 字面量值
     */
    private void addToken(TokenType type, Object literal) {
        // 提取当前Token的文本
        String text = source.substring(start, current);
        // 创建并添加Token到列表
        tokens.add(new Token(type, text, literal, line));
    }

    /**
     * 检查是否到达文件末尾
     *
     * @return 是否到达文件末尾
     */
    private boolean isAtEnd() {
        return current >= source.length();
    }

    /**
     * 判断下一个字符是不是预期值
     *
     * @param c 需要匹配的字符
     * @return 是否是指定字符
     */
    private boolean match(char c) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != c) return false;
        current++;
        return true;
    }

    /**
     * 解析字符串
     *
     * @param quotationMark 引号
     */
    private void string(char quotationMark) {
        StringBuilder str = new StringBuilder();
        while (peek() != quotationMark && !isAtEnd()) {
            if (peek() == '\n')
                line++;

            // 处理反斜杠转义序列
            if (peek() == '\\') {
                advance(); // 跳过反斜杠
                if (isAtEnd())
                    break;

                char escapedChar = peek();
                switch (escapedChar) {
                    case 'n':
                        str.append('\n');
                        break;
                    case 't':
                        str.append('\t');
                        break;
                    case '"':
                        str.append('"');
                        break;
                    case '\\':
                        str.append('\\');
                        break;
                    default:
                        // 对于未知的转义序列，直接添加原字符
                        str.append(escapedChar);
                }
                advance(); // 跳过被转义的字符
            } else {
                str.append(peek());
                advance();
            }
        }

        if (isAtEnd()) {
            Lox.error(line, "Unterminated string");
        }

        // 读取"关闭字符串
        advance();

        addToken(STRING, str.toString());
    }

    /**
     * 判断字符是否是数字
     *
     * @param c 字符
     * @return 是否是数字
     */
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    /**
     * 判断是否是合法字母
     *
     * @param c 字符
     * @return 判断结果
     */
    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }

    /**
     * 判断是否是字母和数字
     *
     * @param c 字符
     * @return 判断结果
     */
    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    /**
     * 解析数字字面量
     */
    private void number() {
        // 扫描整数部分
        while (isDigit(peek()))
            advance();

        // 检查是否有小数部分
        if (peek() == '.' && isDigit(peek(2))) {
            // 跳过小数点
            advance();

            // 扫描小数部分
            while (isDigit(peek()))
                advance();
        }

        // 创建数字Token，字面量为Double类型
        double value = Double.parseDouble(source.substring(start, current));
        addToken(NUMBER, value);
    }

    /**
     * 解析标识符
     */
    private void identifier() {
        while (isAlphaNumeric(peek()))
            advance();

        // 标识符中挑出关键字
        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        if (type == null)
            type = IDENTIFIER;
        addToken(type);
    }

    /**
     * 解析注释
     */
    private void comments() {
        if (match('/')) {
            // 跳过注释内容
            while (peek() != '\n' && !isAtEnd())
                advance();
        } else if (match('*')) {
            while (!isAtEnd()) {
                char currentChar = advance();
                if (currentChar == '*' && peek() == '/') {
                    advance();
                    break;
                }
            }
        }

        // System.out.println(source.substring(start, current));
    }
}
