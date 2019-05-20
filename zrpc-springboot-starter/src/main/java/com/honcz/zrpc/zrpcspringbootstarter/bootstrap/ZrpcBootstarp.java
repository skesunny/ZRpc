package com.honcz.zrpc.zrpcspringbootstarter.bootstrap;

import com.honcz.zrpc.zrpcclient.annotation.EnableZRPCClients;
import com.honcz.zrpc.zrpcclient.servicehandler.ServiceBeanDefinitionHandler;
import com.honcz.zrpc.zrpccommon.util.NetUtils;
import com.honcz.zrpc.zrpcregistry.servicecenter.consulservice.ConsulServiceDiscoveryImpl;
import com.honcz.zrpc.zrpcregistry.servicecenter.consulservice.ConsulServiceRegistryImpl;
import com.honcz.zrpc.zrpcserver.rpccenter.RPCServer;
import com.honcz.zrpc.zrpcserver.springlistenner.SpringInitApplicationListenner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author honc.z
 * @date 2019/5/20
 */
@Configuration
//@ConditionalOnBean(EnableZRPCClients.class)
public class ZrpcBootstarp {
    @Value("${spring.cloud.consul.host}")
    private String consulHost;

    @Value("${spring.cloud.consul.port}")
    private String consulPort;

    /**
     * 将使用了@ZrpcService注解的接口，在注入到spring容器里时，代理为自定义FactoryBean
     * @return
     */
    @Bean
    public ServiceBeanDefinitionHandler serviceBeanDefinitionHandler(){
        return new ServiceBeanDefinitionHandler();
    }

    /**
     *
     * @return
     */
    @Bean
    @DependsOn("getRegistryConsul")
    public RPCServer rpcServer() {
        InetAddress inet = null;
        try {
            inet = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        RPCServer rpcServer = new RPCServer(inet.getHostAddress(), Integer.valueOf(NetUtils.getAvailablePort(inet.getHostAddress(),6000,10000)), getRegistryConsul());
        return rpcServer;
    }

    @Bean
    public ConsulServiceRegistryImpl getRegistryConsul() {
        return new ConsulServiceRegistryImpl(consulHost + ":" + consulPort);
    }

    @Bean
    public ConsulServiceDiscoveryImpl getDiscoveryConsul() {
        ConsulServiceDiscoveryImpl consulServiceDiscovery = new ConsulServiceDiscoveryImpl(consulHost+":"+consulPort);
        consulServiceDiscovery.setDiscoveryAddress(consulHost+":"+consulPort);
        return consulServiceDiscovery;
    }

    @Bean
    public SpringInitApplicationListenner springInitApplicationListenner(){
        return new SpringInitApplicationListenner();
    }
}
