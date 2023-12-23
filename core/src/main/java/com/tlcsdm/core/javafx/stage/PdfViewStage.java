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

package com.tlcsdm.core.javafx.stage;

import cn.hutool.log.StaticLog;
import com.dlsc.pdfviewfx.PDFView;
import com.dlsc.pdfviewfx.PDFView.Document;
import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.util.I18nUtils;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.InputStream;
import java.util.function.Supplier;

/**
 * PDF View
 *
 * @author unknowIfGuestInDream
 */
public class PdfViewStage extends Stage {
    private FileChooser chooser;
    private final PDFView pdfView;

    public PdfViewStage() {
        this.initOwner(FxApp.primaryStage);
        FxApp.setupIcon(this);
        pdfView = new PDFView();
        MenuItem loadItem = new MenuItem(I18nUtils.get("core.stage.pdfView.menu.file.load"));
        loadItem.setAccelerator(KeyCombination.valueOf("SHORTCUT+o"));
        loadItem.setOnAction(evt -> {
            if (chooser == null) {
                chooser = new FileChooser();
                chooser.setTitle(I18nUtils.get("core.stage.pdfView.chooser.title"));
                final ExtensionFilter filter = new ExtensionFilter("PDF Files", "*.pdf");
                chooser.getExtensionFilters().add(filter);
                chooser.setSelectedExtensionFilter(filter);
            }

            final File file = chooser.showOpenDialog(pdfView.getScene().getWindow());
            if (file != null) {
                pdfView.load(file);
            }
        });

        MenuItem closeItem = new MenuItem(I18nUtils.get("core.stage.pdfView.menu.file.close"));
        closeItem.setAccelerator(KeyCombination.valueOf("SHORTCUT+c"));
        closeItem.setOnAction(evt -> pdfView.unload());
        closeItem.disableProperty().bind(Bindings.isNull(pdfView.documentProperty()));

        MenuItem printItem = new MenuItem(I18nUtils.get("core.stage.pdfView.menu.file.print"));
        printItem.setAccelerator(KeyCombination.valueOf("SHORTCUT+p"));
        printItem.setOnAction(evt -> SwingUtilities.invokeLater(() -> {
            Document pdfDoc = pdfView.getDocument();
            if (pdfDoc != null) {
                PrinterJob job = PrinterJob.getPrinterJob();
                job.setPageable(pdfDoc.getPageable());
                if (job.printDialog()) {
                    try {
                        job.print();
                    } catch (PrinterException e) {
                        StaticLog.error(e);
                    }
                }
            }
        }));
        printItem.disableProperty().bind(Bindings.isNull(pdfView.documentProperty()));

        Menu fileMenu = new Menu(I18nUtils.get("core.stage.pdfView.menu.file"));
        ObservableList<MenuItem> fileMenuItems = fileMenu.getItems();
        fileMenuItems.add(loadItem);
        fileMenuItems.add(closeItem);
        fileMenuItems.add(new SeparatorMenuItem());
        fileMenuItems.add(printItem);

        MenuBar menuBar = new MenuBar(fileMenu);
        menuBar.setUseSystemMenuBar(false);

        VBox.setVgrow(pdfView, Priority.ALWAYS);
        VBox box = new VBox(menuBar, pdfView);
        box.setFillWidth(true);

        Scene scene = new Scene(box);
        setTitle(I18nUtils.get("core.stage.pdfView.title"));
        setWidth(1000);
        setHeight(900);
        setScene(scene);
        centerOnScreen();
    }

    public void loadPdf(File file) {
        try {
            pdfView.load(file);
        } catch (Exception e) {
            StaticLog.error(e);
        }
    }

    public void loadPdf(InputStream stream) {
        try {
            pdfView.load(stream);
        } catch (Exception e) {
            StaticLog.error(e);
        }
    }

    public void loadPdf(Supplier<Document> supplier) {
        try {
            pdfView.load(supplier);
        } catch (Exception e) {
            StaticLog.error(e);
        }
    }

    public PDFView getPdfView() {
        return this.pdfView;
    }

    public void showStage() {
        this.show();
        this.toFront();
    }
}
