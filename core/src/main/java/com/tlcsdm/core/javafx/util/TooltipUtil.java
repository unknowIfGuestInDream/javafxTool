/*
 * Copyright (c) 2023 unknowIfGuestInDream.
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

package com.tlcsdm.core.javafx.util;

import com.tlcsdm.core.javafx.dialog.FxNotifications;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.stage.Window;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.tools.Utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 提示组件,相当于使用
 * {@link org.controlsfx.control.Notifications#showInformation()}
 *
 * @author unknowIfGuestInDream
 */
public class TooltipUtil {

    private TooltipUtil() {
    }

    public static void showToast(String message) {
        showToast((Node) null, message);
    }

    public static void showToast(Node node, String message) {
        Window window = Utils.getWindow(node);
        double x;
        double y;
        if (node != null) {
            x = ScreenUtil.getScreenX(node) + ScreenUtil.getWidth(node) / 2.0D;
            y = ScreenUtil.getScreenY(node) + ScreenUtil.getHeight(node);
        } else {
            x = window.getX() + window.getWidth() / 2.0D;
            y = window.getY() + window.getHeight();
        }

        showToast(window, message, 3000L, x, y);
    }

    public static void showToast(Window window, String message, long time, double x, double y) {
        final Tooltip tooltip = new Tooltip(message);
        tooltip.setAutoHide(true);
        tooltip.setOpacity(0.9D);
        tooltip.setWrapText(true);
        tooltip.show(window, x, y);
        tooltip.setAnchorX(tooltip.getAnchorX() - tooltip.getWidth() / 2.0D);
        tooltip.setAnchorY(tooltip.getAnchorY() / 5.0D);
        if (time > 0L) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(tooltip::hide);
                }
            }, time);
        }
    }

    public static void showToast(String message, Pos pos) {
        showToast(null, message, null, 3.0D, pos, null, null, true, false);
    }

    public static void showToast(String title, String message) {
        showToast(title, message, null, 3.0D, Pos.TOP_CENTER, null, null, true, false);
    }

    public static void showToast(String title, String message, Pos pos) {
        showToast(title, message, null, 3.0D, pos, null, null, true, false);
    }

    public static void showToast(String title, String message, Node graphic, double hideTime, Pos pos,
                                 EventHandler<ActionEvent> onAction, Object owner, boolean isHideCloseButton, boolean isDarkStyle) {
        Notifications notificationBuilder = FxNotifications.notifications(Duration.seconds(hideTime), pos).title(title)
            .text(message).graphic(graphic).onAction(onAction);
        if (owner != null) {
            notificationBuilder.owner(owner);
        }

        if (isHideCloseButton) {
            notificationBuilder.hideCloseButton();
        }

        if (isDarkStyle) {
            notificationBuilder.darkStyle();
        }

        Platform.runLater(notificationBuilder::show);
    }
}
