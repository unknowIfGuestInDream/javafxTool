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

package com.tlcsdm.core.util;

import cn.hutool.log.StaticLog;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.StringReader;
import java.io.StringWriter;

/**
 * Jaxb 工具类.
 *
 * @author unknowIfGuestInDream
 */
public class JaxbUtils {

    /**
     * Xml转换为JavaBean.
     */
    public static <T> T xmlToBean(String xmlStr, Class<T> clsLoader) {
        JAXBContext context;
        StringReader reader;
        T res = null;
        try {
            //1、创建context
            context = JAXBContext.newInstance(clsLoader);
            //2、创建Unmarshaller
            Unmarshaller unmarshaller = context.createUnmarshaller();
            //3、实例化reader，将xml字符串放入参数
            reader = new StringReader(xmlStr);
            //4、使用unmarshaller.unmarshal 参数放入read
            res = (T) unmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            StaticLog.error(e);
        }
        //5、输出结果
        return res;
    }

    /**
     * JavaBean转换xml.
     */
    public static <T> String beanToXml(T t) throws JAXBException {
        JAXBContext context;
        StringWriter writer;
        //1、创建上下文
        context = JAXBContext.newInstance(t.getClass());
        //2、创建Marshaller 这个是用于转换成xml的
        Marshaller marshaller = context.createMarshaller();
        //3、设置marshaller的属性
        //JAXB_FORMATTED_OUTPUT：格式化
        //JAXB_ENCODING:编码格式
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        //4、实例化StringWriter
        writer = new StringWriter();
        //5、将实体类读取到writer中
        marshaller.marshal(t, writer);
        //6、输出结果
        return writer.toString();
    }
}
