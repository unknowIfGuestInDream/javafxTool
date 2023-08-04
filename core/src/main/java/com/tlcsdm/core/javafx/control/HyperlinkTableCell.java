package com.tlcsdm.core.javafx.control;

import com.tlcsdm.core.util.CoreUtil;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.util.function.Function;

/**
 * Hyperlink Cell for TableView
 * 
 * @author: unknowIfGuestInDream
 * @date: 2023/8/4 13:43
 */
public class HyperlinkTableCell<S, T> extends TableCell<S, T> {

    private Hyperlink hyperlink;
    Function<S, ?> func;

    public HyperlinkTableCell(Function<S, ?> func) {
        this.hyperlink = new Hyperlink();
        this.hyperlink.setUnderline(false);
        this.func = func;
        setAlignment(Pos.CENTER);
        setGraphic(hyperlink);
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            setGraphic(hyperlink);
            getTableRow();
            getTableView().getItems();
            hyperlink.setText(getTableColumn().getCellObservableValue(getIndex()).getValue().toString());
            hyperlink.setOnAction(e -> {
                String url = func.apply(getTableView().getItems().get(getIndex())).toString();
                if (!url.isEmpty()) {
                    CoreUtil.openWeb(url);
                }
            });
        }
    }

    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(Function<S, ?> func) {
        return paramTableColumn -> new HyperlinkTableCell<>(func);
    }
}
