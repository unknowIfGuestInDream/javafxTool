module com.tlcsdm.smc {
    requires java.desktop;
    requires java.net.http;
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires com.tlcsdm.core;
    requires com.tlcsdm.frame;
    requires com.tlcsdm.login;
    requires org.controlsfx.controls;
    requires io.github.javadiffutils;
    requires hutool.core;
    requires hutool.poi;
    requires hutool.log;
    requires hutool.json;
    requires org.apache.poi.poi;
    requires org.slf4j;

    exports com.tlcsdm.smc;
    exports com.tlcsdm.smc.tools;
    exports com.tlcsdm.smc.unitTest;
    exports com.tlcsdm.smc.codeDev;
    exports com.tlcsdm.smc.unitDesign;

    provides com.tlcsdm.frame.FXSamplerProject with com.tlcsdm.smc.SmcSamplerProject;
    provides com.tlcsdm.frame.MenubarConfigration with com.tlcsdm.smc.SmcMenubarConfigration;
    provides com.tlcsdm.frame.CenterPanelService with com.tlcsdm.smc.SmcCenterPanelService;
    provides com.tlcsdm.frame.FXSamplerConfiguration with com.tlcsdm.smc.SmcConfiguration;
    provides com.tlcsdm.login.LoginCheck with com.tlcsdm.smc.SmcLoginCheck;
}