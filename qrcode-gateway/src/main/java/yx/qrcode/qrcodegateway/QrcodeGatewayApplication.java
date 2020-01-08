package yx.qrcode.qrcodegateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy//网关
@EnableDiscoveryClient
public class QrcodeGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(QrcodeGatewayApplication.class, args);
    }

}
