package com.tlcsdm.core.javafx.richtext;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import org.fxmisc.richtext.CodeArea;

/**
 * Default contextMenu for CodeArea
 *
 * @author: unknowIfGuestInDream
 * @date: 2023/8/3 1414
 */
public class CodeAreaDefaultContextMenu extends ContextMenu {
    private MenuItem fold, unfold;

    public CodeAreaDefaultContextMenu() {
        fold = new MenuItem("Fold selected text");
        fold.setOnAction(ae -> {
            hide();
            fold();
        });

        unfold = new MenuItem("Unfold from cursor");
        unfold.setOnAction(ae -> {
            hide();
            unfold();
        });

        getItems().addAll(fold, unfold);
    }

    /**
     * Folds multiple lines of selected text, only showing the first line and hiding the rest.
     */
    private void fold() {
        ((CodeArea) getOwnerNode()).foldSelectedParagraphs();
    }

    /**
     * Unfold the CURRENT line/paragraph if it has a fold.
     */
    private void unfold() {
        CodeArea area = (CodeArea) getOwnerNode();
        area.unfoldParagraphs(area.getCurrentParagraph());
    }

}
