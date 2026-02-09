package com.cheng.lox;

/**
 * Token类型枚举
 * 定义了Lox语言中所有可能的Token类型
 */
enum TokenType {
    // -------- 单字符Token类型 --------
    /**
     * 左括号 '('
     */
    LEFT_PAREN,
    /**
     * 右括号 ')'
     */
    RIGHT_PAREN,
    /**
     * 左花括号 '{'
     */
    LEFT_BRACE,
    /**
     * 右花括号 '}'
     */
    RIGHT_BRACE,
    /**
     * 逗号 ','
     */
    COMMA,
    /**
     * 点 '.'
     */
    DOT,
    /**
     * 减号 '-'
     */
    MINUS,
    /**
     * 加号 '+'
     */
    PLUS,
    /**
     * 分号 ';'
     */
    SEMICOLON,
    /**
     * 斜杠 '/'
     */
    SLASH,
    /**
     * 星号 '*'
     */
    STAR,
    /**
     * 问号 '?'
     */
    QUESTION,
    /**
     * 冒号 ':'
     */
    COLON,

    // -------- 一或两个字符的Token类型 --------
    /**
     * 感叹号 '!'
     */
    BANG,
    /**
     * 不等于 '!='
     */
    BANG_EQUAL,
    /**
     * 等于 '='
     */
    EQUAL,
    /**
     * 相等 '=='
     */
    EQUAL_EQUAL,
    /**
     * 大于 '>'
     */
    GREATER,
    /**
     * 大于等于 '>='
     */
    GREATER_EQUAL,
    /**
     * 小于 '<'
     */
    LESS,
    /**
     * 小于等于 '<='
     */
    LESS_EQUAL,

    // 字面量类型
    /**
     * 标识符（变量名、函数名等）
     */
    IDENTIFIER,
    /**
     * 字符串字面量
     */
    STRING,
    /**
     * 数字字面量
     */
    NUMBER,

    // 关键字类型
    /**
     * 关键字 'and'
     */
    AND,
    /**
     * 关键字 'class'
     */
    CLASS,
    /**
     * 关键字 'else'
     */
    ELSE,
    /**
     * 关键字 'false'
     */
    FALSE,
    /**
     * 关键字 'fun'
     */
    FUN,
    /**
     * 关键字 'for'
     */
    FOR,
    /**
     * 关键字 'if'
     */
    IF,
    /**
     * 关键字 'null'
     */
    NULL,
    /**
     * 关键字 'or'
     */
    OR,
    /**
     * 关键字 'print'
     */
    PRINT,
    /**
     * 关键字 'return'
     */
    RETURN,
    /**
     * 关键字 'super'
     */
    SUPER,
    /**
     * 关键字 'this'
     */
    THIS,
    /**
     * 关键字 'true'
     */
    TRUE,
    /**
     * 关键字 'var'
     */
    VAR,
    /**
     * 关键字 'while'
     */
    WHILE,

    /**
     * 文件结束符
     */
    EOF,
}