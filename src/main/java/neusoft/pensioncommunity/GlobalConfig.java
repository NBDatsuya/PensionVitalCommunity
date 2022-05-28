package neusoft.pensioncommunity;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import neusoft.pensioncommunity.dao.UserDao;
import neusoft.pensioncommunity.model.User;
import neusoft.pensioncommunity.service.UserService;

import java.net.URL;
import java.util.Objects;

@Getter

public class GlobalConfig {

    public final static String TITLE_LOGIN = "登录 - 东软活力养老系统";
    public final static String TITLE_MAIN = "欢迎使用活力长者社区系统";


    public static final ObservableList<String> USERMANAGE_ROLE =
            FXCollections.observableArrayList("生活管家","后勤管理");

    public static final ObservableList<String> USERMANAGE_GENDER =
            FXCollections.observableArrayList("男","女","其他");

    public static final ObservableList<String> USERMANAGE_BYS =
            FXCollections.observableArrayList("ID","登录名","真实姓名","权限","性别","联系方式","出生日期");
    public static final UserService userService = UserService.getInstance();
    public static User currentUser;

    public static void loadConfig() {
        //dump
    }

    public static URL getViewUrl(String fxml) {
        try {
            return new URL(Objects.requireNonNull(
                    GlobalConfig.class.getResource(
                    "/neusoft/pensioncommunity/view/")
            ).toExternalForm() + fxml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void save(){
        userService.save();
    }
}
