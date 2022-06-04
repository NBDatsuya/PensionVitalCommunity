package neusoft.pensioncommunity.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import neusoft.pensioncommunity.GlobalConfig;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Bus extends Model{

    private String code;
    private String name;
    private int direction;
    private String annual;

    /**
     * 0 = 全天
     * 1 = 上午
     * 2 = 下午
     * 3 = 晚上
     */
    private int hours;
    private LocalTime timeBegin;
    private LocalTime timeDeadline;
    private String memo;

    public int getCountPassenger(){
        int count = 0;
        for (Reserve item : GlobalConfig.reserveSevice.getAll())
            if(item.getBusId()==this.id && item.getStatus()==0)
                count++;
        return count;
    }
    public boolean isDDLSet(){
        return (timeDeadline!=null);
    }
}
