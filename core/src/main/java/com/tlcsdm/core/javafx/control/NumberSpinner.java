/*
 * Copyright (c) 2019, 2023 unknowIfGuestInDream
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

package com.tlcsdm.core.javafx.control;

import java.math.BigDecimal;
import java.text.NumberFormat;

import javax.swing.JSpinner;

import javafx.beans.binding.NumberBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

/**
 * JavaFX Control that behaves like a {@link JSpinner} known in Swing. The
 * number in the textfield can be incremented or decremented by a configurable
 * stepWidth using the arrow buttons in the control or the up and down arrow
 * keys.
 *
 * @author Thomas Bolz
 */
public class NumberSpinner extends HBox {

    public static final String ARROW = "NumberSpinnerArrow";
    public static final String NUMBER_FIELD = "NumberField";
    public static final String NUMBER_SPINNER = "NumberSpinner";
    public static final String SPINNER_BUTTON_UP = "SpinnerButtonUp";
    public static final String SPINNER_BUTTON_DOWN = "SpinnerButtonDown";
    private final String BUTTONS_BOX = "ButtonsBox";
    private final NumberTextField numberField;
    private final ObjectProperty<BigDecimal> stepWitdhProperty = new SimpleObjectProperty<>();
    private final double ARROW_SIZE = 4;
    private final Button incrementButton;
    private final Button decrementButton;
    private final NumberBinding buttonHeight;
    private final NumberBinding spacing;

    public NumberSpinner() {
        this(BigDecimal.ZERO, BigDecimal.ONE);
    }

    public NumberSpinner(BigDecimal value, BigDecimal stepWidth) {
        this(value, stepWidth, NumberFormat.getInstance());
    }

    public NumberSpinner(BigDecimal value, BigDecimal stepWidth, NumberFormat nf) {
        super();
        getStyleClass().add("number-spinner");
        this.setId(NUMBER_SPINNER);
        this.stepWitdhProperty.set(stepWidth);

        // TextField
        numberField = new NumberTextField(value, nf);
        numberField.setId(NUMBER_FIELD);

        // Enable arrow keys for dec/inc
        numberField.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.DOWN) {
                    decrement();
                    keyEvent.consume();
                }
                if (keyEvent.getCode() == KeyCode.UP) {
                    increment();
                    keyEvent.consume();
                }
            }
        });

        // Painting the up and down arrows
        Path arrowUp = new Path();
        arrowUp.setId(ARROW);
        arrowUp.getElements().addAll(new MoveTo(-ARROW_SIZE, 0), new LineTo(ARROW_SIZE, 0), new LineTo(0, -ARROW_SIZE),
                new LineTo(-ARROW_SIZE, 0));
        // mouse clicks should be forwarded to the underlying button
        arrowUp.setMouseTransparent(true);

        Path arrowDown = new Path();
        arrowDown.setId(ARROW);
        arrowDown.getElements().addAll(new MoveTo(-ARROW_SIZE, 0), new LineTo(ARROW_SIZE, 0), new LineTo(0, ARROW_SIZE),
                new LineTo(-ARROW_SIZE, 0));
        arrowDown.setMouseTransparent(true);

        // the spinner buttons scale with the textfield size
        // the following approach leads to the desired result, but it is
        // not fully understood why and obviously it is not quite elegant
        buttonHeight = numberField.heightProperty().subtract(3).divide(2);
        // give unused space in the buttons VBox to the incrementBUtton
        spacing = numberField.heightProperty().subtract(2).subtract(buttonHeight.multiply(2));

        // inc/dec buttons
        VBox buttons = new VBox();
        buttons.setId(BUTTONS_BOX);
        incrementButton = new Button();
        incrementButton.setId(SPINNER_BUTTON_UP);
        incrementButton.prefWidthProperty().bind(numberField.heightProperty());
        incrementButton.minWidthProperty().bind(numberField.heightProperty());
        incrementButton.maxHeightProperty().bind(buttonHeight.add(spacing));
        incrementButton.prefHeightProperty().bind(buttonHeight.add(spacing));
        incrementButton.minHeightProperty().bind(buttonHeight.add(spacing));
        incrementButton.setFocusTraversable(false);
        incrementButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent ae) {
                increment();
                ae.consume();
            }
        });

        // Paint arrow path on button using a StackPane
        StackPane incPane = new StackPane();
        incPane.getChildren().addAll(incrementButton, arrowUp);
        incPane.setAlignment(Pos.CENTER);

        decrementButton = new Button();
        decrementButton.setId(SPINNER_BUTTON_DOWN);
        decrementButton.prefWidthProperty().bind(numberField.heightProperty());
        decrementButton.minWidthProperty().bind(numberField.heightProperty());
        decrementButton.maxHeightProperty().bind(buttonHeight);
        decrementButton.prefHeightProperty().bind(buttonHeight);
        decrementButton.minHeightProperty().bind(buttonHeight);

        decrementButton.setFocusTraversable(false);
        decrementButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent ae) {
                decrement();
                ae.consume();
            }
        });

        StackPane decPane = new StackPane();
        decPane.getChildren().addAll(decrementButton, arrowDown);
        decPane.setAlignment(Pos.CENTER);

        buttons.getChildren().addAll(incPane, decPane);
        GridPane.setHgrow(numberField, Priority.ALWAYS);
        this.getChildren().addAll(numberField, buttons);
    }

    /**
     * increment number value by stepWidth
     */
    private void increment() {
        BigDecimal value = numberField.getNumber();
        value = value.add(stepWitdhProperty.get());
        numberField.setNumber(value);
    }

    /**
     * decrement number value by stepWidth
     */
    private void decrement() {
        BigDecimal value = numberField.getNumber();
        value = value.subtract(stepWitdhProperty.get());
        numberField.setNumber(value);
    }

    public final void setNumber(BigDecimal value) {
        numberField.setNumber(value);
    }

    public ObjectProperty<BigDecimal> numberProperty() {
        return numberField.numberProperty();
    }

    public final BigDecimal getNumber() {
        return numberField.getNumber();
    }

    @Override
    public String getUserAgentStylesheet() {
        return getClass().getResource("number_spinner.css").toExternalForm();
    }
}
