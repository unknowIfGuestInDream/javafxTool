package com.tlcsdm.core.javafx.util;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.tlcsdm.core.exception.ResourceNotFoundException;

import javafx.fxml.FXMLLoader;

/**
 * @author: unknowIfGuestInDream
 * @date: 2022/12/10 20:51
 */
public class FxmlUtil {

	public static FXMLLoader loadFxmlFromResource(URL location) {
		return loadFxmlFromResource(location, (ResourceBundle) null);
	}

	public static FXMLLoader loadFxmlFromResource(URL location, ResourceBundle resourceBundle) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(location);
			fxmlLoader.setResources(resourceBundle);
			fxmlLoader.load();
			return fxmlLoader;
		} catch (IOException var4) {
			throw new ResourceNotFoundException(var4);
		}
	}
}
