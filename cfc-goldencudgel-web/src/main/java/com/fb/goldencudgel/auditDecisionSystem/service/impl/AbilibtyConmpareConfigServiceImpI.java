package com.fb.goldencudgel.auditDecisionSystem.service.impl;

import com.comwave.core.data.domain.QueryPage;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;
import com.fb.goldencudgel.auditDecisionSystem.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.comwave.core.jpa.BaseJpaDAO;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AbilibtyConmpareConfigServiceImpI extends BaseJpaDAO {

    public QueryPage<Object[]> findByConditions(String type, String abilityType, String startDate, String endDate
            , QueryPage<Object[]> queryPage, String startDates, String endDates) {
        // 查选用户角色、所在组、所在区域信息
        User user = UserUtil.getCurrUser();
        Map<String, Object> userparam = new HashMap<>();
        StringBuffer usersql = new StringBuffer("select USER_NAME,USER_AREA,USER_GROUP,USER_OFFICE,USER_TYPE from FB_USER WHERE 1=1 ");
        usersql.append(" and USER_CODE=:usercode");
        userparam.put("usercode", user.getUserCode());
        List<Object[]> userinfos = findBySQL(usersql, userparam).getContent();
        String userRole = "ERROR";
        String userGroup = null;
        String userArea = null;
        if ( userinfos != null && userinfos.size() > 0) {
            userRole = userinfos.get(0)[4]+"";
            userGroup =  user.getUserGroup() == null ? (userinfos.get(0)[2] + "") : user.getUserGroup();
            userArea = user.getUserArea() == null ? (userinfos.get(0)[1] + "") : user.getUserArea();
        }
        //查询
        Map<String,Object> params =  new HashMap<String,Object>();
        StringBuffer sql = new StringBuffer("SELECT CONFIG_ID");
        sql.append(" ,(CASE WHEN TYPE = '01' THEN '經營能力' WHEN TYPE = '02' THEN '償債能力' WHEN TYPE = '03' THEN '財務能力' END) TYPE_NAME ");
        sql.append(" ,(CASE WHEN START_RANGE > 0.00 AND START_INCLUDE_FLAG = '0' THEN CONCAT('(+', START_RANGE, '%') WHEN START_INCLUDE_FLAG = 0 AND START_RANGE <= 0.00 THEN CONCAT('(', START_RANGE, '%') WHEN START_INCLUDE_FLAG = 1 AND START_RANGE <= 0.00 THEN CONCAT('[', START_RANGE, '%') WHEN START_INCLUDE_FLAG = 1 AND START_RANGE > 0 THEN CONCAT('[+', START_RANGE, '%') WHEN START_RANGE IS NULL THEN START_RANGE END) START_RANGE_NAME ");
        sql.append(" ,START_INCLUDE_FLAG ");
        sql.append(" ,(CASE WHEN END_INCLUDE_FLAG = '0' AND END_RANGE > 0.00 THEN CONCAT('+', END_RANGE, '%)') WHEN END_INCLUDE_FLAG = 0 AND END_RANGE <= 0.00 THEN CONCAT(END_RANGE, '%)') WHEN END_INCLUDE_FLAG = 1 AND END_RANGE <= 0.00 THEN CONCAT(END_RANGE, '%]') WHEN END_INCLUDE_FLAG = 1 AND END_RANGE > 0 THEN CONCAT('+', END_RANGE, '%]') WHEN END_RANGE IS NULL THEN END_RANGE END) END_RANGE_NAME ");
        sql.append(" ,END_INCLUDE_FLAG ");
        sql.append(" ,(CASE WHEN ABILITY_TYPE = '01' THEN '優於同業' WHEN ABILITY_TYPE = '02' THEN '與同業相當' WHEN ABILITY_TYPE = '03' THEN '劣於同業' END) ABILITY_TYPE_NAME ");
        sql.append(" ,ABILITY_DESCRIBE,DATE_FORMAT(CREATE_TIME, '%Y-%m-%d') CREATE_TIME,DATE_FORMAT(UPDATE_TIME, '%Y-%m-%d') UPDATE_TIME ");
        sql.append(" FROM COMPANY_ABILITY_COMPARE_CONFIG ");
        sql.append(" WHERE 1=1 ");

        if (StringUtils.isNoneBlank(type)){
            sql.append(" AND TYPE=:type");
            params.put("type",type);
        }
        /*  if (null != endDate ){*/
        if (StringUtils.isNoneBlank(abilityType)){
            sql.append(" AND ABILITY_TYPE=:abilityType ");
            params.put("abilityType",abilityType);
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


        sql.append(" ORDER BY TYPE,ABILITY_TYPE ");

        // System.out.println(sql.toString());
        queryPage = findBySQL(sql,queryPage,params);

        return  queryPage;
    }


    public QueryPage<Object[]> findById(String configid,QueryPage<Object[]> queryPage) {
        //查询
        Map<String,Object> params =  new HashMap<String,Object>();
        StringBuffer sql = new StringBuffer("SELECT \tCONFIG_ID,\n" +
                "\t(\n" +
                "\t\tCASE\n" +
                "\t\tWHEN TYPE = '01' THEN\n" +
                "\t\t\t'經營能力'\n" +
                "\t\tWHEN TYPE = '02' THEN\n" +
                "\t\t\t'償債能力'\n" +
                "\t\tWHEN TYPE = '03' THEN\n" +
                "\t\t\t'財務能力'\n" +
                "\t\tEND\n" +
                "\t) TYPE,\n" +
                "\t(\n" +
                "\t\tCASE\n" +
                "\t\tWHEN START_RANGE > 0.00\n" +
                "\t\tAND START_INCLUDE_FLAG = '0' THEN\n" +
                "\t\t\tCONCAT('(+', START_RANGE, '%')\n" +
                "\t\tWHEN START_INCLUDE_FLAG = 0\n" +
                "\t\tAND START_RANGE <= 0.00 THEN\n" +
                "\t\t\tCONCAT('(', START_RANGE, '%')\n" +
                "\t\tWHEN START_INCLUDE_FLAG = 1\n" +
                "\t\tAND START_RANGE <= 0.00 THEN\n" +
                "\t\t\tCONCAT('[', START_RANGE, '%')\n" +
                "\t\tWHEN START_INCLUDE_FLAG = 1\n" +
                "\t\tAND START_RANGE > 0 THEN\n" +
                "\t\t\tCONCAT('[+', START_RANGE, '%')\n" +
                "\t\tWHEN START_RANGE IS NULL THEN\n" +
                "\t\t\tSTART_RANGE\n" +
                "\t\tEND\n" +
                "\t) START_RANGE,\n" +
                "\tSTART_INCLUDE_FLAG,\n" +
                "\t(\n" +
                "\t\tCASE\n" +
                "\t\tWHEN START_INCLUDE_FLAG = '0'\n" +
                "\t\tAND END_RANGE > 0.00 THEN\n" +
                "\t\t\tCONCAT('+', END_RANGE, '%)')\n" +
                "\t\tWHEN END_INCLUDE_FLAG = 0\n" +
                "\t\tAND END_RANGE <= 0.00 THEN\n" +
                "\t\t\tCONCAT(END_RANGE, '%)')\n" +
                "\t\tWHEN END_INCLUDE_FLAG = 1\n" +
                "\t\tAND END_RANGE <= 0.00 THEN\n" +
                "\t\t\tCONCAT(END_RANGE, '%]')\n" +
                "\t\tWHEN END_INCLUDE_FLAG = 1\n" +
                "\t\tAND END_RANGE > 0 THEN\n" +
                "\t\t\tCONCAT('+', END_RANGE, '%]')\n" +
                "\t\tWHEN END_RANGE IS NULL THEN\n" +
                "\t\t\tEND_RANGE\n" +
                "\t\tEND\n" +
                "\t) END_RANGE,\n" +
                "\tEND_INCLUDE_FLAG,\n" +
                "\t(\n" +
                "\t\tCASE\n" +
                "\t\tWHEN ABILITY_TYPE = '01' THEN\n" +
                "\t\t\t'優於同業'\n" +
                "\t\tWHEN ABILITY_TYPE = '02' THEN\n" +
                "\t\t\t'與同業相當'\n" +
                "\t\tWHEN ABILITY_TYPE = '03' THEN\n" +
                "\t\t\t'劣於同業'\n" +
                "\t\tEND\n" +
                "\t) ABILITY_TYPE,\n" +
                "\tABILITY_DESCRIBE,\n" +
                "\t CREATE_TIME,\n" +
                "\tDATE_FORMAT(UPDATE_TIME, '%Y-%m-%d') UPDATE_TIME FROM COMPANY_ABILITY_COMPARE_CONFIG  where CONFIG_ID=:configid");
        params.put("configid",configid);
        queryPage= findBySQL(sql,queryPage,params);
        return queryPage;
    }
    public QueryPage<Object[]> findByIds(String configid,QueryPage<Object[]> queryPage) {
        //查询
        Map<String,Object> params =  new HashMap<String,Object>();
        StringBuffer sql = new StringBuffer("SELECT * FROM COMPANY_ABILITY_COMPARE_CONFIG  where CONFIG_ID=:configid");
        params.put("configid",configid);
        queryPage= findBySQL(sql,queryPage,params);
        return queryPage;
    }



    public QueryPage<Object[]> findByIdEdit(String configid,QueryPage<Object[]> queryPage) {
        //查询
        Map<String,Object> params =  new HashMap<String,Object>();
        StringBuffer sql = new StringBuffer("SELECT \tCONFIG_ID,\n" +
                "\t(\n" +
                "\t\tCASE\n" +
                "\t\tWHEN TYPE = '01' THEN\n" +
                "\t\t\t'經營能力'\n" +
                "\t\tWHEN TYPE = '02' THEN\n" +
                "\t\t\t'償債能力'\n" +
                "\t\tWHEN TYPE = '03' THEN\n" +
                "\t\t\t'財務能力'\n" +
                "\t\tEND\n" +
                "\t) TYPE,\n" +
                "\t START_RANGE,\n" +
                "\tSTART_INCLUDE_FLAG,\n" +
                "END_RANGE,\n" +
                "\tEND_INCLUDE_FLAG,\n" +
                "\t(\n" +
                "\t\tCASE\n" +
                "\t\tWHEN ABILITY_TYPE = '01' THEN\n" +
                "\t\t\t'優於同業'\n" +
                "\t\tWHEN ABILITY_TYPE = '02' THEN\n" +
                "\t\t\t'與同業相當'\n" +
                "\t\tWHEN ABILITY_TYPE = '03' THEN\n" +
                "\t\t\t'劣於同業'\n" +
                "\t\tEND\n" +
                "\t) ABILITY_TYPE,\n" +
                "\tABILITY_DESCRIBE,\n" +
                "\t CREATE_TIME,\n" +
                "\tUPDATE_TIME FROM COMPANY_ABILITY_COMPARE_CONFIG   where CONFIG_ID=:configid");
        params.put("configid",configid);
        queryPage= findBySQL(sql,queryPage,params);
        return queryPage;
    }
}
