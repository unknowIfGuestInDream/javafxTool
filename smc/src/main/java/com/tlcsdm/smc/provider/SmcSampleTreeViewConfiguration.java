package com.tlcsdm.smc.provider;

import com.tlcsdm.frame.Sample;
import com.tlcsdm.frame.model.SampleTreeViewModel;
import com.tlcsdm.frame.service.SamplesTreeViewConfiguration;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.util.Callback;

public class SmcSampleTreeViewConfiguration implements SamplesTreeViewConfiguration {
    @Override
    public Callback<TreeView<Sample>, TreeCell<Sample>> cellFactory() {
        return null;
    }

    @Override
    public void configSampleTreeView(SampleTreeViewModel sampleTreeViewModel) {
        // Do nothing
    }
}
