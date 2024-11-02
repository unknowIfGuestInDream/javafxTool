/*
 * Copyright (c) 2024 unknowIfGuestInDream.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of unknowIfGuestInDream, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL UNKNOWIFGUESTINDREAM BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.tlcsdm.core.javapoet;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeName;
import com.palantir.javapoet.TypeSpec;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;

/**
 * <a href="https://github.com/palantir/javapoet">官网实例</a>
 *
 * @author unknowIfGuestInDream
 */
public class JavapoetTest {

    /**
     * 为了声明 main 方法，我们创建了一个 MethodSpec “main”，它配置了修饰符、返回类型、
     * 参数和代码语句。我们将 main 方法添加到 HelloWorld 类中，然后将其添加到 HelloWorld.java 文件中。
     * <p>
     * 在本例中，我们将文件写入 System.out，但我们也可以将其作为字符串 （JavaFile.toString（）） 或
     * 将其写入文件系统 （JavaFile.writeTo（））。
     */
    @Test
    void testMain() throws IOException {
        MethodSpec main = MethodSpec.methodBuilder("main")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(void.class)
            .addParameter(String[].class, "args")
            .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
            .build();

        print(main);
    }

    /**
     * 不对方法和构造函数的主体进行建模。没有表达式类，没有语句类或语法树节点。
     * 相反，JavaPoet 使用字符串作为代码块：
     *
     * @throws IOException
     */
    @Test
    void codeFlow1() throws IOException {
        MethodSpec main = MethodSpec.methodBuilder("main")
            .addCode(""
                + "int total = 0;\n"
                + "for (int i = 0; i < 10; i++) {\n"
                + "  total += i;\n"
                + "}\n")
            .build();

        print(main);
    }

    /**
     * 手动分号、换行和缩进很繁琐，因此 JavaPoet 提供了 API 来简化它。
     * 有 addStatement（） 来处理分号和换行符，
     * 和 beginControlFlow（） + endControlFlow（） 一起用于大括号、换行符和缩进：
     */
    @Test
    void codeFlow2() throws IOException {
        MethodSpec main = MethodSpec.methodBuilder("main")
            .addStatement("int total = 0")
            .beginControlFlow("for (int i = 0; i < 10; i++)")
            .addStatement("total += i")
            .endControlFlow()
            .build();

        print(main);
    }

    /**
     * 假设我们不是简单地将 0 添加到 10，而是希望使操作和范围可配置。下面是一个生成方法的方法：
     *
     * @throws IOException
     */
    @Test
    void codeFlow3() throws IOException {
        MethodSpec main = MethodSpec.methodBuilder("multiply10to20")
            .returns(int.class)
            .addStatement("int result = 1")
            .beginControlFlow("for (int i = " + "10" + "; i < " + "20" + "; i++)")
            .addStatement("result = result " + "*" + " i")
            .endControlFlow()
            .addStatement("return result")
            .build();

        print(main);
    }

    /**
     * 某些控制流语句（例如 if/else）可以具有无限的控制流可能性。
     * 您可以使用 nextControlFlow（） 处理这些选项：
     *
     * @throws IOException
     */
    @Test
    void codeFlow4() throws IOException {
        MethodSpec main = MethodSpec.methodBuilder("main")
            .addStatement("long now = $T.currentTimeMillis()", System.class)
            .beginControlFlow("if ($T.currentTimeMillis() < now)", System.class)
            .addStatement("$T.out.println($S)", System.class, "Time travelling, woo hoo!")
            .nextControlFlow("else if ($T.currentTimeMillis() == now)", System.class)
            .addStatement("$T.out.println($S)", System.class, "Time stood still!")
            .nextControlFlow("else")
            .addStatement("$T.out.println($S)", System.class, "Ok, time still moving forward")
            .endControlFlow()
            .build();

        print(main);
    }

    /**
     * 使用 try/catch 捕获异常也是 nextControlFlow（） 的一个用例：
     */
    @Test
    void codeFlow5() throws IOException {
        MethodSpec main = MethodSpec.methodBuilder("main")
            .beginControlFlow("try")
            .addStatement("throw new Exception($S)", "Failed")
            .nextControlFlow("catch ($T e)", Exception.class)
            .addStatement("throw new $T(e)", RuntimeException.class)
            .endControlFlow()
            .build();

        print(main);
    }

    /**
     * 对 beginControlFlow（） 和 addStatement 的调用中的字符串连接会分散注意力。
     * 运算符太多。为了解决这个问题，JavaPoet 提供了一种受 String.format（） 启发但不兼容的语法。
     * 它接受 $L 在输出中发出 Literal 值。这就像 Formatter 的 %s 一样：
     * <p>
     * 文本直接发出到输出代码，无需转义。文本的参数可以是字符串，
     * 原语和一些 JavaPoet 类型。
     *
     * @throws IOException
     */
    @Test
    void literals() throws IOException {
        MethodSpec main = MethodSpec.methodBuilder("main")
            .returns(int.class)
            .addStatement("int result = 0")
            .beginControlFlow("for (int i = $L; i < $L; i++)", 1, 10)
            .addStatement("result = result $L i", "*")
            .endControlFlow()
            .addStatement("return result")
            .build();
        print(main);
    }

