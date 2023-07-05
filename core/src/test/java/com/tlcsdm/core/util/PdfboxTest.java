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

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionJavaScript;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

@DisabledIfSystemProperty(named = "workEnv", matches = "ci")
class PdfboxTest {

    private final static String pdfPath = "C:\\workspace\\test\\box.pdf";
    private final static String resultFloder = "C:\\workspace\\test\\pdfbox";
    private final static String imgPath = "C:\\workspace\\test\\pdfbox\\fuji.jpg";

    @BeforeAll
    public static void init() {
    }

    /**
     * 创建一个空的PDF文档
     */
    @Test
    void create() throws IOException {
        PDDocument document = new PDDocument();
        document.save(resultFloder + File.separator + "demo.pdf");
        document.close();
    }

    /**
     * 创建一个带有空白页面的PDF文档
     */
    @Test
    void page() throws IOException {
        PDDocument document = new PDDocument();
        PDPage my_page = new PDPage();
        document.addPage(my_page);
        document.save(resultFloder + File.separator + "demo.pdf");
        document.close();
    }

    /**
     * 加载现有的PDF文档
     */
    @Test
    void load() throws IOException {
        File file = new File(resultFloder + File.separator + "demo.pdf");
        PDDocument document = PDDocument.load(file);
        PDPage my_page = new PDPage();
        document.addPage(my_page);
        document.save(file);
        document.close();
    }

    /**
     * 从现有文档中删除页面
     */
    @Test
    void del() throws IOException {
        File file = new File(resultFloder + File.separator + "demo.pdf");
        PDDocument document = PDDocument.load(file);
        // 列出页数
        int noOfPages = document.getNumberOfPages();
        Assertions.assertTrue(noOfPages > 0);
        document.removePage(0);
        Assertions.assertEquals(document.getNumberOfPages(), noOfPages - 1);
        document.save(file);
        document.close();
    }

    /**
     * 设置文档属性
     * File 该属性保存文件的名称。
     * Title 使用此属性，可以设置文档的标题。
     * Author 使用此属性，可以设置文档的作者姓名。
     * Subject 使用此属性，可以指定PDF文档的主题。
     * Keywords 使用此属性，列出可以搜索文档的关键字。
     * Created 使用此属性，可以设置为文档修改的日期
     * Application 使用此属性，可以设置文档的应用程序。
     */
    @Test
    void property() throws IOException {
        File file = new File(resultFloder + File.separator + "demo.pdf");
        PDDocument document = PDDocument.load(file);
        PDPage my_page = new PDPage();
        document.addPage(my_page);
        // Creating the PDDocumentInformation object
        PDDocumentInformation pdd = document.getDocumentInformation();
        pdd.setAuthor("unknowIfGuestInDream");
        pdd.setTitle("一个简单的文档标题");
        pdd.setCreator("PDF Examples");
        pdd.setSubject("文档标题");
        // Setting the created date of the document
        Calendar date = new GregorianCalendar();
        date.set(2017, 11, 5);
        pdd.setCreationDate(date);
        // Setting the modified date of the document
        date.set(2018, 10, 5);
        pdd.setModificationDate(date);
        // Setting keywords for the document
        pdd.setKeywords("pdfbox, first example, my pdf");

        document.save(file);

        Assertions.assertEquals("unknowIfGuestInDream", pdd.getAuthor());
        Assertions.assertEquals("一个简单的文档标题", pdd.getTitle());
        Assertions.assertEquals("PDF Examples", pdd.getCreator());
        Assertions.assertEquals("文档标题", pdd.getSubject());
        document.close();
    }

