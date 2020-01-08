package user.provider.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import yx.qrcode.pojo.UserBusinessLog;

import javax.websocket.server.PathParam;
import java.util.List;

@Mapper
@Component
public interface LogMapper {

    @Insert("insert into lgr_userbusinesslogs(user_id,b_info,b_time) value(#{userId},#{info},now())")
    void addLog(@PathParam("userId") int userId, @PathParam("info") String info, @PathParam("time") long time);

    @Select("select b_info,b_time from lgr_userbusinesslogs where user_id=#{userId} and b_time BETWEEN #{login} AND #{exit}")
    @Results({@Result(column = "b_info",property = "business"),@Result(column = "b_time",property = "time")})
    List<UserBusinessLog> getLogs(@PathParam("userId")int userId,@PathParam("login") long login,@PathParam("exit") long exit);
//    @Select("select b_info,b_time from userbusinesslog" +
//            "where user_id=#{userId} and b_time BETWEEN #{login} AND #{exit} LIMIT #{start},#{size}")
//    @Results({@Result(column = "b_info",property = "business"),@Result(column = "b_time",property = "time")})
//    List<UserBusinessLog> getLogs(@PathParam("userId")int userId,@PathParam("login") long login,@PathParam("exit") long exit,@PathParam("start")int start,@PathParam("size")int size);
    @Select("select b_info,b_time from lgr_userbusinesslogs " +
            "where user_id=#{userId} and b_time BETWEEN #{login} AND #{exit}")
    @Results({@Result(column = "b_info",property = "business"),@Result(column = "b_time",property = "time")})
    List<UserBusinessLog> getLogsByLogin(@PathParam("userId")int userId,@PathParam("login") long login);
}
