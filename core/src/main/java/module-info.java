module com.tlcsdm.core {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.swing;
    requires java.desktop;
    requires java.net.http;
    requires java.sql;
    requires org.apache.commons.lang3;
    requires javafx.controls;
    requires org.apache.commons.configuration2;
    requires org.controlsfx.controls;
    requires cn.hutool.log;
    requires cn.hutool.core;
    requires static cn.hutool.http;
    requires static cn.hutool.poi;
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
    requires static org.fxmisc.richtext;
    requires static org.fxmisc.flowless;
    requires static org.fxmisc.undo;
    requires static reactfx;
    requires static net.coobird.thumbnailator;
    requires static com.dlsc.pdfviewfx;
    requires static tabula;
    requires jsr305;
    requires static com.dlsc.preferencesfx;
    requires static yuicompressor;
    requires static jave.core;
    requires static com.sun.jna;
    requires static vosk;

    opens com.tlcsdm.core.javafx.controller to javafx.fxml;
    opens com.tlcsdm.core.javafx.view to javafx.fxml;
    opens com.tlcsdm.core.util to org.junit.platform.engine, org.junit.jupiter.api;
    opens com.tlcsdm.core.modal;

    exports com.tlcsdm.core.javafx;
    exports com.tlcsdm.core.javafx.control;
    exports com.tlcsdm.core.javafx.controlsfx;
    exports com.tlcsdm.core.javafx.dialog;
    exports com.tlcsdm.core.javafx.helper;
    exports com.tlcsdm.core.javafx.util;
    exports com.tlcsdm.core.javafx.bind;
    exports com.tlcsdm.core.javafx.stage;
    exports com.tlcsdm.core.javafx.richtext;
    exports com.tlcsdm.core.javafx.richtext.hyperlink;
    exports com.tlcsdm.core.util;
    exports com.tlcsdm.core.exception;
    exports com.tlcsdm.core.annotation;
    exports com.tlcsdm.core.factory;
    exports com.tlcsdm.core.factory.config;
    exports com.tlcsdm.core.freemarker;
    exports com.tlcsdm.core.freemarker.template;
    exports com.tlcsdm.core.groovy;
    exports com.tlcsdm.core.logging;
    exports com.tlcsdm.core.logging.logback;
    exports com.tlcsdm.core.eventbus;
    exports com.tlcsdm.core.event;
    exports com.tlcsdm.core.httpserver;
    exports com.tlcsdm.core.wrap.hutool;

    uses com.tlcsdm.core.freemarker.TemplateLoaderService;
    uses com.tlcsdm.core.groovy.GroovyLoaderService;

}
