package com.honcz.zrpc.zrpcserver.rpccenter;

import com.honcz.zrpc.zrpccommon.model.RPCRequest;
import com.honcz.zrpc.zrpccommon.model.RPCResponse;
import com.honcz.zrpc.zrpccommon.model.ServiceAddress;
import com.honcz.zrpc.zrpcregistry.servicecenter.consulservice.ConsulServiceRegistryImpl;
import com.honcz.zrpc.zrpcserialization.coder.RPCDecoder;
import com.honcz.zrpc.zrpcserialization.coder.RPCEncoder;
import com.honcz.zrpc.zrpcserialization.serialization.impl.ProtobufSerializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author hongbin
 * Created on 21/10/2017
 */
@RequiredArgsConstructor
@Slf4j
public class RPCServer {
    @NonNull
    public String serverIp;
    @NonNull
    public int serverPort;
    @Autowired
    public ConsulServiceRegistryImpl serviceRegistry;

    public void startServer(Map<String, Object> handlerMap) {

        // Get ip and port
        log.debug("Starting server on port: {}", serverPort);
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(new RPCDecoder(RPCRequest.class, new ProtobufSerializer()));
                            pipeline.addLast(new RPCEncoder(RPCResponse.class, new ProtobufSerializer()));
                            pipeline.addLast(new RPCServerHandler(handlerMap));
                        }
                    });
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.bind(serverPort).sync();

            registerServices(handlerMap);

            log.info("Server started");

            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException("Server shutdown!", e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    private void registerServices(Map<String, Object> handlerMap) {
        if (serviceRegistry != null) {
            for (String interfaceName : handlerMap.keySet()) {
                serviceRegistry.serviceRegister(interfaceName, new ServiceAddress(serverIp, serverPort));
                log.info("Registering service: {} with address: {}:{}", interfaceName, serverIp, serverPort);
            }
        }
    }
}
