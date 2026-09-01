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

package com.tlcsdm.autoupdate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link AutoUpdate} 版本比较逻辑的单元测试.
 *
 * @author unknowIfGuestInDream
 */
public class AutoUpdateTest {

    @Test
    public void newerPatchAndMinor() {
        assertTrue(AutoUpdate.isNewer("1.1.1", "1.1.0"));
        assertTrue(AutoUpdate.isNewer("1.2.0", "1.1.9"));
        assertTrue(AutoUpdate.isNewer("2.0.0", "1.9.9"));
    }

    @Test
    public void numericSegmentsNotLexical() {
        assertTrue(AutoUpdate.isNewer("1.10.0", "1.9.0"));
        assertFalse(AutoUpdate.isNewer("1.9.0", "1.10.0"));
    }

    @Test
    public void equalOrOlderIsNotNewer() {
        assertFalse(AutoUpdate.isNewer("1.1.0", "1.1.0"));
        assertFalse(AutoUpdate.isNewer("1.0.0", "1.1.0"));
    }

    @Test
    public void ignoresLeadingVAndDifferentLength() {
        assertTrue(AutoUpdate.isNewer("v1.2.0", "1.1.0"));
        assertFalse(AutoUpdate.isNewer("1.1", "1.1.0"));
        assertTrue(AutoUpdate.isNewer("1.1.1", "1.1"));
    }
}
