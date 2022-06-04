package neusoft.pensioncommunity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Reserve extends Model{
    private int busId;
    private int seniorId;
    private int logisticId;
    /**
     * 0 = 未确认
     * 1 = 已确认
     * 2 = 已取消
     */
    private int status;
    private LocalDateTime reserveTime;
}
