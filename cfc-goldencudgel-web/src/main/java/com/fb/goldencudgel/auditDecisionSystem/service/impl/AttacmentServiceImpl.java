package com.fb.goldencudgel.auditDecisionSystem.service.impl;

import com.comwave.core.jpa.BaseJpaDAO;
import com.fb.goldencudgel.auditDecisionSystem.repository.ApplyProcRepository;
import com.fb.goldencudgel.auditDecisionSystem.utils.ObjectUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AttacmentServiceImpl extends BaseJpaDAO {

    @Autowired
    private ApplyProcRepository applyProcRepository;

    /**
     * @param compilationNo
     * @param trandId
     * @param nodeCode        1 拜访笔记 2 申请进件 3 征信实访
     * @param comIndustryCode
     * @return Map<类型 , 附件列表></>类型 ： musList音频附件 docList其他档案附件 其他为图片
     */
    public Map<Object, List<Object[]>> findAttach(String compilationNo, String trandId, String nodeCode, String comIndustryCode) {
        Map<Object, List<Object[]>> result = new HashMap<Object, List<Object[]>>();
        StringBuffer sql = new StringBuffer();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT A.FILE_NAME,DATE_FORMAT(A.CREATE_TIME, '%Y-%c-%d %H:%i:%s') AS CREATE_TIME,");
        sql.append("       A.ATTACH_ID,IFNULL(A.ATTACH_TYPE_NAME,''), ");
        sql.append("       A.FILE_CATEGORY,A.ATTACH_TYPE_CODE, ");
        sql.append("       concat('/',substring_index(A.ATTACH_URL, '/', -2))");
        sql.append("  FROM FB_CREDIT_REPORTING_FILE CRF ");
        sql.append(" INNER JOIN FB_ATTACHMENT A ON A.FILE_CONTEXT_ID = CRF.ATTACH_ID ");
        sql.append("  LEFT JOIN FB_ATTACHMENT_CONFIG FAC ON FAC.NODE_CODE = CRF.NODE_CODE ");
        sql.append("   AND FAC.ATTACH_TYPE_CODE=A.ATTACH_TYPE_CODE AND A.ATTACH_NAME_CODE=FAC.ATTACH_NAME_CODE ");
        if (StringUtils.isNoneBlank(comIndustryCode)) {
            sql.append(" AND FAC.INDUSTRY_TYPE = :comIndustryCode");
            params.put("comIndustryCode", comIndustryCode);
        }
        sql.append(" WHERE CRF.COMPILATION_NO = :compilationNo");
        params.put("compilationNo", compilationNo);
        sql.append(" AND CRF.TRAND_ID = :trandId");
        params.put("trandId", trandId);
        sql.append(" AND CRF.NODE_CODE = :nodeCode");
        params.put("nodeCode", nodeCode);
        sql.append(" ORDER BY FAC.SHOW_ORDER ASC ");
        List<Object[]> content = findBySQL(sql, params).getContent();
        List<Object[]> musList = new ArrayList<>();
        List<Object[]> docList = new ArrayList<>();
        for (Object[] obj : content) {
            if ("1".equals(ObjectUtil.obj2String(obj[4]))) {//音频
                musList.add(obj);
            } else if ("2".equals(ObjectUtil.obj2String(obj[4]))) { //图片
                Map<String, String> key = new HashMap<String, String>();
                key.put(ObjectUtil.obj2String(obj[5]), ObjectUtil.obj2String(obj[3]));
                List<Object[]> attachmentList = result.get(key);
                if (attachmentList == null) {
                    attachmentList = new ArrayList<>();
                }
                attachmentList.add(obj);
                result.put(key, attachmentList);
            } else { //其他
                docList.add(obj);
            }
        }
        result.put("musList", musList);//音频附件
        result.put("docList", docList);//其他档案附件
        return result;
    }

    /**
     * 附件配置类型
     */
    public List<Object[]> findCheckItems(String nodeCode, String comIndustryCode,String compCode) {
        String prodCode = applyProcRepository.getApplyProdCode(compCode);
        Map<String, Object> params = new HashMap<>();
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT DISTINCT ATTACH_TYPE_CODE,ATTACH_TYPE_NAME FROM FB_ATTACHMENT_CONFIG WHERE 1=1");
        sb.append(" AND IS_ENABLE = 1");
        sb.append(" AND NODE_CODE=:nodeCode");
        params.put("nodeCode", nodeCode);
        if(StringUtils.isNoneBlank(comIndustryCode)) {
            sb.append(" AND INDUSTRY_TYPE=:comIndustryCode");
            params.put("comIndustryCode", comIndustryCode);
        }
        sb.append("  ORDER BY SHOW_ORDER");
        return findBySQL(sb, params).getContent();
    }

    public String findByCheckItems(String checkItem,String comIndustryCode,String compCode,String nodeCode) {
        Map<String, Object> params = new HashMap<>();
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT A.ATTACH_NAME_CODE ,A.ATTACH_NAME ");
        sb.append("  FROM FB_ATTACHMENT_CONFIG A ");
        sb.append(" WHERE A.NODE_CODE=:nodeCode");
        params.put("nodeCode", nodeCode);
        sb.append("   AND A.IS_ENABLE = 1");
        sb.append("   AND A.ATTACH_TYPE_CODE=:checkItem");
        params.put("checkItem", checkItem);
        if(StringUtils.isNoneBlank(comIndustryCode)) {
            sb.append(" AND A.INDUSTRY_TYPE=:comIndustryCode");
            params.put("comIndustryCode", comIndustryCode);
        }
        sb.append("  ORDER BY A.SHOW_ORDER");
        List<Object[]> result = findBySQL(sb,params).getContent();
        StringBuffer options = new StringBuffer();
        options.append("<option value=''>--請選擇--</option>");
        if(null != result && result.size()>0){
            for(Object[] obj:result){
                options.append("<option value='" + obj[0] + "'>" + obj[1] + "</option>");
            }
        }
        return options.toString();
    }
}
