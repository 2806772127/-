package com.fb.goldencudgel.auditDecisionSystem.service.impl;

import com.comwave.core.data.domain.QueryPage;
import com.comwave.core.jpa.BaseJpaDAO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class FbTargetConfigServiceImpl extends BaseJpaDAO {

    /**
     *
     * @param queryPage
     * @return
     */
    public QueryPage<Object[]> findByIds(String dictId, String areaId,
                                         String groupId,String userId,String mon,String targetId, QueryPage<Object[]> queryPage) {

        Map<String,Object> params =  new HashMap<String,Object>();
        StringBuffer  sql = new StringBuffer("");
        if(dictId.equals("1") && targetId.equals("1")) {
            //企業貸款進件數
            sql.append("SELECT  IFNULL(A.TARGET_USER,''),'進件數' TARS,IFNULL(A.COM_LOAN_ACOUNT,''),IFNULL(A.TARGET_CYCLE,''),IFNULL(DATE_FORMAT(A.OPERATION_TIME,'%Y-%m-%d'),'') FROM FB_TARGET_CONFIG A   ");
        }else if(dictId.equals("1") && targetId.equals("2")) {
            //企業貸款業績數
            sql.append("SELECT  IFNULL(A.TARGET_USER,''),'業績數' TARS,IFNULL(A.COM_APPROVE_AMT,''),IFNULL(A.TARGET_CYCLE,''),IFNULL(DATE_FORMAT(A.OPERATION_TIME,'%Y-%m-%d'),'') FROM FB_TARGET_CONFIG A  ");
        }else if(dictId.equals("2") && targetId.equals("1")) {
            //企業主房貸進件數
            sql.append("SELECT  IFNULL(A.TARGET_USER,''),'進件數' TARS,IFNULL(A.HOUSE_LOAN_ACOUNT,''),IFNULL(A.TARGET_CYCLE,''),IFNULL(DATE_FORMAT(A.OPERATION_TIME,'%Y-%m-%d'),'') FROM FB_TARGET_CONFIG A   ");
        }else if(dictId.equals("2") && targetId.equals("2")) {
            //企業主房貸業績數
            sql.append("SELECT  IFNULL(A.TARGET_USER,''),'業績數' TARS,IFNULL(A.HOUSE_APPROVE_AMT,''),IFNULL(A.TARGET_CYCLE,''),IFNULL(DATE_FORMAT(A.OPERATION_TIME,'%Y-%m-%d'),'') FROM FB_TARGET_CONFIG A  ");
        }else if(dictId.equals("3") && targetId.equals("1")) {
            //企業主信貸進件數
            sql.append("SELECT  IFNULL(A.TARGET_USER,''),'進件數' TARS,IFNULL(A.CREDIT_LOAN_ACOUNT,''),IFNULL(A.TARGET_CYCLE,''),IFNULL(DATE_FORMAT(A.OPERATION_TIME,'%Y-%m-%d'),'') FROM FB_TARGET_CONFIG  A  ");
        }else if(dictId.equals("3") && targetId.equals("2")) {
            //企業主信貸業績數
            sql.append("SELECT  IFNULL(A.TARGET_USER,''),'業績數' TARS,IFNULL(A.CREDIT_APPROVE_AMT,''),IFNULL(A.TARGET_CYCLE,''),IFNULL(DATE_FORMAT(A.OPERATION_TIME,'%Y-%m-%d'),'') FROM FB_TARGET_CONFIG A   ");
        }
        if(areaId.equals("allarea") && groupId.equals("allgroup") && userId.equals("alluser")){

            if( !mon.equals("allmon")){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
                Date date = new Date();
                String dateStr = simpleDateFormat.format(date);
                sql.append(" WHERE  A.TARGET_CYCLE = :mon");
                if(!mon.equals("10") && !mon.equals("11") && !mon.equals("12")){
                    params.put("mon", dateStr+"0"+mon);
                }else {
                    params.put("mon", dateStr+mon);
                }
            }

        }else{
            sql.append(" ,FB_USER B WHERE 1=1 AND A.TARGET_USER_CODE = B.USER_CODE  ");
            if( !areaId.equals("allarea")){
                sql.append(" AND B.USER_AREA = :areaId");
                params.put("areaId", areaId);
            }
            if( !groupId.equals("allgroup")){
                sql.append(" AND B.USER_GROUP = :groupId");
                params.put("groupId", groupId);
            }
            if( !userId.equals("alluser")){
                sql.append(" AND B.USER_CODE = :userId");
                params.put("userId", userId);
            }
            if( !mon.equals("allmon")){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
                Date date = new Date();
                String dateStr = simpleDateFormat.format(date);
                sql.append(" AND  A.TARGET_CYCLE = :mon");
                if(!mon.equals("10") && !mon.equals("11") && !mon.equals("12")){
                    params.put("mon", dateStr+"0"+mon);
                }else {
                    params.put("mon", dateStr+mon);
                }
            }
        }
        sql.append(" ORDER BY A.TARGET_USER ,DATE_FORMAT(A.OPERATION_TIME,'%Y-%m-%d') ");
        QueryPage<Object[]> result = findBySQL(sql,queryPage,params);
        return  result;
    }
    
    
    //365指标达成率的list页面数据方法
    public QueryPage<Object[]> findByConditions(String employeeIdNum, String employeeNum,
            String startDate,String endDate,QueryPage<Object[]> queryPage) {
    	
    	 Map<String,Object> params =  new HashMap<String,Object>();
         StringBuffer  sql = new StringBuffer();
    
         sql.append(" SELECT USER_CUSTOMER_ID,USER_CODE,COM_RATE_GOAL,CREDIT_RATE_GOAL,HOUSE_RATE_GOAL,RATE_DATE FROM FB_YEAR_APPROPRIATION_RATE FYAR where 1=1 ");
    
         if(StringUtils.isNoneBlank(employeeIdNum)){
             sql.append(" and FYAR.USER_CUSTOMER_ID=:employeeIdNum ");
             params.put("employeeIdNum",employeeIdNum);
         }
         if(StringUtils.isNoneBlank(employeeNum)){
             sql.append(" and FYAR.USER_CODE=:employeeNum ");
             params.put("employeeNum",employeeNum);
         }
         if(StringUtils.isNoneBlank(startDate)){
             sql.append(" and STR_TO_DATE(FYAR.RATE_DATE,'%Y/%m/%d') >= :startDate ");
             params.put("startDate",startDate);
         }
         if(StringUtils.isNoneBlank(endDate)){
             sql.append(" and STR_TO_DATE(FYAR.RATE_DATE,'%Y/%m/%d') <= :endDate ");
             params.put("endDate",endDate);
         }
         //sql.append(" ORDER BY DATE_FORMAT(FYAR.RATE_DATE,'%Y-%m-%d') ");
         QueryPage<Object[]> result = findBySQL(sql,queryPage,params);
         return  result;
    }

}
