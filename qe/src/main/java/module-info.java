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
import com.tlcsdm.core.groovy.GroovyLoaderService;
import com.tlcsdm.frame.service.CenterPanelService;
import com.tlcsdm.frame.service.FXSamplerConfiguration;
import com.tlcsdm.frame.service.FXSamplerProject;
import com.tlcsdm.frame.service.MenubarConfigration;
import com.tlcsdm.frame.service.SamplePostProcessorService;
import com.tlcsdm.frame.service.SamplesTreeViewConfiguration;
import com.tlcsdm.frame.service.SplashScreen;
import com.tlcsdm.frame.service.VersionCheckerService;
import com.tlcsdm.qe.config.QeGroovyLoaderProvider;
import com.tlcsdm.qe.config.QeTemplateLoaderProvider;
import com.tlcsdm.qe.provider.QeCenterPanelProvider;
import com.tlcsdm.qe.provider.QeConfigurationProvider;
import com.tlcsdm.qe.provider.QeMenubarConfigrationProvider;
import com.tlcsdm.qe.provider.QeSamplePostProcessorProvider;
import com.tlcsdm.qe.provider.QeSampleTreeViewConfiguration;
import com.tlcsdm.qe.provider.QeSamplerProjectProvider;
import com.tlcsdm.qe.provider.QeSplashProvider;
import com.tlcsdm.qe.provider.QeVersionCheckerProvider;

module com.tlcsdm.qe {
    requires java.desktop;
    requires java.net.http;
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires com.tlcsdm.core;
    requires com.tlcsdm.frame;
    requires org.controlsfx.controls;
    requires cn.hutool.core;
    requires cn.hutool.poi;
    requires cn.hutool.log;
    requires cn.hutool.json;
    requires org.apache.poi.poi;
    requires org.slf4j;
    requires freemarker;
    requires javafx.base;

    exports com.tlcsdm.qe;
    exports com.tlcsdm.qe.provider to com.tlcsdm.core, com.tlcsdm.frame, com.tlcsdm.login;
    exports com.tlcsdm.qe.tools to com.tlcsdm.frame;
    exports com.tlcsdm.qe.config to com.tlcsdm.core;

    opens com.tlcsdm.qe.tools to javafx.graphics, javafx.fxml, javafx.base;

    provides FXSamplerProject with QeSamplerProjectProvider;
    provides MenubarConfigration with QeMenubarConfigrationProvider;
    provides CenterPanelService with QeCenterPanelProvider;
    provides FXSamplerConfiguration with QeConfigurationProvider;
    provides SplashScreen with QeSplashProvider;
    provides TemplateLoaderService with QeTemplateLoaderProvider;
    provides GroovyLoaderService with QeGroovyLoaderProvider;
    provides SamplePostProcessorService with QeSamplePostProcessorProvider;
    provides VersionCheckerService with QeVersionCheckerProvider;
    provides SamplesTreeViewConfiguration with QeSampleTreeViewConfiguration;
}
