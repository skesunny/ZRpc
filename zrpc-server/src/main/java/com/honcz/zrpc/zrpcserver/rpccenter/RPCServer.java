package com.honcz.zrpc.zrpcserver.rpccenter;

import com.honcz.zrpc.zrpccommon.annotation.ZRpcService;
import com.honcz.zrpc.zrpccommon.model.ServiceAddress;
import com.honcz.zrpc.zrpccommon.util.ApplicationHelper;
import com.honcz.zrpc.zrpcregistry.servicecenter.consulservice.ConsulServiceRegistryImpl;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hongbin
 * Created on 21/10/2017
 */
@RequiredArgsConstructor
@Slf4j
public class RPCServer{

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @NonNull
    public String serverIp;
    @NonNull
    public int serverPort;
    @NonNull
    public ConsulServiceRegistryImpl serviceRegistry;

    private Map<String, Object> handlerMap = new HashMap<>();


//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//        Map<String, Object> beanMap = ApplicationHelper.getBeansByAnnotion(ZRpcService.class);
//        for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
//            handlerMap.put(entry.getValue().getClass().getInterfaces()[0].getName(), entry.getValue());
//        }
////        threadPoolTaskExecutor.execute(new Runnable() {
////            @Override
////            public void run() {
////                startServer();
////            }
////        });
//        startServer();
//    }
//
//    private void startServer() {
//
//        // Get ip and port
//        log.debug("Starting server on port: {}", serverPort);
//        EventLoopGroup bossGroup = new NioEventLoopGroup();
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//        try {
//            ServerBootstrap bootstrap = new ServerBootstrap();
//            bootstrap.group(bossGroup, workerGroup)
//                    .channel(NioServerSocketChannel.class)
//                    .childHandler(new ChannelInitializer<SocketChannel>() {
//                        @Override
//                        public void initChannel(SocketChannel channel) throws Exception {
//                            ChannelPipeline pipeline = channel.pipeline();
//                            pipeline.addLast(new RPCDecoder(RPCRequest.class, new ProtobufSerializer()));
//                            pipeline.addLast(new RPCEncoder(RPCResponse.class, new ProtobufSerializer()));
//                            pipeline.addLast(new RPCServerHandler(handlerMap));
//                        }
//                    });
//            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
//            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
//
//            ChannelFuture future = bootstrap.bind(serverPort).sync();
//
//            registerServices();
//
//            log.info("Server started");
//
//            future.channel().closeFuture().sync();
//        } catch (InterruptedException e) {
//            throw new RuntimeException("Server shutdown!", e);
//        } finally {
//            workerGroup.shutdownGracefully();
//            bossGroup.shutdownGracefully();
//        }
//    }
//
//    private void registerServices() {
//        if (serviceRegistry != null) {
//            for (String interfaceName : handlerMap.keySet()) {
//                serviceRegistry.serviceRegister(interfaceName, new ServiceAddress(serverIp, serverPort));
//                log.info("Registering service: {} with address: {}:{}", interfaceName, serverIp, serverPort);
//            }
//        }
//    }

//	private List<Class<?>> getServiceInterfaces(ApplicationContext ctx) {
//		Class<? extends Annotation> clazz = ZRpcService.class;
//		return ctx.getBeansWithAnnotation(clazz)
//				.values().stream()
//				.map(AopUtils::getTargetClass)
//				.map(cls -> Arrays.asList(cls.getInterfaces()))
//				.flatMap(List::stream)
//				.filter(cls -> Objects.nonNull(cls.getAnnotation(clazz)))
//				.collect(Collectors.toList());
//	}
}
