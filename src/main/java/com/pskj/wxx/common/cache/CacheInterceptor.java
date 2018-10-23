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
    	System.out.println("�����]�Ў�����");
    }
    
    /**
     * ����xml���� 
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    public Object testAround(ProceedingJoinPoint joinPoint) throws Throwable {
    	//Ŀ����
    	System.out.println(joinPoint.getSignature().getDeclaringTypeName() +  "." + joinPoint.getSignature().getName());
    	System.out.println(joinPoint.getTarget().getClass());
    	Annotation[] as = joinPoint.getTarget().getClass().getAnnotations();
    	for (Annotation annotation : as) {
			System.out.println("����֪ͨ--->Ŀ�귽����ע�⣺" + annotation.getClass().getName());
		}
    	//����ָ����ע��
    	System.out.println("����֪ͨ����");
    	
    	//����jdk��̬���� <aop:config proxy-target-class="false"> Ĭ����false
    	//ʹ�÷���
    	Object result = null;//Ŀ�귽������ֵ
    	Object target = joinPoint.getTarget();
    	String methodName = joinPoint.getSignature().getName();
    	Class<?>[] parameterTypes = ((MethodSignature)joinPoint.getSignature()).getMethod().getParameterTypes();//Ŀ�귽���Ҳ��������б�
    	try {
			Method m = target.getClass().getMethod(methodName, parameterTypes);
			Annotation[] mas = m.getAnnotations();
			String value = null;
			for (Annotation annotation : mas) {
				System.out.println("����jdk��̬����");
				System.out.println(annotation);
				if(annotation instanceof RedisCache){
					RedisCache redisCache = (RedisCache) annotation;
					String cacheKey = redisCache.cacheKey();
					int exp = redisCache.expire();
					Class<?> modelType = redisCache.type();
					try {//��ȡredis�����쳣ʱ��Ϊ�˲�Ӱ���������ִ�У���Ҫtry..catch()...  
			            //���redis���Ƿ��л���  
			            value = jedisClient.hget(modelType.getName(),cacheKey);  
			            System.out.println(jedisClient.hget(modelType.getName(),cacheKey));
			        } catch (Exception e) {  
			            e.printStackTrace();  
			            System.out.println("�����������������,�����䣬����Ϣ...");  
			        } 
					if (null == value) {  
			            // ����δ����  
			            System.out.println("����δ����");  
			            // ��˲�ѯ����    
			            result = joinPoint.proceed();  
			            try {//��ȡredis�����쳣ʱ��Ϊ�˲�Ӱ���������ִ�У���Ҫtry..catch()...  
			                // ���л�������뻺��  
			                String json = serialize(result);  
			                jedisClient.hset(modelType.getName(), cacheKey, json);  
			                if(redisCache.expire()>0) {   
			                    jedisClient.expire(cacheKey, exp);//���û���ʱ��  
			                }  
			            } catch (Exception e) {  
			                e.printStackTrace();  
			                System.out.println("�����������������,�����䣬����Ϣ...");  
			            }  
			        } else {  
			            try{//������ת��ʧ�ܷ����쳣ʱ��Ϊ�˲�Ӱ���������ִ�У���Ҫtry..catch()...  
			                // int i =1/0;  
			                // �õ����������ķ���ֵ����  
			                Class<?> returnType = ((MethodSignature) joinPoint.getSignature()).getReturnType();  
			                //��json�����л�  
			                result = deserialize(value, returnType, modelType);  
			                // ��������  
			                System.out.println("��������");  
			            } catch (Exception e) {  
			                //����ת��ʧ�ܣ�����˲�ѯ����    
			                result = joinPoint.proceed();  
			                e.printStackTrace();  
			                System.out.println("��������,������ת��ʧ��...");  
			            }  
			        }
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	//Ŀ�귽��
    	//����cglib��̬���� <aop:config proxy-target-class="true">
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
    		System.out.println("����cglib��̬����");
			System.out.println(annotation);
		}*/
    	return result;
    }
 
    //ǰ���������ݿ����ݱ��  ����redis����  
    //@Before("@annotation(redisEvict)")  
    public void doBefore (JoinPoint jp,RedisEvict redisEvict){
        try{  
 
            String modelName = redisEvict.type().getName();  
            // �����Ӧ����  
            jedisClient.del(modelName);    
            System.out.println("-----------------------------------------------");
        }catch (Exception e) {  
            e.printStackTrace();  
            System.out.println("�����������������,�����䣬����Ϣ...");       
        }  
    }  
 
    // ���û��Ʒ���  
    /**
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = "@annotation(redisCache)")//���� RedisCacheע�ⷽ��
    public Object doAround(ProceedingJoinPoint pjp, RedisCache redisCache)  
            throws Throwable {  
        //�õ�ע��������  
        Class<?> modelType = redisCache.type();  
        //System.out.println(modelType.getName());  
        // ȥRedis�п�����û�����ǵ����� ����+ ���� + ������ + ����(���)  
        String cacheKey = redisCache.cacheKey();  
        System.out.println(cacheKey);  
        String value = null;  
        try {//��ȡredis�����쳣ʱ��Ϊ�˲�Ӱ���������ִ�У���Ҫtry..catch()...  
            //���redis���Ƿ��л���  
            value = jedisClient.hget(modelType.getName(),cacheKey);  
            System.out.println(jedisClient.hget(modelType.getName(),cacheKey));
        } catch (Exception e) {  
            e.printStackTrace();  
            System.out.println("�����������������,�����䣬����Ϣ...");  
        }  
        // result�Ƿ��������շ��ؽ��  
        Object result = null;  
        if (null == value) {  
            // ����δ����  
            System.out.println("����δ����");  
            // ��˲�ѯ����    
            result = pjp.proceed();  
            try {//��ȡredis�����쳣ʱ��Ϊ�˲�Ӱ���������ִ�У���Ҫtry..catch()...  
                // ���л�������뻺��  
                String json = serialize(result);  
                jedisClient.hset(modelType.getName(), cacheKey, json);  
                if(redisCache.expire()>0) {   
                    jedisClient.expire(cacheKey, redisCache.expire());//���û���ʱ��  
                }  
            } catch (Exception e) {  
                e.printStackTrace();  
                System.out.println("�����������������,�����䣬����Ϣ...");  
            }  
        } else {  
            try{//������ת��ʧ�ܷ����쳣ʱ��Ϊ�˲�Ӱ���������ִ�У���Ҫtry..catch()...  
                // int i =1/0;  
                // �õ����������ķ���ֵ����  
                Class<?> returnType = ((MethodSignature) pjp.getSignature()).getReturnType();  
                //��json�����л�  
                result = deserialize(value, returnType, modelType);  
                // ��������  
                System.out.println("��������");  
            } catch (Exception e) {  
                //����ת��ʧ�ܣ�����˲�ѯ����    
                result = pjp.proceed();  
                e.printStackTrace();  
                System.out.println("��������,������ת��ʧ��...");  
            }  
        }  
        return result;  
    }  
 
 
    protected String serialize(Object target) {  
        return JsonUtils.objectToJson(target);  
    }  
 
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Object deserialize(String jsonString, Class clazz, Class modelType) {  
        // ���л����Ӧ����List����  
        if (clazz.isAssignableFrom(List.class)) {  
            return JsonUtils.jsonToList(jsonString, modelType);  
        }  
 
        // ���л��������ͨ����  
        return JsonUtils.jsonToPojo(jsonString, clazz);  
    }  
 
 
    // ����+ ���� + ������ + ����(���) ����Key  
    public String getCacheKey(ProceedingJoinPoint pjp) {  
        StringBuffer key = new StringBuffer();  
        // ����+ ���� cn.core.serice.product.ProductServiceImpl.productList  
        String packageName = pjp.getTarget().getClass().getName();  
        key.append(packageName);  
        // ������  
        String methodName = pjp.getSignature().getName();  
        key.append(".").append(methodName);  
        // ����(���)  
        Object[] args = pjp.getArgs();  
        for (Object arg : args) {  
            // ����  
            key.append(".").append(arg.toString());  
        }  
        return key.toString();  
    }  
}  

