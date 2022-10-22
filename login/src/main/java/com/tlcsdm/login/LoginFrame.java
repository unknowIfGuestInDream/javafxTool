package com.tlcsdm.login;

import com.tlcsdm.login.util.I18nUtils;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.ServiceLoader;

/**
 * 登录框架
 */
public class LoginFrame extends Application {
    Label nameLabel = new Label(I18nUtils.get("login.userNameLabel"));
    Label passwordLabel = new Label(I18nUtils.get("login.passwordLabel"));

    HBox user = new HBox();
    HBox password = new HBox();
    TextField tfUser = new TextField();
    PasswordField tfPassword = new PasswordField();
    Button btLogIn = new Button(I18nUtils.get("login.btLogIn"));
    Button btSignIn = new Button(I18nUtils.get("login.btSignIn"));
    HBox h3 = new HBox();
    VBox pane = new VBox();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        ServiceLoader<LoginCheck> loginCheckServiceLoader = ServiceLoader.load(LoginCheck.class);
        var ref = new Object() {
            LoginCheck loginCheck = null;
        };
        for (LoginCheck lc : loginCheckServiceLoader) {
            ref.loginCheck = lc;
        }
        LoginNodes loginNodes = new LoginNodes(nameLabel, passwordLabel, user, password, tfUser, tfPassword, btLogIn, btSignIn, h3);

        user.getChildren().addAll(nameLabel, tfUser);
        user.setAlignment(Pos.CENTER);
        user.setSpacing(20);

        password.getChildren().addAll(passwordLabel, tfPassword);
        password.setAlignment(Pos.CENTER);
        password.setSpacing(20);

        h3.setAlignment(Pos.CENTER);
        btLogIn.setAlignment(Pos.BASELINE_RIGHT);
        btSignIn.setAlignment(Pos.BASELINE_RIGHT);
        h3.getChildren().addAll(btLogIn, btSignIn);
        h3.setSpacing(20);

        pane.setAlignment(Pos.CENTER);
        pane.setSpacing(20);
        pane.getChildren().addAll(user, password, h3);

        stage.getIcons()
                .add(new Image(Objects.requireNonNull(getClass().getResource("/static/logo.png")).toExternalForm()));
        stage.setScene(new Scene(pane, 400, 250));
        stage.setTitle(I18nUtils.get("login.title"));
        stage.show();

        btLogIn.setOnAction(e -> {
            if (ref.loginCheck != null) {
                ref.loginCheck.loginAction(stage, loginNodes);
            }
        });

        btSignIn.setOnAction(e -> {
            if (ref.loginCheck != null) {
                ref.loginCheck.signAction(loginNodes);
            }
        });

        if (ref.loginCheck != null) {
            ref.loginCheck.initNode(loginNodes);
        }
    }

}