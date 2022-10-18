module com.tlcsdm.frame {

	requires transitive javafx.controls;
	requires transitive javafx.web;

	exports com.tlcsdm.frame;
	exports com.tlcsdm.frame.model;

	uses com.tlcsdm.frame.FXSamplerProject;
	uses com.tlcsdm.frame.FXSamplerConfiguration;
}