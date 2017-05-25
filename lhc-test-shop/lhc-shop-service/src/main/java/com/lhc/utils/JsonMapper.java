package com.lhc.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

/**
 * 简单封装Jackson，实现JSON String<->Java Object的Mapper.
 * 
 * 封装不同的输出风格, 使用不同的builder函数创建实例.
 * 
 * @author calvin
 */
public class JsonMapper {
	private static Logger logger = LoggerFactory.getLogger(JsonMapper.class);
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static ObjectMapper mapper = new ObjectMapper();

	public JsonMapper() {
		this(null);
	}

	public JsonMapper(Include include) {
		mapper = new ObjectMapper();
		// 设置输出时包含属性的风格
		if (include != null) {
			mapper.setSerializationInclusion(include);
		}
		// 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	/**
	 * 创建只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的Mapper,建议在外部接口中使用.
	 */
	public static JsonMapper nonEmptyMapper() {
		return new JsonMapper(Include.NON_EMPTY);
	}

	/**
	 * 创建只输出初始值被改变的属性到Json字符串的Mapper, 最节约的存储方式，建议在内部接口中使用。
	 */
	public static JsonMapper nonDefaultMapper() {
		return new JsonMapper(Include.NON_DEFAULT);
	}

	/**
	 * Object可以是POJO，也可以是Collection或数组。
	 * 如果对象为Null, 返回"null".
	 * 如果集合为空集合, 返回"[]".
	 * 空串转换成 ""
	 */
	public String toJsonWithEmptyStr(Object object) {
//		mapper = new ObjectMapper();
		try {
		mapper.setDateFormat(dateFormat);
		mapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>(){
			@Override
			public void serialize(Object arg0, JsonGenerator arg1,
					SerializerProvider arg2) throws IOException,
					JsonProcessingException {
				arg1.writeString("");
				
			}});
		
			return mapper.writeValueAsString(object);
		} catch (IOException e) {
			logger.warn("write to json string error:" + object, e);
			return null;
		}
		
		
	}
	
	/**
	 * Object可以是POJO，也可以是Collection或数组。
	 * 如果对象为Null, 返回"null".
	 * 如果集合为空集合, 返回"[]".
	 */
	public String toJson(Object object) {

		try {
			mapper.setDateFormat(dateFormat);
			return mapper.writeValueAsString(object);
		} catch (IOException e) {
			logger.warn("write to json string error:" + object, e);
			return null;
		}
	}

	/**
	 * 反序列化POJO或简单Collection如List<String>.
	 * 
	 * 如果JSON字符串为Null或"null"字符串, 返回Null.
	 * 如果JSON字符串为"[]", 返回空集合.
	 * 
	 * 如需反序列化复杂Collection如List<MyBean>, 请使用fromJson(String, JavaType)
	 * 
	 * @see #fromJson(String, JavaType)
	 */
	public <T> T fromJson(String jsonString, Class<T> clazz) {
		if (StringUtils.isEmpty(jsonString)) {
			return null;
		}

		try {
			return mapper.readValue(jsonString, clazz);
		} catch (IOException e) {
			logger.warn("parse json string error:" + jsonString, e);
			return null;
		}
	}

	/**
	 * 反序列化复杂Collection如List<Bean>, 先使用createCollectionType()或contructMapType()构造类型, 然后调用本函数.
	 * 
	 * @see #createCollectionType(Class, Class...)
	 */
	@SuppressWarnings("all")
	public <T> T fromJson(String jsonString, JavaType javaType) {
		if (StringUtils.isEmpty(jsonString)) {
			return null;
		}

		try {
			return (T) mapper.readValue(jsonString, javaType);
		} catch (IOException e) {
			logger.warn("parse json string error:" + jsonString, e);
			return null;
		}
	}

	/**
	 * 构造Collection类型.
	 */
	@SuppressWarnings("all")
	public JavaType contructCollectionType(Class<? extends Collection> collectionClass, Class<?> elementClass) {
		return mapper.getTypeFactory().constructCollectionType(collectionClass, elementClass);
	}

	/**
	 * 构造Map类型.
	 */
	@SuppressWarnings("all")
	public JavaType contructMapType(Class<? extends Map> mapClass, Class<?> keyClass, Class<?> valueClass) {
		return mapper.getTypeFactory().constructMapType(mapClass, keyClass, valueClass);
	}

	/**
	 * 当JSON里只含有Bean的部分屬性時，更新一個已存在Bean，只覆蓋該部分的屬性.
	 */
	public void update(String jsonString, Object object) {
		try {
			mapper.readerForUpdating(object).readValue(jsonString);
		} catch (JsonProcessingException e) {
			logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
		} catch (IOException e) {
			logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
		}
	}

	/**
	 * 輸出JSONP格式數據.
	 */
	public String toJsonP(String functionName, Object object) {
		return toJson(new JSONPObject(functionName, object));
	}

