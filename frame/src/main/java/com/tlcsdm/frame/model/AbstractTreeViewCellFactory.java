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

package com.tlcsdm.frame.model;

import com.tlcsdm.core.javafx.controlsfx.FxAction;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.javafx.util.FxXmlHelper;
import com.tlcsdm.core.util.I18nUtils;
import com.tlcsdm.frame.Sample;
import javafx.beans.binding.Bindings;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTreeViewCellFactory implements Callback<TreeView<Sample>, TreeCell<Sample>> {
    @Override
    public TreeCell<Sample> call(TreeView<Sample> sampleTreeView) {
        TreeCell<Sample> treeCell = getTreeCell();
        setupImageBinding(treeCell);
        setupTooltipBinding(treeCell);
        setupContextMenu(treeCell, sampleTreeView);
        return treeCell;
    }

    protected TreeCell<Sample> getTreeCell() {
        return new TreeCell<>() {
            @Override
            protected void updateItem(Sample item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText("");
                } else {
                    setText(item.getSampleName());
                }
            }
        };
    }

    protected void setupImageBinding(TreeCell<Sample> treeCell) {
        //Do nothing
    }

    protected void setupTooltipBinding(TreeCell<Sample> treeCell) {
        final Tooltip tooltip = new Tooltip();
        tooltip.textProperty().bind(Bindings.createStringBinding(() -> {
            final Sample sample = treeCell.getItem();
            if (sample == null) {
                return "";
            }
            return sample.getSampleName();
        }, treeCell.itemProperty()));
        treeCell.tooltipProperty()
            .bind(Bindings.when(treeCell.itemProperty().isNotNull()).then(tooltip).otherwise((Tooltip) null));
    }

    protected void setupContextMenu(TreeCell<Sample> treeCell, TreeView<Sample> classes) {
        //Do nothing
    }

    /**
     * 导出设置右键菜单
     */
    protected MenuItem createSettingExport(TreeCell<Sample> treeCell, String projectName) {
        final MenuItem settingExport = new MenuItem();
        settingExport.setGraphic(LayoutHelper.iconView(FxAction.class.getResource("/com/tlcsdm/core/static/icon/export.png")));
        settingExport.textProperty().bind(Bindings.createStringBinding(() -> {
            final Sample sample = treeCell.getItem();
            if (sample == null) {
                return "";
            }
            return I18nUtils.get("core.button.export");
        }, treeCell.itemProperty()));

        settingExport.setOnAction(e -> {
            final Sample sample = treeCell.getItem();
            if (sample == null) {
                return;
            }
            List<String> sampleList = new ArrayList<>();
            if (sample instanceof EmptySample) {
                treeCell.getTreeItem().getChildren().forEach(w -> {
                    walkSample(w, sampleList);
                });
            } else {
                sampleList.add(sample.getSampleXmlPrefix());
            }
            FxXmlHelper.exportData(projectName, sampleList);
        });
        return settingExport;
    }

    /**
     * 获取选择项下的sample的xml配置前缀
     */
    private void walkSample(TreeItem<Sample> item, List<String> sampleList) {
        if (item.isLeaf()) {
            sampleList.add(item.getValue().getSampleXmlPrefix());
        } else {
            for (TreeItem<Sample> t : item.getChildren()) {
                walkSample(t, sampleList);
            }
        }
    }

}
