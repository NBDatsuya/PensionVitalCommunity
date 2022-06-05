package neusoft.pensioncommunity.controller;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import neusoft.pensioncommunity.GlobalConfig;
import neusoft.pensioncommunity.model.Bus;
import neusoft.pensioncommunity.model.Reserve;
import neusoft.pensioncommunity.model.Senior;
import neusoft.pensioncommunity.model.User;
import neusoft.pensioncommunity.service.ReserveService;
import neusoft.pensioncommunity.service.Service;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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

        cbxBy.getItems().setAll(GlobalConfig.SELECTION_RESERVEFIELDS);
        cbxBy.setValue("");
        cbxBy1.getItems().setAll(GlobalConfig.SELECTION_RESERVEFIELDS);
        cbxBy1.setValue("");
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
        modelBAnnual.setText(GlobalConfig.valueOfAnnual(modelBus.getAnnual()));
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
        enableButton();
    }

    public void refreshList(ObservableList<Senior> r, ObservableList<Senior> c){
        listViewToReserve = GlobalConfig.reserveSevice.searchForRestSeniors(modelBus.getId());
        tblSenior.setItems(r);
        tblSenior.refresh();

        listViewToCheck = GlobalConfig.reserveSevice.searchForUnchecked(modelBus.getId());
        tblSenior1.setItems(c);
        tblSenior1.refresh();
    }

    public void refreshRestList(int field){
        LocalService ls = new LocalService(listViewToReserve);
        if(cbxBy.getSelectionModel().getSelectedItem().isEmpty() || cbxKey.getValue().isEmpty()){ refreshList();return;}
        switch (field){
            case 0:
                tblSenior.getSelectionModel().clearSelection();
                Senior senior = ls.searchById(Integer.parseInt(cbxKey.getValue()));
                if(senior!=null) tblSenior.getSelectionModel().select(senior);
                break;
            case 1:
                refreshList(ls.searchByName(cbxKey.getValue()), listViewToCheck);
                break;
            case 2:
                refreshList(ls.searchByGender(cbxKey.getSelectionModel().getSelectedIndex()), listViewToCheck);
                break;
            case 3:
                refreshList(ls.searchByIdentity(cbxKey.getValue()), listViewToCheck);
                break;
            case 4:
                refreshList(ls.searchByTel(cbxKey.getValue()), listViewToCheck);
                break;
            case 5:
                refreshList(ls.searchByAssistantId(Integer.parseInt(cbxKey.getValue())), listViewToCheck);
                break;
            case 6:
                refreshList(ls.searchByAssistantName(cbxKey.getValue()), listViewToCheck);
                break;
        }
    }
    public void refreshUnCheckedList(int field){
        LocalService ls = new LocalService(listViewToCheck);
        if(cbxBy1.getSelectionModel().getSelectedItem().isEmpty() || cbxKey1.getValue().isEmpty()){ refreshList();return;}
        switch (field){
            case 0:
                tblSenior1.getSelectionModel().clearSelection();
                Senior senior = ls.searchById(Integer.parseInt(cbxKey1.getValue()));
                if(senior!=null) tblSenior1.getSelectionModel().select(senior);
                break;
            case 1:
                refreshList(listViewToReserve, ls.searchByName(cbxKey1.getValue()));
                break;
            case 2:
                refreshList(listViewToReserve, ls.searchByGender(cbxKey1.getSelectionModel().getSelectedIndex()));
                break;
            case 3:
                refreshList(listViewToReserve, ls.searchByIdentity(cbxKey1.getValue()));
                break;
            case 4:
                refreshList(listViewToReserve, ls.searchByTel(cbxKey1.getValue()));
                break;
            case 5:
                refreshList(listViewToReserve, ls.searchByAssistantId(Integer.parseInt(cbxKey1.getValue())));
                break;
            case 6:
                refreshList(listViewToReserve, ls.searchByAssistantName(cbxKey1.getValue()));
                break;
            case 7:
                refreshList(listViewToReserve, ls.searchByReserveTime(cbxKey1.getValue()));
                break;
        }
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

        if (cbxBy.getSelectionModel().getSelectedIndex()==2) {
            cbxKey.setEditable(false);
            cbxKey.getItems().setAll(GlobalConfig.SELECTION_GENDER);
            cbxKey.getSelectionModel().select(0);
        }
        else{
            cbxKey.setValue("");
            cbxKey.setEditable(true);
            cbxKey.getItems().clear();
        }

        cbxKey.requestFocus();
    }

    @FXML
    void actBy1(ActionEvent event) {

        if (cbxBy1.getSelectionModel().getSelectedIndex()==2) {
            cbxKey1.setEditable(false);
            cbxKey1.getItems().setAll(GlobalConfig.SELECTION_GENDER);
            cbxKey1.getSelectionModel().select(1);
        }
        else{
            cbxKey1.setEditable(true);
            cbxKey1.setValue("");
            cbxKey1.getItems().clear();
        }

        cbxKey1.requestFocus();
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

        if(!modelBus.isReservable()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("已经超过本班次的预约时间，不能预约");
            alert.show();
            return;
        }

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
        refreshRestList(cbxBy.getSelectionModel().getSelectedIndex());
    }
    @FXML
    void eventSearch1(ActionEvent event) {
        refreshUnCheckedList(cbxBy1.getSelectionModel().getSelectedIndex());
    }

    @FXML
    void eventSelectItemToReserve(MouseEvent event) {
        enableButton();
    }
    @FXML
    void eventSelectItemToCheck(MouseEvent event) {
        enableButton();
    }

    private void enableButton() {
        btnReserve.setDisable(tblSenior.getSelectionModel().getSelectedItems().isEmpty());
        btnConfirm.setDisable(tblSenior1.getSelectionModel().getSelectedItems().isEmpty());
        btnCancel.setDisable(btnConfirm.isDisable());
    }

    @AllArgsConstructor
    private class LocalService{

        private ObservableList<Senior> list;

        Senior searchById(int id){
            for(Senior item : list)
                if(item.getId()==id)
                    return item;
            return null;
        }

        ObservableList<Senior> searchByName(String name){
            ObservableList<Senior> subList = FXCollections.observableArrayList();
            for(Senior item : list)
                if(item.getName().contains(name))
                    subList.add(item);

            return subList;
        }

        ObservableList<Senior> searchByGender(int gender){
            ObservableList<Senior> subList = FXCollections.observableArrayList();
            for(Senior item : list)
                if(item.getGender()==gender)
                    subList.add(item);

            return subList;
        }

        ObservableList<Senior> searchByIdentity(String identity){
            ObservableList<Senior> subList = FXCollections.observableArrayList();
            for(Senior item : list)
                if(item.getIdentity().contains(identity))
                    subList.add(item);

            return subList;
        }

        ObservableList<Senior> searchByTel(String tel){
            ObservableList<Senior> subList = FXCollections.observableArrayList();
            for(Senior item : list)
                if(item.getTelSelf().contains(tel))
                    subList.add(item);

            return subList;
        }

        ObservableList<Senior> searchByAssistantId(int id){
            ObservableList<Senior> subList = FXCollections.observableArrayList();
            for(Senior item : list)
                if(item.getAssistantId()==id)
                    subList.add(item);

            return subList;
        }

        ObservableList<Senior> searchByAssistantName(String name){
            ObservableList<Senior> subList = FXCollections.observableArrayList();
            for(Senior item : list)
                if((GlobalConfig.userService.searchById(
                        item.getAssistantId()
                ).getRealName().contains(name)))
                    subList.add(item);

            return subList;
        }

        ObservableList<Senior> searchByReserveTime(String strTime){
            ObservableList<Senior> subList = FXCollections.observableArrayList();
            for(Senior item : list)
                if(service.getUnCheckedReserveTime(
                        modelBus.getId(),item.getId()
                ).toString().contains(strTime))
                    subList.add(item);

            return subList;
        }
    }
}
