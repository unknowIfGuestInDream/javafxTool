package com.tlcsdm.core.modal;

import java.io.Serializable;

/**
 * vosk 结果.
 *
 * @author unknowIfGuestInDream
 */
public class VoskModal implements Serializable {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
