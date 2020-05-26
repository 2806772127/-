package com.fb.goldencudgel.auditDecisionSystem.service.impl;

import com.comwave.core.data.domain.QueryPage;
import com.comwave.core.jpa.BaseJpaDAO;
import com.fb.goldencudgel.auditDecisionSystem.model.User;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class RatingConfigServiceImpI extends BaseJpaDAO {

    public QueryPage<Object[]> findByConditions(String rating, String startDate, String endDate
            , QueryPage<Object[]> queryPage, String startDates, String endDates) {
        // 查选用户角色、所在组、所在区域信息User user = UserUtil.getCurrUser();

        //查询
        Map<String,Object> params =  new HashMap<String,Object>();
        StringBuffer sql = new StringBuffer("SELECT CONFIG_ID ");
        sql.append(" ,RATING,RATING_DESCRIBE,DATE_FORMAT(CREATE_TIME,'%Y-%m-%d') CREATE_TIME ");
        sql.append(" ,DATE_FORMAT(UPDATE_TIME,'%Y-%m-%d') UPDATE_TIME ");
        sql.append(" FROM COMPANY_RATING_CONFIG ");
        sql.append(" WHERE 1=1 ");

        if (StringUtils.isNoneBlank(rating)){
            sql.append(" AND RATING=:rating");
            params.put("rating",rating);
        }

        if (StringUtils.isNoneBlank(startDate)){
            sql.append(" AND Date(CREATE_TIME) >= :startDate");
            params.put("startDate",startDate);
        }
        /*  if (null != endDate ){*/
        if (StringUtils.isNoneBlank(endDate)){
            sql.append(" AND Date(CREATE_TIME) <= :endDate");
            params.put("endDate",endDate);
        }

        if (StringUtils.isNoneBlank(startDates)){
            sql.append(" AND Date(UPDATE_TIME) >= :startDates");
            params.put("startDates",startDates);
        }
        /*  if (null != endDate ){*/
        if (StringUtils.isNoneBlank(endDates)){
            sql.append(" AND Date(UPDATE_TIME) <= :endDates");
            params.put("endDates",endDates);
        }

        sql.append(" ORDER BY RATING ");
        queryPage = findBySQL(sql,queryPage,params);

        return  queryPage;
    }


    public QueryPage<Object[]> findById(String configid,QueryPage<Object[]> queryPage) {
        //查询
        Map<String,Object> params =  new HashMap<String,Object>();
        StringBuffer sql = new StringBuffer("SELECT * FROM COMPANY_RATING_CONFIG where CONFIG_ID=:configid");
        params.put("configid",configid);
        queryPage= findBySQL(sql,queryPage,params);
        return queryPage;
    }


}
