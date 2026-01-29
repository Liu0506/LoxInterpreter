package com.cheng.lox;

/**
 * AST（抽象语法树）打印器
 * 实现了访问者模式，用于将表达式树转换为可读的字符串表示形式
 * 输出格式为带括号的前缀表示法，便于调试和理解表达式结构
 */
class AstPrinter implements Expr.Visitor<String> {
    /**
     * 主方法（测试用）
     * 用于测试表达式打印功能
     */
    // public static void main(String[] args) {
    //     // 测试代码：创建一个表达式 (-123) * (45.67)
    //     Expr expression = new Expr.Binary(
    //             new Expr.Unary(
    //                     new Token(TokenType.MINUS, "-", null, 1),
    //                     new Expr.Literal(123)),
    //             new Token(TokenType.STAR, "*", null, 1),
    //             new Expr.Grouping(
    //                     new Expr.Literal(45.67)));

    //     System.out.println(new AstPrinter().print(expression));
    // }

    /**
     * 打印表达式
     *
     * @param expr 要打印的表达式
     * @return 表达式的字符串表示（前缀表示法）
     */
    String print(Expr expr) {
        return expr.accept(this);
    }

    /**
     * 访问一元表达式
     *
     * @param expr 一元表达式对象
     * @return 格式化后的字符串表示
     */
    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return parenthesize(expr.operator.lexeme, expr.right);
    }

    /**
     * 访问二元表达式
     *
     * @param expr 二元表达式对象
     * @return 格式化后的字符串表示
     */
    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right);
    }

    /**
     * 访问分组表达式
     *
     * @param expr 分组表达式对象
     * @return 格式化后的字符串表示
     */
    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return parenthesize("group", expr.expression);
    }

    /**
     * 访问字面量表达式
     *
     * @param expr 字面量表达式对象
     * @return 格式化后的字符串表示
     */
    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if (expr.value == null) return "null";
        return expr.value.toString();
    }

    /**
     * 将表达式和名称包装在括号中
     *
     * @param name  操作符或分组名称
     * @param exprs 要包装的表达式列表
     * @return 格式化后的字符串（前缀表示法）
     */
    private String parenthesize(String name, Expr... exprs) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        for (Expr expr : exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");

        return builder.toString();
    }
}
