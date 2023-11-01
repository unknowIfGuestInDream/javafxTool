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

package com.tlcsdm.smc.tool;

import cn.hutool.core.io.resource.ResourceUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.tlcsdm.core.util.JacksonUtil;
import com.tlcsdm.core.wrap.hutool.FileUtil;
import com.tlcsdm.smc.tools.girret.Change;
import com.tlcsdm.smc.tools.girret.Comment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import java.util.List;
import java.util.Map;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GirretParserTest {

    @Test
    @Order(1)
    void change() {
        String changeJson = FileUtil
            .readUtf8String(FileUtil.file(ResourceUtil.getResource("girret").getPath(), "changes.json"));
        List<Change> changeList = JacksonUtil.json2List(changeJson, Change.class);
        if (changeList != null) {
            Assertions.assertEquals(25, changeList.size());
        }
    }

    @Test
    @Order(2)
    void comment() {
        String commentJson = FileUtil
            .readUtf8String(FileUtil.file(ResourceUtil.getResource("girret").getPath(), "comments.json"));
        Map<String, List<Comment>> map = JacksonUtil.json2MapValueList(commentJson, String.class, Comment.class);
        if (map != null) {
            Assertions.assertEquals(2, map.size());
        }
    }

    @Test
    @Order(3)
    @DisabledIfSystemProperty(named = "workEnv", matches = "ci")
    void writePretty() throws JsonProcessingException {
        String changeJson = FileUtil
            .readUtf8String(FileUtil.file(ResourceUtil.getResource("girret").getPath(), "changes.json"));
        List<Change> changeList = JacksonUtil.json2List(changeJson, Change.class);
        System.out.println(JacksonUtil.getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(changeList));
    }

}
