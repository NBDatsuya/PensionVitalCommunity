package neusoft.pensioncommunity.model;

import lombok.*;

import java.time.LocalDate;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class User extends Model{

    private int id;
    private String name;
    private String password;
    /**
     * 0 = Admin
     * 1 = Steward
     * 2 = Logistic
     */
    private int role;

    private String realName;

    /**
     * 0 = Other
     * 1 = Male
     * 2 = Female
     */
    private int gender;
    private String tel;
    private LocalDate birthDay;

    public static String valueOfRole(int role) {
        switch (role) {
            case 0:
                return "管理员";
            case 1:
                return "生活管家";
            case 2:
            default:
                return "后勤管理";
        }
    }

    public static String valueOfGender(int gender){
        switch (gender) {
            case 2:
            default:
                return "其他";
            case 0:
                return "男";
            case 1:
                return "女";
        }
    }
    public int getId() {
        return id;
    }

}
