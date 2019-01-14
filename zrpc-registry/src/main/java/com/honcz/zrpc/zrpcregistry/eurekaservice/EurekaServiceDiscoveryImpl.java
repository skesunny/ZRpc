//package com.honcz.zrpc.zrpcregistry.eurekaservice;
//
//import com.honcz.zrpc.zrpccommon.model.ServiceAddress;
//import com.honcz.zrpc.zrpcregistry.ServiceDiscovery;
//import com.netflix.appinfo.CloudInstanceConfig;
//import com.netflix.appinfo.InstanceInfo;
//import com.netflix.appinfo.providers.CloudInstanceConfigProvider;
//import com.netflix.appinfo.providers.EurekaConfigBasedInstanceInfoProvider;
//import com.netflix.discovery.DefaultEurekaClientConfig;
//import com.netflix.discovery.DiscoveryClient;
//import com.netflix.discovery.EurekaClient;
//import com.netflix.discovery.shared.Application;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.netflix.eureka.InstanceInfoFactory;
//
//import javax.xml.ws.soap.Addressing;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author honc.z
// * @date 2018/10/18
// */
//public class EurekaServiceDiscoveryImpl implements ServiceDiscovery {
//    /**
//     * 拿到eureka注册与发现的EurekaClient
//     */
//    @Autowired
//    private EurekaClient eurekaClient;
//
//    /**
//     * 服务发现
//     * @param serviceName
//     * @return
//     */
//    @Override
//    public String serviceDiscory(String serviceName) {
//        //获取所有servicename的服务实例
//        Application instance = eurekaClient.getApplications().getRegisteredApplications(serviceName);
//        List<InstanceInfo> list = instance.getInstances();
//        if (list.isEmpty()){
//            throw new RuntimeException(String.format("No service instance for %s", serviceName));
//        }
//        List<ServiceAddress> serviceList = new ArrayList<>();
//        for (InstanceInfo instanceInfo : list){
//            int port = instanceInfo.getPort();
//            String ip = instanceInfo.getIPAddr();
//            ServiceAddress serviceAddress = new ServiceAddress(ip,port);
//            serviceList.add(serviceAddress);
//        }
//        //todo 负载均衡逻辑
//        String address = loadBalance(serviceList);
//        return address;
//    }
//
//    /**
//     * 负载均衡
//     * @param list
//     * @return
//     */
//    private String loadBalance(List<ServiceAddress> list){
//        ServiceAddress serviceAddress = list.get(0);
//        return serviceAddress.getIp()+":"+serviceAddress.getPort();
//    }
//}
