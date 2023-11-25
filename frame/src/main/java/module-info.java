module com.tlcsdm.frame {

    requires transitive javafx.controls;
    requires transitive javafx.web;
    requires javafx.graphics;
    requires javafx.base;
    requires com.tlcsdm.core;
    requires cn.hutool.core;
    requires cn.hutool.log;
    requires cn.hutool.crypto;
    requires static com.github.benmanes.caffeine;
    requires javafx.fxml;
    requires java.net.http;

    exports com.tlcsdm.frame;
    exports com.tlcsdm.frame.model;
    exports com.tlcsdm.frame.service;
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
}
