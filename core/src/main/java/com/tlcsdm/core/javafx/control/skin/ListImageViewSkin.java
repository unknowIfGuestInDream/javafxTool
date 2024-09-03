/*
 * Copyright (c) 2024 unknowIfGuestInDream.
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

package com.tlcsdm.core.javafx.control.skin;

import com.tlcsdm.core.javafx.control.ListImageView;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SkinBase;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

/**
 * @author unknowIfGuestInDream
 */
public class ListImageViewSkin extends SkinBase<ListImageView> {

    private BorderPane appBorderPane;
    private ListView<ListImageView.Photo> listView;
    private ImageView imageView;
    private Pane imageViewPane;

    public ListImageViewSkin(ListImageView listImageView) {
        super(listImageView);
    }

    private class ImageTextCell extends ListCell<ListImageView.Photo> {
        private VBox vbox = new VBox(8.0);
        private Label label = new Label();
        private ImageView thumbImageView = new ImageView();

        //init block
        {
            thumbImageView.setFitHeight(100.0);
            thumbImageView.setPreserveRatio(true);

            label.setWrapText(true);
            label.setTextAlignment(TextAlignment.CENTER);
            label.setLabelFor(thumbImageView);
            label.underlineProperty().setValue(true);

            vbox.setAlignment(Pos.CENTER);
            vbox.getChildren().addAll(thumbImageView, label);
            vbox.cursorProperty().setValue(Cursor.HAND);

        }

        @Override
        protected void updateItem(final ListImageView.Photo photo, final boolean empty) {
            super.updateItem(photo, empty);

            if (empty || photo == null) {
                setGraphic(null);
                return;
            }

            label.setText(photo.title());
            thumbImageView.setImage(new Image(photo.location()));

            setGraphic(vbox);

        }
    }
}
