package com.lhc.utils;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;


/**
 * 
 * 用于处理controller 返回值的工具类
 * 
 * @author lijw
 *
 */
public class Response {

	// private static transient JsonMapper jm = new JsonMapper();
	/**
	 * Response 消息
	 */
	private String message;

	/**
	 * Response 状态码
	 */
	private String status;

	/**
	 * Response 数据
	 */
	private Object data;

	/**
	 * 服务器当前时间
	 */
	private String date;

	/**
	 * 通话令牌
	 */
	private String token;

	private String index;
	private String total;
	private String persize;
	private String totalPages;
	private String uuid;
	private String phoneVCode;

	/**
	 * public static void main(String[] args) {
	 * 
	 * Response re = new Response(); re.setData("data").setStatus("status");
	 * 
	 * List<Response> list = new ArrayList<Response>(); list.add(re);
	 * list.add(re);
	 * 
	 * String result =
	 * 
	 * Response.build().setStatus("200").setData(new Date()).toJSON();
	 * 
	 * System.out.println(result);
	 * 
	 * }
	 */

	public static Response build() {
		return new Response();
	}

	public String toJSON() {

		StringBuffer sb = new StringBuffer();
		beforeJson(sb);
		if (this.data != null) {
			JsonMapper jm = new JsonMapper();
			sb.append("\"data\":").append(jm.toJson(data)).append(",");
		}
		endjson(sb);
		String result = sb.toString();
		if (result.lastIndexOf(",") == -1) {
			return result;
		}
		return result.substring(0, result.lastIndexOf(",")).concat(result.substring(result.lastIndexOf(",") + 1));
	}

	public String toJSON(String withEmptyStr) {
		StringBuffer sb = new StringBuffer();
		beforeJson(sb);
		if (this.data != null) {
			JsonMapper jm = new JsonMapper();
			sb.append("\"data\":").append(jm.toJsonWithEmptyStr(data)).append(",");
		}
		endjson(sb);
		String result = sb.toString();
		return result.substring(0, result.lastIndexOf(",")).concat(result.substring(result.lastIndexOf(",") + 1));

	}

	/**
	 * 方法描述：多个过滤器过滤转换需要保留的属性
	 * 
	 * @param filterMap
	 *            filterMap.key 需要过滤的类类型 filterMap.value 需要保留的属性数组
	 * @return String json串
	 */
	public String toJsonWithFilterReserveAllAttributes(final Map<Class<?>, String[]> filterMap) {
		StringBuffer sb = new StringBuffer();
		beforeJson(sb);
		if (this.data != null) {
			JsonMapper jm = new JsonMapper();
			sb.append("\"data\":").append(jm.toJsonWithFilterReserveAllAttributes(data, filterMap)).append(",");
		}
		endjson(sb);
		String result = sb.toString();
		return result.substring(0, result.lastIndexOf(",")).concat(result.substring(result.lastIndexOf(",") + 1));

	}

	/**
	 * 方法描述：
	 * 
	 * @param clazz
	 *            需要过滤的类类型
	 * @param filterReserveAllAttributes
	 *            需要保留的实体属性
	 * @return String
	 */
	public String toJsonWithFilterReserveAllAttributes(final Class<?> clazz, final String... filterReserveAllAttributes) {
		StringBuffer sb = new StringBuffer();
		beforeJson(sb);
		if (this.data != null) {
			JsonMapper jm = new JsonMapper();
			sb.append("\"data\":").append(jm.toJsonWithFilterReserveAllAttributes(data, clazz, filterReserveAllAttributes)).append(",");
		}
		endjson(sb);
		String result = sb.toString();
		return result.substring(0, result.lastIndexOf(",")).concat(result.substring(result.lastIndexOf(",") + 1));

	}

