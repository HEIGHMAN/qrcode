package user.consumer.controller;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import yx.qrcode.pojo.QRCode;

import java.util.List;

@RestController
@RequestMapping("/api/qr")
public class QRCodeConsumerController {
    private static final String adress="http://localhost:17831/";
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/addQR")
    public String add(@RequestBody QRCode qrCode) {
        System.out.println(qrCode);
        String url=adress+"qr/getQR";
        System.out.println("start addqr");
        String address = restTemplate.postForEntity(url,qrCode, String.class).getBody();
        System.out.println(address);
        return address;
    }
    //获取历史记录的条数
    @GetMapping("/count")
    public int getLogCount(@RequestParam("userId") int userId){
        String url=adress+"qr/count?userId="+userId;
        int c = restTemplate.getForObject(url,Integer.class);
        System.out.println(c);
        return  c;
    }
    //获取历史查询记录
    @GetMapping("/history/{userId}")
    public List<QRCode> queryQRCodeHistory(@PathVariable int userId,@RequestParam("start") int start,@RequestParam("size") int size) {
        String url=adress+"qr/history/"+userId+"?start="+start+"&size="+size;
        return restTemplate.getForObject(url,List.class);
    }

    @PostMapping("/aQL")
    public void addQueryLog(@RequestParam("userId") int userId,@RequestParam("qrId")int qrId){
        String url = adress+"qr/aQL?userId="+userId+"&qrId="+qrId;
        restTemplate.postForEntity(url,null,null);
    }
    //给链接加备注
    @PostMapping("/rk")
    public boolean addRemark(@RequestBody QRCode qrCode) {
        String url=adress+"qr/rk";
        boolean flag = restTemplate.postForEntity(url,qrCode, Boolean.class).getBody();
        return flag;
    }

    /**
     * //修改备注
     * @param userId
     * @param qLId  用户查询日志的id
     * @param remark
     */
    @PutMapping("/upRk")
    public void updateRemark(@RequestParam("userId")int userId,@RequestParam("qLId") int qLId,@RequestParam("remark") String remark,@RequestParam("newRemark") String newRemark){
        String url = adress+"qr/upRk?userId="+userId+"&qLId="+qLId+"&remark="+remark+"&newRemark="+newRemark;
        restTemplate.put(url,null);
    }

    /**
     * 删除：设置删除标记为1
     * @param id
     */
    @PutMapping("/del")
    public void delQRcode(@RequestParam("userId") int userId,@RequestParam("id") int id) {
        String url=adress+"qr/del?userId="+userId+"&id="+id;
        restTemplate.put(url,null);
    }

    //管理查询二维记录
    @GetMapping("/qLC")
    public int getQueryLogCount(){
        String url=adress+"qr/qLC";
        return restTemplate.getForObject(url,Integer.class);
    }
    @GetMapping("/qLog")
    public List<QRCode> getQueryLogs(@RequestParam("userId") int userId,@RequestParam("start") int start,@RequestParam("size") int size){
        String url=adress+"qr/qLog?userId="+userId+"&start="+start+"&size="+size;
        return restTemplate.getForObject(url,List.class);
    }
}
