package com.fb.goldencudgel.auditDecisionSystem.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.comwave.core.data.domain.QueryPage;
import com.comwave.core.jpa.BaseJpaDAO;
import com.fb.goldencudgel.auditDecisionSystem.domain.attachmentConfig.FbAttachmentConfig;
import com.fb.goldencudgel.auditDecisionSystem.repository.AttachmentConfigRepository;

@Service
@Transactional
public class AttacmentConfigServiceImpl extends BaseJpaDAO {

    @Autowired
    private AttachmentConfigRepository attachmentConfigRepository;

    public List<FbAttachmentConfig> findAll(){
        return  attachmentConfigRepository.findAll();
    }

    public QueryPage<Object[]> findByIndustryType(String industryType,QueryPage<Object[]> curPage){
        Map<String,Object> param = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer("  SELECT ATTACH_TYPE_ID,NODE_CODE ,NODE_NAME,INDUSTRY_TYPE ,CHECK_ITEM ,ATTACH_TYPE_CODE,ATTACH_TYPE_NAME,IS_ENABLE ,IS_REQUIRED FROM FB_ATTACHMENT_CONFIG WHERE 1=1  ");
//        sql.append(" AND IS_ENABLE ='1' ");

        if(StringUtils.isNoneBlank(industryType) && !"nothing".equals(industryType)){
            sql.append(" AND INDUSTRY_TYPE = :industryType ");
            param.put("industryType",industryType);
        }

        return  findBySQL(sql,curPage,param);
    }

    public QueryPage<Object[]> findByInTypeAndNodeCo(String industryType,String nodeCode,QueryPage<Object[]> curPage){
        Map<String,Object> param = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer("SELECT ");
        sql.append(" AC.ATTACH_TYPE_ID ");
        sql.append(" , AC.NODE_CODE ");
        sql.append(" , AC.NODE_NAME ");
        sql.append(" , AC.INDUSTRY_TYPE ");
        sql.append(" , AC.CHECK_ITEM ");
        sql.append(" , AC.ATTACH_TYPE_CODE ");
        sql.append(" , AC.ATTACH_TYPE_NAME ");
        sql.append(" , AC.IS_ENABLE ");
        sql.append(" , AC.IS_REQUIRED ");
        sql.append(" , AC.ATTACH_NAME_CODE ");
        sql.append(" , AC.ATTACH_NAME ");
        sql.append(" , VDDI.ITEM_NAME AS INDUSTRY_TYPE_NAME ");
        sql.append(" , AC.SHOW_ORDER");
        sql.append(" FROM FB_ATTACHMENT_CONFIG AC ");
        sql.append(" LEFT JOIN VIEW_DATA_DICT_ITEM VDDI ON AC.INDUSTRY_TYPE = VDDI.ITEM_CODE AND VDDI.DICT_ID = 'BUSINESS_TYPE' ");

        sql.append(" WHERE 1=1 ");
        if(StringUtils.isNoneBlank(nodeCode) && !"nothing".equals(nodeCode)){
            sql.append(" AND AC.NODE_CODE = :nodeCode ");
            param.put("nodeCode",nodeCode);
        }
        if("3".equals(nodeCode) || "2".equals(nodeCode)){
            if(StringUtils.isNoneBlank(industryType) && !"nothing".equals(industryType)){
                sql.append(" AND AC.INDUSTRY_TYPE = :industryType ");
                param.put("industryType",industryType);
            }
        }
        sql.append(" ORDER BY NODE_CODE, INDUSTRY_TYPE, SHOW_ORDER ");
        return  findBySQL(sql,curPage,param);
    }

    public QueryPage<Object[]> findByFcodeAndIndustryType(String industryType,String attachTypeCode,QueryPage<Object[]> curPage){
        Map<String,Object> param = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer("  SELECT ATTACH_TYPE_ID,NODE_CODE ,NODE_NAME,INDUSTRY_TYPE ,CHECK_ITEM ,ATTACH_NAME_CODE,ATTACH_NAME,IS_ENABLE ,IS_REQUIRED FROM FB_ATTACHMENT_CONFIG WHERE NODE_CODE=3  ");
        if(StringUtils.isNoneBlank(industryType) && !"nothing".equals(industryType)){
            sql.append(" AND INDUSTRY_TYPE = :industryType ");
            param.put("industryType",industryType);
        }
        if(StringUtils.isNoneBlank(attachTypeCode) && !"nothing".equals(attachTypeCode)){
            sql.append(" AND ATTACH_TYPE_CODE = :attachTypeCode ");
            param.put("attachTypeCode",attachTypeCode);
        }
        return  findBySQL(sql,curPage,param);
    }

    public String findMaxShowOrder (){
        Map<String,Object> param = new HashMap<String, Object>();
        String sql = "select IFNULL(MAX(SHOW_ORDER),'0') from FB_ATTACHMENT_CONFIG";
        QueryPage<Object[]> result = findBySQL(sql,param);
        List concent = result.getContent();
        String showOrder = concent.get(0).toString();
        return showOrder;

    }


}