	/**
	 * 方法描述： 过滤所有不需要的属性集合
	 * 
	 * @param clazz
	 *            需要过滤的类类型
	 * @param filterOutAllAttributes
	 *            需要过滤的属性数组
	 * @return String
	 */
	public String toJsonWithFilterOutAllAttributes(final Class<?> clazz, final String... filterOutAllAttributes) {
		StringBuffer sb = new StringBuffer();
		beforeJson(sb);
		if (this.data != null) {
			JsonMapper jm = new JsonMapper();
			sb.append("\"data\":").append(jm.toJsonWithFilterOutAllAttributes(data, clazz, filterOutAllAttributes)).append(",");
		}
		endjson(sb);
		String result = sb.toString();
		return result.substring(0, result.lastIndexOf(",")).concat(result.substring(result.lastIndexOf(",") + 1));

	}

	/**
	 * 方法描述：多个过滤器过滤转换不需要的属性
	 * 
	 * @param filterMap
	 *            filterMap.key 要过滤的 类类型 Class<?> filterMap.value 需要过滤的属性数组
	 * @return String json串
	 */
	public String toJsonWithFilterOutAllAttributes(final Map<Class<?>, String[]> filterMap) {
		StringBuffer sb = new StringBuffer();
		beforeJson(sb);
		if (this.data != null) {
			JsonMapper jm = new JsonMapper();
			sb.append("\"data\":").append(jm.toJsonWithFilterOutAllAttributes(data, filterMap)).append(",");
		}
		endjson(sb);
		String result = sb.toString();
		return result.substring(0, result.lastIndexOf(",")).concat(result.substring(result.lastIndexOf(",") + 1));
	}

	private void endjson(StringBuffer sb) {
		sb.append("}");
	}

	private void beforeJson(StringBuffer sb) {
		sb.append("{");
		if (StringUtils.isNotBlank(this.status)) {
			sb.append("\"status\":\"").append(status).append("\",");
		}
		if (StringUtil.objIsNotEmpty(this.date)) {
			sb.append("\"date\":\"").append(date).append("\",");
		}
		if (StringUtils.isNotBlank(this.message)) {
			sb.append("\"message\":\"").append(message).append("\",");
		}
		if (StringUtils.isNotBlank(this.token)) {
			sb.append("\"token\":\"").append(token).append("\",");
		}
		if (StringUtils.isNotBlank(this.uuid)) {
			sb.append("\"uuid\":\"").append(uuid).append("\",");
		}
		if (StringUtils.isNotBlank(this.phoneVCode)) {
			sb.append("\"phoneVCode\":\"").append(phoneVCode).append("\",");
		}
		if (StringUtils.isNotBlank(this.index)) {
			sb.append("\"index\":\"").append(index).append("\",");
		}
		if (StringUtils.isNotBlank(this.total)) {
			sb.append("\"total\":\"").append(total).append("\",");
		}
		if (StringUtils.isNotBlank(this.persize)) {
			sb.append("\"persize\":\"").append(persize).append("\",");
		}
		if (StringUtils.isNotBlank(this.totalPages)) {
			sb.append("\"totalPages\":\"").append(totalPages).append("\",");
		}
	}

	public String getStatus() {
		return status;
	}

	public Response setStatus(String status) {
		this.status = status;
		return this;
	}

	public Object getData() {
		return data;
	}

	public Response setData(Object data) {
		this.data = data;
		return this;
	}

	public String getIndex() {
		return index;
	}

	public Response setIndex(String index) {
		this.index = index;
		return this;
	}

	public String getTotal() {
		return total;
	}

	public Response setTotal(String total) {
		this.total = total;
		return this;
	}

	public String getPersize() {
		return persize;
	}

	public Response setPersize(String persize) {
		this.persize = persize;
		return this;
	}

	public String getTotalPages() {
		return totalPages;
	}

	public Response setTotalPages(String totalPages) {
		this.totalPages = totalPages;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public Response setMessage(String message) {
		this.message = message;
		return this;
	}

	public String getToken() {
		return token;
	}

	public Response setToken(String token) {
		this.token = token;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public Response setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getPhoneVCode() {
		return phoneVCode;
	}

	public Response setPhoneVCode(String phoneVCode) {
		this.phoneVCode = phoneVCode;
		return this;
	}

	public String getDate() {
		return date;
	}

	public Response setDate(String date) {
		this.date = date;
		return this;
	}

}
