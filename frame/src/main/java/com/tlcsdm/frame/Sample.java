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

package com.tlcsdm.frame;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.log.StaticLog;
import com.tlcsdm.frame.model.EmptySample;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * 组件对象.
 *
 * @author unknowIfGuestInDream
 */
public sealed interface Sample permits EmptySample, SampleBase {

    /**
     * 组件id.
     */
    String getSampleId();

    /**
     * A short, most likely single-word, name to show to the user - e.g. "CheckBox".
     */
    String getSampleName();

    /**
     * A short, multiple sentence description of the sample.
     */
    String getSampleDescription();

    /**
     * Returns the name of the project that this sample belongs to (e.g. 'JFXtras'.
     * or 'ControlsFX').
     */
    String getProjectName();

    /**
     * Returns the version of the project that this sample belongs to (e.g. '1.0.0').
     */
    String getProjectVersion();

    /**
     * sample version.
     */
    String getSampleVersion();

    /**
     * sample icon.
     */
    ImageView getSampleImageIcon();

    /**
     * Returns the main sample panel.
     */
    Node getPanel(final Stage stage);

    /**
     * Returns the panel to display to the user that allows for manipulating
     * the sample.
     */
    Node getControlPanel();

    /**
     * Note that initialize() is called after getPanel().
     * 实现类是FXML实现，推荐不使用此接口初始化，而是需要手动初始化
     */
    void initialize();

    /**
     * Provides a place to dispose of any resources when sample is deselected.
     */
    void dispose();

    /**
     * Returns divider position to use for split between main panel and control panel.
     */
    double getControlPanelDividerPosition();

    /**
     * If true this sample is shown to users, if false it is not.
     */
    boolean isVisible();

    /**
     * 排序字段.
     */
    String getOrderKey();

    /**
     * xml配置key前缀，用于导出等功能使用.
     */
    String getSampleXmlPrefix();

    /**
     * 是否有右侧区域.
     */
    boolean hasControlPanel();

    /**
     * 当前实现类是否是fxml实现，如果是的话初始化需要额外处理。
     */
    default boolean isFxml() {
        Class<?> clazz = getClass();
        // 判断是否实现FXML的Initializable接口
        if (!clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers()) && Initializable.class.isAssignableFrom(clazz)) {
            return true;
        }
        // 通过被@FXML修饰的initialize的实现判断是否时fxml实现
        try {
            Method method = getClass().getMethod("initialize");
            final List<Annotation> annotations = AnnotationUtil.scanMethod(method);
            for (Annotation annotation : annotations) {
                if (annotation instanceof FXML) {
                    return true;
                }
            }
        } catch (NoSuchMethodException e) {
            StaticLog.error(e);
        }
        return false;
    }

}
