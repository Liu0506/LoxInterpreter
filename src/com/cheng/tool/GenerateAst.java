package com.cheng.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/**
 * AST（抽象语法树）代码生成器
 * 用于根据类型定义自动生成对应的 Java 代码
 */
public class GenerateAst {
    /** 缩进空格 */
    static String tabSpace = "    ";

    /**
     * 主方法
     * 用于生成表达式的 AST 类
     */
    public static void main(String[] args) throws IOException {
        defineAst("./src/com/cheng/lox", "Expr", Arrays.asList(
                "Unary    : Token operator, Expr right",
                "Binary   : Expr left, Token operator, Expr right",
                "Grouping : Expr expression",
                "Literal  : Object value"
        ));
    }

    /**
     * 定义 AST 基类和所有子类型
     * @param outputDir 输出目录
     * @param baseName 基类名称
     * @param types 类型定义列表
     * @throws IOException 当文件写入失败时
     */
    private static void defineAst(String outputDir, String baseName, List<String> types) throws IOException {
        String path = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        writer.println("package com.cheng.lox;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + baseName + " {");

        // 定义访问者模式的访问者接口
        defineVisitor(writer, baseName, types);
        writer.println();

        // 基类的 accept 方法
        writer.println(tabSpace + "abstract <R> R accept(Visitor<R> visitor);");
        writer.println();

        // 定义 AST 类
        for (String type : types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            defineType(writer, baseName, className, fields);
        }

        writer.println("}");
        writer.close();
    }

    /**
     * 定义访问者接口
     * @param writer 输出写入器
     * @param baseName 基类名称
     * @param types 类型定义列表
     */
    private static void defineVisitor(PrintWriter writer, String baseName, List<String> types) {
        writer.println(tabSpace + "interface Visitor<R> {");

        for (String type : types) {
            String typeName = type.split(":")[0].trim();
            writer.println(tabSpace + tabSpace + "R visit" + typeName + baseName + "(" +
                    typeName + " " + baseName.toLowerCase() + ");");
        }

        writer.println(tabSpace + "}");
    }

    /**
     * 定义具体的 AST 类型
     * @param writer 输出写入器
     * @param baseName 基类名称
     * @param className 类名
     * @param fieldList 字段列表
     */
    private static void defineType(PrintWriter writer, String baseName, String className, String fieldList) {
        String[] fields = fieldList.split(", ");

        writer.println(tabSpace + "static class " + className + " extends " + baseName + " {");

        // 字段
        for (String field : fields) {
            writer.println(tabSpace + tabSpace + "final " + field + ";");
        }

        // 构造方法
        writer.println();
        writer.println(tabSpace + tabSpace + className + "(" + fieldList + ") {");

        for (String field : fields) {
            String name = field.split(" ")[1];
            writer.println(tabSpace + tabSpace + tabSpace + "this." + name + " = " + name + ";");
        }

        writer.println(tabSpace + tabSpace + "}");

        // 访问者模式实现
        writer.println();
        writer.println(tabSpace + tabSpace + "@Override");
        writer.println(tabSpace + tabSpace + "<R> R accept(Visitor<R> visitor) {");
        writer.println(tabSpace + tabSpace + tabSpace + "return visitor.visit" +
                className + baseName + "(this);");
        writer.println(tabSpace + tabSpace + "}");

        writer.println(tabSpace + "}");
        writer.println();
    }
}
