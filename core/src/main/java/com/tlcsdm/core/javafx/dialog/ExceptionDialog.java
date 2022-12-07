package com.tlcsdm.core.javafx.dialog;

import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.util.I18nUtils;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.controlsfx.dialog.ProgressDialog;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

/**
 * @author: 唐 亮
 * @date: 2022/12/7 17:57
 */
public class ExceptionDialog extends Dialog<ButtonType> {
    public ExceptionDialog(Throwable exception) {
        DialogPane dialogPane = this.getDialogPane();
        this.setTitle(I18nUtils.get("core.dialog.exception.dlg.title"));
        dialogPane.setHeaderText(I18nUtils.get("core.dialog.exception.dlg.header"));
        dialogPane.getStyleClass().add("exception-dialog");
        dialogPane.getStylesheets().add(Objects.requireNonNull(ProgressDialog.class.getResource("dialogs.css")).toExternalForm());
        dialogPane.getButtonTypes().addAll(ButtonType.OK);
        this.setContentText(exception.getLocalizedMessage());
        this.initOwner(FxApp.primaryStage);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        String exceptionText = sw.toString();
        Label label = new Label(I18nUtils.get("core.dialog.exception.dlg.label"));
        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        GridPane root = new GridPane();
        root.setMaxWidth(Double.MAX_VALUE);
        root.add(label, 0, 0);
        root.add(textArea, 0, 1);
        dialogPane.setExpandableContent(root);
    }
}
