package neusoft.pensioncommunity.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Senior extends Model{

    private String name;
    private int gender;
    private String identity;
    private LocalDate birthDay;
    private String telSelf;
    private String telRelative;

    private int assistantId;

    /**
     * 0 = 未分配管家
     * 1 = 在住
     * 2 = 迁出
     * 3 = 去世
     */
    private int status;

    public int getAge(){
        return LocalDate.now().getYear()-birthDay.getYear();
    }
}
