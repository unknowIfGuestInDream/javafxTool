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

package com.tlcsdm.core.jtidy;

import org.apache.tools.ant.filters.StringInputStream;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author unknowIfGuestInDream
 */
public class TidyTest {

    private String document = """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <!-- 作者信息 -->
            <meta name="author" content="unknowIfGuestInDream, liang.tang.cx@gmail.com">
            <!-- 运用docsify构建 -->
            <meta name="generator" content="docsify 4.12.0"/>
            <meta name="copyright" content="tlcsdm">
            <meta charset="UTF-8">
            <title>知识记录</title>
            <!-- 360浏览器使用谷歌内核 -->
            <meta name="renderer" content="webkit">
            <!-- 搜索关键字 -->
            <meta name="keywords" content="springBoot,linux,springCloud,java,extjs,antd">
            <!-- 以chrome内核来渲染页面 -->
            <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
            <!-- 告知 Google 不希望提供该网页的翻译 -->
            <meta name="google" content="notranslate"/>
            <!-- 设置浏览器图标 -->
            <link rel="icon" href="favicon.ico" type="image/x-icon"/>
            <link rel="shortcut icon" href="favicon.ico" type="image/x-icon"/>
            <link rel=”canonical” href="https://www.tlcsdm.com"/>
            <meta name="description" content="SpringBoot2.x, SpringCloud, Java8, Linux安装软件, Extjs框架学习记录文档, antd">
            <meta name="viewport"
                  content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
            <!-- 默认主题 -->
        <!--    <link rel="stylesheet" href="themes/vue.css">-->
            <!-- Theme: Simple (light + dark) -->
            <link rel="stylesheet" media="(prefers-color-scheme: light)" href="themes/theme-simple.css">
            <link rel="stylesheet" media="(prefers-color-scheme: dark)" href="themes/theme-simple-dark.css">
        <!--    <link rel="stylesheet" href="lib/docsify-top-banner-plugin.css">-->
            <!-- 评论插件-->
            <!--    <script charset="utf-8" type="text/javascript" src="https://cy-cdn.kuaizhan.com/upload/changyan.js"></script>-->
            <!--    <script type="text/javascript">-->
            <!--        window.changyan.api.config({-->
            <!--            appid: 'cyvzsG8gD',-->
            <!--            conf: 'prod_c383ca2e7f8ef624a8e845962f065096'-->
            <!--        });-->
            <!--    </script>-->
        </head>

        <body>
        <!--<nav>-->
        <!--    <a href="https://blog.tlcsdm.com">🐮🐮 TangLiang's Blog</a>-->
        <!--    <a href="https://gitee.com/unknowIfGuestInDream">❤️❤️ Gitee</a>-->
        <!--</nav>-->
        <!-- 定义加载时候的动作 -->
        <div id="app"></div>
        <script>
            // document.oncontextmenu = function () {
            //     Toast('右键菜单已禁用', 1000);
            //     event.returnValue = false;
            // };
            // //禁用开发者工具F12
            // document.onkeydown = document.onkeyup = document.onkeypress = function (event) {
            //     let e = event || window.event || arguments.callee.caller.arguments[0];
            //     if (e && e.keyCode === 123) {
            //         e.returnValue = false;
            //         Toast('开发工具已禁用', 1000);
            //         return false;
            //     }
            // };
            // let userAgent = navigator.userAgent;
            // if (userAgent.indexOf("Firefox") > -1) {
            //     let checkStatus;
            //     let devtools = /./;
            //     devtools.toString = function () {
            //         checkStatus = "on";
            //     };
            //     setInterval(function () {
            //         checkStatus = "off";
            //         console.log(devtools);
            //         console.log(checkStatus);
            //         console.clear();
            //         if (checkStatus === "on") {
            //             let target = "";
            //             try {
            //                 window.open("about:blank", (target = "_self"));
            //             } catch (err) {
            //                 let a = document.createElement("button");
            //                 a.onclick = function () {
            //                     window.open("about:blank", (target = "_self"));
            //                 };
            //                 a.click();
            //             }
            //         }
            //     }, 200);
            // } else {
            //     //禁用控制台
            //     let ConsoleManager = {
            //         onOpen: function () {
            //             alert("Console is opened");
            //         },
            //         onClose: function () {
            //             alert("Console is closed");
            //         },
            //         init: function () {
            //             let self = this;
            //             let x = document.createElement("div");
            //             let isOpening = false,
            //                 isOpened = false;
            //             Object.defineProperty(x, "id", {
            //                 get: function () {
            //                     if (!isOpening) {
            //                         self.onOpen();
            //                         isOpening = true;
            //                     }
            //                     isOpened = true;
            //                     return true;
            //                 }
            //             });
            //             setInterval(function () {
            //                 isOpened = false;
            //                 console.info(x);
            //                 console.clear();
            //                 if (!isOpened && isOpening) {
            //                     self.onClose();
            //                     isOpening = false;
            //                 }
            //             }, 200);
            //         }
            //     };
            //     ConsoleManager.onOpen = function () {
            //         //打开控制台，跳转
            //         let target = "";
            //         try {
            //             window.open("about:blank", (target = "_self"));
            //         } catch (err) {
            //             let a = document.createElement("button");
            //             a.onclick = function () {
            //                 window.open("about:blank", (target = "_self"));
            //             };
            //             a.click();
            //         }
            //     };
            //     ConsoleManager.onClose = function () {
            //         alert("Console is closed!!!!!");
            //     };
            //     ConsoleManager.init();
            // }

            window.$docsify = {
                themeColor: '#42b983',//主题颜色
                // 项目名称
                name: 'Learning',
                // 仓库地址，点击右上角的Github章鱼猫头像会跳转到此地址
                repo: 'https://github.com/unknowIfGuestInDream',
                auto2top: true,//切换页面后是否自动跳转到页面顶部。
                onlyCover: false, //将封面跟文档分开 而不是上下连接
                // 侧边栏支持，默认加载的是项目根目录下的_sidebar.md文件
                loadSidebar: true,
                // 导航栏支持，默认加载的是项目根目录下的_navbar.md文件
                loadNavbar: true,
                // 封面支持，默认加载的是项目根目录下的_coverpage.md文件
                coverpage: true,
                //coverpage: ['/', '/linux/'],
                // 最大支持渲染的标题层级
                maxLevel: 4,
                // 自定义侧边栏后默认不会再生成目录，设置生成目录的最大层级，建议配置为1或者2
                subMaxLevel: 3,
                alias: {
                    '/linux/(.*)':
                        '/linux/$1',
                    '/extjs/(.*)':
                        '/extjs/$1',
                    '/java/(.*)':
                        '/java/$1',
                    '/springCloud/(.*)':
                        '/springCloud/$1',
                    '/database/(.*)':
                        '/database/$1',
                    '/designPattern/(.*)':
                        '/designPattern/$1',
                    '/antd/(.*)':
                        '/antd/$1',
                    '/japanese/(.*)':
                        '/japanese/$1',
                    '/javafx/(.*)':
                        '/javafx/$1',
                    '/.*/_sidebar.md': '/_sidebar.md',//防止意外回退
                    '/.*/_navbar.md': '/_navbar.md'
                },
                notFoundPage: '404.md',
                search: {
                    maxAge: 86400000,
                    paths: 'auto',
                    placeholder: '搜索',
                    noData: '找不到结果',
                    depth: 4,
                    hideOtherSidebarContent: false,
                    namespace: 'website-1',
                },
                count: {
                    countable: true,
                    fontsize: '0.9em',
                    color: 'rgb(90,90,90)',
                    language: 'chinese'
                },
                scrollToTop: {
                    auto: true,
                    text: 'Top',
                    right: 15,
                    bottom: 15,
                    offset: 500
                },
                themeable: {
                    readyTransition : true, // default
                    responsiveTables: true  // default
                },
                tabs: {
                    persist: true,      // default
                    sync: true,      // default
                    theme: 'classic', // default
                    tabComments: true,      // default
                    tabHeadings: true       // default
                },
                // topBanner: {
                //     content: 'Welcome to my website',
                //     defaultTag: 'span',
                //     position: 'fixed',
                //     backgroundColor: '#deebff',
                //     zIndex: '99',
                //     textColor: '#091E42',
                //     linkColor: '#091E42',
                //     textAlign: 'center'
                // },
                // pagination: {
                //     previousText: '上一章节',
                //     nextText: '下一章节',
                //     crossChapter: true,
                //     crossChapterText: true,
                // },
                plugins: [
                    function (hook) {
                        //header模块暂时隐藏
                        var header = [
                            '<p>',
                            '<a href="https://www.aliyun.com/sale-season/2020/procurement-new-members?userCode=44mjc0yx">',
                            '<img src="/images/aliyun_sale.jpg" alt="阿里云" width="640px" height="60px">',
                            '</a>',
                            '</p>'
                        ].join('');
                        var footer = [
                            // '<div id="SOHUCS" ></div>',
                            '<hr/>',
                            '<footer style="text-align: center">',
                            '<img style="width: 16px; height: 16px;" src="images/police.png">',
                            '<span style="margin-left: 5px;"><a href="https://beian.mps.gov.cn/#/query/webSearch?code=21020302000532" rel="noreferrer" target="_blank">辽公网安备21020302000532</a></span>',
                            '<span style="margin-left: 20px;"><a href="https://beian.miit.gov.cn/" target="_blank">辽ICP备2021000033号-1</a></span>',
                            '</footer>'
                        ].join('');
                        hook.afterEach(function (html) {
                            // var isReadme = window.location.href.indexOf("README");
                            var isReadme = 1;
                            if (isReadme === -1) {
                                return header + html + footer;
                            } else {
                                return html + footer;
                            }
                        });
                    },
                    function (hook, vm) {
                        hook.beforeEach(function (html) {
                            var url =
                                'https://github.com/unknowIfGuestInDream/document/tree/master/docs/' +
                                vm.route.file;
                            var editHtml = '[:memo: 修改文档](' + url + ')\\n';

                            return (
                                editHtml +
                                html
                                // + '<a href="https://docsify.js.org" target="_blank" style="color: inherit; font-weight: normal; text-decoration: none;">Powered by docsify</a>'
                                // vercel
                            );
                        });
                    }
                ],
            }

            //提示信息 封装
            // function Toast(msg, duration) {
            //     duration = isNaN(duration) ? 3000 : duration;
            //     var m = document.createElement('div');
            //     m.innerHTML = msg;
            //     m.style.cssText = "font-size: .32rem;color: rgb(255, 255, 255);background-color: rgba(0, 0, 0, 0.6);padding: 10px 15px;margin: 0 0 0 -60px;border-radius: 4px;position: fixed;    top: 50%;left: 50%;width: 130px;text-align: center;";
            //     document.body.appendChild(m);
            //     setTimeout(function () {
            //         var d = 0.5;
            //         m.style.opacity = '0';
            //         setTimeout(function () {
            //             document.body.removeChild(m)
            //         }, d * 1000);
            //     }, duration);
            // }
        </script>
        <!-- docsify的js依赖 -->
        <script src="lib/docsify.min.js"></script>
        <!-- 主题 -->
        <script src="lib/docsify-themeable.min.js"></script>
        <!-- emoji表情支持 -->
        <script src="lib/emoji.min.js"></script>
        <!-- top banner -->
        <!--<script src="lib/docsify-top-banner-plugin.js"></script>-->
        <!-- tab插件 -->
        <script src="lib/docsify-tabs-min.js" defer></script>
        <!-- pdf支持 -->
        <script src="lib/pdfobject.min.js" defer></script>
        <script src="lib/docsify-pdf-embed.js" defer></script>
        <!-- 图片放大缩小支持 -->
        <script src="lib/zoom-image.min.js" defer></script>
        <!-- 搜索功能支持 -->
        <script src="lib/search.js"></script>
        <!-- 复制到剪贴板 -->
        <script src="lib/docsify-copy-code.min.js" defer></script>
        <!-- 字数统计 -->
        <script src="lib/countable.js" defer></script>
        <!-- 回到顶部 -->
        <script src="lib/docsify-scroll-to-top.min.js" defer></script>
        <!--Java代码高亮-->
        <script src="lib/prism-java.js" defer></script>
        <script src="lib/prism-sql.js" defer></script>
        <script src="lib/prism-yaml.js" defer></script>
        <script src="lib/prism-bash.js" defer></script>
        <script src="lib/prism-markdown.min.js" defer></script>
        <script src="lib/prism-nginx.min.js" defer></script>
        <script src="lib/prism-c.min.js" defer></script>
        <script src="lib/prism-cpp.min.js" defer></script>
        <script src="lib/prism-csharp.min.js" defer></script>
        <script src="lib/prism-javascript.min.js" defer></script>
        <script src="lib/prism-php.min.js" defer></script>
        <script src="lib/prism-python.min.js" defer></script>
        <!-- 聊天室 -->
        <!--<script>-->
        <!--((window.gitter = {}).chat = {}).options = {-->
        <!--room: 'TaCheJW/Lobby'-->
        <!--};-->
        <!--</script>-->
        <!--<script src="lib/sidecar.v1.js" async defer></script>-->
        <!-- 分页 -->
        <!--<script src="//unpkg.com/docsify-pagination/dist/docsify-pagination.min.js"></script>-->
        <!-- 百度统计 -->
        <script src="lib/baiduTj.js" async></script>
        </body>
        </html>
        """;

