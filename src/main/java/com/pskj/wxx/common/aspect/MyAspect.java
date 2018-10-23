package com.pskj.wxx.common.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * �Զ������� ����xml����
 * @author 533
 *
 */
public class MyAspect {

	public void before() {
		System.out.println("ǰ��֪ͨ��");
	}

	public void after() {
		System.out.println("����֪ͨ��");
	}

	public Object around(ProceedingJoinPoint pjpoint) throws Throwable {
		System.out.println("����֪ͨ����ʼ---------------------");
		Object result = null;
		
		Object[] objs = pjpoint.getArgs();
		for (Object obj : objs) {
			System.out.println("Ŀ�귽��������" + obj);
		}
		//Ŀ�귽�������б�
		Class<?>[] parameterTypes = ((MethodSignature)pjpoint.getSignature()).getMethod().getParameterTypes();//���������б�
		//Ŀ�귽��ǩ��
		String methodName = pjpoint.getSignature().getName();
		//Ŀ����
		Object target = pjpoint.getTarget();
		Method m = target.getClass().getMethod(methodName, parameterTypes);
		Annotation[] as = m.getAnnotations();
		for (Annotation annotation : as) {
			System.out.println(annotation);
		}
		result = pjpoint.proceed();
		System.out.println("����֪ͨ������---------------------");
		return result;
	}

}
