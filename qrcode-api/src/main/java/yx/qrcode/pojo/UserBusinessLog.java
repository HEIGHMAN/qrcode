package yx.qrcode.pojo;

import lombok.*;
import lombok.experimental.Accessors;

import java.sql.Date;

@Data
@ToString()
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
public class UserBusinessLog {
    private int id;
    private int userId;
    private String business;
    private long time;
    private int result;

    public UserBusinessLog(String business,long time){
        this.business=business;
        this.time=time;
    }

    public UserBusinessLog(int user_id, String business) {
        this.userId = user_id;
        this.business = business;
    }

    public UserBusinessLog(int user_id, String business, long time) {
        this.userId = user_id;
        this.business = business;
        this.time = time;
    }

    public UserBusinessLog(int user_id, String business, long time, int result) {
        this.userId = user_id;
        this.business = business;
        this.time = time;
        this.result = result;
    }

    public UserBusinessLog(int user_id, String business, int result) {
        this.userId = user_id;
        this.business = business;
        this.result = result;
    }
}
