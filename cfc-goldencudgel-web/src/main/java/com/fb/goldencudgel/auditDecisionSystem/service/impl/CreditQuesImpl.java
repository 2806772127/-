package com.fb.goldencudgel.auditDecisionSystem.service.impl;

import com.comwave.core.data.domain.QueryPage;
import com.comwave.core.jpa.BaseJpaDAO;
import com.fb.goldencudgel.auditDecisionSystem.controller.bwCe.SaveCreditReportController;
import com.fb.goldencudgel.auditDecisionSystem.dao.FileContextDao;
import com.fb.goldencudgel.auditDecisionSystem.domain.LoanCompany.FbLoanCompany;
import com.fb.goldencudgel.auditDecisionSystem.domain.appointMentRecord.FbAppointmentRecord;
import com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.FbMeasureWord;
import com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.FbQuestionRecord;
import com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.question.FbQuestionnaire;
import com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.question.FbQuestionnaireAnswer;
import com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.question.FbQuestionnaireDetail;
import com.fb.goldencudgel.auditDecisionSystem.mapper.FileContext;
import com.fb.goldencudgel.auditDecisionSystem.model.User;
import com.fb.goldencudgel.auditDecisionSystem.repository.*;
import com.fb.goldencudgel.auditDecisionSystem.schema.getSchedulerBaseInfo.GetSchedulerBaseInfoRq;
import com.fb.goldencudgel.auditDecisionSystem.schema.getSchedulerBaseInfo.GetSchedulerBaseInfoRs;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;

@Service
public class CreditQuesImpl extends BaseJpaDAO {

    private static final Logger logger = LoggerFactory.getLogger(CreditQuesImpl.class);

    @Autowired
    private FbQuestionRecordRepository fbQuestionRecordRepository;

    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @Autowired
    private QuestionnaireDetailRepository questionnaireDetailRepository;

    @Autowired
    private MeasureWordRepository measureWordRepository;

    @Autowired
    private SaveCreditReportServiceImpl saveCreditReportService;

    @Autowired
    private FileContextDao fileContextDao;

    @Autowired
    private LoanCompanyRepository loanCompanyRepository;

    @Autowired
    private AppointmentRecordRepository appointmentRecordRepository;

    @Autowired
    private InterfaceServiceImpl interfaceService;

    public QueryPage<Object[]> findByConditions(String compilationNo, String companyName, String questionnaireName, String startDate, String endDate,String creditName,
                                                QueryPage<Object[]> queryPage) {
        User user = UserUtil.getCurrUser();
        Map<String, Object> userparam = new HashMap<>();
        StringBuffer usersql = new StringBuffer("select USER_NAME,USER_AREA,USER_GROUP,USER_OFFICE,USER_TYPE from FB_USER WHERE 1=1 ");
        usersql.append(" and USER_CODE=:usercode");
        userparam.put("usercode", user.getUserCode());
        List<Object[]> userinfos = findBySQL(usersql, userparam).getContent();
        String userRole = "ERROR";
        String userGroup = null;
        String userArea = null;
        if (userinfos != null && userinfos.size() > 0) {
            userRole = userinfos.get(0)[4] + "";
            userGroup = user.getUserGroup() == null ? (userinfos.get(0)[2] + "") : user.getUserGroup();
            userArea = user.getUserArea() == null ? (userinfos.get(0)[1] + "") : user.getUserArea();

        }

        Map<String, Object> userparams = new HashMap<>();
        StringBuffer usersqls = new StringBuffer("select USER_CODE from FB_USER_AGENT WHERE AGENT_CODE=:usercode and ( now() BETWEEN AGENT_START_DATE and AGENT_END_DATA)");
        userparams.put("usercode", user.getUserCode());
        List<String > usercodes = findBySQL(usersqls, userparams).getContent();
        StringBuffer usercode = new StringBuffer("");
        if (usercodes.size()>0){
            usercode.append(",");
            for (int i = 0; i < usercodes.size(); i++) {

                usercode.append("'"+usercodes.get(i)+"'");
                if (i!=usercodes.size()-1){
                    usercode.append(",");
                }else {

                }
            }
        }

        Map<String, Object> params = new HashMap<String, Object>();

        /* StringBuffer sql = new StringBuffer(" SELECT MW.MEASURE_ID, MW.COMPILATION_NO, AR.COMPILATION_NAME, QN.NAME,DATE_FORMAT(MW.MEASURE_DATE, '%Y-%m-%d') AS MEASURE_DATE, DATE_FORMAT(MW.UPDATA_DATE, '%Y-%m-%d') AS UPDATA_DATE  FROM FB_MEASURE_WORD MW LEFT JOIN FB_APPOINTMENT_RECORD AR ON AR.COMPILATION_NO = MW.COMPILATION_NO LEFT JOIN FB_QUESTIONRECORD QC ON QC.MEASURE_ID = MW.MEASURE_ID LEFT JOIN FB_QUESTIONNAIRE_DETAIL QD ON QD.ID = QC.QUESTION_ID LEFT JOIN FB_QUESTIONNAIRE  QN ON QN.ID = QD.QUESTIONNAIRE_ID ");*/
        StringBuffer sql = new StringBuffer();
        // 0-MW.MEASURE_ID; 1-MW.COMPILATION_NO; 2-MW.CUSTOMER_NAME
        // 3-TEMP.NAME; 4-MEASURE_DATE; 5-UPDATA_DATE; 6-FU.USER_NAME
        // 7-LAST_FLAG; 8-T3.QUESTIONNAIRE_TYPE; 9-T3.DESCRIPTION
        // 10-TEMP_FLAG; 11-FU.USER_CODE; 12-STATUS; 13-MW.CREATE_USER
        sql.append(" SELECT DISTINCT MW.MEASURE_ID,MW.COMPILATION_NO,MW.CUSTOMER_NAME, ");
        sql.append(" 		TEMP.NAME, ");
        sql.append(" 		DATE_FORMAT(MW.MEASURE_DATE, '%Y-%m-%d %H:%i') AS MEASURE_DATE, ");
        sql.append(" 		DATE_FORMAT(MW.UPDATA_DATE, '%Y-%m-%d %H:%i') AS UPDATA_DATE, ");
        sql.append(" 		FU.USER_NAME, ");
        sql.append("        CASE WHEN far_max.APPOINTMENT_ID!='' THEN '1' ELSE '0' END AS LAST_FLAG,");
        //sql.append(" 		(CASE WHEN DATE_FORMAT(T3.MEASURE_DATE, '%Y-%m-%d %H:%i') = DATE_FORMAT(MW.MEASURE_DATE, '%Y-%m-%d %H:%i') THEN '1' ELSE '0' END ) as LATEST_TIME,T3.QUESTIONNAIRE_TYPE,T3.DESCRIPTION ,( CASE WHEN MW.TEMP_FLAG='01'   THEN '1' ELSE '0' END ) AS TEMP_FLAG,FU.USER_CODE ,");
        sql.append("        T3.QUESTIONNAIRE_TYPE,T3.DESCRIPTION ,( CASE WHEN MW.TEMP_FLAG='01'   THEN '1' ELSE '0' END ) AS TEMP_FLAG,FU.USER_CODE ,");
        sql.append("   (CASE WHEN MW.CREATE_USER in   ('0'"+ usercode+") THEN '1'  ELSE '0' END ) as STATUS ,MW.CREATE_USER  FROM FB_MEASURE_WORD MW  ");
        sql.append("  LEFT JOIN (SELECT * FROM (SELECT DISTINCT MWW.COMPILATION_NO,MWW.MEASURE_DATE, QNN.QUESTIONNAIRE_TYPE, QNN.DESCRIPTION  ");
        sql.append(" 							 FROM FB_MEASURE_WORD MWW LEFT JOIN FB_QUESTIONRECORD QCC ON QCC.MEASURE_ID = MWW.MEASURE_ID  ");
        sql.append(" 							  AND QCC.QUESTION_TYPE IN ('04','05','06','07','08','09','10','11','12','13') ");
        sql.append(" 							INNER JOIN FB_QUESTIONNAIRE QNN ON QNN.QUESTIONNAIRE_TYPE = QCC.QUESTION_TYPE AND QNN.IS_ENABLE = '1'");
        sql.append(" 							INNER JOIN FB_USER FUU ON MWW.CREATE_USER = FUU.USER_CODE ORDER BY MWW.MEASURE_DATE DESC) a  ");
        sql.append(" 							GROUP BY COMPILATION_NO ORDER BY a.MEASURE_DATE) T3 ON T3.COMPILATION_NO=MW.COMPILATION_NO ");
        sql.append(" left join (select DISTINCT fqn.NAME,fqr.MEASURE_ID from FB_QUESTIONRECORD fqr  ");
        sql.append(" 						inner join FB_QUESTIONNAIRE fqn on fqr.QUESTION_TYPE = fqn.QUESTIONNAIRE_TYPE AND fqn.IS_ENABLE = '1' ");
        sql.append(" 						where (fqr.QUESTION_TYPE,fqr.MEASURE_ID) in (select max(QUESTION_TYPE),MEASURE_ID from FB_QUESTIONRECORD  ");
        sql.append(" 												 where QUESTION_TYPE in ('04','07','08','09','10','11','12') group by MEASURE_ID)) TEMP ");
        sql.append(" 	on TEMP.MEASURE_ID = MW.MEASURE_ID");

        sql.append(" INNER JOIN FB_USER FU ON MW.CREATE_USER = FU.USER_CODE ");
        sql.append(" LEFT JOIN (");
        sql.append(" select t1.APPOINTMENT_ID from FB_APPOINTMENT_RECORD t1 ");
        sql.append(" where t1.CREATE_TIME = ( ");
        sql.append(" SELECT max(t2.CREATE_TIME) from FB_APPOINTMENT_RECORD t2 where t2.APPOINTMENT_TYPE='2' and t1.COMPILATION_NO=t2.COMPILATION_NO ");
        sql.append(" ) GROUP BY t1.COMPILATION_NO ");
        sql.append(" ) far_max ON far_max.APPOINTMENT_ID=MW.SCHEDULER_ID ");
        sql.append(" WHERE MW.QUESTION_TYPE = '04' ");

      /*  if ("Z".equals(user.getUserType()) || "S".equals(user.getUserType())) {
            sql.append(" and MW.CREATE_USER=:createUser ");
            params.put("createUser", user.getUserCode());
        } else if ("C".equals(user.getUserType())) {
            sql.append(" AND USER_GROUP =:userGroup ");
            params.put("userGroup", user.getUserGroup());
        } else if ("A".equals(user.getUserType())) {
            sql.append(" INNER JOIN FB_USER FU ON MW.CREATE_USER = FU.USER_CODE WHERE 1=1 AND  USER_AREA =:userArea ");
            params.put("userArea", user.getUserArea());
        }*/

  /*      sql.append(" AND MW.CREATE_USER=:usercode");
        params.put("usercode",user.getUserCode());*/

        if (StringUtils.isNoneBlank(compilationNo)) {
            sql.append(" AND MW.COMPILATION_NO = :compilationNo ");
            params.put("compilationNo", compilationNo);
        }
        if (StringUtils.isNoneBlank(companyName)) {
            sql.append(" AND MW.CUSTOMER_NAME like :companyName ");
            params.put("companyName", "%" + companyName + "%");
        }
        if (StringUtils.isNoneBlank(questionnaireName)) {
            sql.append(" AND TEMP.NAME = :quesName ");
            params.put("quesName", questionnaireName);
        }
        if (StringUtils.isNoneBlank(startDate)) {
            sql.append(" AND MW.MEASURE_DATE >= :startDate ");
            params.put("startDate", startDate);
        }
        if (StringUtils.isNoneBlank(endDate)) {
            sql.append(" AND MW.MEASURE_DATE <= :endDate ");
            params.put("endDate", endDate);
        }
        if (StringUtils.isNoneBlank(creditName)) {
            sql.append(" AND FU.USER_NAME like :creditName ");
            params.put("creditName", "%" + creditName + "%");
        }
       // sql.append(" AND MW.STATUS = 1 ");

        /* sql.append(" ORDER BY MW.COMPILATION_NO DESC");*/
        sql.append(" ORDER BY MW.UPDATA_DATE DESC");
        queryPage = findBySQL(sql, queryPage, params);
        return queryPage;
    }

