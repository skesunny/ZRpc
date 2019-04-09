# ZRpc
来自于honc.z的RPC框架

一.框架整体设计方案和步骤。
1.启动。
1）客户端：检查启动类是否使用@EnableZRpcClients注解,如果是，扫描注解value路径下的类（这个路径就是api的路径）。
2）API包：扫描路径下使用了@ZRpcService注解的接口，将这些接口注入到当前springcontext上下文中的容器中，便于@Autowired使用。
3）服务端：扫描使用了@ZrpcService注解的接口，通过注册中心提供的接口，将这些类注册到注册中心去。

2.调用。
1）客户端：需要用到代理工厂。注意：此处为框架核心。
    对于扫描Api包中的所有使用@ZRpcService注解的类，加入代理工厂。当这些类的方法被使用时，会被移交给代理工厂中的doInvoke方法；
    doInvoke:拿到这次调用的类的class和方法名，作为参数调用注册中心的discovery接口，拿到server的ip，
    然后通过netty进行序列化调用。
    代理工厂：通过实现FactoryBean接口，可以定制类实例化逻辑（调用@Autowired下的类，实际上spring容器帮我们做了实例化，
    我们可以自己来定制这个实例化），这也是为什么一定要实现BeanDefinitionRegistryPostProcessor这个接口，定制化类注入方式的
    关键原因。
