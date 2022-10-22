package com.tlcsdm.login;

import javafx.stage.Stage;

/**
 * 登录校验(应用模块实现)
 */
public interface LoginCheck {

    /**
     * 判断信息是否填写完全
     */
    boolean checkInformation(Record loginNodes);

    /**
     * 登录按钮功能实现
     */
    void loginAction(Stage stage, Record loginNodes);

    /**
     * 注册按钮功能实现
     */
    void signAction(Record loginNodes);

    /**
     * 登录页组件设定
     */
    void initNode(Record loginNodes);

}
