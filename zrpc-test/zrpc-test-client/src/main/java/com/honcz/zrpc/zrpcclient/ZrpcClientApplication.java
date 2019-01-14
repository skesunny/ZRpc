package com.honcz.zrpc.zrpcclient;

import com.honcz.zrpc.zrpccore.annotation.EnableRPCClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@EnableRPCClients(basePackages = "com.honcz.zrpc.*")
@SpringBootApplication
public class ZrpcClientApplication {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
            }
        }).start();
//        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        SpringApplication.run(ZrpcClientApplication.class, args);
    }
}
