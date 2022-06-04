package neusoft.pensioncommunity.controller;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lombok.Setter;
import neusoft.pensioncommunity.GlobalConfig;
import neusoft.pensioncommunity.model.Bus;
import neusoft.pensioncommunity.model.Reserve;
import neusoft.pensioncommunity.model.Senior;
import neusoft.pensioncommunity.model.User;
import neusoft.pensioncommunity.service.ReserveService;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ReserveBus implements Controller {

    /**
     * 视图的初始化，数据绑定
     *
     * @param url
     * @param resourceBundle
     */

    @Setter
    private Bus modelBus;
    @Setter
    private BusManage parentController;
    @Setter
    private User operator;
    @Setter
    private Main mainController;

    private final ReserveService service = GlobalConfig.reserveSevice;

    private ObservableList<Senior> listViewToReserve;
    private ObservableList<Senior> listViewToCheck;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colID.setCellValueFactory(c->new SimpleObjectProperty<>(c.getValue().getId()));
        colName.setCellValueFactory(c->new SimpleStringProperty(c.getValue().getName()));
        colGender.setCellValueFactory(
                c->new SimpleStringProperty(
                        GlobalConfig.valueOfGender(c.getValue().getGender())));
        colAssistantID.setCellValueFactory(c->new SimpleObjectProperty<>(c.getValue().getAssistantId()));
        colAssistantName.setCellValueFactory(c->new SimpleStringProperty(
                GlobalConfig.userService.searchById(c.getValue().getAssistantId()).getRealName()));
        colIdentify.setCellValueFactory(c->new SimpleStringProperty(c.getValue().getIdentity()));
        colTelSelf.setCellValueFactory(c->new SimpleStringProperty(c.getValue().getTelSelf()));

        colID1.setCellValueFactory(colID.getCellValueFactory());
        colName1.setCellValueFactory(colName.getCellValueFactory());
        colGender1.setCellValueFactory(colGender.getCellValueFactory());
        colReserveTime.setCellValueFactory(c->
                new SimpleStringProperty(
                        service.getUnCheckedReserveTime(
                                modelBus.getId(),
                                c.getValue().getId()).format(
                                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        colAssistantName1.setCellValueFactory(colAssistantName.getCellValueFactory());
        colIdentify1.setCellValueFactory(colIdentify.getCellValueFactory());
        colTelSelf1.setCellValueFactory(colTelSelf.getCellValueFactory());

        cbxBy.getItems().setAll(GlobalConfig.SELECTION_SENIORFIELDS);
        cbxBy1.getItems().setAll(cbxBy.getItems());
        cbxBy1.getItems().add(1,"预约时间");

        tblSenior.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblSenior1.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    /**
     * 初始化视图后载入数据
     */
    @Override
    public void requestLoadData() {
        modelBID.setText(String.valueOf(modelBus.getId()));
        modelBCode.setText(modelBus.getCode());
        modelBName.setText(modelBus.getName());
        modelBAnnual.setText(modelBus.getAnnual());
        modelBTimeBegin.setText(modelBus.getTimeBegin().toString());
        modelBDeadline.setText(modelBus.getTimeDeadline().toString());
        modelBDirection.setText(GlobalConfig.valueOfDirection(modelBus.getDirection()));
        modelBMemo.setText(modelBus.getMemo());
        modelBHours.setText(GlobalConfig.valueOfHours(modelBus.getHours()));
        refreshList();
    }

    public void refreshList(){
        listViewToReserve = GlobalConfig.reserveSevice.searchForRestSeniors(modelBus.getId());
        tblSenior.setItems(listViewToReserve);
        tblSenior.refresh();

        listViewToCheck = GlobalConfig.reserveSevice.searchForUnchecked(modelBus.getId());
        tblSenior1.setItems(listViewToCheck);
        tblSenior1.refresh();

        modelBCountPassenger.setText(String.valueOf(modelBus.getCountPassenger()));
    }

    @FXML
    private AnchorPane apService;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXButton btnConfirm;

    @FXML
    private JFXButton btnExport;

    @FXML
    private JFXButton btnReserve;

    @FXML
    private JFXButton btnSearch;

    @FXML
    private JFXButton btnSearch1;

    @FXML
    private ComboBox<String> cbxBy;

    @FXML
    private ComboBox<String> cbxBy1;

    @FXML
    private ComboBox<String> cbxKey;

    @FXML
    private ComboBox<String> cbxKey1;

    @FXML
    private TableColumn<Senior, Integer> colAssistantID;

    @FXML
    private TableColumn<Senior, String> colReserveTime;

    @FXML
    private TableColumn<Senior, String> colAssistantName;

    @FXML
    private TableColumn<Senior, String> colAssistantName1;

    @FXML
    private TableColumn<Senior, String> colGender;

    @FXML
    private TableColumn<Senior, String> colGender1;

    @FXML
    private TableColumn<Senior, Integer> colID;

    @FXML
    private TableColumn<Senior, Integer> colID1;

    @FXML
    private TableColumn<Senior, String> colIdentify;

    @FXML
    private TableColumn<Senior, String> colIdentify1;

    @FXML
    private TableColumn<Senior, String> colName;

    @FXML
    private TableColumn<Senior, String> colName1;

    @FXML
    private TableColumn<Senior, String> colTelSelf;

    @FXML
    private TableColumn<Senior, String> colTelSelf1;


    @FXML
    private Label modelBAnnual;

    @FXML
    private Label modelBCode;

    @FXML
    private Label modelBCountPassenger;

    @FXML
    private Label modelBDeadline;

    @FXML
    private Label modelBDirection;

    @FXML
    private Label modelBHours;

    @FXML
    private Label modelBID;

    @FXML
    private Label modelBMemo;

    @FXML
    private Label modelBName;

    @FXML
    private Label modelBTimeBegin;

    @FXML
    private Tab tabCheck;

    @FXML
    private Tab tabReserve;

    @FXML
    private TabPane tabpane;

    @FXML
    private TableView<Senior> tblSenior;

    @FXML
    private TableView<Senior> tblSenior1;

    @FXML
    void actBy(ActionEvent event) {
        switch (cbxBy.getSelectionModel().getSelectedIndex()){
            case 2:
                cbxKey.setEditable(false);
                cbxKey.getItems().addAll(GlobalConfig.SELECTION_GENDER);
                cbxKey.getSelectionModel().select(0);
                break;
            case 6:
                ArrayList<User> users = new ArrayList<>(GlobalConfig.userService.searchByRole(1));
                if(!users.contains(null))
                    for(User user : users)
                        cbxKey.getItems().add(String.valueOf(user.getId()));
            default:cbxKey.setEditable(true);
        }
        cbxKey.setValue("");
        cbxKey.requestFocus();
    }

    @FXML
    void eventBack(ActionEvent event) {
        parentController.requestCloseReserve();
        parentController.requestLoadData();
    }

    @FXML
    void eventCancel(ActionEvent event) {
        ObservableList<Senior> selItems = tblSenior1.getSelectionModel().getSelectedItems();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("请确认是否进行取消操作？");
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.OK)){
            service.cancelReserve(modelBus.getId(),selItems);
            mainController.requestMessage("操作成功！");
            refreshList();
        }
    }

    @FXML
    void eventConfirm(ActionEvent event) {
        ObservableList<Senior> selItems = tblSenior1.getSelectionModel().getSelectedItems();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("请确认是否进行确认操作？");
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.OK)){
            service.confirmReserve(modelBus.getId(),selItems);
            mainController.requestMessage("操作成功！");
            refreshList();
        }
    }


    @FXML
    void eventReserve(ActionEvent event) {
        ObservableList<Senior> selItems = tblSenior.getSelectionModel().getSelectedItems();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("请确认是否要预约？");
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.OK)){
            service.submitReserve(modelBus.getId(),selItems);
            mainController.requestMessage("预约成功！");
            refreshList();
        }else{
            return;
        }
    }

    @FXML
    void eventSearch(ActionEvent event) {

    }

    @FXML
    void eventSelectItemToReserve(MouseEvent event) {
        if(tblSenior.getSelectionModel().getSelectedItems().isEmpty()) return;
        enableButton(0);
    }
    @FXML
    void eventSelectItemToCheck(MouseEvent event) {
        if(tblSenior1.getSelectionModel().getSelectedItems().isEmpty()) return;
        enableButton(1);
    }

    private void enableButton(int tab) {
        switch (tab){
            case 0:
                btnReserve.setDisable(false);
                btnConfirm.setDisable(true);
                break;
            case 1:
                btnReserve.setDisable(true);
                btnConfirm.setDisable(false);
                break;
        }
        btnCancel.setDisable(btnConfirm.isDisable());
    }

}
