package neusoft.pensioncommunity;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import lombok.Getter;
import neusoft.pensioncommunity.model.User;
import neusoft.pensioncommunity.service.BusService;
import neusoft.pensioncommunity.service.ReserveService;
import neusoft.pensioncommunity.service.SeniorService;
import neusoft.pensioncommunity.service.UserService;

import java.net.URL;
import java.util.List;
import java.util.Objects;

@Getter

public class GlobalConfig {

    public final static String TITLE_LOGIN = "登录 - 东软活力养老系统";
    public final static String TITLE_MAIN = "欢迎使用活力长者社区系统";


    public static final List<String> SELECTION_ROLE =
            FXCollections.observableArrayList("生活管家","后勤管理");

    public static final List<String> SELECTION_GENDER =
            FXCollections.observableArrayList("男","女","其他");

    public static final List<String> SELECTION_USERFIELDS =
            FXCollections.observableArrayList("ID","登录名","真实姓名","权限","性别","联系方式");

    public static final List<String> SELECTION_SENIORFIELDS =
            FXCollections.observableArrayList("ID","姓名","性别","年龄>=","年龄<=","身份证号",
                    "管家编号","本人联系方式","家属联系方式");

    public static final List<String> SELECTION_BUSFIELDS =
            FXCollections.observableArrayList("ID","代码","名称","方向","运营周期","运营时段",
                    "截止时间","发车时间","备注");

    public static final List<String> SELECTION_STATUS =
            FXCollections.observableArrayList("未分配管理员","已分配管理员","迁出","去世");

    public static final List<String> SELECTION_DIRECTION =
            FXCollections.observableArrayList(
                    "上行","下行");

    public static final List<String> SELECTION_ANNUAL =
            FXCollections.observableArrayList(
                    "每天","隔天","每周日","每周一",
                    "每周二","每周三","每周四","每周五",
                    "每周六");
    public static final List<String> SELECTION_HOURS =
            FXCollections.observableArrayList("全天","早上","下午","晚上");

    public static UserService userService;
    public static SeniorService seniorService;
    public static BusService busService;
    public static ReserveService reserveSevice;

    public static User currentUser;

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

    public static void loadConfig(){
        try{
            userService = UserService.getInstance();
            seniorService = SeniorService.getInstance();
            busService = BusService.getInstance();
            reserveSevice = ReserveService.getInstance();
        }
        catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("数据文件读取错误，程序即将关闭！");
            alert.show();
            Platform.exit();
        }
    }
    public static void save(){
        userService.save();
        seniorService.save();
        busService.save();
        reserveSevice.save();
    }

    public static String valueOfRole(int role) {
        switch (role) {
            case 0:
                return "管理员";
            case 1:
                return "生活管家";
            case 2:
            default:
                return "后勤管理";
        }
    }
    public static String valueOfGender(int gender){
        switch (gender) {
            case 2:
            default:
                return "其他";
            case 0:
                return "男";
            case 1:
                return "女";
        }
    }
    public static String valueOfDirection(int dir){
        switch (dir) {
            case 0:
                return "上行";
            case 1:
            default:
                return "下行";
        }
    }
    public static String valueOfStatus(int status){
        switch (status){
            case 0:
                return "未分配管理员";
            case 1:
                return "在住";
            case 2:
                return "迁出";
            case 3:
            default:
                return "去世";
        }
    }
    public static String valueOfHours(int hours){
        switch (hours){
            case 0:
                return "全天";
            case 1:
                return "上午";
            case 2:
                return "下午";
            case 3:
            default:
                return "晚上";
        }
    }

}
