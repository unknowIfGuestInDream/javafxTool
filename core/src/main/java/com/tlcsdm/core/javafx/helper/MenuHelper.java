/*
 * Copyright (c) 2019, 2023 unknowIfGuestInDream
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

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.ImageView;

import java.util.function.Supplier;

/**
 * @author: unknowIfGuestInDream
 * @date: 2023/1/14 18:36
 */
public class MenuHelper {
    public static ContextMenu contextMenu(MenuItem... menuItems) {
        return new ContextMenu(menuItems);
    }

    public static MenuItem menuItem(String text, Runnable action) {
        return menuItem(text, (Supplier) null, action);
    }

    public static MenuItem menuItem(String text, ImageView icon, Runnable action) {
        return menuItem(text, () -> {
            return icon;
        }, action);
    }

    public static MenuItem menuItem(String text, Supplier<ImageView> iconSupplier, Runnable action) {
        MenuItem menuItem = new MenuItem(text);
        menuItem.setOnAction((event) -> {
            action.run();
        });
        if (iconSupplier != null) {
            menuItem.setGraphic(iconSupplier.get());
        }

        return menuItem;
    }

    public static SeparatorMenuItem separator() {
        return new SeparatorMenuItem();
    }
}
