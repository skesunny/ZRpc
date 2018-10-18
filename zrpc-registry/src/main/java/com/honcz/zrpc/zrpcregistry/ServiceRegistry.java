package com.honcz.zrpc.zrpcregistry;

import com.honcz.zrpc.zrpccommon.ServiceAddress;

/**
 * 服务注册接口
 *
 * @author honc.z
 * @date 2018/10/18
 */
public interface ServiceRegistry {
    /**
     * 通过服务地址与服务名进行注册
     * @param serviceName
     * @param serviceAddress
     */
    void register(String serviceName, ServiceAddress serviceAddress);
}
