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
import com.tlcsdm.core.javafx.richtext.InformationArea;
import com.tlcsdm.core.javafx.richtext.PropertiesArea;
import com.tlcsdm.core.util.I18nUtils;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.InlineCssTextArea;
import org.fxmisc.richtext.LineNumberFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

/**
 * Property弹窗
 *
 * @author unknowIfGuestInDream
 */
public class PropertiesDialog {

    private PropertiesDialog() {
    }

    public static void openPropertiesDialog() {
        VBox vbox = new VBox();
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getStyleClass().add(TabPane.STYLE_CLASS_FLOATING);

        InformationArea overviewArea = new InformationArea();
        overviewArea.setEditable(false);
        overviewArea.setStyle("-fx-font-size: 14;-fx-padding: 5 0 0 5;");
        buildOverviewArea(overviewArea);
        VirtualizedScrollPane<InformationArea> overviewPane = new VirtualizedScrollPane<>(overviewArea);

        InlineCssTextArea jvmArea = new InlineCssTextArea();
        jvmArea.setEditable(false);
        jvmArea.setStyle("-fx-font-size: 14;-fx-padding: 5 0 0 5;");
        jvmArea.setParagraphGraphicFactory(LineNumberFactory.get(jvmArea));
        buildJvmArea(jvmArea);
        VirtualizedScrollPane<InlineCssTextArea> jvmPane = new VirtualizedScrollPane<>(jvmArea);

        PropertiesArea sysArea = new PropertiesArea();
        sysArea.setEditable(false);
        buildSysArea(sysArea);
        VirtualizedScrollPane<PropertiesArea> sysPane = new VirtualizedScrollPane<>(sysArea);

        Tab overviewTab = new Tab("Overview");
        overviewTab.setContent(overviewPane);

        Tab jvmTab = new Tab("JVM");
        jvmTab.setContent(jvmPane);

        Tab sysTab = new Tab("System");
        sysTab.setContent(sysPane);

        tabPane.getTabs().setAll(overviewTab, jvmTab, sysTab);

        vbox.getChildren().addAll(tabPane);
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        FxDialog<VBox> dialog = new FxDialog<VBox>().setTitle(
                I18nUtils.get("core.menubar.setting.systemProperties")).setOwner(FxApp.primaryStage)
            .setPrefSize(680, 540).setResizable(true).setBody(vbox)
            .setButtonTypes(FxButtonType.CLOSE);
        dialog.setButtonHandler(FxButtonType.CLOSE, (e, s) -> s.close());
        dialog.show();
    }

    private static void buildOverviewArea(InformationArea area) {
        // get name representing the running Java virtual machine.
        String name = ManagementFactory.getRuntimeMXBean().getName();
        // get pid
        String pid = name.split("@")[0];
        // 基本JVM信息
        String jvmName = ManagementFactory.getRuntimeMXBean().getVmName();
        String jvmVersion = System.getProperty("java.vm.version");
        String jvmVendor = System.getProperty("java.vm.vendor");
        String javaHome = System.getProperty("java.home");
        String fileEncoding = Charset.defaultCharset().displayName();
        String workingDir = System.getProperty("user.dir");
        // 运行时信息
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        // 获取JVM启动时间（毫秒）
        long startTime = runtimeMXBean.getStartTime();
        LocalDateTime startDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(startTime), ZoneId.systemDefault());
        // 获取JVM运行时间（毫秒）
        long uptime = runtimeMXBean.getUptime();
        Duration duration = Duration.ofMillis(uptime);
        String runTime = "%d Day %d Hour %d Min %d s".formatted(duration.toDays(), duration.toHours() % 24,
            duration.toMinutes() % 60, duration.getSeconds() % 60);

        area.appendText("PID: " + pid);
        area.appendText(System.lineSeparator());
        area.appendText("JVM: " + jvmName + " (" + System.getProperty("java.vm.info") + ")");
        area.appendText(System.lineSeparator());
        area.appendText("Java: " + jvmVendor + " " + jvmVersion);
        area.appendText(System.lineSeparator());
        area.appendText("Java Home: " + javaHome);
        area.appendText(System.lineSeparator());
        area.appendText("JVM StartUpTime: " + startDateTime);
        area.appendText(System.lineSeparator());
        area.appendText("JVM Run time: " + runTime);
        area.appendText(System.lineSeparator());
        area.appendText("Program Path: " + workingDir);
        area.appendText(System.lineSeparator());
        area.appendText("Encoding: " + fileEncoding);
    }

    private static void buildJvmArea(InlineCssTextArea area) {
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        List<String> arguments = runtimeMxBean.getInputArguments();
        for (String arg : arguments) {
            area.appendText(arg);
            area.appendText(System.lineSeparator());
        }
    }

    private static void buildSysArea(PropertiesArea area) {
        Map<String, String> env = System.getenv();
        env.forEach((key, value) -> {
            area.appendText(key + " = " + value);
            area.appendText(System.lineSeparator());
        });
    }

}
