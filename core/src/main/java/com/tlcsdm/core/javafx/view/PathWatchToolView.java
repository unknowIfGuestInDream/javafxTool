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

package com.tlcsdm.core.javafx.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * 文件夹监控工具
 *
 * @author unknowIfGuestInDream
 */
public abstract class PathWatchToolView implements Initializable {
    @FXML
    protected TextField watchPathTextField;
    @FXML
    protected Button watchPathButton;
    @FXML
    protected CheckBox isShowNotificationCheckBox;
    @FXML
    protected Button watchButton;
    @FXML
    protected TextField fileNameContainsTextField;
    @FXML
    protected TextField fileNameNotContainsTextField;
    @FXML
    protected CheckBox fileNameSupportRegexCheckBox;
    @FXML
    protected TextField folderPathContainsTextField;
    @FXML
    protected TextField folderPathNotContainsTextField;
    @FXML
    protected CheckBox folderPathSupportRegexCheckBox;
    @FXML
    protected TextArea watchLogTextArea;

    public TextField getWatchPathTextField() {
        return watchPathTextField;
    }

    public Button getWatchPathButton() {
        return watchPathButton;
    }

    public CheckBox getIsShowNotificationCheckBox() {
        return isShowNotificationCheckBox;
    }

    public Button getWatchButton() {
        return watchButton;
    }

    public TextField getFileNameContainsTextField() {
        return fileNameContainsTextField;
    }

    public TextField getFileNameNotContainsTextField() {
        return fileNameNotContainsTextField;
    }

    public CheckBox getFileNameSupportRegexCheckBox() {
        return fileNameSupportRegexCheckBox;
    }

    public TextField getFolderPathContainsTextField() {
        return folderPathContainsTextField;
    }

    public TextField getFolderPathNotContainsTextField() {
        return folderPathNotContainsTextField;
    }

    public CheckBox getFolderPathSupportRegexCheckBox() {
        return folderPathSupportRegexCheckBox;
    }

    public TextArea getWatchLogTextArea() {
        return watchLogTextArea;
    }
}
