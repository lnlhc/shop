package com.lhc.shop.service.test;

import java.util.List;
import java.util.Map;

public interface IthirdYearService {
	/**
	 * @Description: 查询最新50条中奖名单
	 * @author liuhaichao
	 * @date 2017年4月17日
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getUserWinnerMsg()throws Exception;
}
