package com.honcz.zrpc.zrpccommon;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 服务所在地址
 *
 * @author honc.z
 * @date 2018/10/18
 */
@Data
@AllArgsConstructor
public class ServiceAddress {
    /**
     * 服务ip
     */
    private String ip;
    /**
     * 服务端口
     */
    private int port;

    @Override
    public String toString(){
        return ip + ":" + port;
    }
}
