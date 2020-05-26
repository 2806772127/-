package com.fb.goldencudgel.auditDecisionSystem.service.impl;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.comwave.core.data.domain.QueryPage;
import com.comwave.core.jpa.BaseJpaDAO;

@Service
@Transactional
public class AuditConfigurationServiceImpl extends BaseJpaDAO{
	
	@SuppressWarnings("unchecked")
	public QueryPage<Object[]> findByConditions(String productCode,String agentUserCode,String agentUserName,String approveUserCode,String approveUserName,
			QueryPage<Object[]> queryPage) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT FAC.APPROVE_ID,FAC.FUNCTION_CODE,FAC.FUNCTION_NAME,");
		sb.append("       FAC.AGENT_USER_CODE,FAC.AGENT_USER_NAME,FAC.APPROVE_USER_CODE,");
		sb.append("       FAC.APPROVE_USER_NAME,FAC.AGENT_USER_AREA,FAC.AGENT_USER_GROUP,");
		sb.append("       FAC.APPROVE_USER_AREA,FAC.APPROVE_USER_GROUP ");
		sb.append("  FROM FB_APPROVE_CONFIG FAC ");
		sb.append(" WHERE 1=1 ");
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtils.isNoneBlank(productCode)) {
			sb.append(" AND FAC.FUNCTION_CODE=:productCode");
			params.put("productCode", productCode);
		}
		if(StringUtils.isNoneBlank(agentUserCode)) {
			sb.append(" AND FAC.AGENT_USER_CODE=:agentUserCode");
			params.put("agentUserCode", agentUserCode);
		}
		if(StringUtils.isNoneBlank(agentUserName)) {
			sb.append(" AND FAC.AGENT_USER_NAME like '%"+agentUserName+"%'");
		}
		if(StringUtils.isNoneBlank(approveUserCode)) {
			sb.append(" AND FAC.APPROVE_USER_CODE=:approveUserCode");
			params.put("approveUserCode", approveUserCode);
		}
		if(StringUtils.isNoneBlank(approveUserName)) {
			sb.append(" AND FAC.APPROVE_USER_NAME like '%"+approveUserName+"%'");
		}
		sb.append("  ORDER BY FAC.CREATE_TIME desc ");
		queryPage = findBySQL(sb, queryPage, params);
		return queryPage;
	}
	
	 
    public Map<String, String> getProductFunction() {
        Map<String, String> result = new LinkedHashMap<String, String>();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ITEM_CODE,ITEM_NAME FROM VIEW_DATA_DICT_ITEM a WHERE a.DICT_ID='PRODUCT_FUNCTION' ORDER BY a.ITEM_CODE ");
        List<Object[]> context = findBySQL(sql).getContent();
        for(Object[] ob : context) {
            String itemCode = ob[0] == null ? "" : ob[0].toString();
            String itemName = ob[1] == null ? "" : ob[1].toString();
            result.put(itemCode,itemName);
        }
        return result;
    }
}
