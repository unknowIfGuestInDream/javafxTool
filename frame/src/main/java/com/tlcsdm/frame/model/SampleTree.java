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

package com.tlcsdm.frame.model;

import com.tlcsdm.frame.Sample;
import javafx.scene.control.TreeItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author unknowIfGuestInDream
 */
public class SampleTree {
    private final TreeNode root;

    private int count = 0;

    public SampleTree(Sample rootSample) {
        root = new TreeNode(null, null, rootSample, 0);
    }

    public TreeNode getRoot() {
        return root;
    }

    public Object size() {
        return count;
    }

    public void addSample(String[] packages, Sample sample) {
        if (packages.length == 0) {
            root.addSample(sample);
            return;
        }

        TreeNode n = root;
        for (String packageName : packages) {
            if (n.containsChild(packageName)) {
                n = n.getChild(packageName);
            } else {
                TreeNode newNode = new TreeNode(packageName, n.getDepth() + 1);
                n.addNode(newNode);
                n = newNode;
            }
        }

        if (n.packageName.equals(packages[packages.length - 1])) {
            n.addSample(sample);
            count++;
        }
    }

    @Override
    public String toString() {
        return root.toString();
    }

    public static class TreeNode {
        private final Sample sample;
        private final String packageName;
        private final TreeNode parent;
        private final List<TreeNode> children;
        private final int depth;

        public TreeNode(String packageName, int depth) {
            this(null, packageName, null, depth);
        }

        public TreeNode(TreeNode parent, String packageName, Sample sample, int depth) {
            this.children = new ArrayList<>();
            this.sample = sample;
            this.parent = parent;
            this.packageName = packageName;
            this.depth = depth;
        }

        public boolean containsChild(String packageName) {
            if (packageName == null) {
                return false;
            }

            for (TreeNode n : children) {
                if (packageName.equals(n.packageName)) {
                    return true;
                }
            }
            return false;
        }

        public TreeNode getChild(String packageName) {
            if (packageName == null) {
                return null;
            }

            for (TreeNode n : children) {
                if (packageName.equals(n.packageName)) {
                    return n;
                }
            }
            return null;
        }

        public void addSample(Sample sample) {
            children.add(new TreeNode(this, null, sample, depth + 1));
        }

        public void addNode(TreeNode n) {
            children.add(n);
        }

        public Sample getSample() {
            return sample;
        }

        public TreeNode getParent() {
            return parent;
        }

        public String getPackageName() {
            return packageName;
        }

        public int getDepth() {
            return depth;
        }

        public TreeItem<Sample> createTreeItem() {
            TreeItem<Sample> treeItem = null;

            if (sample != null) {
                treeItem = new TreeItem<Sample>(sample);
            } else if (packageName != null) {
                treeItem = new TreeItem<Sample>(new EmptySample(packageName));
            }

            treeItem.setExpanded(true);

            // recursively add in children
            for (TreeNode n : children) {
                if (n.getDepth() > 2) {
                    treeItem.setExpanded(false);
                }
                // if(StrUtil.isNotEmpty(n.getPackageName()) && )
                treeItem.getChildren().add(n.createTreeItem());
            }

            return treeItem;
        }

        @Override
        public String toString() {
            if (sample != null) {
                return " Sample [ sampleName: " + sample.getSampleName() + ", children: " + children + " ]";
            } else {
                return " Sample [ packageName: " + packageName + ", children: " + children + " ]";
            }
        }
    }
}
