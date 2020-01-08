package yx.qrcode.pojo;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Date;

/**
 * 用户类，包括
 *用户编号
 * 账号
 * 密码
 *
 */
@Data
@ToString()
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
public class User implements Serializable {
    private static final long serialVersionUID = -4035754716493701436L;
    private int id;
    private int logId;
    private String username;
    private String password;
    private long time;//注册
    private long loginTime;
    private long exitTime;
    private int groupid=2;//2为普通用户，1为超级用户
    private String rediskey;

    public User(String name, String password) {
        this.username = name;
        this.password = password;
    }

    public User(int id, String name, long time, int groupid) {
        this.id = id;
        this.username = name;
        this.time = time;
        this.groupid = groupid;
    }

    public User(int id, String username,int groupid) {
        this.id = id;
        this.username = username;
        this.groupid=groupid;
    }

    public User(String username, long loginTime, long exitTime, int groupid) {
        this.username = username;
        this.loginTime = loginTime;
        this.exitTime = exitTime;
        this.groupid = groupid;
    }

    public User(int id, String username, long loginTime, long exitTime, int groupid, int logId) {
        this.id = id;
        this.username = username;
        this.loginTime = loginTime;
        this.exitTime = exitTime;
        this.groupid = groupid;
        this.logId = logId;
    }

}