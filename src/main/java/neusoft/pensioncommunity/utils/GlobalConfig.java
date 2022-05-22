package neusoft.pensioncommunity.utils;

import javafx.stage.Stage;
import lombok.Getter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

@Getter

public class GlobalConfig {

    public final static String TITLE_LOGIN = "登录 - 东软活力养老系统";
    public final static String TITLE_MAIN = "欢迎使用活力长者社区系统";

    public static void loadConfig() {
        //dump
    }

    public static URL getViewUrl(String fxml) {
        try {
            return new URL(Objects.requireNonNull(GlobalConfig.class.getResource(
                    "/neusoft/pensioncommunity/view/")).toExternalForm() + fxml);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
