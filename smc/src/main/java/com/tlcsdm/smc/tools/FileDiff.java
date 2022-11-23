package com.tlcsdm.smc.tools;

import java.io.File;

import org.controlsfx.control.MaskerPane;

import com.tlcsdm.smc.SmcSample;
import com.tlcsdm.smc.util.I18nUtils;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * 检测指定路径下文件内容长度是否超过120
 * 
 * @author unknowIfGuestInDream
 */
public class FileDiff extends SmcSample {

	private ProgressIndicator progressIndicator = new ProgressIndicator();
	private StackPane progressIndicatorPane = new StackPane(progressIndicator);

	private MaskerPane masker = new MaskerPane();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public String getSampleName() {
		return I18nUtils.get("smc.sampleName.fileDiff");
	}

	@Override
	public Node getPanel(Stage stage) {
		GridPane grid = new GridPane();
		grid.setVgap(12);
		grid.setHgap(12);
		grid.setPadding(new Insets(24));
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("text files", "*.txt", "*.c", "*.h",
				"*.java", "*.html");
		fileChooser.getExtensionFilters().add(extFilter);
		// fileChooser.setTitle("");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("all", "*"));
		// fileChooser.initialDirectoryProperty().bindBidirectional(null);
		// fileChooser.setInitialDirectory(new File("C:\\workspace\\JavaFX Scene
		// Builder"));
		Button buttonLoad = new Button("Load");
		TextField textField = new TextField();
		textField.setPrefWidth(300);
		System.out.println(stage.getScene().getWidth());
		textField.setEditable(false);
		buttonLoad.setOnAction(arg0 -> {
			File file = fileChooser.showOpenDialog(stage);
			if (file != null) {
				textField.setText(file.getPath());
			}
		});
		grid.add(buttonLoad, 0, 0);
		grid.add(textField, 1, 0);
		WebView webView = new WebView();
		webView.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		grid.add(webView, 0, 1, 2, 1);

//		WebView webView = new WebView();
//		webView.getEngine().loadContent("");
		return grid;
	}

	@Override
	public String getOrderKey() {
		return "FillDiff";
	}

	@Override
	public String getSampleDescription() {
		return I18nUtils.get("smc.sampleName.fileDiff.description");
	}

	private void prepareTabContent(Tab tab, WebView webView) {
		// tab.setContent(progressIndicatorPane);
		final WebEngine engine = webView.getEngine();
//        progressIndicator.progressProperty().bind(engine.getLoadWorker().progressProperty());
//        engine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue == Worker.State.SUCCEEDED) {
//                tab.setContent(webView);
//                tab.getProperties().put(TAB_LOAD_CACHE, true);
//            }
//        });
	}

//    private void loadWebViewContent(WebView webView, ControlsFXSample sample, Function<ControlsFXSample, String> urlFunction,
//                                    Function<ControlsFXSample, String> contentFunction) {
//        final String url = urlFunction.apply(sample);
//        if (url != null && url.startsWith("http")) {
//            webView.getEngine().load(url);
//        } else {
//            webView.getEngine().loadContent(contentFunction.apply(sample));
//        }
//    }

}
