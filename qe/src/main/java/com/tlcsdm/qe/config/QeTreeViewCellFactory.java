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

package com.tlcsdm.qe.config;

import com.tlcsdm.core.javafx.helper.ImageViewHelper;
import com.tlcsdm.frame.Sample;
import com.tlcsdm.frame.model.AbstractTreeViewCellFactory;
import com.tlcsdm.frame.model.EmptySample;
import com.tlcsdm.qe.util.QeConstant;
import javafx.beans.binding.Bindings;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;

/**
 * @author unknowIfGuestInDream
 */
public class QeTreeViewCellFactory extends AbstractTreeViewCellFactory {

    @Override
    public void setupImageBinding(TreeCell<Sample> treeCell) {
        treeCell.graphicProperty().bind(Bindings.createObjectBinding(() -> {
            final Sample item = treeCell.getItem();
            if (item == null) {
                return null;
            }
            if (item instanceof EmptySample emptySample) {
                return switch (item.getSampleName()) {
                    case "Qe", "Common" -> ImageViewHelper.get("folder");
                    case "Tools" -> ImageViewHelper.get("tools");
                    default -> emptySample.getSampleImageIcon();
                };
            }
            return item.getSampleImageIcon();
        }, treeCell.itemProperty()));
    }

    @Override
    public void setupContextMenu(TreeCell<Sample> treeCell, TreeView<Sample> sampleTreeView) {
        final ContextMenu sampleContextMenu = new ContextMenu();
        final MenuItem settingExport = createSettingExport(treeCell, QeConstant.PROJECT_NAME);

        treeCell.itemProperty().addListener((obs, old, newv) -> {
            sampleContextMenu.getItems().clear();
            if (newv != null) {
                sampleContextMenu.getItems().addAll(settingExport);
            }
        });

        treeCell.setContextMenu(sampleContextMenu);
    }

}
