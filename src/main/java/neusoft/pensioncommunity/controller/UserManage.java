package neusoft.pensioncommunity.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import neusoft.pensioncommunity.GlobalConfig;
import neusoft.pensioncommunity.dao.UserDao;
import neusoft.pensioncommunity.model.User;
import neusoft.pensioncommunity.service.UserService;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class UserManage implements Controller {

    @FXML
    TableView<User> tblUser;

    @FXML
    private JFXButton btnDel;

    @FXML
    private JFXButton btnModify;

    @FXML
    private JFXButton btnNew;

    @FXML
    private JFXButton btnSearch;

    @FXML
    private JFXButton btnView;

    @FXML
    private TableColumn<User, Integer> colID;

    @FXML
    private TableColumn<User, String> colName;

    @FXML
    private TableColumn<User, String> colRealName;

    @FXML
    private TableColumn<User, String> colRole;

    @FXML
    private TableColumn<User, String> colTel;

    @FXML
    private TableColumn<User, LocalDate> colBirth;

    @FXML
    private TableColumn<User,String> colGender;

    @FXML
    private DatePicker modelUBirth;

    @FXML
    private Label modelUID;

    @FXML
    private Label lblCaution;

    @FXML
    private TextField modelUName;

    @FXML
    private TextField modelUPassword;

    @FXML
    private TextField modelURealName;

    @FXML
    private JFXComboBox<String> modelUGender;

    @FXML
    private JFXComboBox<String> modelURole;

    @FXML
    private TextField modelUTel;

    @FXML
    private Tab tabList;

    @FXML
    private Tab tabModel;

    @FXML
    private TabPane tabPane;

    @FXML
    private ComboBox<String> cbxBy;

    @FXML
    private ComboBox<String> cbxKey;

    /**
     * 0 = Not Editing
     * 1 = Add
     * 2 = Modify
     */
    private int modelFunction = 0;
    private final UserService service = GlobalConfig.userService;
    private ObservableList<User> listView;
    private User userModel = new User();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colID.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());

        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        colRealName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRealName()));
        colRole.setCellValueFactory(cellData -> new SimpleStringProperty(
                    User.valueOfRole(cellData.getValue().getRole()
                )));
        colTel.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTel()));
        colBirth.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getBirthDay()));
        colGender.setCellValueFactory(cellData -> new SimpleStringProperty(
                    User.valueOfGender(cellData.getValue().getGender()
                )));

        refreshList();
        tblUser.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        cbxBy.setItems(GlobalConfig.USERMANAGE_BYS);
        cbxKey.getSelectionModel().select(0);
        modelURole.getItems().setAll(GlobalConfig.USERMANAGE_ROLE);
        modelUGender.getItems().setAll(GlobalConfig.USERMANAGE_GENDER);
        clearModel();

    }

    private void clearModel() {
        lblCaution.setOpacity(0);
        modelUName.requestFocus();
        userModel = new User();

        userModel.setId(service.size());
        userModel.setBirthDay(LocalDate.now());

        modelUID.setText(String.valueOf(userModel.getId()));
        modelUName.setText("");
        modelUTel.setText("");
        modelURealName.setText("");
        modelUPassword.setText("");
        modelUBirth.setValue(userModel.getBirthDay());

        SingleSelectionModel<String> ssm = modelURole.getSelectionModel();
        ssm.select(0);
        ssm = modelUGender.getSelectionModel();
        ssm.select(0);
    }
    private void loadModel(){
        lblCaution.setOpacity(0);
        modelUName.requestFocus();

        modelUID.textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(userModel.getId())));
        modelUName.textProperty().bindBidirectional(new SimpleStringProperty(userModel.getName()));
        modelUTel.textProperty().bindBidirectional(new SimpleStringProperty(userModel.getTel()));
        modelURealName.textProperty().bindBidirectional(new SimpleStringProperty(userModel.getRealName()));
        modelUPassword.textProperty().bindBidirectional(new SimpleStringProperty(userModel.getPassword()));
        modelUBirth.valueProperty().bindBidirectional(new SimpleObjectProperty<>(userModel.getBirthDay()));

        SingleSelectionModel<String> ssm = modelURole.getSelectionModel();
        ssm.select(userModel.getRole()-1);

        ssm = modelUGender.getSelectionModel();
        ssm.select(userModel.getGender());
    }

    private void refreshList(){
        listView = service.searchByRole(0,false);
        tblUser.setItems(listView);
        tblUser.refresh();
        tblUser.autosize();
    }
    private void refreshList(int field){
        switch (field){
            case 0:
                listView.clear();
                listView.addAll(service.searchById(Integer.parseInt(cbxKey.getValue())));
        }
        tblUser.setItems(listView);
        tblUser.refresh();
        tblUser.autosize();
    }

    public void eventNew(ActionEvent event){
        enableEditTab(true,1);
    }
    public void eventModify(ActionEvent event){
        enableEditTab(true,2);
    }
    public void eventDelete(ActionEvent event){
        ObservableList<User> selItem = tblUser.getSelectionModel().getSelectedItems();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("确定要删除这"+selItem.size()+"条数据吗？");
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.OK)){
            for(User item:selItem){
                service.remove(item.getId());
            }
        }
        enableEditTab(false,0);
    }
    public void eventCancel(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("你确定要取消吗?");
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.OK))
            enableEditTab(false,0);

    }
    public void eventSave(ActionEvent event){
        userModel.setBirthDay(modelUBirth.getValue());
        userModel.setGender(modelUGender.getItems().indexOf(modelUGender.getValue()));
        userModel.setName(modelUName.getText());

        userModel.setPassword(modelUPassword.getText());
        userModel.setRealName(modelURealName.getText());
        userModel.setRole(1+modelURole.getItems().indexOf(modelURole.getValue()));
        userModel.setTel(modelUTel.getText());

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("请问是否保存？");
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.OK)){
            switch (modelFunction){
                case 1:
                    service.add(userModel);
                    break;
                case 2:
                    service.modify(userModel.getId(),userModel);
                    break;
            }
            enableEditTab(false,0);
        }else
            modelUName.requestFocus();
    }
    public void eventSearch(ActionEvent event){
        if(cbxKey.getValue()==null || cbxKey.getValue().equals("")){
            refreshList();
        }else
            refreshList(cbxBy.getSelectionModel().getSelectedIndex());
    }
    public void enableEditTab(boolean bool, int modelFunction){
        btnNew.setDisable(bool);
        btnModify.setDisable(bool);
        btnDel.setDisable(bool);
        btnView.setDisable(bool);
        SingleSelectionModel<Tab> ssm = tabPane.getSelectionModel();
        ssm.select(bool?1:0);
        tabModel.setText("");
        tabModel.setDisable(!bool);
        this.modelFunction = modelFunction;
        tblUser.setDisable(bool);
        switch (modelFunction){
            case 1:
                tabModel.setText("新增用户");
                clearModel();
                break;
            case 2:
                tabModel.setText("修改用户");
                loadModel();
                break;
        }

        if(!bool){
            refreshList();
        }
    }

    public void eventSelectItem(MouseEvent event){
        ObservableList<User> selItem = tblUser.getSelectionModel().getSelectedItems();
        if(selItem.size()==1){
            userModel = tblUser.getSelectionModel().getSelectedItem();
            if (event.getClickCount()==2 &&
                    event.getButton().equals(MouseButton.PRIMARY))
                enableEditTab(true,2);
        }
        itemSelected(selItem.size() != 0,
                selItem.size() > 1);
    }
    public void itemSelected(boolean selected,boolean multisel){
        btnModify.setDisable(multisel||(!selected));
        btnView.setDisable(btnModify.isDisable());
        btnDel.setDisable(!selected);
    }

    public void actBy(ActionEvent event){
        switch (cbxBy.getSelectionModel().getSelectedIndex()){
            case 3:
                cbxKey.setEditable(false);
                cbxKey.setItems(GlobalConfig.USERMANAGE_ROLE);
                cbxKey.getSelectionModel().select(0);
                break;
            case 4:
                cbxKey.setEditable(false);
                cbxKey.setItems(GlobalConfig.USERMANAGE_GENDER);
                cbxKey.getSelectionModel().select(0);
                break;
            default:
                cbxKey.setEditable(true);
                cbxKey.setItems(null);
                cbxKey.setValue("");
        }

        cbxKey.requestFocus();
    }
}
