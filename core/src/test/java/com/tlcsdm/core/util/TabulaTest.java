/*
 * Copyright (c) 2023 unknowIfGuestInDream
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

import cn.hutool.core.io.FileUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import technology.tabula.CommandLineApp;
import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.PageIterator;
import technology.tabula.RectangularTextContainer;
import technology.tabula.Table;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;
import technology.tabula.writers.CSVWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@DisabledIfSystemProperty(named = "workEnv", matches = "ci")
public class TabulaTest {

    private final static String pdfPath = "E:\\testPlace\\result\\tabula\\单片机基础2.pdf";
    private final static String expectCsvPath = "E:\\testPlace\\result\\tabula\\tabula.csv";

    @Test
    void demo() throws IOException {
        InputStream in = FileUtil.getInputStream(pdfPath);
        try (PDDocument document = Loader.loadPDF(new RandomAccessReadBuffer(in))) {
            SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
            PageIterator pi = new ObjectExtractor(document).extract();
            while (pi.hasNext()) {
                // iterate over the pages of the document
                Page page = pi.next();
                List<Table> table = sea.extract(page);
                (new CSVWriter()).write(new FileWriter(FileUtil.file(expectCsvPath)), table);
                // iterate over the tables of the page
                for (Table tables : table) {
                    List<List<RectangularTextContainer>> rows = tables.getRows();
                    // iterate over the rows of the table
                    for (List<RectangularTextContainer> cells : rows) {
                        // print all column-cells of the row plus linefeed
                        for (RectangularTextContainer content : cells) {
                            // Note: Cell.getText() uses \r to concat text chunks
                            String text = content.getText().replace("\r", " ");
                            System.out.print(text + "|");
                        }
                        System.out.println();
                    }
                }
            }
        }
    }

    @Test
    public void command() throws IOException {
        String[] ar = {"-o=" + expectCsvPath, "-p=all", "-l", "-t", pdfPath};
        CommandLineApp.main(ar);
    }

    public Page getAreaFromFirstPage(String path, float top, float left, float bottom, float right) throws IOException {
        return getAreaFromPage(path, 1, top, left, bottom, right);
    }

    public Page getAreaFromPage(String path, int page, float top, float left, float bottom, float right)
        throws IOException {
        return getPage(path, page).getArea(top, left, bottom, right);
    }

    public Page getPage(String path, int pageNumber) throws IOException {
        ObjectExtractor oe = null;
        try {
            PDDocument document = Loader.loadPDF(new File(path));
            oe = new ObjectExtractor(document);
            Page page = oe.extract(pageNumber);
            return page;
        } finally {
            if (oe != null)
                oe.close();
        }
    }

    public String[][] tableToArrayOfRows(Table table) {
        List<List<RectangularTextContainer>> tableRows = table.getRows();

        int maxColCount = 0;

        for (int i = 0; i < tableRows.size(); i++) {
            List<RectangularTextContainer> row = tableRows.get(i);
            if (maxColCount < row.size()) {
                maxColCount = row.size();
            }
        }

        String[][] rv = new String[tableRows.size()][maxColCount];

        for (int i = 0; i < tableRows.size(); i++) {
            List<RectangularTextContainer> row = tableRows.get(i);
            for (int j = 0; j < row.size(); j++) {
                rv[i][j] = table.getCell(i, j).getText();
            }
        }

        return rv;
    }

    public String loadJson(String path) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }

        return stringBuilder.toString();

    }

    public String loadCsv(String path) throws IOException {

        StringBuilder out = new StringBuilder();
        CSVParser parse = org.apache.commons.csv.CSVParser.parse(new File(path), StandardCharsets.UTF_8,
            CSVFormat.EXCEL);

        CSVPrinter printer = new CSVPrinter(out, CSVFormat.EXCEL);
        printer.printRecords(parse);
        printer.close();

        String csv = out.toString().replaceAll("(?<!\r)\n", "\r");
        return csv;

    }

}
