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

package com.tlcsdm.demo;

import com.tlcsdm.frame.service.MenubarConfigration;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleGroup;

public class FXMenubarConfigration implements MenubarConfigration {

    @Override
    public MenuBar setMenuBar(MenuBar menuBar) {
        Menu menu1 = new Menu("menu1");
        Menu menu2 = new Menu("menu2");
        Menu menu3 = new Menu("menu3");
        menuBar.getMenus().addAll(menu1, menu2, menu3);
        MenuItem item1 = new MenuItem("item1");
        MenuItem item2 = new MenuItem("item2");

        MenuItem item3 = new MenuItem("item3");
        // 分割符（一条线）
        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        // 子菜单
        Menu menu4 = new Menu("menu4");
        MenuItem item5 = new MenuItem("MenuItem5");
        MenuItem item6 = new MenuItem("MenuItem6");
        menu4.getItems().addAll(item5, item6);

        menu1.getItems().addAll(item1, item2, separatorMenuItem, menu4, item3);
        // 菜单栏单选
        ToggleGroup tg = new ToggleGroup(); // 组
        RadioMenuItem radioMenuItem1 = new RadioMenuItem("RadioMenuItem1");
        RadioMenuItem radioMenuItem2 = new RadioMenuItem("RadioMenuItem2");
        RadioMenuItem radioMenuItem3 = new RadioMenuItem("RadioMenuItem3");
        radioMenuItem1.setToggleGroup(tg);
        radioMenuItem2.setToggleGroup(tg);
        radioMenuItem3.setToggleGroup(tg);
        menu2.getItems().addAll(radioMenuItem1, radioMenuItem2, radioMenuItem3);

        radioMenuItem3.setSelected(true); // 设置默认为选中状态

        // 绑定单击事件
        radioMenuItem1.setOnAction(event -> {
            RadioMenuItem source = (RadioMenuItem) event.getSource();// 获得事件源
            System.out.println(source.isSelected()); // 判断是否被选中
            // 或者直接点
            System.out.println(radioMenuItem1.isSelected());

        });

        // 菜单栏多选
        CheckMenuItem checkMenuItem1 = new CheckMenuItem("checkMenuItem1");
        CheckMenuItem checkMenuItem2 = new CheckMenuItem("checkMenuItem2");
        CheckMenuItem checkMenuItem3 = new CheckMenuItem("checkMenuItem3");
        menu3.getItems().addAll(checkMenuItem1, checkMenuItem2, checkMenuItem3);

        checkMenuItem2.setSelected(true); // 设置默认选中
        checkMenuItem3.setSelected(true);

        // 绑定单击事件
        checkMenuItem1.setOnAction(event -> {
            CheckMenuItem source = (CheckMenuItem) event.getSource();// 获得事件源
            System.out.println(source.isSelected()); // 判断是否被选中
            // 或者直接点
            System.out.println(checkMenuItem1.isSelected());
        });

        return menuBar;
    }

}
