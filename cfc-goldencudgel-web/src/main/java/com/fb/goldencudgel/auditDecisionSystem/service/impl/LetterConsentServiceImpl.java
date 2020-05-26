package com.fb.goldencudgel.auditDecisionSystem.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fb.goldencudgel.auditDecisionSystem.repository.LetterConsentRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.comwave.core.data.domain.QueryPage;
import com.comwave.core.jpa.BaseJpaDAO;
import com.fb.goldencudgel.auditDecisionSystem.domain.letterConsent.FbLetterConsent;
import com.fb.goldencudgel.auditDecisionSystem.domain.letterConsent.FbLetterConsentDetail;
import com.fb.goldencudgel.auditDecisionSystem.model.User;
import com.fb.goldencudgel.auditDecisionSystem.repository.LetterConsentDetailRepository;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;

@Service
@Transactional
public class LetterConsentServiceImpl extends BaseJpaDAO{

	@Autowired
	private LetterConsentRepository letterConsentRepository;

	@Autowired
	private LetterConsentDetailRepository letterConsentDetailRepository;

	@SuppressWarnings("unchecked")
	public QueryPage<FbLetterConsent> findByConditions(String compilationNo, String customerName,
			String searchType, QueryPage<FbLetterConsent> queryPage,String createUsers,String userGroups,String userAreas,String account) {
		Map<String,Object> params = new HashMap<String,Object>();
		// 0-COMPILATION_NO; 1-CUSTOMER_NAME; 2-AUDIT_STATUS; 3-LETTERID; 
		// 4-CREATE_TIME; 5-APPLY_TIME; 6-fu.USER_NAME; 7-fc.QRY_RESEAN_NAME
		StringBuffer jql = new StringBuffer("select COMPILATION_NO, CUSTOMER_NAME, AUDIT_STATUS, LETTERID, "
		        + "DATE_FORMAT(CREATE_TIME, '%Y-%m-%d %H:%i:%S') as CREATE_TIME, DATE_FORMAT(APPLY_TIME, '%Y-%m-%d %H:%i:%S') as APPLY_TIME,fu.USER_NAME,fc.QRY_RESEAN_NAME from FB_LETTER_CONSENT fc ");
		
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
        
        // 数据过滤
        if ("Z".equals(userRole) || "S".equals(userRole)) {
            jql.append(" INNER JOIN FB_USER fu ON fc.ORIGINATOR_USER = fu.USER_CODE where 1=1 and fc.ORIGINATOR_USER = :createUser ");
            params.put("createUser",user.getUserCode());
        } else if ("C".equals(userRole)) {
            jql.append(" INNER JOIN FB_USER fu ON fc.ORIGINATOR_USER = fu.USER_CODE WHERE 1=1 AND fu.USER_GROUP = :userGroup ");
            params.put("userGroup", userGroup);
			if(StringUtils.isNotBlank(createUsers)){
				jql.append(" and fc.ORIGINATOR_USER=:createUsers");
				params.put("createUsers",createUsers);
			}
        } else if ("A".equals(userRole)) {
            jql.append(" INNER JOIN FB_USER fu ON fc.ORIGINATOR_USER = fu.USER_CODE WHERE 1=1 AND  fu.USER_AREA = :userArea ");
            params.put("userArea",userArea);
			if(StringUtils.isNotBlank(createUsers)){
				jql.append(" and fc.ORIGINATOR_USER=:createUsers");
				params.put("createUsers",createUsers);
			}
			if(StringUtils.isNotBlank(userGroups)){
				jql.append(" and fu.USER_GROUP =:userGroups");
				params.put("userGroups",userGroups);
			}

        } else if ("M".equals(userRole)) {
            jql.append(" INNER JOIN FB_USER fu ON fc.ORIGINATOR_USER = fu.USER_CODE WHERE 1=1  ");

			if(StringUtils.isNotBlank(createUsers)){
				jql.append(" and fc.ORIGINATOR_USER=:createUsers");
				params.put("createUsers",createUsers);
			}
			if(StringUtils.isNotBlank(userGroups)){
				jql.append(" and fu.USER_GROUP =:userGroups");
				params.put("userGroups",userGroups);
			}
			if(StringUtils.isNotBlank(userAreas)){
				jql.append(" and fu.USER_AREA =:userAreas");
				params.put("userAreas",userAreas);
			}
        } else {
            jql.append(" INNER JOIN FB_USER fu ON fc.ORIGINATOR_USER = fu.USER_CODE WHERE 1=1  AND fc.ORIGINATOR_USER = 'error_input_create_user' ");
        }
        
        jql.append(" and AUDIT_STATUS IS NOT NULL AND LENGTH(trim(AUDIT_STATUS)) >= 1 ");
		if(StringUtils.isNoneBlank(account)) {
			jql.append(" and fc.QRY_RESEAN_CODE = :account");
			params.put("account", account);
		}
		if(StringUtils.isNoneBlank(compilationNo)) {
			jql.append(" and fc.COMPILATION_NO = :compilationNo");
			params.put("compilationNo", compilationNo);
		}
		if(StringUtils.isNoneBlank(customerName)) {
			jql.append(" and fc.CUSTOMER_NAME like :customerName");
			String NewCom = "%" + customerName +"%";
			params.put("customerName", NewCom);
		}
		if(StringUtils.isNoneBlank(searchType)) {
			jql.append(" and fc.AUDIT_STATUS =:searchType");
			params.put("searchType", searchType);
		}
		jql.append(" order by fc.APPLY_TIME desc");
		queryPage = findBySQL(jql, queryPage, params);
		return queryPage;
	}

