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

package com.tlcsdm.qe.tools;

import cn.hutool.core.util.StrUtil;
import com.tlcsdm.core.javafx.control.FxTextInput;
import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.FxmlUtil;
import com.tlcsdm.qe.QeSample;
import com.tlcsdm.qe.util.I18nUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeView;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * 测试用，测试fxml集成
 *
 * @author unknowIfGuestInDream
 */
public class FxmlDemo extends QeSample implements Initializable {

    @FXML
    private SplitPane splitPane;
    @FXML
    private TreeView treeView;

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public Node getPanel(Stage stage) {
        FXMLLoader fxmlLoader = FxmlUtil.loadFxmlFromResource(getClass().getResource("/com/tlcsdm/qe/fxml/light.fxml"),
            ResourceBundle.getBundle(I18nUtils.BASENAME, Config.defaultLocale));
        return fxmlLoader.getRoot();
    }

    @Override
    public Node getControlPanel() {
        String content = """
            """;
        Map<String, String> map = new HashMap<>();
        return FxTextInput.textArea(StrUtil.format(content, map));
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getSampleId() {
        return "fxmlDemo";
    }

    @Override
    public String getSampleName() {
        return "FXML示例";
    }

    @Override
    public String getOrderKey() {
        return "fxmlDemo";
    }

    @Override
    public String getSampleDescription() {
        return "此组件测试用";
    }

    @Override
    public String getSampleVersion() {
        return "1.0.0-Beta";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public SVGPath loadPathToMM(String pathName) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        SVGPath path = new SVGPath();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            //禁止DTD验证,防止网络阻塞
            builder.setEntityResolver(
                (publicId, systemId) -> new InputSource(new StringReader(""))
            );
            Document d = builder.parse(getClass().getResourceAsStream(pathName));
            org.w3c.dom.Node node = d.getElementsByTagName("path").item(0);
            String content = node.getAttributes().getNamedItem("d").getNodeValue();

            path.setContent(content);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

}
