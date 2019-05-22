package com.honcz.zrpc.zrpcclient.servicehandler;

import com.honcz.zrpc.zrpcclient.netty.ChannelManager;
import com.honcz.zrpc.zrpcclient.netty.RPCResponseFuture;
import com.honcz.zrpc.zrpcclient.netty.ResponseFutureManager;
import com.honcz.zrpc.zrpccommon.model.RPCRequest;
import com.honcz.zrpc.zrpccommon.model.RPCResponse;
import com.honcz.zrpc.zrpccommon.util.ApplicationHelper;
import com.honcz.zrpc.zrpcregistry.servicecenter.ServiceDiscovery;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author honc.z
 * @date 2019/5/22
 *
 * 负责代理调用过程：
 * 1.封装请求参数为RPCRequest
 * 2.通过Discovery和请求中的接口名,找到注册中心的服务地址
 * 3.通过地址拿到netty连接通道，使用netty将RPCRequest发送到地址
 * 4.返回返回参数
 */
@Slf4j
public class RPCClient {

    /**
     * 代理请求过程
     * @param classType
     * @return
     */
    public Object proxyRequest(Class<?> classType){
        return Proxy.newProxyInstance(classType.getClassLoader(), new Class<?>[]{classType}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                log.info("开始代理发现服务，发送请求过程");
                String targetServiceName = classType.getName();

                // Create request
                RPCRequest request = RPCRequest.builder()
                        .requestId(generateRequestId(targetServiceName))
                        .interfaceName(method.getDeclaringClass().getName())
                        .methodName(method.getName())
                        .parameters(args)
                        .parameterTypes(method.getParameterTypes()).build();

                // Get service address
                InetSocketAddress serviceAddress = getServiceAddress(targetServiceName);

                // Get channel by service address
                //通过服务地址拿到netty的连接通道
                Channel channel = ChannelManager.getInstance().getChannel(serviceAddress);
                if (null == channel) {
                    throw new RuntimeException("Cann't get channel for address" + serviceAddress);
                }

                // Send request
                RPCResponse response = sendRequest(channel, request);
                if (response == null) {
                    throw new RuntimeException("response is null");
                }
                if (response.hasException()) {
                    throw response.getException();
                } else {
                    log.info(response.getRequestId()+"调用收到的响应为"+response.getResult());
                    return response.getResult();
                }
            }
        });
    }


    /**
     * 生成此次requestId
     * @param targetServiceName
     * @return
     */
    private String generateRequestId(String targetServiceName) {
        return targetServiceName + "-" + UUID.randomUUID().toString();
    }

    /**
     * 通过服务名查询注册中心，拿到注册地址
     * @param targetServiceName
     * @return
     */
    private InetSocketAddress getServiceAddress(String targetServiceName) {
        String serviceAddress = "";
        ServiceDiscovery serviceDiscovery = (ServiceDiscovery) ApplicationHelper.getBean(ServiceDiscovery.class);
        if (serviceDiscovery != null) {
            serviceAddress = serviceDiscovery.serviceDiscory(targetServiceName);
            log.debug("Get address: {} for service: {}", serviceAddress, targetServiceName);
        }
        if (StringUtils.isEmpty(serviceAddress)) {
            throw new RuntimeException(String.format("Address of target service %s is empty", targetServiceName));
        }
        String[] array = StringUtils.split(serviceAddress, ":");
        String host = array[0];
        int port = Integer.parseInt(array[1]);
        return new InetSocketAddress(host, port);
    }

    /**
     * 向netty通道发送request
     * @param channel
     * @param request
     * @return
     */
    private RPCResponse sendRequest(Channel channel, RPCRequest request) {
        CountDownLatch latch = new CountDownLatch(1);
        RPCResponseFuture rpcResponseFuture = new RPCResponseFuture(request.getRequestId());
        ResponseFutureManager.getInstance().registerFuture(rpcResponseFuture);
        channel.writeAndFlush(request).addListener((ChannelFutureListener) future -> {
            latch.countDown();
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }

        try {
            // TODO: make timeout configurable
            return rpcResponseFuture.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.warn("Exception:", e);
            return null;
        }
    }
}
