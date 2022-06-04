package neusoft.pensioncommunity.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lombok.Setter;
import neusoft.pensioncommunity.GlobalConfig;
import neusoft.pensioncommunity.model.User;

import java.net.URL;
import java.util.ResourceBundle;

public class UserConfig implements Controller{
    /**
     * 视图的初始化
     *
     * @param url
     * @param resourceBundle
     */

    @Setter
    private Main mainController;
    private User currentUser = GlobalConfig.currentUser;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modelUGender.getItems().setAll(GlobalConfig.SELECTION_GENDER);
    }

    @Override
    public void requestLoadData() {
        modelUName.setText(currentUser.getName());
        modelURealName.setText(currentUser.getRealName());
        modelUTel.setText(currentUser.getTel());
        modelURole.setText(GlobalConfig.valueOfRole(currentUser.getRole()));
        modelUBirthday.setValue(currentUser.getBirthDay());

        modelUGender.getSelectionModel().select(currentUser.getGender());

        modelUName.requestFocus();
    }

    @FXML
    private AnchorPane apModule;

    @FXML
    private Pane apPwd;

    @FXML
    private JFXButton btnChangePwd;

    @FXML
    private JFXButton btnReset;

    @FXML
    private JFXButton btnSave;

    @FXML
    private ImageView imgAvatar;

    @FXML
    private DatePicker modelUBirthday;

    @FXML
    private ComboBox<String> modelUGender;

    @FXML
    private TextField modelUName;

    @FXML
    private TextField modelURealName;

    @FXML
    private Label modelURole;

    @FXML
    private TextField modelUTel;

    @FXML
    private JFXPasswordField txtOldPwd;
    @FXML
    private JFXPasswordField txtNewPwd;
    @FXML
    private JFXPasswordField txtNewConfirm;

    @FXML
    private Label lblStat;

    @FXML
    void eventCancel(ActionEvent event) {
        apPwd.setVisible(false);
        modelUName.requestFocus();
    }

    @FXML
    void eventChangePwd(ActionEvent event) {
        apPwd.setVisible(true);
        txtOldPwd.requestFocus();
        txtOldPwd.setText("");
        txtNewPwd.setText("");
        txtNewConfirm.setText("");
    }

    @FXML
    void eventReset(ActionEvent event) {
        requestLoadData();
    }

    @FXML
    void eventSave(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("请问是否保存？");
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.OK)){
            currentUser.setBirthDay(modelUBirthday.getValue());
            currentUser.setGender(modelUGender.getItems().indexOf(modelUGender.getValue()));
            currentUser.setName(modelUName.getText());
            currentUser.setRealName(modelURealName.getText());
            currentUser.setTel(modelUTel.getText());
            GlobalConfig.userService.modify(currentUser.getId(),currentUser);
            mainController.requestLoadData();
            requestLoadData();
        }else
            modelUName.requestFocus();

    }

    @FXML
    void eventSavePwd(ActionEvent event) {
        if (!txtOldPwd.getText().equals(currentUser.getPassword())) {
            lblStat.setVisible(true);
            lblStat.setText("旧密码错误");
            txtOldPwd.requestFocus();
            return;
        }

        if(txtNewPwd.getText().equals(currentUser.getPassword())){
            lblStat.setVisible(true);
            lblStat.setText("旧密码与新密码相同");
            txtNewPwd.requestFocus();
            return;
        }

        if (txtNewConfirm.getText().equals(txtNewPwd.getText())) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("请问是否保存？");
            alert.showAndWait();
            if (alert.getResult().equals(ButtonType.OK)) {
                currentUser.setPassword(txtNewPwd.getText());
                mainController.requestMessage("密码修改成功");
                eventCancel(event);
            }
        }else {
                lblStat.setVisible(true);
                lblStat.setText("两次输入的密码不一致");
                txtNewConfirm.requestFocus();
        }
    }
}
