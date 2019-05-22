package com.honcz.zrpc.zrpcspringbootstarter.bootstrap;

import com.honcz.zrpc.zrpccommon.util.NetUtils;
import com.honcz.zrpc.zrpcserver.rpccenter.RPCServer;
import com.honcz.zrpc.zrpcserver.springlistenner.SpringInitApplicationListenner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author honc.z
 * @date 2019/5/20
 * <p>
 * 负责框架server部分的启动
 */
@Configuration
public class ZrpcServerBootstarp {

    /**
     * 初始化RpcServer
     *
     * @return
     */
    @Bean
    public RPCServer rpcServer() {
        InetAddress inet = null;
        try {
            inet = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        RPCServer rpcServer = new RPCServer(inet.getHostAddress(), Integer.valueOf(NetUtils.getAvailablePort(inet.getHostAddress(), 6000, 10000)));
        return rpcServer;
    }

    /**
     * 监听Spring容器初始化完毕事件（ContextRefreshedEvent）
     *
     * @return
     */
    @Bean
    public SpringInitApplicationListenner springInitApplicationListenner() {
        return new SpringInitApplicationListenner();
    }
}
