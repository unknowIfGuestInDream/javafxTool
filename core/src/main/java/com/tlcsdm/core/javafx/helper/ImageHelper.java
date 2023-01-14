package com.tlcsdm.core.javafx.helper;

import com.tlcsdm.core.exception.CoreException;
import javafx.scene.image.Image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author: unknowIfGuestInDream
 * @date: 2023/1/14 18:34
 */
public class ImageHelper {
    public static final String CLASSPATH_PREFIX = "classpath:";
    public static final String HTTP_PREFIX = "http:";
    public static final String HTTPS_PREFIX = "https:";
    public static final String FILE_PREFIX = "file:";

    public ImageHelper() {
    }

    public static Image image(String path) {
        try {
            String lowerCasePath = path.toLowerCase();
            Image image;
            if (lowerCasePath.startsWith(CLASSPATH_PREFIX)) {
                image = new Image(Objects.requireNonNull(ImageHelper.class.getResourceAsStream(path.substring(CLASSPATH_PREFIX.length()))));
            } else if (!lowerCasePath.startsWith(HTTP_PREFIX) && !lowerCasePath.startsWith(HTTPS_PREFIX) && !lowerCasePath.startsWith(FILE_PREFIX)) {
                image = new Image(Files.newInputStream(Paths.get(path)));
            } else {
                image = new Image(path);
            }

            return image;
        } catch (IOException e) {
            throw new CoreException(e);
        }
    }
}
