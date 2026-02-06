package com.cheng.lox;

import java.util.List;

import static com.cheng.lox.TokenType.*;

/**
 * 解析器类
 * 用于将 Token 序列转换为抽象语法树 (AST)
 * 实现了递归下降解析器
 */
class Parser {
    private final List<Token> tokens;
    private int current = 0;
    /**
     * 构造方法
     *
     * @param tokens 词法分析器生成的 Token 序列
     */
    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    Expr parse() {
        try {
            return expression();
        } catch (ParseError error) {
            return null;
        }
    }

    /**
     * 表达式解析的入口点
     *
     * @return 解析后的表达式抽象语法树
     */
    private Expr expression() {
        return equality();
    }

    /**
     * 解析相等性表达式
     * 语法规则：equality → comparison ( ( "!=" | "==" ) comparison )* ;
     *
     * @return 相等性表达式的抽象语法树
     */
    private Expr equality() {
        Expr expr = comparison();

        while (match(BANG_EQUAL, EQUAL_EQUAL)) {
            Token operator = previous();
            Expr right = comparison();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    /**
     * 解析比较表达式
     * 语法规则：comparison → term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
     *
     * @return 比较表达式的抽象语法树
     */
    private Expr comparison() {
        Expr expr = term();

        while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
            Token operator = previous();
            Expr right = term();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    /**
     * 解析项表达式（加减法）
     * 语法规则：term → factor ( ( "+" | "-" ) factor )* ;
     *
     * @return 项表达式的抽象语法树
     */
    private Expr term() {
        Expr expr = factor();

        while (match(MINUS, PLUS)) {
            Token operator = previous();
            Expr right = factor();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    /**
     * 解析因子表达式（乘除法）
     * 语法规则：factor → unary ( ( "/" | "*" ) unary )* ;
     *
     * @return 因子表达式的抽象语法树
     */
    private Expr factor() {
        Expr expr = unary();

        while (match(SLASH, STAR)) {
            Token operator = previous();
            Expr right = unary();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    /**
     * 解析一元表达式
     * 语法规则：unary → ( "!" | "-" ) unary | primary ;
     *
     * @return 一元表达式的抽象语法树
     */
    private Expr unary() {
        if (match(BANG, MINUS)) {
            Token operator = previous();
            Expr right = unary();
            return new Expr.Unary(operator, right);
        }

        return primary();
    }

    /**
     * 解析基本表达式
     * 语法规则：primary → NUMBER | STRING | "true" | "false" | "null" | "(" expression ")" ;
     *
     * @return 基本表达式的抽象语法树
     */
    private Expr primary() {
        if (match(FALSE)) return new Expr.Literal(false);
        if (match(TRUE)) return new Expr.Literal(true);
        if (match(NULL)) return new Expr.Literal(null);

        if (match(NUMBER, STRING)) {
            return new Expr.Literal(previous().literal);
        }

        if (match(LEFT_PAREN)) {
            Expr expr = expression();
            consume(RIGHT_PAREN, "Expect ')' after expression.");
            return new Expr.Grouping(expr);
        }

        throw error(peek(), "Expect expression.");
    }

    /**
     * 消耗指定类型的 Token
     * 如果当前 Token 类型匹配则前进，否则抛出错误
     *
     * @param type    期望的 Token 类型
     * @param message 错误信息
     * @return 消耗的 Token
     */
    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();

        throw error(peek(), message);
    }

    /**
     * 生成解析错误
     *
     * @param token   出错的 Token
     * @param message 错误信息
     * @return 解析错误对象
     */
    private ParseError error(Token token, String message) {
        Lox.error(token, message);
        return new ParseError();
    }

    /**
     * 检查当前 Token 是否匹配指定类型
     * 如果匹配则前进到下一个 Token
     *
     * @param types 要检查的 Token 类型列表
     * @return 如果匹配则返回 true，否则返回 false
     */
    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    /**
     * 检查当前 Token 是否为指定类型
     * 不消耗 Token
     *
     * @param type 要检查的 Token 类型
     * @return 如果匹配则返回 true，否则返回 false
     */
    private boolean check(TokenType type) {
        if (isAtEnd()) {
            return false;
        }
        return peek().type == type;
    }

    /**
     * 前进到下一个 Token
     *
     * @return 之前的 Token
     */
    private Token advance() {
        if (!isAtEnd()) {
            current++;
        }
        return previous();
    }

    /**
     * 检查是否到达 Token 序列末尾
     *
     * @return 如果到达末尾则返回 true，否则返回 false
     */
    private boolean isAtEnd() {
        return peek().type == EOF;
    }

    /**
     * 查看当前 Token
     * 不消耗 Token
     *
     * @return 当前 Token
     */
    private Token peek() {
        return tokens.get(current);
    }

    /**
     * 获取前一个 Token
     *
     * @return 前一个 Token
     */
    private Token previous() {
        return tokens.get(current - 1);
    }

    private static class ParseError extends RuntimeException {
    }
}
