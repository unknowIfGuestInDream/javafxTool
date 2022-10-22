package com.tlcsdm.login;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public record LoginNodes(Label nameLabel, Label passwordLabel, HBox user, HBox password, TextField tfUser,
                         PasswordField tfPassword, Button btLogIn, Button btSignIn, HBox h3) {
}
