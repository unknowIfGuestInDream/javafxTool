module com.tlcsdm.frame {

    requires transitive javafx.controls;
    requires transitive javafx.web;
	requires javafx.graphics;
	requires javafx.base;
    requires com.tlcsdm.core;
	requires hutool.core;
	requires hutool.crypto;

    exports com.tlcsdm.frame;
    exports com.tlcsdm.frame.model;

    uses com.tlcsdm.frame.FXSamplerProject;
    uses com.tlcsdm.frame.MenubarConfigration;
    uses com.tlcsdm.frame.FXSamplerConfiguration;
    uses com.tlcsdm.frame.CenterPanelService;
    uses com.tlcsdm.frame.SplashScreen;
}