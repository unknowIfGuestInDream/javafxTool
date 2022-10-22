import com.tlcsdm.frame.CenterPanelService;

module com.tlcsdm.frame {

	requires transitive javafx.controls;
	requires transitive javafx.web;
	requires tlcsdm.common;
	requires hutool.all;

	exports com.tlcsdm.frame;
	exports com.tlcsdm.frame.model;

	uses com.tlcsdm.frame.FXSamplerProject;
	uses com.tlcsdm.frame.MenubarConfigration;
	uses com.tlcsdm.frame.FXSamplerConfiguration;
	uses CenterPanelService;
}