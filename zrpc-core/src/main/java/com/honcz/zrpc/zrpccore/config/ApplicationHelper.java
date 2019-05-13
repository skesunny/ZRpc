package com.honcz.zrpc.zrpccore.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @author honc.z
 * @date 2019/4/11
 */
@Component
public class ApplicationHelper implements ApplicationContextAware {
    private static ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        applicationContext = ctx;
    }

    public static Object getBean(Class beanName){
        return applicationContext.getBean(beanName);
    }

    public static Map<String, Object> getBeansByAnnotion(Class<? extends Annotation> clz){
        return applicationContext.getBeansWithAnnotation(clz);
    }

}
