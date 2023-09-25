package com.tlcsdm.smc.tool;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import com.tlcsdm.core.util.JacksonUtil;
import com.tlcsdm.smc.tools.girret.Change;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * girret json解析测试.
 * 
 * @author unknowIfGuestInDream
 */
@Disabled
class GirretParserTest {

    @Test
    void change() {
        String changeJson = FileUtil.readUtf8String(FileUtil.file(ResourceUtil.getResource("girret/changes.json")));
        List<Change> changeList = JacksonUtil.json2List(changeJson, Change.class);
        System.out.println(changeList);
    }

}
