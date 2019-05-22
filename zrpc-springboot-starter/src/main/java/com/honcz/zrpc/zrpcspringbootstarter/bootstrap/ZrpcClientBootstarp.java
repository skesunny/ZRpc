package com.honcz.zrpc.zrpcspringbootstarter.bootstrap;

import com.honcz.zrpc.zrpcclient.annotation.EnableZRPCClients;
import com.honcz.zrpc.zrpcclient.servicehandler.RPCClient;
import com.honcz.zrpc.zrpcclient.servicehandler.ServiceBeanDefinitionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author honc.z
 * @date 2019/5/22
 *
 * 负责框架client部分的启动
 */
@Configuration
public class ZrpcClientBootstarp {
    /**
     * 将使用了@ZrpcService注解的接口，在注入到spring容器里时，代理为自定义FactoryBean
     *
     * @return
     */
    @Bean
    public ServiceBeanDefinitionHandler serviceBeanDefinitionHandler() {
        return new ServiceBeanDefinitionHandler(rpcClient());
    }

    /**
     * 初始话RPCclient
     * @return
     */
    @Bean
    public RPCClient rpcClient() {
        return new RPCClient();
    }
}
