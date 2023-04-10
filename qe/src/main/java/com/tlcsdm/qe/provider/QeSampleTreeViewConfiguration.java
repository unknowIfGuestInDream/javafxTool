package com.tlcsdm.qe.provider;

import com.tlcsdm.frame.Sample;
import com.tlcsdm.frame.model.SampleTreeViewModel;
import com.tlcsdm.frame.service.SamplesTreeViewConfiguration;
import com.tlcsdm.qe.config.QeTreeViewCellFactory;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.util.Callback;

public class QeSampleTreeViewConfiguration implements SamplesTreeViewConfiguration {
    @Override
    public Callback<TreeView<Sample>, TreeCell<Sample>> cellFactory() {
        return new QeTreeViewCellFactory();
    }

    @Override
    public void configSampleTreeView(SampleTreeViewModel sampleTreeViewModel) {
        // Do nothing
    }
}
