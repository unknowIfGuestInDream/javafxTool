module com.tlcsdm.core {
    requires javafx.graphics;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires org.apache.commons.lang3;
    requires javafx.controls;
    requires org.apache.commons.configuration2;
    requires org.controlsfx.controls;
    requires cn.hutool.log;
    requires cn.hutool.core;
    requires static io.github.javadiffutils;
    requires static freemarker;
    requires static com.fasterxml.jackson.core;
    requires static com.fasterxml.jackson.databind;
    requires static com.fasterxml.jackson.annotation;
    requires static org.dom4j;
    requires static commons.jexl3;
    requires static org.apache.groovy;
    // JSR223Test测试类时使用
    requires static java.scripting;
    requires ch.qos.logback.classic;
    requires ch.qos.logback.core;
    requires org.reflections;
    requires static org.apache.fontbox;
    requires static org.apache.pdfbox;
    requires org.apache.commons.io;
    requires org.fxmisc.richtext;
    requires org.fxmisc.flowless;
    requires reactfx;

    opens com.tlcsdm.core.javafx.controller to javafx.fxml;
    opens com.tlcsdm.core.javafx.view to javafx.fxml;
    opens com.tlcsdm.core.util to org.junit.platform.engine;

    exports com.tlcsdm.core.javafx;
    exports com.tlcsdm.core.javafx.control;
    exports com.tlcsdm.core.javafx.controlsfx;
    exports com.tlcsdm.core.javafx.dialog;
    exports com.tlcsdm.core.javafx.helper;
    exports com.tlcsdm.core.javafx.util;
    exports com.tlcsdm.core.javafx.bind;
    exports com.tlcsdm.core.javafx.richtext;
    exports com.tlcsdm.core.javafx.richtext.hyperlink;
    exports com.tlcsdm.core.util;
    exports com.tlcsdm.core.exception;
    exports com.tlcsdm.core.annotation;
    exports com.tlcsdm.core.factory;
    exports com.tlcsdm.core.factory.config;
    exports com.tlcsdm.core.freemarker;
    exports com.tlcsdm.core.freemarker.template;
    exports com.tlcsdm.core.logging.logback;

    uses com.tlcsdm.core.freemarker.TemplateLoaderService;
    uses com.tlcsdm.core.freemarker.GroovyLoaderService;

}
