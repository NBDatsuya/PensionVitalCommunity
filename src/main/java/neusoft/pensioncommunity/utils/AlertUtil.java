package neusoft.pensioncommunity.utils;

import javafx.scene.control.Alert;
import lombok.Setter;

public class AlertUtil {
    private Alert alert = new Alert(null);
    @Setter
    private String headerText;
    @Setter
    private Alert.AlertType alertType;

    public void show(String headerText, Alert.AlertType alertType){

    }

    public void alert(String headerText, Alert.AlertType alertType){

    }
}
