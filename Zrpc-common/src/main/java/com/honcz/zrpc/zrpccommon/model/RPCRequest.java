package com.honcz.zrpc.zrpccommon.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author honc.z
 * @date 2018/10/17
 * RPC调用请求model
 */
@Data
@Builder
public class RPCRequest {
	private String requestId;
	private String interfaceName;
	private String methodName;
	private Class<?>[] parameterTypes;
	private Object[] parameters;
}
