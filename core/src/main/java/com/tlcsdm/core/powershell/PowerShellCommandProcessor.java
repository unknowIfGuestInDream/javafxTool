/*
 * Copyright (c) 2025 unknowIfGuestInDream.
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

/*
 * Copyright 2016-2018 Javier Garcia Alonso.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tlcsdm.core.powershell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Processor used to send commands to PowerShell console.<p>
 * It works as an independent thread and its results are collected using the Future interface.
 *
 * @author Javier Garcia Alonso
 */
class PowerShellCommandProcessor implements Callable<String> {

    private final BufferedReader reader;

    private boolean closed = false;

    private final int waitPause;

    /**
     * Constructor that takes the output and the input of the PowerShell session
     *
     * @param name        the name of the CommandProcessor
     * @param inputStream the stream needed to read the command output
     * @param waitPause   long the wait pause in milliseconds
     */
    public PowerShellCommandProcessor(String name, InputStream inputStream, int waitPause) {
        this.reader = new BufferedReader(new InputStreamReader(
            inputStream, StandardCharsets.UTF_8));
        this.waitPause = waitPause;
    }

    /**
     * Calls the command and returns its output
     *
     * @return String output of call
     * @throws InterruptedException error when reading data
     */
    public String call() throws InterruptedException {
        StringBuilder powerShellOutput = new StringBuilder();

        try {
            if (startReading()) {
                readData(powerShellOutput);
            }
        } catch (IOException ioe) {
            Logger.getLogger(PowerShell.class.getName()).log(Level.SEVERE, "Unexpected error reading PowerShell output",
                ioe);
            return ioe.getMessage();
        }

        return powerShellOutput.toString();
    }

    //Reads all data from output
    private void readData(StringBuilder powerShellOutput) throws IOException {
        boolean isReadyFinishing = false;
        int lineIndex = 0;
        String line;
        while (null != (line = this.reader.readLine())) {

            //It finishes when the last line is read
            if (line.equals(PowerShell.END_COMMAND_STRING)) {
                isReadyFinishing = true;
            } else {
                if (lineIndex > 0) {
                    powerShellOutput.append(System.lineSeparator());
                }
                if (line.contains(PowerShell.END_COMMAND)) {
                    isReadyFinishing = true;
                    line = line.replace(PowerShell.END_COMMAND, "");
                }
                powerShellOutput.append(line);
                lineIndex++;
            }

            //It exits when the command is finished
            try {
                if (this.closed || (isReadyFinishing && !canContinueReading())) {
                    break;
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(PowerShellCommandProcessor.class.getName()).log(Level.SEVERE,
                    "Error executing command and reading result", ex);
            }
        }
    }

    //Checks when we can start reading the output. Timeout if it takes too long in order to avoid hangs
    private boolean startReading() throws IOException, InterruptedException {
        //If the reader is not ready, gives it some milliseconds
        while (!this.reader.ready()) {
            Thread.sleep(this.waitPause);
            if (this.closed) {
                return false;
            }
        }
        return true;
    }

    //Checks when we the reader can continue to read.
    private boolean canContinueReading() throws IOException, InterruptedException {
        //If the reader is not ready, gives it some milliseconds
        //It is important to do that, because the ready method guarantees that the readline will not be blocking
        if (!this.reader.ready()) {
            Thread.sleep(this.waitPause);
        }

        //If not ready yet, wait a moment to make sure it is finished
        if (!this.reader.ready()) {
            Thread.sleep(50);
        }

        return this.reader.ready();
    }

    /**
     * Closes the command processor, canceling the current work if not finish
     */
    public void close() {
        this.closed = true;
    }
}
