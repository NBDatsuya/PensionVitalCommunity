package neusoft.pensioncommunity.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import lombok.Setter;

public class AlertUtil {
    private Alert alert = new Alert(null);
    @Setter
    private String headerText;
    @Setter
    private Alert.AlertType alertType;

    public ButtonType showToConfirm(String headerText){
        return null;
    }

    public void alertWarning(String headerText){}
    public void alertError(String headerText){}

}
