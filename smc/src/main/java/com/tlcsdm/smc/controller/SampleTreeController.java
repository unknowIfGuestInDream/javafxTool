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

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.tlcsdm.smc.provider.SmcSamplePostProcessorProvider;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.controlsfx.control.CheckTreeView;

import java.net.URL;
import java.util.Comparator;
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
    protected CheckTreeView<Tree<String>> sampleTree;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名 都要默认值的
        treeNodeConfig.setIdKey("rid");
        //转换器
        List<Tree<String>> treeNodes = TreeUtil.build(SmcSamplePostProcessorProvider.getSampleNodeList(),
                SmcSamplePostProcessorProvider.SAMPLES_ROOTID, treeNodeConfig,
                (treeNode, tree) -> {
                    tree.setId(treeNode.getId());
                    tree.setParentId(treeNode.getParentId());
                    tree.setName(treeNode.getName());
                    tree.putExtra("depth", treeNode.getExtra().get(SmcSamplePostProcessorProvider.SAMPLES_DEPTH));
                    tree.putExtra("order", treeNode.getWeight());
                });
        treeNodes = treeNodes.stream().sorted(Comparator.comparing(o -> o.get("order").toString())).toList();
        Tree<String> rootTree = new Tree<>();
        rootTree.setId(SmcSamplePostProcessorProvider.SAMPLES_ROOTID);
        CheckBoxTreeItem<Tree<String>> root = new CheckBoxTreeItem<>(rootTree);
        for (Tree<String> tree : treeNodes) {
            root.getChildren().add(buildTreeItem(tree));
        }
        buildTreeItem(rootTree);
        root.setExpanded(true);
        sampleTree.setRoot(root);
        sampleTree.setShowRoot(false);
        sampleTree.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        sampleTree.setMinWidth(300);
        sampleTree.setMaxWidth(400);
        sampleTree.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<TreeItem<Tree<String>>>() {
            @Override
            public void onChanged(Change<? extends TreeItem<Tree<String>>> change) {
                System.out.println(change.getList());
            }
        });

        sampleTree.getCheckModel().getCheckedItems().addListener(new ListChangeListener<TreeItem<Tree<String>>>() {
            @Override
            public void onChanged(Change<? extends TreeItem<Tree<String>>> change) {
                System.out.println(change.getList());

                while (change.next()) {
                    System.out.println("============================================");
                    System.out.println("Change: " + change);
                    System.out.println("Added sublist " + change.getAddedSubList());
                    System.out.println("Removed sublist " + change.getRemoved());
                    System.out.println("List " + change.getList());
                    System.out.println("Added " + change.wasAdded() + " Permutated " + change.wasPermutated() + " Removed " + change.wasRemoved() + " Replaced "
                            + change.wasReplaced() + " Updated " + change.wasUpdated());
                    System.out.println("============================================");
                }
            }
        });
        sampleTree.setCellFactory(treeTreeView -> new TreeCell<Tree<String>>() {
            @Override
            protected void updateItem(Tree<String> item, boolean empty) {
                if (empty) {
                    setText("");
                } else {
                    setText(item.getName().toString());
                }
            }
        });
    }

    public CheckBoxTreeItem<Tree<String>> buildTreeItem(Tree<String> node) {
        CheckBoxTreeItem<Tree<String>> treeItem = new CheckBoxTreeItem<>(node);
        treeItem.setExpanded(true);
        if (node.hasChild()) {
            for (Tree<String> n : node.getChildren()) {
                treeItem.getChildren().add(buildTreeItem(n));
            }
        }
        return treeItem;
    }
}
