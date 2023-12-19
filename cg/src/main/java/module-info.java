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

import com.tlcsdm.cg.provider.CgBanner;
import com.tlcsdm.cg.provider.CgCenterPanelProvider;
import com.tlcsdm.cg.provider.CgConfigurationProvider;
import com.tlcsdm.cg.provider.CgMenubarConfigrationProvider;
import com.tlcsdm.cg.provider.CgSamplePostProcessorProvider;
import com.tlcsdm.cg.provider.CgSampleTreeViewConfiguration;
import com.tlcsdm.cg.provider.CgSamplerProjectProvider;
import com.tlcsdm.cg.provider.CgSplashProvider;
import com.tlcsdm.cg.provider.CgVersionCheckerProvider;
import com.tlcsdm.frame.service.BannerPrinterService;
import com.tlcsdm.frame.service.CenterPanelService;
import com.tlcsdm.frame.service.FXSamplerConfiguration;
import com.tlcsdm.frame.service.FXSamplerProject;
import com.tlcsdm.frame.service.MenubarConfigration;
import com.tlcsdm.frame.service.SamplePostProcessorService;
import com.tlcsdm.frame.service.SamplesTreeViewConfiguration;
import com.tlcsdm.frame.service.SplashScreen;
import com.tlcsdm.frame.service.VersionCheckerService;

module com.tlcsdm.cg {
    requires java.desktop;
    requires java.net.http;
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires org.controlsfx.controls;
    requires cn.hutool.core;
    requires cn.hutool.poi;
    requires cn.hutool.log;
    requires org.apache.poi.poi;
    requires org.slf4j;
    requires com.fasterxml.jackson.databind;
    requires com.tlcsdm.frame;
    requires com.tlcsdm.core;
    requires jdk.httpserver;

    exports com.tlcsdm.cg;
    exports com.tlcsdm.cg.provider to com.tlcsdm.core, com.tlcsdm.frame;

    provides FXSamplerProject with CgSamplerProjectProvider;
    provides MenubarConfigration with CgMenubarConfigrationProvider;
    provides CenterPanelService with CgCenterPanelProvider;
    provides FXSamplerConfiguration with CgConfigurationProvider;
    provides SplashScreen with CgSplashProvider;
    provides SamplePostProcessorService with CgSamplePostProcessorProvider;
    provides VersionCheckerService with CgVersionCheckerProvider;
    provides SamplesTreeViewConfiguration with CgSampleTreeViewConfiguration;
    provides BannerPrinterService with CgBanner;

}
