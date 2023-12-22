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

package com.tlcsdm.core.dsl.impl;

import com.tlcsdm.core.dsl.DSL;

import java.io.File;
import java.io.IOException;

/**
 * Support iterating over the key-value pairs of any INI file.
 *
 * @author unknowIfGuestInDream
 * @author Konloch
 */
public class IterateINI {

    private IterateINI() {
    }

    /**
     * Define a new DSL as INI format
     */
    private static final DSL ini = new DSL('=', '%', '(', ')', '{', '}', '#');

    /**
     * Parse and iterate the key-value pairs of any INI file.
     *
     * @param filePath    any String as the file path of the INI file
     * @param onPairFound any KeyValuePairFound called whenever a key-value pair is found
     * @throws IOException if an I/O error occurs reading from the stream
     */
    public static void fromFile(String filePath, KeyValuePairFound onPairFound) throws IOException {
        fromFile(new File(filePath), onPairFound);
    }

    /**
     * Parse and iterate the key-value pairs of any INI file.
     *
     * @param file        any File in INI file format
     * @param onPairFound any KeyValuePairFound called whenever a key-value pair is found
     * @throws IOException if an I/O error occurs reading from the stream
     */
    public static void fromFile(File file, KeyValuePairFound onPairFound) throws IOException {
        //load the file as ini format
        ini.parse(file);

        //parse each ini key-value pair
        for (String key : ini.getRuntime().getCommands().keySet()) {
            String value = ini.getRuntime().getCommands().get(key).getParameters()[0];
            onPairFound.found(key, value);
        }
    }

}
