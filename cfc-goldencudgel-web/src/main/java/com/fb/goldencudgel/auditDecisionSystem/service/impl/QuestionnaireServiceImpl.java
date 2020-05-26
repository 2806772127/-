package com.fb.goldencudgel.auditDecisionSystem.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import com.fb.goldencudgel.auditDecisionSystem.domain.messageTemplate.MessageTemplate;
import com.fb.goldencudgel.auditDecisionSystem.repository.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.StringBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.comwave.core.data.domain.QueryPage;
import com.comwave.core.jpa.BaseJpaDAO;
import com.fb.goldencudgel.auditDecisionSystem.controller.questionaire.QuestionaireController;
import com.fb.goldencudgel.auditDecisionSystem.domain.fbProdQuestion.FbProdQuestion;
import com.fb.goldencudgel.auditDecisionSystem.domain.fbProduct.FbProduct;
import com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.question.FbQuestionnaire;
import com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.question.FbQuestionnaireAnswer;
import com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.question.FbQuestionnaireDetail;
import com.fb.goldencudgel.auditDecisionSystem.domain.user.FbUser;
import com.fb.goldencudgel.auditDecisionSystem.schema.webSendMessage.WebSendMessageRq;
import com.fb.goldencudgel.auditDecisionSystem.schema.webSendMessage.WebSendMessageRs;
import com.fb.goldencudgel.auditDecisionSystem.service.IInterfaceService;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;


@Service
@Transactional
public class QuestionnaireServiceImpl extends BaseJpaDAO{
	
	private static final Logger logger = LoggerFactory.getLogger(QuestionnaireServiceImpl.class);

	@Autowired
	private QuestionnaireRepository questionnaireRepository;
	@Autowired
	private QuestionnaireDetailRepository questionnaireDetailRepository;

	@Autowired
	private QuestionnaireAnswerRepository answerRepository;

    @Autowired
	private FbProdQuestionRepository fbProdQuestionRepository;

    @Autowired
   	private MessageTemplateRepository messageTemplateRepository;

    @Autowired
   	private AuditConfigurationRepository auditConfigurationRepository;

    @Autowired
    private IInterfaceService interfaceService;

