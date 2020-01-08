package yx.qrcode.qrcodeconfigprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class QrcodeConfigProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(QrcodeConfigProviderApplication.class, args);
    }

}
