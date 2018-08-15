package com.charleslai.charleslaispringcloudeurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class CharleslaiSpringcloudEurekaserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(CharleslaiSpringcloudEurekaserverApplication.class, args);
    }
}