	@SuppressWarnings("unchecked")
	public QueryPage<Object[]> findByConditions(String questionName, Date startDate, Date endDate,
			QueryPage<Object[]> queryPage,String introduceType) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select fq.NAME,fq.DESCRIPTION,fq.CREATE_DATE,fq.IS_ENABLE,fq.ID ");
		sql.append("        ,case when (fq.IS_ENABLE = '0' and ifnull(fpq.PQ_ID,'') !='') then '0' else '1' end  ");
		sql.append("        ,AUDIT_STATUS,fq.QUESTIONNAIRE_TYPE  ");
		sql.append("   from FB_QUESTIONNAIRE fq ");
		sql.append("   left join FB_PROD_QUESTION fpq on fq.ID = fpq.QUESTION_ID ");
		sql.append("  where fq.QUESTION_TYPE='"+introduceType+"'");
		sql.append("    and fq.QUESTIONNAIRE_TYPE !='00'");
		sql.append("    AND IFNULL(fq.DELETE_FLAG, '') != '2'");

		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtils.isNoneBlank(questionName)) {
			sql.append(" and fq.NAME=:questionName");
			params.put("questionName", questionName);
		}
		if(startDate!=null) {
			sql.append(" and fq.CREATE_DATE >=:startDate");
			params.put("startDate", startDate);
		}
		if(endDate!=null) {
			sql.append(" and fq.CREATE_DATE <=:endDate");
			params.put("endDate", endDate);
		}
		sql.append(" order by fq.IS_ENABLE desc, fq.CREATE_DATE desc ");
		return findBySQL(sql,queryPage,params);
	}

	@SuppressWarnings("unchecked")
	public QueryPage<Object[]> findByConditionsForAuditor(String questionName, String startDate, String endDate,
			QueryPage<Object[]> queryPage, String checkState, String createUser) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select fq.NAME,fq.DESCRIPTION,fq.COMMIT_DATE,fq.IS_ENABLE,fq.ID ");
		sql.append("        ,case when (fq.IS_ENABLE = '0' and ifnull(fpq.PQ_ID,'') !='') then '0' else '1' end  ");
		sql.append("        ,fq.AUDIT_STATUS,fq.CREATE_USER,fq.ADD_DEL_UPDATE_FLAG,fq.CREATE_USER_CODE,fq.AUDIT_DATE,fq.AUDIT_RESULT,fq.QUESTIONNAIRE_TYPE,fq.QUESTION_TYPE  ");
		sql.append("   from FB_QUESTIONNAIRE fq ");
		sql.append("   left join FB_PROD_QUESTION fpq on fq.ID = fpq.QUESTION_ID ");
		sql.append("    where fq.QUESTIONNAIRE_TYPE !='00'");
		sql.append("    and IFNULL(fq.AUDIT_STATUS, '') != '' ");
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNoneBlank(questionName)) {
			sql.append(" and fq.NAME like '%"+ questionName +"%'");
		}
		if (StringUtils.isNoneBlank(startDate)) {
            sql.append(" and DATE_FORMAT("+ ("01".equals(checkState) ? "fq.AUDIT_DATE" : "fq.COMMIT_DATE") +",'%Y/%m/%d') >=DATE_FORMAT('" +startDate+"','%Y/%m/%d')");
		}
		if (StringUtils.isNoneBlank(endDate)) {
            sql.append(" and DATE_FORMAT("+ ("01".equals(checkState) ? "fq.AUDIT_DATE" : "fq.COMMIT_DATE") +",'%Y/%m/%d') <=DATE_FORMAT('" +endDate+"','%Y/%m/%d')");
		}
		if (StringUtils.isNoneBlank(checkState)) {
			sql.append(" and fq.AUDIT_STATUS=:checkState");
			params.put("checkState", checkState);
		}
		if (StringUtils.isNoneBlank(createUser)) {
			sql.append(" and fq.CREATE_USER=:createUser");
			params.put("createUser", createUser);
		}
		FbUser curUser = UserUtil.getCurrUser();
		String userCode = curUser.getUserCode();
		String userType = auditConfigurationRepository.queryUserTypeByUserCode(userCode);
		if(!"CA".equals(userType)){
			sql.append(" and fq.AUDIT_CONFIG_USER=:userCode");
			params.put("userCode", userCode);
		}
		if ("01".equals(checkState)) {
			sql.append(" order by fq.AUDIT_DATE desc ");
		}
		if ("02".equals(checkState)) {
			sql.append(" order by fq.COMMIT_DATE desc ");
		}
		return findBySQL(sql, queryPage, params);
	}

	public void addQuestionnaire(FbQuestionnaire fbQuestionnaire) {
		fbQuestionnaire.setAuditStatus("02");
		fbQuestionnaire.setIsEnable("0");// 未审核通过新增的问卷都不启用
		fbQuestionnaire.setAddDelUpdateFlag("ADDFLAG");
		FbUser curUser = UserUtil.getCurrUser();
		fbQuestionnaire.setCreateUser(curUser.getUserName());// 提交人
		fbQuestionnaire.setCreateUserCode(curUser.getUserCode());
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		fbQuestionnaire.setCommitDate(dateFormat.format(date));
		fbQuestionnaire.setCreateDate(date);
		//审核配置审核人
		String functionCode = "01".equals(fbQuestionnaire.getIntroduceType()) ? "C0001" : "C0002";
		String acceptUser = auditConfigurationRepository.queryApproveUserByAgentUser(curUser.getUserCode(),functionCode);
		fbQuestionnaire.setAuditConfigUser(acceptUser);
		FbQuestionnaire questionnaire = questionnaireRepository.saveAndFlush(fbQuestionnaire);
		//查询正在启用的问卷
		FbQuestionnaire useQuestionnaire = questionnaireRepository.findByType(fbQuestionnaire.getQuestionniareType());
		List<FbQuestionnaireDetail> useDetailList = new ArrayList<>();
		if(useQuestionnaire!=null) {
			useDetailList = useQuestionnaire.getDetails();
			Collections.sort(useDetailList, new Comparator<FbQuestionnaireDetail>() {
				@Override
				public int compare(FbQuestionnaireDetail o1, FbQuestionnaireDetail o2) {
					return Integer.valueOf(o1.getSortNo()) - Integer.valueOf(o2.getSortNo());
				}
			});
		}
		for(FbQuestionnaireDetail detail : fbQuestionnaire.getDetails()) {
			//根据题目名称拉取正在使用的题目id
			for(FbQuestionnaireDetail useDetail : useDetailList) {
				if(detail.getName().equals(useDetail.getName())) {
					detail.setDefaultId(useDetail.getDefaultId());
					useDetailList.remove(useDetail);
					break;
				}
			}
			detail.setQuestionnaire(questionnaire);
			detail.setSortNo(String.valueOf(fbQuestionnaire.getDetails().indexOf(detail) +1));
			if(detail.getQuestionLevel().equals("1")){
				detail.setFatherQuestion(null);
			}
			//保存题目
			FbQuestionnaireDetail newDetail = questionnaireDetailRepository.saveAndFlush(detail);
			//保存答案
			List<FbQuestionnaireAnswer> answerList = detail.getAnswers();
			if(answerList!= null && answerList.size()>0) {
				for(FbQuestionnaireAnswer answer : answerList){
					answer.setQuestion(newDetail);
					answerRepository.saveAndFlush(answer);
				}
			}
			//默认id为空的 更新为id
			questionnaireDetailRepository.updateDefaultId(questionnaire.getId());
		}

		//发送消息
		sendMessages(questionnaire,"temp015",acceptUser);
	}

    public void sendMessages(FbQuestionnaire questionnaire,String templateId,String acceptUser){
    	//消息推送
		Map<String,String> keyValue = new HashMap<>();
		keyValue.put("{introduceType}",questionnaire.getIntroduceType());
		keyValue.put("{questionnaireId}",questionnaire.getId());
		keyValue.put("{addDelUpdateFlag}",questionnaire.getAddDelUpdateFlag());
		keyValue.put("{auditRemark}", StringUtils.isNoneBlank(questionnaire.getAuditRemark()) ? questionnaire.getAuditRemark() : "無");
		keyValue.put("{auditResult}", "01".equals(questionnaire.getAuditResult()) ? "已" : "不");
		keyValue.put("{questionnaireName}", questionnaire.getName());
        WebSendMessageRs webSendMessageRs = interfaceService.sendMessage(templateId,keyValue,acceptUser,"03","1");
        if (webSendMessageRs != null) {
            String returnCode = webSendMessageRs.getReturnCode();
            String returnMessage = webSendMessageRs.getReturnMessage();
            logger.info("問卷審核消息推送響應編碼：" + returnCode);
            logger.info("問卷審核消息推送響應內容：" + returnMessage);
        }
    }

	public FbQuestionnaire findById(String questionnaireId) {
		FbQuestionnaire fbQuestionnaire = questionnaireRepository.findByQuestionnaireId(questionnaireId);
		if(fbQuestionnaire!=null) {
			List<FbQuestionnaireDetail> detials = fbQuestionnaire.getDetails();
			Collections.sort(detials, new Comparator<FbQuestionnaireDetail>() {
				public int compare(FbQuestionnaireDetail d1, FbQuestionnaireDetail d2) {
					int diff = Integer.parseInt(d1.getSortNo()) - Integer.parseInt(d2.getSortNo());
					if (diff > 0) {
						return 1;
					} else if (diff < 0) {
						return -1;
					}
					return 0;
				}
			});
			fbQuestionnaire.setDetails(detials);
		}
		return fbQuestionnaire;
	}

	public void delteDetail(List<FbQuestionnaireDetail> detail) {
		if(detail!=null && detail.size()>0) {
			List<FbQuestionnaireAnswer> removeList = new ArrayList<FbQuestionnaireAnswer>();
			for(int i=0;i<detail.size();i++) {
				FbQuestionnaireDetail del = detail.get(i);
				if(del!=null) {
					List<FbQuestionnaireAnswer> answers = del.getAnswers();
					if(answers!=null && answers.size()>0) {
						removeList.addAll(answers);
					}
				}
			}
			answerRepository.deleteAll(removeList);
			questionnaireDetailRepository.deleteAll(detail);
		}
	}

	public void updateQuestionnaire(FbQuestionnaire fbQuestionnaire) {
		//新增一套問卷
		fbQuestionnaire.setCopyOriginalQuestionnaireId(fbQuestionnaire.getId());
		fbQuestionnaire.setId(null);
		FbUser curUser = UserUtil.getCurrUser();
		fbQuestionnaire.setCreateUser(curUser.getUserName());// 提交人
		fbQuestionnaire.setCreateUserCode(curUser.getUserCode());
		fbQuestionnaire.setAuditStatus("02");
		fbQuestionnaire.setIsEnable("0");// 不启用
		fbQuestionnaire.setAddDelUpdateFlag("UPDATEFLAG");
		Date date = new Date();
		fbQuestionnaire.setCreateDate(date);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		fbQuestionnaire.setCommitDate(dateFormat.format(date));
		//审核配置审核人
		String functionCode = "01".equals(fbQuestionnaire.getIntroduceType()) ? "C0001" : "C0002";
		String acceptUser = auditConfigurationRepository.queryApproveUserByAgentUser(curUser.getUserCode(),functionCode);
		fbQuestionnaire.setAuditConfigUser(acceptUser);//審核人
		FbQuestionnaire questionnaire = questionnaireRepository.saveAndFlush(fbQuestionnaire);
		for(FbQuestionnaireDetail detail : fbQuestionnaire.getDetails()) {//新增題目
			String id = StringUtils.isNoneBlank(detail.getId()) ? detail.getId() : UUID.randomUUID().toString().replaceAll("-", "");
			//默认ID为空才更新 防止默认值变化
			if(StringUtils.isBlank(detail.getDefaultId()))
				detail.setDefaultId(id);
			detail.setId(null);
			detail.setQuestionnaire(questionnaire);
			detail.setSortNo(String.valueOf(fbQuestionnaire.getDetails().indexOf(detail) +1));
			if(detail.getQuestionLevel().equals("1")){
				detail.setFatherQuestion(null);
			}
			FbQuestionnaireDetail newDetail = questionnaireDetailRepository.saveAndFlush(detail);
			List<FbQuestionnaireAnswer> answerList = detail.getAnswers();
			if(answerList!= null && answerList.size()>0) {//新增答案
				for(FbQuestionnaireAnswer answer : answerList){
					answer.setId(null);
					answer.setQuestion(newDetail);
					answerRepository.saveAndFlush(answer);
				}
			}
		}
		sendMessages(fbQuestionnaire,"temp015",acceptUser);
	}

	//審核問卷
	public void auditQuestionnaire(String checkResult, String auditRemark,String questionnaireId) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		// 修改审核
		FbQuestionnaire fbQuestionnaireForAudit = questionnaireRepository.findByQuestionnaireId(questionnaireId);
		fbQuestionnaireForAudit.setAuditResult(checkResult);
		fbQuestionnaireForAudit.setAuditRemark(auditRemark);
		fbQuestionnaireForAudit.setAuditStatus("01");
		FbUser curUser = UserUtil.getCurrUser();
		fbQuestionnaireForAudit.setAuditUserCode(curUser.getUserCode());
		fbQuestionnaireForAudit.setAuditUser(curUser.getUserName());
		fbQuestionnaireForAudit.setAuditDate(dateFormat.format(new Date()));
		if("01".equals(checkResult)) {//通過
            //更新、新增问卷审核通过
            if (!"DELETEFLAG".equals(fbQuestionnaireForAudit.getAddDelUpdateFlag())) {
                //更新題目id為默認id 用來帶出歷史答案（有的問卷是根據id查詢）
                // 講舊問卷id更新為新的uuid 用來審核通過的問卷id使用
				List<FbQuestionnaireDetail> details = new ArrayList<>();
                FbQuestionnaire useQuestionnaire = questionnaireRepository.findByType(fbQuestionnaireForAudit.getQuestionniareType());
				FbQuestionnaire UpQuestionnaire = questionnaireRepository.findByQuestionnaireId(fbQuestionnaireForAudit.getCopyOriginalQuestionnaireId());
                if(useQuestionnaire!=null){//更新启用问卷
					details.addAll(useQuestionnaire.getDetails());
					useQuestionnaire.getDetails().clear();
				}
				if(UpQuestionnaire!=null){//更新修改前问卷
					details.addAll(UpQuestionnaire.getDetails());
					UpQuestionnaire.getDetails().clear();
				}
				updateDetail(details);
                // 更新新問卷id 為默認id
                for (FbQuestionnaireDetail detail : fbQuestionnaireForAudit.getDetails()) {
                    answerRepository.updateQuestionId(detail.getDefaultId(), detail.getId());
                }
                questionnaireDetailRepository.updateIdUseDefaultId(questionnaireId);
            }
            //更新问卷启用状态
			if ("DELETEFLAG".equals(fbQuestionnaireForAudit.getAddDelUpdateFlag())) {//刪除
				fbQuestionnaireForAudit.setDeleteFlag("2");
				fbQuestionnaireForAudit.setIsEnable("0");
			} else {
				//將其他同類型問卷設置為禁用
				questionnaireRepository.updateEnableStatus("0", fbQuestionnaireForAudit.getQuestionniareType());
				fbQuestionnaireForAudit.setIsEnable("1");
			}
        }
		questionnaireRepository.saveAndFlush(fbQuestionnaireForAudit);
		// 消息推送
		String acceptUser = fbQuestionnaireForAudit.getCreateUserCode();
		sendMessages(fbQuestionnaireForAudit,"temp016",acceptUser);
	}

	//更新题目id 反正默认id被占用
	public void updateDetail(List<FbQuestionnaireDetail> details){
		//備份新一套題目
		for (FbQuestionnaireDetail detail : details) {
			FbQuestionnaireDetail cpDetail = (FbQuestionnaireDetail) deepClone(detail);
			if(StringUtils.isBlank(cpDetail.getDefaultId()))
				cpDetail.setDefaultId(detail.getId());
			cpDetail.setId(null);
			FbQuestionnaireDetail newDetail = questionnaireDetailRepository.saveAndFlush(cpDetail);
			answerRepository.updateQuestionId(newDetail.getId(), detail.getId());
		}
		questionnaireDetailRepository.deleteAll(details);//刪除舊題目
	}

	@SuppressWarnings("unchecked")
	public List<Object> getRules() {
		Map<String,Object> params = new HashMap<String,Object>();
		StringBuffer sb = new StringBuffer("SELECT DISTINCT RULE_NAME FROM RULE_MAPPING ORDER BY RULE_NAME ASC");
		return findBySQL(sb, params).getContent();
	}

	@SuppressWarnings("unchecked")
	public List<Object> getApplys() {
		Map<String,Object> params = new HashMap<String,Object>();
		StringBuffer sb = new StringBuffer("SELECT DISTINCT APPLY_TABLE_NAME FROM APPLY_MAPPING ORDER BY APPLY_TABLE_NAME ASC");
		return findBySQL(sb, params).getContent();
	}

	@SuppressWarnings("unchecked")
	public List<Object> getMappingItem(String itemName, String type) {
		Map<String,Object> params = new HashMap<String,Object>();
		String tableName = "";
		String columName = "";
		String filerName = "";
		if("rule".equals(type)) {
			tableName = "RULE_MAPPING";
			columName = "RULE_ITEM";
			filerName = "RULE_NAME";
		}else {
			tableName = "APPLY_MAPPING";
			columName = "APPLY_ITEM";
			filerName = "APPLY_TABLE_NAME";
		}
		StringBuffer sb = new StringBuffer("SELECT DISTINCT "+columName+" FROM "+tableName+
				" WHERE "+filerName+" =:fileterName "+" ORDER BY "+columName+" ASC");
		params.put("fileterName", itemName);
		return findBySQL(sb, params).getContent();
	}
	public Object deepClone(Object obj){
    try {
       // 将对象写到流里
       ByteArrayOutputStream bo = new ByteArrayOutputStream();
       ObjectOutputStream oo = new ObjectOutputStream(bo);
       oo.writeObject(obj);
       // 从流里读出来
       ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
       ObjectInputStream oi = new ObjectInputStream(bi);
       return (oi.readObject());
    }
     catch (Exception e) {
      return null;
    }
  }
}
