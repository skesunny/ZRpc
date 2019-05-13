package com.honcz.zrpc.zrpcserver.rpccenter;

import com.honcz.zrpc.zrpccommon.model.RPCRequest;
import com.honcz.zrpc.zrpccommon.model.RPCResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Handle the RPC request
 * 实现ChannelHandler接口，自定义拆包/装包逻辑
 * 通过netty，拿到request并去注册中心找到request中的调用接口
 *
 * @author hongbin
 * Created on 21/10/2017
 */
@Slf4j
@AllArgsConstructor
public class RPCServerHandler extends SimpleChannelInboundHandler<RPCRequest> {

	private Map<String, Object> handlerMap;

	@Override
	public void channelRead0(final ChannelHandlerContext ctx, RPCRequest request) throws Exception {
		log.info("Get request: {}", request);
		RPCResponse response = new RPCResponse();
		response.setRequestId(request.getRequestId());
		try {
			Object result = handleRequest(request);
			response.setResult(result);
		} catch (Exception e) {
			log.error("Get exception when hanlding request, exception: {}", e);
			response.setException(e);
		}
		ctx.writeAndFlush(response).addListener(
				(ChannelFutureListener) channelFuture -> {
					log.info("Sent response for request: {}", request.getRequestId());
				});
	}

	private Object handleRequest(RPCRequest request) throws Exception {
		// Get service bean
		String serviceName = request.getInterfaceName();
		Object serviceBean = handlerMap.get(serviceName);
		if (serviceBean == null) {
			throw new RuntimeException(String.format("No service bean available: %s", serviceName));
		}

		// Invoke by reflect
		Class<?> serviceClass = serviceBean.getClass();
		String methodName = request.getMethodName();
		Class<?>[] parameterTypes = request.getParameterTypes();
		Object[] parameters = request.getParameters();
		Method method = serviceClass.getMethod(methodName, parameterTypes);
		method.setAccessible(true);
		return method.invoke(serviceBean, parameters);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		log.error("server caught exception", cause);
		ctx.close();
	}
}
