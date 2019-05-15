package com.honcz.zrpc.zrpcapi;

import com.honcz.zrpc.zrpccore.annotation.ZRpcService;

/**
 * @author honc.z
 * @date 2018/10/25
 */
@ZRpcService(TestService.class)
public interface TestService {
    String getId(String id);
}
