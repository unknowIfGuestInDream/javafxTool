module com.tlcsdm.core {
    requires javafx.graphics;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires org.apache.commons.lang3;
    requires javafx.controls;
    requires org.apache.commons.configuration2;
    requires org.controlsfx.controls;
    requires hutool.log;
    requires hutool.core;
    requires commons.beanutils;
    requires static io.github.javadiffutils;
    requires static freemarker;
    requires ch.qos.logback.classic;
    requires ch.qos.logback.core;

    opens com.tlcsdm.core.javafx.controller to javafx.fxml;
    opens com.tlcsdm.core.javafx.view to javafx.fxml;

    exports com.tlcsdm.core.javafx;
    exports com.tlcsdm.core.javafx.control;
    exports com.tlcsdm.core.javafx.controlsfx;
    exports com.tlcsdm.core.javafx.dialog;
    exports com.tlcsdm.core.javafx.helper;
    exports com.tlcsdm.core.javafx.util;
    exports com.tlcsdm.core.util;
    exports com.tlcsdm.core.exception;
    exports com.tlcsdm.core.factory;
    exports com.tlcsdm.core.factory.config;
    exports com.tlcsdm.core.freemarker;
    exports com.tlcsdm.core.freemarker.template;
    exports com.tlcsdm.core.logging.logback;

    uses com.tlcsdm.core.freemarker.TemplateLoaderService;

}