    public QueryPage<Object[]> findByConditionId(String compilationNo, String companyName, String questionnaireName, String startDate, String endDate,String creditName,
                                                QueryPage<Object[]> queryPage) {
        Map<String, Object> params = new HashMap<String, Object>();

        /* StringBuffer sql = new StringBuffer(" SELECT MW.MEASURE_ID, MW.COMPILATION_NO, AR.COMPILATION_NAME, QN.NAME,DATE_FORMAT(MW.MEASURE_DATE, '%Y-%m-%d') AS MEASURE_DATE, DATE_FORMAT(MW.UPDATA_DATE, '%Y-%m-%d') AS UPDATA_DATE  FROM FB_MEASURE_WORD MW LEFT JOIN FB_APPOINTMENT_RECORD AR ON AR.COMPILATION_NO = MW.COMPILATION_NO LEFT JOIN FB_QUESTIONRECORD QC ON QC.MEASURE_ID = MW.MEASURE_ID LEFT JOIN FB_QUESTIONNAIRE_DETAIL QD ON QD.ID = QC.QUESTION_ID LEFT JOIN FB_QUESTIONNAIRE  QN ON QN.ID = QD.QUESTIONNAIRE_ID ");*/
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT count(DISTINCT MW.MEASURE_ID) FROM ");
        sql.append(" FB_MEASURE_WORD MW ");
        sql.append(" left join (select DISTINCT fqn.NAME,fqr.MEASURE_ID from FB_QUESTIONRECORD fqr  ");
        sql.append(" 						inner join FB_QUESTIONNAIRE fqn on fqr.QUESTION_TYPE = fqn.QUESTIONNAIRE_TYPE  ");
        sql.append(" 						where (fqr.QUESTION_TYPE,fqr.MEASURE_ID) in (select max(QUESTION_TYPE),MEASURE_ID from FB_QUESTIONRECORD  ");
        sql.append(" 												 where QUESTION_TYPE in ('04','07','08','09','10','11','12') group by MEASURE_ID)) TEMP ");
        sql.append(" 	on TEMP.MEASURE_ID = MW.MEASURE_ID");
        sql.append(" INNER JOIN FB_USER FU ON MW.CREATE_USER = FU.USER_CODE WHERE MW.QUESTION_TYPE = '04' ");
        User user = UserUtil.getCurrUser();

        if ("Z".equals(user.getUserType()) || "S".equals(user.getUserType())) {
            sql.append(" and MW.CREATE_USER=:createUser ");
            params.put("createUser", user.getUserCode());
        } else if ("C".equals(user.getUserType())) {
            sql.append(" AND USER_GROUP =:userGroup ");
            params.put("userGroup", user.getUserGroup());
        } else if ("A".equals(user.getUserType())) {
            sql.append(" INNER JOIN FB_USER FU ON MW.CREATE_USER = FU.USER_CODE WHERE 1=1 AND  USER_AREA =:userArea ");
            params.put("userArea", user.getUserArea());
        }
  /*      sql.append(" AND MW.CREATE_USER=:usercode");
        params.put("usercode",user.getUserCode());*/

        if (StringUtils.isNoneBlank(compilationNo)) {
            sql.append(" AND MW.COMPILATION_NO = :compilationNo ");
            params.put("compilationNo", compilationNo);
        }
        if (StringUtils.isNoneBlank(companyName)) {
            sql.append(" AND MW.CREATE_USER like :companyName ");
            params.put("companyName", "%" + companyName + "%");
        }
        if (StringUtils.isNoneBlank(questionnaireName)) {
            sql.append(" AND TEMP.NAME = :quesName ");
            params.put("quesName", questionnaireName);
        }
        if (StringUtils.isNoneBlank(startDate)) {
            sql.append(" AND MW.MEASURE_DATE >= :startDate ");
            params.put("startDate", startDate);
        }
        if (StringUtils.isNoneBlank(endDate)) {
            sql.append(" AND MW.MEASURE_DATE <= :endDate ");
            params.put("endDate", endDate);
        }
        if (StringUtils.isNoneBlank(creditName)) {
            sql.append(" AND FU.USER_NAME like :creditName ");
            params.put("creditName", "%" + creditName + "%");
        }
        /* sql.append(" ORDER BY MW.COMPILATION_NO DESC");*/
        //sql.append(" AND MW.STATUS = 1 ");
        sql.append(" ORDER BY MW.MEASURE_DATE DESC");
        queryPage = findBySQL(sql, queryPage, params);
        return queryPage;
    }

