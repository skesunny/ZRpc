package com.honcz.server.zrpcserver.serviceimpl;

import com.honcz.zrpc.zrpcapi.TestService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author honc.z
 * @date 2018/10/25
 */
@Service
public class TestServiceImpl implements TestService {
    @Override
    public String getId(String id) {
        return id+"success";
    }
}
