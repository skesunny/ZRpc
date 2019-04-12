package com.honcz.zrpc.zrpccore.servicecenter.consulservice;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.ConsulRawClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.health.model.HealthService;
import com.honcz.zrpc.zrpccommon.model.ServiceAddress;
import com.honcz.zrpc.zrpcloadbalance.LoadBalancer;
import com.honcz.zrpc.zrpcloadbalance.impl.RandomLoadBalancer;
import com.honcz.zrpc.zrpccore.servicecenter.ServiceDiscovery;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author hongbin
 * Created on 21/10/2017
 */
@Slf4j
@Data
public class ConsulServiceDiscoveryImpl implements ServiceDiscovery {
//	@Value("${spring.cloud.consul.host}")
//	private String consulHost;
//
//	@Value("${spring.cloud.consul.port}")
//	private String consulPort;

	public String discoveryAddress;

	private ConsulClient consulClient;

	Map<String, LoadBalancer<ServiceAddress>> loadBalancerMap = new ConcurrentHashMap<>();

	public ConsulServiceDiscoveryImpl(String consulAdress) {
		log.debug("Use consul to do service discovery: {}", consulAdress);
		String[] address = consulAdress.split(":");
		ConsulRawClient rawClient = new ConsulRawClient(address[0], Integer.valueOf(address[1]));
		consulClient = new ConsulClient(rawClient);
	}

	@Override
	public String serviceDiscory(String serviceName) {
		log.debug("Use consul to do service discovery: {}", discoveryAddress);
		String[] address = discoveryAddress.split(":");
		ConsulRawClient rawClient = new ConsulRawClient(address[0], Integer.valueOf(address[1]));
		consulClient = new ConsulClient(rawClient);
		List<HealthService> healthServices;
		if (!loadBalancerMap.containsKey(serviceName)) {
			healthServices = consulClient.getHealthServices(serviceName, true, QueryParams.DEFAULT)
					.getValue();
			loadBalancerMap.put(serviceName, buildLoadBalancer(healthServices));

			// Watch consul
			longPolling(serviceName);
		}

		ServiceAddress loadBalAddress = loadBalancerMap.get(serviceName).next();
		if (address == null) {
			throw new RuntimeException(String.format("No service instance for %s", serviceName));
		}

		return loadBalAddress.toString();
	}

	private void longPolling(String serviceName){
		new Thread(new Runnable() {
			@Override
			public void run() {
				long consulIndex = -1;
				do {

					QueryParams param =
							QueryParams.Builder.builder()
									.setIndex(consulIndex)
									.build();

					Response<List<HealthService>> healthyServices =
							consulClient.getHealthServices(serviceName, true, param);

					consulIndex = healthyServices.getConsulIndex();
					log.debug("consul index for {} is: {}", serviceName, consulIndex);

					List<HealthService> healthServices = healthyServices.getValue();
					log.debug("service addresses of {} is: {}", serviceName, healthServices);

					loadBalancerMap.put(serviceName, buildLoadBalancer(healthServices));
				} while(true);
			}
		}).start();
	}

	private LoadBalancer buildLoadBalancer(List<HealthService> healthServices) {
		// TODO: make load balancer type configurable
		return new RandomLoadBalancer(healthServices.stream()
				.map(healthService -> {
					HealthService.Service service =healthService.getService();
					return new ServiceAddress(service.getAddress() , service.getPort());
				})
				.collect(Collectors.toList()));
	}
}
