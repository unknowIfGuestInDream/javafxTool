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

package com.tlcsdm.core.util;

import cn.hutool.json.JSONUtil;
import com.tlcsdm.core.javafx.util.ConfigureUtil;
import com.tlcsdm.core.javafx.util.FxXmlUtil;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.tree.DefaultExpressionEngine;
import org.apache.commons.configuration2.tree.DefaultExpressionEngineSymbols;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author unknowIfGuestInDream
 * @date 2022/12/11 19:11
 */
@Disabled
public class FxXmlUtilTest {

    @Test
    public void setValue() {
        FxXmlUtil.set("conf", "hello");
        FxXmlUtil.set("conf.compon", "girret");
    }

    @Test
    public void setMapValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", "123");
        map.put("name", "girret");
        map.put("version", "1.0");
        map.put("desc", "hi");
        FxXmlUtil.set("map", map);
    }

    @Test
    public void getMapValue() {
        Map<String, Object> map = JSONUtil.parseObj(FxXmlUtil.get("map", ""));
        System.out.println(map.get("name"));
    }

    @Test
    public void getValue() {
        System.out.println(FxXmlUtil.get("conf", ""));
    }

    @Test
    public void setHeader() {
        FxXmlUtil.getConfig().setHeader("hello smcss");
    }

    @Test
    public void setHeaderAndValue() {
        FxXmlUtil.getConfig().setHeader("smc desc");
        FxXmlUtil.set("smc", "smcValue");
    }

    @Test
    public void xmlConfig() throws ConfigurationException {
        Configurations configs = new Configurations();
        XMLConfiguration config = configs.xml(ConfigureUtil.getConfigureFile("smc.xml"));
        config.setListDelimiterHandler(new DefaultListDelimiterHandler(','));
//        this.addEventListener(ConfigurationEvent.ANY, this.autoSaveListener);
//        this.autoSaveListener.updateFileHandler(this.getFileHandler());
        // 使用默认的符号定义创建一个表达式引擎
        DefaultExpressionEngine engine = new DefaultExpressionEngine(DefaultExpressionEngineSymbols.DEFAULT_SYMBOLS);
        // 指定表达式引擎
        config.setExpressionEngine(engine);
        config.setProperty("token.device.validate", false);
        config.setProperty("token.device[@version]", "1.0");
        System.out.println(config.getString("token.device[@version]"));
        System.out.println(config.getBoolean("token.device.validate"));
        System.out.println(config.getInt("token.person.expire"));
        System.out.println(config.getString("token.person.expire[@description]"));
    }

}
