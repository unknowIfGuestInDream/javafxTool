package com.tlcsdm.smc;

import com.tlcsdm.frame.FXSampler;
import com.tlcsdm.login.LoginCheck;
import com.tlcsdm.login.LoginNodes;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * @author: 唐 亮
 * @date: 2022/10/22 9:16
 */
public class SmcLoginCheck implements LoginCheck {
    @Override
    public boolean checkInformation(LoginNodes loginNodes) {
        if ("".equals(loginNodes.tfUser().getText()) || "".equals(loginNodes.tfPassword().getText())) {
            HBox hBox = new HBox();
            Label label = new Label("用户信息填写不全！");
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(10);
            hBox.getChildren().addAll(label);
            Stage stage1 = new Stage();
            stage1.setScene(new Scene(hBox, 300, 200));
            stage1.setTitle("ERROR");
            stage1.show();
            return false;
        }
        return true;
    }

    @Override
    public void loginAction(Stage stage, LoginNodes loginNodes) {
        if (!user_exist()) {
            HBox hBox = new HBox();
            Label label = new Label("用户不存在");
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(10);
            hBox.getChildren().addAll(label);
            Stage stage1 = new Stage();
            stage1.setScene(new Scene(hBox, 300, 200));
            stage1.setTitle("ERROR");
            stage1.show();
        } else if (user_right() & checkInformation(loginNodes)) {
            stage.hide();
            //登陆成功
            login();
        }
    }

    @Override
    public void signAction(LoginNodes loginNodes) {
        if (user_exist()) {
            HBox hBox = new HBox();
            Label label = new Label("用户已存在");
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(10);
            hBox.getChildren().addAll(label);
            Stage stage1 = new Stage();
            stage1.setScene(new Scene(hBox, 300, 200));
            stage1.setTitle("ERROR");
            stage1.show();
        } else if (checkInformation(loginNodes)) {
            HBox hBox = new HBox();
            Label label = new Label("注册成功！");
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(10);
            hBox.getChildren().addAll(label);
            Stage stage1 = new Stage();
            stage1.setScene(new Scene(hBox, 300, 200));
            stage1.show();
        }
    }

    @Override
    public void initNode(LoginNodes loginNodes) {
        // Do nothing
    }

    private void login() {
        FXSampler fxSampler = new FXSampler();
        fxSampler.start(new Stage());
    }

    public Boolean user_exist() {//判断是否已存在用户
        return true;
    }

    public Boolean user_right() {
        return true;
    }
}
