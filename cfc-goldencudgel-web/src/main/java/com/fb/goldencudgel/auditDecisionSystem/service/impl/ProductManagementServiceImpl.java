package com.fb.goldencudgel.auditDecisionSystem.service.impl;
import com.comwave.core.data.domain.QueryPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.comwave.core.jpa.BaseJpaDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

@Service
@Transactional
public class ProductManagementServiceImpl  extends  BaseJpaDAO{

    private Logger logger = LoggerFactory.getLogger(ProductManagementServiceImpl.class);
   
	// 没审核权限
	public QueryPage<Object[]> findByProductName(String productName, QueryPage<Object[]> queryPage) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer(
				"SELECT PROD_NAME,MISSION_SCHEDULE,VISIT_NOTE,LETTER_CONSENT,MEASURE_WORD,APPLY_INCOM,CREDIT_REPORT,DATE_FORMAT(ESTIMATED_LAUNCH_TIME, '%Y/%m/%d %H:%i') as ESTIMATED_LAUNCH_TIME,PRODUCT_VERSION,QUESTIONNAIRE_TYPE,PROD_ID,CHECK_RESULT,AUDIT_REMARK,AUDIT_STATE FROM FB_PRODUCT WHERE 1=1 ");
		if (StringUtils.isNoneBlank(productName)) {
			sql.append(" AND PROD_ID = :productName");
			params.put("productName", productName);
		}
		queryPage = findBySQL(sql, queryPage, params);
		return queryPage;
	}
    
	// 有审核权限
	public QueryPage<Object[]> findByCheck(String productName, String setItem, String checkState,List<String> agenUserCode,
			QueryPage<Object[]> queryPage) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer(
				"SELECT PROD_NAME,SET_ITEM,DATE_FORMAT(CREATE_TIME,'%Y/%m/%d %H:%i') as CREATE_TIME,CREATE_USER,AUDIT_STATE,PRODUCT_VERSION,QUESTIONNAIRE_TYPE,DATE_FORMAT(ESTIMATED_LAUNCH_TIME, '%Y/%m/%d %H:%i') as ESTIMATED_LAUNCH_TIME,PROD_ID FROM FB_PRODUCT WHERE 1=1 ");
		if (StringUtils.isNoneBlank(productName)) {
			sql.append(" AND PROD_ID = :productName");
			params.put("productName", productName);
		}
		if (StringUtils.isNoneBlank(setItem)) {
			sql.append(" AND SET_ITEM = :setItem");
			params.put("setItem", setItem);
		}
		if (StringUtils.isNoneBlank(checkState)) {
			sql.append(" AND AUDIT_STATE = :checkState");
			params.put("checkState", checkState);
		}
		if (!agenUserCode.isEmpty()) {
			sql.append(" AND CREATE_USER in :agenUserCode");
			params.put("agenUserCode", agenUserCode);
		}
		queryPage = findBySQL(sql, queryPage, params);
		return queryPage;
	}
    
	@SuppressWarnings("unchecked")
	public Map<String, String> getQuestionNames(List<String> questionIds) {
		Map<String, String> result = new LinkedHashMap<String, String>();
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("SELECT ID,NAME FROM FB_QUESTIONNAIRE WHERE 1=1 ");
		sql.append(" AND ID IN :questionId");
		params.put("questionId", questionIds);
		List<Object[]> context = findBySQL(sql, params).getContent();
		for (Object[] ob : context) {
			String areaCode = ob[0] == null ? "" : ob[0].toString();
			String areaName = ob[1] == null ? "" : ob[1].toString();
			result.put(areaCode, areaName);
		}
		return result;
	}
    
	@SuppressWarnings("unchecked")
	public Map<String, String> getQuestionName(String questionIds) {
		Map<String, String> result = new LinkedHashMap<String, String>();
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("SELECT ID,NAME FROM FB_QUESTIONNAIRE WHERE 1=1 ");
		sql.append(" AND ID = :questionId");
		params.put("questionId", questionIds);
		List<Object[]> context = findBySQL(sql, params).getContent();
		for (Object[] ob : context) {
			String areaCode = ob[0] == null ? "" : ob[0].toString();
			String areaName = ob[1] == null ? "" : ob[1].toString();
			result.put(areaCode, areaName);
		}
		return result;
	}
	
}
