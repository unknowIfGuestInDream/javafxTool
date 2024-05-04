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

import cn.hutool.core.io.resource.ResourceUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * commons-csv test.
 *
 * @author unknowIfGuestInDream
 */
@DisabledIfSystemProperty(named = "workEnv", matches = "ci")
class CsvTest {

    private final static String csvPath = "C:\\workspace\\test\\csv\\cccie31_1.csv";
    private final static String outPath = "C:\\workspace\\test\\csv\\result.csv";

    /**
     * 解析 Excel CSV 文件.
     */
    @Test
    void readCsv() throws UnsupportedEncodingException, IOException {
        Reader in = new InputStreamReader(Files.newInputStream(Paths.get(csvPath)), StandardCharsets.UTF_8);
        Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
        for (CSVRecord record : records) {
            String field_1 = record.get(0);
            String field_2 = record.get(1);
            System.out.println(field_1);
            System.out.println(field_2);
        }
    }

    /**
     * 使用自定义标头.
     */
    @Test
    void readWithHeader() throws UnsupportedEncodingException, IOException {
        Reader in = new InputStreamReader(Files.newInputStream(Paths.get(csvPath)), StandardCharsets.UTF_8);
        Iterable<CSVRecord> records = CSVFormat.EXCEL.builder().setHeader("ID", "CustomerNo", "Name").build().parse(in);
        for (CSVRecord record : records) {
            String field_1 = record.get("ID");
            String field_2 = record.get("Name");
            System.out.println(field_1);
            System.out.println(field_2);
        }
    }

    /**
     * 使用枚举标头.
     */
    @Test
    void readWithEnumHeader() throws UnsupportedEncodingException, IOException {
        Reader in = new InputStreamReader(Files.newInputStream(Paths.get(csvPath)), StandardCharsets.UTF_8);
        Iterable<CSVRecord> records = CSVFormat.EXCEL.builder().setHeader(Headers.class).build().parse(in);
        for (CSVRecord record : records) {
            String field_1 = record.get("ID");
            String field_2 = record.get("Name");
            System.out.println(field_1);
            System.out.println(field_2);
        }
    }

    /**
     * 标头自动检测.
     */
    @Test
    void readWithAutoHeader() throws UnsupportedEncodingException, IOException {
        Reader in = new InputStreamReader(Files.newInputStream(Paths.get(csvPath)), StandardCharsets.UTF_8);
        Iterable<CSVRecord> records = CSVFormat.EXCEL.builder().setHeader().setSkipHeaderRecord(true).build().parse(in);
        for (CSVRecord record : records) {
            String field_1 = record.get(0);
            String field_2 = record.get(1);
            System.out.println(field_1);
            System.out.println(field_2);
        }
    }

    /**
     * 使用标题打印.
     */
    @Test
    void print() throws IOException {
        CSVPrinter csvPrinter = CSVFormat.DEFAULT.builder().setHeader("Index", "Girth", "Height", "Volume").build()
            .print(new File(outPath), StandardCharsets.UTF_8);
        csvPrinter.printRecord("1", "8.3", "70", "10.3");
        csvPrinter.flush();
    }

    @Test
    void readCie1931CC() throws IOException {
        Reader in = new InputStreamReader(ResourceUtil.getResource("util/dali/cie_1931_2deg_xyz_cc.csv").openStream());
        Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
        for (CSVRecord record : records) {
            String field_1 = record.get(0);
            String field_2 = record.get(1);
            String field_3 = record.get(2);
            String field_4 = record.get(3);
            System.out.println(field_1);
            System.out.println(field_2);
            System.out.println(field_3);
            System.out.println(field_4);
        }
    }

    @Test
    void readCie1931Xyz() throws IOException {
        Reader in = new InputStreamReader(ResourceUtil.getResource("util/dali/cie_1931_2deg_xyz.csv").openStream());
        Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
        for (CSVRecord record : records) {
            String field_1 = record.get(0);
            String field_2 = record.get(1);
            String field_3 = record.get(2);
            String field_4 = record.get(3);
            System.out.println(field_1);
            System.out.println(field_2);
            System.out.println(field_3);
            System.out.println(field_4);
        }
    }

    enum Headers {
        ID, CustomerNo, Name
    }
}
