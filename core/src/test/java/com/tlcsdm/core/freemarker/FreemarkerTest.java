/*
 * Copyright (c) 2019, 2023 unknowIfGuestInDream
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

package com.tlcsdm.core.freemarker;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.resource.ResourceUtil;
import com.google.common.collect.ImmutableMap;
import com.tlcsdm.core.freemarker.directive.HexDirective;
import com.tlcsdm.core.freemarker.directive.LowerDirective;
import com.tlcsdm.core.freemarker.directive.RegorDirective;
import com.tlcsdm.core.freemarker.directive.RepeatDirective;
import com.tlcsdm.core.freemarker.directive.StyleDirective;
import com.tlcsdm.core.freemarker.directive.UpperDirective;
import com.tlcsdm.core.freemarker.format.AppMetaTemplateDateFormatFactory;
import com.tlcsdm.core.freemarker.format.BaseNTemplateNumberFormatFactory;
import com.tlcsdm.core.freemarker.format.EpochMillisDivTemplateDateFormatFactory;
import com.tlcsdm.core.freemarker.format.EpochMillisTemplateDateFormatFactory;
import com.tlcsdm.core.freemarker.format.HTMLISOTemplateDateFormatFactory;
import com.tlcsdm.core.freemarker.format.HexTemplateNumberFormatFactory;
import com.tlcsdm.core.freemarker.format.LocAndTZSensitiveTemplateDateFormatFactory;
import com.tlcsdm.core.freemarker.format.LocaleSensitiveTemplateNumberFormatFactory;
import com.tlcsdm.core.freemarker.format.PrintfGTemplateNumberFormatFactory;
import com.tlcsdm.core.freemarker.format.UnitAwareTemplateNumberFormatFactory;
import com.tlcsdm.core.freemarker.format.UnitAwareTemplateNumberModel;
import com.tlcsdm.core.freemarker.method.IndexOfMethod;
import com.tlcsdm.core.util.CoreConstant;
import freemarker.cache.ConditionalTemplateConfigurationFactory;
import freemarker.cache.FileNameGlobMatcher;
import freemarker.cache.MergingTemplateConfigurationFactory;
import freemarker.cache.StringTemplateLoader;
import freemarker.core.AliasTemplateDateFormatFactory;
import freemarker.core.TemplateConfiguration;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.SimpleDate;
import freemarker.template.Template;
import freemarker.template.TemplateDateModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateHashModel;
import freemarker.template.utility.StringUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FreemarkerTest {
    private static Configuration configuration;

    @BeforeAll
    public static void init() {
        configuration = new Configuration(Configuration.VERSION_2_3_32);
        try {
            configuration.setDirectoryForTemplateLoading(
                new File(ResourceUtil.getResource("freemarker/templates").getPath()));
            configuration.setDefaultEncoding(CoreConstant.ENCODING_UTF_8);
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            configuration.setSetting(Configuration.CACHE_STORAGE_KEY, "strong:20,soft:250");
        } catch (IORuntimeException | IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    private String normalizeNewLines(String s) {
        return StringUtil.replace(s, "\r\n", "\n").replace('\r', '\n');
    }

    private Map<String, Object> createCommonTestValuesDataModel() {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("map", Collections.singletonMap("key", "value"));
        dataModel.put("list", Collections.singletonList("item"));
        dataModel.put("s", "text");
        dataModel.put("n", 1);
        dataModel.put("b", true);
        dataModel.put("bean", new TestBean());
        return dataModel;
    }

    /**
     * include, assign, list
     */
    @Test
    public void test1() throws IOException, TemplateException {
        Template template = configuration.getTemplate("test1.ftl");
        Map<String, Object> map = new HashMap<>();
        map.put("message", "freemarker");
        StringWriter stringWriter = new StringWriter();
        template.process(map, stringWriter);
        System.out.println(stringWriter);
    }

    /**
     * if, switch, noparse, compress
     */
    @Test
    public void test2() throws IOException, TemplateException {
        Template template = configuration.getTemplate("test2.ftl");
        Map<String, Object> map = new HashMap<>();
        StringWriter stringWriter = new StringWriter();
        template.process(map, stringWriter);
        System.out.println(stringWriter);
    }

    /**
     * 自定义指令通过设置共享变量来生效
     */
    @Test
    public void upper1() throws IOException, TemplateException {
        configuration.setSharedVariable("upper", new UpperDirective());
        configuration.setAutoIncludes(Collections.singletonList("head.ftl"));
        Template template = configuration.getTemplate("upper1.ftl");
        StringWriter stringWriter = new StringWriter();
        template.process(new HashMap<>(), stringWriter);
        System.out.println(stringWriter);
    }

    @Test
    public void autoIncludes() throws IOException, TemplateException {
        configuration.setSharedVariable("upper", new UpperDirective());
        configuration.setAutoIncludes(Collections.singletonList("head.ftl"));
        Template template = configuration.getTemplate("upper1.ftl");
        StringWriter stringWriter = new StringWriter();
        template.process(new HashMap<>(), stringWriter);
        System.out.println(stringWriter);
    }

    /**
     * 自定义指令通过内建函数new来生效
     */
    @Test
    public void upper2() throws IOException, TemplateException {
        Template template = configuration.getTemplate("upper2.ftl");
        StringWriter stringWriter = new StringWriter();
        template.process(new HashMap(), stringWriter);
        System.out.println(stringWriter);
    }

    /**
     * 自定义指令通过放到数据模型来生效
     */
    @Test
    public void upper3() throws IOException, TemplateException {
        Template template = configuration.getTemplate("upper1.ftl");
        Map<String, Object> map = new HashMap<>();
        map.put("upper", new UpperDirective());
        StringWriter stringWriter = new StringWriter();
        template.process(map, stringWriter);
        System.out.println(stringWriter);
    }

    /**
     * 自定义指令 lower
     */
    @Test
    public void lower() throws IOException, TemplateException {
        Template template = configuration.getTemplate("lower.ftl");
        Map<String, Object> map = new HashMap<>();
        map.put("lower", new LowerDirective());
        StringWriter stringWriter = new StringWriter();
        template.process(map, stringWriter);
        System.out.println(stringWriter);
    }

    /**
     * 自定义指令 repeat
     */
    @Test
    public void repeat() throws IOException, TemplateException {
        Template template = configuration.getTemplate("repeat.ftl");
        Map<String, Object> map = new HashMap<>();
        map.put("repeat", new RepeatDirective());
        StringWriter stringWriter = new StringWriter();
        template.process(map, stringWriter);
        System.out.println(stringWriter);
    }

    /**
     * 自定义指令 hex
     */
    @Test
    public void hex() throws IOException, TemplateException {
        Template template = configuration.getTemplate("hex.ftl");
        Map<String, Object> map = new HashMap<>();
        map.put("hextest", new HexDirective());
        StringWriter stringWriter = new StringWriter();
        template.process(map, stringWriter);
        System.out.println(stringWriter);
    }

    /**
     * 自定义指令 regor
     */
    @Test
    public void regor() throws IOException, TemplateException {
        Template template = configuration.getTemplate("regor.ftl");
        Map<String, Object> map = new HashMap<>();
        map.put("regor", new RegorDirective());
        map.put("list", "dma");
        map.put("gogo", "tau");
        map.put("aaa", "dts");
        StringWriter stringWriter = new StringWriter();
        template.process(map, stringWriter);
        System.out.println(stringWriter);
    }

    /**
     * 自定义指令 style
     */
    @Test
    public void style() throws IOException, TemplateException {
        Template template = configuration.getTemplate("style.ftl");
        Map<String, Object> map = new HashMap<>();
        map.put("style", new StyleDirective());
        map.put("regor", new RegorDirective());
        map.put("list", "dmaasasqsdfqsfa".repeat(10));
        map.put("test", "dataaa".repeat(2));
        StringWriter stringWriter = new StringWriter();
        template.process(map, stringWriter);
        System.out.println(stringWriter);
    }

    @Test
    public void output() throws IOException, TemplateException {
        Template template = configuration.getTemplate("output_format.ftl");
        StringWriter stringWriter = new StringWriter();
        template.process(new HashMap<>(), stringWriter);
        System.out.println(stringWriter);
    }

    @Test
    public void format() throws IOException, TemplateException {
        Template template = configuration.getTemplate("format.ftl");
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> map = new HashMap<>();
        map.put("s", "a'b\"c\u0001");
        template.process(map, stringWriter);
        System.out.println(stringWriter);
    }

    @Test
    public void label() throws IOException, TemplateException {
        Template template = configuration.getTemplate("label.ftl");
        //configuration.setTagSyntax(Configuration.SQUARE_BRACKET_TAG_SYNTAX);
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> map = new HashMap<>();
        map.put("y", "33");
        template.process(map, stringWriter);
        System.out.println(stringWriter);
    }

    @Test
    public void methodModelEx() throws IOException, TemplateException {
        Template template = configuration.getTemplate("methodModelEx.ftl");
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> map = new HashMap<>();
        map.put("indexOf", new IndexOfMethod());
        template.process(map, stringWriter);
        System.out.println(stringWriter);
    }

    @Test
    public void templateConfig() throws IOException, TemplateException {
        StringTemplateLoader tl = new StringTemplateLoader();
        tl.putTemplate("(de).ftl", "${.locale}");
        tl.putTemplate("default.ftl", "${.locale}");
        tl.putTemplate("(de)-fr.ftl", ("<#ftl locale='fr_FR'>${.locale}"));
        tl.putTemplate("default-fr.ftl", ("<#ftl locale='fr_FR'>${.locale}"));
        configuration.setTemplateLoader(tl);

        TemplateConfiguration tcDe = new TemplateConfiguration();
        tcDe.setLocale(Locale.GERMANY);
        configuration.setTemplateConfigurations(
            new ConditionalTemplateConfigurationFactory(new FileNameGlobMatcher("*(de)*"), tcDe));

        Template t1 = configuration.getTemplate("(de).ftl");
        Assertions.assertEquals(Locale.GERMANY, t1.getLocale());
        Assertions.assertEquals("de_DE", getTemplateOutput(t1));

        Template t2 = configuration.getTemplate("(de).ftl", Locale.ITALY);
        Assertions.assertEquals(Locale.GERMANY, t2.getLocale());
        Assertions.assertEquals("de_DE", getTemplateOutput(t2));

        Template t3 = configuration.getTemplate("default.ftl");
        Assertions.assertEquals(Locale.getDefault(), t3.getLocale());
        Assertions.assertEquals(Locale.getDefault().toString(), getTemplateOutput(t3));

        Template t4 = configuration.getTemplate("default.ftl", Locale.ITALY);
        Assertions.assertEquals(Locale.ITALY, t4.getLocale());
        Assertions.assertEquals("it_IT", getTemplateOutput(t4));
    }

    @Test
    public void mergeTemplateConfig() throws IOException, TemplateException {
        configuration.setLocale(Locale.US);
        TemplateConfiguration tcFR = new TemplateConfiguration();
        tcFR.setLocale(Locale.FRANCE);
        TemplateConfiguration tcYN = new TemplateConfiguration();
        tcYN.setBooleanFormat("Y,N");
        TemplateConfiguration tc00 = new TemplateConfiguration();
        tc00.setNumberFormat("0.00");
        configuration.setTemplateConfigurations(new MergingTemplateConfigurationFactory(
            new ConditionalTemplateConfigurationFactory(new FileNameGlobMatcher("*(fr)*"), tcFR),
            new ConditionalTemplateConfigurationFactory(new FileNameGlobMatcher("*(yn)*"), tcYN),
            new ConditionalTemplateConfigurationFactory(new FileNameGlobMatcher("*(00)*"), tc00)));

        String commonFTL = "${.locale} ${true?string} ${1.2}";
        StringTemplateLoader tl = new StringTemplateLoader();
        tl.putTemplate("default", commonFTL);
        tl.putTemplate("(fr)", commonFTL);
        tl.putTemplate("(yn)(00)", commonFTL);
        tl.putTemplate("(00)(fr)", commonFTL);
        configuration.setTemplateLoader(tl);

        Assertions.assertEquals("en_US true 1.2", getTemplateOutput(configuration.getTemplate("default")));
        Assertions.assertEquals("fr_FR true 1,2", getTemplateOutput(configuration.getTemplate("(fr)")));
        Assertions.assertEquals("en_US Y 1.20", getTemplateOutput(configuration.getTemplate("(yn)(00)")));
        Assertions.assertEquals("fr_FR true 1,20", getTemplateOutput(configuration.getTemplate("(00)(fr)")));
    }

    @Test
    public void customNumberFormat() throws IOException, TemplateException {
        Template template = configuration.getTemplate("customNumberFormat.ftl");
        configuration.setCustomNumberFormats(ImmutableMap.of("hex", HexTemplateNumberFormatFactory.INSTANCE, "loc",
            LocaleSensitiveTemplateNumberFormatFactory.INSTANCE, "base", BaseNTemplateNumberFormatFactory.INSTANCE,
            "printfG", PrintfGTemplateNumberFormatFactory.INSTANCE, "ua",
            UnitAwareTemplateNumberFormatFactory.INSTANCE));
        // configuration.setNumberFormat("@hex");
        // configuration.setNumberFormat("@base 8");
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> map = new HashMap<>();
        map.put("weight", new UnitAwareTemplateNumberModel(1.5, "kg"));
        map.put("n", 1234567);
        template.process(map, stringWriter);
        System.out.println(stringWriter);
    }

    @Test
    public void customDataFormat() throws IOException, TemplateException {
        Template template = configuration.getTemplate("customDataFormat.ftl");
        configuration.setCustomDateFormats(
            ImmutableMap.of("epoch", EpochMillisTemplateDateFormatFactory.INSTANCE, "loc",
                LocAndTZSensitiveTemplateDateFormatFactory.INSTANCE, "div",
                EpochMillisDivTemplateDateFormatFactory.INSTANCE, "appMeta", AppMetaTemplateDateFormatFactory.INSTANCE,
                "htmlIso", HTMLISOTemplateDateFormatFactory.INSTANCE, "fileDate",
                new AliasTemplateDateFormatFactory("dd/MMM/yy hh:mm a")));
        // configuration.setDateTimeFormat("@epoch");
        // configuration.setDateTimeFormat("@div 1000");
        // configuration.setDateTimeFormat("'@'yyyy");
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> map = new HashMap<>();
        map.put("d", new Date(123456789));
        map.put("dt", new SimpleDate(new Date(12345678L), TemplateDateModel.DATETIME));
        template.process(map, stringWriter);
        System.out.println(stringWriter);
    }

    @Disabled
    @Test
    public void beansWrapper() throws IOException, TemplateException {
        Template template = configuration.getTemplate("beansWrapper.ftl");
        BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
        TemplateHashModel staticModels = wrapper.getStaticModels();
        TemplateHashModel fileStatics = (TemplateHashModel) staticModels.get("java.io.File");
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> map = new HashMap<>();
        map.put("File", fileStatics);
        map.put("statics", BeansWrapper.getDefaultInstance().getStaticModels());
        template.process(map, stringWriter);
        System.out.println(stringWriter);
    }

    private String getTemplateOutput(Template t) throws TemplateException, IOException {
        StringWriter sw = new StringWriter();
        t.process(null, sw);
        return sw.toString();
    }

    public static class TestBean {
        private int x;
        private boolean b;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public boolean isB() {
            return b;
        }

        public void setB(boolean b) {
            this.b = b;
        }

        public int intM() {
            return 1;
        }

        public int intMP(int x) {
            return x;
        }

        public void voidM() {

        }

    }

}
