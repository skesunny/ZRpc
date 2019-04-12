package com.honcz.zrpc.zrpccore.servicecenter;

import com.honcz.zrpc.zrpccommon.model.ServiceAddress;

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
    void serviceRegister(String serviceName, ServiceAddress serviceAddress);
}
