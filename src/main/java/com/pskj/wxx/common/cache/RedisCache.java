package com.pskj.wxx.common.cache;


import java.lang.annotation.Documented;  
import java.lang.annotation.ElementType;  
import java.lang.annotation.Retention;  
import java.lang.annotation.RetentionPolicy;  
import java.lang.annotation.Target;  
 
@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.METHOD)  
@Documented  
public @interface RedisCache {  
      
    @SuppressWarnings("rawtypes")
    Class type();  
    public int expire() default 0;      //���������,Ĭ��������    
    public String cacheKey() default "";
}
 
 
 