    /**
     * 将文本添加到现有的PDF文档
     * Note:
     * <p>
     * 当文本太多导致一行显示不下时，需要进行换行操作。最简单粗暴的做法就是直接根据字数的多少换行，
     * 此种办法在处理中英文混合的文本时显得有些力不从心，会导致文本显示犬牙交错不整齐。不过，这种办法足以应付大部分的情况。要想精益求精，就得想些办法。
     * 最直接了当的办法就是根据字体和字体大小算出文本实际占用的宽度，进而将文本分成多行再进行显示。在Java
     * JDK中，如果使用Graphics2D，同样也是可以计算文本宽度的。
     * <pre>
     * {@code
     * public static int calCharactersWidth(Graphics2D g, Font font, String text) {
     *    FontRenderContext context = g.getFontRenderContext();
     *  // 获取字体的像素范围对象
     *  Rectangle2D stringBounds = font.getStringBounds(text, context);
     *  int fontWidth = (int)stringBounds.getWidth();
     * return fontWidth;
     * }
     * 在PDFBox中，不必直接用上面的计算方式，而是用它自己的函数。
     * float realWidth = 14 * pdFont.getStringWidth(text) / 1000;
     * }
     * </pre>
     */
    @Test
    void line() throws IOException {
        File file = new File(resultFloder + File.separator + "demo.pdf");
        PDDocument document = PDDocument.load(file);
        PDPage page = document.getPage(0);
        // 使用PDPageContentStream类的对象插入各种数据元素
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        // 开始文本
        contentStream.beginText();
        // 设置文本的位置
        contentStream.newLineAtOffset(25, 500);
        // 设置字体
        PDFont pdFont = PDType1Font.HELVETICA_BOLD;
        contentStream.setFont(pdFont, 14);
        String text = "This is the sample document and we are adding content to it.";

        float realWidth = 14 * pdFont.getStringWidth(text) / 1000;
        System.out.println(realWidth);
        // 插入文本
        contentStream.showText(text);
        contentStream.endText();
        contentStream.close();

        document.save(file);
        document.close();
    }

    /**
     * 多行文本添加
     */
    @Test
    void content() throws IOException {
        File file = new File(resultFloder + File.separator + "demo.pdf");
        PDDocument document = PDDocument.load(file);
        PDPage page = document.getPage(0);
        // 使用PDPageContentStream类的对象插入各种数据元素
        PDPageContentStream contentStream = new PDPageContentStream(document, page, AppendMode.APPEND, true);
        // 开始文本
        contentStream.beginText();
        // 设置文本的位置
        contentStream.newLineAtOffset(25, 725);
        // 设置字体
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 16);
        // 设置文本引导
        contentStream.setLeading(14.5f);
        // 使用newline()插入多个字符串
        String text1 = "This is an example of adding text to a page in the pdf document. we can add as many lines";
        String text2 = "as we want like this using the ShowText()  method of the ContentStream class";
        // 插入文本
        contentStream.showText(text1);
        contentStream.newLine();
        contentStream.newLine();
        contentStream.showText(text2);
        contentStream.newLine();

        contentStream.endText();
        contentStream.close();

