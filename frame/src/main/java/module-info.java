/*
 * Copyright (c) 2024 unknowIfGuestInDream.
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

import com.tlcsdm.frame.service.EasterEggService;
import com.tlcsdm.frame.service.impl.DefaultEasterEggService;

module com.tlcsdm.frame {

    requires transitive javafx.controls;
    requires javafx.graphics;
    requires javafx.base;
    requires com.tlcsdm.core;
    requires cn.hutool.core;
    requires cn.hutool.log;
    requires cn.hutool.crypto;
    requires static com.github.benmanes.caffeine;
    requires javafx.fxml;
    requires java.net.http;
    requires fr.brouillard.oss.cssfx;

    exports com.tlcsdm.frame;
    exports com.tlcsdm.frame.model;
    exports com.tlcsdm.frame.service;
    exports com.tlcsdm.frame.service.impl;
    exports com.tlcsdm.frame.event;
    exports com.tlcsdm.frame.cache;

    uses com.tlcsdm.frame.service.FXSamplerProject;
    uses com.tlcsdm.frame.service.MenubarConfigration;
    uses com.tlcsdm.frame.service.FXSamplerConfiguration;
    uses com.tlcsdm.frame.service.CenterPanelService;
    uses com.tlcsdm.frame.service.SplashScreen;
    uses com.tlcsdm.frame.service.SamplePostProcessorService;
    uses com.tlcsdm.frame.service.VersionCheckerService;
    uses com.tlcsdm.frame.service.SamplesTreeViewConfiguration;
    uses com.tlcsdm.frame.service.BannerPrinterService;
    uses com.tlcsdm.frame.service.EasterEggService;

    provides EasterEggService with DefaultEasterEggService;
}
