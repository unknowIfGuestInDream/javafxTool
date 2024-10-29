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

import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeSpec;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.Modifier;
import java.io.IOException;

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

        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addMethod(main)
            .build();

        JavaFile javaFile = JavaFile.builder("com.example.helloworld", helloWorld)
            .build();

        javaFile.writeTo(System.out);
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

        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addMethod(main)
            .build();

        JavaFile javaFile = JavaFile.builder("com.example.helloworld", helloWorld)
            .build();

        javaFile.writeTo(System.out);
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

        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addMethod(main)
            .build();

        JavaFile javaFile = JavaFile.builder("com.example.helloworld", helloWorld)
            .build();

        javaFile.writeTo(System.out);
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

        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addMethod(main)
            .build();

        JavaFile javaFile = JavaFile.builder("com.example.helloworld", helloWorld)
            .build();

        javaFile.writeTo(System.out);
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

        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addMethod(main)
            .build();

        JavaFile javaFile = JavaFile.builder("com.example.helloworld", helloWorld)
            .build();

        javaFile.writeTo(System.out);
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

        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addMethod(main)
            .build();

        JavaFile javaFile = JavaFile.builder("com.example.helloworld", helloWorld)
            .build();

        javaFile.writeTo(System.out);
    }
}
