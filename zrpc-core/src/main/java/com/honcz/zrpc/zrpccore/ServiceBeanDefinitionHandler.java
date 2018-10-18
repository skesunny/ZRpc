package com.honcz.zrpc.zrpccore;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

/**
 * 将提供服务的interface接口作为bean注入到spring bean容器
 * 1.首先获取注解了@ZRpcService的接口
 * 2.通过在bean加载器中增加一条bean注入规则，将接口bean注入
 *
 * 解释：BeanDefinitionRegistryPostProcessor：
 *
 *
 * @author honc.z
 * @date 2018/10/18
 */
@Slf4j
public class ServiceBeanDefinitionHandler implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
