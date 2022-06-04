package neusoft.pensioncommunity.model;

import lombok.*;

import java.time.LocalDate;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class User extends Model{

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
}
