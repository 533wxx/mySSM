package com.pskj.wxx.common.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * 自定义切面 基于xml配置
 * @author 533
 *
 */
public class MyAspect {

	public void before() {
		System.out.println("前置通知！");
	}

	public void after() {
		System.out.println("后置通知！");
	}

	public Object around(ProceedingJoinPoint pjpoint) throws Throwable {
		System.out.println("环绕通知！开始---------------------");
		Object result = null;
		
		Object[] objs = pjpoint.getArgs();
		for (Object obj : objs) {
			System.out.println("目标方法参数：" + obj);
		}
		//目标方法参数列表
		Class<?>[] parameterTypes = ((MethodSignature)pjpoint.getSignature()).getMethod().getParameterTypes();//方法参数列表
		//目标方法签名
		String methodName = pjpoint.getSignature().getName();
		//目标类
		Object target = pjpoint.getTarget();
		Method m = target.getClass().getMethod(methodName, parameterTypes);
		Annotation[] as = m.getAnnotations();
		for (Annotation annotation : as) {
			System.out.println(annotation);
		}
		result = pjpoint.proceed();
		System.out.println("环绕通知！结束---------------------");
		return result;
	}

}
