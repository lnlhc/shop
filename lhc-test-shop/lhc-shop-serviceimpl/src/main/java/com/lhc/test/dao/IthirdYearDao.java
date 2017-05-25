package com.lhc.test.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface IthirdYearDao {

	/**
	 * @Description: 查询最新50条中奖名单
	 * @author liuhaichao
	 * @date 2017年4月17日
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getUserWinnerMsg()throws Exception;

}
