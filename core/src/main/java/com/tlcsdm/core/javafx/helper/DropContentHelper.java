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

package com.tlcsdm.core.javafx.helper;

import javafx.scene.Node;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.io.File;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * @author unknowIfGuestInDream
 * @date 2023/7/9 14:57
 */
public class DropContentHelper {
    private DropContentHelper() {
    }

    public static <T extends Node> void acceptText(T node, BiConsumer<T, String> onAccept) {
        node.setOnDragOver((event) -> {
            Dragboard board = event.getDragboard();
            if (!board.hasString() && !board.hasHtml() && !board.hasRtf() && !board.hasUrl()) {
                event.acceptTransferModes(TransferMode.NONE);
            } else {
                event.acceptTransferModes(TransferMode.ANY);
            }

        });
        node.setOnDragDropped((event) -> {
            Dragboard dragboard = event.getDragboard();
            if (dragboard.hasString()) {
                onAccept.accept(node, dragboard.getString());
            } else if (dragboard.hasHtml()) {
                onAccept.accept(node, dragboard.getHtml());
            } else if (dragboard.hasRtf()) {
                onAccept.accept(node, dragboard.getRtf());
            } else if (dragboard.hasUrl()) {
                onAccept.accept(node, dragboard.getUrl());
            }

        });
    }

    public static <T extends Node> void acceptFile(T node, BiConsumer<T, List<File>> onAccept) {
        node.setOnDragOver((event) -> {
            if (event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.ANY);
            } else {
                event.acceptTransferModes(TransferMode.NONE);
            }

        });
        node.setOnDragDropped((event) -> {
            Dragboard dragboard = event.getDragboard();
            if (dragboard.hasFiles()) {
                onAccept.accept(node, dragboard.getFiles());
            }
        });
    }

    public static <T extends Node> void accept(T node, Predicate<Dragboard> predicate, BiConsumer<T, Dragboard> onAccept) {
        node.setOnDragOver((event) -> {
            if (predicate.test(event.getDragboard())) {
                event.acceptTransferModes(TransferMode.ANY);
            } else {
                event.acceptTransferModes(TransferMode.NONE);
            }
        });
        node.setOnDragDropped((event) -> {
            Dragboard dragboard = event.getDragboard();
            onAccept.accept(node, dragboard);
        });
    }
}
