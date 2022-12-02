module com.tlcsdm.smc {
	requires javafx.fxml;
	requires com.tlcsdm.frame;
	requires com.tlcsdm.login;
	requires java.desktop;
	requires org.controlsfx.controls;
	requires javafx.controls;
	requires io.github.javadiffutils;
	requires javafx.graphics;
	requires com.tlcsdm.core;
	requires hutool.core;
	requires hutool.poi;
	requires org.apache.poi.poi;

	exports com.tlcsdm.smc;
	exports com.tlcsdm.smc.tools;

	provides com.tlcsdm.frame.FXSamplerProject with com.tlcsdm.smc.SmcSamplerProject;
	provides com.tlcsdm.frame.MenubarConfigration with com.tlcsdm.smc.SmcMenubarConfigration;
	provides com.tlcsdm.frame.CenterPanelService with com.tlcsdm.smc.SmcCenterPanelService;
	provides com.tlcsdm.frame.FXSamplerConfiguration with com.tlcsdm.smc.SmcConfiguration;
	provides com.tlcsdm.login.LoginCheck with com.tlcsdm.smc.SmcLoginCheck;
}