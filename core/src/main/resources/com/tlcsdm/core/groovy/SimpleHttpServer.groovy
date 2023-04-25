package com.tlcsdm.core.groovy

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import groovy.transform.CompileStatic
import org.codehaus.groovy.runtime.IOGroovyMethods

import java.util.concurrent.Executors
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

/**
 * SimpleHTTPServer for Groovy, inspired by Python's SimpleHTTPServer
 */
@CompileStatic
class SimpleHttpServer {
    private HttpServer server;
    private int port;
    private String contextRoot;
    private File docBase;

    SimpleHttpServer() throws IOException {
        this(8000);
    }

    SimpleHttpServer(final int port) throws IOException {
        this(port, "/", new File("."));
    }

    SimpleHttpServer(final int port, final String contextRoot, final File docBase) throws IOException {
        this.port = port;
        this.contextRoot = contextRoot.startsWith("/") ? contextRoot : ("/" + contextRoot);
        this.docBase = docBase;

        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.setExecutor(Executors.newCachedThreadPool());
        server.createContext(this.contextRoot, new HttpHandler() {
            @Override
            void handle(HttpExchange exchg) throws IOException {
                BufferedOutputStream bos = new BufferedOutputStream(exchg.getResponseBody());
                byte[] content = null;

                try {
                    String uri = exchg.getRequestURI().getPath();
                    String path =
                        !"/".equals(SimpleHttpServer.this.contextRoot) && uri.startsWith(SimpleHttpServer.this.contextRoot) ? uri.substring(SimpleHttpServer.this.contextRoot.length()) : uri;

                    content = readContent(docBase, path);
                    exchg.sendResponseHeaders(HttpURLConnection.HTTP_OK, content.length);
                    bos.write(content);
                } catch (Exception e) {
                    content = e.getMessage().getBytes();
                    exchg.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, content.length);
                    bos.write(content);
                } finally {
                    bos.close();
                    exchg.close();
                }
            }
        });
    }

    private byte[] readContent(File docBase, String path) throws IOException {
        if ("/".equals(path)) {
            return "Groovy SimpleHTTPServer is running".getBytes();
        } else {
            if (docBase.isDirectory()) {
                return readFile(docBase, path);
            } else {
                return readZipEntry(docBase, path);
            }
        }
    }

    private byte[] readFile(File docBase, String path) throws IOException {
        File file = new File((docBase.getCanonicalPath() + File.separator + path).trim());

        if (file.isDirectory()) {
            return ("Accessing the directory[" + file.getCanonicalPath() + "] is forbidden").getBytes();
        } else {
            return IOGroovyMethods.getBytes(
                new BufferedInputStream(
                    new FileInputStream(file)));
        }
    }

    private byte[] readZipEntry(File docBase, String entryName) throws IOException {
        entryName = entryName.startsWith("/") ? entryName.substring(1) : entryName;

        ZipFile zf = new ZipFile(docBase);
        try {
            ZipEntry ze = new ZipEntry(entryName);

            if (ze.isDirectory()) {
                return ("Accessing the directory entry[" + ze.name + "] is forbidden").getBytes();
            }

            BufferedInputStream bis =
                new BufferedInputStream(
                    zf.getInputStream(ze))
            return IOGroovyMethods.getBytes(bis);
        } finally {
            zf.close();
        }
    }

    void start() {
        server.start();
        System.out.println("HTTP Server started up, visit http://localhost:" + this.port + this.contextRoot + " to access the files in the " + this.docBase.getCanonicalPath());
    }

    static void main(String[] args) {
        int argCnt = args.length;
        SimpleHttpServer shs;

        if (0 == argCnt) {
            shs = new SimpleHttpServer();
        } else if (1 == argCnt) {
            shs = new SimpleHttpServer(args[0] as int);
        } else if (2 == argCnt) {
            shs = new SimpleHttpServer(args[0] as int, "/", new File(args[1]));
        } else if (3 == argCnt) {
            shs = new SimpleHttpServer(args[0] as int, args[2], new File(args[1]));
        } else {
            throw new IllegalArgumentException("Too many arguments: " + (args as List) + ", usage: groovy SimpleHttpServer <port> <base dir|zip file> <context root>");
        }

        shs.start();
    }
}
