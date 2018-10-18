package com.honcz.zrpc.zrpccore.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author honc.z
 * @date 2018/10/18
 * 启动类注解，表示这是一个调用了rpc的服务
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableRPCClients {
    //引用的api接口的位置
    String[] basePackages() default {};
}
