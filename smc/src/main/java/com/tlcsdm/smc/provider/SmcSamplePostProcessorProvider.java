package com.tlcsdm.smc.provider;

import java.util.HashSet;
import java.util.Set;

import com.tlcsdm.frame.service.SamplePostProcessorService;

import cn.hutool.core.lang.tree.TreeNode;

public class SmcSamplePostProcessorProvider implements SamplePostProcessorService {

    @Override
    public void postProcessBeanFactory() {
        Set<String> set = new HashSet<>();
        SamplePostProcessorService.Samples.forEach(s -> {
            TreeNode node = new TreeNode<>();
            // id为空处理
            node.setId(s.getValue().getProjectName() + "." + s.getValue().getSampleId());
            node.setWeight(s.getValue().getOrderKey());
            System.out.println(s.getChildren());
        });

    }

//    private TreeItem<Sample> createTreeItem() {
//        TreeItem<Sample> treeItem = null;
//
//        if (sample != null) {
//            treeItem = new TreeItem<Sample>(sample);
//        } else if (packageName != null) {
//            treeItem = new TreeItem<Sample>(new EmptySample(packageName));
//        }
//
//        treeItem.setExpanded(true);
//
//        // recursively add in children
//        for (TreeNode n : children) {
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
