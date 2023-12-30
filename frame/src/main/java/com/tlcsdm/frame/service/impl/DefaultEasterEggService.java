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

package com.tlcsdm.frame.service.impl;

import com.tlcsdm.core.factory.config.ScheduledTaskExecutor;
import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.stage.SakuraState;
import com.tlcsdm.core.javafx.stage.SnowState;
import com.tlcsdm.frame.service.EasterEggService;

import java.time.LocalDate;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author unknowIfGuestInDream
 */
public class DefaultEasterEggService implements EasterEggService {

    private ScheduledFuture<?> runnableFuture;

    @Override
    public void initializeEasterEgg() {
        runnableFuture = ScheduledTaskExecutor.get().scheduleAtFixedRate(() -> {
            LocalDate today = LocalDate.now();
            int mouth = today.getMonthValue();
            int day = today.getDayOfMonth();
            if (mouth == 12 && (day == 24 || day == 25)) {
                // 平安夜和圣诞节
                FxApp.runLater(() -> SnowState.getInstance().show());
            } else if (mouth == 1 || day == 1) {
                // 元旦
                FxApp.runLater(() -> SnowState.getInstance().show());
            } else if (mouth == 4 || day == 5) {
                // 清明
                //FxApp.runLater(() -> SnowState.getInstance().show());
            } else if (mouth == 3 || day == 21) {
                // 春分の日
                FxApp.runLater(() -> SakuraState.getInstance().show());
            } else {
                FxApp.runLater(() -> {
                    SnowState.getInstance().close();
                    SakuraState.getInstance().close();
                });
            }
        }, 2, 60 * 60 * 6, TimeUnit.SECONDS);
    }

    @Override
    public void start() {
        if (runnableFuture == null || runnableFuture.isCancelled()) {
            initializeEasterEgg();
        }
    }

    @Override
    public void stop() {
        runnableFuture.cancel(false);
    }
}
