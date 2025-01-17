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

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class ParamUISetting {
    @XmlElement(name = "sectionId", required = true)
    protected String sectionId;
    @XmlElementWrapper(name = "groups")
    @XmlElement(name = "group")
    protected List<Group> groupList;
    @XmlElementWrapper(name = "configuration")
    @XmlElement(name = "property")
    protected List<Property> propertyList;

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }

    public List<Property> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List<Property> propertyList) {
        this.propertyList = propertyList;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Group {

        @XmlAttribute(name = "id", required = true)
        protected String id;
        @XmlAttribute(name = "name", required = true)
        protected String name;
        @XmlAttribute(name = "jpName", required = true)
        protected String jpName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

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
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Property {

        @XmlAttribute(name = "id", required = true)
        protected String id;
        @XmlAttribute(name = "default", required = true)
        protected String defaultS;
        @XmlAttribute(name = "name", required = true)
        protected String name;
        @XmlAttribute(name = "jpName")
        protected String jpName;
        @XmlAttribute(name = "type")
        protected String type;
        @XmlAttribute(name = "conditionalDefault")
        protected String conditionalDefault;
        @XmlAttribute(name = "groupId")
        protected String groupId;
        @XmlAttribute(name = "tooltipImageURL")
        protected String tooltipImageURL;
        @XmlAttribute(name = "tooltip")
        protected String tooltip;
        @XmlAttribute(name = "jptooltip")
        protected String jptooltip;
        @XmlElement(name = "condition")
        protected Condition condition;
        @XmlElement(name = "item")
        protected List<PropertyItem> itemList;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDefaultS() {
            return defaultS;
        }

        public void setDefaultS(String defaultS) {
            this.defaultS = defaultS;
        }

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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getConditionalDefault() {
            return conditionalDefault;
        }

        public void setConditionalDefault(String conditionalDefault) {
            this.conditionalDefault = conditionalDefault;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getTooltipImageURL() {
            return tooltipImageURL;
        }

        public void setTooltipImageURL(String tooltipImageURL) {
            this.tooltipImageURL = tooltipImageURL;
        }

        public String getTooltip() {
            return tooltip;
        }

        public void setTooltip(String tooltip) {
            this.tooltip = tooltip;
        }

        public String getJptooltip() {
            return jptooltip;
        }

        public void setJptooltip(String jptooltip) {
            this.jptooltip = jptooltip;
        }

        public Condition getCondition() {
            return condition;
        }

        public void setCondition(Condition condition) {
            this.condition = condition;
        }

        public List<PropertyItem> getItemList() {
            return itemList;
        }

        public void setItemList(List<PropertyItem> itemList) {
            this.itemList = itemList;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class PropertyItem {
        @XmlAttribute(name = "id", required = true)
        protected String id;
        @XmlAttribute(name = "name", required = true)
        protected String name;
        @XmlAttribute(name = "jpName")
        protected String jpName;
        @XmlAttribute(name = "value")
        protected String value;
        @XmlElement(name = "condition")
        protected Condition condition;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

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

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Condition getCondition() {
            return condition;
        }

        public void setCondition(Condition condition) {
            this.condition = condition;
        }
    }
}
