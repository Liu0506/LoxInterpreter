package com.cheng.lox;

class Token {
    final TokenType type;
    /** 原始文本 */
    final String lexeme;
    /** 字面量 */
    final Object literal;
    final int line;

    Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    public String toString() {
        return type + " " + lexeme + " " + literal;
    }
}
