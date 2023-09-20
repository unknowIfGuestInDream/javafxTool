package com.tlcsdm.core.util;

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

    enum Headers {
        ID, CustomerNo, Name
    }
}
