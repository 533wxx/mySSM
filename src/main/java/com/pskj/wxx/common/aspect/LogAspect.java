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
 * 	前置通知（Before Advice）：在切入点选择的连接点处的方法之前执行的通知，该通知不影响正常程序执行流程（除非该通知抛出异常，该异常将中断当前方法链的执行而返回）。

	后置通知（After Advice）：在切入点选择的连接点处的方法之后执行的通知（无论方法执行是否成功都会被调用）。
	
	后置返回通知（After returning Advice）：在切入点选择的连接点处的方法正常执行完毕时执行的通知，必须是连接点处的方法没抛出任何异常正常返回时才调用。
	
	后置异常通知（After throwing Advice）: 在切入点选择的连接点处的方法抛出异常返回时执行的通知，必须是连接点处的方法抛出任何异常返回时才调用异常通知。
	
	环绕通知（Around Advices）：环绕着在切入点选择的连接点处的方法所执行的通知，环绕通知可以在方法调用之前和之后自定义任何行为，并且可以决定是否执行连接点处的方法、替换返回值、抛出异常等等。
 */
/**
 * 使用注解，aop实现日志
 * @author 533
 *
 */
@Aspect
@Component
public class LogAspect {
	
	@Resource
	private LoggingService loggingService;
	private  static  final Logger logger = LoggerFactory.getLogger(LogAspect. class);
	
	@Before(value = "@annotation(logging)")//第一个星号是否方法的返回值 第二个星是只service的所有子包 另一个是任意方法
    public void beforeMethod(JoinPoint joinPoint, Logging logging){
        System.out.println("======================方法开始======================");
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
        logger.info(rightnow+"执行了["+methodName+"方法开始执行......******参数"+list+"******]");
        System.out.println("======================方法开始======================");
    }
    /**
     * 切面的后置方法，不管抛不抛异常都会走此方法
     * 在目标方法执行之后的通知
     * @param joinPoint
     */
    @After("execution(* com.pskj.wxx..*.service..*(..))")
    public void afterMethod(JoinPoint joinPoint){
        Object object = joinPoint.getSignature();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String rightnow=sdf.format(date);
        System.out.println(rightnow+"执行了【"+object+"方法结束......】"); 
    }
    
    /**
     * 在方法正常执行通过之后执行的通知叫做返回通知
     * 可以返回到方法的返回值 在注解后加入returning
     * @param joinPoint
     */
    @AfterReturning(value="execution(* com.pskj.wxx..*.service..*(..))",returning="result")
    public void afterReturn(JoinPoint joinPoint,Object result ){
        Object object = joinPoint.getSignature();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String rightnow=sdf.format(date);
        System.out.println(rightnow+"执行了【"+object+"方法正常执行结束......】"+"【返回结果:"+result+"】"); 
        System.out.println("√√√√√√√√√√√√√√√√√√√√√√方法结束√√√√√√√√√√√√√√√√√√√√√√");
    }
    
    /**
     * 在目标方法非正常执行完成 发生异常 抛出异常的时候会走此方法
     * 获得异常可以用throwing
     * @param joinPoint
     * @param ex
     */
    @AfterThrowing(value="execution(* com.liyi.service.*.*(..))",throwing="ex")
    public void afterThrowing(JoinPoint joinPoint,Exception ex ){
        Object object = joinPoint.getSignature();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String rightnow=sdf.format(date);
        System.out.println(rightnow+"执行了【"+object+"方法发生异常......】"+"【异常报告:"+ex+"】"); 
        System.out.println("xxxxxxxxxxxxxxxxxx方法发生异常结束xxxxxxxxxxxxxxxxxx");
    }
    /**
     * 环绕通知需要携带ProceedingJoinPoint 这个类型的参数
     * 环绕通知类似于动态代理的全过程 ProceedingJoinPoint类型的参数可以决定是否执行目标函数
     * 环绕通知必须有返回值
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
            //前置通知
            System.out.println(rightnow+"环绕通知执行了【"+classMethod+"方法开始执行......】"); 
            //执行目标方法
            result = proceedingJoinPoint.proceed(); 
            //返回通知
            System.out.println(rightnow+"环绕通知正常执行【"+classMethod+"方法完毕......】"+"【返回结果：】"+result);
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //异常通知
            System.out.println(rightnow+"环绕通知非正常执行【"+classMethod+"方法完毕，抛出异常......】"+"【返回异常：】"+e);
        }
            //后置通知
        System.out.println(rightnow+"环绕通知执行【"+classMethod+"方法完毕】");
        return result;
    }
	

}
