package glory.liu.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Base64;

/**
 * author: Glory
 * 我定义的工具类
 */
public class GloryUtil {
    private static int height = 200;
    private static int width=200;
    //邮箱验证码（注册）

    //二维码
    public static String makeQRCodeImage(String url) throws WriterException, IOException {
        String address ="qrImages/"+System.currentTimeMillis()+".png";
        //本地存储地址
        //        String file = "D:/qrcode/"+address;
        String file = "/webapp/qatst/lgr/"+address;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE,height,width);
        //本地TOMCAT
//        String webAddress = "http://localhost:8080/"+address;
        String webAddress = "http://134.160.36.204:9080/"+address;

        Path path = FileSystems.getDefault().getPath(file);
        MatrixToImageWriter.writeToPath(bitMatrix,"PNG",path);
        Client client = new Client();

        WebResource resource = client.resource(webAddress);
        byte[] bytes = FileUtils.readFileToByteArray(new File(file));
        resource.put(String.class,bytes);
        System.out.println("成功");
        return address;
    }

    public static void makeQRCodeImage(String url, String address) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE,height,width);
        Path path = FileSystems.getDefault().getPath(address);
        MatrixToImageWriter.writeToPath(bitMatrix,"PNG",path);
    }

    /**
     * 简单加密
     * @param data 原数据
     * @param key   加密的密钥
     * @return  密文
     */
    public static String encode(String data, String key){
        byte[] sj = data.getBytes();
        int len1 = sj.length;
        byte[] k = key.getBytes();
        int len2 = k.length;
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < len1; i++) {
            System.out.print(sj[i]+" ");
            sj[i] = (byte) (sj[i] ^ k[i % len2]);
            sb.append((char)((sj[i] ^ k[i % len2])));
        }
        System.out.println();
        return sb.toString();
    }

    /**
     * 简单解密
     * @param data 密文
     * @param key   密钥
     * @return  原数据
     */
    public static String decode(String data, String key){
        byte[] bt = data.getBytes();
        int len1 = bt.length;
        byte[] k = key.getBytes();
        int len2 = k.length;
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < len1; i++){
            bt[i] = (byte) (bt[i]^k[i%len2]);
            System.out.print(bt[i] + " ");
            sb.append((char)((bt[i] ^ k[i % len2])));
        }
        return sb.toString();
    }

    /**
     * 最简单的解密
     *  与自身按一定的规则就行取异或
     * @param data 原数据
     * @return  密文
     */
    public static String encode(String data){
        byte[] bt = data.getBytes();
        int len = bt.length;
        StringBuffer sb = new StringBuffer();
        for (int i = 0;i < len; i++){
            bt[i]=(byte)(bt[i]^bt[(i+2)%len]);
            sb.append((char)bt[i]);
        }
        return sb.toString();
    }

    /**
     * 最简单的解密
     *  按一定的规则与自身取异或
     * @param data  密文
     * @return  原数据
     */
    public static String decode(String data){
        byte[] bt = data.getBytes();
        int len = bt.length;
        StringBuffer sb = new StringBuffer();
        for(int i = len-1; i >= 0; i--){
            bt[i]=(byte)(bt[i]^bt[(i+2)%len]);
            sb.append((char)bt[i]);
        }
        sb.reverse();
        return sb.toString();
    }

    public static void main(String[] args) {
//        System.out.println(decode(encode("hello", "glory"), "glory"));
//        System.out.println(decode(encode("I love you")));
        try {
//            makeQRCodeImage("http://www.baidu.com");
//            long time = System.currentTimeMillis();
//            String path = "../qrcode/user-consumer/src/main/resources/static/qrcodeImages/"+time+".png";
//            makeQRCodeImage("bibi.com",path);
            System.out.println(makeQRCodeImage("www.glory.com"));
        }catch (IOException e){

        }catch (WriterException e){

        }finally {
            System.out.println("over ");
        }

    }
}
