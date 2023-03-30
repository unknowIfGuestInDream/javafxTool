/*
 * Copyright (c) 2019, 2023 unknowIfGuestInDream
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of unknowIfGuestInDream, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL UNKNOWIFGUESTINDREAM BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.tlcsdm.login;

import com.tlcsdm.login.service.LoginCheck;
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
        LoginNodes loginNodes = new LoginNodes(nameLabel, passwordLabel, user, password, tfUser, tfPassword, btLogIn,
                btSignIn, h3);

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