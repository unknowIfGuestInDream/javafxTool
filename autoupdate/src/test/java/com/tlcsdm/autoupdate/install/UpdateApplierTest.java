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

package com.tlcsdm.autoupdate.install;

import com.tlcsdm.core.javafx.util.OSUtil;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link UpdateApplier#buildUpdateScript} 的单元测试.
 *
 * @author unknowIfGuestInDream
 */
public class UpdateApplierTest {

    private final Path zipPackage = Paths.get("downloads", "smcTool-win-1.2.0.zip");
    private final Path exePackage = Paths.get("downloads", "smcTool-win-1.2.0.exe");
    private final Path installDir = Paths.get("apps", "smcTool");

    @Test
    public void windowsZipWithRelaunch() {
        String script = UpdateApplier.buildUpdateScript(OSUtil.OS.WINDOWS, zipPackage, installDir, "smcTool.exe");
        assertTrue(script.startsWith("@echo off"), script);
        assertTrue(script.contains("timeout /t 3 /nobreak"), script);
        assertTrue(script.contains("Expand-Archive -LiteralPath '" + zipPackage + "'"), script);
        assertTrue(script.contains("-DestinationPath '" + installDir + "' -Force"), script);
        assertTrue(script.contains("start \"\" \"smcTool.exe\""), script);
        assertTrue(script.contains("del \"%~f0\""), script);
    }

    @Test
    public void windowsInstallerWithoutRelaunch() {
        String script = UpdateApplier.buildUpdateScript(OSUtil.OS.WINDOWS, exePackage, installDir, null);
        assertFalse(script.contains("Expand-Archive"), script);
        assertTrue(script.contains("start \"\" \"" + exePackage + "\""), script);
        assertTrue(script.contains("del \"%~f0\""), script);
    }

    @Test
    public void linuxZipWithRelaunch() {
        String script = UpdateApplier.buildUpdateScript(OSUtil.OS.LINUX, zipPackage, installDir, "./bin/smcTool");
        assertTrue(script.startsWith("#!/bin/sh"), script);
        assertTrue(script.contains("sleep 3"), script);
        assertTrue(script.contains("unzip -o \"" + zipPackage + "\" -d \"" + installDir + "\""), script);
        assertTrue(script.contains("\"./bin/smcTool\" &"), script);
        assertTrue(script.contains("rm -- \"$0\""), script);
    }

    @Test
    public void macInstallerWithoutRelaunch() {
        String script = UpdateApplier.buildUpdateScript(OSUtil.OS.MAC, exePackage, installDir, "");
        assertTrue(script.startsWith("#!/bin/sh"), script);
        assertFalse(script.contains("unzip"), script);
        assertTrue(script.contains("\"" + exePackage + "\""), script);
        assertTrue(script.contains("rm -- \"$0\""), script);
    }
}
