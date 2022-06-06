package neusoft.pensioncommunity.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import neusoft.pensioncommunity.dao.UserDao;
import neusoft.pensioncommunity.model.User;
import neusoft.pensioncommunity.GlobalConfig;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Login implements Controller {

    @FXML
    private AnchorPane apLogin;

    @FXML
    private JFXButton btnExit;

    @FXML
    private JFXButton btnLogin;

    @FXML
    private JFXButton btnReset;

    @FXML
    private JFXButton btnClose;

    @FXML
    private Label btnSystem;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXPasswordField txtPass;

    @FXML
    private Label btnGetPass;

    @FXML
    private Label lblStat;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    void eventExit(ActionEvent event) {
        Stage stage = (Stage) apLogin.getScene().getWindow();
        stage.close();
        Platform.exit();
    }

    @FXML
    void eventLogin(ActionEvent event){

        if (Objects.equals(txtName.getText(), "") ||
                Objects.equals(txtPass.getText(), "")) {
            lblStat.setText("请输入用户名或密码");
            txtName.requestFocus();
            return;
        }

        User currentUser = GlobalConfig.userService.verifyUser(
                txtName.getText(),txtPass.getText());
        if (currentUser == null){
            lblStat.setText("用户名或密码输入错误");
            txtName.requestFocus();
            return;
        }

        GlobalConfig.currentUser = currentUser;

        Stage stageLogin = (Stage) apLogin.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(
                GlobalConfig.getViewUrl("main.fxml"));

        Stage stageMain = new Stage();
        Scene sceneMain = null;
        try {
            sceneMain = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        stageMain.setScene(sceneMain);
        stageMain.initStyle(StageStyle.TRANSPARENT);
        stageMain.setTitle(GlobalConfig.TITLE_MAIN);
        stageMain.toFront();
        stageMain.show();
        Main ctrlMain = loader.getController();
        ctrlMain.requestLoadData();

        stageLogin.close();
    }

    @FXML
    void eventReset(ActionEvent event) {
        txtName.setText("");
        txtPass.setText("");
        txtName.requestFocus();
    }


    @FXML
    void mouseEnter(MouseEvent event) {
        if (event.getTarget() instanceof Label) {
            Label lblBtn = (Label) event.getTarget();
            lblBtn.setTextFill(Color.web("#FFD801"));
        }
    }

    @FXML
    void mouseExit(MouseEvent event) {
        if (event.getTarget() instanceof Label) {
            Label lblBtn = (Label) event.getTarget();
            lblBtn.setTextFill(Color.web("#FFFFFF"));
        }

    }

    @FXML
    void clickGetPass(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("ah!");
        alert.show();
    }

    @FXML
    void mouseDragged(MouseEvent event) {
        Window window = apLogin.getScene().getWindow();
        window.setX(event.getScreenX() - xOffset);
        window.setY(event.getScreenY() - yOffset);
    }


    @FXML
    void mousePressed(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    @FXML
    void eventHideAlert(MouseEvent event) {
        lblStat.setText("");
    }

    public void load() {
        Scene sceneLogin = null;
        try {
            FXMLLoader loader = new FXMLLoader(
                    GlobalConfig.getViewUrl("login.fxml"
                    ));
            sceneLogin = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(sceneLogin);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setTitle(GlobalConfig.TITLE_LOGIN);
            stage.centerOnScreen();
            stage.toFront();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void requestLoadData() {

    }
}
