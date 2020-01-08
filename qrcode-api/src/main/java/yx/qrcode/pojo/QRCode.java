package yx.qrcode.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 这是定义的二维码的类，其包括
 * 二维码编号
 * 链接数据
 * 在服务器上的存储位置
 * 备注
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class QRCode implements Serializable {
    private int qrId;
    private int userId;
    private String url;
    private String address;
    private int count;
    private String remark;
    private long time;

    //二维码
    public QRCode(int id, String url, String address, int count, String remark) {
        this.qrId = id;
        this.url = url;
        this.address = address;
        this.count = count;
        this.remark = remark;
    }

    //备注记录 更改记录时的,及查询二维码日志记录

    public QRCode(int id, int userId, String remark) {
        this.qrId = id;
        this.userId = userId;
        this.remark = remark;
    }

    //添加url
    public QRCode(int userId, String url, String remark) {
        this.userId = userId;
        this.url = url;
        this.remark = remark;
    }

    public QRCode(int qrId, String url, String remark, long time,String address) {
        this.qrId = qrId;
        this.url = url;
        this.remark = remark;
        this.time = time;
        this.address = address;
    }

    public QRCode(String url, int count) {
        this.url = url;
        this.count = count;
    }
}
