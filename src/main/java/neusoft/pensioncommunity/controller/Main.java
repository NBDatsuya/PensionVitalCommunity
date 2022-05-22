package neusoft.pensioncommunity.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;


import neusoft.pensioncommunity.model.User;
import neusoft.pensioncommunity.utils.GlobalConfig;

import java.io.IOException;
import java.net.URL;

public class Main implements Controller{

    public User currentUser;

    @FXML
    private Label lblUser;

    @FXML
    private Label lblStat;

    @FXML
    private AnchorPane apMain;

    @FXML
    private AnchorPane apChild;

    @FXML
    private Pane pnStat;

    @FXML
    private ImageView imgAvatar;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    void mouseDragged(MouseEvent event) {
        Window window = apMain.getScene().getWindow();
        window.setX(event.getScreenX() - xOffset);
        window.setY(event.getScreenY() - yOffset);
    }

    @FXML
    void mousePressed(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    @FXML
    void eventExit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("你确定要退出系统吗?");
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.OK)) {
            Stage stage = (Stage) apMain.getScene().getWindow();
            stage.close();
            Platform.exit();
        }
    }

    @FXML
    void eventChangeUser(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("你确定要退出登录吗?");
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.OK)) {
            Stage stage = (Stage) apMain.getScene().getWindow();
            Login formLogin = new Login();
            formLogin.load();

            stage.close();
        }
    }

    public void load() {
        double userInfoLayoutX;

        lblStat.setText("你好，尊敬的" +
                User.valueOfRole(currentUser.getRole()) +
                currentUser.getRealName() + "，祝您拥有愉快的一天。");
        lblStat.autosize();
        userInfoLayoutX = (pnStat.getWidth() - lblStat.getWidth()) / 2;
        lblStat.setLayoutX(userInfoLayoutX);

        lblUser.setText(User.valueOfRole(currentUser.getRole()) + " - " +
                currentUser.getRealName());
        lblUser.autosize();
        userInfoLayoutX = imgAvatar.getLayoutX() +
                (imgAvatar.getFitWidth() - lblUser.getWidth()) / 2;

        lblUser.setLayoutX(userInfoLayoutX);

        this.switchToUserConfig();
    }

    public void load(User currentUser) {
        this.currentUser = currentUser;
        this.load();
    }

    @FXML
    void eventMin(ActionEvent event) {
        Stage stage = (Stage) apMain.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void switchToUserConfig(MouseEvent event) {
        switchToUserConfig();
    }

    void switchToUserConfig(){
        FXMLLoader loader =null;
        try {
            loader = new FXMLLoader(GlobalConfig.getViewUrl(
                    "userConfig.fxml"));
            apChild.getChildren().setAll((Node) loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        UserConfig ctrlUConfig = loader.getController();
        ctrlUConfig.load(currentUser);

    }

    @FXML
    void switchToUserManage(ActionEvent event) {
        FXMLLoader loader =null;
        try {
            loader = new FXMLLoader(GlobalConfig.getViewUrl(
                    "userManage.fxml"));
            apChild.getChildren().setAll((Node) loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
