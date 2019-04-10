package com.honcz.zrpc.zrpccore.config;

import com.honcz.zrpc.zrpccore.consulservice.ConsulServiceRegistryImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    @Bean
    public ConsulServiceRegistryImpl getConsul() {
        return new ConsulServiceRegistryImpl(consulHost+":"+consulPort);
    }
}
