package com.tlcsdm.frame;

import javafx.scene.control.MenuBar;
import javafx.stage.Stage;

/**
 * 菜单栏配置(应用模块实现)
 */
public interface MenubarConfigration {

	MenuBar menuBar = new MenuBar();

	MenuBar setMenuBar(MenuBar menuBar, Stage stage);

	default MenuBar getMenuBar(Stage primaryStage) {
		setMenuBar(menuBar, primaryStage);
		// 设置菜单条长度
		menuBar.setPrefWidth(primaryStage.getWidth());
		// 宽度监听设置菜单条长度
		menuBar.widthProperty()
				.addListener((observable, oldValue, newValue) -> menuBar.setPrefWidth(newValue.doubleValue()));
		return menuBar;
	}

}
