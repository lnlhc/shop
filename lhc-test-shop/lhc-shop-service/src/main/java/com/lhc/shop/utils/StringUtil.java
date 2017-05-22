package com.lhc.shop.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringUtil {

	public Map<String, Object> convertMap(Object bean) throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Class type = bean.getClass();
		HashMap<String, Object> map = new HashMap<String, Object>();

		BeanInfo beanInfo = Introspector.getBeanInfo(type);

		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			if (!propertyName.equals("class")) {
				Method readMethod = descriptor.getReadMethod();
				Object result = readMethod.invoke(bean, new Object[0]);
				if (result != null) {
					map.put(propertyName, result);
				} else {
					map.put(propertyName, "");
				}
			}
		}
		return map;
	}

	/**
	 * 字符串转数值相加
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public String getInttoString(String str1, String str2) {
		float t1 = Float.parseFloat(str1);
		float t2 = Float.parseFloat(str2);
		String strs = String.valueOf(t1 + t2);
		return strs;

	}

	/**
	 * 将null字符串进行处理,防止出现NullPointerException
	 * 
	 * <pre>
	 *  for:
	 *    Object  value = map.get("key");
	 *    String  tvalue = StringUtil.nullStr(value);
	 * </pre>
	 * 
	 * @param obj
	 *            对象值
	 * @return String
	 */
	public static String nullStr(Object obj) {
		return (null == obj ? "" : obj.toString().trim());
	}

	/**
	 * 将String字符串转换为int
	 * 
	 * @param obj
	 * @return
	 */
	public static int StrToInt(Object obj) {
		return (null == obj || "".equals(obj)) ? 0 : Integer.valueOf(obj.toString());
	}

	/**
	 * 检查List集合的值，不为空返回:true，为空返回:false
	 * 
	 * @param list
	 * @return
	 */
	public static <T> boolean listIsEmpty(List<T> list) {
		if (list != null && list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 检查Map集合的值，不为空返回:true，为空返回:false
	 * 
	 * @param map
	 * @return
	 */
	public static boolean mapIsEmpty(Map<?, ?> map) {
		return map == null ? false : true;
	}

	/**
	 * 检查Object对象的值，不为空返回：true，为空返回：false
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean objIsNotEmpty(Object obj) {
		return obj == null ? false : true;
	}

	/**
	 * 检查Object对象的值，不为空返回：false，为空返回：true
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean objIsEmpty(Object obj) {
		return obj == null ? true : false;
	}
}
