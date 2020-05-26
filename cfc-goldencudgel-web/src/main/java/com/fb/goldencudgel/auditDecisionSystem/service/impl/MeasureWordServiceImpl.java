package com.fb.goldencudgel.auditDecisionSystem.service.impl;

import com.comwave.core.data.domain.QueryPage;
import com.comwave.core.jpa.BaseJpaDAO;
import com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.FbMeasureWord;
import com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.question.FbQuestionnaire;
import com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.question.FbQuestionnaireDetail;
import com.fb.goldencudgel.auditDecisionSystem.model.User;
import com.fb.goldencudgel.auditDecisionSystem.repository.MeasureWordRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.QuestionnaireRepository;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class MeasureWordServiceImpl extends BaseJpaDAO{
    
	@Autowired
	private MeasureWordRepository measureWordRepository;

	@Autowired
	private QuestionnaireRepository questionnaireRepository;

	@SuppressWarnings("unchecked")
	public QueryPage<Object[]> findByConditions(String compilationNo, String customerName, Date startDate, Date endDate,
														 QueryPage<Object[]> queryPage , String createUsers, String userGroups, String userAreas,int opcount,int qucount,String productName) {
		Map<String,Object> params = new HashMap<String,Object>();
		StringBuffer sb = new StringBuffer();
		// 0-COMPILATION_NO; 1-CUSTOMER_NAME; 2-ITEM_NAME
		// 3-MEASURE_DATE; 4-MEASURE_RESULT; 5-SOLUTION; 6-fmw.MEASURE_ID; 7-fu.USER_NAME
		// 8-ORES_COUNT; 9-FQST_COUNT; 10-fmw.ZJX_ID; 11-JCIC_COUNT; 12-fmw.ESTIMATE_AMTTOUNT
		// 13-MSG; 14-fmw.PROD_CODE; 15-fu.USER_CODE; 16-fmw.TRAND_ID; 17-fmw.ZJX_TIMER_RESULT
		sb.append(" SELECT fmw.COMPILATION_NO AS COMPILATION_NO,CUSTOMER_NAME,VDDI.ITEM_NAME AS ITEM_NAME, ");
		sb.append(" 	   DATE_FORMAT(MEASURE_DATE,'%Y-%m-%d %H:%i') AS MEASURE_DATE, ");
		sb.append(" 	   MEASURE_RESULT,SOLUTION,fmw.MEASURE_ID,fu.USER_NAME, ");
		sb.append(" 	   (CASE WHEN IFNULL(T.ORES_COUNT,0) > 0 THEN '是' else '否' end ) ORES_COUNT , ");
		sb.append(" 	   (CASE WHEN IFNULL(T2.FQST_COUNT,0) > 0 THEN '是' else '否' end ) FQST_COUNT, ");
		sb.append(" 	   fmw.ZJX_ID,count(JCIC.JCIC_ID) as JCIC_COUNT,fmw.ESTIMATE_AMTTOUNT,fmw.ZJX_TIMER_MSG as MSG,fmw.PROD_CODE ");
		sb.append("      , fu.USER_CODE "); // 12 -> 15
		sb.append("      , fmw.TRAND_ID "); // 13 -> 16
		sb.append("      , fmw.ZJX_TIMER_RESULT "); // 14 -> 17
		sb.append("   FROM FB_MEASURE_WORD fmw ");
		sb.append("   LEFT JOIN FB_VISIT_COMPANY_DETAIL FVCD ON FVCD.COMPILATION_NO = fmw.COMPILATION_NO AND fmw.TRAND_ID = FVCD.TRAND_ID ");
		sb.append("   LEFT JOIN VIEW_DATA_DICT_ITEM VDDI ON FVCD.COM_INDUSTRY = VDDI.ITEM_CODE AND VDDI.DICT_ID = 'BUSINESS_TYPE' ");
		sb.append("   LEFT JOIN (SELECT MEASURE_ID,COUNT(1) AS ORES_COUNT FROM OPER_REPORT_SEND_RECORD  GROUP BY MEASURE_ID) T ON fmw.MEASURE_ID= T.MEASURE_ID ");
		sb.append("   LEFT JOIN (SELECT MEASURE_ID,COUNT(1) AS FQST_COUNT FROM FB_QUESTIONRECORD FQST WHERE FQST.QUESTION_TYPE='02' GROUP BY MEASURE_ID) T2 ON T2.MEASURE_ID = fmw.MEASURE_ID ");
		sb.append("   LEFT JOIN FB_ZJX_JCIC JCIC ON JCIC.ZJX_ID = fmw.ZJX_ID ");
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
            sb.append(" INNER JOIN FB_USER fu ON fmw.CREATE_USER = fu.USER_CODE WHERE 1=1 and fmw.CREATE_USER = :createUser ");
            params.put("createUser",user.getUserCode());
        } else if ("C".equals(userRole)) {
            sb.append(" INNER JOIN FB_USER fu ON fmw.CREATE_USER = fu.USER_CODE WHERE 1=1 AND fu.USER_GROUP = :userGroup ");
            params.put("userGroup", userGroup);
			if(StringUtils.isNotBlank(createUsers)){
				sb.append(" and fmw.CREATE_USER=:createUsers");
				params.put("createUsers",createUsers);
			}
        } else if ("A".equals(userRole)) {
            sb.append(" INNER JOIN FB_USER fu ON fmw.CREATE_USER = fu.USER_CODE WHERE 1=1 AND fu.USER_AREA = :userArea ");
            params.put("userArea",userArea);
			if(StringUtils.isNotBlank(createUsers)){
				sb.append(" and fmw.CREATE_USER=:createUsers");
				params.put("createUsers",createUsers);
			}
			if(StringUtils.isNotBlank(userGroups)){
				sb.append(" and fu.USER_GROUP =:userGroups");
				params.put("userGroups",userGroups);
			}
        } else if ("M".equals(userRole)){
            sb.append(" INNER JOIN FB_USER fu ON fmw.CREATE_USER = fu.USER_CODE WHERE 1=1  ");
			if(StringUtils.isNotBlank(createUsers)){
				sb.append(" and fmw.CREATE_USER=:createUsers");
				params.put("createUsers",createUsers);
			}
			if(StringUtils.isNotBlank(userGroups)){
				sb.append(" and fu.USER_GROUP =:userGroups");
				params.put("userGroups",userGroups);
			}
			if(StringUtils.isNotBlank(userAreas)){
				sb.append(" and fu.USER_AREA =:userAreas");
				params.put("userAreas",userAreas);
			}
        } else {
            sb.append(" INNER JOIN FB_USER fu ON fmw.CREATE_USER = fu.USER_CODE ");
            sb.append(" WHERE 1=1 AND fmw.CREATE_USER = 'error_input_create_user' ");
        }
		sb.append(" AND fmw.QUESTION_TYPE is null");
		if(StringUtils.isNoneBlank(compilationNo)) {
			sb.append(" AND fmw.COMPILATION_NO = :compilationNo");
			params.put("compilationNo", compilationNo);
		}
		if(StringUtils.isNoneBlank(customerName)) {
			sb.append(" AND CUSTOMER_NAME like :customerName");
			String NewCom = "%" + customerName +"%";
			params.put("customerName", NewCom);
		}
		if(startDate != null){
			sb.append(" AND MEASURE_DATE >= :startDate");
		      params.put("startDate", startDate);
		}
		if(endDate != null){
			sb.append(" AND MEASURE_DATE <= :endDate");
		    params.put("endDate", endDate);
		}
		if (opcount==2){
			sb.append(" and IFNULL(T.ORES_COUNT,0)>0");
		}
		if (opcount==1){
			sb.append(" and IFNULL(T.ORES_COUNT,0)=0");
		}
		if (qucount==2){
			sb.append(" and IFNULL(T2.FQST_COUNT,0)>0");
		}
		if (qucount==1){
			sb.append(" and IFNULL(T2.FQST_COUNT,0)=0");
		}
		//测试时先关闭，没有数据
		if(StringUtils.isNotBlank(productName)){
			sb.append(" and IFNULL(fmw.PROD_CODE,'P0001') =:productName");
			params.put("productName",productName);
		}
		sb.append(" group by fmw.MEASURE_ID ");
		sb.append(" order by fmw.MEASURE_DATE desc");

		queryPage = findBySQL(sb, queryPage, params);
	    return queryPage;
	}
	public FbMeasureWord findById(String measureId) {
		return measureWordRepository.findByMeasureId(measureId);
	}


	public Long queryOpCount(String cutNo, QueryPage<Object[]> queryPage) {
		StringBuffer sql = new StringBuffer(" ");
		Map<String,Object> params = new HashMap<String,Object>();
		sql.append("SELECT RECORD_ID FROM OPER_REPORT_SEND_RECORD WHERE COMP_CODE=:cutNo");
		params.put("cutNo", cutNo);
		queryPage = findBySQL(sql, queryPage, params);
		return queryPage.getTotalElements();
	}

	public Long queryQtCount(String cutNo, QueryPage<Object[]> queryPage) {
		StringBuffer sql = new StringBuffer(" ");
		Map<String,Object> params = new HashMap<String,Object>();
		sql.append("SELECT ID FROM FB_QUESTIONRECORD WHERE CUS_CODE=:cutNo");
		params.put("cutNo", cutNo);
		queryPage = findBySQL(sql, queryPage, params);
		return queryPage.getTotalElements();
	}

	public ArrayList<ArrayList<String>> getMeasureLevelData(List<String> questionList) {
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		StringBuilder sql = new StringBuilder();
		sql.append(" select fu.USER_AREA_NAME,fu.USER_GROUP_NAME,fu.USER_NAME,fmw.CREATE_USER,fmw.COMPILATION_NO,flc.COM_NAME,fmw.MEASURE_RESULT, ");
		sql.append(" 	    ifnull(ar.C_GRE_PAYMENT_AMT,0)+ifnull(ar.C_REPAYMENT_AMT,0)+ifnull(ar.CG_CYCLE_AMT,0)+ifnull(ar.C_CYCLE_AMT,0) as amount,date_format(fmw.REPLY_END_TIME,'%Y/%m/%d'),");
		for(int i =0,len = questionList.size();i<len;i++) {
			sql.append(" substring_index(group_concat(case when locate('"+ questionList.get(i).replaceAll("\\?","") +"',fq.QUESTION_NAME) >0 then if(fq.QUESTUIB_ANSWER='','',concat(fq.QUESTUIB_ANSWER,',')) else '' end order by fq.CREATE_TIME desc separator ''),',',1)");
			sql.append(i == len - 1 ? " " : ",");
		}
		sql.append("   from FB_MEASURE_WORD fmw ");
		sql.append("   left join FB_LOAN_COMPANY flc on flc.COMPILATION_NO = fmw.COMPILATION_NO ");
		sql.append("   left join FB_USER fu on fu.USER_CODE = fmw.CREATE_USER ");
		sql.append("   left join AMOUNT_RECORD ar on ar.MEASURD_ID = fmw.MEASURE_ID ");
		sql.append("   left join FB_QUESTIONRECORD fq on fq.MEASURE_ID = fmw.MEASURE_ID and fq.QUESTION_TYPE in ('02','03') ");
		sql.append("  where date_format(fmw.REPLY_END_TIME,'%Y%m')  = date_format(CURDATE(),'%Y%m') ");
		sql.append("  group by fq.MEASURE_ID");

		List<Object[]> content = findBySQL(sql).getContent();
		for(Object[] ob : content ) {
			ArrayList<String> rowData = new ArrayList<String>();
			for(int i=0 , len = 8 + questionList.size(); i<= len ;i++) {
				rowData.add(ob[i] == null ? "" : ob[i].toString());
			}
			data.add(rowData);
		}
		return data;
	}

	public ArrayList<ArrayList<String>> getMeasureProdData(List<String> questionList) {
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		StringBuilder sql = new StringBuilder();
		sql.append(" select fu.USER_AREA_NAME,fu.USER_GROUP_NAME,fu.USER_NAME,fmw.CREATE_USER,fmw.COMPILATION_NO,flc.COM_NAME,fmw.MEASURE_RESULT, ");
		sql.append(" 		ifnull(ar.C_GRE_PAYMENT_AMT,0)+ifnull(ar.C_REPAYMENT_AMT,0)+ifnull(ar.CG_CYCLE_AMT,0)+ifnull(ar.C_CYCLE_AMT,0) as amount,date_format(fmw.MEASURE_DATE,'%Y/%m/%d'),");
		for(int i =0,len = questionList.size();i<len;i++) {
			sql.append(" substring_index(group_concat(case when locate('"+ questionList.get(i).replaceAll("\\?","") +"',fq.QUESTION_NAME) >0 then if(fq.QUESTUIB_ANSWER='','',concat(fq.QUESTUIB_ANSWER,',')) else '' end order by fq.CREATE_TIME desc separator ''),',',1)");
			sql.append(i == len - 1 ? " " : ",");
		}
		sql.append("   from FB_MEASURE_WORD fmw  ");
		sql.append("   left join FB_LOAN_COMPANY flc on flc.COMPILATION_NO = fmw.COMPILATION_NO ");
		sql.append("   left join FB_USER fu on fu.USER_CODE = fmw.CREATE_USER ");
		sql.append("   left join AMOUNT_RECORD ar on ar.MEASURD_ID = fmw.MEASURE_ID ");
		sql.append("   left join FB_QUESTIONRECORD fq on fq.MEASURE_ID = fmw.MEASURE_ID and fq.QUESTION_TYPE in ('02','03') ");
		sql.append("  where date_format(fmw.MEASURE_DATE,'%Y%m')  = date_format(CURDATE(),'%Y%m') ");
		sql.append("    and ifnull(fmw.QUESTION_TYPE,'') != '04' ");
		sql.append("  group by fq.MEASURE_ID");
		List<Object[]> content = findBySQL(sql).getContent();
		for(Object[] ob : content ) {
			ArrayList<String> rowData = new ArrayList<String>();
			for(int i=0 , len = 8 + questionList.size(); i<= len ;i++) {
				rowData.add(ob[i] == null ? "" : ob[i].toString());
			}
			data.add(rowData);
		}
		return data;
	}

	public List<String> getQuestionName(String questionType) {
		List<String> questionList = new ArrayList<String>();
		FbQuestionnaire questionnaire = questionnaireRepository.findByType(questionType);
		if(questionnaire != null && questionnaire.getDetails() != null) {
			List<FbQuestionnaireDetail> questionnaireDetails = questionnaire.getDetails();
			Collections.sort(questionnaireDetails, new Comparator<FbQuestionnaireDetail>() {
				@Override
				public int compare(FbQuestionnaireDetail o1, FbQuestionnaireDetail o2) {
					return Integer.valueOf(o1.getSortNo()).compareTo(Integer.valueOf(o2.getSortNo()));
				}
			});
			questionList = questionnaire.getDetails().stream().map(FbQuestionnaireDetail::getName).collect(Collectors.toList());
		}
		return questionList;
	}
}
