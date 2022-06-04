package neusoft.pensioncommunity;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                GlobalConfig.getViewUrl("login.fxml"));

        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle(GlobalConfig.TITLE_LOGIN);
        stage.centerOnScreen();
        stage.show();

        GlobalConfig.loadConfig();
    }
}
