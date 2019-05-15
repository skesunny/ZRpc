package com.honcz.zrpc.zrpcserver.config;

import com.honcz.zrpc.zrpccore.servicecenter.consulservice.ConsulServiceRegistryImpl;
import com.honcz.zrpc.zrpcserver.rpccenter.RPCServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author honc.z
 * @date 2019/4/10 23:29
 */
@Configuration
public class RegistryConfig {

    @Value("${spring.cloud.consul.host}")
    private String consulHost;

    @Value("${spring.cloud.consul.port}")
    private String consulPort;

    @Value("${server.port}")
    private String serverPort;

    @Bean
    public ConsulServiceRegistryImpl getRegistryConsul() {
        return new ConsulServiceRegistryImpl(consulHost + ":" + consulPort);
    }

    @Bean
    public RPCServer rpcServer() {
        InetAddress inet = null;
        try {
            inet = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        RPCServer rpcServer = new RPCServer(inet.getHostAddress(), Integer.valueOf(serverPort), getRegistryConsul());
        return rpcServer;
    }


}
