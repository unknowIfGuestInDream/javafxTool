package com.tlcsdm.core.javafx;

import com.tlcsdm.core.util.HttpUtil;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.HashMap;
import java.util.Map;

public class HttpTestTool extends VBox {
    private TextField urlField;
    private ComboBox<String> methodComboBox;
    private VBox headersContainer;
    private TextArea requestBodyArea;
    private TextArea responseArea;
    private Button sendRequestButton;

    public HttpTestTool() {
        initializeUI();
    }

    private void initializeUI() {
        setPadding(new Insets(10));
        setSpacing(5);

        urlField = new TextField();
        urlField.setPromptText("Enter URL");

        methodComboBox = new ComboBox<>();
        methodComboBox.getItems().addAll("GET", "POST", "PUT", "DELETE");
        methodComboBox.setValue("GET");

        headersContainer = new VBox();
        headersContainer.setSpacing(5);
        addHeaderField();

        requestBodyArea = new TextArea();
        requestBodyArea.setPromptText("Enter request body");

        sendRequestButton = new Button("Send Request");
        sendRequestButton.setOnAction(e -> sendRequest());

        responseArea = new TextArea();
        responseArea.setEditable(false);

        getChildren().addAll(new Label("URL:"), urlField, new Label("Method:"), methodComboBox, new Label("Headers:"), headersContainer, new Label("Request Body:"), requestBodyArea, sendRequestButton, new Label("Response:"), responseArea);
    }

    private void addHeaderField() {
        HBox headerField = new HBox(5);
        TextField keyField = new TextField();
        keyField.setPromptText("Key");
        TextField valueField = new TextField();
        valueField.setPromptText("Value");
        Button removeButton = new Button("-");
        removeButton.setOnAction(e -> headersContainer.getChildren().remove(headerField));
        headerField.getChildren().addAll(keyField, valueField, removeButton);
        headersContainer.getChildren().add(headerField);
    }

    private void sendRequest() {
        String url = urlField.getText();
        String method = methodComboBox.getValue();
        Map<String, String> headers = new HashMap<>();
        for (var child : headersContainer.getChildren()) {
            HBox headerField = (HBox) child;
            TextField keyField = (TextField) headerField.getChildren().get(0);
            TextField valueField = (TextField) headerField.getChildren().get(1);
            headers.put(keyField.getText(), valueField.getText());
        }
        String body = requestBodyArea.getText();
        String response;
        switch (method) {
            case "GET":
                response = HttpUtil.doGet(url, headers);
                break;
            case "POST":
                response = HttpUtil.doPost(url, headers, body);
                break;
            default:
                response = "Unsupported method: " + method;
                break;
        }
        responseArea.setText(response);
    }
}
