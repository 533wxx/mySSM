package com.pskj.wxx.common.aspect;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.pskj.wxx.common.annotation.Logging;
import com.pskj.wxx.logging.beans.SysLog;
import com.pskj.wxx.logging.service.LoggingService;
/*
 * 	ǰ��֪ͨ��Before Advice�����������ѡ������ӵ㴦�ķ���֮ǰִ�е�֪ͨ����֪ͨ��Ӱ����������ִ�����̣����Ǹ�֪ͨ�׳��쳣�����쳣���жϵ�ǰ��������ִ�ж����أ���

	����֪ͨ��After Advice�����������ѡ������ӵ㴦�ķ���֮��ִ�е�֪ͨ�����۷���ִ���Ƿ�ɹ����ᱻ���ã���
	
	���÷���֪ͨ��After returning Advice�����������ѡ������ӵ㴦�ķ�������ִ�����ʱִ�е�֪ͨ�����������ӵ㴦�ķ���û�׳��κ��쳣��������ʱ�ŵ��á�
	
	�����쳣֪ͨ��After throwing Advice��: �������ѡ������ӵ㴦�ķ����׳��쳣����ʱִ�е�֪ͨ�����������ӵ㴦�ķ����׳��κ��쳣����ʱ�ŵ����쳣֪ͨ��
	
	����֪ͨ��Around Advices�����������������ѡ������ӵ㴦�ķ�����ִ�е�֪ͨ������֪ͨ�����ڷ�������֮ǰ��֮���Զ����κ���Ϊ�����ҿ��Ծ����Ƿ�ִ�����ӵ㴦�ķ������滻����ֵ���׳��쳣�ȵȡ�
 */
/**
 * ʹ��ע�⣬aopʵ����־
 * @author 533
 *
 */
@Aspect
@Component
public class LogAspect {
	
	@Resource
	private LoggingService loggingService;
	private  static  final Logger logger = LoggerFactory.getLogger(LogAspect. class);
	
	@Before(value = "@annotation(logging)")//��һ���Ǻ��Ƿ񷽷��ķ���ֵ �ڶ�������ֻservice�������Ӱ� ��һ�������ⷽ��
    public void beforeMethod(JoinPoint joinPoint, Logging logging){
        System.out.println("======================������ʼ======================");
        String methodName = joinPoint.getSignature().getName();
        List<Object> list = Arrays.asList(joinPoint.getArgs());
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String rightnow=sdf.format(date);
        SysLog log = new SysLog();
        log.setModelName(logging.modelName());
        log.setCreateDate(date);
        log.setOperation(logging.operation());
        log.setHandler("admin");
        loggingService.addLogging(log);
        logger.info(rightnow+"ִ����["+methodName+"������ʼִ��......******����"+list+"******]");
        System.out.println("======================������ʼ======================");
    }
    /**
     * ����ĺ��÷����������ײ����쳣�����ߴ˷���
     * ��Ŀ�귽��ִ��֮���֪ͨ
     * @param joinPoint
     */
    @After("execution(* com.pskj.wxx..*.service..*(..))")
    public void afterMethod(JoinPoint joinPoint){
        Object object = joinPoint.getSignature();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String rightnow=sdf.format(date);
        System.out.println(rightnow+"ִ���ˡ�"+object+"��������......��"); 
    }
    
    /**
     * �ڷ�������ִ��ͨ��֮��ִ�е�֪ͨ��������֪ͨ
     * ���Է��ص������ķ���ֵ ��ע������returning
     * @param joinPoint
     */
    @AfterReturning(value="execution(* com.pskj.wxx..*.service..*(..))",returning="result")
    public void afterReturn(JoinPoint joinPoint,Object result ){
        Object object = joinPoint.getSignature();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String rightnow=sdf.format(date);
        System.out.println(rightnow+"ִ���ˡ�"+object+"��������ִ�н���......��"+"�����ؽ��:"+result+"��"); 
        System.out.println("�̷̡̡̡̡̡̡̡̡̡̡̡̡̡̡̡̡̡̡̡̡̡��������̡̡̡̡̡̡̡̡̡̡̡̡̡̡̡̡̡̡̡̡̡�");
    }
    
    /**
     * ��Ŀ�귽��������ִ����� �����쳣 �׳��쳣��ʱ����ߴ˷���
     * ����쳣������throwing
     * @param joinPoint
     * @param ex
     */
    @AfterThrowing(value="execution(* com.liyi.service.*.*(..))",throwing="ex")
    public void afterThrowing(JoinPoint joinPoint,Exception ex ){
        Object object = joinPoint.getSignature();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String rightnow=sdf.format(date);
        System.out.println(rightnow+"ִ���ˡ�"+object+"���������쳣......��"+"���쳣����:"+ex+"��"); 
        System.out.println("xxxxxxxxxxxxxxxxxx���������쳣����xxxxxxxxxxxxxxxxxx");
    }
    /**
     * ����֪ͨ��ҪЯ��ProceedingJoinPoint ������͵Ĳ���
     * ����֪ͨ�����ڶ�̬�����ȫ���� ProceedingJoinPoint���͵Ĳ������Ծ����Ƿ�ִ��Ŀ�꺯��
     * ����֪ͨ�����з���ֵ
     * @param proceedingJoinPoint
     * @return
     */
    @Around(value="execution(* com.pskj.wxx..*.service..*(..))")
    public Object aroundMethod(ProceedingJoinPoint proceedingJoinPoint){
        Object result=null;
        Object classMethod=proceedingJoinPoint.getSignature();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String rightnow=sdf.format(date);
        try {
            //ǰ��֪ͨ
            System.out.println(rightnow+"����ִ֪ͨ���ˡ�"+classMethod+"������ʼִ��......��"); 
            //ִ��Ŀ�귽��
            result = proceedingJoinPoint.proceed(); 
            //����֪ͨ
            System.out.println(rightnow+"����֪ͨ����ִ�С�"+classMethod+"�������......��"+"�����ؽ������"+result);
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //�쳣֪ͨ
            System.out.println(rightnow+"����֪ͨ������ִ�С�"+classMethod+"������ϣ��׳��쳣......��"+"�������쳣����"+e);
        }
            //����֪ͨ
        System.out.println(rightnow+"����ִ֪ͨ�С�"+classMethod+"������ϡ�");
        return result;
    }
	

}
