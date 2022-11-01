module com.tlcsdm.smc {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.tlcsdm.frame;
    requires com.tlcsdm.login;

    requires org.controlsfx.controls;

    opens com.tlcsdm.smc to javafx.fxml;
    exports com.tlcsdm.smc;
}