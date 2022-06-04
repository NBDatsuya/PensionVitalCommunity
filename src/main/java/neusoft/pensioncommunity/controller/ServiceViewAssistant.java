package neusoft.pensioncommunity.controller;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import lombok.Setter;
import neusoft.pensioncommunity.GlobalConfig;
import neusoft.pensioncommunity.model.Senior;
import neusoft.pensioncommunity.model.User;
import neusoft.pensioncommunity.service.SeniorService;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class ServiceViewAssistant implements Controller{
    @FXML
    private AnchorPane apModule;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnExport;

    @FXML
    private TableColumn<Senior, Integer> colAge;

    @FXML
    private TableColumn<Senior, String> colGender;

    @FXML
    private TableColumn<Senior, Integer> colID;

    @FXML
    private TableColumn<Senior, String> colIdentify;

    @FXML
    private TableColumn<Senior, String> colName;

    @FXML
    private TableColumn<Senior, String> colTelRelative;

    @FXML
    private TableColumn<Senior, String> colTelSelf;

    @FXML
    private TableColumn<Senior, LocalDate> colBirthDay;

    @FXML
    private ImageView imgUAvatar;

    @FXML
    private Label modelUBirthday;

    @FXML
    private Label modelUGender;

    @FXML
    private Label modelUId;

    @FXML
    private Label modelUName;

    @FXML
    private Label modelURealName;

    @FXML
    private Label modelUTel;

    @FXML
    private TableView<Senior> tblSenior;

    @Setter
    private User modelUser;
    @Setter
    private UserManage parentController;

    private final SeniorService service = GlobalConfig.seniorService;
    private ObservableList<Senior> listView = FXCollections.observableArrayList();
    @FXML
    void eventBack(ActionEvent event) {
        parentController.requestCloseView();
    }


    /**
     * 视图的初始化
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colID.setCellValueFactory(cellData -> new SimpleIntegerProperty(
                cellData.getValue().getId()).asObject());
        colName.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getName()
        ));
        colGender.setCellValueFactory(cellData -> new SimpleStringProperty(
                GlobalConfig.valueOfGender(cellData.getValue().getGender())
        ));
        colIdentify.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getIdentity()
        ));
        colTelRelative.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getTelRelative()
        ));
        colTelSelf.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getTelSelf()
        ));
        colAge.setCellValueFactory(cellData -> new SimpleIntegerProperty(
                cellData.getValue().getAge()
        ).asObject());

        colBirthDay.setCellValueFactory(c -> new SimpleObjectProperty<LocalDate>(
                c.getValue().getBirthDay()
        ));
    }

    @Override
    public void requestLoadData() {
        modelUId.setText(String.valueOf(modelUser.getId()));
        modelUName.setText(modelUser.getName());
        modelURealName.setText(modelUser.getRealName());
        modelUGender.setText(GlobalConfig.valueOfGender(modelUser.getGender()));
        modelUTel.setText(modelUser.getTel());
        modelUBirthday.setText(modelUser.getBirthDay().toString());

        listView = service.searchByAssitant(modelUser.getId());
        tblSenior.setItems(listView);
        tblSenior.refresh();
    }

    public void switchToSeniorManage(ActionEvent event){
        Main mainController = parentController.getMainController();
        mainController.switchToSeniorManage(modelUser);
    }
}
