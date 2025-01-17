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
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "powerControl")
@XmlAccessorType(XmlAccessType.FIELD)
public class PowerControlSettings {
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "version", required = true)
    protected String version;
    @XmlAttribute(name = "requireVersion", required = true)
    protected String requireVersion;
    @XmlAttribute(name = "boardId", required = true)
    protected String boardId;
    @XmlElement(name = "boardName", required = true)
    protected String boardName;
    @XmlElement(name = "boardImageURL", required = true)
    protected String boardImageURL;
    @XmlElementWrapper(name = "boardCircuits")
    @XmlElement(name = "boardCircuit", required = true)
    protected List<BoardCircuit> boardCircuitList;
    @XmlElement(name = "paramUISetting", required = true)
    protected ParamUISetting paramUISetting;
    @XmlElement(name = "paramCodeSetting", required = true)
    protected ParamCodeSetting paramCodeSetting;
    @XmlElement(name = "toolchain")
    protected List<PowerControlToolchain> toolchain;
    @XmlElement(name = "variableSetting")
    protected PowerControlVariableSetting variableSetting;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRequireVersion() {
        return requireVersion;
    }

    public void setRequireVersion(String requireVersion) {
        this.requireVersion = requireVersion;
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getBoardImageURL() {
        return boardImageURL;
    }

    public void setBoardImageURL(String boardImageURL) {
        this.boardImageURL = boardImageURL;
    }

    public List<BoardCircuit> getBoardCircuitList() {
        return boardCircuitList;
    }

    public void setBoardCircuitList(List<BoardCircuit> boardCircuitList) {
        this.boardCircuitList = boardCircuitList;
    }

    public ParamUISetting getParamUISetting() {
        return paramUISetting;
    }

    public void setParamUISetting(ParamUISetting paramUISetting) {
        this.paramUISetting = paramUISetting;
    }

    public ParamCodeSetting getParamCodeSetting() {
        return paramCodeSetting;
    }

    public void setParamCodeSetting(ParamCodeSetting paramCodeSetting) {
        this.paramCodeSetting = paramCodeSetting;
    }

    public List<PowerControlToolchain> getToolchain() {
        return toolchain;
    }

    public void setToolchain(List<PowerControlToolchain> toolchain) {
        this.toolchain = toolchain;
    }

    public PowerControlVariableSetting getVariableSetting() {
        return variableSetting;
    }

    public void setVariableSetting(PowerControlVariableSetting variableSetting) {
        this.variableSetting = variableSetting;
    }
}