    @Test
    void testTidy() throws IOException, URISyntaxException {
        Tidy tidy = new Tidy();
        tidy.setQuiet(true);  // 设置是否输出警告和错误
        tidy.setXHTML(true);  // 设置是否将输出格式设置为XHTML
        tidy.setInputEncoding("UTF-8");  // 设置输入字符编码
        tidy.setXmlOut(true);    // 输出格式 xml
        tidy.setDropFontTags(true);   // 删除字体节点
        tidy.setDropEmptyParas(true);  // 删除空段落
        tidy.setFixComments(true);   // 修复注释
        tidy.setFixBackslash(true);   // 修复反斜杆
        tidy.setMakeClean(true);   // 删除混乱的表示
        tidy.setQuoteNbsp(false);   // 将空格输出为 &nbsp;
        tidy.setQuoteMarks(false);   // 将双引号输出为 &quot;
        tidy.setQuoteAmpersand(true);  // 将 &amp; 输出为 &
        tidy.setShowWarnings(false);  // 不显示警告信息
        InputStream inputStream = new StringInputStream(document);
        StringWriter sw = new StringWriter();
        tidy.parse(inputStream, sw);
        inputStream.close();
        System.out.println(sw);
    }

    private Templates template;

    /*
     * 解析网页
     * XSLTFileName 用于解析网页的样式表文件名
     * HTMLFileName 待解析的网页文件名
     * OutputFileName 输出文件名
     */
    public void parser(String HTMLFileName, String OutputFileName) {
        if (this.template != null) {
            Document doc = this.HTMLToXML(HTMLFileName); // 解析网页，返回 W3c Document 文档对象
            Transformer(doc, OutputFileName);    // 使用样式表转换 Document 为最终结果
        }
    }

