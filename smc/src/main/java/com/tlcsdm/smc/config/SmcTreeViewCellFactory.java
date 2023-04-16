package com.tlcsdm.smc.config;

import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.frame.Sample;
import com.tlcsdm.frame.model.EmptySample;
import javafx.beans.binding.Bindings;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.util.Callback;

public class SmcTreeViewCellFactory implements Callback<TreeView<Sample>, TreeCell<Sample>> {

    @Override
    public TreeCell<Sample> call(TreeView<Sample> sampleTreeView) {
        TreeCell<Sample> treeCell = new TreeCell<>() {
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
        setupImageBinding(treeCell);
        return treeCell;
    }

    private void setupImageBinding(TreeCell<Sample> treeCell) {
        treeCell.graphicProperty().bind(Bindings.createObjectBinding(() -> {
            final Sample item = treeCell.getItem();
            if (item == null) {
                return null;
            }
            if (item instanceof EmptySample emptySample) {
                return switch (item.getSampleName()) {
                    case "Smc", "Common" -> LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/smc/static/icon/folder.png"));
                    case "CodeDev" -> LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/smc/static/icon/code.png"));
                    case "Tools" -> LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/smc/static/icon/tools.png"));
                    case "UnitDesign" -> LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/smc/static/icon/design.png"));
                    case "UnitTest" -> LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/smc/static/icon/test.png"));
                    case "Ecm" -> LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/smc/static/icon/ecm.png"));
                    default -> emptySample.getSampleImageIcon();
                };
            }
            return item.getSampleImageIcon();
        }, treeCell.itemProperty()));
    }

}
