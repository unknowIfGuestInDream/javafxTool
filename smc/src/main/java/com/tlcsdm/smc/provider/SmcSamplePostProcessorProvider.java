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

package com.tlcsdm.smc.provider;

import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.tlcsdm.core.exception.SampleDefinitionException;
import com.tlcsdm.frame.Sample;
import com.tlcsdm.frame.model.EmptySample;
import com.tlcsdm.frame.service.SamplePostProcessorService;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import org.controlsfx.control.CheckTreeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author unknowIfGuestInDream
 */
public class SmcSamplePostProcessorProvider implements SamplePostProcessorService {

    private static final List<TreeNode<String>> sampleNodeList = new ArrayList<>();
    private static CheckBoxTreeItem<String> root = new CheckBoxTreeItem<>();
    private static CheckTreeView<String> sampleTree = null;
    public static String SAMPLES_ROOTID = "parent";
    public static String SAMPLES_DEPTH = "depth";
    public static String SAMPLES_FOLDER = "folder";
    public static String SAMPLES_XMLPREFIX = "xmlPrefix";

    @Override
    public void postProcessBeanFactory() {
        // 防止重启带来的重复数据
        if (sampleNodeList.size() > 0) {
            return;
        }
        SamplePostProcessorService.Samples.forEach(s -> {
            Sample sample = s.getValue();
            TreeNode<String> node = new TreeNode<>();
            node.setId(sample.getSampleName());
            node.setName(sample.getSampleName());
            node.setParentId(SAMPLES_ROOTID);
            node.setWeight(0);
            Map<String, Object> map = new HashMap<>();
            map.put(SAMPLES_DEPTH, 0);
            map.put(SAMPLES_FOLDER, true);
            node.setExtra(map);
            sampleNodeList.add(node);
            CheckBoxTreeItem<String> item = new CheckBoxTreeItem<>(sample.getSampleName());
            item.setExpanded(true);
            root.getChildren().add(item);
            buildTree(node, s.getChildren(), item);
        });
        // 校验是否有重复sampleId
        Set<String> set = new HashSet<>();
        sampleNodeList.forEach(n -> {
            if ((boolean) n.getExtra().get(SAMPLES_FOLDER)) {
                return;
            }
            if (set.contains(n.getId())) {
                throw new SampleDefinitionException("Componment '" + n.getId() + "' Already defined.");
            }
            set.add(n.getId());
        });
    }

    private void buildTree(TreeNode<String> n, ObservableList<TreeItem<Sample>> list, CheckBoxTreeItem<String> item) {
        if (list.isEmpty()) {
            return;
        }
        for (TreeItem<Sample> t : list) {
            Sample sample = t.getValue();
            TreeNode<String> node = new TreeNode<>();
            int depth = (int) n.getExtra().get(SAMPLES_DEPTH);
            Map<String, Object> map = MapUtil.of(SAMPLES_DEPTH, depth + 1);
            if (sample instanceof EmptySample e) {
                node.setId(e.getSampleName());
                node.setName(e.getSampleName());
                node.setParentId(n.getId());
                map.put(SAMPLES_FOLDER, true);
            } else {
                String id = sample.getProjectName();
                if (StrUtil.isNotEmpty(sample.getSampleId())) {
                    id += "." + sample.getSampleId();
                }
                node.setId(id);
                node.setName(sample.getSampleName());
                node.setParentId(n.getId());
                node.setWeight(sample.getOrderKey());
                map.put(SAMPLES_FOLDER, false);
                map.put(SAMPLES_XMLPREFIX, sample.getSampleXmlPrefix());
            }
            node.setExtra(map);
            sampleNodeList.add(node);
            CheckBoxTreeItem<String> subItem = new CheckBoxTreeItem<>(node.getName().toString());
            subItem.setExpanded(true);
            item.getChildren().add(subItem);
            buildTree(node, t.getChildren(), subItem);
        }
    }

    public static List<TreeNode<String>> getSampleNodeList() {
        return sampleNodeList;
    }

    /**
     * <pre>{@code
     *         sampleTree.getCheckModel().getCheckedItems().addListener((ListChangeListener<TreeItem<String>>) change -> {
     *             System.out.println("getCheckedItems");
     *             System.out.println(change.getList());
     *             while (change.next()) {
     *                 System.out.println("============================================");
     *                 System.out.println("Change: " + change);
     *                 System.out.println("Added sublist " + change.getAddedSubList());
     *                 System.out.println("Removed sublist " + change.getRemoved());
     *                 System.out.println("List " + change.getList());
     *                 System.out.println("Added " + change.wasAdded() + " Permutated " + change.wasPermutated() + " Removed " + change.wasRemoved() + " Replaced "
     *                     + change.wasReplaced() + " Updated " + change.wasUpdated());
     *                 System.out.println("============================================");
     *             }
     *         });
     * }</pre>
     *
     * @return
     */
    public static CheckTreeView<String> getSampleTree() {
        if (sampleTree != null) {
            return sampleTree;
        }
        root.setExpanded(true);
        sampleTree = new CheckTreeView<>(root);
        sampleTree.setShowRoot(false);
        sampleTree.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        return sampleTree;
    }

}