	@SuppressWarnings("unchecked")
	public QueryPage<FbLetterConsentDetail> findDetailByLetterId(String letterId,
																 QueryPage<FbLetterConsentDetail> queryPage) {
		if(StringUtils.isNoneBlank(letterId)) {
			Map<String,Object> params = new HashMap<String,Object>();
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT LCD.LETTER_TYPE, LCD.COMPILATION_NO, LCD.CUSTOMER_NAME, ");
			sql.append("       LCD.ATTACH_ID, LCD.LETTER_DETAIL_ID, LCD.APPROVER_STATUS, ");
			sql.append("       A.FILE_NAME,LCD.CUSTOMER_ID,ifnull(LCD.QRY_RESEAN_NAME,LC.QRY_RESEAN_NAME) ");
			sql.append("  FROM FB_LETTER_CONSENT_DETAIL LCD ");
			sql.append(" inner join FB_LETTER_CONSENT LC ON LCD.LETTERID = LC.LETTERID ");
			sql.append("  left join FB_ATTACHMENT A ON LCD.ATTACH_ID = A.ATTACH_ID ");
			sql.append(" WHERE LCD.LETTERID = :letterId ");
			sql.append(" ORDER BY LCD.LETTER_TYPE,LCD.SHOW_ORDER ASC");
			params.put("letterId", letterId);

			queryPage = findBySQL(sql, queryPage, params);
			return queryPage;
		}else {
			return null;
		}
	}

	public List<FbLetterConsentDetail> findDetailByCompliNo(String compilationNo) {
		return letterConsentDetailRepository.findDetailByCompliNo(compilationNo);
	}
	
	public List<FbLetterConsentDetail> findDetailByCompliNoAndCreatUser(String compilationNo, String createUser) {
		return letterConsentDetailRepository.findDetailByCompliNoAndCreatUser(compilationNo, createUser);
	}

	public FbLetterConsentDetail findByLetterDetailId(String letterDetailId) {
		return letterConsentDetailRepository.findByLetterDetailId(letterDetailId);
	}

	public List<FbLetterConsentDetail> findLetterDetail(String compilationNo, Date measureDate) {
		Map<String,Object> params = new HashMap<String,Object>();
		StringBuffer jql = new StringBuffer("");
		jql.append("select lcd from FbLetterConsent lc ");
		jql.append(" inner join FbLetterConsentDetail lcd on lc.letterId = lcd.letterId");
		jql.append(" where ifnull(lcd.approverStatus,'1') = '2' ");
		jql.append("   and lc.letterId = (select letterId from FbLetterConsent  ");
		jql.append("       where compilationNo=:compNo ");
		params.put("compNo", compilationNo);
		jql.append("         and createTime = ( select max(createTime) from FbLetterConsent ");
		jql.append("       where compilationNo=:compilationNo ");
		jql.append("         and DATE_FORMAT(createTime,'%Y%d%m') = DATE_FORMAT(:measureDate,'%Y%d%m') ");
		jql.append("         and ifnull(auditStatus,'1') = '2')) ");
		params.put("compilationNo", compilationNo);
		params.put("measureDate", measureDate);
		jql.append("   and ifnull(lc.auditStatus,'1') = '2' ");
		jql.append(" order by lcd.letterType asc,lcd.showOrder asc ");
		return findByJQL(jql,params).getContent();
	}
	
	public List<Object[]> findQuestions(String measureId,String questionType) {
		Map<String,Object> params = new HashMap<String,Object>();
		StringBuffer sql = new StringBuffer("SELECT FQ.QUESTION_NAME,FQ.QUESTUIB_ANSWER FROM  (");
		sql.append("SELECT SUBSTRING_INDEX(GROUP_CONCAT(QUESTUIB_ANSWER ORDER BY CREATE_TIME DESC),',',1) AS QUESTUIB_ANSWER,");
		sql.append("SUBSTRING_INDEX(GROUP_CONCAT(QUESTION_ID ORDER BY CREATE_TIME DESC),',',1) AS QUESTION_ID,");
		sql.append("SUBSTRING_INDEX(GROUP_CONCAT(MEASURE_ID ORDER BY CREATE_TIME DESC),',',1) AS MEASURE_ID,");
		sql.append("SUBSTRING_INDEX(GROUP_CONCAT(QUESTION_TYPE ORDER BY CREATE_TIME DESC),',',1) AS QUESTION_TYPE, ");
		sql.append("SUBSTRING_INDEX(GROUP_CONCAT(QUESTION_NAME ORDER BY CREATE_TIME DESC),',',1) AS QUESTION_NAME ");
		sql.append("FROM FB_QUESTIONRECORD WHERE MEASURE_ID =:measureId GROUP BY QUESTION_ID) FQ LEFT JOIN FB_QUESTIONNAIRE_DETAIL FQD ON FQ.QUESTION_ID = FQD.ID ");
		sql.append(" LEFT JOIN FB_QUESTIONNAIRE A ON FQD.QUESTIONNAIRE_ID = A.ID");
	    sql.append(" WHERE FQ.QUESTION_TYPE IN (:questionType)");
	    sql.append(" ORDER BY A.QUESTIONNAIRE_TYPE ASC, (FQD.SORT_NO + 0) ASC");
	    params.put("measureId", measureId);
	    List<String> paramList = new ArrayList<String>();
	    paramList.add(questionType);
	    //產品需查詢固定問卷
	    if("02".equals(questionType)) {
	      paramList.add("00");
	    }
	    params.put("questionType", paramList);
		return findBySQL(sql, params).getContent();
	}
}
