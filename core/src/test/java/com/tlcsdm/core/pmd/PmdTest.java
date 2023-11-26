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

package com.tlcsdm.core.pmd;

import net.sourceforge.pmd.PMDConfiguration;
import net.sourceforge.pmd.PmdAnalysis;
import net.sourceforge.pmd.cpd.CPD;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

@DisabledIfSystemProperty(named = "workEnv", matches = "ci")
class PmdTest {

    @Test
    void doPmd() {
        PMDConfiguration configuration = new PMDConfiguration();
        configuration.setInputPaths("E:\\javaWorkSpace\\javafxTool\\smc\\src");
        configuration.setRuleSets("rulesets/java/quickstart.xml,rulesets/java/design.xml,rulesets/java/javabeans.xml," +
            "rulesets/java/basic.xml,rulesets/java/braces.xml,rulesets/java/clone.xml,rulesets/java/codesize.xml,rulesets/java/comments.xml," +
            "rulesets/java/empty.xml,rulesets/java/imports.xml,rulesets/java/logging-java.xml,rulesets/java/metrics.xml,rulesets/java/naming.xml," +
            "rulesets/java/optimization.xml,rulesets/java/strings.xml,rulesets/java/sunsecure.xml,rulesets/java/typeresolution.xml," +
            "rulesets/java/unnecessary.xml,rulesets/java/unusedcode.xml");
        configuration.setReportFormat("csv");
        configuration.setReportFile("E:\\testPlace\\pmd\\java-report.csv");
        PmdAnalysis.create(configuration).performAnalysis();
        //PMD.doPMD(configuration);
    }

    @Test
    void doCpp() {
//        CPDConfiguration config = new CPDConfiguration();
//        config.setMinimumTileSize(100);
//        config.setLanguage(LanguageFactory.createLanguage("cpp"));
//        config.setEncoding("UTF-8");
//        config.setSkipDuplicates(false);
//        config.setSkipLexicalErrors(false);
//        config.setRendererName("csv");
//        CPD cpd = new CPD(config);
//        cpd.addAllInDirectory(new File("E:\\cppWorkspace\\design-patterns-cpp"));
        CPD.runCpd("--minimum-tokens", "10", "--dir", "E:\\cppWorkspace\\design-patterns-cpp", "--language", "cpp");
    }
}
