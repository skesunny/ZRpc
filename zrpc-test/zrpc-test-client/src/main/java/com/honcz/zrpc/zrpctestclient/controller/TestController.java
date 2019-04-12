package com.honcz.zrpc.zrpctestclient.controller;

import com.honcz.zrpc.zrpcapi.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author honc.z
 * @date 2019/4/12
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private TestService testService;

    @RequestMapping("/id")
    public String getId(@RequestParam("id")String id){
        return testService.getId(id);
    }
}
