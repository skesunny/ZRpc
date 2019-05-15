package com.honcz.zrpc.zrpccore.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author honc.z
 * @date 2018/10/17
 * 注解在api接口类，表示提供服务的api
 */
@Target(ElementType.TYPE) //接口、类、枚举、注解
@Retention(RetentionPolicy.RUNTIME)
@Component
@Inherited //说明子类可以继承父类中的该注解
public @interface ZRpcService {
    //注解类的反射
    Class<?> value();
}
