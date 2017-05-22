package com.lhc.shop.service.test.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhc.shop.dao.test.IthirdYearDao;
import com.lhc.shop.service.test.IthirdYearService;
@Service
public class ThirdYearServiceImpl implements IthirdYearService{

	@Autowired
	private IthirdYearDao thirdYearDao;
	


	public List<Map<String, Object>> getUserWinnerMsg() throws Exception {
		return thirdYearDao.getUserWinnerMsg();
	}
}
