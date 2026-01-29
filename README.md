# LoxInterpreter

一个基于 Java 实现的 Lox 编程语言解释器，遵循 Robert Nystrom 的《Crafting Interpreters》一书中的设计。

## 项目结构

```
LoxInterpreter/
├── examples/           # 示例代码目录
│   └── jlox/           # Jlox 示例
├── src/com/cheng/lox/  # 核心源代码目录
├── src/com/cheng/tool/ # 工具代码目录
├── README.md           # 项目说明文档
└── LoxInterpreter.iml  # IntelliJ IDEA 项目文件
```

## 核心功能

### 词法分析器 (Scanner)

- **词法分析**：将源代码转换为 Token 序列
- **字符串处理**：支持字符串字面量和反斜杠转义序列
- **数字处理**：支持整数和浮点数
- **注释处理**：支持单行注释和块注释
- **错误处理**：提供详细的错误信息和行号

### 抽象语法树 (AST)

- **Expr 类层次结构**：表示各种表达式类型
- **访问者模式**：用于遍历和操作 AST
- **AST 打印器**：将 AST 转换为可读的字符串表示

### AST 生成工具

- **功能**：自动生成抽象语法树 (AST) 相关的 Java 类
- **使用方法**：运行 `GenerateAst.java` 工具来生成 Expr 类及其子类
- **支持的表达式类型**：
  - Unary：一元表达式（如 -1）
  - Binary：二元表达式（如 1 + 2）
  - Grouping：分组表达式（如 (1 + 2)）
  - Literal：字面量表达式（如数字、字符串）

### 解释器核心

- **Lox 类**：解释器的主入口
- **表达式计算**：支持基本的算术和逻辑运算
- **错误处理**：提供运行时错误检测和报告

## 快速开始

### 1. 编译和运行解释器

1. 编译源代码：

   ```bash
   javac -d out src/com/cheng/lox/*.java
   ```

2. 运行示例：

   ```bash
   java -cp out com.cheng.lox.Lox examples/jlox/demo.lox
   ```

3. 交互式模式：

   ```bash
   java -cp out com.cheng.lox.Lox
   ```

### 2. 使用 AST 生成工具

1. 编译工具：

   ```bash
   javac -d out src/com/cheng/tool/GenerateAst.java
   ```

2. 运行工具生成 AST 类：

   ```bash
   java -cp out com.cheng.tool.GenerateAst
   ```

3. 这将在 `src/com/cheng/lox/` 目录下生成 `Expr.java` 文件，包含所有表达式相关的类。

## 示例代码

### demo.lox

```lox
// 基本算术运算
print 1 + 2 * 3;
print (1 + 2) * 3;
print -123;
print 45.67;

// 字符串
print "Hello, world!";

// 表达式分组
print (1 + 2) * (3 - 4);
```

## 技术说明

### 访问者模式

本项目使用访问者模式来处理不同类型的表达式，这使得代码更加模块化和可扩展。主要相关类：

- `Expr.Visitor`：访问者接口
- `AstPrinter`：实现了访问者接口，用于打印表达式

### 错误处理策略

- **词法错误**：在扫描过程中检测并报告
- **语法错误**：在解析过程中检测并报告
- **运行时错误**：在执行过程中检测并报告

## 参考资料

- 《Crafting Interpreters》by Robert Nystrom
- [Crafting Interpreters 官方网站](https://craftinginterpreters.com/contents.html)
- [Java 访问者模式](https://refactoring.guru/design-patterns/visitor)
