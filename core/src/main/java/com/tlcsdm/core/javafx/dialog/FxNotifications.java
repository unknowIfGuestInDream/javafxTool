package com.tlcsdm.core.javafx.dialog;

import org.controlsfx.control.Notifications;

import com.tlcsdm.core.javafx.FxApp;

import javafx.geometry.Pos;
import javafx.util.Duration;

/**
 * @author: unknowIfGuestInDream
 * @date: 2022/12/3 21:20
 */
public class FxNotifications {

	public static Notifications defaultNotify() {
		return notifications(Duration.seconds(5), Pos.TOP_CENTER);
	}

	public static Notifications notifications(Duration duration, Pos position) {
		return Notifications.create().hideAfter(duration).position(position).owner(FxApp.primaryStage);
	}

}
