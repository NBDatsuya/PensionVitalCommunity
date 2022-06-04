package neusoft.pensioncommunity.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;


import neusoft.pensioncommunity.model.Senior;
import neusoft.pensioncommunity.model.User;
import neusoft.pensioncommunity.GlobalConfig;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Main implements Controller{

    private final User currentUser = GlobalConfig.currentUser;
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
    @FXML
    private Label lblUName;

    @FXML
    private JFXButton switchToUserManage;
    @FXML
    private JFXButton switchToSeniorManage;
    @FXML
    private JFXButton switchToBusManage;

    @FXML
    private Button btnUserConfig;
    @FXML
    private Label lblURName;

    @FXML
    private Label lblURole;
    @FXML
    private VBox adminMenu;

    private double xOffset = 0;
    private double yOffset = 0;
    private String defaultStat ="";
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
    void eventExit(ActionEvent event) throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("你确定要退出系统吗?");
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.OK)) {
            GlobalConfig.save();
            Stage stage = (Stage) apMain.getScene().getWindow();
            stage.close();
            Platform.exit();
        }
    }

    @FXML
    void eventChangeUser(ActionEvent event) throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("你确定要退出登录吗?");
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.OK)) {
            GlobalConfig.save();
            Stage stage = (Stage) apMain.getScene().getWindow();
            Login formLogin = new Login();
            formLogin.load();

            stage.close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        switch (currentUser.getRole()) {
            case 0:
                switchToUserManage();
                break;
            case 1:
                switchToSeniorManage();
                break;
            case 2:
                switchToBusManage();
                break;
        }

        adminMenu.setVisible(currentUser.getRole()==0);
    }

    @Override
    public void requestLoadData() {
        double userInfoLayoutX;
        defaultStat = "你好，尊敬的" +
                currentUser.getRealName() + "，祝您拥有愉快的一天。";
        lblStat.setText(defaultStat);
        lblStat.autosize();
        userInfoLayoutX = (pnStat.getWidth() - lblStat.getWidth()) / 2;
        lblStat.setLayoutX(userInfoLayoutX);

        lblUName.setText(currentUser.getName());
        lblURName.setText(currentUser.getRealName());
        lblURole.setText(GlobalConfig.valueOfRole(currentUser.getRole()));
    }

    @FXML
    void eventMin(ActionEvent event) {
        Stage stage = (Stage) apMain.getScene().getWindow();
        stage.setIconified(true);
    }

    public void switchToUserManage(ActionEvent event){switchToUserManage();}
    public void switchToUserManage() {
        FXMLLoader loader = null;
        Controller child;
        try {
            loader = new FXMLLoader(GlobalConfig.getViewUrl(
                    "userManage.fxml"));
            apChild.getChildren().setAll((Node) loader.load());
            child = loader.getController();
            ((UserManage)child).setMainController(this);
            child.requestLoadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        enableMenu(1);
    }
    public void enableMenu(int index){
        imgAvatar.setDisable(index==0);
        btnUserConfig.setDisable(index==0);
        switchToUserManage.setDisable(index==1);
        switchToSeniorManage.setDisable(index==2);
        switchToBusManage.setDisable(index==3);
        apChild.requestFocus();
    }

    public void switchToSeniorManage(){
        FXMLLoader loader = null;
        Controller child;
        try {
            loader = new FXMLLoader(GlobalConfig.getViewUrl(
                    "SeniorManage.fxml"));
            apChild.getChildren().setAll((Node) loader.load());
            child = loader.getController();
            ((SeniorManage) child).setMainController(this);
            child.requestLoadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        enableMenu(2);
    }
    public void switchToSeniorManage(User keyUser){
        FXMLLoader loader = null;
        Controller child;
        try {
            loader = new FXMLLoader(GlobalConfig.getViewUrl(
                    "SeniorManage.fxml"));
            apChild.getChildren().setAll((Node) loader.load());
            child = loader.getController();
            ((SeniorManage) child).setMainController(this);
            ((SeniorManage) child).requestLoadData(keyUser);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML void switchToBusManage(ActionEvent event){
        switchToBusManage();
    }
    public void switchToBusManage(){
        FXMLLoader loader = null;
        Controller child;
        try {
            loader = new FXMLLoader(GlobalConfig.getViewUrl(
                    "BusManage.fxml"));
            apChild.getChildren().setAll((Node) loader.load());
            child = loader.getController();
            ((BusManage)child).setMainController(this);
            child.requestLoadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        enableMenu(3);
    }

    @FXML void switchToUserConfig(Event event){
        FXMLLoader loader = null;
        Controller child;
        try {
            loader = new FXMLLoader(GlobalConfig.getViewUrl(
                    "UserConfig.fxml"));
            apChild.getChildren().setAll((Node) loader.load());
            child = loader.getController();
            ((UserConfig)child).setMainController(this);
            child.requestLoadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        enableMenu(0);
    }
    void requestMessage(String message){
        lblStat.setText(message);
        lblStat.autosize();

        lblStat.setLayoutX((pnStat.getWidth() - lblStat.getWidth()) / 2);
    }
}
