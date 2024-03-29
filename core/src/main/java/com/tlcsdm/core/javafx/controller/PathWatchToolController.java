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

package com.tlcsdm.core.javafx.controller;

import com.tlcsdm.core.javafx.bind.TextInputControlEmptyBinding;
import com.tlcsdm.core.javafx.service.PathWatchToolService;
import com.tlcsdm.core.javafx.util.FileChooserUtil;
import com.tlcsdm.core.javafx.view.PathWatchToolView;
import com.tlcsdm.core.util.I18nUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Folder monitoring tool
 *
 * @author unknowIfGuestInDream
 */
public class PathWatchToolController extends PathWatchToolView {
    private final PathWatchToolService pathWatchToolService = new PathWatchToolService(this);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initView();
        initEvent();
    }

    private void initView() {
        // Do nothing
    }

    private void initEvent() {
        FileChooserUtil.setOnDrag(watchPathTextField, FileChooserUtil.FileType.FOLDER);
        watchButton.disableProperty().bind(new TextInputControlEmptyBinding(watchPathTextField).build());
        BooleanBinding watchButtonStatusBinding = Bindings.createBooleanBinding(
            () -> !watchButton.isDisabled()
                && I18nUtils.get("core.menubar.setting.pathWatch.button.stopWatch").equals(watchButton.getText()),
            watchButton.textProperty());
        watchPathTextField.disableProperty().bind(watchButtonStatusBinding);
        watchPathButton.disableProperty().bind(watchButtonStatusBinding);
        isShowNotificationCheckBox.disableProperty().bind(watchButtonStatusBinding);
        fileNameContainsTextField.disableProperty().bind(watchButtonStatusBinding);
        fileNameNotContainsTextField.disableProperty().bind(watchButtonStatusBinding);
        fileNameSupportRegexCheckBox.disableProperty().bind(watchButtonStatusBinding);
        folderPathContainsTextField.disableProperty().bind(watchButtonStatusBinding);
        folderPathNotContainsTextField.disableProperty().bind(watchButtonStatusBinding);
        folderPathSupportRegexCheckBox.disableProperty().bind(watchButtonStatusBinding);
    }

    @FXML
    private void watchPathAction(ActionEvent event) {
        File file = FileChooserUtil.chooseDirectory();
        if (file != null) {
            watchPathTextField.setText(file.getPath());
        }
    }

    @FXML
    private void watchAction(ActionEvent event) {
        String watch = I18nUtils.get("core.menubar.setting.pathWatch.button.watch");
        if (watch.equals(watchButton.getText())) {
            if (pathWatchToolService.watchAction()) {
                watchButton.setText(I18nUtils.get("core.menubar.setting.pathWatch.button.stopWatch"));
            }
        } else {
            pathWatchToolService.stopWatchAction();
            watchButton.setText(watch);
        }
    }

}
