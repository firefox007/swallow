package com.dianping.swallow.common.internal.codec;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 简单封装Jackson，实现JSON String<->Java Object的Mapper.
 * 
 * @author wukezhu
 */
public class JsonBinder {

   private static Logger logger = LoggerFactory.getLogger(JsonBinder.class);

   private ObjectMapper  mapper;

   public JsonBinder(Include include) {
      mapper = new ObjectMapper();
      //设置输出时包含属性的风格
      mapper.setSerializationInclusion(include);
      //设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
      mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
   }

   /**
    * 创建只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的Mapper,建议在外部接口中使用.
    */
   public static JsonBinder buildBinder() {
      return new JsonBinder(Include.NON_EMPTY);
   }

   /**
    * Object可以是POJO，也可以是Collection或数组。 如果对象为Null, 返回"null". 如果集合为空集合, 返回"[]".
    */
   public String toJson(Object object) {

      try {
         return mapper.writeValueAsString(object);
      } catch (IOException e) {
         logger.warn("write to json string error:" + object, e);
         return null;
      }
   }

   /**
    * 反序列化POJO或简单Collection如List<String>. 如果JSON字符串为Null或"null"字符串, 返回Null.
    * 如果JSON字符串为"[]", 返回空集合. 如需反序列化复杂Collection如List<MyBean>,
    * 请使用fromJson(String,JavaType)
    * 
    * @see #fromJson(String, JavaType)
    */
   public <T> T fromJson(String jsonString, Class<T> clazz) {
      if (jsonString == null || "".equals(jsonString.trim())) {
         return null;
      }

      try {
         return mapper.readValue(jsonString, clazz);
      } catch (IOException e) {
         logger.warn("parse json string error:" + jsonString, e);
         return null;
      }
   }
}