    /**
     * 解析网页，转换为 W3C Document 文档对象
     *
     * @param fileName HTML 网页的文件名
     * @return utf-8 W3C Document 文档对象
     */
    private Document HTMLToXML(String fileName) {
        Logger log = Logger.getLogger("HTMLToXML");
        Document doc = null;
        try {
            FileInputStream in = new FileInputStream(fileName); // 打开文件，转换为 UTF-8 编码
            InputStreamReader isr = new InputStreamReader(in, "GB2312"); // 源文件编码为 gb2312

            File tmpNewFile = File.createTempFile("GB2312", ".html"); // 转换后的文件，设定编码为 utf-8
            FileOutputStream out = new FileOutputStream(tmpNewFile); // 需要将文件转换为字符流
            OutputStreamWriter osw = new OutputStreamWriter(out, "UTF-8");// 指定目标编码为 utf-8
            osw.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");

            char[] buffer = new char[10240];      // 文件缓冲区
            int len = 0;           // 使用字符读取方式，循环读取源文件内容
            while ((len = isr.read(buffer)) != -1)     // 转换后写入目标文件中
            {
                osw.write(buffer, 0, len);
            }
            osw.close();           // 转换完成
            isr.close();
            out.close();
            in.close();

            if (log.isLoggable(Level.INFO)) {
                log.info("HTML 文档转 UTF-8 编码完成！");
            }

            //  设置 tidy ，准备转换
            Tidy tidy = new Tidy();
            tidy.setXmlOut(true);    // 输出格式 xml
            tidy.setDropFontTags(true);   // 删除字体节点
            tidy.setDropEmptyParas(true);  // 删除空段落
            tidy.setFixComments(true);   // 修复注释
            tidy.setFixBackslash(true);   // 修复反斜杆
            tidy.setMakeClean(true);   // 删除混乱的表示
            tidy.setQuoteNbsp(false);   // 将空格输出为 &nbsp;
            tidy.setQuoteMarks(false);   // 将双引号输出为 &quot;
            tidy.setQuoteAmpersand(true);  // 将 &amp; 输出为 &
            tidy.setShowWarnings(false);  // 不显示警告信息
            //tidy.setCharEncoding(Configuration.UTF8); // 文件编码为 UTF8

            FileInputStream src = new FileInputStream(tmpNewFile); //
            doc = tidy.parseDOM(src, null); // 通过 JTidy 将 HTML 网页解析为
            src.close();           // W3C 的 Document 对象
            tmpNewFile.delete();         // 删除临时文件

            NodeList list = doc.getChildNodes();     // 页面中 DOCTYPE 中可能问题
            for (int i = 0; i < list.getLength(); i++)     // 删除 DOCTYPE 元素
            {
                Node node = list.item(i);
                if (node.getNodeType() == Node.DOCUMENT_TYPE_NODE) // 查找类型定义节点
                {
                    node.getParentNode().removeChild(node);
                    if (log.isLoggable(Level.INFO)) {
                        log.info("已经将文档定义节点删除！");
                    }
                }
            }

            list = doc.getElementsByTagName("script");    // 脚本中的注释有时有问题
            for (int i = 0; i < list.getLength(); i++) {     // 清理 script 元素
                Element script = (Element) list.item(i);
                if (script.getFirstChild() != null) {
                    if (log.isLoggable(Level.FINEST)) {
                        log.finest("删除脚本元素: " + script.getFirstChild().getNodeValue());
                    }
                    script.removeChild(script.getFirstChild());
                }
            }

            list = doc.getElementsByTagName("span");    // sina 中 span 元素有时有问题
            for (int i = 0; i < list.getLength(); i++) {     // 清理 span 元素
                Element span = (Element) list.item(i);
                span.getParentNode().removeChild(span);
                if (log.isLoggable(Level.FINEST)) {
                    log.finest("删除 span 元素: ");
                }

            }

            list = doc.getElementsByTagName("sohuadcode");   // 清除 sohuadcode 元素
            for (int i = 0; i < list.getLength(); i++) {
                Element sohuadcode = (Element) list.item(i);
                sohuadcode.getParentNode().removeChild(sohuadcode);
            }

            if (log.isLoggable(Level.INFO)) {
                log.info("HTML 文档解析 DOM 完成.");
            }
        } catch (Exception e) {
            log.severe(e.getMessage());
            e.printStackTrace();
        } finally {

        }
        return doc;
    }

