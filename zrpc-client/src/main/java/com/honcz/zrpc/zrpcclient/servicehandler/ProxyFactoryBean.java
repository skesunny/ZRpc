package com.honcz.zrpc.zrpcclient.servicehandler;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;

/**
 * FactoryBean for service proxy
 * <p>
 * 解释：FactoryBean：可以通过实现该接口定制实例化Bean的逻辑
 * 例如自己实现一个FactoryBean，功能：用来代理一个对象，对该对象的所有方法做一个拦截，在调用前后都输出一行LOG，模仿ProxyFactoryBean的功能
 *
 * @author honc.z
 * @date 2018/10/18
 */
@Slf4j
@Data
public class ProxyFactoryBean implements FactoryBean<Object> {
    private Class<?> type;

    private RPCClient rpcClient;

    @SuppressWarnings("unchecked")
    @Override
    public Object getObject() throws Exception {
        return rpcClient.proxyRequest(type);
    }


    @Override
    public Class<?> getObjectType() {
        return this.type;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
