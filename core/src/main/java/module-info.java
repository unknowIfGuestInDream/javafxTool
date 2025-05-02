/*
 * Copyright (c) 2024 unknowIfGuestInDream. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of unknowIfGuestInDream, any
 * associated website, nor the names of its contributors may be used to endorse
 * or promote products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL UNKNOWIFGUESTINDREAM BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import ch.qos.logback.classic.spi.Configurator;

module com.tlcsdm.core {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.swing;
    requires static javafx.media;
    requires static javafx.web;
    requires java.desktop;
    requires java.net.http;
    requires java.sql;
    requires java.xml;
    requires static java.compiler;
    requires org.apache.commons.lang3;
    requires javafx.controls;
    requires org.apache.commons.configuration2;
    requires org.apache.commons.beanutils;
    requires org.controlsfx.controls;
    requires cn.hutool.log;
    requires cn.hutool.core;
    requires static io.github.javadiffutils;
    requires static freemarker;
    requires static com.fasterxml.jackson.core;
    requires static com.fasterxml.jackson.databind;
    requires static com.fasterxml.jackson.annotation;
    requires static com.fasterxml.jackson.dataformat.yaml;
    requires static com.fasterxml.jackson.datatype.jsr310;
    requires static org.dom4j;
    requires static org.apache.commons.jexl3;
    requires static org.apache.groovy;
    // JSR223Test测试类时使用
    requires static java.scripting;
    requires ch.qos.logback.classic;
    requires ch.qos.logback.core;
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
    requires static com.dlsc.preferencesfx;
    requires static yuicompressor;
    requires static jave.core;
    requires static com.sun.jna;
    requires static com.sun.jna.platform;
    requires static vosk;
    requires static jakarta.xml.bind;
    requires static org.python.jython2;
    requires static com.zaxxer.hikari;
    requires static druid;
    requires static jssc;
    requires static com.fazecast.jSerialComm;
    requires static com.github.oshi;
    requires static zip4j;
    requires static org.apache.commons.compress;
    requires static org.apache.commons.imaging;
    requires static net.jonathangiles.tools.teenyhttpd;
    requires org.reflections;
    requires static org.apache.commons.csv;
    requires static org.openjdk.nashorn;
    requires java.management;

    opens com.tlcsdm.core.javafx.controller to javafx.fxml;
    opens com.tlcsdm.core.javafx.view to javafx.fxml;
    opens com.tlcsdm.core.util to org.junit.platform.engine, org.junit.jupiter.api, jakarta.xml.bind;
    opens com.tlcsdm.core.model;

    exports com.tlcsdm.core.javafx;
    exports com.tlcsdm.core.javafx.control;
    exports com.tlcsdm.core.javafx.control.skin;
    exports com.tlcsdm.core.javafx.controlsfx;
    exports com.tlcsdm.core.javafx.dialog;
    exports com.tlcsdm.core.javafx.helper;
    exports com.tlcsdm.core.javafx.util;
    exports com.tlcsdm.core.javafx.bind;
    exports com.tlcsdm.core.javafx.stage;
    exports com.tlcsdm.core.javafx.richtext;
    exports com.tlcsdm.core.javafx.richtext.hyperlink;
    exports com.tlcsdm.core.javafx.factory;
    exports com.tlcsdm.core.javafx.chart;
    exports com.tlcsdm.core.util;
    exports com.tlcsdm.core.exception;
    exports com.tlcsdm.core.annotation;
    exports com.tlcsdm.core.factory;
    exports com.tlcsdm.core.factory.config;
    exports com.tlcsdm.core.freemarker;
    exports com.tlcsdm.core.freemarker.code;
    exports com.tlcsdm.core.freemarker.template;
    exports com.tlcsdm.core.freemarker.directive;
    exports com.tlcsdm.core.freemarker.method;
    exports com.tlcsdm.core.freemarker.format;
    exports com.tlcsdm.core.groovy;
    exports com.tlcsdm.core.logging.logback;
    exports com.tlcsdm.core.eventbus;
    exports com.tlcsdm.core.event;
    exports com.tlcsdm.core.httpserver;
    exports com.tlcsdm.core.concurrent;
    exports com.tlcsdm.core.dsl;
    exports com.tlcsdm.core.dsl.commands;
    exports com.tlcsdm.core.dsl.runtime;
    exports com.tlcsdm.core.dsl.impl;
    exports com.tlcsdm.core.database;
    exports com.tlcsdm.core.oshi;
    exports com.tlcsdm.core.powershell;
    exports com.tlcsdm.core.watermark;
    exports com.tlcsdm.core.ai.deepseek;

    uses com.tlcsdm.core.freemarker.TemplateLoaderService;
    uses com.tlcsdm.core.groovy.GroovyLoaderService;

    provides Configurator with com.tlcsdm.core.logging.logback.CoreConfigurator;

}
