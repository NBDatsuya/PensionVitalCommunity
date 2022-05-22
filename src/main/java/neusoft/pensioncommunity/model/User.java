package neusoft.pensioncommunity.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User extends Model {
    private int id;
    private String name;
    private String realName;
    private String password;
    private String avatar;

    /**
     * 1 = Admin
     * 2 = Steward
     * 3 = Logistic
     */
    private int role;

//    private final IntegerProperty idProperty;
//    private final StringProperty nameProperty;
//    private final StringProperty realNameProperty;
//    private final StringProperty passwordProperty;
//    private final StringProperty avatarProperty;
//    private final IntegerProperty roleProperty;


    public static String valueOfRole(Integer role) {
        switch (role) {
            case 1:
                return "管理员";
            case 2:
                return "生活管家";
            case 3:
            default:
                return "后勤管理";
        }
    }

    @Override
    public int getID() {
        return id;
    }
}
