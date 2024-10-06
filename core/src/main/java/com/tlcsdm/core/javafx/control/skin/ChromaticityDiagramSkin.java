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

package com.tlcsdm.core.javafx.control.skin;

import cn.hutool.log.StaticLog;
import com.tlcsdm.core.javafx.control.ChromaticityDiagram;
import com.tlcsdm.core.model.CIEData;
import com.tlcsdm.core.model.CIEData1931;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SkinBase;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author unknowIfGuestInDream
 */
public class ChromaticityDiagramSkin extends SkinBase<ChromaticityDiagram> {

    private int fontSize;
    private int width = 1300, height = 1300;
    private int margins = 60, ruler = 10, each = 5, grid = each * ruler;
    private int stepH = (height - margins * 2) / grid;
    private int stepW = (width - margins * 2) / grid;
    private int startH = margins + stepH, startW = margins + stepW;
    private int endH = stepH * grid + margins;
    private int endW = stepW * grid + margins;
    // 数据样式 线/点
    protected boolean isLine = true;
    protected boolean hasWaveLength = true;
    protected boolean hasGrid = true;
    protected boolean hasCalculate;
    protected int dotSize;
    protected java.awt.Color bgColor, calculateColor, textColor, gridColor, rulerColor;
    private double calculateX = -1, calculateY = -1;
    private String title;

    private ScrollPane scrollPane;
    private ImageView imageView;
    private Image img;
    protected BufferedImage image;
    private Graphics2D g;
    private Font dataFont, commentsFont;

