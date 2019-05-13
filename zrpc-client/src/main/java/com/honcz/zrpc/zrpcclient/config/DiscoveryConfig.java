package com.honcz.zrpc.zrpcclient.config;

import com.honcz.zrpc.zrpccore.servicecenter.consulservice.ConsulServiceDiscoveryImpl;
import com.honcz.zrpc.zrpccore.servicecenter.consulservice.ConsulServiceRegistryImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author honc.z
 * @date 2019/4/10 23:29
 */
@Configuration
public class DiscoveryConfig {

    @Value("${spring.cloud.consul.host}")
    private String consulHost;

    @Value("${spring.cloud.consul.port}")
    private String consulPort;

    @Bean
    public ConsulServiceDiscoveryImpl getDiscoveryConsul() {
        ConsulServiceDiscoveryImpl consulServiceDiscovery = new ConsulServiceDiscoveryImpl(consulHost+":"+consulPort);
        consulServiceDiscovery.setDiscoveryAddress(consulHost+":"+consulPort);
        return consulServiceDiscovery;
    }


//    @DependsOn(value = "getDiscoveryConsul")
//    @Bean
//    public ServiceBeanDefinitionHandler getHandler(){
//        ServiceBeanDefinitionHandler serviceBeanDefinitionHandler = new ServiceBeanDefinitionHandler();
//        return serviceBeanDefinitionHandler;
//    }

}
