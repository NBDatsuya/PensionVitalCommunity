package neusoft.pensioncommunity.controller;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lombok.Setter;
import neusoft.pensioncommunity.GlobalConfig;
import neusoft.pensioncommunity.model.Bus;
import neusoft.pensioncommunity.model.Senior;
import neusoft.pensioncommunity.model.User;
import neusoft.pensioncommunity.service.BusService;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class BusManage implements Controller{
    @Setter
    private Main mainController;

    private BusService service = GlobalConfig.busService;
    private ObservableList<Bus> listView;
    private Bus busModel;
    private int modelFunction;

    private LocalTime tempTimeBegin;
    private LocalTime tempTimeDeadline;

    /**
     * 视图的初始化
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colID.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()).asObject());

        colName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        colCode.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCode()));
        colDir.setCellValueFactory(c -> new SimpleStringProperty(
                GlobalConfig.valueOfDirection(c.getValue().getDirection()
                )));
        colAnnual.setCellValueFactory(c -> new SimpleStringProperty(GlobalConfig.valueOfAnnual(c.getValue().getAnnual())));
        colHours.setCellValueFactory(c -> new SimpleStringProperty(
                GlobalConfig.valueOfHours(c.getValue().getHours())));
        colBegin.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getTimeBegin()));
        colDeadline.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getTimeDeadline().equals(LocalTime.MIN)?
                "未设置":
                c.getValue().getTimeDeadline().toString()));
        colCount.setCellValueFactory(c->new SimpleObjectProperty<>(c.getValue().getCountPassenger()));
        colMemo.setCellValueFactory(c->new SimpleStringProperty(c.getValue().getMemo()));

        tblBus.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        cbxBy.getItems().setAll(GlobalConfig.SELECTION_BUSFIELDS);
        cbxBy.getSelectionModel().select(0);

        modelBAnnual.getItems().setAll(GlobalConfig.SELECTION_ANNUAL);
        modelBDirection.getItems().setAll(GlobalConfig.SELECTION_DIRECTION);
        modelBHours.getItems().setAll(GlobalConfig.SELECTION_HOURS);
        clearModel();

        modelBBegin.focusedProperty().addListener((ov, oldV, newV) -> {
            if (!newV) { // 验证时间格式
                lblCaution.setText("");
                if(modelBBegin.getText().isEmpty()){
                    lblCaution.setText("出发时间不能为空");
                    lblCaution.setVisible(true);
                    modelBBegin.requestFocus();
                    return;
                }
                if(modelBBegin.getText().length()==4) modelBBegin.setText("0"+modelBBegin.getText());

                try{
                    tempTimeBegin = LocalTime.parse(modelBBegin.getText());
                    if(tempTimeBegin.isBefore(LocalTime.of(12,0))
                            && tempTimeBegin.isAfter(LocalTime.of(6,0)))
                        modelBHours.getSelectionModel().select(1);
                    else if(tempTimeBegin.isBefore(LocalTime.of(18,0))
                            && tempTimeBegin.isAfter(LocalTime.of(12,0)))
                        modelBHours.getSelectionModel().select(2);
                    else if(tempTimeBegin.isBefore(LocalTime.of(22,0))
                            && tempTimeBegin.isAfter(LocalTime.of(18,0)))
                        modelBHours.getSelectionModel().select(3);
                    else
                        modelBHours.getSelectionModel().select(0);
                }
                catch(Exception e){
                    lblCaution.setText("出发时间格式错误");
                    lblCaution.setVisible(true);
                    modelBBegin.requestFocus();
                }


            }
        });

        modelBDeadline.focusedProperty().addListener((ov, oldV, newV) -> {
            if (!newV) { // 验证时间格式
                lblCaution.setText("");
                if(modelBDeadline.getText().length()==4) modelBDeadline.setText("0"+modelBDeadline.getText());
                if(modelBDeadline.getText().isEmpty()){
                    tempTimeDeadline = null;
                    lblCaution.setText("可以在添加班次后\n手动添加截止时间");
                    lblCaution.setVisible(true);
                }
                try{
                    if(modelBDeadline.getText().isEmpty()) return;
                    tempTimeDeadline = LocalTime.parse(modelBDeadline.getText());
                }
                catch(Exception e){
                    lblCaution.setText("截止时间格式错误");
                    lblCaution.setVisible(true);
                    modelBDeadline.requestFocus();
                }
            }
        });


    }

    private void clearModel() {
        busModel = new Bus();
        busModel.setId(service.getNewId());
        busModel.setTimeBegin(LocalTime.now());
        busModel.setTimeDeadline(null);
        tempTimeDeadline = null;
        tempTimeBegin = LocalTime.now();

        modelBID.setText(String.valueOf(busModel.getId()));
        modelBCode.setText("");
        modelBName.setText("");
        modelBMemo.setText("");
        modelBBegin.setText("");
        modelBDeadline.setText("");

        modelBDirection.getSelectionModel().select(0);
        modelBAnnual.getSelectionModel().select(0);
        modelBHours.getSelectionModel().select(0);

        lblCaution.setVisible(false);
        modelBCode.requestFocus();
    }
    private void loadModel(){
        busModel.isReservable();

        modelBID.setText(String.valueOf(busModel.getId()));
        modelBCode.setText(busModel.getCode());
        modelBName.setText(busModel.getName());
        modelBMemo.setText(busModel.getMemo());
        modelBBegin.setText(busModel.getTimeBegin().toString());
        modelBDeadline.setText(busModel.isDDLSet()?busModel.getTimeDeadline().toString():"");

        modelBDirection.getSelectionModel().select(busModel.getDirection());
        modelBAnnual.getSelectionModel().select(busModel.getAnnual());
        modelBHours.getSelectionModel().select(busModel.getHours());

        tempTimeBegin = busModel.getTimeBegin();
        tempTimeDeadline = busModel.isDDLSet()?busModel.getTimeDeadline():null;

        lblCaution.setVisible(false);
        modelBCode.requestFocus();
    }

    private void refreshList() {
        listView = service.getAll();
        tblBus.setItems(listView);
        tblBus.refresh();
        tblBus.autosize();
    }
    private void refreshList(int field) {
        if(cbxKey.getValue().isEmpty()) {refreshList();return;}
        switch (field){
            case 0:
                tblBus.getSelectionModel().clearSelection();
                for(Bus item : tblBus.getItems())
                    if (item.getId()==Integer.parseInt(cbxKey.getValue())){
                        tblBus.getSelectionModel().select(item);
                        break;
                    }
                break;
            case 1:
                listView.setAll(service.searchByCode(cbxKey.getValue()));
                break;
            case 2:
                listView.setAll(service.searchByName(cbxKey.getValue()));
                break;
            case 3:
                listView.setAll(service.searchByDirection(cbxKey.getSelectionModel().getSelectedIndex()));
                break;
            case 4:
                listView.setAll(service.searchByAnnual(cbxKey.getSelectionModel().getSelectedIndex()));
                break;
            case 5:
                listView.setAll(service.searchByHours(cbxKey.getSelectionModel().getSelectedIndex()));
                break;
            case 6:
                listView.setAll(service.searchByDeadline(cbxKey.getValue()));
                break;
            case 7:
                listView.setAll(service.searchByBeginTime(cbxKey.getValue()));
                break;
            case 8:
                listView.setAll(service.searchByMemo(cbxKey.getValue()));
                break;
        }
    }

    /**
     * 初始化视图后载入数据
     */
    @Override
    public void requestLoadData() {
        if(GlobalConfig.currentUser.getRole()!=2) btnReserve.setVisible(false);
        refreshList();
    }

    private void enableEditTab(boolean bool, int modelFunction){
        btnNew.setDisable(bool);
        btnModify.setDisable((bool ^
                tblBus.getSelectionModel().getSelectedItems().isEmpty()) ||
                modelFunction>0);
        btnDel.setDisable(btnModify.isDisable());
        btnReserve.setDisable(bool);
        tabPane.getSelectionModel().select(bool?1:0);
        tabModel.setDisable(!bool);
        this.modelFunction = modelFunction;
        tblBus.setDisable(bool);
        switch (modelFunction){
            case 1:
                tabModel.setText("新增班车");
                clearModel();
                break;
            case 2:
                tabModel.setText("修改班车");
                loadModel();
                break;
            case 0:
                tabModel.setText("");
        }

        if(!bool){
            refreshList();
        }
    }

    @FXML
    private AnchorPane apModule;

    @FXML
    private AnchorPane apReserve;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXButton btnDel;

    @FXML
    private JFXButton btnModify;

    @FXML
    private JFXButton btnNew;

    @FXML
    private JFXButton btnOK;

    @FXML
    private JFXButton btnReserve;

    @FXML
    private JFXButton btnSearch;

    @FXML
    private ComboBox<String> cbxBy;

    @FXML
    private ComboBox<String> cbxKey;

    @FXML
    private TableColumn<Bus, String> colAnnual;

    @FXML
    private TableColumn<Bus, LocalTime> colBegin;

    @FXML
    private TableColumn<Bus, String> colCode;

    @FXML
    private TableColumn<Bus, Integer> colCount;

    @FXML
    private TableColumn<Bus, String> colDir;

    @FXML
    private TableColumn<Bus, Integer> colID;

    @FXML
    private TableColumn<Bus, String> colMemo;

    @FXML
    private TableColumn<Bus, String> colName;

    @FXML
    private TableColumn<Bus, String> colHours;

    @FXML
    private TableColumn<Bus, String> colDeadline;

    @FXML
    private Label lblCaution;

    @FXML
    private ComboBox<String> modelBAnnual;

    @FXML
    private TextField modelBBegin;

    @FXML
    private TextField modelBCode;

    @FXML
    private ComboBox<String> modelBDirection;

    @FXML
    private ComboBox<String> modelBHours;

    @FXML
    private Label modelBID;

    @FXML
    private TextArea modelBMemo;

    @FXML
    private TextField modelBName;

    @FXML
    private TextField modelBDeadline;

    @FXML
    private Tab tabList;

    @FXML
    private Tab tabModel;

    @FXML
    private TabPane tabPane;

    @FXML
    private TableView<Bus> tblBus;

    @FXML
    void actBy(ActionEvent event) {
        switch (cbxBy.getSelectionModel().getSelectedIndex()){
            case 3:
                cbxKey.setEditable(false);
                cbxKey.getItems().setAll(GlobalConfig.SELECTION_DIRECTION);
                break;
            case 4:
                cbxKey.setEditable(false);
                cbxKey.getItems().setAll(GlobalConfig.SELECTION_ANNUAL);
                break;
            case 5:
                cbxKey.setEditable(false);
                cbxKey.getItems().setAll(GlobalConfig.SELECTION_HOURS);
                break;
            default:
                cbxKey.setEditable(true);
                cbxKey.setValue("");

        }
        cbxKey.requestFocus();
    }

    @FXML
    void eventCancel(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("你确定要取消吗?");
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.OK))
            enableEditTab(false,0);
        else
            modelBCode.requestFocus();
    }

    @FXML
    void eventDel(ActionEvent event) {
        ObservableList<Bus> selItem = tblBus.getSelectionModel().getSelectedItems();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("确定要删除这"+selItem.size()+"条数据吗？");
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.OK)){
            for(Bus item:selItem){
                if(!service.remove(item.getId())){
                    alert.setAlertType(Alert.AlertType.WARNING);
                    alert.setHeaderText("无法删除班次("+item.getCode()+")"+item.getName()+"，因为该班次关联了多条未确认的预约数据！");
                    alert.show();
                }
            }
        }
        enableEditTab(false,0);
    }

    @FXML
    void eventModify(ActionEvent event) {
        enableEditTab(true,2);
    }

    @FXML
    void eventNew(ActionEvent event) {
        enableEditTab(true,1);
    }

    @FXML
    void eventReserve(ActionEvent event) {

        FXMLLoader loader = null;
        try {
            loader = new FXMLLoader(GlobalConfig.getViewUrl(
                    "ReserveBus.fxml"));

            apReserve.getChildren().setAll((Node) loader.load());

            ReserveBus rb = loader.getController();
            rb.setModelBus(busModel);
            rb.setParentController(this);
            rb.setMainController(mainController);
            rb.requestLoadData();
            apReserve.setVisible(true);
            apReserve.setFocusTraversable(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void requestCloseReserve(){
        apReserve.setVisible(false);
        apReserve.setFocusTraversable(false);
    }
    @FXML
    void eventSave(ActionEvent event) {

        if(!verifyBus()) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("请问是否保存？");
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.OK)){

            busModel.setCode(modelBCode.getText());
            busModel.setName(modelBName.getText());
            busModel.setAnnual(modelBAnnual.getSelectionModel().getSelectedIndex());
            busModel.setDirection(modelBDirection.getSelectionModel().getSelectedIndex());
            busModel.setTimeDeadline(tempTimeDeadline==null?LocalTime.MIN:tempTimeDeadline);
            busModel.setTimeBegin(tempTimeBegin);
            busModel.setHours(modelBHours.getSelectionModel().getSelectedIndex());
            busModel.setMemo(modelBMemo.getText());

            switch (modelFunction){
                case 1:
                    service.add(busModel);
                    break;
                case 2:
                    service.modify(busModel.getId(),busModel);
                    break;
            }
            enableEditTab(false,0);
        }else
            modelBCode.requestFocus();
    }

    private boolean verifyBus() {
        if(tempTimeDeadline!=null && tempTimeBegin.isBefore(tempTimeDeadline)){
            lblCaution.setVisible(true);
            lblCaution.setText("发车时间不能早于\n预约截止时间");
            return false;
        }
        if(modelBCode.getText().isEmpty()){
            lblCaution.setVisible(true);
            lblCaution.setText("代码不能为空");
            return false;
        }
        if(modelBName.getText().isEmpty()){
            lblCaution.setVisible(true);
            lblCaution.setText("路线名称不能为空");
            return false;
        }
        lblCaution.setVisible(false);
        return true;
    }

    @FXML
    void eventSearch(ActionEvent event) {
        refreshList(cbxBy.getSelectionModel().getSelectedIndex());
    }

    @FXML
    void eventSelectItem(MouseEvent event) {
        ObservableList<Bus> selItem = tblBus.getSelectionModel().getSelectedItems();
        boolean isEmpty = selItem.isEmpty();
        if(selItem.size()==1){
            busModel = tblBus.getSelectionModel().getSelectedItem();
            if (event.getClickCount()==2 &&
                    event.getButton().equals(MouseButton.PRIMARY))
                enableEditTab(true,2);
        }
        itemSelected(!isEmpty,
                selItem.size() > 1);
    }

    public void itemSelected(boolean selected,boolean multisel){
        btnModify.setDisable((!selected)||(modelFunction!=0) ||(multisel));
        btnReserve.setDisable((btnModify.isDisable()) ^ (GlobalConfig.currentUser.getRole()!=2));
        btnDel.setDisable((!selected) || (modelFunction!=0));
    }
}
