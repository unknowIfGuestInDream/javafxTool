module com.tlcsdm.core {
	requires javafx.graphics;
	requires java.desktop;
	requires java.sql;
	requires org.apache.commons.lang3;
	requires javafx.controls;
	requires org.apache.commons.configuration2;
	requires org.controlsfx.controls;
	requires hutool.log;
	requires commons.beanutils;

	exports com.tlcsdm.core.javafx;
    exports com.tlcsdm.core.javafx.control;
    exports com.tlcsdm.core.javafx.dialog;
	exports com.tlcsdm.core.javafx.helper;
	exports com.tlcsdm.core.javafx.util;

}