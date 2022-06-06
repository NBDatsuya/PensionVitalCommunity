package neusoft.pensioncommunity.model;

import javafx.util.converter.LocalDateTimeStringConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import neusoft.pensioncommunity.GlobalConfig;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Bus extends Model{

    private String code;
    private String name;
    private int direction;
    /**
     * 0 = 每天
     * 1 = 每周一
     * 2 = 每周二
     * 3 = 每周三
     * 4 = 每周四
     * 5 = 每周五
     * 6 = 每周六
     * 7 = 每周日
     */
    private int annual;

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

    public boolean isReservable(){
        if (timeDeadline==null) return false;
        LocalDateTime nextDeadline = LocalDateTime.from(LocalDate.now().atTime(timeDeadline));
        int now = LocalDateTime.now().getDayOfWeek().getValue();

        if(now == annual || annual==0)
            return nextDeadline.isAfter(LocalDateTime.now());
        else
            return false;
    }
}
