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
 * @author: unknowIfGuestInDream
 * @date: 2022/12/11 19:11
 */
public class FxXmlUtilTest {

    @Test
    public void setValue() {
        FxXmlUtil.set("conf", "hello");
        FxXmlUtil.set("conf.compon", "girret");
    }

    @Test
    @Disabled
    public void setMapValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", "123");
        map.put("name", "girret");
        map.put("version", "1.0");
        map.put("desc", "hi");
        FxXmlUtil.set("map", map);
    }

    @Test
    @Disabled
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
