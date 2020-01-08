package qrcode.provider.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.server.PathParam;

@Mapper
@Component
public interface LogMapper {
    @Insert("insert into lgr_userbusinesslogs(user_id,b_info,b_time) value(#{userId},#{info},now())")
    void addLog(@PathParam("userId") int userId, @PathParam("info") String info,@PathParam("time") long time);
}
