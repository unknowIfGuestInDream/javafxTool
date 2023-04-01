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

package com.tlcsdm.smc.controller;

import cn.hutool.core.lang.tree.TreeNode;
import com.tlcsdm.frame.Sample;
import com.tlcsdm.frame.service.SamplePostProcessorService;
import com.tlcsdm.smc.provider.SmcSamplePostProcessorProvider;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import org.controlsfx.control.CheckTreeView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author: unknowIfGuestInDream
 * @date: 2023/4/1 8:49
 */
public class SampleTreeController implements Initializable {

    @FXML
    protected Button exportButton;

    @FXML
    protected CheckTreeView<String> sampleTree;

    private CheckTreeView<String> checkTreeView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CheckBoxTreeItem<String> root = new CheckBoxTreeItem<>(SmcSamplePostProcessorProvider.SAMPLES_ROOTID);
        root.setExpanded(true);

        checkTreeView = new CheckTreeView<>(root);
        checkTreeView.setShowRoot(false);
        checkTreeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        checkTreeView.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<TreeItem<String>>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends TreeItem<String>> c) {
                System.out.println("update");
            }
        });
        List<TreeItem<Sample>> list = SamplePostProcessorService.Samples;
        List<TreeNode<String>> sampleList = SmcSamplePostProcessorProvider.getSampleNodeList();
    }

    private CheckBoxTreeItem<String> createTreeItem(TreeNode<String> node) {
        CheckBoxTreeItem<String> treeItem = new CheckBoxTreeItem<>(node.getName().toString());
        treeItem.setExpanded(true);
        return treeItem;
    }

//    public CheckBoxTreeItem<String> createTreeItem() {
//        CheckBoxTreeItem<String> treeItem = new CheckBoxTreeItem<>(node.getName().toString());
//
//        treeItem.setExpanded(true);
//        // recursively add in children
//        for (SampleTree.TreeNode n : children) {
//            if (n.getDepth() > 2) {
//                treeItem.setExpanded(false);
//            }
//            // if(StrUtil.isNotEmpty(n.getPackageName()) && )
//            treeItem.getChildren().add(n.createTreeItem());
//        }
//
//        return treeItem;
//    }
}
