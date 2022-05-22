package neusoft.pensioncommunity.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import neusoft.pensioncommunity.model.User;

public class UserConfig implements Controller {

    @FXML
    Label lblUName;

    @FXML
    Label lblURName;

    @FXML
    Label lblURole;


    User currentUser;

    public void load(User currentUser) {
        this.currentUser = currentUser;
        lblUName.setText(currentUser.getName());
        lblURName.setText(currentUser.getRealName());
        lblURole.setText(User.valueOfRole(currentUser.getRole()));

        load();
    }

    @Override
    public void load() {

    }
}
