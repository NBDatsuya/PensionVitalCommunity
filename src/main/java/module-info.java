module neusoft.pensioncommunity {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires lombok;
    requires com.google.gson;

    opens neusoft.pensioncommunity to javafx.fxml;
    exports neusoft.pensioncommunity;

    opens neusoft.pensioncommunity.model to javafx.fxml,com.google.gson;
    exports neusoft.pensioncommunity.model;

    opens neusoft.pensioncommunity.controller to javafx.fxml;
    exports neusoft.pensioncommunity.controller;

}