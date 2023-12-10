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

package com.tlcsdm.qe;

import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.TreeView;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.UnaryOperator;
import java.util.jar.JarFile;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;

public class AppHelper {

    private final FxRobotPlus fxRobot;

    public AppHelper(FxRobotPlus fxRobot) {
        this.fxRobot = fxRobot;
    }

    public ListView<?> getJvmList() {
        return fxRobot.lookup("#processes").queryListView();
    }

    public void selectJvm(String jvmName) {
        fxRobot.waitUntil(() -> fxRobot.select(getJvmList(), jvmName), 5000);
    }

    public void selectJvmAction(String action) {
        final ListView<?> jvms = getJvmList();
        fxRobot.selectContextMenu(jvms, action);
    }

    public TreeView<?> getClassTree() {
        return fxRobot.lookup("#classes").queryAs(TreeView.class);
    }

    public void selectClass(String className) {
        fxRobot.waitUntil(() -> fxRobot.select(getClassTree(), className), 5000);
    }

    public TreeView<?> getFieldTree() {
        return fxRobot.lookup("#classFields").queryAs(TreeView.class);
    }

    public void waitUntilDecompile() {
        final String className = getClassTree().getSelectionModel().getSelectedItem().getValue().toString();
        waitUntilJavaCodeContains("class " + className);
    }

    public void waitUntilDisassemble() {
        final CodeArea byteCode = getByteCode();
        final String className = getClassTree().getSelectionModel().getSelectedItem().getValue().toString();
        fxRobot.waitUntil(() -> byteCode.getText().contains("class " + className), 5000);
    }

    public TextInputControl getSearchClasses() {
        return fxRobot.lookup("#searchClasses").queryTextInputControl();
    }

    public void selectClassAction(String action) {
        fxRobot.selectContextMenu(getClassTree(), action);
    }

    public void searchClasses(String text) {
        fxRobot.interact(() -> getSearchClasses().setText(text));
    }

    public File createTempJar() {
        try {
            return File.createTempFile("test", ".jar");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public File createTempClass() {
        try {
            return File.createTempFile("test", ".class");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Stream<String> streamClasses(File jarFile) {
        try {
            return new JarFile(jarFile).stream().filter(j -> !j.isDirectory()).map(ZipEntry::getName);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void waitForAlert(String titleRegex, String containsText) {
        fxRobot.waitUntil((Runnable) () -> fxRobot.targetWindow(titleRegex).lookup(containsText).query(), 5000);
    }

    public boolean isTabChangesPending(int index) {
        final TabPane tabPane = getTabPane();
        return tabPane.getTabs().get(index).getText().endsWith("*");
    }

    public void waitUntilChangesNotPending(int index) {
        fxRobot.waitUntil(() -> !isTabChangesPending(index), 5000);
    }

    public void patchCode(UnaryOperator<String> patcher) {
        final CodeArea classFile = getJavaCode();
        final String updatedText = patcher.apply(classFile.getText());
        fxRobot.interact(() -> classFile.replaceText(updatedText));
    }

    public void patchBytecode(UnaryOperator<String> patcher) {
        final CodeArea byteCode = getByteCode();
        final String updatedText = patcher.apply(byteCode.getText());
        fxRobot.interact(() -> byteCode.replaceText(updatedText));
    }

    public CodeArea getJavaCode() {
        return fxRobot.lookup("#classFile").queryAs(CodeArea.class);
    }

    public CodeArea getByteCode() {
        return fxRobot.lookup("#bytecode").queryAs(CodeArea.class);
    }

    public void saveCodeChanges() {
        selectCodeAction("Save Changes");
    }

    public void saveBytecodeChanges() {
        final CodeArea classFile = getByteCode();
        fxRobot.selectContextMenu(classFile.getContextMenu(), "Save Changes");
    }

    public void resetCodeChanges() {
        selectCodeAction("Reset Changes");
    }

    public void resetBytecodeChanges() {
        final CodeArea classFile = getByteCode();
        fxRobot.selectContextMenu(classFile.getContextMenu(), "Reset Changes");
    }

    public void selectCodeAction(String action) {
        final CodeArea classFile = getJavaCode();
        fxRobot.selectContextMenu(classFile.getContextMenu(), action);
    }

    public TabPane getTabPane() {
        return fxRobot.lookup("#currentClassTabPane").queryAs(TabPane.class);
    }

    public void openTab(int index) {
        final TabPane tabPane = getTabPane();
        tabPane.getSelectionModel().select(index);
    }

    public void waitUntilJavaCodeContains(String text) {
        fxRobot.waitUntil(() -> getJavaCode().getText().contains(text), 5000);
    }

    public void waitUntilByteCodeContains(String text) {
        fxRobot.waitUntil(() -> getByteCode().getText().contains(text), 5000);
    }

}
