package com.honcz.server.zrpcserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(value = "com.honcz")
public class ZrpcServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZrpcServerApplication.class, args);
    }
}
