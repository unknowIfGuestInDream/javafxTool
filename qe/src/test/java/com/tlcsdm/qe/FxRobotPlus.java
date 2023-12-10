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

import cn.hutool.log.StaticLog;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import org.testfx.api.FxRobot;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class FxRobotPlus extends FxRobot {

    private final FxRobot fxRobot;

    public FxRobotPlus(FxRobot fxRobot) {
        this.fxRobot = fxRobot;
    }

    public void selectContextMenu(ContextMenu contextMenu, String action) {
        StaticLog.debug("Selecting context menu item with {} in {}", action, contextMenu);
        waitUntil(() -> {
            final MenuItem menuItem = getMenuItem(contextMenu, action);
            fire(menuItem);
        }, 5000);
    }

    public void selectContextMenu(Control control, String action) {
        StaticLog.debug("Selecting context menu item with {} in {}", action, control);
        waitUntil(() -> {
            final MenuItem menuItem = getMenuItem(control.getContextMenu(), action);
            fire(menuItem);
        }, 5000);
    }

    public <T> void selectContextMenu(ListView<T> listView, String action) {
        final T selected = listView.getSelectionModel().getSelectedItem();
        StaticLog.debug("Selecting context menu item with {} in {} (selectedItem={})", action, listView, selected);
        waitUntil(() -> {
            listView.scrollTo(selected);
            final MenuItem menuItem = listView.lookupAll(".cell")
                .stream()
                .map(n -> (ListCell<T>) n)
                .filter(cell -> selected == null || selected.equals(cell.getItem()))
                .map(cell -> getMenuItem(cell.getContextMenu(), action))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow();
            fire(menuItem);
        }, 5000);
    }

    public <T> void selectContextMenu(ListView<T> listView, Predicate<T> cellSelector, String action) {
        StaticLog.debug("Selecting context menu item with {} in {}", action, listView);
        waitUntil(() -> {
            final MenuItem menuItem = listView.lookupAll(".cell")
                .stream()
                .map(n -> (ListCell<T>) n)
                .filter(cell -> cellSelector.test(cell.getItem()))
                .map(cell -> getMenuItem(cell.getContextMenu(), action))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow();
            fire(menuItem);
        }, 5000);
    }

    public <T> void selectContextMenu(TreeView<T> treeView, String action) {
        final TreeItem<T> selected = treeView.getSelectionModel().getSelectedItem();
        StaticLog.debug("Selecting context menu item with {} in {} (selectedItem={})", action, treeView, selected);
        waitUntil(() -> {
            final int selectedIndex = treeView.getSelectionModel().getSelectedIndex();
            treeView.scrollTo(selectedIndex);
            final MenuItem menuItem = treeView.lookupAll(".cell")
                .stream()
                .map(n -> (TreeCell<T>) n)
                .filter(c -> c.getContextMenu() != null
                    && c.getContextMenu().getItems().size() > 0)
                .filter(cell -> selected == null || selected.getValue()
                    .equals(cell.getItem()))
                .map(treeCell -> getMenuItem(treeCell.getContextMenu(), action))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow();
            fire(menuItem);
        }, 5000);
    }

    private MenuItem getMenuItem(ContextMenu contextMenu, String action) {
        return contextMenu.getItems()
            .stream()
            .filter(m -> m.getText() != null && m.getText().startsWith(action))
            .findFirst()
            .orElse(null);
    }

    private void fire(MenuItem menuItem) {
        if (menuItem instanceof CheckMenuItem) {
            // JavaFX doesn't automatically do this through fire()...
            final CheckMenuItem checkMenuItem = (CheckMenuItem) menuItem;
            checkMenuItem.setSelected(!checkMenuItem.isSelected());
        }
        menuItem.fire();
    }

    public <T> boolean select(ListView<T> listView, String name) {
        final T process = listView.getItems()
            .stream()
            .filter(p -> p.toString().contains(name))
            .findFirst()
            .orElse(null);
        if (process == null) {
            return false;
        }
        listView.getSelectionModel().select(process);
        return true;
    }

    public <T> boolean select(TreeView<T> treeView, String name) {
        final Queue<TreeItem<T>> next = new ArrayDeque<>();
        next.add(treeView.getRoot());
        while (!next.isEmpty()) {
            final TreeItem<T> nextTreeItem = next.poll();
            if (String.valueOf(nextTreeItem.getValue()).contains(name)) {
                treeView.getSelectionModel().select(nextTreeItem);
                return true;
            }
            next.addAll(nextTreeItem.getChildren());
        }
        return false;
    }

    public void waitForExists(String query) {
        waitUntil(() -> fxRobot.lookup(query).tryQuery().isPresent(), 5000);
    }

    public void waitUntil(BooleanSupplier condition, long timeoutMs) throws RuntimeException {
        final long end = System.currentTimeMillis() + timeoutMs;
        while (System.currentTimeMillis() < end) {
            final AtomicBoolean success = new AtomicBoolean(false);
            interact(() -> success.set(condition.getAsBoolean()));
            if (success.get()) {
                return;
            }
            sleep(5);
        }
        throw new IllegalStateException("Condition not reached - follow stack trace to see condition");
    }

    public void waitUntil(Runnable action, long timeoutMs) throws RuntimeException {
        waitUntil(() -> {
            try {
                action.run();
                return true;
            } catch (Exception e) {
                StaticLog.debug("Test attempt failed with {}", e.getClass().getName() + ": " + e.getMessage());
                return false;
            }
        }, timeoutMs);
    }

    public void waitForStageExists(String titleRegex) {
        waitUntil(() -> fxRobot.listWindows()
            .stream()
            .filter(Stage.class::isInstance)
            .map(Stage.class::cast)
            .anyMatch(w -> w.getTitle().matches(titleRegex)), 5000);
    }

    public <T> T waitFor(Supplier<T> supplier, long timeoutMs) throws RuntimeException {
        final long end = System.currentTimeMillis() + timeoutMs;
        while (System.currentTimeMillis() < end) {
            final T result = supplier.get();
            if (result != null) {
                return result;
            }
            sleep(10);
        }
        throw new IllegalStateException("No result found after " + timeoutMs + " ms");
    }

    public <T> void selectComboBox(ComboBox<T> comboBox, String text) {
        waitUntil(() -> {
            final T comboBoxItem = comboBox.getItems()
                .stream()
                .filter(item -> item.toString().contains(text))
                .findFirst()
                .orElseThrow();
            comboBox.getSelectionModel().select(comboBoxItem);
        }, 5000);
    }

}
