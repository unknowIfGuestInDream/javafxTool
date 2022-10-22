package com.tlcsdm.login;

import javafx.stage.Stage;

/**
 * 登录校验(应用模块实现)
 */
public interface LoginCheck {

    /**
     * 判断信息是否填写完全
     */
    boolean checkInformation(LoginNodes loginNodes);

    /**
     * 登录按钮功能实现
     */
    void loginAction(Stage stage, LoginNodes loginNodes);

    /**
     * 注册按钮功能实现
     */
    void signAction(LoginNodes loginNodes);

    /**
     * 登录页组件设定
     */
    void initNode(LoginNodes loginNodes);

}
