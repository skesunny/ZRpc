package com.honcz.zrpc.zrpccore.servicehandler;

import com.honcz.zrpc.zrpccore.annotation.EnableRPCClients;
import com.honcz.zrpc.zrpccommon.annotation.ZRpcService;
import com.honcz.zrpc.zrpcregistry.ServiceDiscovery;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 将提供服务的interface接口作为bean注入到spring bean容器
 * 整体思路：
 * 1.通过启动类上@RPCclients注解中的value属性，找到引用api的路径。
 * 2.通过api路径，找到api包中，所有用了@RPCservice注解的接口。
 * 3.拿到这些接口注入到bean工厂中的元数据metadata，拿到它们的classname。
 * 4.动态生成ProxyFactoryBean,将classname和一个ServiceDiscovery实例交给ProxyFactoryBean进行代理。
 *
 * 解释：BeanDefinitionRegistryPostProcessor：
 *
 *
 * @author honc.z
 * @date 2018/10/18
 */
@Slf4j
@RequiredArgsConstructor
public class ServiceBeanDefinitionHandler implements BeanDefinitionRegistryPostProcessor {
    @NonNull
    private ServiceDiscovery serviceDiscovery;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        log.info("开始注入bean");
        //拿到类目录bean扫描器
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        //增加一个注解过滤器
        scanner.addIncludeFilter(new AnnotationTypeFilter(ZRpcService.class));
        //告诉bean加载器扫描注解的位置
        for (String apiPackages : getApiPackages()){
            //拿到路径下申请组件者的Bean set集合
            Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(apiPackages);
            for (BeanDefinition candidateComponent : candidateComponents){
                if (candidateComponent instanceof AnnotatedBeanDefinition){
                    AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                    AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
                    BeanDefinitionHolder holder = createBeanDefinition(annotationMetadata);
                    BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
                }
        }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    /**
     * 将引用包路径放入一个无序不重复的set
     * @return
     */
    private Set<String> getApiPackages() {
        //@EnableRPCClients注解的value路径数组
        String[] basePackages = getMainClass().getAnnotation(EnableRPCClients.class).basePackages();
        Set set = new HashSet<>();
        Collections.addAll(set, basePackages);
        return set;
    }

    /**
     * 通过反射拿到系统变量中的mainclass
     * @return
     */
    private Class<?> getMainClass(){
        if (null != System.getProperty("sun.java.command")) {
            String mainClass = System.getProperty("sun.java.command");
            log.debug("Main class: {}", mainClass);
            try {
                return Class.forName(mainClass);
            }catch (ClassNotFoundException e){
                throw new IllegalStateException("Cannot determine main class.");
            }
        }
        throw new IllegalStateException("Cannot determine main class.");
    }

    private BeanDefinitionHolder createBeanDefinition(AnnotationMetadata annotationMetadata) {
        String className = annotationMetadata.getClassName();
        log.info("Creating bean definition for class: {}", className);

        BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(ProxyFactoryBean.class);
        String beanName = StringUtils.uncapitalize(className.substring(className.lastIndexOf('.') + 1));
        definition.addPropertyValue("type", className);
        definition.addPropertyValue("serviceDiscovery", serviceDiscovery);

        return new BeanDefinitionHolder(definition.getBeanDefinition(), beanName);
    }
}
