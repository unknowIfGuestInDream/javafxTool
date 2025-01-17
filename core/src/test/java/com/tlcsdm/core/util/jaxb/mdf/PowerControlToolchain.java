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
public class PowerControlToolchain {
    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlElement(name = "compilerOpt", required = true)
    protected String compilerOpt;
    @XmlElement(name = "param")
    protected Param Param;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompilerOpt() {
        return compilerOpt;
    }

    public void setCompilerOpt(String compilerOpt) {
        this.compilerOpt = compilerOpt;
    }

    public PowerControlToolchain.Param getParam() {
        return Param;
    }

    public void setParam(PowerControlToolchain.Param param) {
        Param = param;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Param {
        @XmlElement(name = "commandParam")
        protected List<CommandParam> commandParamList;
        @XmlElement(name = "linkParam")
        protected List<LinkParam> linkParamList;

        public List<CommandParam> getCommandParamList() {
            return commandParamList;
        }

        public void setCommandParamList(List<CommandParam> commandParamList) {
            this.commandParamList = commandParamList;
        }

        public List<LinkParam> getLinkParamList() {
            return linkParamList;
        }

        public void setLinkParamList(List<LinkParam> linkParamList) {
            this.linkParamList = linkParamList;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class CommandParam {

        @XmlAttribute(name = "fileType", required = true)
        protected String fileType;
        @XmlElement(name = "param", required = true)
        protected List<String> paramList;
        @XmlElement(name = "condition")
        protected Condition condition;

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public List<String> getParamList() {
            return paramList;
        }

        public void setParamList(List<String> paramList) {
            this.paramList = paramList;
        }

        public Condition getCondition() {
            return condition;
        }

        public void setCondition(Condition condition) {
            this.condition = condition;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class LinkParam {
        @XmlElement(name = "param", required = true)
        protected List<String> paramList;
        @XmlElement(name = "condition")
        protected Condition condition;

        public List<String> getParamList() {
            return paramList;
        }

        public void setParamList(List<String> paramList) {
            this.paramList = paramList;
        }

        public Condition getCondition() {
            return condition;
        }

        public void setCondition(Condition condition) {
            this.condition = condition;
        }
    }
}
