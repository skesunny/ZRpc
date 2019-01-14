package com.honcz.zrpc.zrpcserver;

import com.honcz.zrpc.zrpccommon.annotation.ZRpcService;
import com.honcz.zrpc.zrpccommon.model.RPCRequest;
import com.honcz.zrpc.zrpccommon.model.RPCResponse;
import com.honcz.zrpc.zrpccommon.model.ServiceAddress;
import com.honcz.zrpc.zrpcregistry.ServiceRegistry;
import com.honcz.zrpc.zrpcserialization.coder.RPCDecoder;
import com.honcz.zrpc.zrpcserialization.coder.RPCEncoder;
import com.honcz.zrpc.zrpcserialization.serialization.impl.ProtobufSerializer;
import com.honcz.zrpc.zrpcserver.handler.RPCServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

/**
 * @author hongbin
 * Created on 21/10/2017
 */
@RequiredArgsConstructor
@Slf4j
public class RPCServer implements ApplicationContextAware, InitializingBean {

    @NonNull
    private String serverIp;
    @NonNull
    private int serverPort;
	@NonNull
	private ServiceRegistry serviceRegistry;

	private Map<String, Object> handlerMap = new HashMap<>();

	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		log.info("Putting handler");
		// Register handler
		getServiceInterfaces(ctx)
				.stream()
				.forEach(interfaceClazz -> {
					String serviceName = interfaceClazz.getAnnotation(ZRpcService.class).value().getName();
					Object serviceBean = ctx.getBean(interfaceClazz);
					handlerMap.put(serviceName, serviceBean);
					log.debug("Put handler: {}, {}", serviceName, serviceBean);
				});
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		startServer();
	}

	private void startServer() {
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

			ChannelFuture future = bootstrap.bind(serverIp, serverPort).sync();

			registerServices();

			log.info("Server started");

			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			throw new RuntimeException("Server shutdown!", e);
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	private void registerServices() {
		if (serviceRegistry != null) {
			for (String interfaceName : handlerMap.keySet()) {
				serviceRegistry.serviceRegister(interfaceName, new ServiceAddress(serverIp, serverPort));
				log.info("Registering service: {} with address: {}:{}", interfaceName, serverIp, serverPort);
			}
		}
	}

	private List<Class<?>> getServiceInterfaces(ApplicationContext ctx) {
		Class<? extends Annotation> clazz = ZRpcService.class;
		return ctx.getBeansWithAnnotation(clazz)
				.values().stream()
				.map(AopUtils::getTargetClass)
				.map(cls -> Arrays.asList(cls.getInterfaces()))
				.flatMap(List::stream)
				.filter(cls -> Objects.nonNull(cls.getAnnotation(clazz)))
				.collect(Collectors.toList());
	}
}
