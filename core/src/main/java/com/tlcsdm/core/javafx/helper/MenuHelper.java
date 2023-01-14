package com.tlcsdm.core.javafx.helper;

import javafx.scene.Node;
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
            menuItem.setGraphic((Node) iconSupplier.get());
        }

        return menuItem;
    }

    public static SeparatorMenuItem separator() {
        return new SeparatorMenuItem();
    }
}
