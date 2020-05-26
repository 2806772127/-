package com.fb.goldencudgel.auditDecisionSystem.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comwave.core.data.domain.QueryPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.comwave.core.jpa.BaseJpaDAO;
import com.fb.goldencudgel.auditDecisionSystem.domain.holiday.Holiday;
import com.fb.goldencudgel.auditDecisionSystem.repository.HolidayRepository;

@Service
@Transactional
public class HolidayServiceImpl extends BaseJpaDAO {

	@Autowired
	private HolidayRepository holidayRepository;
	
	public List<Holiday> getHolidayByYear(String year) {
		return holidayRepository.getHolidayByYear(year);
	}

	public void saveList(List<Holiday> holidays) {
		holidayRepository.saveAll(holidays);
	}


	public void deleteAll() {
		holidayRepository.deleteAllInBatch();
	}

	public QueryPage<Object[]> findAll(QueryPage<Object[]> queryPage,String holiday,String years) {
		Map<String,Object> params =  new HashMap<String,Object>();
		StringBuffer  sql = new StringBuffer("");
		sql.append("select HOLIDAY_OF_YEAR,HOLIDAY,left(IMPORT_TIME,16),REMARK from HOLIDAY ");
		sql.append(" where 1=1 ");

		if (StringUtils.isNoneBlank(holiday)){
			sql.append(" AND HOLIDAY = :holiday");
			params.put("holiday", holiday);
		}
		if (StringUtils.isNoneBlank(years)){
			sql.append(" AND HOLIDAY_OF_YEAR = :years");
			params.put("years", years);
		}
		queryPage = findBySQL(sql,queryPage,params);
		return queryPage;
	}
}