    /**
     * 解析转换的样式表，保存为模板
     *
     * @param xsltFileName 样式表文件名
     * @return 样式表模板对象
     */
    public Templates setXSLT(String xsltFileName) {
        Logger log = Logger.getLogger("setXSLT");
        File xsltFile = new File(xsltFileName);
        StreamSource xsltSource = new StreamSource(xsltFile);  // 使用 JAXP 标准方法建立样式表的模板对象
        TransformerFactory tff = TransformerFactory.newInstance(); // 可以重复利用这个模板
        Templates template = null;
        try {
            template = tff.newTemplates(xsltSource);
            if (log.isLoggable(Level.INFO)) {
                log.info("样式表文件 " + xsltFileName + " 解析完成");
            }
        } catch (TransformerConfigurationException e) {
            log.severe(e.getMessage());
        }
        this.template = template;
        return template;
    }

    /**
     * 使用样式表转换文档对象，得到最终的结果
     *
     * @param doc         文档对象
     * @param outFileName 保存转换结果的文件名
     */
    private void Transformer(Document doc, String outFileName) {
        Logger log = Logger.getLogger("Transformer");
        try {
            Source source = new DOMSource(doc);

            File outFile = new File(outFileName);
            Result result = new StreamResult(outFile);

            Transformer transformer = template.newTransformer(); // 使用保存的样式表模板对象
            transformer.transform(source, result);     // 生成转换器，转换文档对象
            if (log.isLoggable(Level.INFO)) {
                log.info("转换完成, 请查看 " + outFileName + " 文件。");
            }
        } catch (Exception e) {
            log.severe(e.getMessage());
        }
    }
}