    public ChromaticityDiagramSkin(ChromaticityDiagram chromaticityDiagram) {
        super(chromaticityDiagram);
        fontSize = 20;
        dotSize = 4;
        title = "色度图";
        gridColor = new Color(0.9f, 0.9f, 0.9f);
        rulerColor = Color.LIGHT_GRAY;

        hasCalculate = true;
        calculateX = 0.3;
        calculateY = 0.4;

        scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        imageView = new ImageView();
        img = SwingFXUtils.toFXImage(drawData(), null);
        imageView.setImage(img);
        scrollPane.setContent(imageView);
        getChildren().add(scrollPane);

        imageView.fitWidthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                if (Math.abs(new_val.intValue() - old_val.intValue()) > 20) {
                    refinePane();
                }
            }
        });
        imageView.fitHeightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                if (Math.abs(new_val.intValue() - old_val.intValue()) > 20) {
                    refinePane();
                }
            }
        });
        scrollPane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                if (Math.abs(new_val.intValue() - old_val.intValue()) > 20) {
                    refinePane();
                }
            }
        });
    }

    public void refinePane() {
        if (scrollPane == null || imageView == null || imageView.getImage() == null) {
            return;
        }
        scrollPane.setVvalue(scrollPane.getVmin());
    }

    public BufferedImage drawData() {
        try {
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            g = image.createGraphics();
            Map<RenderingHints.Key, Object> imageHints = new HashMap<>();
            imageHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            imageHints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            imageHints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            imageHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.addRenderingHints(imageHints);
            if (bgColor != null) {
                g.setColor(bgColor);
                g.fillRect(0, 0, width, height);
            }
            if (fontSize <= 0) {
                fontSize = 20;
            }
            dataFont = new Font(null, Font.PLAIN, fontSize);
            commentsFont = new Font(null, Font.BOLD, fontSize + 8);
            if (Color.BLACK.equals(bgColor)) {
                textColor = Color.WHITE;
            } else {
                textColor = Color.BLACK;
            }

            // Title / Bottom
            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setComposite(ac);
            g.setFont(commentsFont);
            g.setColor(textColor);
            g.drawString(title, margins + 400, 50);
            g.setFont(dataFont);
            g.drawString("comments", 20, endH + 55);

            backGround();
            outlines();
            calculate();

            g.dispose();
            return image;
        } catch (Exception e) {
            StaticLog.error(e);
            return null;
        }
    }

    private void backGround() {
        try {
            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
            g.setComposite(ac);
            BasicStroke stroke;
            stroke = new BasicStroke(2);
            g.setStroke(stroke);
            g.setFont(dataFont);

            if (hasGrid) {
                g.setColor(gridColor);
                for (int i = 0; i < grid; ++i) {
                    int h = startH + i * stepH;
                    g.drawLine(margins, h, endW, h);
                    int w = startW + i * stepW;
                    g.drawLine(w, margins, w, endH);
                }
                g.setColor(rulerColor);
                for (int i = 0; i < ruler; ++i) {
                    int h = margins + i * stepH * each;
                    g.drawLine(margins, h, endW, h);
                    int w = margins + i * stepW * each;
                    g.drawLine(w, margins, w, endH);
                }
            }

            g.setColor(textColor);
            g.drawLine(margins, margins, margins, endH);
            g.drawLine(margins, endH, endW, endH);
            for (int i = 0; i < ruler; ++i) {
                int h = margins + (i + 1) * stepH * each;
                g.drawString("0." + (9 - i), 10, h + 10);
                int w = margins + i * stepW * each;
                g.drawString("0." + i, w - 10, endH + 30);
            }
            g.drawString("1.0", endW - 15, endH + 30);
            g.drawString("1.0", 10, margins);

        } catch (Exception e) {
            StaticLog.error(e);
        }
    }

    private void outlines() {
        try {
            List<CIEData> data;
            //CIE1931
            data = CIEData1931.getInstance();
            outline(data, "CIE 1931", 535, textColor);
        } catch (Exception e) {
            StaticLog.error(e);
        }
    }

    private void outline(List<CIEData> data, String name, int markWave, Color waveColor) {
        try {
            if (data == null || data.isEmpty()) {
                return;
            }
            Color pColor;
            int dotSizeHalf = dotSize / 2;
            BasicStroke strokeSized = new BasicStroke(dotSize);
            BasicStroke stroke1 = new BasicStroke(1);
            int dataW = width - margins * 2, dataH = height - margins * 2;
            int x, y, wave, lastx = -1, lasty = -1;
            double[] srgb;
            for (CIEData d : data) {
                wave = d.getWaveLength();
                x = (int) Math.round(margins + dataW * d.getNormalizedX());
                y = (int) Math.round(endH - dataH * d.getNormalizedY());
                srgb = CIEData.sRGB65(d);
                pColor = new Color((float) srgb[0], (float) srgb[1], (float) srgb[2]);
                g.setColor(pColor);
                g.setStroke(stroke1);
                if (isLine) {
                    if (lastx >= 0 && lasty >= 0) {
                        g.setStroke(strokeSized);
                        g.drawLine(lastx, lasty, x, y);
                    } else {
                        g.fillRect(x - dotSizeHalf, y - dotSizeHalf, dotSize, dotSize);
                    }
                    lastx = x;
                    lasty = y;
                } else {
                    g.fillRect(x - dotSizeHalf, y - dotSizeHalf, dotSize, dotSize);
                }
                if (wave == markWave) {
                    g.setColor(waveColor);
                    g.drawLine(x, y, x + 300, y + markWave - 560);
                    g.drawString(name, x + 310, y + markWave - 560);
                }
                if (hasWaveLength) {
                    if (wave == 360 || wave == 830 || wave == 460) {
                        g.setColor(waveColor);
                        g.drawString(wave + "nm", x + 10, y);
                    } else if (wave > 470 && wave < 620) {
                        if (wave % 5 == 0) {
                            g.setColor(waveColor);
                            g.drawString(wave + "nm", x + 10, y);
                        }
                    } else if (wave >= 620 && wave <= 640) {
                        if (wave % 10 == 0) {
                            g.setColor(waveColor);
                            g.drawString(wave + "nm", x + 10, y);
                        }
                    }
                }
            }

            // Bottom line
            CIEData d1 = data.get(0);
            CIEData d2 = data.get(data.size() - 1);
            double x1 = d1.getNormalizedX(), x2 = d2.getNormalizedX();
            double y1 = d1.getNormalizedY(), y2 = d2.getNormalizedY();
            colorLine(x1, y1, x2, y2);
        } catch (Exception e) {
            StaticLog.error(e);
        }
    }

    private void colorLine(double ix1, double iy1, double ix2, double iy2) {
        if (image == null || g == null || ix1 == ix2) {
            return;
        }
        double x1, y1, x2, y2;
        if (ix1 < ix2) {
            x1 = ix1;
            y1 = iy1;
            x2 = ix2;
            y2 = iy2;
        } else {
            x1 = ix2;
            y1 = iy2;
            x2 = ix1;
            y2 = iy1;
        }
        Color pColor;
        BasicStroke strokeSized = new BasicStroke(dotSize);
        BasicStroke stroke1 = new BasicStroke(1);
        int dataW = width - margins * 2, dataH = height - margins * 2;
        int x, y, halfDot = dotSize / 2, lastx = -1, lasty = -1;
        double ratio = (y2 - y1) / (x2 - x1);
        double step = (x2 - x1) / 100;
        double[] srgb;
        for (double bx = x1 + step; bx < x2; bx += step) {
            double by = (bx - x1) * ratio + y1;
            double bz = 1 - bx - by;
            double[] relativeXYZ = CIEData.relative(bx, by, bz);
            srgb = CIEData.XYZd50toSRGBd65(relativeXYZ);
            pColor = new Color((float) srgb[0], (float) srgb[1], (float) srgb[2]);
            g.setColor(pColor);
            x = (int) Math.round(margins + dataW * bx);
            y = (int) Math.round(endH - dataH * by);
            if (isLine) {
                if (lastx >= 0 && lasty >= 0) {
                    g.setStroke(strokeSized);
                    g.drawLine(lastx, lasty, x, y);
                } else {
                    g.setStroke(stroke1);
                    g.fillRect(x - halfDot, y - halfDot, dotSize, dotSize);
                }
                lastx = x;
                lasty = y;
            } else {
                g.setStroke(stroke1);
                g.fillRect(x - halfDot, y - halfDot, dotSize, dotSize);
            }
        }
    }

    private void calculate() {
        if (!hasCalculate
            || calculateX < 0 || calculateX > 1
            || calculateY <= 0 || calculateY > 1) {
            return;
        }
        int dataW = width - margins * 2, dataH = height - margins * 2;
        BasicStroke stroke1 = new BasicStroke(1);
        g.setStroke(stroke1);

        double z = 1 - calculateX - calculateY;
        if (z < 0 || z > 1) {
            return;
        }
        Color pColor = calculateColor;
        if (pColor == null) {
            double[] relativeXYZ = CIEData.relative(calculateX, calculateY, z);
            double[] srgb = CIEData.XYZd50toSRGBd65(relativeXYZ);
            pColor = new Color((float) srgb[0], (float) srgb[1], (float) srgb[2]);
        }
        int x = (int) Math.round(margins + dataW * calculateX);
        int y = (int) Math.round(endH - dataH * calculateY);
        g.setColor(pColor);
        g.fillOval(x - 10, y - 10, 20, 20);

        g.setFont(dataFont);
        g.setColor(textColor);
        g.drawString("Calculated values", x + 15, y + 5);

    }
}
