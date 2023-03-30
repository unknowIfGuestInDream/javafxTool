module com.tlcsdm.frame {

    requires transitive javafx.controls;
    requires transitive javafx.web;
    requires javafx.graphics;
    requires javafx.base;
    requires com.tlcsdm.core;
    requires cn.hutool.core;
    requires cn.hutool.log;
    requires cn.hutool.crypto;

    exports com.tlcsdm.frame;
    exports com.tlcsdm.frame.model;
    exports com.tlcsdm.frame.service;

    uses com.tlcsdm.frame.FXSamplerProject;
    uses com.tlcsdm.frame.MenubarConfigration;
    uses com.tlcsdm.frame.FXSamplerConfiguration;
    uses com.tlcsdm.frame.CenterPanelService;
    uses com.tlcsdm.frame.SplashScreen;
    uses com.tlcsdm.frame.service.SamplePostProcessorService;
}