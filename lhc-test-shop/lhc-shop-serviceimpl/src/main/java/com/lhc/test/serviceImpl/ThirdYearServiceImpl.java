package com.lhc.test.serviceImpl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhc.test.dao.IthirdYearDao;
import com.lhc.test.service.IthirdYearService;

@Service
public class ThirdYearServiceImpl implements IthirdYearService,Serializable{

	@Autowired
	private IthirdYearDao thirdYearDao;
	


	public List<Map<String, Object>> getUserWinnerMsg() throws Exception {
		return thirdYearDao.getUserWinnerMsg();
	}
}
