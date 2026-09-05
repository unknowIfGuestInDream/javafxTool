/*
 * Copyright (c) 2026 unknowIfGuestInDream.
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

package com.tlcsdm.autoupdate.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link UpdateInfo} 的单元测试.
 *
 * @author unknowIfGuestInDream
 */
public class UpdateInfoTest {

    @Test
    public void buildExposesValues() {
        UpdateInfo info = UpdateInfo.builder()
            .version("1.2.0")
            .currentVersion("1.1.0")
            .downloadUrl("https://example.com/app-1.2.0.zip")
            .size(1024)
            .sha256("ABC")
            .releaseUrl("https://example.com/releases")
            .releaseNotes("notes")
            .build();
        assertEquals("1.2.0", info.getVersion());
        assertEquals("1.1.0", info.getCurrentVersion());
        assertEquals("https://example.com/app-1.2.0.zip", info.getDownloadUrl());
        assertEquals("app-1.2.0.zip", info.getFileName());
        assertEquals(1024, info.getSize());
        assertEquals("ABC", info.getSha256());
        assertEquals("https://example.com/releases", info.getReleaseUrl());
        assertEquals("notes", info.getReleaseNotes());
        assertTrue(info.hasChecksum());
    }

    @Test
    public void fileNameGuessedFromUrlIgnoringQuery() {
        UpdateInfo info = UpdateInfo.builder()
            .downloadUrl("https://host/download/smcTool-win-1.1.0.zip?token=abc")
            .build();
        assertEquals("smcTool-win-1.1.0.zip", info.getFileName());
    }

    @Test
    public void explicitFileNameWins() {
        UpdateInfo info = UpdateInfo.builder()
            .downloadUrl("https://host/x")
            .fileName("custom.exe")
            .build();
        assertEquals("custom.exe", info.getFileName());
    }

    @Test
    public void hasChecksumFalseWhenBlank() {
        UpdateInfo info = UpdateInfo.builder()
            .downloadUrl("https://host/app.zip")
            .build();
        assertFalse(info.hasChecksum());
    }

    @Test
    public void blankDownloadUrlRejected() {
        assertThrows(IllegalArgumentException.class, () -> UpdateInfo.builder().build());
    }
}
