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

package com.tlcsdm.smc.provider;

import com.tlcsdm.core.event.ApplicationStartingEvent;
import com.tlcsdm.core.eventbus.EventBus;
import com.tlcsdm.core.eventbus.Subscribe;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.frame.service.SplashScreen;
import javafx.scene.image.Image;

/**
 * 闪屏图片
 *
 * @author unknowIfGuestInDream
 * @date 2023/3/3 22:47
 */
public class SmcSplashProvider implements SplashScreen {

    public SmcSplashProvider() {
        EventBus.getDefault().register(this);
    }

    @Override
    public Image getImage() {
        return LayoutHelper.icon(getClass().getResource("/com/tlcsdm/smc/static/splash.png"));
    }

    @Subscribe
    @Override
    public void appStartingHandler(ApplicationStartingEvent event) {
        SplashScreen.super.appStartingHandler(event);
    }
}
