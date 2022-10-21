package com.tlcsdm.login;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author: 唐 亮
 * @date: 2022/10/21 23:06
 */
public class LoginFrame extends Application {
    Label nameLabel = new Label("User Name :");
    Label passwordLabel = new Label("Password  : ");

    HBox user = new HBox();
    HBox password = new HBox();
    TextField tfUser = new TextField();
    PasswordField tfPassword = new PasswordField();
    Button btLogIn = new Button("Log in");
    Button btSignIn = new Button("Sign in");
    HBox h3 = new HBox();//装按钮
    VBox pane = new VBox();

    @Override
    public void start(Stage stage) {
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

        stage.setScene(new Scene(pane, 400, 250));
        stage.setTitle("Welcome!");
        stage.show();

        btLogIn.setOnAction(e -> {
            if (user_exist() == false) {
                System.out.println("用户不存在");
                HBox hBox = new HBox();
                Label label = new Label("用户不存在");
                ImageView image = new ImageView("No.png");
                image.setFitWidth(150);
                image.setFitHeight(120);
                hBox.setAlignment(Pos.CENTER);
                hBox.setSpacing(10);
                hBox.getChildren().addAll(image, label);
                Stage stage1 = new Stage();
                stage1.setScene(new Scene(hBox, 300, 200));
                stage1.setTitle("ERROR");
                stage1.show();
            } else if (user_right() & detection_information()) {
                stage.hide();
                //登陆成功
                //todo new Table01();
            }
        });

        btSignIn.setOnAction(e -> {
            if (user_exist()) {
                System.out.println("用户已存在");
                HBox hBox = new HBox();
                Label label = new Label("用户已存在");
                ImageView image = new ImageView("No.png");
                image.setFitWidth(150);
                image.setFitHeight(120);
                hBox.setAlignment(Pos.CENTER);
                hBox.setSpacing(10);
                hBox.getChildren().addAll(image, label);
                Stage stage1 = new Stage();
                stage1.setScene(new Scene(hBox, 300, 200));
                stage1.setTitle("ERROR");
                stage1.show();
            } else if (detection_information()) {
//                DataBaseUtil db = new DataBaseUtil();
//                String sql = "INSERT INTO t_users (userName,password) VALUES (?,?)";
//                db.updateExecute(sql,new String[]{tfUser.getText(),tfPassword.getText()});
                HBox hBox = new HBox();
                Label label = new Label("注册成功！");
                ImageView image = new ImageView("yes.png");
                image.setFitWidth(150);
                image.setFitHeight(120);
                hBox.setAlignment(Pos.CENTER);
                hBox.setSpacing(10);
                hBox.getChildren().addAll(image, label);
                Stage stage1 = new Stage();
                stage1.setScene(new Scene(hBox, 300, 200));
                stage1.show();
            }
        });
    }

    public Boolean user_exist() {//判断是否已存在用户
//        DataBaseUtil db = new DataBaseUtil();
//        String sql = "select count(*) from t_users where username = '"+tfUser.getText()+"'";
//        try {
//            ResultSet rs = db.queryExecute(sql);
//            rs.next();
//            if(rs.getInt(1)==1){
//                return true;
//            }
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        return false;
        return true;
    }

    public Boolean user_right() {
//        DataBaseUtil db = new DataBaseUtil();
//        String sql = "select count(*) from t_users where username = ? and password = ?";
//        try {
//            ResultSet rs = db.queryExecute(sql, new String[]{tfUser.getText(), tfPassword.getText()});
//            rs.next();
//            if (rs.getInt(1) == 1) {
//                return true;
//            }
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }

        System.out.println("用户名或密码不正确");
        HBox hBox = new HBox();
        Label label = new Label("用户名或密码不正确");
        ImageView image = new ImageView("No.png");
        image.setFitWidth(150);
        image.setFitHeight(120);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);
        hBox.getChildren().addAll(image, label);
        Stage stage1 = new Stage();
        stage1.setScene(new Scene(hBox, 300, 200));
        stage1.setTitle("ERROR");
        stage1.show();
        //return false;
        return true;
    }

    public Boolean detection_information() {//判断信息是否填写完全

        if (tfUser.getText().equals("") || tfPassword.getText().equals("")) {
            System.out.println("信息不全！");
            HBox hBox = new HBox();
            Label label = new Label("用户信息填写不全！");
            ImageView image = new ImageView("No.png");
            image.setFitWidth(150);
            image.setFitHeight(120);
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(10);
            hBox.getChildren().addAll(image, label);
            Stage stage1 = new Stage();
            stage1.setScene(new Scene(hBox, 300, 200));
            stage1.setTitle("ERROR");
            stage1.show();
            return false;//信息不全
        }
        return true;//信息全
    }

}
