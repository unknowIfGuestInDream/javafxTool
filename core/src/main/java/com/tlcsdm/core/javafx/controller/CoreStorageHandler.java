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

package com.tlcsdm.core.javafx.controller;

import com.dlsc.preferencesfx.util.StorageHandler;
import com.tlcsdm.core.event.ConfigRefreshEvent;
import com.tlcsdm.core.eventbus.EventBus;
import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.Keys;
import com.tlcsdm.core.javafx.util.ScreenUtil;
import javafx.collections.ObservableList;

import java.util.Objects;

/**
 * refer to {@link com.dlsc.preferencesfx.util.PreferencesBasedStorageHandler}
 *
 * @author unknowIfGuestInDream
 */
public class CoreStorageHandler implements StorageHandler {
    @Override
    public void saveSelectedCategory(String s) {
        Config.set(Keys.PreferenceSelectedCategory, s);
    }

    @Override
    public String loadSelectedCategory() {
        return Config.get(Keys.PreferenceSelectedCategory, null);
    }

    @Override
    public void saveDividerPosition(double v) {
        Config.set(Keys.PreferenceDividerPosition, v);
    }

    @Override
    public double loadDividerPosition() {
        return Config.getDouble(Keys.PreferenceDividerPosition, 0.2);
    }

    @Override
    public void saveWindowWidth(double v) {
        Config.set(Keys.PreferenceWindowWidth, v);
    }

    @Override
    public double loadWindowWidth() {
        return Config.getDouble(Keys.PreferenceWindowWidth, ScreenUtil.getScreenWeight() * 0.6);
    }

    @Override
    public void saveWindowHeight(double v) {
        Config.set(Keys.PreferenceWindowHeight, v);
    }

    @Override
    public double loadWindowHeight() {
        return Config.getDouble(Keys.PreferenceWindowHeight, ScreenUtil.getScreenHeight() * 0.6);
    }

    @Override
    public void saveWindowPosX(double v) {
        Config.set(Keys.PreferenceWindowPosX, v);
    }

    @Override
    public double loadWindowPosX() {
        return Config.getDouble(Keys.PreferenceWindowPosX, ScreenUtil.getScreenHeight() * 0.3);
    }

    @Override
    public void saveWindowPosY(double v) {
        Config.set(Keys.PreferenceWindowPosY, v);
    }

    @Override
    public double loadWindowPosY() {
        return Config.getDouble(Keys.PreferenceWindowPosY, ScreenUtil.getScreenHeight() * 0.2);
    }

    @Override
    public void saveObject(String s, Object o) {
        String keyName = s.substring(s.lastIndexOf(".") + 1);
        Config.set(Objects.requireNonNull(Keys.fromKeyName(keyName)), o);
        EventBus.getDefault().post(new ConfigRefreshEvent(keyName));
    }

    @Override
    public Object loadObject(String s, Object o) {
        Keys key = Keys.fromKeyName(s.substring(s.lastIndexOf(".") + 1));
        if (o instanceof Boolean bool) {
            return Config.getBoolean(key, bool);
        } else if (o instanceof Double d) {
            return Config.getDouble(key, d);
        }
        return Config.get(key, o);
    }

    @Override
    public <T> T loadObject(String s, Class<T> aClass, T t) {
        return null;
    }

    @Override
    public ObservableList loadObservableList(String s, ObservableList observableList) {
        return null;
    }

    @Override
    public <T> ObservableList<T> loadObservableList(String s, Class<T> aClass, ObservableList<T> observableList) {
        return null;
    }

    @Override
    public boolean clearPreferences() {
        return false;
    }
}
