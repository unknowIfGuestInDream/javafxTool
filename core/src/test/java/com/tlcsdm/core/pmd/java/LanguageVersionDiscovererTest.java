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

package com.tlcsdm.core.pmd.java;

import net.sourceforge.pmd.PMDConfiguration;
import net.sourceforge.pmd.lang.LanguageRegistry;
import net.sourceforge.pmd.lang.LanguageVersion;
import net.sourceforge.pmd.lang.LanguageVersionDiscoverer;
import net.sourceforge.pmd.lang.java.JavaLanguageModule;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class LanguageVersionDiscovererTest {

    /**
     * Test on Java file with default options.
     */
    @Test
    @Ignore
    public void testJavaFileUsingDefaults() {
        LanguageVersionDiscoverer discoverer = new LanguageVersionDiscoverer();
        File javaFile = new File("/path/to/MyClass.java");

        LanguageVersion languageVersion = discoverer.getDefaultLanguageVersionForFile(javaFile);
        assertEquals("LanguageVersion must be Java 10 !",
            LanguageRegistry.getLanguage(JavaLanguageModule.NAME).getVersion("10"), languageVersion);
    }

    /**
     * Test on Java file with Java version set to 1.4.
     */
    @Test
    public void testJavaFileUsing14() {
        LanguageVersionDiscoverer discoverer = new LanguageVersionDiscoverer();
        discoverer.setDefaultLanguageVersion(LanguageRegistry.getLanguage(JavaLanguageModule.NAME).getVersion("1.4"));
        File javaFile = new File("/path/to/MyClass.java");

        LanguageVersion languageVersion = discoverer.getDefaultLanguageVersionForFile(javaFile);
        assertEquals("LanguageVersion must be Java 1.4!",
            LanguageRegistry.getLanguage(JavaLanguageModule.NAME).getVersion("1.4"), languageVersion);
    }

    @Test
    @Ignore
    public void testLanguageVersionDiscoverer() {
        PMDConfiguration configuration = new PMDConfiguration();
        LanguageVersionDiscoverer languageVersionDiscoverer = configuration.getLanguageVersionDiscoverer();
        assertEquals("Default Java version", LanguageRegistry.getLanguage(JavaLanguageModule.NAME).getVersion("10"),
            languageVersionDiscoverer
                .getDefaultLanguageVersion(LanguageRegistry.getLanguage(JavaLanguageModule.NAME)));
        configuration
            .setDefaultLanguageVersion(LanguageRegistry.getLanguage(JavaLanguageModule.NAME).getVersion("1.5"));
        assertEquals("Modified Java version", LanguageRegistry.getLanguage(JavaLanguageModule.NAME).getVersion("1.5"),
            languageVersionDiscoverer
                .getDefaultLanguageVersion(LanguageRegistry.getLanguage(JavaLanguageModule.NAME)));
    }
}
