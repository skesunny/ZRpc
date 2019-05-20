package com.honcz.server.zrpcserver;

import com.honcz.zrpc.zrpcclient.annotation.EnableZRPCClients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(value = "com.honcz")
@EnableZRPCClients(basePackages = "com.honcz.zrpc.zrpcapi")
@Slf4j
public class ZrpcServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZrpcServerApplication.class, args);
        log.info("启动成功");
    }
}
