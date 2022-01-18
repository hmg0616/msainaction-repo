package com.hmg.eurekasvr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer // 스프링 서비스에서 유레카 서버 활성화
public class EurekasvrApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekasvrApplication.class, args);
    }

}
