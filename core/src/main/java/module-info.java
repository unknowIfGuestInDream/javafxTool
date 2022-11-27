module com.tlcsdm.core {
    requires javafx.graphics;
    requires java.desktop;
    requires org.apache.commons.lang3;
    requires javafx.controls;
    requires org.apache.commons.configuration2;

    exports com.tlcsdm.core.javafx;
    exports com.tlcsdm.core.javafx.dialog;
    exports com.tlcsdm.core.javafx.helper;
    exports com.tlcsdm.core.javafx.util;

}