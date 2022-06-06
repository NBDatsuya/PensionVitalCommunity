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

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;


import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.Setter;
import neusoft.pensioncommunity.GlobalConfig;
import neusoft.pensioncommunity.dao.UserDao;
import neusoft.pensioncommunity.model.Senior;
import neusoft.pensioncommunity.model.User;
import neusoft.pensioncommunity.service.UserService;

import java.io.IOException;
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
    private AnchorPane apService;

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

    @Setter
    @Getter
    private Main mainController;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colID.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()).asObject());

        colName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        colRealName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getRealName()));
        colRole.setCellValueFactory(c -> new SimpleStringProperty(
                    GlobalConfig.valueOfRole(c.getValue().getRole()
                )));
        colTel.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTel()));
        colBirth.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getBirthDay()));
        colGender.setCellValueFactory(c -> new SimpleStringProperty(
                    GlobalConfig.valueOfGender(c.getValue().getGender()
                )));


        tblUser.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        cbxBy.getItems().setAll(GlobalConfig.SELECTION_USERFIELDS);
        cbxBy.getSelectionModel().select(0);
        cbxKey.getSelectionModel().select(0);
        modelURole.getItems().setAll(GlobalConfig.SELECTION_ROLE);
        modelUGender.getItems().setAll(GlobalConfig.SELECTION_GENDER);
    }

    @Override
    public void requestLoadData() {
        refreshList();
    }

    private void clearModel() {
        userModel = new User();
        userModel.setId(service.getNewId());
        userModel.setBirthDay(LocalDate.now());

        modelUID.setText(String.valueOf(userModel.getId()));
        modelUName.setText("");
        modelUTel.setText("");
        modelURealName.setText("");
        modelUPassword.setText("");
        modelUBirth.setValue(userModel.getBirthDay());

        modelURole.getSelectionModel().select(0);
        modelURole.setDisable(false);
        modelUGender.getSelectionModel().select(0);

        modelUName.requestFocus();

    }
    private void loadModel(){
        modelUID.setText(String.valueOf(userModel.getId()));
        modelUName.setText(userModel.getName());
        modelUTel.setText(userModel.getTel());
        modelURealName.setText(userModel.getRealName());
        modelUPassword.setText(userModel.getPassword());
        modelUBirth.setValue(userModel.getBirthDay());

        modelURole.getSelectionModel().select(userModel.getRole()-1);
        modelURole.setDisable(true);
        modelUGender.getSelectionModel().select(userModel.getGender());

        modelUName.requestFocus();
    }

    private void refreshList(){
        listView = service.searchNotByRole(0);
        tblUser.setItems(listView);
        tblUser.refresh();
        tblUser.autosize();
    }
    private void refreshList(int field){
        switch (field){
            case 0:
                tblUser.getSelectionModel().clearSelection();
                for(User item : tblUser.getItems())
                    if (item.getId()==Integer.parseInt(cbxKey.getValue())){
                        tblUser.getSelectionModel().select(item);
                        break;
                    }
                break;
            case 1:
                listView.setAll(service.searchByName(cbxKey.getValue()));
                break;
            case 2:
                listView.setAll(service.searchByRealName(cbxKey.getValue()));
                break;
            case 3:
                listView.setAll(service.searchByRole(
                        1+cbxKey.getSelectionModel().
                                getSelectedIndex()));
                break;
            case 4:
                listView.setAll(service.searchByGender(
                        cbxKey.getSelectionModel().
                                getSelectedIndex()));
                break;
            case 5:
                listView.setAll(service.searchByTel(cbxKey.getValue()));
                break;
            case 6:
                listView.setAll(service.searchByBirthday(
                        LocalDate.parse(cbxKey.getValue())));
                break;
        }


        if(field==0) return;

        if(listView.contains(null)){ //??可以改进
            tblUser.getItems().clear();
        }else{
            tblUser.setItems(listView);
            tblUser.refresh();
            tblUser.autosize();
        }
        mainController.requestMessage("共找到"+listView.size()+"条数据");
    }

    @FXML void eventNew(ActionEvent event){
        enableEditTab(true,1);
    }
    @FXML void eventModify(ActionEvent event){
        enableEditTab(true,2);
    }
    @FXML void eventDelete(ActionEvent event){
        ObservableList<User> selItem = tblUser.getSelectionModel().getSelectedItems();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("确定要删除这"+selItem.size()+"条数据吗？");
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.OK)){
            for(User item:selItem){
                if(!service.remove(item.getId())){
                    alert.setAlertType(Alert.AlertType.WARNING);
                    alert.setHeaderText("无法删除用户("+item.getId()+")"+item.getRealName()+"，因为该用户关联了多条长者住户的数据！");
                    alert.show();
                }
            }
        }
        enableEditTab(false,0);

    }
    @FXML void eventCancel(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("你确定要取消吗?");
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.OK))
            enableEditTab(false,0);
        else
            modelUName.requestFocus();

    }
    @FXML void eventSave(ActionEvent event){

        if(!verifyUser()) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("请问是否保存？");
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.OK)){

            userModel.setBirthDay(modelUBirth.getValue());
            userModel.setGender(modelUGender.getItems().indexOf(modelUGender.getValue()));
            userModel.setName(modelUName.getText());

            userModel.setPassword(modelUPassword.getText());
            userModel.setRealName(modelURealName.getText());
            userModel.setRole(1+modelURole.getItems().indexOf(modelURole.getValue()));
            userModel.setTel(modelUTel.getText());

            switch (modelFunction){
                case 1:
                    service.add(userModel);
                    break;
                case 2:
                    service.modify(userModel.getId(),userModel);
                    break;
            }
            enableEditTab(false,0);
        }
        else
            modelUName.requestFocus();
    }
    @FXML void eventSearch(ActionEvent event){
        if(cbxKey.getValue()==null || cbxKey.getValue().equals(""))
            refreshList();
        else
            refreshList(cbxBy.getSelectionModel().getSelectedIndex());
    }
    @FXML void eventViewService(ActionEvent event){
        FXMLLoader loader = null;
        try {
            loader = new FXMLLoader(GlobalConfig.getViewUrl(
                    "serviceViewAssistant.fxml"));

            apService.getChildren().setAll((Node) loader.load());

            ServiceViewAssistant sva = loader.getController();
            sva.setModelUser(userModel);
            sva.setParentController(this);
            sva.requestLoadData();

            apService.setVisible(true);
            apService.setFocusTraversable(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML void eventSelectItem(MouseEvent event){
        ObservableList<User> selItem = tblUser.getSelectionModel().getSelectedItems();
        boolean isEmpty = selItem.isEmpty();
        if(selItem.size()==1){
            userModel = tblUser.getSelectionModel().getSelectedItem();
            if (event.getClickCount()==2 &&
                    event.getButton().equals(MouseButton.PRIMARY))
                enableEditTab(true,2);
        }
        itemSelected(!isEmpty,
                selItem.size() > 1);
    }
    public void requestCloseView(){
        apService.setVisible(false);
        apService.setFocusTraversable(false);
    }

    @FXML void actBy(ActionEvent event){
        switch (cbxBy.getSelectionModel().getSelectedIndex()){
            case 3:
                cbxKey.setEditable(false);
                cbxKey.getItems().setAll(GlobalConfig.SELECTION_ROLE);
                cbxKey.getSelectionModel().select(0);
                break;
            case 4:
                cbxKey.setEditable(false);
                cbxKey.getItems().setAll(GlobalConfig.SELECTION_GENDER);
                cbxKey.getSelectionModel().select(0);
                break;
            default:
                cbxKey.setEditable(true);
                cbxKey.getItems().clear();
                cbxKey.setValue("");
        }

        cbxKey.requestFocus();
    }

    public void enableEditTab(boolean bool, int modelFunction){
        btnNew.setDisable(bool);
        btnModify.setDisable((bool ^
                tblUser.getSelectionModel().getSelectedItems().isEmpty()) ||
                modelFunction>0);
        btnDel.setDisable(btnModify.isDisable());
        btnView.setDisable(bool);
        tabPane.getSelectionModel().select(bool?1:0);
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
            case 0:
                tabModel.setText("");
        }

        if(!bool){
            refreshList();
        }
    }

    public void itemSelected(boolean selected,boolean multisel){
        btnModify.setDisable((!selected)||(modelFunction!=0) ||(multisel));
        btnView.setDisable(multisel ||
                tblUser.getSelectionModel().getSelectedItem()==null ||
                tblUser.getSelectionModel().getSelectedItem().getRole() != 1);
        btnDel.setDisable((!selected) || (modelFunction!=0));
    }

    public boolean verifyUser(){
        if(modelUName.getText().isEmpty()){
            lblCaution.setVisible(true);
            lblCaution.setText("登录名不能为空");
            modelUName.requestFocus();
            return false;
        }
        if(modelURealName.getText().isEmpty()){
            lblCaution.setVisible(true);
            lblCaution.setText("真实姓名不能为空");
            modelURealName.requestFocus();
            return false;
        }
        if(modelUPassword.getText().isEmpty()){
            lblCaution.setVisible(true);
            lblCaution.setText("密码不能为空");
            modelUPassword.requestFocus();
            return false;
        }
        if(!service.verifyName(modelUName.getText(),modelFunction==1?null:userModel)){
            lblCaution.setVisible(true);
            lblCaution.setText("用户名已经存在");
            modelUName.requestFocus();
            return false;
        }
        if(!service.verifyPassword(modelUPassword.getText())){
            lblCaution.setVisible(true);
            lblCaution.setText("密码必须不少于8位");
            modelUPassword.requestFocus();
            return false;
        }
        lblCaution.setVisible(false);
        return true;
    }
}
