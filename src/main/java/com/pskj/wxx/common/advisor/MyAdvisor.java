package com.pskj.wxx.common.advisor;

import java.lang.reflect.Method;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

/*
 * < aop:advisor>��������������
 */
public class MyAdvisor implements MethodBeforeAdvice,AfterReturningAdvice {

	@Override
	public void afterReturning(Object arg0, Method arg1, Object[] arg2, Object arg3) throws Throwable {
		System.out.println("ʵ�� MethodBeforeAdvice ��ǰ��֪ͨ");
	}

	@Override
	public void before(Method arg0, Object[] arg1, Object arg2) throws Throwable {
		System.out.println("ʵ�� AfterReturningAdvice �ĺ���֪ͨ");
	}

}
