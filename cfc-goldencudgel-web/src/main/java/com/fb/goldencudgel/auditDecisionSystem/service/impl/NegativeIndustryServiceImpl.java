package com.fb.goldencudgel.auditDecisionSystem.service.impl;
import com.comwave.core.data.domain.QueryPage;
import com.comwave.core.jpa.BaseJpaDAO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
@Service
@Transactional
public class NegativeIndustryServiceImpl extends BaseJpaDAO {
    public QueryPage<Object[]> findByTypeAndName(String negativeType,String negativeName, QueryPage<Object[]> queryPage) {
        Map<String,Object> params =  new HashMap<String,Object>();
        StringBuffer sql = new StringBuffer("SELECT NEGATIVE_ID,NEGATIVE_TYPE,NEGATIVE_NAME,NEGATIVE_SCORE,CREATE_TIME FROM FB_NEGATIVE_INDUSTRIES WHERE 1=1 ");
        if (StringUtils.isNoneBlank(negativeType)){
            sql.append(" AND NEGATIVE_TYPE = :negativeType");
            params.put("negativeType", negativeType);
        }
        if (StringUtils.isNoneBlank(negativeName)){
            sql.append(" AND NEGATIVE_NAME = :negativeName");
            params.put("negativeName", negativeName);
        }
        sql.append(" ORDER BY CREATE_TIME DESC");
        QueryPage<Object[]> result = findBySQL(sql,queryPage,params);
        return  result;
    }
    
    public Map<String, String> getIndustryNameList() {
        Map<String, String> result = new LinkedHashMap<String, String>();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ITEM_CODE,ITEM_NAME");
        sql.append(" FROM VIEW_DATA_DICT_ITEM WHERE 1=1 AND DICT_ID='NEGATIVE_INDUSTRY' ");
        QueryPage<Object[]> queryPage = findBySQL(sql);
        if(queryPage != null) {
            for (Object[] ob : queryPage.getContent()) {
                String itemCode = ob[0] == null ? "" : ob[0].toString();
                String itemName = ob[1] == null ? "" : ob[1].toString();
                result.put(itemCode, itemName);
            }
        }
        return result;
    }
}
