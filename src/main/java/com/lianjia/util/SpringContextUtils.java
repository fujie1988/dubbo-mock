package com.lianjia.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringContextUtils implements ApplicationContextAware {

	private static ApplicationContext applicationContext = null;

	@SuppressWarnings("static-access")
	public synchronized void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (this.applicationContext == null)
			this.applicationContext = applicationContext;
	}

	public synchronized static ApplicationContext getApplicationContext() {
		if (applicationContext == null) {
			applicationContext = new ClassPathXmlApplicationContext(new String[] { "spring.xml" });
		}
		return applicationContext;
	}

	public static Object getBean(String beanName) throws BeansException {
		return getApplicationContext().getBean(beanName);
	}

	public static <T> T getBean(String beanName, Class<T> requiredType) throws BeansException {
		return getApplicationContext().getBean(beanName, requiredType);
	}

	public static <T> T getBean(Class<T> requiredType) throws BeansException {
		return getApplicationContext().getBean(requiredType);
	}

	public static boolean containsBean(String beanName) {
		return getApplicationContext().containsBean(beanName);
	}

	public static boolean isSingleton(String beanName) throws NoSuchBeanDefinitionException {
		return getApplicationContext().isSingleton(beanName);
	}

	public static Class<?> getType(String beanName) throws NoSuchBeanDefinitionException {
		return getApplicationContext().getType(beanName);
	}

	public static String[] getAliases(String beanName) throws NoSuchBeanDefinitionException {
		return getApplicationContext().getAliases(beanName);
	}

}
