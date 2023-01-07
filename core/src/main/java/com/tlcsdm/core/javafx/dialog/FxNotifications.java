package com.tlcsdm.core.javafx.dialog;

import com.tlcsdm.core.javafx.FxApp;
import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 * @author: 唐 亮
 * @date: 2022/12/3 21:20
 */
public class FxNotifications {

    public static Notifications defaultNotify() {
        return notifications(Duration.seconds(5), Pos.TOP_CENTER);
    }

    public static Notifications notifications(Duration duration, Pos position) {
        return Notifications.create().hideAfter(duration)
                .position(position).owner(FxApp.primaryStage);
    }

}
