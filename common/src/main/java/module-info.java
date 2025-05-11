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

import com.tlcsdm.core.freemarker.TemplateLoaderService;
import com.tlcsdm.jfxcommon.config.CommonTemplateLoaderProvider;

module com.tlcsdm.jfxcommon {
    requires java.desktop;
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.swing;
    requires static javafx.web;
    requires com.tlcsdm.core;
    requires com.tlcsdm.frame;
    requires org.controlsfx.controls;
    requires cn.hutool.core;
    requires cn.hutool.poi;
    requires cn.hutool.log;
    requires org.slf4j;
    requires static freemarker;
    requires static com.fasterxml.jackson.databind;
    requires static org.apache.commons.csv;
    requires static org.apache.poi.poi;
    requires static net.coobird.thumbnailator;
    requires org.apache.commons.codec;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;
    requires org.apache.commons.text;
    requires java.net.http;
    requires org.apache.commons.configuration2;
    requires static org.fxmisc.flowless;

    exports com.tlcsdm.jfxcommon;
    exports com.tlcsdm.jfxcommon.provider to com.tlcsdm.frame;
    exports com.tlcsdm.jfxcommon.tools to javafx.fxml, com.tlcsdm.frame;
    exports com.tlcsdm.jfxcommon.tools.image to javafx.fxml, com.tlcsdm.frame;
    exports com.tlcsdm.jfxcommon.tools.escape to com.tlcsdm.frame, javafx.fxml;
    exports com.tlcsdm.jfxcommon.code to javafx.fxml, com.tlcsdm.frame;
    exports com.tlcsdm.jfxcommon.debug to javafx.fxml, com.tlcsdm.frame;

    opens com.tlcsdm.jfxcommon.tools to javafx.graphics;
    opens com.tlcsdm.jfxcommon.code to javafx.fxml, javafx.graphics;
    opens com.tlcsdm.jfxcommon.debug to javafx.fxml, javafx.graphics;
    opens com.tlcsdm.jfxcommon.tools.image to javafx.fxml, javafx.graphics;
    opens com.tlcsdm.jfxcommon.tools.escape to javafx.fxml, javafx.graphics;

    provides com.tlcsdm.frame.service.FXSamplerProject with com.tlcsdm.jfxcommon.provider.CommonSamplerProjectProvider;
    provides TemplateLoaderService with CommonTemplateLoaderProvider;

}
