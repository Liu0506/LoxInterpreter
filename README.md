# LoxInterpreter

一个基于 Java 实现的 Lox 编程语言解释器，遵循 Robert Nystrom 的《Crafting Interpreters》一书中的设计。

## 项目结构

```
LoxInterpreter/
├── src/com/cheng/lox/  # 源代码目录
├── demo.lox            # 示例代码
└── README.md           # 项目说明文档
```

## 核心功能

### 词法分析

- **词法分析**：将源代码转换为 Token 序列
- **字符串处理**：支持字符串字面量和反斜杠转义序列
- **数字处理**：支持整数和浮点数
- **注释处理**：支持单行注释和块注释
- **错误处理**：提供详细的错误信息和行号

## 快速开始

1. 编译源代码：

   ```bash
   javac -d out src/com/cheng/lox/*.java
   ```

2. 运行示例：
   ```bash
   java -cp out com.cheng.lox.Lox demo.lox
   ```

## 参考资料

- 《Crafting Interpreters》by Robert Nystrom
- [Crafting Interpreters 官方网站](https://craftinginterpreters.com/contents.html)
