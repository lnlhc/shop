package com.lhc.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lhc.shop.service.test.IthirdYearService;
import com.lhc.shop.utils.ActiveStatusConstants;
import com.lhc.shop.utils.Response;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/shop/test")    
public class TestController {
	private final Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private IthirdYearService thirdService;
	
	/**
	 * @Description: 3、查询最新50条用户中奖名单
	 * @param token
	 * @author liuhaichao
	 * @date 2017年4月13日
	 * @return
	 * @throws Exception
    */
	@ResponseBody
	@RequestMapping(value = { "/userWinnerMsg" }, method = {RequestMethod.GET, RequestMethod.POST })
	public String userWinnerMsg(@RequestParam(required = true, value = "source") String source){
		
		logger.info("查询最新50条用户中奖名单api开始"+"请求来源" +source);
		
		try {
			
			/**
			 * 1、查询最新50条中奖名单
			 */
			
			List<JSONObject> json = new ArrayList<JSONObject>();
			String prizeName = "";//奖品名称
			
			List<Map<String, Object>> list = thirdService.getUserWinnerMsg();
			logger.info("查询最新50条中奖名单list="+list);
			if (list.size() != 0) {
				for (Map<String, Object> map : list) {
					JSONObject jsons = new JSONObject();
					int type = Integer.parseInt(map.get("a_prize_type").toString());
					if (type ==1) {
						prizeName = "途虎养车券";
					}else if (type == 2 ) {
						prizeName = "宜生到家红包";
					}else if (type == 3) {
						prizeName = "加息券";
					}else if (type == 4) {
						prizeName = "5元抵现红包";
					}else if (type == 5) {
						prizeName = "20元抵现红包";
					}else if (type == 6) {
						prizeName = "充电宝";
					}else if (type == 7) {
						prizeName = "蓝牙耳机";
					}else {
						prizeName = "iphone7 Plus";
					} 
					
					jsons.put("phone",String.valueOf(map.get("phone")));
					jsons.put("prizeName", prizeName);
					json.add(jsons);
				}
			}
			
			JSONObject total = new JSONObject();
			total.put("userWinnerMsg" , json);
			logger.info("查询解析之后的记录total="+total);
			return Response.build()
					.setStatus(ActiveStatusConstants.Common.SUCCESS.status)
					.setMessage(ActiveStatusConstants.Common.SUCCESS.message)
					.setData(total).toJSON();
		} catch (Exception e) {
			logger.error("活动接口异常！" + e.getMessage());
			e.printStackTrace();
			return Response.build()
					.setStatus(ActiveStatusConstants.Common.SYSTEM_ERROE.status)
					.setMessage(ActiveStatusConstants.Common.SYSTEM_ERROE.message).
					toJSON();
		}
	}
	
	public static void main(String[] args) {
		
	}
}





