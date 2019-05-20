package com.honcz.zrpc.zrpctestclient;

import com.honcz.zrpc.zrpcclient.annotation.EnableZRPCClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableZRPCClients(basePackages = "com.honcz.zrpc.zrpcapi")
@ComponentScan(value = "com.honcz.zrpc")
public class ZrpcTestClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZrpcTestClientApplication.class, args);
	}

}

