/*
 * Copyright (c) 2023 unknowIfGuestInDream
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

package com.tlcsdm.core.util.groovy;

import com.tlcsdm.core.util.GroovyUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

/**
 * @author: unknowIfGuestInDream
 * @date: 2023/4/24 22:07
 */
//@DisabledIfEnvironmentVariable(named = "WORK_ENV", matches = "github", disabledReason = "The scope of Groovy is provided")
@DisabledIfSystemProperty(named = "workEnv", matches = "github")
public class GroovyUtilTest {
    /**
     * 测试没有参数的方法调用
     */
    @Test
    @Disabled
    public void testGroovyWithoutParam() throws Exception {
        String result = (String) GroovyUtil.invokeMethod("hello2.groovy", "helloWithoutParam");
        System.out.println("testGroovy4: " + result + "\n");
    }

    /**
     * 测试携带参数的方法调用
     */
    @Test
    @Disabled
    public void testGroovyWithParam() throws Exception {
        Person person = new Person("wchi", "nanjing", 30);
        String result = (String) GroovyUtil.invokeMethod("hello2.groovy", "helloWithParam", person, "testGroovy4");
        System.out.println("testGroovy4: " + result + "\n");
    }
}
