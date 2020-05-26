package com.fb.goldencudgel.auditDecisionSystem.service;

import com.comwave.core.jpa.BaseJpaDAO;
import com.fb.goldencudgel.auditDecisionSystem.domain.commons.SystemConfig;
import com.fb.goldencudgel.auditDecisionSystem.domain.token.FbTokenDetial;
import com.fb.goldencudgel.auditDecisionSystem.repository.FbTokenDetialRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.SystemConfigRepository;
import com.fb.goldencudgel.auditDecisionSystem.utils.ObjectUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.*;

@Service
@Transactional
public class IndexService extends BaseJpaDAO {

    @Autowired
    private FbTokenDetialRepository tokenDetialRepository;

    @Autowired
    private SystemConfigRepository systemConfigRepository;

    public List<Object> findHasFunction(String roleId,String userCode) {
        Map<String,Object> params =  new HashMap<>();
        StringBuffer sql = new StringBuffer(" SELECT FUNC_CODE FROM SYS_ROLE_FUNC WHERE IS_ENABLED='1' ");
        if (StringUtils.isNoneBlank(roleId)){
            sql.append(" AND ROLE_ID = :roleId");
            params.put("roleId", roleId);
        }
        sql.append(" union all  ");
        sql.append(" select FUNC_CODE FROM SYS_ROLE_FUNC  ");
        sql.append("  where IS_ENABLED='1' and AUTH_TYPE = '2' ");
        sql.append("    and ROLE_ID in(select fe.ENTRUST_JURISDICTION from FB_ENTRUST fe ");
        sql.append(" 					where DATE_FORMAT(NOW(),'%Y-%d-%d %H:%i') >= DATE_FORMAT(ENTRUST_START_TIME,'%Y-%d-%d %H:%i') ");
        sql.append(" 					  and DATE_FORMAT(NOW(),'%Y-%d-%d %H:%i') < DATE_FORMAT(ENTRUST_END_TIME,'%Y-%d-%d %H:%i') ");
        sql.append(" 					  and fe.ENTRUST_USER_CODE = :userCode) ");
        sql.append(" 	 and FUNC_CODE in (select ITEM_CODE from VIEW_DATA_DICT_ITEM where DICT_ID = 'AGENT_FUNCATION_CODE')");
        params.put("userCode", userCode);
        sql.append(" union all  ");
        sql.append("SELECT DISTINCT CASE WHEN FUNCTION_CODE = 'C0001' THEN 'Z_WORD_SHOP' WHEN FUNCTION_CODE = 'C0002' THEN 'Z_CREDIT_REPORTING' ELSE '' END FUNC_CODE FROM FB_APPROVE_CONFIG FAC WHERE FAC.AGENT_USER_CODE='"+userCode+"' ");
        sql.append("UNION ALL ");           
        sql.append("SELECT DISTINCT CASE WHEN FUNCTION_CODE = 'C0001' THEN 'Z_AUDIT' WHEN FUNCTION_CODE = 'C0002' THEN 'Z_AUDIT' ELSE '' END FUNC_CODE FROM FB_APPROVE_CONFIG FAC WHERE FAC.APPROVE_USER_CODE='"+userCode+"'");
        return  findBySQL(sql,params).getContent();
    }

    public FbTokenDetial updateToken(String uuid) {
        FbTokenDetial tokenDetial = tokenDetialRepository.findByUid(uuid);
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        if(tokenDetial == null || StringUtils.isEmpty(tokenDetial.getToken())) {
            tokenDetial = new FbTokenDetial();
            tokenDetial.setUserUid(uuid);
            tokenDetial.setCreateTime(new Date());
            tokenDetial.setLoginTime(new Date());
            tokenDetial.setLoginType("1");
            tokenDetial.setToken(token);
            SystemConfig systemConfig = systemConfigRepository.findByID("TOKEN_VALIDATION");
            tokenDetial.setTokenValid(Double.valueOf(StringUtils.isNoneBlank(systemConfig.getKeyvalue()) ? systemConfig.getKeyvalue() : "0"));
            tokenDetial.setTokenValidType("M");
        }
        tokenDetial.setToken(token);
        tokenDetial.setLastOpratorTime(new Timestamp(new Date().getTime()));
        tokenDetialRepository.saveAndFlush(tokenDetial);
        return tokenDetial;
    }
    
    public Map<String, String> getOrgList(String userCode) {
        Map<String, String> result = new HashMap<>();
        StringBuffer sql = new StringBuffer();
        sql.append("select USER_CODE, USER_NAME, USER_AREA, USER_AREA_NAME, USER_GROUP, USER_GROUP_NAME FROM ORG_LIST ");
        sql.append(" where USER_CODE = '" + userCode + "'");
        List<Object[]> context = findBySQL(sql).getContent();
        if (!CollectionUtils.isEmpty(context)) {
            Object[] queryResult = context.get(0);
            result.put("userCode", ObjectUtil.obj2String(queryResult[0]));
            result.put("userName", ObjectUtil.obj2String(queryResult[1]));
            result.put("userAreaCode", ObjectUtil.obj2String(queryResult[2]));
            result.put("userAreaName", ObjectUtil.obj2String(queryResult[3]));
            result.put("userGroupCode", ObjectUtil.obj2String(queryResult[4]));
            result.put("userGroupName", ObjectUtil.obj2String(queryResult[5]));
        }
        
        return result;
    }
}
