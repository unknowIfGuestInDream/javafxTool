/*
 * Copyright (c) 2023 unknowIfGuestInDream
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

package com.tlcsdm.jfxcommon.tools;

import cn.hutool.core.util.StrUtil;
import com.tlcsdm.core.javafx.control.FxTextInput;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.FxmlUtil;
import com.tlcsdm.core.util.CoreUtil;
import com.tlcsdm.core.util.FreemarkerUtil;
import com.tlcsdm.jfxcommon.CommonSample;
import com.tlcsdm.jfxcommon.util.CommonConstant;
import com.tlcsdm.jfxcommon.util.I18nUtils;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * 数据格式转换.
 *
 * @author unknowIfGuestInDream
 * @since 1.0.1
 */
public class DataFormatConvert extends CommonSample implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeUserDataBindings();
        initializeBindings();
        initializeUserData();

        initializeUI();
    }

    private void initializeUI() {
    }

    @Override
    public void initializeBindings() {
        super.initializeBindings();
    }

    @Override
    public void initializeUserDataBindings() {
        super.initializeUserDataBindings();
    }

    @Override
    public Node getPanel(Stage stage) {
        FXMLLoader fxmlLoader = FxmlUtil.loadFxmlFromResource(
            DataFormatConvert.class.getResource("/com/tlcsdm/jfxcommon/fxml/dataFormatConvert.fxml"),
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
        return "dataFormatConvert";
    }

    @Override
    public String getSampleName() {
        return "DataFormatConvert";
    }

    @Override
    public String getSampleVersion() {
        return "1.0.1";
    }

    @Override
    public String getSampleDescription() {
        return "";
    }

    @Override
    public ImageView getSampleImageIcon() {
        return LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/jfxcommon/static/icon/data.png"));
    }

    @Override
    public String getOrderKey() {
        return "dataFormatConvert";
    }

    /**
     * 此结果在开发环境和打包后执行结果不同，原因是打包后执行SPI配置时需要从应用模块的配置中读取.
     * 但在开发环境由于模块化原因即使SPI配置中未配置实现也可以读取到.
     */
    @Override
    public boolean isVisible() {
        if (!CoreUtil.hasClass("freemarker.cache.TemplateLoader")) {
            return false;
        }
        TemplateLoader templateLoader = FreemarkerUtil.configuration().getTemplateLoader();
        if (templateLoader instanceof MultiTemplateLoader loader) {
            for (int i = 0; i < loader.getTemplateLoaderCount(); i++) {
                if (loader.getTemplateLoader(i) instanceof ClassTemplateLoader l) {
                    if (l.getBasePackagePath().contains(CommonConstant.FREEMARKER_BASE_PACKAGE_PATH)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
