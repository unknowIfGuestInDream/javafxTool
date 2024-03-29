/*
 * Copyright (c) 2023 unknowIfGuestInDream.
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

package com.tlcsdm.core.javafx.factory;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 * 快捷键工厂.
 *
 * @author unknowIfGuestInDream
 */
public class KeyCombinationFactory {

    private KeyCombinationFactory() {
    }

    /**
     * 日志控制台.
     */
    public static final KeyCombination CTRL_SHIFT_L = new KeyCodeCombination(KeyCode.L, KeyCombination.SHIFT_DOWN,
        KeyCombination.CONTROL_DOWN);
    /**
     * 设置.
     */
    public static final KeyCombination SHORTCUT_P = KeyCombination.valueOf("SHORTCUT+p");
    /**
     * 屏幕截图.
     */
    public static final KeyCombination CTRL_SHIFT_S = new KeyCodeCombination(KeyCode.S, KeyCombination.SHIFT_DOWN,
        KeyCombination.CONTROL_DOWN);
    /**
     * 颜色提取器.
     */
    public static final KeyCombination CTRL_SHIFT_C = new KeyCodeCombination(KeyCode.C, KeyCombination.SHIFT_DOWN,
        KeyCombination.CONTROL_DOWN);

}
