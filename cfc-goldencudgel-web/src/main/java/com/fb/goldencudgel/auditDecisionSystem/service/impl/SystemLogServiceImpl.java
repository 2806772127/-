package com.fb.goldencudgel.auditDecisionSystem.service.impl;

import com.comwave.core.data.domain.QueryPage;
import com.comwave.core.jpa.BaseJpaDAO;
import com.fb.goldencudgel.auditDecisionSystem.model.User;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SystemLogServiceImpl extends BaseJpaDAO {

    public QueryPage<Object[]> findByConditions(String operationType, String operationUser, String startDate, String endDate,
                                                QueryPage<Object[]> queryPage) {

        Map<String,Object> params =  new HashMap<String,Object>();
        StringBuffer sql = new StringBuffer("select IFNULL(OPERATION_USER,'') OPERATION_USER,IFNULL(OPERATION_IP,'') OPERATION_IP, " +
                "CASE WHEN OPERATION_TYPE ='01' THEN '登入' WHEN OPERATION_TYPE ='02' THEN '登出' ELSE '' END OPERATION_TYPE, " +
                "IFNULL(CREATE_TIME,'') CREATE_TIME from SYSTWM_LOG where 1=1 ");
        if (StringUtils.isNoneBlank(operationType) && !"nothing".equals(operationType)){
            sql.append(" AND OPERATION_TYPE = :operationType");
            params.put("operationType", operationType);
        }
        if (StringUtils.isNoneBlank(operationUser)){
            sql.append(" AND OPERATION_USER  like  :operationUser");
            params.put("operationUser","%" + operationUser + "%");
        }
        if (StringUtils.isNoneBlank(startDate)){
            sql.append(" AND CREATE_TIME >= :startDate");
            params.put("startDate",startDate);
        }
        if (StringUtils.isNoneBlank(endDate)){
            sql.append(" AND CREATE_TIME <= :endDate");
            params.put("endDate",endDate);
        }
        sql.append(" ORDER BY CREATE_TIME DESC ");
        queryPage = findBySQL(sql,queryPage,params);

        return  queryPage;
    }

}