	/**
	 * 設定是否使用Enum的toString函數來讀寫Enum,
	 * 為False時時使用Enum的name()函數來讀寫Enum, 默認為False.
	 * 注意本函數一定要在Mapper創建後, 所有的讀寫動作之前調用.
	 */
	public void enableEnumUseToString() {
		mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
		mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
	}

	/**
	 * 支持使用Jaxb的Annotation，使得POJO上的annotation不用与Jackson耦合。
	 * 默认会先查找jaxb的annotation，如果找不到再找jackson的。
	 */
	public void enableJaxbAnnotation() {
		JaxbAnnotationModule module = new JaxbAnnotationModule();
		mapper.registerModule(module);
	}

	/**
	 * 取出Mapper做进一步的设置或使用其他序列化API.
	 */
	public ObjectMapper getMapper() {
		return mapper;
	}
	

	   public static synchronized Map<String, Object> parseJSON2Map(String jsonStr){  
	        Map<String, Object> map = new HashMap<String, Object>();  
	        //最外层解析  
	        JSONObject json = JSONObject.fromObject(jsonStr);  
	        for(Object k : json.keySet()){  
	            Object v = json.get(k);   
	            //如果内层还是数组的话，继续解析  
	            if(v instanceof JSONArray){  
	                List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();  
	                Iterator<JSONObject> it = ((JSONArray)v).iterator();  
	                while(it.hasNext()){  
	                    JSONObject json2 = it.next();  
	                    list.add(parseJSON2Map(json2.toString()));  
	                }  
	                map.put(k.toString(), list);  
	            } else {  
	                map.put(k.toString(), v);  
	            }  
	        }  
	        return map;  
	    }
	
	   
	//+++++++++++++++++++===============新增属性过滤器=======================++++++++++++++++++++++++++++++   
	   
