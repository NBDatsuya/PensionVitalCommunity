package neusoft.pensioncommunity.controller;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public interface Controller extends Initializable {

    /**
     * 视图的初始化，数据绑定
     */
    @Override
    void initialize(URL url, ResourceBundle resourceBundle) ;

    /**
     * 初始化视图后载入数据
     */
    void requestLoadData();
}