        document.save(file);
        document.close();
    }

    /**
     * 从PDF文档中提取文本
     */
    @Test
    void read() throws IOException {
        File file = new File(resultFloder + File.separator + "demo.pdf");
        PDDocument document = PDDocument.load(file);
        PDFTextStripper pdfStripper = new PDFTextStripper();
        // 检索文本
        String text = pdfStripper.getText(document);
        System.out.println(text);

        document.close();
    }

    /**
     * 添加图片
     */
    @Test
    void image() throws IOException {
        File file = new File(resultFloder + File.separator + "demo.pdf");
        PDDocument document = PDDocument.load(file);
        PDPage page = document.getPage(0);
        // 使用PDImageXObject
        PDImageXObject pdImage = PDImageXObject.createFromFile(imgPath, document);
        // 准备内容流
        PDPageContentStream contentStream = new PDPageContentStream(document, page, AppendMode.APPEND, true);
        // 在PDF文档中绘制图像
        contentStream.drawImage(pdImage, 70, 250);
        contentStream.close();

        document.save(file);
        document.close();
    }

    /**
     * 加密PDF文档
     */
    @Test
    void encrypt() throws IOException {
        File file = new File(resultFloder + File.separator + "demo.pdf");
        PDDocument document = PDDocument.load(file);
        AccessPermission accessPermission = new AccessPermission();
        // 通过传递所有者密码，用户密码和AccessPermission对象来实例化StandardProtectionPolicy类
        StandardProtectionPolicy spp = new StandardProtectionPolicy("1234", "1234", accessPermission);
        // 设置加密密钥的长度
        spp.setEncryptionKeyLength(128);
        // 设置权限
        spp.setPermissions(accessPermission);
        // 保护文档
        document.protect(spp);

        document.save(resultFloder + File.separator + "pswd.pdf");
        document.close();
    }

    /**
     * 将JavaScript添加到PDF文档
     */
    @Test
    void jscript() throws IOException {
        File file = new File(resultFloder + File.separator + "demo.pdf");
        PDDocument document = PDDocument.load(file);
        String javaScript = "app.alert( {cMsg: 'this is an example', nIcon: 3,"
            + " nType: 0,cTitle: 'PDFBox Javascript example' } );";
        PDActionJavaScript PDAjavascript = new PDActionJavaScript(javaScript);
        // 嵌入 JavaScript
        document.getDocumentCatalog().setOpenAction(PDAjavascript);

        document.save(resultFloder + File.separator + "demo.pdf");
        document.close();
    }

    /**
     * 分割PDF文档中的页面
     */
    @Test
    void split() throws IOException {
        File file = new File(resultFloder + File.separator + "demo.pdf");
        PDDocument document = PDDocument.load(file);
        Splitter splitter = new Splitter();
        List<PDDocument> Pages = splitter.split(document);
        Iterator<PDDocument> iterator = Pages.listIterator();
        int i = 1;
        while (iterator.hasNext()) {
            PDDocument pd = iterator.next();
            pd.save(resultFloder + File.separator + "demo" + i + ".pdf");
            i = i + 1;
        }
        document.close();
    }

    /**
     * 合并多个PDF文档
     */
    @Test
    void merge() throws IOException {
        File file1 = new File(resultFloder + File.separator + "demo1.pdf");
        PDDocument document1 = PDDocument.load(file1);
        File file2 = new File(resultFloder + File.separator + "demo2.pdf");
        PDDocument document2 = PDDocument.load(file2);
        PDFMergerUtility PDFmerger = new PDFMergerUtility();
        // 设置目标文件
        PDFmerger.setDestinationFileName(resultFloder + File.separator + "merge.pdf");
        // 设置源文件
        PDFmerger.addSource(file1);
        PDFmerger.addSource(file2);
        // 合并文档
        MemoryUsageSetting memUsageSetting = MemoryUsageSetting.setupMainMemoryOnly();
        PDFmerger.mergeDocuments(memUsageSetting);

        document1.close();
        document2.close();
    }

    /**
     * 从PDF文档生成图像
     */
    @Test
    void getImg() throws IOException {
        File file = new File(resultFloder + File.separator + "demo.pdf");
        PDDocument document = PDDocument.load(file);
        // PDFRenderer类将PDF文档呈现到AWT BufferedImage中
        PDFRenderer renderer = new PDFRenderer(document);
        // 从PDF文档渲染图像
        BufferedImage image = renderer.renderImage(0);
        // 将图像写入文件
        ImageIO.write(image, "JPEG", new File(resultFloder + File.separator + "image.jpg"));

        document.close();
    }

    /**
     * 在PDF文档中创建框
     */
    @Test
    void draw() throws IOException {
        File file = new File(resultFloder + File.separator + "demo.pdf");
        PDDocument document = PDDocument.load(file);
        PDPage page = document.getPage(1);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        // 设置不划线颜色, 使用PDPageContentStream类的setNonStrokingColor()方法将非划线颜色设置为矩形。
        // 对于这个方法，需要将所需的颜色作为参数传递
        PDColor red = new PDColor(new float[]{1, 0, 0}, PDDeviceRGB.INSTANCE);
        contentStream.setNonStrokingColor(red);
        // 绘制矩形
        contentStream.addRect(200, 650, 100, 100);
        // 填充矩形
        contentStream.fill();
        contentStream.close();
        document.save(resultFloder + File.separator + "demo.pdf");
        document.close();
    }

}
