package com.honcz.zrpc.zrpctestclient;

import com.honcz.zrpc.zrpccore.annotation.EnableRPCClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableRPCClients(basePackages = "com.honcz.zrpc.zrpcapi")
@ComponentScan(value = "com.honcz.zrpc")
public class ZrpcTestClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZrpcTestClientApplication.class, args);
	}

}