		/**
		 * 方法描述：多个过滤器过滤转换不需要的属性
		 * @param bean
		 * @param filterMap
		 * 			filterMap.key  要过滤的 类类型 Class<?> 
		 * 			filterMap.value  需要过滤的属性数组
		 * @return
		 * String   json串
		 */
		public String toJsonWithFilterOutAllAttributes(Object bean, final Map<Class<?>,String[]> filterMap){
//			mapper = new ObjectMapper();
			try {
				mapper.setDateFormat(dateFormat);
				SimpleFilterProvider filterProvider = new SimpleFilterProvider();
				//遍历添加所有的过滤器
				int temp =0;
				for( Iterator<Class<?>> key = filterMap.keySet().iterator(); key.hasNext();){
					Class<?> clazz = key.next();
					switch (temp) {
					case 0:
						filterProvider.addFilter(DefaultMarkInterface.MARK0.getFilterName(),SimpleBeanPropertyFilter.serializeAllExcept(filterMap.get(clazz)==null?new String[]{}:filterMap.get(clazz)));
						mapper.addMixInAnnotations(clazz, DefaultMarkInterface.MARK0.getMarkInterface());
						break;
					case 1:
						filterProvider.addFilter(DefaultMarkInterface.MARK1.getFilterName(),SimpleBeanPropertyFilter.serializeAllExcept(filterMap.get(clazz)==null?new String[]{}:filterMap.get(clazz)));
						mapper.addMixInAnnotations(clazz, DefaultMarkInterface.MARK1.getMarkInterface());
						break;
					case 2:
						filterProvider.addFilter(DefaultMarkInterface.MARK2.getFilterName(),SimpleBeanPropertyFilter.serializeAllExcept(filterMap.get(clazz)==null?new String[]{}:filterMap.get(clazz)));
						mapper.addMixInAnnotations(clazz, DefaultMarkInterface.MARK2.getMarkInterface());
						break;
					case 3:
						filterProvider.addFilter(DefaultMarkInterface.MARK3.getFilterName(),SimpleBeanPropertyFilter.serializeAllExcept(filterMap.get(clazz)==null?new String[]{}:filterMap.get(clazz)));
						mapper.addMixInAnnotations(clazz, DefaultMarkInterface.MARK3.getMarkInterface());
						break;
					case 4:
						filterProvider.addFilter(DefaultMarkInterface.MARK4.getFilterName(),SimpleBeanPropertyFilter.serializeAllExcept(filterMap.get(clazz)==null?new String[]{}:filterMap.get(clazz)));
						mapper.addMixInAnnotations(clazz, DefaultMarkInterface.MARK4.getMarkInterface());
						break;
					case 5:
						filterProvider.addFilter(DefaultMarkInterface.MARK5.getFilterName(),SimpleBeanPropertyFilter.serializeAllExcept(filterMap.get(clazz)==null?new String[]{}:filterMap.get(clazz)));
						mapper.addMixInAnnotations(clazz, DefaultMarkInterface.MARK5.getMarkInterface());
						break;
					case 6:
						filterProvider.addFilter(DefaultMarkInterface.MARK6.getFilterName(),SimpleBeanPropertyFilter.serializeAllExcept(filterMap.get(clazz)==null?new String[]{}:filterMap.get(clazz)));
						mapper.addMixInAnnotations(clazz, DefaultMarkInterface.MARK6.getMarkInterface());
						break;
					case 7:
						filterProvider.addFilter(DefaultMarkInterface.MARK7.getFilterName(),SimpleBeanPropertyFilter.serializeAllExcept(filterMap.get(clazz)==null?new String[]{}:filterMap.get(clazz)));
						mapper.addMixInAnnotations(clazz, DefaultMarkInterface.MARK7.getMarkInterface());
						break;
					case 8:
						filterProvider.addFilter(DefaultMarkInterface.MARK8.getFilterName(),SimpleBeanPropertyFilter.serializeAllExcept(filterMap.get(clazz)==null?new String[]{}:filterMap.get(clazz)));
						mapper.addMixInAnnotations(clazz, DefaultMarkInterface.MARK8.getMarkInterface());
						break;
					case 9:
						filterProvider.addFilter(DefaultMarkInterface.MARK9.getFilterName(),SimpleBeanPropertyFilter.serializeAllExcept(filterMap.get(clazz)==null?new String[]{}:filterMap.get(clazz)));
						mapper.addMixInAnnotations(clazz, DefaultMarkInterface.MARK9.getMarkInterface());
						break;
					default:
						break;
					}
					temp ++;
				}
				//设置过滤器
				mapper.setFilters(filterProvider);
				mapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>(){
					@Override
					public void serialize(Object arg0, JsonGenerator arg1,SerializerProvider arg2) throws IOException,JsonProcessingException {
						arg1.writeString("");
					}});
				return mapper.writer(filterProvider).writeValueAsString(bean);
			} catch (IOException e) {
				logger.warn("write to json string error:" + bean, e);
				return null;
			}
			
		}

		/**
		 * 方法描述： 过滤所有不需要的属性集合
		 * @param bean 需要转换的对象或者集合
		 * @param clazz   需要过滤的类类型
		 * @param filterOutAllAttributes 需要过滤的属性数组
		 * @return
		 * String
		 */
		public String toJsonWithFilterOutAllAttributes(Object bean, final Class<?> clazz,final String...filterOutAllAttributes){
//			mapper = new ObjectMapper();
			try {
			mapper.setDateFormat(dateFormat);
			FilterProvider filterProvider = new SimpleFilterProvider().addFilter(DefaultMarkInterface.MARK0.getFilterName(),SimpleBeanPropertyFilter.serializeAllExcept(filterOutAllAttributes));
//			mapper.getSerializerProvider().findNullKeySerializer(bean,  BeanProperty.class.);
			mapper.addMixInAnnotations(clazz, DefaultMarkInterface.MARK0.getMarkInterface());
			mapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>(){
				@Override
				public void serialize(Object arg0, JsonGenerator arg1,
						SerializerProvider arg2) throws IOException,
						JsonProcessingException {
					arg1.writeString("");
				}});
			return mapper.writer(filterProvider).writeValueAsString(bean);
			} catch (IOException e) {
				logger.warn("write to json string error:" + bean, e);
				return null;
			}
		}

		/**
		 * 方法描述：
		 * @param bean 需要转换的实体或集合
		 * @param clazz  需要过滤的类类型
		 * @param filterReserveAllAttributes 需要保留的实体属性
		 * @return
		 * String
		 */
		public String  toJsonWithFilterReserveAllAttributes(Object bean, final Class<?> clazz,final String...filterReserveAllAttributes){
//			mapper = new ObjectMapper();
			try {
			mapper.setDateFormat(dateFormat);
			FilterProvider filterProvider = new SimpleFilterProvider().addFilter(DefaultMarkInterface.MARK0.getFilterName(),SimpleBeanPropertyFilter.filterOutAllExcept(filterReserveAllAttributes));
			mapper.addMixInAnnotations(clazz, DefaultMarkInterface.MARK0.getMarkInterface());
			mapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>(){
				@Override
				public void serialize(Object arg0, JsonGenerator arg1,
						SerializerProvider arg2) throws IOException,
						JsonProcessingException {
					arg1.writeString("");
				}});
			return mapper.writer(filterProvider).writeValueAsString(bean);
			} catch (IOException e) {
				logger.warn("write to json string error:" + bean, e);
				return null;
			}
			
		}
		
		/**
		 * 方法描述：多个过滤器过滤转换需要保留的属性
		 * @param bean
		 * @param filterMap
		 * 			filterMap.key  需要过滤的类类型
		 * 			filterMap.value  需要保留的属性数组
		 * @return
		 * String   json串
		 */
		public String toJsonWithFilterReserveAllAttributes(Object bean, final Map<Class<?>,String[]> filterMap){
//			mapper = new ObjectMapper();
			try {
				mapper.setDateFormat(dateFormat);
				SimpleFilterProvider filterProvider = new SimpleFilterProvider();
				//遍历添加所有的过滤器
				int temp =0;
				for( Iterator<Class<?>> key = filterMap.keySet().iterator(); key.hasNext();){
					Class<?> clazz = key.next();
					switch (temp) {
					case 0:
						filterProvider.addFilter(DefaultMarkInterface.MARK0.getFilterName(),SimpleBeanPropertyFilter.filterOutAllExcept(filterMap.get(clazz)==null?new String[]{}:filterMap.get(clazz)));
						mapper.addMixInAnnotations(clazz, DefaultMarkInterface.MARK0.getMarkInterface());
						break;
					case 1:
						filterProvider.addFilter(DefaultMarkInterface.MARK1.getFilterName(),SimpleBeanPropertyFilter.filterOutAllExcept(filterMap.get(clazz)==null?new String[]{}:filterMap.get(clazz)));
						mapper.addMixInAnnotations(clazz, DefaultMarkInterface.MARK1.getMarkInterface());
						break;
					case 2:
						filterProvider.addFilter(DefaultMarkInterface.MARK2.getFilterName(),SimpleBeanPropertyFilter.filterOutAllExcept(filterMap.get(clazz)==null?new String[]{}:filterMap.get(clazz)));
						mapper.addMixInAnnotations(clazz, DefaultMarkInterface.MARK2.getMarkInterface());
						break;
					case 3:
						filterProvider.addFilter(DefaultMarkInterface.MARK3.getFilterName(),SimpleBeanPropertyFilter.filterOutAllExcept(filterMap.get(clazz)==null?new String[]{}:filterMap.get(clazz)));
						mapper.addMixInAnnotations(clazz, DefaultMarkInterface.MARK3.getMarkInterface());
						break;
					case 4:
						filterProvider.addFilter(DefaultMarkInterface.MARK4.getFilterName(),SimpleBeanPropertyFilter.filterOutAllExcept(filterMap.get(clazz)==null?new String[]{}:filterMap.get(clazz)));
						mapper.addMixInAnnotations(clazz, DefaultMarkInterface.MARK4.getMarkInterface());
						break;
					case 5:
						filterProvider.addFilter(DefaultMarkInterface.MARK5.getFilterName(),SimpleBeanPropertyFilter.filterOutAllExcept(filterMap.get(clazz)==null?new String[]{}:filterMap.get(clazz)));
						mapper.addMixInAnnotations(clazz, DefaultMarkInterface.MARK5.getMarkInterface());
						break;
					case 6:
						filterProvider.addFilter(DefaultMarkInterface.MARK6.getFilterName(),SimpleBeanPropertyFilter.filterOutAllExcept(filterMap.get(clazz)==null?new String[]{}:filterMap.get(clazz)));
						mapper.addMixInAnnotations(clazz, DefaultMarkInterface.MARK6.getMarkInterface());
						break;
					case 7:
						filterProvider.addFilter(DefaultMarkInterface.MARK7.getFilterName(),SimpleBeanPropertyFilter.filterOutAllExcept(filterMap.get(clazz)==null?new String[]{}:filterMap.get(clazz)));
						mapper.addMixInAnnotations(clazz, DefaultMarkInterface.MARK7.getMarkInterface());
						break;
					case 8:
						filterProvider.addFilter(DefaultMarkInterface.MARK8.getFilterName(),SimpleBeanPropertyFilter.filterOutAllExcept(filterMap.get(clazz)==null?new String[]{}:filterMap.get(clazz)));
						mapper.addMixInAnnotations(clazz, DefaultMarkInterface.MARK8.getMarkInterface());
						break;
					case 9:
						filterProvider.addFilter(DefaultMarkInterface.MARK9.getFilterName(),SimpleBeanPropertyFilter.filterOutAllExcept(filterMap.get(clazz)==null?new String[]{}:filterMap.get(clazz)));
						mapper.addMixInAnnotations(clazz, DefaultMarkInterface.MARK9.getMarkInterface());
						break;
					default:
						break;
					}
					temp ++;
				}
				//设置过滤器
//				mapper.setFilters(filterProvider);
				mapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>(){
					@Override
					public void serialize(Object arg0, JsonGenerator arg1,SerializerProvider arg2) throws IOException,JsonProcessingException {
						arg1.writeString("");
					}});
				return mapper.writer(filterProvider).writeValueAsString(bean);
			} catch (IOException e) {
				logger.warn("write to json string error:" + bean, e);
				return null;
			}
			
		}

}
