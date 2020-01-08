package qrcode.provider.controller;

import glory.liu.util.GloryUtil;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import qrcode.provider.mapper.LogMapper;
import qrcode.provider.mapper.QRCodeMapper;
import yx.qrcode.pojo.QRCode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 关于对qrcodes表的操作
 * 1.查询记录的插入(qrcodes+userqrhistory+remarkqrhistory)
 * 2.历史记录的查询（userqrhistory+qrcodes+remarkqrlhistory）
 * 3.备注（remarkqrhistory）
 * 4.删除（userqrhistory设置del_flag=1）
 * 5.日志
 */
@RestController
@RequestMapping("/qr")
public class QRCodeController {

    @Autowired
    @Lazy
    private QRCodeMapper qrCodeMapper;
    @Autowired
    @Lazy
    private LogMapper logMapper;

    //本地TOMCAT
//    private static final String TOMCAT_PATH="http://localhost:8080/";
    //服务器TOMCAT
    private static final String TOMCAT_PATH="http://134.160.36.204:9080/";
    //查询二维码存储地址
    @GetMapping("/getAddress")
    public String address(@RequestParam("userId") int userId,@RequestParam("url") String url){
        logMapper.addLog(userId,"在个人查询二维码历史中查询 "+url+"的二维码",System.currentTimeMillis());
        return qrCodeMapper.getAddress(url).getAddress();
    }

    /**
     * 添加url记录
     * @param qrCode
     */
    @PostMapping("/getQR")
    String add(@RequestBody QRCode qrCode) {
        logMapper.addLog(qrCode.getUserId(),"首页搜索查询"+qrCode.getUrl()+"的二维码",System.currentTimeMillis());
        System.out.println(qrCode);
        QRCode tmpQR = qrCodeMapper.getAddress(qrCode.getUrl());//获取地址
        System.out.println(tmpQR);
        if(tmpQR != null) {
            System.out.println("qrcode表中数据");
            qrCode.setQrId(tmpQR.getQrId());
            addLog(qrCode);
            return TOMCAT_PATH+tmpQR.getAddress();
        }

        try{
            String path = GloryUtil.makeQRCodeImage(qrCode.getUrl());
            //将二维码信息加入到数据库中
            qrCode.setAddress(path);
            System.out.println("进入二维码记录添加");
            if(qrCodeMapper.add(qrCode)>0){
                qrCode.setQrId(qrCodeMapper.getQrId(qrCode.getUrl()));
                addLog(qrCode);
                return TOMCAT_PATH+path;//返回存储路径
            }
        }catch (Exception e){
            System.out.println("异常2");
            e.printStackTrace();
        }finally {
            System.out.println("Is OK");
        }
        return "";
    }
    private void addLog(QRCode qrCode){
        //更新查询日志记录 日志记录
        qrCode.setTime(System.currentTimeMillis());
        qrCodeMapper.addQueryLogByObject(qrCode);
        System.out.println("日志记录");
    }
    //从日志中查询二维码时加入数据
    @PostMapping("/aQL")
    public void addQueryLog(@RequestParam("userId") int userId,@RequestParam("qrId")int qrId){
        String url= qrCodeMapper.getUrl(qrId);
        logMapper.addLog(userId,"从历史记录中查询："+url+" 的二维码记录",System.currentTimeMillis());
        qrCodeMapper.getQueryLogs(userId,qrId);
    }

    //获取历史记录的条数
    @GetMapping("/count")
    public int getLogCount(@RequestParam("userId") int userId){
        return qrCodeMapper.getLogCount(userId);
    }
    //个人历史记录
    @GetMapping("/history/{userId}")
    List<QRCode> queryQRCodeHistory(@PathVariable int userId,@RequestParam("start") int start,@RequestParam("size") int size) {
        logMapper.addLog(userId,"获得个人查询二维码记录：start="+start+", size="+size,System.currentTimeMillis());
        List<QRCode> qrCodes = qrCodeMapper.getList(userId, start, size);
        if(qrCodes!=null) {
            List<QRCode> newQRs = new ArrayList<>();
            for (QRCode qrCode:qrCodes){
                qrCode.setAddress(TOMCAT_PATH+qrCode.getAddress());
                newQRs.add(qrCode);
            }
            return newQRs;
        }
        return null;
    }

    //插入新的备注信息
    @PostMapping("/rk")
    boolean addRemark(@RequestBody QRCode qrCode) {
        if (qrCodeMapper.addRemark(qrCode) > 0)
            return true;
        return false;
    }
    //修改备注
    @PutMapping("/upRk")
    public void updateRemark(@RequestParam("userId")int userId,@RequestParam("qLId") int qLID,@RequestParam("remark") String remark,@RequestParam("newRemark") String newRemark){
        logMapper.addLog(userId,"修改用户查询日志表中第"+qLID+"条的备注信息："+remark+"修改为："+newRemark,System.currentTimeMillis());
        qrCodeMapper.updateRemark(qLID,newRemark,System.currentTimeMillis());
    }
    //删除二维码记录
    @PutMapping("/del")
    void delQRcode(@RequestParam("userId") int userId,@RequestParam("id") int id) {
        logMapper.addLog(userId,"删除userqrhistory表中第"+id+"条数据",System.currentTimeMillis());
        qrCodeMapper.delQRCode(id);
    }

    //对所有二维码的查询
    @GetMapping("/qLC")
    public int getQueryLogCount(){
        return qrCodeMapper.getQueryLogCount();
    }
    @GetMapping("/qLog")
    public List<QRCode> getQueryLog(@RequestParam("userId") int userId,@RequestParam("start") int start,@RequestParam("size") int size){
        logMapper.addLog(userId,"管理员产看二维码查询记录从第"+start+"起"+size+"条记录",System.currentTimeMillis());
        return qrCodeMapper.getQueryLogs(start,size);
    }
}

