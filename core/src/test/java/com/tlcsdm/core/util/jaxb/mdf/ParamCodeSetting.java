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

package com.tlcsdm.core.util.jaxb.mdf;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class ParamCodeSetting {

    @XmlElement(name = "zipsource", required = true)
    protected String zipsource;
    @XmlElement(name = "dir")
    protected List<Dir> dirList;
    @XmlElement(name = "file")
    protected List<File> fileList;

    public String getZipsource() {
        return zipsource;
    }

    public void setZipsource(String zipsource) {
        this.zipsource = zipsource;
    }

    public List<Dir> getDirList() {
        return dirList;
    }

    public void setDirList(List<Dir> dirList) {
        this.dirList = dirList;
    }

    public List<File> getFileList() {
        return fileList;
    }

    public void setFileList(List<File> fileList) {
        this.fileList = fileList;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Dir {
        @XmlAttribute(name = "incdir", required = true)
        protected String incdir;
        @XmlAttribute(name = "output")
        protected String output;
        @XmlElement(name = "condition")
        protected Condition condition;

        public String getIncdir() {
            return incdir;
        }

        public void setIncdir(String incdir) {
            this.incdir = incdir;
        }

        public String getOutput() {
            return output;
        }

        public void setOutput(String output) {
            this.output = output;
        }

        public Condition getCondition() {
            return condition;
        }

        public void setCondition(Condition condition) {
            this.condition = condition;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class File {
        @XmlAttribute(name = "incfile", required = true)
        protected String incfile;
        @XmlAttribute(name = "output")
        protected String output;
        @XmlElement(name = "condition")
        protected Condition condition;

        public String getIncfile() {
            return incfile;
        }

        public void setIncfile(String incfile) {
            this.incfile = incfile;
        }

        public String getOutput() {
            return output;
        }

        public void setOutput(String output) {
            this.output = output;
        }

        public Condition getCondition() {
            return condition;
        }

        public void setCondition(Condition condition) {
            this.condition = condition;
        }
    }
}
