package com.tlcsdm.frame.service;

import com.tlcsdm.frame.Sample;
import com.tlcsdm.frame.model.SampleTreeViewModel;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.util.Callback;

/**
 * 左侧菜单树配置CellFactory
 */
public interface SamplesTreeViewConfiguration {

    Callback<TreeView<Sample>, TreeCell<Sample>> cellFactory();

    void configSampleTreeView(SampleTreeViewModel sampleTreeViewModel);
}
