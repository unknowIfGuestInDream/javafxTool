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

package com.tlcsdm.core.javafx.dialog;

import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.util.JavaFxSystemUtil;
import com.tlcsdm.core.logging.logback.ConsoleLogAppender;
import com.tlcsdm.core.util.I18nUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * 日志输出控制台
 * 配合logback使用
 * <pre><code>
 *     <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
 *         <encoder>
 *             <pattern>%d{HH:mm:ss.SSS} [%-5level] [%thread] %logger{3600} - %msg%n</pattern>
 *         </encoder>
 *     </appender>
 *
 *         <root>
 *         <appender-ref ref="CONSOLELOGAPPENDER"/>
 *     </root>
 * </code></pre>
 *
 * @author: unknowIfGuestInDream
 * @date: 2023/3/27 21:07
 */
public class LogConsoleDialog {

    public static void addLogConsole() {
        TextArea textArea = new TextArea();
        textArea.setFocusTraversable(true);
        ConsoleLogAppender.textAreaList.add(textArea);
        Stage newStage = new Stage();
        VBox dialogContainer = new VBox(textArea);
        VBox.setVgrow(textArea, Priority.ALWAYS);
        dialogContainer.setPadding(new Insets(5.0D));
        dialogContainer.setSpacing(5.0D);
        double[] screenSize = JavaFxSystemUtil.getScreenSizeByScale(0.54D, 0.6D);
        newStage.setTitle(I18nUtils.get("core.dialog.logConsole.title"));
        newStage.setScene(new Scene(dialogContainer, screenSize[0], screenSize[1]));
        newStage.setResizable(true);
        if (FxApp.appIcon != null) {
            newStage.getIcons().add(FxApp.appIcon);
        }
        newStage.initModality(Modality.NONE);
        newStage.show();
        newStage.setOnCloseRequest(event1 -> {
            ConsoleLogAppender.textAreaList.remove(textArea);
        });
    }
}
