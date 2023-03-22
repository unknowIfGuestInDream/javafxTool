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
    requires cn.hutool.core;
    requires cn.hutool.poi;
    requires cn.hutool.log;
    requires cn.hutool.json;
    requires org.apache.poi.poi;
    requires org.slf4j;
    requires freemarker;
    requires java.xml;

    exports com.tlcsdm.smc;
    exports com.tlcsdm.smc.tools to com.tlcsdm.frame;
    exports com.tlcsdm.smc.unitTest to com.tlcsdm.frame;
    exports com.tlcsdm.smc.codeDev to com.tlcsdm.frame;
    exports com.tlcsdm.smc.unitDesign to com.tlcsdm.frame;
    exports com.tlcsdm.smc.config to com.tlcsdm.core;

    opens com.tlcsdm.smc.codeDev to javafx.graphics;
    opens com.tlcsdm.smc.tools to javafx.graphics;
    opens com.tlcsdm.smc.unitDesign to javafx.graphics;
    opens com.tlcsdm.smc.unitTest to javafx.graphics;

    provides com.tlcsdm.frame.FXSamplerProject with com.tlcsdm.smc.SmcSamplerProject;
    provides com.tlcsdm.frame.MenubarConfigration with com.tlcsdm.smc.SmcMenubarConfigration;
    provides com.tlcsdm.frame.CenterPanelService with com.tlcsdm.smc.SmcCenterPanelService;
    provides com.tlcsdm.frame.FXSamplerConfiguration with com.tlcsdm.smc.SmcConfiguration;
    provides com.tlcsdm.frame.SplashScreen with com.tlcsdm.smc.SmcSplash;
    provides com.tlcsdm.login.LoginCheck with com.tlcsdm.smc.SmcLoginCheck;
    provides com.tlcsdm.core.freemarker.TemplateLoaderService with com.tlcsdm.smc.config.SmcTemplateLoaderProvider;
}