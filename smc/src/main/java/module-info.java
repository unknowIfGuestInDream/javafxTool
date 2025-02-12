/*
 * Copyright (c) 2023 unknowIfGuestInDream.
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
import com.tlcsdm.frame.service.BannerPrinterService;
import com.tlcsdm.frame.service.CenterPanelService;
import com.tlcsdm.frame.service.FXSamplerConfiguration;
import com.tlcsdm.frame.service.FXSamplerProject;
import com.tlcsdm.frame.service.MenubarConfigration;
import com.tlcsdm.frame.service.SamplePostProcessorService;
import com.tlcsdm.frame.service.SamplesTreeViewConfiguration;
import com.tlcsdm.frame.service.SplashScreen;
import com.tlcsdm.frame.service.VersionCheckerService;
import com.tlcsdm.smc.config.SmcGroovyLoaderProvider;
import com.tlcsdm.smc.config.SmcTemplateLoaderProvider;
import com.tlcsdm.smc.provider.SmcBanner;
import com.tlcsdm.smc.provider.SmcCenterPanelProvider;
import com.tlcsdm.smc.provider.SmcConfigurationProvider;
import com.tlcsdm.smc.provider.SmcMenubarConfigrationProvider;
import com.tlcsdm.smc.provider.SmcSamplePostProcessorProvider;
import com.tlcsdm.smc.provider.SmcSampleTreeViewConfiguration;
import com.tlcsdm.smc.provider.SmcSamplerProjectProvider;
import com.tlcsdm.smc.provider.SmcSplashProvider;
import com.tlcsdm.smc.provider.SmcVersionCheckerProvider;

module com.tlcsdm.smc {
    requires java.desktop;
    requires java.net.http;
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.web;
    requires com.tlcsdm.core;
    requires com.tlcsdm.frame;
    requires org.controlsfx.controls;
    requires cn.hutool.core;
    requires cn.hutool.poi;
    requires cn.hutool.log;
    requires org.apache.poi.poi;
    requires org.slf4j;
    requires freemarker;
    requires jdk.httpserver;
    requires org.fxmisc.flowless;
    requires com.dlsc.formsfx;
    requires com.dlsc.preferencesfx;
    requires com.fasterxml.jackson.databind;
    requires com.github.benmanes.caffeine;

    exports com.tlcsdm.smc;
    exports com.tlcsdm.smc.provider to com.tlcsdm.core, com.tlcsdm.frame;
    exports com.tlcsdm.smc.tools to com.tlcsdm.frame;
    exports com.tlcsdm.smc.unitTest to com.tlcsdm.frame;
    exports com.tlcsdm.smc.codeDev to com.tlcsdm.frame;
    exports com.tlcsdm.smc.codeDev.ecm to com.tlcsdm.frame;
    exports com.tlcsdm.smc.unitDesign to com.tlcsdm.frame;
    exports com.tlcsdm.smc.config to com.tlcsdm.core;
    exports com.tlcsdm.smc.tools.girret to com.fasterxml.jackson.databind;

    opens com.tlcsdm.smc.codeDev to javafx.graphics;
    opens com.tlcsdm.smc.codeDev.ecm to javafx.graphics;
    opens com.tlcsdm.smc.tools to javafx.graphics;
    opens com.tlcsdm.smc.unitDesign to javafx.graphics;
    opens com.tlcsdm.smc.unitTest to javafx.graphics;

    provides FXSamplerProject with SmcSamplerProjectProvider;
    provides MenubarConfigration with SmcMenubarConfigrationProvider;
    provides CenterPanelService with SmcCenterPanelProvider;
    provides FXSamplerConfiguration with SmcConfigurationProvider;
    provides SplashScreen with SmcSplashProvider;
    provides TemplateLoaderService with SmcTemplateLoaderProvider;
    provides GroovyLoaderService with SmcGroovyLoaderProvider;
    provides SamplePostProcessorService with SmcSamplePostProcessorProvider;
    provides VersionCheckerService with SmcVersionCheckerProvider;
    provides SamplesTreeViewConfiguration with SmcSampleTreeViewConfiguration;
    provides BannerPrinterService with SmcBanner;
}
