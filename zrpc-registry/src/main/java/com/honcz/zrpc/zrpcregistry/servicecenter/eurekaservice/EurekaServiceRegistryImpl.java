//package com.honcz.zrpc.zrpcregistry.servicecenter.eurekaservice;
//
//import com.honcz.zrpc.zrpccommon.model.ServiceAddress;
//import com.honcz.zrpc.zrpcregistry.ServiceRegistry;
//import com.netflix.appinfo.CloudInstanceConfig;
//import com.netflix.appinfo.InstanceInfo;
//import com.netflix.appinfo.providers.CloudInstanceConfigProvider;
//import com.netflix.discovery.DefaultEurekaClientConfig;
//import com.netflix.discovery.DiscoveryClient;
//import org.springframework.cloud.netflix.eureka.InstanceInfoFactory;
//
///**
// * @author honc.z
// * @date 2018/10/18
// */
//public class EurekaServiceRegistryImpl implements ServiceRegistry {
//    DiscoveryClient discoveryClient;
//    @Override
//    public void register(String serviceName, ServiceAddress serviceAddress) {
//        CloudInstanceConfigProvider cloudInstanceConfigProvider = new CloudInstanceConfigProvider();
//        CloudInstanceConfig cloudInstanceConfig = cloudInstanceConfigProvider.get();
//        InstanceInfoFactory instanceInfoFactory = new InstanceInfoFactory();
//        InstanceInfo instanceInfo = instanceInfoFactory.create(cloudInstanceConfig);
//        DefaultEurekaClientConfig defaultEurekaClientConfig = new DefaultEurekaClientConfig(serviceName);
//        discoveryClient = new DiscoveryClient(instanceInfo,defaultEurekaClientConfig);
//    }
//}
