package com.pskj.wxx.common.cache;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pskj.wxx.utils.JsonUtils;  
 
 
@Aspect  
@Component  
public class CacheInterceptor {  
 
    @Autowired  
    JedisClient jedisClient;
    
    public void setJedisClient(JedisClient jedisClient) {
		this.jedisClient = jedisClient;
	}
    
    @Pointcut("execution(* com.pskj.wxx..*.service..*(..))")
    public void pointCut(){}
    
    //@Before("pointCut()")
    public void testBefore(JoinPoint jp){
    	System.out.println(jp.getSignature().getDeclaringTypeName() +  "." + jp.getSignature().getName());
    	System.out.println(jp.getTarget().getClass());
    	System.out.println(jp.getTarget().getClass().getName());
    	Annotation[] as = jp.getTarget().getClass().getAnnotations();
    	for (Annotation annotation : as) {
			System.out.println(annotation.getClass().getName());
		}
    	System.out.println("方法沒有帶參數");
    }
    
    /**
     * 基于xml配置 
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    public Object testAround(ProceedingJoinPoint joinPoint) throws Throwable {
    	//目标类
    	System.out.println(joinPoint.getSignature().getDeclaringTypeName() +  "." + joinPoint.getSignature().getName());
    	System.out.println(joinPoint.getTarget().getClass());
    	Annotation[] as = joinPoint.getTarget().getClass().getAnnotations();
    	for (Annotation annotation : as) {
			System.out.println("环绕通知--->目标方法的注解：" + annotation.getClass().getName());
		}
    	//拦截指定的注解
    	System.out.println("环绕通知！！");
    	
    	//基于jdk动态代理 <aop:config proxy-target-class="false"> 默认是false
    	//使用反射
    	Object result = null;//目标方法返回值
    	Object target = joinPoint.getTarget();
    	String methodName = joinPoint.getSignature().getName();
    	Class<?>[] parameterTypes = ((MethodSignature)joinPoint.getSignature()).getMethod().getParameterTypes();//目标方法惨参数类型列表
    	try {
			Method m = target.getClass().getMethod(methodName, parameterTypes);
			Annotation[] mas = m.getAnnotations();
			String value = null;
			for (Annotation annotation : mas) {
				System.out.println("基于jdk动态代理");
				System.out.println(annotation);
				if(annotation instanceof RedisCache){
					RedisCache redisCache = (RedisCache) annotation;
					String cacheKey = redisCache.cacheKey();
					int exp = redisCache.expire();
					Class<?> modelType = redisCache.type();
					try {//当取redis发生异常时，为了不影响程序正常执行，需要try..catch()...  
			            //检查redis中是否有缓存  
			            value = jedisClient.hget(modelType.getName(),cacheKey);  
			            System.out.println(jedisClient.hget(modelType.getName(),cacheKey));
			        } catch (Exception e) {  
			            e.printStackTrace();  
			            System.out.println("缓存服务器出现问题,发邮箱，发信息...");  
			        } 
					if (null == value) {  
			            // 缓存未命中  
			            System.out.println("缓存未命中");  
			            // 后端查询数据    
			            result = joinPoint.proceed();  
			            try {//当取redis发生异常时，为了不影响程序正常执行，需要try..catch()...  
			                // 序列化结果放入缓存  
			                String json = serialize(result);  
			                jedisClient.hset(modelType.getName(), cacheKey, json);  
			                if(redisCache.expire()>0) {   
			                    jedisClient.expire(cacheKey, exp);//设置缓存时间  
			                }  
			            } catch (Exception e) {  
			                e.printStackTrace();  
			                System.out.println("缓存服务器出现问题,发邮箱，发信息...");  
			            }  
			        } else {  
			            try{//当数据转换失败发生异常时，为了不影响程序正常执行，需要try..catch()...  
			                // int i =1/0;  
			                // 得到被代理方法的返回值类型  
			                Class<?> returnType = ((MethodSignature) joinPoint.getSignature()).getReturnType();  
			                //把json反序列化  
			                result = deserialize(value, returnType, modelType);  
			                // 缓存命中  
			                System.out.println("缓存命中");  
			            } catch (Exception e) {  
			                //数据转换失败，到后端查询数据    
			                result = joinPoint.proceed();  
			                e.printStackTrace();  
			                System.out.println("缓存命中,但数据转换失败...");  
			            }  
			        }
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	//目标方法
    	//基于cglib动态代理 <aop:config proxy-target-class="true">
    	/*Signature signature = joinPoint.getSignature();    
    	MethodSignature methodSignature = (MethodSignature)signature;    
    	Method targetMethod = methodSignature.getMethod();
    	
    	System.out.println("classname:" + targetMethod.getDeclaringClass().getName());    
    	System.out.println("superclass:" + targetMethod.getDeclaringClass().getSuperclass().getName());    
    	System.out.println("isinterface:" + targetMethod.getDeclaringClass().isInterface());    
    	System.out.println("target:" + joinPoint.getTarget().getClass().getName());    
    	System.out.println("proxy:" + joinPoint.getThis().getClass().getName());    
    	System.out.println("method:" + targetMethod.getName()); 
    	
    	Annotation[] as1 = targetMethod.getAnnotations();
    	for (Annotation annotation : as1) {
    		System.out.println("基于cglib动态代理");
			System.out.println(annotation);
		}*/
    	return result;
    }
 
    //前置由于数据库数据变更  清理redis缓存  
    //@Before("@annotation(redisEvict)")  
    public void doBefore (JoinPoint jp,RedisEvict redisEvict){
        try{  
 
            String modelName = redisEvict.type().getName();  
            // 清除对应缓存  
            jedisClient.del(modelName);    
            System.out.println("-----------------------------------------------");
        }catch (Exception e) {  
            e.printStackTrace();  
            System.out.println("缓存服务器出现问题,发邮箱，发信息...");       
        }  
    }  
 
    // 配置环绕方法  
    /**
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = "@annotation(redisCache)")//拦截 RedisCache注解方法
    public Object doAround(ProceedingJoinPoint pjp, RedisCache redisCache)  
            throws Throwable {  
        //得到注解上类型  
        Class<?> modelType = redisCache.type();  
        //System.out.println(modelType.getName());  
        // 去Redis中看看有没有我们的数据 包名+ 类名 + 方法名 + 参数(多个)  
        String cacheKey = redisCache.cacheKey();  
        System.out.println(cacheKey);  
        String value = null;  
        try {//当取redis发生异常时，为了不影响程序正常执行，需要try..catch()...  
            //检查redis中是否有缓存  
            value = jedisClient.hget(modelType.getName(),cacheKey);  
            System.out.println(jedisClient.hget(modelType.getName(),cacheKey));
        } catch (Exception e) {  
            e.printStackTrace();  
            System.out.println("缓存服务器出现问题,发邮箱，发信息...");  
        }  
        // result是方法的最终返回结果  
        Object result = null;  
        if (null == value) {  
            // 缓存未命中  
            System.out.println("缓存未命中");  
            // 后端查询数据    
            result = pjp.proceed();  
            try {//当取redis发生异常时，为了不影响程序正常执行，需要try..catch()...  
                // 序列化结果放入缓存  
                String json = serialize(result);  
                jedisClient.hset(modelType.getName(), cacheKey, json);  
                if(redisCache.expire()>0) {   
                    jedisClient.expire(cacheKey, redisCache.expire());//设置缓存时间  
                }  
            } catch (Exception e) {  
                e.printStackTrace();  
                System.out.println("缓存服务器出现问题,发邮箱，发信息...");  
            }  
        } else {  
            try{//当数据转换失败发生异常时，为了不影响程序正常执行，需要try..catch()...  
                // int i =1/0;  
                // 得到被代理方法的返回值类型  
                Class<?> returnType = ((MethodSignature) pjp.getSignature()).getReturnType();  
                //把json反序列化  
                result = deserialize(value, returnType, modelType);  
                // 缓存命中  
                System.out.println("缓存命中");  
            } catch (Exception e) {  
                //数据转换失败，到后端查询数据    
                result = pjp.proceed();  
                e.printStackTrace();  
                System.out.println("缓存命中,但数据转换失败...");  
            }  
        }  
        return result;  
    }  
 
 
    protected String serialize(Object target) {  
        return JsonUtils.objectToJson(target);  
    }  
 
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Object deserialize(String jsonString, Class clazz, Class modelType) {  
        // 序列化结果应该是List对象  
        if (clazz.isAssignableFrom(List.class)) {  
            return JsonUtils.jsonToList(jsonString, modelType);  
        }  
 
        // 序列化结果是普通对象  
        return JsonUtils.jsonToPojo(jsonString, clazz);  
    }  
 
 
    // 包名+ 类名 + 方法名 + 参数(多个) 生成Key  
    public String getCacheKey(ProceedingJoinPoint pjp) {  
        StringBuffer key = new StringBuffer();  
        // 包名+ 类名 cn.core.serice.product.ProductServiceImpl.productList  
        String packageName = pjp.getTarget().getClass().getName();  
        key.append(packageName);  
        // 方法名  
        String methodName = pjp.getSignature().getName();  
        key.append(".").append(methodName);  
        // 参数(多个)  
        Object[] args = pjp.getArgs();  
        for (Object arg : args) {  
            // 参数  
            key.append(".").append(arg.toString());  
        }  
        return key.toString();  
    }  
}  

