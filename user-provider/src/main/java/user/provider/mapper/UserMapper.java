package user.provider.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import yx.qrcode.pojo.User;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * 对users表进行操作
 * 1.添加会员
 * 2.更新（密码）
 */
@Mapper
@Component
public interface UserMapper {

    //用户登录获取用户密码
    @Select("select user_id,username,password,reg_time,groupid from lgr_users where username=#{name}")
    @Results({@Result(column = "user_id",property = "id"),@Result(column = "reg_time",property = "time")})
    User userLogin(@PathParam("name") String name);

    //添加登录日志
    @Insert("insert into lgr_userloginlogs(user_id,login_time) value(#{userId},now())")
    void addLoginLog(@PathParam("userId") int userId,@PathParam("login") long login);
    //用户退出日志添加
    @Update("update lgr_userloginlogs set exit_time=now() where login_id=#{id}")
    void addExitLog(@PathParam("id") int id,@PathParam("login") long exit);
    //获取当前用户登录日志的id
    @Select("select MAX(login_id) from lgr_userloginlogs where user_id=#{userId}")
    int getLoginLogId(@PathParam("userId") int userId);

    //更新密码
    @Update("update lgr_users set password=#{password} where user_id=#{userId}")
    boolean update(@PathParam("userId") int userId,@PathParam("password") String password);
    @Select("select password from lgr_users where user_id=#{userId}")
    String getPwd(@PathParam("userid") int userId);

    //获取用户登录记录
    @Select("select uh.login_id,users.user_id,username,login_time,exit_time,groupid from lgr_users users,lgr_userloginlogs uh " +
            "where users.user_id=uh.user_id ORDER BY login_time DESC LIMIT #{start},#{size}")
    @Results({@Result(column = "user_id",property = "id"),@Result(column = "username",property = "username"),
        @Result(column = "login_time",property = "loginTime"),@Result(column = "exit_time",property = "exitTime"),
            @Result(column = "login_id",property = "logId")
    })
    List<User>getLoginLogs(@PathParam("start") int start,@PathParam("size") int size);
    @Select("select uh.login_id,users.user_id,username,login_time,exit_time,groupid from lgr_users users,lgr_userloginlogs uh " +
            "where users.user_id=uh.user_id ORDER BY login_time DESC")
    @Results({@Result(column = "user_id",property = "id"),@Result(column = "username",property = "username"),
            @Result(column = "login_time",property = "loginTime"),@Result(column = "exit_time",property = "exitTime"),
            @Result(column = "login_id",property = "logId")
    })
    List<User> userAllLoginLogs();

    @Select("SELECT count(*) from lgr_userloginlogs")
    int getUserLogCount();

    //提供用户名获取用户登录情况%
    @Select("select uh.login_id,users.user_id,username,uh.login_time,uh.exit_time,groupid from lgr_users users,lgr_userloginlogs uh " +
            "where users.username LIKE #{username} and users.user_id=uh.user_id ORDER BY login_time DESC LIMIT #{start},#{size}")
    @Results({@Result(column = "user_id",property = "id"),@Result(column = "username",property = "username"),
            @Result(column = "login_time",property = "loginTime"),@Result(column = "exit_time",property = "exitTime"),
            @Result(column = "login_id",property = "logId")
    })
    List<User> getLoginLogByUsername(@PathParam("username") String username,@PathParam("start")int start,@PathParam("size")int size);

    @Select("select count(*) from lgr_users users,lgr_userloginlogs uh where username LIKE #{username} and users.user_id=uh.user_id")
    int getUserLogCountByusername(@PathParam("username") String username);


}
