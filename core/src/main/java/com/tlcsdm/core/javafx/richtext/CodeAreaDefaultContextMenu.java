/*
 * Copyright (c) 2023 unknowIfGuestInDream
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

package com.tlcsdm.core.javafx.richtext;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import org.fxmisc.richtext.CodeArea;

/**
 * Default contextMenu for CodeArea
 *
 * @author unknowIfGuestInDream
 * @date 2023/8/3 1414
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
