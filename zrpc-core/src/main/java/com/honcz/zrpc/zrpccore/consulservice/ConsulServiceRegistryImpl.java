package com.honcz.zrpc.zrpccore.consulservice;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.ConsulRawClient;
import com.ecwid.consul.v1.agent.model.NewService;
import com.honcz.zrpc.zrpccommon.model.ServiceAddress;
import com.honcz.zrpc.zrpccore.ServiceRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * @author hongbin
 * Created on 21/10/2017
 */

public class ConsulServiceRegistryImpl implements ServiceRegistry {
//	@Value("${spring.cloud.consul.host}")
//	private String consulHost;
//
//	@Value("${spring.cloud.consul.port}")
//	private String consulPort;

	private ConsulClient consulClient;

	public ConsulServiceRegistryImpl(String consulAddress) {
//		String consulAddress = consulHost+":"+consulPort;
		String address[] = consulAddress.split(":");
		ConsulRawClient rawClient = new ConsulRawClient(address[0], Integer.valueOf(address[1]));
		consulClient = new ConsulClient(rawClient);
	}

	@Override
	public void serviceRegister(String serviceName, ServiceAddress serviceAddress) {
		NewService newService = new NewService();
		newService.setId(generateNewIdForService(serviceName, serviceAddress));
		newService.setName(serviceName);
		newService.setTags(new ArrayList<>());
		newService.setAddress(serviceAddress.getIp());
		newService.setPort(serviceAddress.getPort());

		// TODO: make check configurable
		NewService.Check check = new NewService.Check();
		check.setTcp(serviceAddress.toString());
		check.setInterval("1s");
		newService.setCheck(check);
		consulClient.agentServiceRegister(newService);
	}

	private String generateNewIdForService(String serviceName, ServiceAddress serviceAddress){
		// serviceName + ip + port
		return serviceName + "-" + serviceAddress.getIp() + "-" + serviceAddress.getPort();
	}
}
