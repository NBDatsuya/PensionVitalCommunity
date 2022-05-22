module neusoft.pensioncommunity {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires lombok;

    opens neusoft.pensioncommunity to javafx.fxml;
    exports neusoft.pensioncommunity;

    opens neusoft.pensioncommunity.model to javafx.fxml;
    exports neusoft.pensioncommunity.model;

    opens neusoft.pensioncommunity.controller to javafx.fxml;
    exports neusoft.pensioncommunity.controller;

    opens neusoft.pensioncommunity.view to javafx.fxml;
    exports neusoft.pensioncommunity.view;
}