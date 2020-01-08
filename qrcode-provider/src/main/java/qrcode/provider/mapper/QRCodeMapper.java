package qrcode.provider.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import yx.qrcode.pojo.QRCode;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * 关于对qrcodes表的操作
 *  1.查询记录的插入(qrcodes+userqrhistory+remarkqrhistory)
 *  2.历史记录的查询（userqrhistory+qrcodes+remarkqrlhistory）
 *  3.备注（remarkqrhistory）
 *  4.删除（userqrhistory设置del_flag=1）
 */
@Mapper
@Component
public interface QRCodeMapper {
    @Select("select qr_url from lgr_qrcodes where qr_id=#{qrId}")
    String getUrl(@PathParam("qrId") int qrId);
    //获取链接编号
    @Select("select qr_id from lgr_qrcodes where qr_url=#{url}")
    int getQrId(@PathParam("url") String url);

    //获取二维码存储地址
    @Results({@Result(column = "qr_id",property = "qrId"),@Result(column = "qr_url",property = "url"),
            @Result(column = "qr_address",property = "address")
    })
    @Select("select * from lgr_qrcodes where qr_url=#{url}")
    QRCode getAddress(@PathParam("url") String url);

    //添加二维码记录
    @Insert("insert into lgr_qrcodes(qr_url,qr_address) value(#{url},#{address})")
    int add(QRCode qrCode);

    @Select("select count(user_id) from lgr_userqrlogs where user_id=#{userId} and del_flag=0")
    int getLogCount(@PathParam("userId") int userId);
    //获取用户历史记录
    @Select("select query_id,q.qr_id,qr_url,qr_address,string,query_time " +
            "from lgr_userqrlogs u,lgr_qrcodes q " +
            "where u.user_id=#{userId} and q.qr_id=u.qr_id and del_flag=0 limit #{start},#{size}")
    @Results({@Result(column = "query_id",property = "userId"),@Result(column = "qr_id",property = "qrId"),@Result( column = "qr_url",property = "url"),
        @Result(column = "qr_address",property = "address"),@Result(column = "string",property = "remark"),
        @Result(column = "query_time",property = "time"),
    })
    List<QRCode> getList(@PathParam("userId") int userId,@PathParam("start") int start,@PathParam("size") int size);

    //添加二维码备注记录
    @Insert("insert into lgr_userqrlogs(qr_id,user_id,string) value(#{qrId},#{userId},#{remark})")
    int addRemark(QRCode qrCode);

    //修改  备注
    @Update("update lgr_userqrlogs set string=#{remark},query_time=now() where query_id=#{id}")
    void updateRemark(@PathParam("id") int id,@PathParam("remark") String remark,@PathParam("time") long time);

    //用户本地删除二维码记录
    @Update("update lgr_userqrlogs set del_flag=1 where query_id=#{id}")
    int delQRCode(@PathParam("id")int id);

    //日志记录（二维码查询记录日志）
    @Insert("insert into lgr_userqrlogs(qr_id,user_id,string,query_time) value(#{qrId},#{userId},#{remark},now())")
    void addQueryLogByObject(QRCode qrCode);
    @Insert("insert into lgr_userqrlogs(qr_id,user_id,query_time) value(#{qrId},#{userId},now())")
    void addQueryLog(@PathParam("userId")int userId,@PathParam("qrId")int qrId,@PathParam("time") long time);

    //对所有二维码的查询
    @Select("SELECT qr_url,c from lgr_querylog_view ORDER BY c DESC limit #{start},#{size}")
    @Results({@Result(column = "c",property = "count"),@Result(column = "qr_url",property = "url")})
    public List<QRCode> getQueryLogs(@PathParam("start") int start,@PathParam("size") int size);
    @Select("SELECT count(*) from lgr_querylog_view")
    public int getQueryLogCount();
}