package com.tlcsdm.frame.model;

import com.tlcsdm.frame.Sample;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.util.Callback;

/**
 * 默认CellFactory
 */
public class DefaultTreeViewCellFactory implements Callback<TreeView<Sample>, TreeCell<Sample>> {
    @Override
    public TreeCell<Sample> call(TreeView<Sample> sampleTreeView) {
        return new TreeCell<>() {
            @Override
            protected void updateItem(Sample item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText("");
                } else {
                    setText(item.getSampleName());
                }
            }
        };
    }
}
