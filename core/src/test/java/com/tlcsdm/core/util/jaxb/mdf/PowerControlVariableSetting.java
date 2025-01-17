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
public class PowerControlVariableSetting {
    @XmlElement(name = "tab", required = true)
    protected List<Tab> tabList;

    public List<Tab> getTabList() {
        return tabList;
    }

    public void setTabList(List<Tab> tabList) {
        this.tabList = tabList;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Tab {
        @XmlAttribute(name = "name", required = true)
        protected String name;
        @XmlAttribute(name = "jpName")
        protected String jpName;
        @XmlElement(name = "condition")
        protected Condition condition;
        @XmlElement(name = "variable")
        protected List<Variable> variableList;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getJpName() {
            return jpName;
        }

        public void setJpName(String jpName) {
            this.jpName = jpName;
        }

        public Condition getCondition() {
            return condition;
        }

        public void setCondition(Condition condition) {
            this.condition = condition;
        }

        public List<Variable> getVariableList() {
            return variableList;
        }

        public void setVariableList(List<Variable> variableList) {
            this.variableList = variableList;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Variable {
        @XmlAttribute(name = "address")
        protected String address;
        @XmlAttribute(name = "name", required = true)
        protected String name;
        @XmlAttribute(name = "displayName")
        protected String displayName;
        @XmlAttribute(name = "description")
        protected String description;
        @XmlAttribute(name = "bitSize")
        protected Integer bitSize;
        @XmlAttribute(name = "rwPermission")
        protected String rwPermission;
        @XmlElement(name = "condition")
        protected Condition condition;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getBitSize() {
            return bitSize;
        }

        public void setBitSize(int bitSize) {
            this.bitSize = bitSize;
        }

        public String getRwPermission() {
            return rwPermission;
        }

        public void setRwPermission(String rwPermission) {
            this.rwPermission = rwPermission;
        }

        public Condition getCondition() {
            return condition;
        }

        public void setCondition(Condition condition) {
            this.condition = condition;
        }
    }
}
