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

package com.tlcsdm.core.javafx.control;

import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.util.I18nUtils;
import javafx.scene.control.Button;

/**
 * 封装一些常用的button的初始化
 *
 * @author: unknowIfGuestInDream
 */
public class FxButton {

    public static Button choose() {
        return new Button(I18nUtils.get("core.button.choose"));
    }

    public static Button chooseWithGrapgic() {
        return new Button(I18nUtils.get("core.button.choose"),
                LayoutHelper.iconView(FxButton.class.getResource("/com/tlcsdm/core/static/icon/choose.png")));
    }

    public static Button clear() {
        return new Button(I18nUtils.get("core.button.clear"));
    }

    public static Button clearWithGrapgic() {
        return new Button(I18nUtils.get("core.button.clear"),
                LayoutHelper.iconView(FxButton.class.getResource("/com/tlcsdm/core/static/icon/clear.png")));
    }

    public static Button reset() {
        return new Button(I18nUtils.get("core.button.reset"));
    }

    public static Button resetWithGrapgic() {
        return new Button(I18nUtils.get("core.button.reset"),
                LayoutHelper.iconView(FxButton.class.getResource("/com/tlcsdm/core/static/icon/reset.png")));
    }

    public static Button copy() {
        return new Button(I18nUtils.get("core.button.copy"));
    }

    public static Button copyWithGrapgic() {
        return new Button(I18nUtils.get("core.button.copy"),
                LayoutHelper.iconView(FxButton.class.getResource("/com/tlcsdm/core/static/icon/copy.png")));
    }

}
