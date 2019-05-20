package com.honcz.zrpc.zrpcregistry.loadbalance;

/**
 * @author hongbin
 * Created on 18/11/2017
 */
public interface LoadBalancer<T> {

	T next();
}
