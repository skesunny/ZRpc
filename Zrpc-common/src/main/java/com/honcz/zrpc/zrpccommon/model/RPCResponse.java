package com.honcz.zrpc.zrpccommon.model;

import lombok.Data;

/**
 * @author honc.z
 * @date 2018/10/17
 * RPC调用响应model
 */
@Data
public class RPCResponse {

    private String requestId;
    private Exception exception;
    private Object result;

    public boolean hasException() {
        return exception != null;
    }
}
