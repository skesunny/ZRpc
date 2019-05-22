package com.honcz.zrpc.zrpcspringbootstarter.bootstrap;

import com.honcz.zrpc.zrpcregistry.servicecenter.consulservice.ConsulServiceDiscoveryImpl;
import com.honcz.zrpc.zrpcregistry.servicecenter.consulservice.ConsulServiceRegistryImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author honc.z
 * @date 2019/4/10 23:29
 *
 * 负责注册和发现服务的bean注入
 */
@Configuration
public class DiscoveryBootStrap {

    @Value("${spring.cloud.consul.host}")
    private String consulHost;

    @Value("${spring.cloud.consul.port}")
    private String consulPort;

    /**
     * consul服务发现
     * @return
     */
    @Bean
    public ConsulServiceDiscoveryImpl getDiscoveryConsul() {
        ConsulServiceDiscoveryImpl consulServiceDiscovery = new ConsulServiceDiscoveryImpl(consulHost+":"+consulPort);
        consulServiceDiscovery.setDiscoveryAddress(consulHost+":"+consulPort);
        return consulServiceDiscovery;
    }

    /**
     * consul服务注册
     * @return
     */
    @Bean
    public ConsulServiceRegistryImpl getRegistryConsul() {
        return new ConsulServiceRegistryImpl(consulHost + ":" + consulPort);
    }
}
