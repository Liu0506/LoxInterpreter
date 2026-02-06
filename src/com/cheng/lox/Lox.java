package com.cheng.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Lox 解释器主类
 * 负责处理命令行参数、文件读取、交互式命令行和错误处理
 */
public class Lox {
    /**
     * 错误标志，用于跟踪是否发生了解析错误
     */
    static boolean hadError = false;

    /**
     * 主方法
     * 处理命令行参数，决定运行模式
     * 
     * @param args 命令行参数
     * @throws IOException 输入输出异常
     */
    public static void main(String[] args) throws IOException {
        System.out.println(Arrays.toString(args));
        if (args.length > 1) {
            System.out.println("Usage: jlox [scirpt]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    /**
     * 读取文件
     *
     * @param path 代码文件地址
     * @throws IOException
     */
    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));

        if (hadError) System.exit(65);
    }

    /**
     * 运行解析方法
     * 负责解析源代码，生成Token并执行
     *
     * @param source 源代码
     */
    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        Parser parser = new Parser(tokens);
        Expr expression = parser.parse();

        if (hadError) return;

        System.out.println(new AstPrinter().print(expression));
    }

    /**
     * 运行交互式命令行
     * 允许用户逐行输入代码并执行
     * 
     * @throws IOException 输入输出异常
     */
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        while (true) {
            System.out.println("> ");
            String line = reader.readLine();
            if (line == null) break;
            run(line);
            hadError = false;
        }
    }

    /**
     * 报告错误
     * 
     * @param line 错误所在行
     * @param message 错误信息
     */
    static void error(int line, String message) {
        report(line, "", message);
    }

    /**
     * 报告错误
     *
     * @param token 错误Token
     * @param message 错误信息
     */
    static void error(Token token, String message) {
        if (token.type == TokenType.EOF) {
            report(token.line, " at end", message);
        } else {
            report(token.line, " at '" + token.lexeme + "'", message);
        }
    }

    /**
     * 生成错误报告
     * 
     * @param line 错误所在行
     * @param where 错误位置描述
     * @param message 错误信息
     */
    private static void report(int line, String where, String message) {
        System.out.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }
}
