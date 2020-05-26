package com.fb.goldencudgel.auditDecisionSystem.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.comwave.core.data.domain.QueryPage;
import com.comwave.core.jpa.BaseJpaDAO;
import com.fb.goldencudgel.auditDecisionSystem.domain.systemData.FbSystemData;
import com.fb.goldencudgel.auditDecisionSystem.model.User;
import com.fb.goldencudgel.auditDecisionSystem.repository.FbSystemDataRepository;
import com.fb.goldencudgel.auditDecisionSystem.utils.ObjectUtil;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;

/**
 * @author mazongjian
 * @createdDate 2019年8月5日 - 下午11:24:02 
 */
@Service
@Transactional
public class SystemDataServiceImpl extends BaseJpaDAO {
    @Autowired
    private FbSystemDataRepository fbSystemDataRepository;
    
    /**
     * @description: 查詢列表數據
     * @author: mazongjian
     * @return  
     * @date 2019年8月5日
     */
    @SuppressWarnings("unchecked")
    public QueryPage<Object[]> querySystemDataList(String schemaName, String tableName, QueryPage<Object[]> queryPage) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("    SA.SCHEMA_NAME, ");
        sql.append("    SA.TABLE_NAME, ");
        sql.append("    SA.GENERATE_STATUS, ");
        sql.append("    SA.UPDATE_TIME, ");
        sql.append("    DD.ITEM_NAME GENERATE_STATUS_NAME, ");
        sql.append("    SA.ID ");
        sql.append(" FROM FB_SYSTEM_DATA SA ");
        sql.append(" INNER JOIN VIEW_DATA_DICT_ITEM DD ON SA.GENERATE_STATUS = DD.ITEM_CODE AND DD.DICT_ID = 'DATA_GENERATE_STATUS'");
        sql.append(" WHERE 1 = 1 ");
        
        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isEmpty(schemaName)) {
            sql.append(" AND SCHEMA_NAME = :schemaName ");
            params.put("schemaName", schemaName);
        }
        if (!StringUtils.isEmpty(tableName)) {
            sql.append(" AND TABLE_NAME = :tableName ");
            params.put("tableName", tableName);
        }
        QueryPage<Object[]> queryPageResult = findBySQL(sql, queryPage, params);
        return queryPageResult;
    }
    
    @SuppressWarnings("unchecked")
    public QueryPage<Object[]> querySystemConfig(String queryParams) {
        
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("    ID, ");
        sql.append("    KEYNAME, ");
        sql.append("    KEYVALUE ");
        sql.append(" FROM SYSTEM_CONFIG ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND ENABLED = 1 ");
        sql.append(" AND ID IN ").append(queryParams);
        sql.append(" ORDER BY ID");
        
        QueryPage<Object[]> queryPage = findBySQL(sql);
        return queryPage;
    }
    
    @SuppressWarnings("unchecked")
    public QueryPage<Object[]> queryGenerateStatus(String schemaName, String tableName) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("    SA.GENERATE_STATUS, ");
        sql.append("    DD.ITEM_NAME GENERATE_STATUS_NAME, ");
        sql.append("    DATE_FORMAT(SA.UPDATE_TIME, '%Y-%m-%d %H:%i:%s') UPDATE_TIME ");
        sql.append(" FROM FB_SYSTEM_DATA SA ");
        sql.append(" INNER JOIN VIEW_DATA_DICT_ITEM DD ON SA.GENERATE_STATUS = DD.ITEM_CODE AND DD.DICT_ID = 'DATA_GENERATE_STATUS'");
        sql.append(" WHERE 1 = 1");
        sql.append(" AND SA.SCHEMA_NAME = :schemaName");
        sql.append(" AND SA.TABLE_NAME = :tableName");
        Map<String, Object> params = new HashMap<>();
        params.put("schemaName", schemaName);
        params.put("tableName", tableName);
        
        QueryPage<Object[]> queryPage = findBySQL(sql, params);
        return queryPage;
    }
    
    /**
     * @description: 更新产制状态为产制中
     * @author: mazongjian
     * @param schemaName
     * @param tableName  
     * @date 2019年8月12日
     */
    public void updateGenerateStatus(String schemaName, String tableName, String status) {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE FB_SYSTEM_DATA set GENERATE_STATUS = :status, UPDATE_TIME = NOW()");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND SCHEMA_NAME = :schemaName ");
        sql.append(" AND TABLE_NAME = :tableName ");
        Map<String, Object> params = new HashMap<>();
        params.put("status", status);
        params.put("schemaName", schemaName);
        params.put("tableName", tableName);
        executeSQL(sql, params);
    }
    
    public Map<String, Object> addSystemData(Map<String, Object> systemDataMap) {
        User user = UserUtil.getCurrUser();
        Map<String, Object> returnMap = new HashMap<>();
        
        String schemaName = ObjectUtil.obj2String(systemDataMap.get("schemaName"));
        String tableName = ObjectUtil.obj2String(systemDataMap.get("tableName"));
        String maskColumn = ObjectUtil.obj2String(systemDataMap.get("maskColumn"));
        
        if (StringUtils.isBlank(schemaName) || StringUtils.isBlank(tableName)) {
            returnMap.put("returnFlag", false);
            returnMap.put("returnCode", "9999");
            returnMap.put("returnMsg", "關鍵欄位缺失");
            return returnMap;
        }
        
        FbSystemData fbSystemData = new FbSystemData();
        fbSystemData.setSchemaName(schemaName);
        fbSystemData.setTableName(tableName.toUpperCase());
        fbSystemData.setGenerateStatus("0");
        if (StringUtils.isNotEmpty(maskColumn)) {
            String maskColumnUpper = maskColumn.toUpperCase();
            fbSystemData.setMaskColumn(maskColumnUpper);
        }
        fbSystemData.setCreateTime(new Date());
        fbSystemData.setCreateUser(user.getUserCode());
        fbSystemData.setUpdateTime(new Date());
        fbSystemData.setUpdateUser(user.getUserCode());
        fbSystemDataRepository.saveAndFlush(fbSystemData);
        returnMap.put("returnFlag", true);
        returnMap.put("returnCode", "0000");
        returnMap.put("returnMsg", "新增成功");
        
        return returnMap;
    }
    
    public Map<String, Object> deleteSystemDataById(String id) {
        Map<String, Object> returnMap = new HashMap<>();
        if (StringUtils.isBlank(id)) {
            returnMap.put("returnFlag", false);
            returnMap.put("returnCode", "9999");
            returnMap.put("returnMsg", "主鍵缺失");
            return returnMap;
        }
        
        fbSystemDataRepository.deleteById(id);
        
        returnMap.put("returnFlag", true);
        returnMap.put("returnCode", "0000");
        returnMap.put("returnMsg", "刪除成功");
        
        return returnMap;
    }
    
}
