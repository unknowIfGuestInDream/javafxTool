module com.tlcsdm.smc {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.tlcsdm.smc to javafx.fxml;
    exports com.tlcsdm.smc;
}