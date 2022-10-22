module com.tlcsdm.login {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.tlcsdm.login to javafx.fxml;
    exports com.tlcsdm.login;

    uses com.tlcsdm.login.LoginCheck;
}