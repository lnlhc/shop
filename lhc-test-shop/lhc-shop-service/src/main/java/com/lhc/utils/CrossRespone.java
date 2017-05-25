package com.lhc.utils;

import javax.servlet.http.HttpServletRequest;

/**
 *	json杞垚jsonp(瑙ｅ喅璺ㄥ煙闂)
 * @author 浜庢柉娲�
 * @Description: 
 * @Author: evan
 * @Version: V1.00 
 * @Create Date: 2017骞�4鏈�21鏃ヤ笅鍗�2:00:23
 * @Parameters:
 */
public class CrossRespone {
	//寰綉绔欒姹傝法鍩熻浆鎴恓sonp
	public static String jsonpCallBack(String response,String source,HttpServletRequest request){ 
		if("WX".equals(source)){//寰綉绔� 
			String jsonpCallback = request.getParameter("jsonpCallback"); 
			return jsonpCallback + "(" +response+ ")"; 
		}else{ 
			return response; 
			} 
		}

}
