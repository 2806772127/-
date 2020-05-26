package com.fb.goldencudgel.auditDecisionSystem.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.comwave.core.data.domain.QueryPage;
import com.comwave.core.jpa.BaseJpaDAO;
import com.fb.goldencudgel.auditDecisionSystem.domain.commons.SystemConfig;
import com.fb.goldencudgel.auditDecisionSystem.domain.report.FbReportConfig;
import com.fb.goldencudgel.auditDecisionSystem.model.User;
import com.fb.goldencudgel.auditDecisionSystem.repository.FbReportConfigRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.SystemConfigRepository;
import com.fb.goldencudgel.auditDecisionSystem.utils.ObjectUtil;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;

/**
 * @author mazongjian
 * @createdDate 2019年3月24日 - 下午2:46:15 
 */
@Service
@Transactional
public class ReportServiceImpl extends BaseJpaDAO {
    @Autowired
    private FbReportConfigRepository fbReportConfigRepository;
    
    @Autowired
    private SystemConfigRepository systemConfigRepository;
    
    public Map<String, Object> saveReportConfig(String reportCode, String reportName, String reportPath, String reportType, String queryDateLabel, String queryDateFormat, String reportQueryRegion, String reportDownload, String roleStr) {
        Map<String, Object> map = new HashMap<>();
        try {
            User user = UserUtil.getCurrUser();
            FbReportConfig reportConfig = new FbReportConfig();
            reportConfig.setReportCode(reportCode);
            reportConfig.setReportName(reportName);
            reportConfig.setReportPath(reportPath);
            reportConfig.setReportType(reportType);
            reportConfig.setQueryDateLabel(queryDateLabel);
            reportConfig.setQueryDateFormat(queryDateFormat);
            reportConfig.setReportQueryRegion(reportQueryRegion);
            reportConfig.setReportDownload(reportDownload);
            reportConfig.setCreateUser(user.getUserCode());
            reportConfig.setCreateTime(new Date());
            reportConfig.setUpdateUser(user.getUserCode());
            reportConfig.setUpdateTime(new Date());
            reportConfig.setReportAuthority(roleStr);
            fbReportConfigRepository.save(reportConfig);
            map.put("flag", true);
            map.put("msg", "儲存成功");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("flag", false);
            map.put("msg", "儲存失敗");
        }
        
        return map;
    }
    
    public QueryPage<FbReportConfig> findReportConfigByName(String reportName, QueryPage<FbReportConfig> queryPage) {
        User user = UserUtil.getCurrUser();
        String roleId = user.getRoleId();
        StringBuffer sql = new StringBuffer();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT T.REPORT_NAME, T.REPORT_PATH, ");
        sql.append(" CASE WHEN T.REPORT_TYPE = 'S' THEN 'Spotfire報表' ELSE 'Excel報表'  END REPORT_TYPE, ");
        sql.append(" T.UPDATE_TIME, T.CREATE_TIME, T.REPORT_ID");
        sql.append(", T.QUERY_DATE_LABEL"); // 6
        sql.append(", T.QUERY_DATE_FORMAT"); // 7
        sql.append(", T.REPORT_QUERY_REGION"); // 8
        sql.append(", T.REPORT_DOWNLOAD"); // 9
        sql.append(", T.REPORT_CODE"); // 10
        sql.append(", T.REPORT_AUTHORITY"); // 11
        sql.append(" FROM FB_REPORT_CONFIG T");
        sql.append(" left join VIEW_DATA_DICT_ITEM T1 ");
        sql.append(" on T.REPORT_CODE = T1.ITEM_CODE ");
        sql.append(" WHERE 1 = 1 AND REPORT_AUTHORITY LIKE :roleStr");
        sql.append(" ORDER BY T1.ITEM_EXT + 0");
        params.put("roleStr", "%" + roleId + "%");
        
        if (!StringUtils.isBlank(reportName)) {
            sql.append(" AND REPORT_NAME = :reportName");
            params.put("reportName", reportName);
        }
        queryPage = findBySQL(sql, queryPage, params);
        return queryPage;
    }
    
    public Map<String, Object> delReportConfigById(String reportId) {
        Map<String, Object> map = new HashMap<>();
        try {
            fbReportConfigRepository.deleteById(reportId);
            map.put("flag", true);
            map.put("msg", "刪除成功");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("flag", true);
            map.put("msg", "刪除失敗");
        }
        return map;
    }
    
