package com.tlcsdm.core.javafx.richtext;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Properties TextArea
 *
 * @author: unknowIfGuestInDream
 * @date: 2023/8/4 10:03
 */
public class PropertiesArea extends CodeArea {

    public PropertiesArea() {
        super();
        getStyleClass().add("text-properties-area");
        getStylesheets()
            .add(getClass().getResource("/com/tlcsdm/core/static/javafx/richtext/properties.css").toExternalForm());
        this.setParagraphGraphicFactory(LineNumberFactory.get(this));
        this.textProperty().addListener((obs, oldText, newText) -> {
            this.setStyleSpans(0, computeHighlighting(newText));
        });
    }

    private StyleSpans<Collection<String>> computeHighlighting(String text) {
        List<String> list = StrUtil.split(text, "\n");
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        for (String str : list) {
            List<String> l = CharSequenceUtil.split(str, "=");
            if (l.size() != 2) {
                continue;
            }
            spansBuilder.add(Collections.singleton("propertyKey"), l.get(0).length());
            spansBuilder.add(Collections.singleton("propertyEqu"), 1);
            spansBuilder.add(Collections.emptyList(), l.get(1).length() + 1);
        }
        return spansBuilder.create();
    }

}
