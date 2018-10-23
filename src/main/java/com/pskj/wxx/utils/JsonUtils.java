package com.pskj.wxx.utils;

import java.io.IOException;  
import java.util.HashMap;  
import java.util.List;  
import java.util.Map;  
import java.util.Map.Entry;  
 
import org.apache.log4j.Logger;  
 
import com.fasterxml.jackson.core.type.TypeReference;  
import com.fasterxml.jackson.databind.JavaType;  
import com.fasterxml.jackson.databind.ObjectMapper;  
 
 
 
public class JsonUtils {  
 
    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(JsonUtils.class);  
 
    // 定义jackson对象  
    private static final ObjectMapper MAPPER = new ObjectMapper();  
 
    /**  
     * 将对象转换成json字符串。  
     *   
     * @param data  
     * @return  
     * @throws IOException  
     */  
    public static String objectToJson(Object data) {  
        try {  
            String string = MAPPER.writeValueAsString(data);  
            return string;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
 
    /**  
     * 将json结果集转化为对象  
     *   
     * @param jsonData  
     *            json数据  
     * @param clazz  
     *            对象中的object类型  
     * @return  
     */  
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {  
        try {  
            T t = MAPPER.readValue(jsonData, beanType);  
            return t;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
 
    /**  
     * 将json数据转换成pojo对象list  
     *   
     * @param jsonData  
     * @param beanType  
     * @return  
     */  
    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {  
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(  
                List.class, beanType);  
        try {  
            List<T> list = MAPPER.readValue(jsonData, javaType);  
            return list;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
 
        return null;  
    }  
 
    /**  
     * json string convert to map with javaBean  
     */  
    public static <T> Map<String, T> jsonTomap(String jsonStr, Class<T> clazz)  
            throws Exception {  
        Map<String, Map<String, Object>> map = MAPPER.readValue(jsonStr,  
                new TypeReference<Map<String, T>>() {  
                });  
        Map<String, T> result = new HashMap<String, T>();  
        for (Entry<String, Map<String, Object>> entry : map.entrySet()) {  
            result.put(entry.getKey(), mapTopojo(entry.getValue(), clazz));  
        }  
        return result;  
    }  
 
    /**  
     * json string convert to map  
     */  
    @SuppressWarnings("unchecked")
    public static <T> Map<String, Object> jsonTomap(String jsonStr) {  
        try {  
            return MAPPER.readValue(jsonStr, Map.class);  
        } catch (Exception e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return null;  
    }  
 
    /**  
     * map convert to javaBean  
     */  
    @SuppressWarnings("rawtypes")
    public static <T> T mapTopojo(Map map, Class<T> clazz) {  
 
        try {  
            return MAPPER.convertValue(map, clazz);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
 
    }  
 
}  

