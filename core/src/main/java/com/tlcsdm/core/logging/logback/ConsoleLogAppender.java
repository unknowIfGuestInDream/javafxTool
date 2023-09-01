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

package com.tlcsdm.core.logging.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;
import javafx.scene.control.TextArea;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 日志打印控制台
 *
 * @author unknowIfGuestInDream
 * @date 2023/3/26 20:56
 */
public class ConsoleLogAppender extends OutputStreamAppender<ILoggingEvent> {
    public final static List<TextArea> textAreaList = new ArrayList<>();

    @Override
    public void start() {
        OutputStream targetStream = new OutputStream() {
            @Override
            public void write(int b) {
                for (TextArea textArea : textAreaList) {
                    textArea.appendText(String.valueOf(b));
                }
            }

            @Override
            public void write(byte[] b) {
                for (TextArea textArea : textAreaList) {
                    textArea.appendText(new String(b));
                }
            }
        };
        setOutputStream(targetStream);
        super.start();
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        super.append(eventObject);
    }
}
