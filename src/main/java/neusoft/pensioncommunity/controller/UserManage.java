package neusoft.pensioncommunity.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import neusoft.pensioncommunity.dao.UserDao;
import neusoft.pensioncommunity.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserManage implements Controller{

    @FXML
    TableView<User> tblUser;

    @FXML
    private TableColumn<User, Integer> colID;

    @FXML
    private TableColumn<User, String> colName;

    @FXML
    private TableColumn<User, String> colRealName;

    @FXML
    private TableColumn<User, Integer> colRole;

    @Override
    public void load() {
        ObservableList<User> myList= FXCollections.observableArrayList();
        myList.add(new User(1,"123","黄旭达","123","",1));
        myList.add(new User(2,"321","雷培根","123","",2));
        myList.add(new User(3,"123","林源奇","123","",3));
        myList.add(new User(4,"123","付会柱","123","",2));
    }
}
