module com.tlcsdm.login {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.tlcsdm.core;

    opens com.tlcsdm.login to javafx.fxml;
    exports com.tlcsdm.login;
    exports com.tlcsdm.login.service;
    opens com.tlcsdm.login.service to javafx.fxml;

    uses com.tlcsdm.login.service.LoginCheck;
}