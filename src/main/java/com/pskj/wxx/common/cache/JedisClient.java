package com.pskj.wxx.common.cache;


public interface JedisClient {  
    String get(String key);  
 
    byte[] get(byte[] key);  
 
    String set(String key, String value);  
 
    String set(byte[] key, byte[] value);  
 
    String hget(String hkey, String key);  
 
    long hset(String hkey, String key, String value);  
 
    long incr(String key);  
 
    long expire(String key, int second);  
 
    long ttl(String key);  
 
    long del(String key);  
 
    long hdel(String hkey, String key);  
      
}  

