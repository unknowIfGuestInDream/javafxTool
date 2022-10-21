module com.tlcsdm.frame {

	requires transitive javafx.controls;
	requires transitive javafx.web;
	requires com.tlcsdm.javafxtoollogin;

	exports com.tlcsdm.frame;
	exports com.tlcsdm.frame.model;

	uses com.tlcsdm.frame.FXSamplerProject;
	uses com.tlcsdm.frame.MenubarConfigration;
	uses com.tlcsdm.frame.FXSamplerConfiguration;
}