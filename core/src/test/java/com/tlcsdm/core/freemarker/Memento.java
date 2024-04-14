/*
 * Copyright (c) 2024 unknowIfGuestInDream.
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

package com.tlcsdm.core.freemarker;

import cn.hutool.log.StaticLog;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class Memento extends ReadOnlyMemento {

    public Memento(String type) {
        this(type, "");
    }

    public Memento(String type, String id) {
        super(type, id);
    }

    public void putChild(Memento child) {
        String type = child.getType();
        if (!this.typeMap.containsKey(type)) {
            this.typeMap.put(type, new LinkedHashSet<>());
        }

        Set<Memento> typeChildren = this.typeMap.get(type);
        typeChildren.add(child);

        String id = child.getId();
        if (!this.idMap.containsKey(id)) {
            this.idMap.put(id, new HashSet<>());
        }

        Set<Memento> idChildren = this.idMap.get(id);
        idChildren.add(child);

    }

    public void putChildren(Collection<Memento> children) {
        for (Memento child : children) {
            putChild(child);
        }
    }

    public void removeChild(String type) {
        Set<Memento> typeChildren = this.typeMap.get(type);
        if (typeChildren == null) {
            return;
        }

        Memento targetChild = null;
        for (Memento child : typeChildren) {
            targetChild = child;
        }

        if (targetChild == null) {
            return;
        }

        typeChildren.remove(targetChild);
        this.idMap.get(targetChild.getId()).remove(targetChild);
    }

    public void removeChild(String type, String id) {
        Collection<Memento> idChildren = this.idMap.get(id);
        if (idChildren == null) {
            return;
        }

        Memento targetChild = null;
        for (Memento child : idChildren) {
            if (child.getType().equals(type)) {
                targetChild = child;
            }
        }

        if (targetChild == null) {
            return;
        }

        idChildren.remove(targetChild);
        this.typeMap.get(targetChild.getType()).remove(targetChild);
    }

    public void cleanChildren(String type) {
        Set<Memento> removedChildren = this.typeMap.remove(type);
        if (removedChildren == null) {
            return;
        }

        for (Memento child : removedChildren) {
            this.idMap.get(child.getId()).remove(child);
        }
    }

    public void cleanChildren() {
        this.idMap.clear();
        this.typeMap.clear();
    }

    public void putFloat(String key, float value) {

        this.attributes.put(key, String.valueOf(value));
    }

    public void putDouble(String key, double value) {

        this.attributes.put(key, String.valueOf(value));
    }

    public void putInteger(String key, int value) {

        this.attributes.put(key, String.valueOf(value));
    }

    public void putLong(String key, long value) {

        this.attributes.put(key, String.valueOf(value));
    }

    public void putString(String key, String value) {
        this.attributes.put(key, value);
    }

    public void putBoolean(String key, boolean value) {

        this.attributes.put(key, String.valueOf(value));
    }

    public static final String Memento_Id = "id";

    public static void save(Writer writer, Memento memento) {
        if (memento == null) {
            return;
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element rootElement = document.createElement(memento.getType());
            document.appendChild(rootElement);

            parseMemento(document, rootElement, memento);

            DOMSource domSource = new DOMSource(document);
            StreamResult result = new StreamResult(writer);
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(domSource, result);
        } catch (ParserConfigurationException e) {
            StaticLog.error(e);
        } catch (TransformerException e) {
            StaticLog.error(e);
        }
    }

    private static void parseMemento(Document document, Element parentElement, Memento parentMemento) {

        if (!"".equals(parentMemento.getId())) {
            parentElement.setAttribute(Memento_Id, parentMemento.getId());
        }

        for (String key : parentMemento.getAttributeKeys()) {
            parentElement.setAttribute(key, parentMemento.getString(key));
        }

        for (Memento childMemento : parentMemento.getChildren()) {
            Element childElement = document.createElement(childMemento.getType());
            parentElement.appendChild(childElement);

            parseMemento(document, childElement, childMemento);
        }
    }

    public static Memento load(Reader reader) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(reader);
            Document doc = builder.parse(inputSource);
            Element rootElement = doc.getDocumentElement();
            return parseElement(rootElement);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            StaticLog.error(e);
        }

        return null;
    }

    private static Memento parseElement(Element element) {

        String type = element.getTagName();
        String id = element.getAttribute(Memento_Id);
        Memento memento = new Memento(type, id);
        NamedNodeMap map = element.getAttributes();
        for (int i = 0; i < map.getLength(); i++) {
            Attr attr = (Attr) map.item(i);
            if (attr.getName().equals(Memento.Memento_Id)) {
                continue;
            }
            memento.putString(attr.getName(), attr.getValue());
        }

        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                memento.putChild(parseElement((Element) node));
            }
        }

        return memento;
    }
}
