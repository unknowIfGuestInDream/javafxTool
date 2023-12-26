#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import javafx.application.Application;
import javafx.stage.Stage;

#parse("File Header.java")

/**
 * @author ${USER}
 */
public class ${NAME} extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        
    }
}
