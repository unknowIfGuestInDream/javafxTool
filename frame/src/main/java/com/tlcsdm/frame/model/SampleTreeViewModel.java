package com.tlcsdm.frame.model;

import com.tlcsdm.frame.Sample;
import javafx.scene.control.TreeView;

public record SampleTreeViewModel(TreeView<Sample> samplesTreeView) {
}
