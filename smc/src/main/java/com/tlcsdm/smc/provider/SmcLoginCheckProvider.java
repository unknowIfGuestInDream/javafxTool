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

package com.tlcsdm.smc.provider;

import com.tlcsdm.frame.FXSampler;
import com.tlcsdm.login.LoginNodes;
import com.tlcsdm.login.service.LoginCheck;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * @author unknowIfGuestInDream
 * @date 2022/10/22 9:16
 */
public class SmcLoginCheckProvider implements LoginCheck {

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
            // 登陆成功
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

    public Boolean user_exist() {// 判断是否已存在用户
        return true;
    }

    public Boolean user_right() {
        return true;
    }
}
