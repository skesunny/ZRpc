package com.honcz.zrpc.zrpccore.servicecenter;

/**
 * 服务发现接口
 *
 * @author honc.z
 * @date 2018/10/18
 */
public interface ServiceDiscovery {
    /**
     * 通过服务名发现服务（信息）
     * @param serviceName
     * @return
     */
    String serviceDiscory(String serviceName);
}
