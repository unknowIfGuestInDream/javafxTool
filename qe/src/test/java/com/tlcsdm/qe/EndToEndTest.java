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

package com.tlcsdm.qe;

import cn.hutool.log.StaticLog;
import com.tlcsdm.frame.FXSampler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.util.DebugUtils;
import org.testfx.util.WaitForAsyncUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

@ExtendWith(ApplicationExtension.class)
public abstract class EndToEndTest {

    private static final AtomicInteger testNumber = new AtomicInteger();
    private static final Path SCREENSHOT_DIR = Paths.get("integration-test-screenshots");

    protected FxRobotPlus fxRobot;
    protected AppHelper appHelper;

    static {
        SCREENSHOT_DIR.toFile().mkdirs();
        try {
            for (Path path : Files.newDirectoryStream(SCREENSHOT_DIR, f -> f.toFile().isFile())) {
                Files.delete(path);
            }
        } catch (IOException e) {
            StaticLog.warn("Failed to clean up screenshot dir", e);
        }
    }

    @BeforeEach
    void setup(FxRobot fxRobot) throws Exception {
        StaticLog.debug("Setting up test");
        // Closing the stage kills the executor service, and highlighting throws an unhandled exception causing the
        // test to fail
        WaitForAsyncUtils.autoCheckException = false;
        FxToolkit.setupApplication(FXSampler.class);
        this.fxRobot = new FxRobotPlus(fxRobot);
        this.appHelper = new AppHelper(this.fxRobot);
    }

    @AfterEach
    void teardown() throws Exception {
        final StringBuilder debugInfo = DebugUtils.saveScreenshot(this::getScreenshotPath, "   ")
            .apply(new StringBuilder());
        StaticLog.info(debugInfo.toString());
        StaticLog.debug("Tearing down test");
        FxToolkit.cleanupStages();
    }

    private Path getScreenshotPath() {
        return SCREENSHOT_DIR.resolve(getClass().getName() + " - " + testNumber.incrementAndGet() + ".png");
    }

}
