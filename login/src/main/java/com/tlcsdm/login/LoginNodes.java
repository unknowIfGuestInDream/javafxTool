package com.tlcsdm.login;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * LoginFrame中的Node组件, 传递到应用模块进行定制化处理
 */
public record LoginNodes(Label nameLabel, Label passwordLabel, HBox user, HBox password, TextField tfUser,
                         PasswordField tfPassword, Button btLogIn, Button btSignIn, HBox h3) {
}