    public Map<String, Object> updateReportConfigById(String reportId, String reportCode, String reportName, String reportPath, String reportType, String queryDateLabel, 
                                                      String queryDateFormat, String reportQueryRegion, String reportDownload, String roleStr) {
        Map<String, Object> map = new HashMap<>();
        try {
            User user = UserUtil.getCurrUser();
            FbReportConfig  fbReportConfig = fbReportConfigRepository.findByReportId(reportId);
            if (!StringUtils.isBlank(reportCode)) {
                fbReportConfig.setReportCode(reportCode);
            }
            
            if (!StringUtils.isBlank(reportName)) {
                fbReportConfig.setReportName(reportName);
            }
            
            if (!StringUtils.isBlank(reportPath)) {
                fbReportConfig.setReportPath(reportPath);
            }
            
            if (!StringUtils.isBlank(reportType)) {
                fbReportConfig.setReportType(reportType);
            }
            
            if (!StringUtils.isBlank(queryDateLabel)) {
                fbReportConfig.setQueryDateLabel(queryDateLabel);
            }
            
            if (!StringUtils.isBlank(queryDateFormat)) {
                fbReportConfig.setQueryDateFormat(queryDateFormat);
            }
            
            if (!StringUtils.isBlank(reportQueryRegion)) {
                fbReportConfig.setReportQueryRegion(reportQueryRegion);
            }
            
            if (!StringUtils.isBlank(reportDownload)) {
                fbReportConfig.setReportDownload(reportDownload);
            }
            
            if (!StringUtils.isBlank(roleStr)) {
                fbReportConfig.setReportAuthority(roleStr);
            }
            
            fbReportConfig.setUpdateUser(user.getUserCode());
            fbReportConfig.setUpdateTime(new Date());
            fbReportConfigRepository.save(fbReportConfig);
            
            map.put("flag", true);
            map.put("msg", "更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("flag", true);
            map.put("msg", "更新失敗");
        }
        
        return map;
    }
    
    public SystemConfig querySystemConfigById(String id) {
        SystemConfig sysConfig = systemConfigRepository.findByID(id);
        return sysConfig;
    }
    
    /**
     * @description: 查找審查批保逾放率里的批次號
     * @author: mazongjian
     * @return  
     * @date 2019年5月26日
     */
    public List<String> queryBatchNoList() {
        List<String> batchNoList = new ArrayList<>();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append(" BATCH_NUM ");
        sql.append(" FROM EXA_BATCH_RATE_REPORT ");
        sql.append(" GROUP BY BATCH_NUM ");
        sql.append(" ORDER BY BATCH_NUM ");
        
        @SuppressWarnings("unchecked")
        QueryPage<Object[]> queryPage = findBySQL(sql);
        if (queryPage == null) {
            return batchNoList;
        }
        List<Object[]> content = queryPage.getContent();
        if (CollectionUtils.isEmpty(content)) {
            return batchNoList;
        }
        
        for (Object objArr : content) {
            batchNoList.add(ObjectUtil.obj2String(objArr));
        }
        
        return batchNoList;
    }
    
    /**
     * @description: 查找角色信息
     * @author: mazongjian
     * @return  
     * @date 2019年5月26日
     */
    public Map<String, Object> queryRoleInfo() {
        Map<String, Object> roleMap = new HashMap<>();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ROLE_ID ");
        sql.append(", ROLE_NAME ");
        sql.append(" FROM SYS_ROLE ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND ROLE_ENABLE = '1' ");
        
        @SuppressWarnings("unchecked")
        QueryPage<Object[]> queryPage = findBySQL(sql);
        if (queryPage == null) {
            return roleMap;
        }
        List<Object[]> content = queryPage.getContent();
        if (CollectionUtils.isEmpty(content)) {
            return roleMap;
        }
        
        for (Object[] objArr : content) {
            roleMap.put(ObjectUtil.obj2String(objArr[0]), objArr[1]);
        }
        
        return roleMap;
    }
    
    /**
     * @description: 查找专案别
     * @author: mazongjian
     * @return  
     * @date 2019年5月29日
     */
    public List<String> queryPrgCode() {
        List<String> prgCodeList = new ArrayList<>();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append(" APRV_PRG_CODE ");
        sql.append(" FROM APPRO_RATE_REPORT ");
        sql.append(" GROUP BY APRV_PRG_CODE ");
        sql.append(" ORDER BY APRV_PRG_CODE ");
        
        @SuppressWarnings("unchecked")
        QueryPage<Object[]> queryPage = findBySQL(sql);
        if (queryPage == null) {
            return prgCodeList;
        }
        List<Object[]> content = queryPage.getContent();
        if (CollectionUtils.isEmpty(content)) {
            return prgCodeList;
        }
        
        for (Object objArr : content) {
            prgCodeList.add(ObjectUtil.obj2String(objArr));
        }
        
        return prgCodeList;
    }
    
    public Map<String, String> getAreaList(String userCode) {
        Map<String, String> result = new LinkedHashMap<String, String>();
        StringBuffer sql = new StringBuffer();
        sql.append("select USER_AREA, USER_AREA_NAME ");
        sql.append(" from USER_LIST OL");
        sql.append(" INNER JOIN VIEW_DATA_DICT_ITEM VDDI ON OL.USER_AREA = VDDI.ITEM_CODE AND VDDI.DICT_ID = 'ARRAY_SCHEMA_SORT_AREA' ");
        sql.append(" where 1 = 1 ");
        sql.append(" and IFNULL(OL.USER_AREA, '') != '' ");
        sql.append(" and IFNULL(OL.USER_AREA, '') not in ('188J', '188K', '188L') ");
        if (StringUtils.isNotBlank(userCode)) {
            sql.append(" and OL.USER_CODE = '" + userCode + "'");
        }
        sql.append(" group by OL.USER_AREA, OL.USER_AREA_NAME order by VDDI.ITEM_REMARK");
        List<Object[]> context = findBySQL(sql).getContent();
        for(Object[] ob : context) {
            String areaCode = ob[0] == null ? "" : ob[0].toString();
            String areaName = ob[1] == null ? "" : ob[1].toString();
            result.put(areaCode,areaName);
        }
        return result;
    }
    
    public Map<String, String> getGroupList(String areaCode) {
        Map<String, String> result = new LinkedHashMap<>();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT OL.USER_GROUP, OL.USER_GROUP_NAME ");
        sql.append(" FROM USER_LIST OL ");
        sql.append(" INNER JOIN VIEW_DATA_DICT_ITEM VDDI1 ON OL.USER_AREA = VDDI1.ITEM_CODE AND VDDI1.DICT_ID = 'ARRAY_SCHEMA_SORT_AREA' ");
        sql.append(" INNER JOIN VIEW_DATA_DICT_ITEM VDDI2 ON OL.USER_GROUP = VDDI2.ITEM_CODE AND VDDI1.ITEM_CODE = VDDI2.DICT_ID ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND IFNULL(OL.USER_GROUP, '') != '' ");
        sql.append(" AND IFNULL(OL.USER_AREA, '') not in ('188J', '188K', '188L') ");
        if (StringUtils.isNotBlank(areaCode)) {
          sql.append(" and OL.USER_AREA = '" + areaCode + "'");
        }
        sql.append(" GROUP BY OL.USER_GROUP, OL.USER_GROUP_NAME ");
        sql.append(" ORDER BY VDDI1.ITEM_REMARK, VDDI2.ITEM_REMARK");
        List<Object[]> context = findBySQL(sql).getContent();
        for(Object[] ob : context) {
            String groupCode = ob[0] == null ? "" : ob[0].toString();
            String groupName = ob[1] == null ? "" : ob[1].toString();
            result.put(groupCode,groupName);
        }
        return result;
    }
    
    public Map<String, String> getUserList(String areaCode, String groupCode) {
        Map<String, String> result = new LinkedHashMap<>();
        StringBuffer sql = new StringBuffer();
        sql.append("select DISTINCT USER_CODE,USER_NAME from USER_LIST ");
        sql.append("where IFNULL(USER_CODE,'') != '' ");
        sql.append(" and IFNULL(USER_AREA, '') not in ('188J', '188K', '188L') ");
        if(StringUtils.isNotBlank(areaCode)) {
            sql.append(" AND USER_AREA = '" + areaCode + "'");
        }
        if(StringUtils.isNotBlank(groupCode)) {
            sql.append(" AND USER_GROUP = '" + groupCode + "'");
        }
        sql.append(" ORDER BY USER_CODE");
        List<Object[]> context = findBySQL(sql).getContent();
        for(Object[] ob : context) {
            String userCode = ob[0] == null ? "" : ob[0].toString();
            String userName = ob[1] == null ? "" : ob[1].toString();
            result.put(userCode,userName);
        }
        return result;
    }
    
    /**
     * @description: 查詢作業步驟
     * @author: mazongjian
     * @return  
     * @date 2019年9月4日
     */
    public Map<String, Object> queryStepMapFromGSJSB() {
        Map<String, Object> stepMap = new LinkedHashMap<>();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append(" STEP_ID, ");
        sql.append(" STEP_NM ");
        sql.append(" FROM DURATION ");
        sql.append(" GROUP BY STEP_ID, STEP_NM ");
        sql.append(" ORDER BY STEP_ID, STEP_NM ");
        
        @SuppressWarnings("unchecked")
        QueryPage<Object[]> queryPage = findBySQL(sql);
        if (queryPage == null) {
            return stepMap;
        }
        List<Object[]> content = queryPage.getContent();
        if (CollectionUtils.isEmpty(content)) {
            return stepMap;
        }
        
        for (Object[] objArr : content) {
            stepMap.put(ObjectUtil.obj2String(objArr[0]), objArr[1]);
        }
        
        return stepMap;
    }
    
    public Map<String, Object> queryStepMapFromPJGSTJB() {
        Map<String, Object> stepMap = new LinkedHashMap<>();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append(" T.STEP_ID, ");
        sql.append(" T.STEP_NM ");
        sql.append(" FROM FB_CRD_UNFINISHED T");
        sql.append(" INNER JOIN REPORT_AVE_WORKTIME T1 ON T.STEP_ID = T1.ROLE_TYPE");
        sql.append(" GROUP BY T.STEP_ID, T.STEP_NM ");
        sql.append(" ORDER BY T.STEP_ID, T.STEP_NM ");
        
        @SuppressWarnings("unchecked")
        QueryPage<Object[]> queryPage = findBySQL(sql);
        if (queryPage == null) {
            return stepMap;
        }
        List<Object[]> content = queryPage.getContent();
        if (CollectionUtils.isEmpty(content)) {
            return stepMap;
        }
        
        for (Object[] objArr : content) {
            stepMap.put(ObjectUtil.obj2String(objArr[0]), objArr[1]);
        }
        
        return stepMap;
    }
}
