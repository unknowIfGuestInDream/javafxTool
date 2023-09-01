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

/**
 * @author unknowIfGuestInDream
 */
public class Hyperlink {

    private final String originalDisplayedText;
    private final String displayedText;
    private final String link;

    Hyperlink(String originalDisplayedText, String displayedText, String link) {
        this.originalDisplayedText = originalDisplayedText;
        this.displayedText = displayedText;
        this.link = link;
    }

    public boolean isEmpty() {
        return length() == 0;
    }

    public boolean isReal() {
        return length() > 0;
    }

    public boolean shareSameAncestor(Hyperlink other) {
        return link.equals(other.link);
    }

    public int length() {
        return displayedText.length();
    }

    public char charAt(int index) {
        return isEmpty() ? '\0' : displayedText.charAt(index);
    }

    public String getOriginalDisplayedText() {
        return originalDisplayedText;
    }

    public String getDisplayedText() {
        return displayedText;
    }

    public String getLink() {
        return link;
    }

    public Hyperlink subSequence(int start, int end) {
        return new Hyperlink(originalDisplayedText, displayedText.substring(start, end), link);
    }

    public Hyperlink subSequence(int start) {
        return new Hyperlink(originalDisplayedText, displayedText.substring(start), link);
    }

    public Hyperlink mapDisplayedText(String text) {
        return new Hyperlink(originalDisplayedText, text, link);
    }

    @Override
    public String toString() {
        return isEmpty()
            ? String.format("EmptyHyperlink[original=%s link=%s]", originalDisplayedText, link)
            : String.format("RealHyperlink[original=%s displayedText=%s, link=%s]",
            originalDisplayedText, displayedText, link);
    }

}
