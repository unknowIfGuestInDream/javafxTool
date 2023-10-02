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

package com.tlcsdm.core.javafx.util;

import cn.hutool.core.io.FileUtil;
import com.tlcsdm.core.javafx.helper.DropContentHelper;
import com.tlcsdm.core.util.I18nUtils;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author unknowIfGuestInDream
 * @date 2023/7/9 14:12
 */
public class FileChooserUtil {
    public static final File HOME_DIRECTORY = FileSystemView.getFileSystemView().getHomeDirectory();

    private FileChooserUtil() {
    }

    public static List<File> chooseFiles() {
        return chooseFiles((FileChooser.ExtensionFilter[]) null);
    }

    public static List<File> chooseFiles(FileChooser.ExtensionFilter... extensionFilter) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(I18nUtils.get("core.util.fileChooser.title"));
        fileChooser.setInitialDirectory(HOME_DIRECTORY);
        if (extensionFilter != null) {
            fileChooser.getExtensionFilters().addAll(extensionFilter);
        }

        return fileChooser.showOpenMultipleDialog(null);
    }

    public static File chooseFile() {
        return chooseFile((FileChooser.ExtensionFilter[]) null);
    }

    public static File chooseFile(FileChooser.ExtensionFilter... extensionFilter) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(I18nUtils.get("core.util.fileChooser.title"));
        fileChooser.setInitialDirectory(HOME_DIRECTORY);
        if (extensionFilter != null) {
            fileChooser.getExtensionFilters().addAll(extensionFilter);
        }

        return fileChooser.showOpenDialog(null);
    }

    public static File chooseSaveFile(FileChooser.ExtensionFilter... extensionFilter) {
        return chooseSaveFile(null, extensionFilter);
    }

    public static File chooseSaveFile(String fileName) {
        return chooseSaveFile(fileName, (FileChooser.ExtensionFilter[]) null);
    }

    public static File chooseSaveFile(String fileName, FileChooser.ExtensionFilter... extensionFilter) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(HOME_DIRECTORY);
        if (fileName != null) {
            fileChooser.setInitialFileName(fileName);
        }

        if (extensionFilter != null) {
            fileChooser.getExtensionFilters().addAll(extensionFilter);
        }

        return fileChooser.showSaveDialog(null);
    }

    public static File chooseSaveCommonImageFile(String fileName) {
        return chooseSaveFile(fileName, new FileChooser.ExtensionFilter("All Images", "*.*"), new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("PNG", "*.png"), new FileChooser.ExtensionFilter("GIF", "*.gif"), new FileChooser.ExtensionFilter("JPEG", "*.jpeg"), new FileChooser.ExtensionFilter("BMP", "*.bmp"));
    }

    public static File chooseSaveImageFile(String fileName) {
        return chooseSaveFile(fileName, new FileChooser.ExtensionFilter("All Images", "*.*"), new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("PNG", "*.png"), new FileChooser.ExtensionFilter("gif", "*.gif"), new FileChooser.ExtensionFilter("jpeg", "*.jpeg"), new FileChooser.ExtensionFilter("bmp", "*.bmp"), new FileChooser.ExtensionFilter("ICO", "*.ico"), new FileChooser.ExtensionFilter("RGBE", "*.rgbe"));
    }

    public static File chooseDirectory() {
        return chooseDirectory(null);
    }

    public static File chooseDirectory(File initialDirectory) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        if (initialDirectory != null) {
            directoryChooser.setInitialDirectory(initialDirectory);
        }

        return directoryChooser.showDialog(null);
    }

    public static void setOnDrag(TextField textField, FileChooserUtil.FileType fileType) {
        DropContentHelper.accept(textField, (dragboard) -> {
            if (dragboard.hasFiles()) {
                Stream<File> fileStream = dragboard.getFiles().stream();
                Objects.requireNonNull(fileType);
                if (fileStream.anyMatch(fileType::match)) {
                    return true;
                }
            }
            return false;
        }, (__, dragboard) -> {
            Stream<File> fileStream = dragboard.getFiles().stream();
            Objects.requireNonNull(fileType);
            textField.setText(fileStream.filter(fileType::match).map(File::getAbsolutePath).findFirst().orElse(""));
        });
    }

    public static void setOnDragByOpenFile(TextInputControl textField) {
        DropContentHelper.accept(textField, (dragboard) -> dragboard.hasFiles() && dragboard.getFiles().stream().anyMatch(File::isFile), (__, dragboard) -> {
            textField.setText(dragboard.getFiles().stream().filter(File::isFile).map(FileUtil::readUtf8String).findFirst().orElse(""));
        });
    }

    /**
     * FileChooser选择excel文件.
     */
    public static FileChooser.ExtensionFilter excelFilter() {
        return new FileChooser.ExtensionFilter("excel file", "*.xlsx", "*.xls", "*.xlsm");
    }

    public static FileChooser.ExtensionFilter xlsxFilter() {
        return new FileChooser.ExtensionFilter("excel file", "*.xlsx");
    }

    /**
     * FileChooser选择xml文件.
     */
    public static FileChooser.ExtensionFilter xmlFilter() {
        return new FileChooser.ExtensionFilter("Xml file", "*.xml");
    }

    /**
     * FileChooser选择json文件.
     */
    public static FileChooser.ExtensionFilter jsonFilter() {
        return new FileChooser.ExtensionFilter("Json file", "*.json");
    }

    /**
     * FileChooser选择csv文件.
     */
    public static FileChooser.ExtensionFilter csvFilter() {
        return new FileChooser.ExtensionFilter("CSV file", "*.csv");
    }

    public enum FileType {
        /**
         * file
         */
        FILE,
        /**
         * folder
         */
        FOLDER;

        public boolean match(File file) {
            return this == FILE && file.isFile() || this == FOLDER && file.isDirectory();
        }
    }
}