    /**
     * 当发出包含字符串文字的代码时，我们可以使用 $S 来发出一个字符串，并带有换行引号
     * 标记和转义。这是一个发出 3 个方法的程序，每个方法都返回自己的名称：
     *
     * @throws IOException
     */
    @Test
    void strings1() throws IOException {
        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addMethod(whatsMyName("slimShady"))
            .addMethod(whatsMyName("eminem"))
            .addMethod(whatsMyName("marshallMathers"))
            .build();

        JavaFile javaFile = JavaFile.builder("com.example.helloworld", helloWorld)
            .build();

        javaFile.writeTo(System.out);
    }

    private MethodSpec whatsMyName(String name) {
        return MethodSpec.methodBuilder(name)
            .returns(String.class)
            .addStatement("return $S", name)
            .build();
    }

    /**
     * 我们 Java 程序员喜欢我们的类型：它们使我们的代码更容易理解。JavaPoet 也加入了进来。
     * 它内置了丰富的类型支持，包括自动生成 import 语句。只需使用 $T 来引用类型：
     *
     * @throws IOException
     */
    @Test
    void types() throws IOException {
        MethodSpec today = MethodSpec.methodBuilder("today")
            .returns(Date.class)
            .addStatement("return new $T()", Date.class)
            .build();

        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addMethod(today)
            .build();

        JavaFile javaFile = JavaFile.builder("com.example.helloworld", helloWorld)
            .build();

        javaFile.writeTo(System.out);
    }

    /**
     * 我们传递了 Date.class 来引用一个类，该类恰好在我们生成代码时可用。
     * 情况并非如此。下面是一个类似的示例，但此示例引用了一个尚不存在的类：
     *
     * @throws IOException
     */
    @Test
    void types1() throws IOException {
        ClassName hoverboard = ClassName.get("com.mattel", "Hoverboard");

        MethodSpec today = MethodSpec.methodBuilder("tomorrow")
            .returns(hoverboard)
            .addStatement("return new $T()", hoverboard)
            .build();

        print(today);
    }

    /**
     * ClassName 类型非常重要，在使用 JavaPoet 时，您将经常需要它。
     * 它可以识别任何声明的类。声明类型只是 Java 丰富类型系统的开始：
     * 我们还有数组、参数化类型、通配符类型和类型变量。JavaPoet 具有用于构建以下每个类的类：
     *
     * @throws IOException
     */
    @Test
    void types2() throws IOException {
        ClassName hoverboard = ClassName.get("com.mattel", "Hoverboard");
        ClassName list = ClassName.get("java.util", "List");
        ClassName arrayList = ClassName.get("java.util", "ArrayList");
        TypeName listOfHoverboards = ParameterizedTypeName.get(list, hoverboard);

        MethodSpec beyond = MethodSpec.methodBuilder("beyond")
            .returns(listOfHoverboards)
            .addStatement("$T result = new $T<>()", listOfHoverboards, arrayList)
            .addStatement("result.add(new $T())", hoverboard)
            .addStatement("result.add(new $T())", hoverboard)
            .addStatement("result.add(new $T())", hoverboard)
            .addStatement("return result")
            .build();

        print(beyond);
    }

    /**
     * JavaPoet 支持 import static。它通过显式收集类型成员名称来实现这一点。
     * 让我们用一些静态糖来增强前面的例子：
     *
     * @throws IOException
     */
    @Test
    void types3() throws IOException {
        ClassName namedBoards = ClassName.get("com.mattel", "Hoverboard", "Boards");
        ClassName hoverboard = ClassName.get("com.mattel", "Hoverboard");

        MethodSpec main = MethodSpec.methodBuilder("tomorrow")
            .returns(hoverboard)
            .addStatement("return new $T()", hoverboard)
            .build();

        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addMethod(main)
            .build();

        JavaFile javaFile = JavaFile.builder("com.example.helloworld", helloWorld)
            .addStaticImport(hoverboard, "createNimbus")
            .addStaticImport(namedBoards, "*")
            .addStaticImport(Collections.class, "*")
            .build();
        javaFile.writeTo(System.out);
    }

    /**
     * 生成的代码通常是自引用的。使用 $N 按名称引用另一个生成的声明。
     * 这是一个调用另一个方法的方法：
     *
     * @throws IOException
     */
    @Test
    void names() throws IOException {
        MethodSpec hexDigit = MethodSpec.methodBuilder("hexDigit")
            .addParameter(int.class, "i")
            .returns(char.class)
            .addStatement("return (char) (i < 10 ? i + '0' : i - 10 + 'a')")
            .build();

        MethodSpec byteToHex = MethodSpec.methodBuilder("byteToHex")
            .addParameter(int.class, "b")
            .returns(String.class)
            .addStatement("char[] result = new char[2]")
            .addStatement("result[0] = $N((b >>> 4) & 0xf)", hexDigit)
            .addStatement("result[1] = $N(b & 0xf)", hexDigit)
            .addStatement("return new String(result)")
            .build();

        print(byteToHex);
    }

    private void print(MethodSpec main) throws IOException {
        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addMethod(main)
            .build();

        JavaFile javaFile = JavaFile.builder("com.example.helloworld", helloWorld)
            .build();

        javaFile.writeTo(System.out);
    }
}
