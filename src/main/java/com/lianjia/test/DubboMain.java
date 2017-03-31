package com.lianjia.test;

import java.util.concurrent.CountDownLatch;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lianjia.uc.api.user.EhrUserFacade;


public class DubboMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"spring.xml"});
		context.start();
		//EhrUserFacade obj = (EhrUserFacade)context.getBean("ehrUserFacade1");
		//Object abc = obj.getEhrUserByUserId(1000000020076002l);
		try {
			new CountDownLatch(1).await();
		} catch (InterruptedException e) {
			System.out.println("this program terminate.");
			e.printStackTrace();
		}
/*		while(true){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("here");
		}*/
	}

}
