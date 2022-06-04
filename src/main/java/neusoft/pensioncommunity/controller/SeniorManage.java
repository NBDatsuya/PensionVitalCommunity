package neusoft.pensioncommunity.controller;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;
import neusoft.pensioncommunity.GlobalConfig;
import neusoft.pensioncommunity.model.Senior;
import neusoft.pensioncommunity.model.User;
import neusoft.pensioncommunity.service.SeniorService;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SeniorManage implements Controller{
    private final User currentUser = GlobalConfig.currentUser;
    private User keyUser;

    @Setter
    @Getter
    private Main mainController;

    private ObservableList<Senior> listView;
    private SeniorService service = GlobalConfig.seniorService;
    private Senior seniorModel;
    private int modelFunction;
    private int tempGender;
    private LocalDate tempBirthday;
    private int tempAssitantID;
    private int tempStatus;

    /**
     * 视图的初始化
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colID.setCellValueFactory(c -> new SimpleIntegerProperty(
                c.getValue().getId()).asObject());
        colName.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getName()
        ));
        colGender.setCellValueFactory(c -> new SimpleStringProperty(
                GlobalConfig.valueOfGender(c.getValue().getGender())
        ));
        colIdentify.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getIdentity()
        ));
        colTelRelative.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getTelRelative()
        ));
        colTelSelf.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getTelSelf()
        ));
        colAge.setCellValueFactory(c -> new SimpleIntegerProperty(
                c.getValue().getAge()
        ).asObject());
        colBirthDay.setCellValueFactory(c -> new SimpleObjectProperty<>(
                c.getValue().getBirthDay()
        ));
        colAssistant.setCellValueFactory(c -> new SimpleStringProperty(
            (c.getValue().getStatus()==0)?
                ("无") :
            GlobalConfig.userService.searchById(
                c.getValue().getAssistantId()).getRealName()
                        )
        );

        modelSAssistant.focusedProperty().addListener((ov, oldV, newV) -> {
            modelURealName.setOpacity(1);
            if (!newV) { // 失去焦点
                if(modelSAssistant.getText().isEmpty()){
                    modelURealName.setText("您可以在添加住户后\n再设置管家");
                    modelURealName.setTextFill(Color.web("#590176"));
                    btnOK.setDisable(false);
                    tempAssitantID = -1;
                    tempStatus = 0;
                    return;
                }
                User assistant = GlobalConfig.userService.searchById(
                    Integer.parseInt(modelSAssistant.getText())
                );
                if(assistant==null || assistant.getRole()!=1){
                    modelURealName.setText("查无此人\n请重新输入");
                    modelURealName.setTextFill(Color.RED);
                    btnOK.setDisable(true);
                }else{
                    modelURealName.setText(assistant.getRealName());
                    modelURealName.setTextFill(Color.web("#590176"));
                    btnOK.setDisable(false);
                    tempAssitantID = Integer.parseInt(modelSAssistant.getText());
                    tempStatus = 1;
                }
            }
        });

        modelSIdentify.focusedProperty().addListener((ov, oldV, newV) -> {
            if (!newV) { // 身份证号位数错误，身份证对应用户已存在
                if(modelSIdentify.getText().length()!=18){
                    lblCaution.setOpacity(1);
                    btnOK.setDisable(true);
                }
                else{
                    if(!identifyToValue()) return;
                    lblCaution.setOpacity(0);
                    btnOK.setDisable(false);
                }
            }
        });

        //tblSenior.selectionModelProperty().addListener();

        cbxBy.getItems().setAll(GlobalConfig.SELECTION_SENIORFIELDS);
        cbxBy.getSelectionModel().select(0);

        cbxStatus.getItems().setAll(GlobalConfig.SELECTION_STATUS);
        cbxStatus.getSelectionModel().select(1);
    }

    private boolean identifyToValue() {
        String identify = modelSIdentify.getText();
        StringBuilder strBirthday = new StringBuilder(identify.substring(6,14));
        String gender = identify.substring(14,17);

        strBirthday.insert(4,"-");
        strBirthday.insert(7,"-");
        try{
            tempBirthday = LocalDate.parse(strBirthday);
        }catch (DateTimeParseException e){
            lblCaution.setOpacity(1);
            lblCaution.setText("身份证输入有误");
            modelSIdentify.requestFocus();
            return false;
        }
        modelSBirthday.setText(
                LocalDate.parse(strBirthday.toString()).format(
                        DateTimeFormatter.ofPattern("yyyy年M月d日")));
        tempGender = (Integer.parseInt(gender))%2==0?1:0;
        modelSGender.setText(tempGender==0?"男":"女");
        return true;
    }

    /**
     * 初始化视图后载入数据
     */
    @Override
    public void requestLoadData() {
        if (currentUser.getRole() != 0){    //非管理员
            btnAdd.setVisible(false);
            btnChangeStatus.setVisible(false);
            btnModify.setVisible(false);
            colAssistant.setVisible(false);
            cbxStatus.getItems().remove(0);
        }

          //管理员查看指定生活管家的管理信息或所有住户的信息
        if (keyUser!=null){
            tabList.setText(keyUser.getRealName()+"所管理的长者住户信息");
            colAssistant.setVisible(false);
            cbxStatus.setVisible(false);
            lblStatusTip.setVisible(false);
            tempAssitantID = keyUser.getId();
        }
        refreshList();
        clearModel();
    }
    public void requestLoadData(User keyUser) {
        this.keyUser = keyUser;
        requestLoadData();
    }

    private void refreshList(){
        refreshListByStatus(1);
    }
    private void refreshListByStatus(int status){

        listView = FXCollections.observableArrayList(currentUser.getRole() != 0?
                //非管理员
                service.searchByAssitant(
                        currentUser.getId(),status):

                //管理员查看指定生活管家的管理信息或所有住户的信息
                (keyUser==null?
                        service.getAll(status):
                        service.searchByAssitant(keyUser.getId()))

        );

        tblSenior.setItems(listView);
        tblSenior.refresh();
        tblSenior.autosize();
    }

    private void clearModel() {
        lblCaution.setOpacity(0);
        modelURealName.setOpacity(1);
        seniorModel = new Senior();
        seniorModel.setId(service.size()+1);
        seniorModel.setBirthDay(LocalDate.now());
        seniorModel.setStatus(keyUser==null?0:1);

        modelSID.setText(String.valueOf(seniorModel.getId()));
        modelSName.setText("");
        modelSIdentify.setText("");
        modelSGender.setText("");
        modelSBirthday.setText("");
        modelSTelSelf.setText("");
        modelSTelRelative.setText("");
        modelURealName.setText("");

        if(keyUser!=null){
            modelURealName.setText(keyUser.getRealName());
            modelURealName.setTextFill(Color.web("#590176"));

            tempAssitantID = keyUser.getId();
            modelSAssistant.setDisable(true);
        }else{
            modelSAssistant.setDisable(false);
            tempAssitantID = -1;
        }

        modelSAssistant.setText(String.valueOf(
                tempAssitantID == -1?"":tempAssitantID)
        );
        modelSName.requestFocus();

    }

    public void enableTab(boolean bool, int modelFunction){
        btnAdd.setDisable(bool);
        btnModify.setDisable((bool ^
                tblSenior.getSelectionModel().getSelectedItems().isEmpty()) ||
                modelFunction>0);

        btnChangeStatus.setDisable(btnModify.isDisable());
        btnSearch.setDisable(bool);

        this.modelFunction = modelFunction;
        tblSenior.setDisable(bool);

        switch(modelFunction){
            case 1:
                tabModel.setText("新增长者住户");
                tabModel.setDisable(!bool);
                tabPane.getSelectionModel().select(1);
                clearModel();
                break;
            case 2:
                tabModel.setText("修改信息");
                tabModel.setDisable(!bool);
                tabPane.getSelectionModel().select(1);
                loadModelToEdit();
                break;
            case 3:
                tabChangeStatus.setText("更正住户状态");
                tabChangeStatus.setDisable(!bool);
                tabPane.getSelectionModel().select(2);
                loadModelToChangeStatus();
                break;
            case 0:
                tabModel.setDisable(!bool);
                tabChangeStatus.setDisable(!bool);
                tabModel.setText("");
                tabChangeStatus.setText("");
                tabPane.getSelectionModel().select(0);
        }

        if(!bool){
            refreshListByStatus(cbxStatus.getSelectionModel().getSelectedIndex());
        }
    }

    private void loadModelToEdit() {
        lblCaution.setOpacity(0);
        modelURealName.setOpacity(1);
        modelSID.setText(String.valueOf(seniorModel.getId()));
        modelSName.setText(seniorModel.getName());
        modelSIdentify.setText((seniorModel.getIdentity()));
        tempBirthday = seniorModel.getBirthDay();
        modelSBirthday.setText(tempBirthday.toString());
        modelSTelSelf.setText(seniorModel.getTelSelf());
        modelSTelRelative.setText(seniorModel.getTelRelative());

        modelSAssistant.setText(
                String.valueOf(
                        seniorModel.getAssistantId()==-1?"":
                                seniorModel.getAssistantId()
                )
        );
        tempAssitantID = seniorModel.getAssistantId();
        tempStatus = seniorModel.getStatus();
        modelURealName.setText(seniorModel.getStatus()==0?"未分配":
                GlobalConfig.userService.searchById(
                        seniorModel.getAssistantId()
                ).getRealName());

        tempGender=seniorModel.getGender();
        modelSGender.setText(GlobalConfig.valueOfGender(tempGender));

        modelSName.requestFocus();
    }
    private void loadModelToChangeStatus(){
        modelSID1.setText(String.valueOf(seniorModel.getId()));
        modelSName1.setText(seniorModel.getName());
        modelSIdentify1.setText(seniorModel.getIdentity());
        modelSGender1.setText(GlobalConfig.valueOfGender(seniorModel.getGender()));
        modelSBirthday1.setText(seniorModel.getBirthDay().format(
                DateTimeFormatter.ofPattern("yyyy年M月d日")));
        modelSAge1.setText(String.valueOf(seniorModel.getAge()));
        modelSTelSelf1.setText(seniorModel.getTelSelf());
        modelSTelRelative1.setText(seniorModel.getTelRelative());

        modelSStatus.setText(GlobalConfig.valueOfStatus(seniorModel.getStatus()));
        modelURealName1.setText(seniorModel.getAssistantId()!=-1?
                GlobalConfig.userService.searchById(
                        seniorModel.getAssistantId()).getRealName():"无");
        cbxToStatus.getItems().setAll(GlobalConfig.SELECTION_STATUS);
        cbxToStatus.getItems().remove(1);
        cbxStatus.requestFocus();
    }
    @FXML
    private AnchorPane apModule;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXButton btnCancel1;

    @FXML
    private JFXButton btnChangeStatus;

    @FXML
    private JFXButton btnModify;

    @FXML
    private JFXButton btnAdd;

    @FXML
    private JFXButton btnOK;

    @FXML
    private JFXButton btnOK1;

    @FXML
    private JFXButton btnSearch;

    @FXML
    private ComboBox<String> cbxBy;

    @FXML
    private ComboBox<String> cbxKey;

    @FXML
    private ComboBox<String> cbxStatus;

    @FXML
    private ComboBox<String> cbxToStatus;

    @FXML
    private TableColumn<Senior, Integer> colAge;

    @FXML
    private TableColumn<Senior, LocalDate> colBirthDay;

    @FXML
    private TableColumn<Senior, String> colGender;

    @FXML
    private TableColumn<Senior, Integer> colID;

    @FXML
    private TableColumn<Senior, String> colIdentify;

    @FXML
    private TableColumn<Senior, String> colName;

    @FXML
    private TableColumn<Senior, String> colAssistant;

    @FXML
    private TableColumn<Senior, String> colTelRelative;

    @FXML
    private TableColumn<Senior, String> colTelSelf;

    @FXML
    private Label lblCaution;

    @FXML
    private Label modelSAge1;

    @FXML
    private Label modelSBirthday1;

    @FXML
    private Label modelURealName;

    @FXML
    private TextField modelSGender;

    @FXML
    private Label modelSGender1;

    @FXML
    private Label modelSID;

    @FXML
    private Label modelSID1;

    @FXML
    private Label modelSIdentify1;

    @FXML
    private TextField modelSName;

    @FXML
    private TextField modelSAssistant;

    @FXML
    private Label modelSName1;

    @FXML
    private Label modelSStatus;

    @FXML
    private Label lblStatusTip;

    @FXML
    private Label modelSTelRelative1;

    @FXML
    private TextField modelSTelSelf;

    @FXML
    private Label modelSTelSelf1;

    @FXML
    private Label modelURealName1;

    @FXML
    private TextField modelSBirthday;

    @FXML
    private TextField modelSIdentify;

    @FXML
    private TextField modelSTelRelative;

    @FXML
    private Tab tabChangeStatus;

    @FXML
    private Tab tabList;

    @FXML
    private Tab tabModel;

    @FXML
    private TabPane tabPane;

    @FXML
    private TableView<Senior> tblSenior;

    @FXML void actBy(ActionEvent event) {
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

    @FXML void actStatus(ActionEvent event) {
        listView.clear();
        refreshListByStatus(currentUser.getRole()==0?
                cbxStatus.getSelectionModel().getSelectedIndex():
                cbxStatus.getSelectionModel().getSelectedIndex()+1);
        tblSenior.setItems(listView);
        tblSenior.refresh();

        colAssistant.setVisible(cbxStatus.getSelectionModel().getSelectedIndex()==1);
    }

    @FXML void eventCancel(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("你确定要取消吗?");
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.OK))
            enableTab(false,0);
        else
            modelSName.requestFocus();
    }

    @FXML void eventChangeStatus(ActionEvent event) {
        enableTab(true,3);
    }

    @FXML void eventModify(ActionEvent event) {
        enableTab(true,2);
    }

    @FXML void eventAdd(ActionEvent event) {
        enableTab(true,1);
    }

    @FXML void eventSave(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("请问是否保存？");
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.OK)){

            seniorModel.setName(modelSName.getText());
            seniorModel.setGender(tempGender);
            seniorModel.setIdentity(modelSIdentify.getText());
            seniorModel.setBirthDay(tempBirthday);
            seniorModel.setTelRelative(modelSTelRelative.getText());
            seniorModel.setTelSelf(modelSTelSelf.getText());
            seniorModel.setAssistantId(tempAssitantID);
            seniorModel.setStatus(tempAssitantID==-1?0:1);

            switch (modelFunction){
                case 1:
                    service.add(seniorModel);
                    break;
                case 2:
                    service.modify(seniorModel.getId(),seniorModel);
                    break;
            }
            enableTab(false,0);
        }else
            modelSName.requestFocus();
    }

    @FXML void eventSearch(ActionEvent event) {
        if(cbxKey.getValue().isEmpty())
            refreshListByStatus(cbxStatus.getSelectionModel().getSelectedIndex());
        else
            refreshList(cbxBy.getSelectionModel().getSelectedIndex());
    }

    private void refreshList(int field){
        switch(field){
            case 0: //By ID
                for(Senior item : tblSenior.getItems())
                    if (item.getId()==Integer.parseInt(cbxKey.getValue())){
                        tblSenior.getSelectionModel().select(item);
                        return;
                    }

            case 1: //By Name
                listView.clear();
                listView.addAll(service.searchByName(cbxKey.getValue()));
                break;
            case 2: //By Gender
                listView.clear();
                listView.addAll(service.searchByGender(cbxKey.getSelectionModel().getSelectedIndex()));
                break;
            case 3: //Age no less than
                listView.clear();
                listView.addAll(
                        service.searchByAge(
                                0,Integer.parseInt(
                                        cbxKey.getValue())));
                break;
            case 4: //Age no more than
                listView.clear();
                listView.addAll(
                        service.searchByAge(
                                1,Integer.parseInt(
                                        cbxKey.getValue())));
                break;
            case 5: //By Identification Number
                listView.clear();
                listView.addAll(
                        service.searchByIN(
                                cbxKey.getValue()));
                break;
            case 6: //By Assistant ID
                listView.clear();
                listView.addAll(
                        service.searchByAssitant(
                                Integer.parseInt(cbxKey.getValue()),
                                1));
                break;
            case 7: //By Tel of Him/Herself
                listView.clear();
                listView.addAll(
                        service.searchByTelSelf(
                                cbxKey.getValue()));
                break;
            case 8: //By Tel of Relatives
                listView.clear();
                listView.addAll(
                        service.searchByTelRelative(
                                cbxKey.getValue()));
                break;
        }
        tblSenior.setItems(listView);
        tblSenior.refresh();
    }

    @FXML void eventSelectItem(MouseEvent event){
        if(currentUser.getRole()!=0) return;

        boolean isEmpty = tblSenior.getSelectionModel().getSelectedItems().isEmpty();
        seniorModel = isEmpty?null:tblSenior.getSelectionModel().getSelectedItem();
        btnModify.setDisable(tblSenior.getSelectionModel().getSelectedItems().isEmpty());
        btnChangeStatus.setDisable(btnModify.isDisable());
        if (!isEmpty && event.getClickCount()==2 &&
                    event.getButton().equals(MouseButton.PRIMARY))
            enableTab(true,2);
        }

    @FXML void eventSaveChange(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("请问是否保存？");
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.OK)){
            if(cbxToStatus.getSelectionModel().getSelectedIndex()==0){
                seniorModel.setAssistantId(-1);
                seniorModel.setStatus(0);
            }else
                seniorModel.setStatus(cbxToStatus.getSelectionModel().getSelectedIndex()+1);

            enableTab(false,0);
        }else
            cbxToStatus.requestFocus();
    }

    @FXML void eventCancelChange(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("请问是否退出");
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.OK))
            enableTab(false,0);
        else
            cbxToStatus.requestFocus();
    }
}
