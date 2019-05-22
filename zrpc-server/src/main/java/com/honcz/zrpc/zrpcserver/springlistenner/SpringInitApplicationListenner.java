package com.honcz.zrpc.zrpcserver.springlistenner;

import com.honcz.zrpc.zrpccommon.model.RPCRequest;
import com.honcz.zrpc.zrpccommon.model.RPCResponse;
import com.honcz.zrpc.zrpccommon.model.ServiceAddress;
import com.honcz.zrpc.zrpccommon.annotation.ZRpcService;
import com.honcz.zrpc.zrpccommon.util.ApplicationHelper;
import com.honcz.zrpc.zrpcserialization.coder.RPCDecoder;
import com.honcz.zrpc.zrpcserialization.coder.RPCEncoder;
import com.honcz.zrpc.zrpcserialization.serialization.impl.ProtobufSerializer;
import com.honcz.zrpc.zrpcserver.rpccenter.RPCServer;
import com.honcz.zrpc.zrpcserver.rpccenter.RPCServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author honc.z
 * @date 2019/5/17
 */
@Slf4j
public class SpringInitApplicationListenner implements ApplicationListener<ContextRefreshedEvent> {
    private Map<String, Object> handlerMap = new ConcurrentHashMap<>();

    @Autowired
    private RPCServer rpcServer;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Map<String, Object> beanMap = ApplicationHelper.getBeansByAnnotion(ZRpcService.class);
        for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
            //只有value不是FactoryBean（是实现类），才注册到注册中心
            if (!(entry.getValue() instanceof Proxy)) {
                handlerMap.put(entry.getValue().getClass().getInterfaces()[0].getName(), entry.getValue());
            }
        }
        //只有有服务发布，才会启动netty并注册
        if (handlerMap.size() != 0) {
            //让新线程去启动netty，防止netty阻塞 【监听关闭】 导致主线程阻塞
            threadPoolTaskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    rpcServer.startServer(handlerMap);
                }
            });
        }
    }
}
