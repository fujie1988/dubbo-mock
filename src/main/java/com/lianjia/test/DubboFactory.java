package com.lianjia.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import com.lianjia.test.DubboServiceProxy;

public class DubboFactory<T> {

	private Class<T> interfaceClass;
	
	public DubboFactory(Class<T> interfaceClass){
		this.interfaceClass = interfaceClass;
	}
	
	public T createProxy(){
		InvocationHandler hl = new DubboServiceProxy(interfaceClass.getName());
		Class<T>[] interfaces = new Class[]{interfaceClass};
		Object proxy = Proxy.newProxyInstance(interfaceClass.getClassLoader(), interfaces, hl);
		return (T) proxy;
	}
}
