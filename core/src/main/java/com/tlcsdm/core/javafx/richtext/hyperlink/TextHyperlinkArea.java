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

package com.tlcsdm.core.javafx.richtext.hyperlink;

import javafx.geometry.VPos;
import org.fxmisc.richtext.GenericStyledArea;
import org.fxmisc.richtext.TextExt;
import org.fxmisc.richtext.model.ReadOnlyStyledDocument;
import org.fxmisc.richtext.model.SegmentOps;
import org.fxmisc.richtext.model.StyledDocument;
import org.fxmisc.richtext.model.TextOps;
import org.reactfx.util.Either;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * <pre>{@code
 * Consumer<String> showLink = (string) -> {
 * try {
 * Desktop.getDesktop().browse(new URI(string));
 * } catch (IOException | URISyntaxException e) {
 * throw new RuntimeException(e);
 * }
 * };
 * TextHyperlinkArea area = new TextHyperlinkArea(showLink);
 * area.appendText("Some text in the area\n");
 * area.appendWithLink("Google.com", "http://www.google.com");
 * VirtualizedScrollPane<TextHyperlinkArea> vsPane = new VirtualizedScrollPane<>(area);
 * Scene scene = new Scene(vsPane, 500, 500);
 * primaryStage.setScene(scene);
 * primaryStage.show();
 * }</pre>
 *
 * @author unknowIfGuestInDream
 * @date 2023/8/3 14:58
 */
public class TextHyperlinkArea extends GenericStyledArea<Void, Either<String, Hyperlink>, TextStyle> {

    private static final TextOps<String, TextStyle> STYLED_TEXT_OPS = SegmentOps.styledTextOps();
    private static final HyperlinkOps<TextStyle> HYPERLINK_OPS = new HyperlinkOps<>();

    private static final TextOps<Either<String, Hyperlink>, TextStyle> EITHER_OPS = STYLED_TEXT_OPS._or(HYPERLINK_OPS,
        (s1, s2) -> Optional.empty());

    public TextHyperlinkArea(Consumer<String> showLink) {
        super(null, (t, p) -> {
        }, TextStyle.EMPTY, EITHER_OPS, e -> e.getSegment().unify(text -> createStyledTextNode(t -> {
            t.setText(text);
            t.setStyle(e.getStyle().toCss());
        }), hyperlink -> createStyledTextNode(t -> {
            if (hyperlink.isReal()) {
                t.setText(hyperlink.getDisplayedText());
                t.getStyleClass().add("hyperlink");
                t.setOnMouseClicked(ae -> showLink.accept(hyperlink.getLink()));
            }
        })));

        getStyleClass().add("text-hyperlink-area");
        getStylesheets().add(Objects.requireNonNull(TextHyperlinkArea.class
            .getResource("/com/tlcsdm/core/static/javafx/richtext/text-hyperlink-area.css")).toExternalForm());
    }

    public void appendWithLink(String displayedText, String link) {
        replaceWithLink(getLength(), getLength(), displayedText, link);
    }

    public void replaceWithLink(int start, int end, String displayedText, String link) {
        replace(start, end, ReadOnlyStyledDocument.fromSegment(
            Either.right(new Hyperlink(displayedText, displayedText, link)), null, TextStyle.EMPTY, EITHER_OPS));
    }

    @Override
    public void replaceText(int start, int end, String text) {

        if (start > 0 && end > 0) {
            int s = Math.max(0, start - 1);
            int e = Math.min(end + 1, getLength() - 1);
            List<Either<String, Hyperlink>> segList = getDocument().subSequence(s, e).getParagraph(0).getSegments();
            if (!segList.isEmpty() && segList.get(0).isRight()) {
                String link = segList.get(0).getRight().getLink();
                replaceWithLink(start, end, text, link);
                return;
            }
        }
        StyledDocument<Void, Either<String, Hyperlink>, TextStyle> doc = ReadOnlyStyledDocument.fromString(text,
            getParagraphStyleForInsertionAt(start), getTextStyleForInsertionAt(start), EITHER_OPS);
        replace(start, end, doc);
    }

    public static TextExt createStyledTextNode(Consumer<TextExt> applySegment) {
        TextExt t = new TextExt();
        t.setTextOrigin(VPos.TOP);
        applySegment.accept(t);
        return t;
    }
}
