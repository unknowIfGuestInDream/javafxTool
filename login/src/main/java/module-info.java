module com.tlcsdm.javafxtoollogin {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.tlcsdm.javafxtoollogin to javafx.fxml;
    exports com.tlcsdm.javafxtoollogin;
}