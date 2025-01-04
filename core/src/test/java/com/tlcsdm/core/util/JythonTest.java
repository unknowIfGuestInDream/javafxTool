/*
 * Copyright (c) 2023 unknowIfGuestInDream.
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

package com.tlcsdm.core.util;

import cn.hutool.core.io.resource.ResourceUtil;
import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

import java.io.InputStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Jython.
 */
class JythonTest {

    @Test
    void hello() {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.exec("print('hello')");
        interpreter.exec("""
            def add(a, b):
                s = a + b
                return s

            print(add(5, 6))
            """);
        interpreter.exec("""
            import datetime

            def last_day_of_month(date):
                if date.month == 12:
                    return date.replace(day=31)
                return date.replace(month=date.month + 1, day=1) - datetime.timedelta(days=1)

            print(last_day_of_month(datetime.date(2022, 12, 25)))""");
    }

    @Test
    void file() {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.execfile(ResourceUtil.getResourceObj("jython/test.py").getStream());
    }

    @Test
    void simpleEmbedded() {
        PythonInterpreter interp = new PythonInterpreter();
        interp.exec("import sys");
        interp.exec("print sys");

        interp.set("a", new PyInteger(42));
        interp.exec("print a");
        interp.exec("x = 2+2");
        PyObject x = interp.get("x");
        Assertions.assertEquals("4", x.toString());
    }

    @Test
    void path() {
        PythonInterpreter interpreter = new PythonInterpreter();
        InputStream inputStream = ResourceUtil.getResourceObj("jython/test1.py").getStream();
        interpreter.execfile(inputStream);
        PyFunction func = interpreter.get("testStrAdd", PyFunction.class);
        //调用
        PyObject pyObject = func.__call__(new PyString("test1"));
        String result = (String) pyObject.__tojava__(String.class);
        System.out.println(result);
        interpreter.close();
    }

    @Test
    void cpplint() {
        String cppPath = ResourceUtil.getResource("cpp/Decorator.cpp").getPath();
        String lintPath = ResourceUtil.getResource("jython/cpplint.py").getPath();
    }
}