    public QueryPage<Object[]> findByMeasureId(String measureId) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer();
//        sql.append(" SELECT A.QUESTION_NAME ");
//        sql.append(" ,CASE WHEN A.IS_NUM=0 THEN FORMAT(A.QUESTUIB_ANSWER,0) ELSE A.QUESTUIB_ANSWER END QUESTUIB_ANSWER ");
//        sql.append(" ,A.SORT_NO,A.QUESTION_TYPE ");
//        sql.append(" FROM ( ");
        sql.append(" SELECT T.QUESTION_NAME ");
        sql.append(" ,CASE WHEN INSTR(T.QUESTION_NAME,'仟元')>0 THEN FORMAT(T.QUESTUIB_ANSWER,0) ELSE T.QUESTUIB_ANSWER END QUESTUIB_ANSWER ");
//        sql.append(" ,(TRIM(T.QUESTUIB_ANSWER) REGEXP '[^0-9]') IS_NUM ");
        sql.append(" ,CONVERT(D.SORT_NO,SIGNED) AS SORT_NO ");
        sql.append(" ,T.QUESTION_TYPE ");
        sql.append(" FROM ");
        sql.append(" FB_QUESTIONRECORD AS T ");
        sql.append(" INNER JOIN FB_QUESTIONNAIRE_DETAIL AS D ON T.QUESTION_ID = D.ID ");
        sql.append(" WHERE T.MEASURE_ID = :measureId ");
        sql.append(" ORDER BY FIELD(T.QUESTION_TYPE,'04','07','08','09','10','11','12','05','06'),SORT_NO ");
//        sql.append(" ) A ");
        params.put("measureId", measureId);
        QueryPage page = findBySQL(sql, params);
        return page;
    }
    public QueryPage<Object[]> findByMId(String measureId) {
        Map<String, Object> params = new HashMap<String, Object>();
        /*StringBuffer sql = new StringBuffer("SELECT DISTINCT t1.COMPILATION_NO,t.CUS_CODE,t2.NAME,t1.MEASURE_DATE,t1.UPDATA_DATE,t2.DESCRIPTION FROM FB_QUESTIONRECORD t LEFT JOIN FB_MEASURE_WORD t1 ON t.MEASURE_ID=t1.MEASURE_ID LEFT JOIN FB_QUESTIONNAIRE t2 ON t2.QUESTIONNAIRE_TYPE=t.QUESTION_TYPE WHERE t.MEASURE_ID = :measureId AND t.QUESTION_TYPE IN ('07','08','09','10','11','12');");*/
        StringBuffer sql = new StringBuffer("SELECT DISTINCT t1.COMPILATION_NO,t.CUS_CODE,t2.NAME,t1.MEASURE_DATE,t1.UPDATA_DATE,t2.DESCRIPTION,FU.USER_NAME FROM FB_QUESTIONRECORD t LEFT JOIN FB_MEASURE_WORD t1 ON t.MEASURE_ID=t1.MEASURE_ID LEFT JOIN FB_QUESTIONNAIRE t2 ON t2.QUESTIONNAIRE_TYPE=t.QUESTION_TYPE LEFT JOIN FB_USER FU ON FU.USER_CODE = t1.CREATE_USER WHERE t.MEASURE_ID = :measureId AND t.QUESTION_TYPE IN ('07','08','09','10','11','12');");

        params.put("measureId", measureId);
        QueryPage page1 = findBySQL(sql, params);
        return page1;
    }

    public List<FbQuestionRecord> findRecordByMeasureId(String MeasureId){
        return fbQuestionRecordRepository.findByMeasureId(MeasureId);
    }
    
    public FbQuestionnaire findQuestionnaireByType(String questionType){
	    return fbQuestionRecordRepository.findByQuestionType(questionType);
    }
    
   
    public String findQuestionTypeByType(String questionnaireType){
	    return fbQuestionRecordRepository.findQuestionTypeByType(questionnaireType);
    }
    
    public String findQuestionTypeByCompilationNo(String compilationNo){
	    return fbQuestionRecordRepository.findQuestionTypeByCompilationNo(compilationNo);
    }

       public String findIndustryNameByType(String questionnaireType){
	    return fbQuestionRecordRepository.findIndustryNameByType(questionnaireType);
    }

    public String findQuestionType(String measureId){
        List<String> questionTypeList = fbQuestionRecordRepository.findQuestionType(measureId);
        if(questionTypeList != null && questionTypeList.size()>0){
            return questionTypeList.get(0);
        }
        return "";
    }
    
    /**
     * 获取征信问卷信息
     * @param measureId
     * @return
     */
    public QueryPage<Object[]> findQuestionMsgByMId(String measureId) {
        Map<String, Object> params = new HashMap<String, Object>();
        // 现在数据库没有征信问卷数据，测试时type加上 03  否则显示不了
        /*StringBuffer sql = new StringBuffer("SELECT DISTINCT t1.COMPILATION_NO,t.CUS_CODE,t2.NAME,t1.MEASURE_DATE,t1.UPDATA_DATE,t2.DESCRIPTION,t.QUESTION_TYPE FROM FB_QUESTIONRECORD t LEFT JOIN FB_MEASURE_WORD t1 ON t.MEASURE_ID=t1.MEASURE_ID LEFT JOIN FB_QUESTIONNAIRE t2 ON t2.QUESTIONNAIRE_TYPE=t.QUESTION_TYPE WHERE t.MEASURE_ID = :measureId AND t.QUESTION_TYPE IN ('07','08','09','10','11','12');");*/
        StringBuffer sql = new StringBuffer("SELECT DISTINCT t1.COMPILATION_NO,t.CUS_CODE,t2.NAME,t1.MEASURE_DATE,t1.UPDATA_DATE,t2.DESCRIPTION,t.QUESTION_TYPE,FU.USER_NAME FROM FB_QUESTIONRECORD t LEFT JOIN FB_MEASURE_WORD t1 ON t.MEASURE_ID=t1.MEASURE_ID LEFT JOIN FB_QUESTIONNAIRE t2 ON t2.QUESTIONNAIRE_TYPE=t.QUESTION_TYPE LEFT JOIN FB_USER FU ON t1.CREATE_USER= FU.USER_CODE WHERE t.MEASURE_ID = :measureId AND t.QUESTION_TYPE IN ('07','08','09','10','11','12');");
        params.put("measureId", measureId);
        QueryPage page1 = findBySQL(sql, params);
        return page1;
    }

    /**
     * 获取征信的几个问卷，并且将所属主问题以及答案对应的下一题替换成id
     * @param questionniareTypes
     * @return
     */
    public List<FbQuestionnaire> findTypes(List<String> questionniareTypes) {
        List<FbQuestionnaire> lstQuestion = new ArrayList<FbQuestionnaire>();
        for (String questionniareType:questionniareTypes) {
            FbQuestionnaire fbQuestionnaire =  questionnaireRepository.findByType(questionniareType);
            if (null != fbQuestionnaire){
                fbQuestionnaire = questionChangeId(fbQuestionnaire);
                lstQuestion.add(fbQuestionnaire);
            }
        }
        return lstQuestion;
    }

    /**
     * 替换所属主问题和答案对应的下一题id
     * @param questionnaire
     * @return
     */
    public FbQuestionnaire questionChangeId(FbQuestionnaire questionnaire){
        for (FbQuestionnaireDetail currentDetail :
                questionnaire.getDetails()) {
            for (FbQuestionnaireDetail detail:questionnaire.getDetails()) {
                if (StringUtils.isNoneBlank(currentDetail.getFatherQuestion()) && detail.getSortNo().equals(currentDetail.getFatherQuestion())){
//                    currentDetail.setFatherQuestion(detail.getId());
                    currentDetail.setFatherQuesId(detail.getId());
                }
                for (FbQuestionnaireAnswer answer:currentDetail.getAnswers()) {
                    if (StringUtils.isNoneBlank(answer.getNextQuestion()) && detail.getSortNo().equals(answer.getNextQuestion())){
//                        answer.setNextQuestion(detail.getId());
                        answer.setNextQuesId(detail.getId());
                    }
                }
            }
        }
        return questionnaire;
    }

    /**
     * 将问卷问题list 转成 Map<问题id，问题>
     * @param lstFbQuestionnaire
     * @return
     */
    public Map<String,FbQuestionnaireDetail> list2Map(List<FbQuestionnaire> lstFbQuestionnaire) {
        Map<String,FbQuestionnaireDetail> questionMap = new HashMap<>();
        for (FbQuestionnaire questionnaire:
        lstFbQuestionnaire) {
            for (FbQuestionnaireDetail detail :
            questionnaire.getDetails()) {
                questionMap.put(detail.getId(),detail);
            }
        }
        return questionMap;
    }

    public List<FbQuestionnaireDetail> ques2ListDetail(List<FbQuestionnaire> lstFbQuestionnaire){
        List<FbQuestionnaireDetail> lstDetail = new ArrayList<FbQuestionnaireDetail>();
        for (FbQuestionnaire questionnaire : lstFbQuestionnaire) {
        	Collections.sort(questionnaire.getDetails(), new Comparator<FbQuestionnaireDetail>() {
                @Override
                public int compare(FbQuestionnaireDetail o1, FbQuestionnaireDetail o2) {
                    return Integer.valueOf(o1.getSortNo()) - Integer.valueOf(o2.getSortNo());
                }
            });
            for (FbQuestionnaireDetail detail : questionnaire.getDetails()) {
                lstDetail.add(detail);
            }
        }
        return lstDetail;
    }

    /**
     * 将答题记录list转成 Map<问题id，答题记录>
     * @param recordList
     * @return
     */
    public Map<String,FbQuestionRecord> listRecord2Map(List<FbQuestionRecord> recordList,List<FbQuestionnaireDetail> questionnaireList,String measureId,String compilationNanme){
        Map<String,FbQuestionRecord> recordMap = new HashMap<>();
//        Set<String> questionTypeList = new HashSet<String>();
//        for (FbQuestionRecord record:recordList) {
//            String questionType = record.getQuestionType();
//            questionTypeList.add(questionType);
//            String questionId = record.getQuestionId();
//            for(FbQuestionnaireDetail detail : questionnaireList){
//                String detailId = detail.getId();
//                // 同步题库中修改的内容，防止问卷题库修改后修改问卷未更新
//                if(questionId.equals(detailId)){
//                    record.setType(detail.getQuestionType());
//                    record.setRuleItem(detail.getRuleItem());
//                    record.setApplyItem(detail.getApplyItem());
//                    record.setQuestionLevel(detail.getQuestionLevel());
//                    record.setFatherQuertion(detail.getFatherQuestion());
//                    break;
//                }
//            }
//        }
//        for(FbQuestionnaireDetail detail : questionnaireList){
//            String questionType = detail.getQuestionnaire().getQuestionniareType();
//            boolean hasFlag = false;
//            for (String hasQuestionType:questionTypeList) {
//                if(hasQuestionType.equals(questionType)){
//                    hasFlag = true;
//                    break;
//                }
//            }
//            if(!hasFlag){
//                FbQuestionRecord record = new FbQuestionRecord();
//                record.setQuestionId(detail.getId());
//                record.setType(detail.getQuestionType());
//                record.setQuestionType(detail.getQuestionnaire().getQuestionniareType());
//                record.setMeasureId(measureId);
//                record.setRuleItem(detail.getRuleItem());
//                record.setApplyItem(detail.getApplyItem());
//                record.setQuestionLevel(detail.getQuestionLevel());
//                record.setFatherQuertion(detail.getFatherQuestion());
//                record.setCusCode(UserUtil.getCurrUser().getUserCode());
//                record.setQuestuibAnswer("");
//                recordList.add(record);
//            }
//        }
        FbMeasureWord fmw = measureWordRepository.findByMeasureId(measureId);
        String cusCode = fmw.getCompilationNo();
        List list  = this.fbQuestionRecordRepository.findRecordAnswer(fmw.getSchedulerId());
        Map<String,String> map = new HashMap<>();//获取到公司登記地址，實際營業地址，登記負責人名字
        FbLoanCompany fbLoanCompany = this.loanCompanyRepository.findByComplicationNo(cusCode);
//        FbAppointmentRecord fbAppointmentRecord = this.appointmentRecordRepository.findByComNoTrandId(cusCode,fmw.getTrandId());
        FbAppointmentRecord fbAppointmentRecord = appointmentRecordRepository.getOne(fmw.getSchedulerId());
        map.put("comAddress",fbLoanCompany.getComAddress());//公司登記地址
        map.put("businessAddress",fbAppointmentRecord.getBusinessAddress());//實際營業地址
        map.put("principalName",fbLoanCompany.getPrincipalName());//登記負責人名字

        for(FbQuestionnaireDetail detail : questionnaireList){
            boolean hasFlag = false;
            String detailId = detail.getId();
            for (FbQuestionRecord record:recordList) {
                String questionId = record.getQuestionId();
                if(detailId.equals(questionId)){
                    record.setType(detail.getQuestionType());
                    record.setRuleItem(detail.getRuleItem());
                    record.setApplyItem(detail.getApplyItem());
                    record.setQuestionLevel(detail.getQuestionLevel());
                    record.setFatherQuertion(detail.getFatherQuesId());
                    record.setCusCode(cusCode);
                    /*if("2c9074b56b0b8504016b1b664b170649".equals(questionId) && StringUtils.isBlank(record.getQuestuibAnswer())){
                        record.setQuestuibAnswer("");//A21500
                    }*/
                    recordList.add(record);
                    hasFlag = true;
                    break;
                }
            }
            if(!hasFlag){
                FbQuestionRecord record = new FbQuestionRecord();
                record.setQuestionId(detail.getId());
                record.setType(detail.getQuestionType());
                record.setQuestionType(detail.getQuestionnaire().getQuestionniareType());
                record.setMeasureId(measureId);
                record.setRuleItem(detail.getRuleItem());
                record.setApplyItem(detail.getApplyItem());
                record.setQuestionLevel(detail.getQuestionLevel());
                record.setFatherQuertion(detail.getFatherQuesId());
                record.setCusCode(cusCode);
                record.setQuestuibAnswer("");
                recordList.add(record);
            }
        }

        for (FbQuestionRecord record:recordList) {
            if (record.getType().equals("05")||record.getType().equals("07")){
                //xxxx-xx
                String answer = record.getQuestuibAnswer();
                if (StringUtils.isNoneBlank(answer)&&answer.length()>2){
                    record.setRepublicYear(answer.split("-")[0]);
                    record.setRepublicMonth(answer.split("-")[1]);
                }
            }
            setRecordAnswer(record,list,map);
            recordMap.put(record.getQuestionId(),record);
        }
        return recordMap;
    }

    public void setRecordAnswer(FbQuestionRecord record,List<Object> list,Map<String,String> map){
        try {
            if(record == null){
                return;
            }
            String questuibAnswer = record.getQuestuibAnswer();
            if(StringUtils.isNotBlank(questuibAnswer)){
                return;
            }
            String questionId = record.getQuestionId();
            //登記地址  map(comAddress)
            if("2c9074b56b0b8504016b1b664b2a065d".equals(questionId)
                    && StringUtils.isBlank(record.getQuestuibAnswer()) && map.get("comAddress")!=null){
                record.setQuestuibAnswer(map.get("comAddress"));
            }
            //登記負責人名字  map(principalName)
            if(("2c9074b56b0b8504016b1b664b8e06f9".equals(questionId) || "2c9180866bac7f89016bac8fad360089".equals(questionId))
                    && StringUtils.isBlank(record.getQuestuibAnswer()) && map.get("principalName")!=null){
                record.setQuestuibAnswer(map.get("principalName"));
            }
            //實際營業地址  map(businessAddress)
            if("2c9074b56b0b8504016b1b664b2c0662".equals(questionId)
                    && StringUtils.isBlank(record.getQuestuibAnswer()) && map.get("businessAddress")!=null){
                record.setQuestuibAnswer(map.get("businessAddress"));
            }

            if(list == null || list.size()==0){
                return;
            }
            Object[] object = (Object[])list.get(0);
            //1.企業沿革
            //公司名稱  (A21500) object[0]
            if("2c9074b56b0b8504016b1b664b170649".equals(questionId)
                    && StringUtils.isBlank(record.getQuestuibAnswer()) && object[0]!=null){
                record.setQuestuibAnswer(object[0].toString());
            }
            //統編  (A20100) object[1]
            if("2c9074b56b0b8504016b1b664b1c064b".equals(questionId)
                    && StringUtils.isBlank(record.getQuestuibAnswer()) && object[1]!=null){
                record.setQuestuibAnswer(object[1].toString());
            }
            //本次評等  (CREDITGRADE) object[2]
            if("2c9074b56b0b8504016b1b664b200651".equals(questionId)
                    && StringUtils.isBlank(record.getQuestuibAnswer()) && object[2]!=null){
                record.setQuestuibAnswer(object[2].toString());
            }
            //企業設立時間  (A20300) object[3]
            if("2c9074b56b0b8504016b1b664b350670".equals(questionId)
                    && StringUtils.isBlank(record.getQuestuibAnswer()) && object[3]!=null && !object[3].equals("") && object[3].toString().length()>=5){
                String year = String.valueOf(Integer.parseInt(object[3].toString().substring(0, 3)));
                String month = object[3].toString().substring(3, 5);
                record.setRepublicYear(year);
                record.setRepublicMonth(month);
            }
            //3.資金調度及償債能力
            //目前金融負債餘額 (AA0304+AA5000+AA5200) object[4]+object[5]+object[6]
            if("402885d26a618662016a67b1daca013d".equals(questionId)
                    && StringUtils.isBlank(record.getQuestuibAnswer())) {
                String y = "";
                if ((object[4] != null && !object[4].equals("")) || (object[5] != null && !object[5].equals("")) || (object[6] != null && !object[6].equals(""))) {
                    if (object[4] == null || object[4].equals("")) {
                        object[4] = 0;
                    }
                    if (object[5] == null || object[5].equals("")) {
                        object[5] = 0;
                    }
                    if (object[6] == null || object[6].equals("")) {
                        object[6] = 0;
                    }
                    double x = new BigDecimal(object[4].toString()).add(new BigDecimal(object[5].toString())).add(new BigDecimal(object[6].toString())).doubleValue();
//                    if (x > 0) {
    //                    y = String.valueOf(x);
    //                    y = new BigDecimal(String.valueOf(x)).stripTrailingZeros().toPlainString();
                     y = new BigDecimal(String.valueOf(x)).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
//                    }
                }
                record.setQuestuibAnswer(y);
            }
            //去年同期之金融負債餘額 (AA0503+AA5100+AA5300) object[7]+object[8]+object[9]
            if("402885d26a618662016a67b1dafe013f".equals(questionId)
                    && StringUtils.isBlank(record.getQuestuibAnswer())){
                String y = "";
                if ((object[7] != null && !object[7].equals("")) || (object[8] != null && !object[8].equals("")) || (object[9] != null && !object[9].equals(""))) {
                    if (object[7] == null || object[7].equals("")) {
                        object[7] = 0;
                    }
                    if (object[8] == null || object[8].equals("")) {
                        object[8] = 0;
                    }
                    if (object[9] == null || object[9].equals("")) {
                        object[9] = 0;
                    }
//                double x = Double.parseDouble(object[7].toString()) + Double.parseDouble(object[8].toString()) + Double.parseDouble(object[9].toString());
                    double x = new BigDecimal(object[7].toString()).add(new BigDecimal(object[8].toString())).add(new BigDecimal(object[9].toString())).doubleValue();
//                    if (x > 0) {
//                    y = String.valueOf(x);
//                    y = new BigDecimal(String.valueOf(x)).stripTrailingZeros().toPlainString();
                     y = new BigDecimal(String.valueOf(x)).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
//                    }
                }
                record.setQuestuibAnswer(y);
            }
            //每月本息支出 (AL0101+AL0201+AL0301) object[10]+object[11]+object[12]
            if("2c9180866bac7f89016bace741e40530".equals(questionId)
                    && StringUtils.isBlank(record.getQuestuibAnswer())){
                String y = "";
                if ((object[10] != null && !object[10].equals("")) || (object[11] != null && !object[11].equals("")) || (object[12] != null && !object[12].equals(""))) {
                    if (object[10] == null || object[10].equals("")) {
                        object[10] = 0;
                    }
                    if (object[11] == null || object[11].equals("")) {
                        object[11] = 0;
                    }
                    if (object[12] == null || object[12].equals("")) {
                        object[12] = 0;
                    }
//                double x = Double.parseDouble(object[10].toString()) + Double.parseDouble(object[11].toString()) + Double.parseDouble(object[12].toString());
                    double x = new BigDecimal(object[10].toString()).add(new BigDecimal(object[11].toString())).add(new BigDecimal(object[12].toString())).doubleValue();
//                    if (x > 0) {
//                    y = String.valueOf(x);
                     y = new BigDecimal(String.valueOf(x)).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
//                    }
                }
                record.setQuestuibAnswer(y);
            }
            //存款實績 (AB0803+AB0903+AB1003) object[13]+object[14]+object[15]
            if("402885d26a618662016a67b1df350154".equals(questionId)
                    && StringUtils.isBlank(record.getQuestuibAnswer())){
                String y = "";
                if ((object[13] != null && !object[13].equals("")) || (object[14] != null && !object[14].equals("")) || (object[15] != null && !object[15].equals(""))) {
                    if (object[13] == null || object[13].equals("")) {
                        object[13] = 0;
                    }
                    if (object[14] == null || object[14].equals("")) {
                        object[14] = 0;
                    }
                    if (object[15] == null || object[15].equals("")) {
                        object[15] = 0;
                    }
//                double x = Double.parseDouble(object[13].toString()) + Double.parseDouble(object[14].toString()) + Double.parseDouble(object[15].toString());
                    double x = new BigDecimal(object[13].toString()).add(new BigDecimal(object[14].toString())).add(new BigDecimal(object[15].toString())).doubleValue();
//                    if (x > 0) {
//                    y = String.valueOf(x);
//                    y = new BigDecimal(String.valueOf(x)).stripTrailingZeros().toPlainString();
                     y = new BigDecimal(String.valueOf(x)).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
//                    }
                }
                record.setQuestuibAnswer(y);
            }
            //關係企業存款實績 (AB1103) object[16]
            if("402885d26a618662016a67b1df9c0156".equals(questionId)
                    && StringUtils.isBlank(record.getQuestuibAnswer())
                    && object[16]!=null){
                record.setQuestuibAnswer(new BigDecimal(String.valueOf(object[16].toString())).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
            }
            //4.保人資力
            //信用卡循環金額 (AA1103+AA1804) object[17]+object[18]
            if("297e673e6afc9795016afca9cfa3008a".equals(questionId)
                    && StringUtils.isBlank(record.getQuestuibAnswer())){
                String y = "";
                if ((object[17] != null && !object[17].equals("")) || (object[18] != null && !object[18].equals(""))) {
                    if (object[17] == null || object[17].equals("")) {
                        object[17] = 0;
                    }
                    if (object[18] == null || object[18].equals("")) {
                        object[18] = 0;
                    }
//                double x = Double.parseDouble(object[17].toString()) + Double.parseDouble(object[18].toString());
                    double x = new BigDecimal(object[17].toString()).add(new BigDecimal(object[18].toString())).doubleValue();
//                    if (x > 0) {
//                    y = String.valueOf(x);
//                    y = new BigDecimal(String.valueOf(x)).stripTrailingZeros().toPlainString();
                    y = new BigDecimal(String.valueOf(x)).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
//                    }
                }
                record.setQuestuibAnswer(y);
            }
            //信用卡分期帳款餘額 (AA2902+AA3703) object[19]+object[20]
            if("297e673e6afc9795016afca9d003008f".equals(questionId)
                    && StringUtils.isBlank(record.getQuestuibAnswer())){
                String y = "";
                if ((object[19] != null && !object[19].equals("")) || (object[20] != null && !object[20].equals(""))) {
                    if (object[19] == null || object[19].equals("")) {
                        object[19] = 0;
                    }
                    if (object[20] == null || object[20].equals("")) {
                        object[20] = 0;
                    }
//                double x = Double.parseDouble(object[19].toString()) + Double.parseDouble(object[20].toString());
                    double x = new BigDecimal(object[19].toString()).add(new BigDecimal(object[20].toString())).doubleValue();
//                    if (x > 0) {
//                    y = String.valueOf(x);
//                    y = new BigDecimal(String.valueOf(x)).stripTrailingZeros().toPlainString();
                     y = new BigDecimal(String.valueOf(x)).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
//                    }
                }
                record.setQuestuibAnswer(y);
            }
            //2.1產業-批發 2.2產業-零售&餐飲 2.3產業-營造 2.4產業-製造 2.5產業-服務&其他 2.6產業-旅行社
            //近三年营收 第一个年度 (B10000) object[21]
            if(("2c9180866bac7f89016bacbfdd8004e2".equals(questionId) || "297e673e6ae3bb7e016ae3bdfdee003d".equals(questionId)
                    || "2c9180866bdf0899016bdf9f49b204ce".equals(questionId) || "297e673e6acae99f016ace33765d01ee".equals(questionId)
                    || "297e673e6b029024016b029b1b460098".equals(questionId) || "297e673e6b208cc3016b209c35eb008b".equals(questionId))
                    && StringUtils.isBlank(record.getQuestuibAnswer())
                    && object[21]!=null && !object[21].equals("")){
                record.setQuestuibAnswer(String.valueOf(Integer.parseInt(object[21].toString())));
            }
            //第一个年度的营收 (B10140) object[22]
            if(("2c9180866bac7f89016bacbfddd104e4".equals(questionId) || "297e673e6ae3bb7e016ae3bdfe51003f".equals(questionId)
                    || "2c9180866bdf0899016bdf9f4a0c04d0".equals(questionId) || "297e673e6acae99f016ace3376c301f1".equals(questionId)
                    || "297e673e6b029024016b029b1ba5009b".equals(questionId) || "297e673e6b208cc3016b209c3653008d".equals(questionId))
                    && StringUtils.isBlank(record.getQuestuibAnswer())
                    && object[22]!=null && !object[22].equals("")){
                record.setQuestuibAnswer(new BigDecimal(String.valueOf(object[22].toString())).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
            }
            //第二个年度 (B20000) object[23]
            if(("2c9180866bac7f89016bacbfde0c04e6".equals(questionId) || "297e673e6ae3bb7e016ae3bdfeb50041".equals(questionId)
                    || "2c9180866bdf0899016bdf9f4a6104d2".equals(questionId) || "297e673e6acae99f016ace33776e01f3".equals(questionId)
                    || "297e673e6b029024016b029b1c35009d".equals(questionId) || "297e673e6b208cc3016b209c36b80090".equals(questionId))
                    && StringUtils.isBlank(record.getQuestuibAnswer())
                    && object[23]!=null && !object[23].equals("")){
                record.setQuestuibAnswer(String.valueOf(Integer.parseInt(object[23].toString())));
            }
            //第二个年度的营收 (B20140) object[24]
            if(("2c9180866bac7f89016bacbfde4604e8".equals(questionId) || "297e673e6ae3bb7e016ae3bdff170043".equals(questionId)
                    || "2c9180866bdf0899016bdf9f4ab604d4".equals(questionId) || "297e673e6acae99f016ace3377d601f6".equals(questionId)
                    || "297e673e6b029024016b029b1c9500a0".equals(questionId) || "297e673e6b208cc3016b209c374a0092".equals(questionId))
                    && StringUtils.isBlank(record.getQuestuibAnswer())
                    && object[24]!=null && !object[24].equals("")){
                record.setQuestuibAnswer(new BigDecimal(String.valueOf(object[24].toString())).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
            }
            //第三个年度的 (B30000) object[25]
            if(("2c9180866bac7f89016bacbfde9904ea".equals(questionId) || "297e673e6ae3bb7e016ae3bdff7a0045".equals(questionId)
                    || "2c9180866bdf0899016bdf9f4b0804d6".equals(questionId) || "2c9180866bdf0899016bdfc73d5f053e".equals(questionId)
                    || "297e673e6b029024016b029b1d2300a2".equals(questionId) || "297e673e6b208cc3016b209c37a80094".equals(questionId))
                    && StringUtils.isBlank(record.getQuestuibAnswer())
                    && object[25]!=null && !object[25].equals("")){
                record.setQuestuibAnswer(String.valueOf(Integer.parseInt(object[25].toString())));
            }
            //第三个年度的营收 (B30140) object[26]
            if(("2c9180866bac7f89016bacbfded204ec".equals(questionId) || "297e673e6ae3bb7e016ae3bdffde0048".equals(questionId)
                    || "2c9180866bdf0899016bdf9f4b6004d8".equals(questionId) || "2c9180866bdf0899016bdfc73dba0540".equals(questionId)
                    || "2c9180866bdf0899016bdf44f3f203cc".equals(questionId) || "297e673e6b208cc3016b209c380d0099".equals(questionId))
                    && StringUtils.isBlank(record.getQuestuibAnswer())
                    && object[26]!=null && !object[26].equals("")){
                record.setQuestuibAnswer(new BigDecimal(String.valueOf(object[26].toString())).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
            }
            //近一年營收 (AD0204) object[27]
            if(("2c9180866bac7f89016bacbfdf0d04ee".equals(questionId) || "297e673e6ae3bb7e016ae3be0071004a".equals(questionId)
                    || "2c9180866bdf0899016bdf9f4bb404da".equals(questionId) || "297e673e6acae99f016ace33786f01f8".equals(questionId)
                    || "2c9180866bdf0899016bdf44f45703ce".equals(questionId) || "297e673e6b208cc3016b209c3904009b".equals(questionId))
                    && StringUtils.isBlank(record.getQuestuibAnswer())
                    && object[27]!=null && !object[27].equals("")){
                record.setQuestuibAnswer(new BigDecimal(String.valueOf(object[27].toString())).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
    }

    public FbQuestionnaireDetail findDetailById(String detailId){
        return questionnaireDetailRepository.findByDetailId(detailId);
    }

    public List<FbQuestionnaireDetail> findDetailsByQId(String questionnaireId){
        return questionnaireDetailRepository.findDetailsByQId(questionnaireId);
    }

    public List<FbQuestionnaireDetail> findDetailsByFatherSort(String questionnaireId,String sortNo){
        return questionnaireDetailRepository.findDetailsByFatherSort(questionnaireId,sortNo);
    }

    public FbQuestionnaireDetail findDetailsBySort(String questionnaireId,String sortNo){
        return questionnaireDetailRepository.findDetailsBySort(questionnaireId,sortNo);
    }

    public Map<String,Object> getChangeAnswerMap(FbQuestionnaireDetail currentQuestionDetail,String currentAnswer,
                                                 List<FbQuestionnaireDetail> childQuestionDetail,String measureId){
        Map<String,Object> result = new HashMap<String,Object>();

        List<FbQuestionRecord> recordList = fbQuestionRecordRepository.findByMeasureId(measureId);
        for (FbQuestionnaireDetail fbQuestionnaireDetail:childQuestionDetail){
            for (FbQuestionRecord record:recordList) {
                if(fbQuestionnaireDetail.getId().equals(record.getQuestionId())){
                    fbQuestionnaireDetail.setAnswer(record.getQuestuibAnswer());
                    break;
                }
            }
        }

        //增加两个list 分别记录需要/不需要跳转的题目id
        List<String> needQuesId = new ArrayList<String>();
        List<String> noNeedQuesId = new ArrayList<String>();
        // 暂存下一题题目编号
        Set<String> nextIndexIdSet = new HashSet<String>();
        String nextIndexId = "";
        //获取所有子问题答案指向的题目编号
        List<String> allQuesId = new ArrayList<String>();
        //获取当前答案
        for (FbQuestionnaireAnswer currAnswer:currentQuestionDetail.getAnswers()) {
            if (currentAnswer.equals(currAnswer.getAnswer())){
                nextIndexId = currAnswer.getNextQuestion();
                nextIndexIdSet.add(nextIndexId);
                needQuesId.add(nextIndexId);
            }
            allQuesId.add(currAnswer.getNextQuestion());
        }
        //获取所有需要跳转的下一题
        for(int i=0;i<childQuestionDetail.size();i++){
            for (FbQuestionnaireDetail detail:childQuestionDetail) {
                //获取子问题答案
                if(nextIndexId.equals(detail.getSortNo())){
                    String childAnswer = detail.getAnswer();
                    if(StringUtils.isNotBlank(childAnswer) && "02".equals(detail.getQuestionType())){
                        for (FbQuestionnaireAnswer currAnswer:detail.getAnswers()) {
                            if (childAnswer.equals(currAnswer.getAnswer())){
                                nextIndexId = currAnswer.getNextQuestion();
                                nextIndexIdSet.add(nextIndexId);
                            }
                        }
                    }else{
                        nextIndexId = detail.getAnswers().get(0).getNextQuestion();
                        nextIndexIdSet.add(nextIndexId);
                    }
                    break;
                }
            }
        }

        //获取所有需要显示的子问题第一个答案指向的题目编号
        for (FbQuestionnaireDetail detail:childQuestionDetail) {
            if(nextIndexIdSet.contains(detail.getSortNo())){
                //获取子问题答案
                String childAnswer = detail.getAnswer();
                if(StringUtils.isNotBlank(childAnswer) && "02".equals(detail.getQuestionType())){
                    for (FbQuestionnaireAnswer currAnswer:detail.getAnswers()) {
                        if (childAnswer.equals(currAnswer.getAnswer())){
                            needQuesId.add(currAnswer.getNextQuestion());
                        }
                    }
                }else{
                    needQuesId.add(detail.getAnswers().get(0).getNextQuestion());
                }
            }
            //获取子问题答案
            for (FbQuestionnaireAnswer currAnswer:detail.getAnswers()) {
                allQuesId.add(currAnswer.getNextQuestion());
            }
        }
        //需要显示的子问题编号去重
        needQuesId = removeRepetitive(needQuesId);
        allQuesId = removeRepetitive(allQuesId);
        //全部子问题先去掉需要显示的问题编号
//        for (String needId:
//                needQuesId) {
//            for (int i = 0; i < allQuesId.size(); i++) {
//                if (needId.equals(allQuesId.get(i))){
//                    allQuesId.remove(i);
//                    i--;
//                }
//            }
//        }
        //需要删除的问题编号必须是这些子问题里的，否则会将主问题删掉（肯定有子问题的答案会指向下一道主问题）
        List<FbQuestionnaireDetail> needQuestion = new ArrayList<FbQuestionnaireDetail>();
        for (FbQuestionnaireDetail detail:childQuestionDetail) {
            for (String needId:needQuesId) {
                if (needId.equals(detail.getSortNo())){
                    needQuestion.add(detail);
                }
            }
            for (String allNoId:allQuesId) {
                if (allNoId.equals(detail.getSortNo())){
                    noNeedQuesId.add(detail.getId());
                }
            }
        }

        String questionnaireType = null;
        // 对需要新增显示的问题进行处理
        if (!CollectionUtils.isEmpty(needQuestion)){
            FbQuestionnaire fbQuestionnaire = needQuestion.get(0).getQuestionnaire();
            questionnaireType = fbQuestionnaire.getQuestionniareType();
            //将问卷问题里的指向题目编号替换成题目id
            for (FbQuestionnaireDetail currentDetail :fbQuestionnaire.getDetails()) {
                for (FbQuestionnaireDetail detail:needQuestion) {
                    if (StringUtils.isNoneBlank(detail.getFatherQuestion()) && currentDetail.getSortNo().equals(detail.getFatherQuestion())){
                        detail.setFatherQuestion(currentDetail.getId());
                    }
                    for (FbQuestionnaireAnswer answer:detail.getAnswers()) {
                        if (StringUtils.isNoneBlank(answer.getNextQuestion()) && currentDetail.getSortNo().equals(answer.getNextQuestion())){
                            answer.setNextQuestion(currentDetail.getId());
                        }
                    }
                }
            }
            //将问卷题目中的问卷信息置为null
            for (FbQuestionnaireDetail detail:needQuestion) {
                detail.setQuestionnaire(null);
                for (FbQuestionnaireAnswer currAnswer:detail.getAnswers()) {
                    currAnswer.setQuestion(null);
                }
            }
            //根据题号排序
            Collections.sort(needQuestion, new Comparator<FbQuestionnaireDetail>() {
                @Override
                public int compare(FbQuestionnaireDetail o1, FbQuestionnaireDetail o2) {
                    return Integer.valueOf(o1.getSortNo()) - Integer.valueOf(o2.getSortNo());
                }
            });
        }
//        List<FbQuestionRecord> recordList = fbQuestionRecordRepository.findByMeasureId(measureId);
//        for (FbQuestionnaireDetail fbQuestionnaireDetail:needQuestion){
//            for (FbQuestionRecord record:recordList) {
//                if(fbQuestionnaireDetail.getId().equals(record.getQuestionId())){
//                    fbQuestionnaireDetail.setAnswer(record.getQuestuibAnswer());
//                    break;
//                }
//            }
////            fbQuestionnaireDetail.setAnswer(fbQuestionRecordRepository.findByIds(fbQuestionnaireDetail.getId(),measureId));
//        }

        result.put("message","该问题存在子问题，需要刷新题目！");
        result.put("isChange",true);
        result.put("needQuestion",needQuestion);
        result.put("noNeedQuesId",noNeedQuesId);
        result.put("questionnaireType",questionnaireType);
        return result;
    }

    public Map<String,FbQuestionnaireDetail> getNoNeedQuesDetailIds(List<FbQuestionnaireDetail> questionnaireList,Map<String,FbQuestionRecord> questionRecordMap){
        Map<String,FbQuestionnaireDetail> noNeedQuesIdMap = new HashMap<String,FbQuestionnaireDetail>();
        Map<String,List<FbQuestionnaireDetail>> detailMap = new HashMap<String,List<FbQuestionnaireDetail>>();
        for(FbQuestionnaireDetail detail:questionnaireList){
            String questionnaireId  = detail.getQuestionnaire().getId();
            List<FbQuestionnaireDetail> detailList = detailMap.get(questionnaireId);
            if(detailList == null){
                detailList = new ArrayList<FbQuestionnaireDetail>();
            }
            detailList.add(detail);
            detailMap.put(questionnaireId,detailList);
        }

        for(String questionnaireId:detailMap.keySet()){
            List<FbQuestionnaireDetail> detailList = detailMap.get(questionnaireId);
            //获取所有需要跳转的下一题
            String nextIndexId = "1";
            Set<String> nextIndexIdSet = new HashSet<String>();
            nextIndexIdSet.add(nextIndexId);
//            Set<String> needQuesIdSet = new HashSet<String>();
            for(int i=0;i<detailList.size();i++){
                for (FbQuestionnaireDetail detail:detailList) {
                    String detailId = detail.getId();
                    FbQuestionRecord record = questionRecordMap.get(detailId);
                    List<FbQuestionnaireAnswer> answerList = detail.getAnswers();
                    //获取子问题答案
                    if(nextIndexId.equals(detail.getSortNo())){
                        String childAnswer = record!=null?record.getQuestuibAnswer():detail.getAnswer();
                        if(StringUtils.isNotBlank(childAnswer) && "02".equals(detail.getQuestionType())){
                            boolean hasNext = false;
                            for (FbQuestionnaireAnswer currAnswer:answerList) {
                                if (childAnswer.equals(currAnswer.getAnswer())){
                                    nextIndexId = currAnswer.getNextQuestion();
                                    nextIndexIdSet.add(nextIndexId);
                                    hasNext = true;
                                }
                            }
                            if(!hasNext){
                                nextIndexId = detail.getAnswers().get(0).getNextQuestion();
                                nextIndexIdSet.add(nextIndexId);
                            }
                        }else{
                            nextIndexId = detail.getAnswers().get(0).getNextQuestion();
                            nextIndexIdSet.add(nextIndexId);
                        }
                        break;
                    }
                }
            }
            //获取所有需要显示的子问题答案指向的题目编号
//            for (FbQuestionnaireDetail detail:detailList) {
//                if(nextIndexIdSet.contains(detail.getSortNo())){
//                    String detailId = detail.getId();
//                    FbQuestionRecord record = questionRecordMap.get(detailId);
//                    List<FbQuestionnaireAnswer> answerList = detail.getAnswers();
//                    //获取子问题答案
//                    String childAnswer = record!=null?record.getQuestuibAnswer():detail.getAnswer();
//                    if(StringUtils.isNotBlank(childAnswer) && "02".equals(detail.getQuestionType())){
//                        for (FbQuestionnaireAnswer currAnswer:answerList) {
//                            if (childAnswer.equals(currAnswer.getAnswer())){
//                                needQuesIdSet.add(currAnswer.getNextQuestion());
//                            }
//                        }
//                    }else{
//                        needQuesIdSet.add(detail.getAnswers().get(0).getNextQuestion());
//                    }
//                }
//            }
            for (FbQuestionnaireDetail detail:detailList) {
                String detailId = detail.getId();
                String sortNo = detail.getSortNo();
                if(!nextIndexIdSet.contains(sortNo)){
                    noNeedQuesIdMap.put(detailId,detail);
                }
            }
        }
        return noNeedQuesIdMap;
    }

    public List<String> removeRepetitive (List<String> lstStr){
        for (int i = 0 ; i < lstStr.size() ; i++){
            for (int j = i + 1 ; j < lstStr.size() ; j++){
                if (lstStr.get(i).equals(lstStr.get(j))){
                    lstStr.remove(j);
                    j--;
                }
            }
        }
        return lstStr;
    }

    public Map<String,String> updateQuestionRecord(List<FbQuestionRecord> questionRecordnList){
        Map<String,String> map = new HashMap<String,String>();
        String measureId = null;
        String type = null;
        List<FbQuestionRecord> removeRecordList = new ArrayList<FbQuestionRecord>();
        if (!CollectionUtils.isEmpty(questionRecordnList)){
            for (FbQuestionRecord record : questionRecordnList) {
                if((StringUtils.isBlank(record.getQuestuibAnswer()) || record.getQuestuibAnswer().equals("null"))
                        && !record.getType().equals("05") && !record.getType().equals("07")){
                    removeRecordList.add(record);
                    continue;
                }
                if((StringUtils.isBlank(record.getRepublicYear()) || record.getRepublicYear().equals("null")
                        || StringUtils.isBlank(record.getRepublicMonth()) || record.getRepublicMonth().equals("null"))
                        && (record.getType().equals("05") || record.getType().equals("07"))){
                    removeRecordList.add(record);
                    continue;
                }
                if (StringUtils.isNoneBlank(record.getQuestionType())){
                    if (record.getQuestionType().equals("07")
                        || record.getQuestionType().equals("08")
                        || record.getQuestionType().equals("09")
                        || record.getQuestionType().equals("10")
                        || record.getQuestionType().equals("11")
                        || record.getQuestionType().equals("12")
                        ){
                        type = record.getQuestionType();
                    }
                }
                measureId = record.getMeasureId();
                record.setCreateTime(new Date());
                record.setSaveFlag("02");
                if (record.getType().equals("05") || record.getType().equals("07")){
                    record.setQuestuibAnswer(record.getRepublicYear() + "-" + record.getRepublicMonth());
                }
            }
            if (null != measureId){
                questionRecordnList.removeAll(removeRecordList);
                fbQuestionRecordRepository.deleteAllByMeasureId(measureId);
                fbQuestionRecordRepository.saveAll(questionRecordnList);
                FbMeasureWord fbMeasureWord = measureWordRepository.findByMeasureId(measureId);
                map = saveCreditReportService.saveCreditReportWeb(fbMeasureWord.getCompilationNo(),measureId,type,fbMeasureWord.getSchedulerId());
                fbMeasureWord.setAttachId(map.get("creditReportAttachId").toString());
                fbMeasureWord.setUpdataDate(new Date()); // 更新修改时间
                fbMeasureWord.setTempFlag("02");
                measureWordRepository.save(fbMeasureWord) ;
            }
        }
        return map;
    }
    
    public Map<String,String> temporaryStorage(List<FbQuestionRecord> questionRecordnList){
        Map<String,String> map = new HashMap<String,String>();
        String measureId = null;
        String type = null;
        List<FbQuestionRecord> removeRecordList = new ArrayList<FbQuestionRecord>();
        if (!CollectionUtils.isEmpty(questionRecordnList)){
            for (FbQuestionRecord record:questionRecordnList) {
                if((StringUtils.isBlank(record.getQuestuibAnswer()) || record.getQuestuibAnswer().equals("null"))
                        && !record.getType().equals("05") && !record.getType().equals("07")){
                    removeRecordList.add(record);
                    continue;
                }
            	if (StringUtils.isNotBlank(record.getQuestuibAnswer())) {
            		if(record.getQuestuibAnswer().equals("null")){
                		record.setQuestuibAnswer("");
                	}
				}
            	if (StringUtils.isNotBlank(record.getRepublicYear())) {
            	    if (record.getRepublicYear().equals("null")) {
					    record.setRepublicYear("");
				    }
            	}
            	if (StringUtils.isNotBlank(record.getRepublicMonth())) {
            	    if (record.getRepublicMonth().equals("null")) {
					    record.setRepublicMonth("");
				    }
            	}
                if (StringUtils.isNoneBlank(record.getQuestionType())){
                    if (record.getQuestionType().equals("07")
                        || record.getQuestionType().equals("08")
                        || record.getQuestionType().equals("09")
                        || record.getQuestionType().equals("10")
                        || record.getQuestionType().equals("11")
                        || record.getQuestionType().equals("12")
                        ){
                        type = record.getQuestionType();
                    }
                }
                if(StringUtils.isBlank(measureId)){
                    measureId = record.getMeasureId();
                }
                record.setCreateTime(new Date());
                record.setSaveFlag("01");
                if (record.getType().equals("05")||record.getType().equals("07")){
                	if(StringUtils.isNoneBlank(record.getRepublicYear()) && StringUtils.isNoneBlank(record.getRepublicMonth())){
                        record.setQuestuibAnswer(record.getRepublicYear() + "-" + record.getRepublicMonth());
                	}
                }
            }
            if (null != measureId){
                questionRecordnList.removeAll(removeRecordList);
                fbQuestionRecordRepository.deleteAllByMeasureId(measureId);
                fbQuestionRecordRepository.saveAll(questionRecordnList);
                FbMeasureWord fbMeasureWord = measureWordRepository.findByMeasureId(measureId);
                fbMeasureWord.setUpdataDate(new Date()); // 更新修改时间
                fbMeasureWord.setTempFlag("01");
                measureWordRepository.save(fbMeasureWord) ;
                
                map.put("success", "true");
                map.put("message","更新成功");
            }else{
            	 map.put("success", "false");
                 map.put("message","更新失敗,請稍後重試");
            }
        }
        return map;
    }

    /**
     * 征信实访根据测字id获取附件id,从MongoDB下载pdf
     * @param measureId
     * @return
     */
    public void downloadPdf(HttpServletResponse response,String measureId) {

        //获取附件id
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer(" SELECT FA.FILE_CONTEXT_ID, FA.ATTACH_ID FROM FB_ATTACHMENT FA LEFT JOIN FB_MEASURE_WORD FMW ON FMW.ATTACH_ID=FA.ATTACH_ID WHERE FMW.MEASURE_ID=:measureId");
        params.put("measureId",measureId);
        List<Object[]> list = findBySQL(sql, params).getContent();
        if(list.size()>0){

            try {
                Object[] obj = list.get(0);
                String fileId = obj[0].toString();
                String fileName = obj[1].toString();
                FileContext fileContext = fileContextDao.findById(fileId);
                String fileContent = fileContext.getFileContext();
                byte[] fileBytes = getByteByBase64(fileContent.substring(fileContent.indexOf(",")+1));
                //儲存原图
                OutputStream os = response.getOutputStream();// 取得输出流
                response.reset();// 清空输出流
                response.setHeader("Content-disposition", "attachment; filename="+fileName+".pdf");// 设定输出文件头
                response.setContentType("application/pdf");// 定义输出类型
                os.write(fileBytes);
                os.flush();
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("downloadPdf error:  "+ e);
            }
        }
    }


    /**
     * 从base64字符串获取二进制
     *
     * @param fileContent
     * @return
     */
    public static byte[] getByteByBase64(String fileContent) {
        if (null == fileContent) {
            return null;
        } else {
            BASE64Decoder decode = new BASE64Decoder();
            System.out.println("file size ==========>" + fileContent.length());
            fileContent = fileContent.substring(fileContent.indexOf(",")+1);

            byte[] b = null;
            try {
                b = decode.decodeBuffer(fileContent);
                for (int i = 0; i < b.length; ++i) {
                    if (b[i] < 0) {// 调整异常数据
                        b[i] += 256;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.info("getByteByBase64  error:  "+ e);
            }
            return b;
        }
    }

    public String getIndustryName(String compannyNo,String measureId,String lastFlag) {
        //根据统编获取问卷类型
        FbLoanCompany loanCompany = loanCompanyRepository.findByComplicationNo(compannyNo);
        String industryCode = loanCompany == null ? "" : loanCompany.getComCreditIndustryCode();
		industryCode = StringUtils.isBlank(industryCode) ? "99" : industryCode;
        String questionnaireType = "";
        switch (industryCode){
            case "01" : questionnaireType = "07"; break;
            case "02" : questionnaireType = "08"; break;
            case "03" : questionnaireType = "09"; break;
            case "04" : questionnaireType = "11"; break;
            case "99" : questionnaireType = "11"; break;
            case "05" : questionnaireType = "10"; break;
            case "06" : questionnaireType = "12"; break;
            default: questionnaireType = "11"; break;
        }
        //
//        String maxMeasureId = measureWordRepository.getMaxMeasureId(compannyNo);
//        if(!measureId.equals(maxMeasureId)) {
        if("0".equals(lastFlag)){
            //根据测字ID查询答题记录表获取问卷类型（旧数据）
            String quertionType = findQuestionType(measureId);
            if(quertionType!="" && quertionType!=null){
                questionnaireType = quertionType;
            }
        }
        //根据问卷类型获取问卷名称
        String industryName = fbQuestionRecordRepository.findIndustryNameByType(questionnaireType);
        return industryName;
    }

    public String getSchedulerBaseInfo(String measureId){
        FbMeasureWord fmw = measureWordRepository.findByMeasureId(measureId);
        GetSchedulerBaseInfoRq getSchedulerBaseInfoRq = new GetSchedulerBaseInfoRq();
        getSchedulerBaseInfoRq.setSchedulerId(fmw.getSchedulerId());
        String userUid = UserUtil.getCurrUser().getUserUid();
        getSchedulerBaseInfoRq.setUid(userUid);
        GetSchedulerBaseInfoRs getSchedulerBaseInfoRs = interfaceService.getSchedulerBaseInfo(getSchedulerBaseInfoRq);
        if(getSchedulerBaseInfoRs!=null){
            return getSchedulerBaseInfoRs.getAltertMsg();
        }
        return null;
    }

}
