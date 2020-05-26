package com.fb.goldencudgel.auditDecisionSystem.controller.questionaire;

import com.comwave.core.data.domain.QueryPage;
import com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.question.FbQuestionnaire;
import com.fb.goldencudgel.auditDecisionSystem.domain.user.FbUser;
import com.fb.goldencudgel.auditDecisionSystem.repository.AuditConfigurationRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.QuestionnaireRepository;
import com.fb.goldencudgel.auditDecisionSystem.service.IDataDictService;
import com.fb.goldencudgel.auditDecisionSystem.service.IInterfaceService;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.MessageTemplateServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.QuestionnaireServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/questionnaire")
public class QuestionaireController {

	private static final Logger logger = LoggerFactory.getLogger(QuestionaireController.class);
	
	@Autowired
	private QuestionnaireServiceImpl questionnaireService;
	
	@Autowired
	private AuditConfigurationRepository auditConfigurationRepository;
	
    @Autowired
	private QuestionnaireRepository questionnaireRepository;

    @Autowired
   	private MessageTemplateServiceImpl messageTemplateServiceImpl;

	@Autowired
    private IInterfaceService interfaceService;

	private static final Integer PAGE_SIZE = 15;
	
	@Autowired
	private IDataDictService dataDictService;

	//测字馆訪查询页
	@RequestMapping("/viewQuestionnaires")
	public String viewQuestionnaires(Model model,String backFlag) {
		model.addAttribute("backFlag", backFlag);
		model.addAttribute("introduceType", "01");
		model.addAttribute("flag", "view");
		return "questionaire/viewQuestionnaires";
	}

	//徵信實訪查询页
	@RequestMapping("/viewIntroduces")
	public String viewIntroduces(Model model) {
		model.addAttribute("flag", "view");
		model.addAttribute("introduceType", "02");
		return "questionaire/viewIntroduces";
	}

	//审核问卷查询页
	@RequestMapping("/viewAuditQuestionaires")
	public String viewAuditQuestionaires(Model model,String backFlag) {
		model.addAttribute("flag", "view");
		model.addAttribute("backFlag", backFlag);//用於帶出歷史查詢條件
		return "questionaire/viewQuestionnairesForAuditor";
	}
	
	//问卷查询
	@RequestMapping("/queryQuestionaires")
	public String queryQuestionaires(Model model, String questionName, String startDate, String endDate,
			@RequestParam(required = false, defaultValue = "1") Integer curPage, String introduceType) {
		//01：测字馆 02：征信实访
		String returnUrl = "01".equals(introduceType) ? "questionaire/viewQuestionnairesList" : "questionaire/viewIntroduceList";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			Date sDate = null;
			Date eDate = null;
			if (StringUtils.isNoneBlank(startDate)) {
				sDate = sdf.parse(startDate);
			}
			if (StringUtils.isNoneBlank(endDate)) {
				eDate = sdf.parse(endDate);
			}
			QueryPage<Object[]> page = questionnaireService.findByConditions(questionName, sDate, eDate,
					new QueryPage<Object[]>(curPage, PAGE_SIZE), introduceType);
			model.addAttribute("page", page);
			model.addAttribute("questionList", page.getContent());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return returnUrl;
	}
	
	//问卷审核查询
	@RequestMapping("/querAuditQuestionaires")
	public String queryQuestionairesCredit(Model model,String questionName,String startDate,String endDate,
			@RequestParam(required = false, defaultValue = "1") Integer curPage,String checkState,String createUser) {
		try {
			QueryPage<Object[]> page = questionnaireService.findByConditionsForAuditor(questionName, startDate, endDate,
					new QueryPage<Object[]>(curPage, PAGE_SIZE), checkState, createUser);
			model.addAttribute("page", page);
			model.addAttribute("questionList", page.getContent());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "questionaire/viewQuestionnairesListForAuditor";
	}

	//新增问卷頁面
	@RequestMapping("/viewAddQuestionnaire")
	public String viewAddQuestionnaire(Model model, String introduceType) {
		model.addAttribute("flag", "add");
		model.addAttribute("introduceType", introduceType);
		// 初始化下拉框信息
		setSelectInfo(model);
		return "questionaire/viewAddQuestionnaire";
	}

	//新增問卷
	@RequestMapping("/addQuestionnaire")
	@ResponseBody
	public Map<String, Object> addQuestionnaire(Model model, FbQuestionnaire fbQuestionnaire){
		formatName(fbQuestionnaire);
		Map<String, Object> result = new HashMap<>();
		try {
			questionnaireService.addQuestionnaire(fbQuestionnaire);
			result.put("success", true);
			result.put("message", "提交成功");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.put("success", false);
			result.put("message", "提交失敗,請稍後重試");
		}
		return result;
	}

	//修改問卷詳情頁
	@RequestMapping("viewEditQuestionnaire")
	public String viewEditQuestionnaire(Model model, String questionnaireId, String introduceType, String forAuditFlag,
			String viewForAudit, String createUserCode, String auditStatus, String addDelUpdateFlag,String qName,String commitUserName,String startDate,
			String endDate,String checkState) {
		FbQuestionnaire fbQuestionnaire = questionnaireService.findById(questionnaireId);
		model.addAttribute("questionnaireId",questionnaireId);
		//返回带条件使用
		model.addAttribute("qName", qName);
		model.addAttribute("commitUserName", commitUserName);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("checkState", checkState);
		model.addAttribute("backFlag", "backFlag");
		model.addAttribute("flag", "edit");
		model.addAttribute("questionnaire", fbQuestionnaire);
		// 初始化下拉框信息
		setSelectInfo(model);
		model.addAttribute("introduceType", introduceType);
		model.addAttribute("forAuditFlag", forAuditFlag);
		model.addAttribute("viewForAudit", viewForAudit);
		// 修改说明,审核结果，审核备注
		String updateRemark = fbQuestionnaire.getUpdateRemark();
		String auditResult = fbQuestionnaire.getAuditResult();
		String auditRemark = fbQuestionnaire.getAuditRemark();
		if(!"viewForAudit".equals(viewForAudit)){
		  if(StringUtils.isNoneBlank(auditResult)){
		  auditResult = null;
			}
		  if(StringUtils.isNoneBlank(auditRemark)){
		  auditRemark = null;
		}	
		}
		// 修改前问卷
		String beforeQuestionId = fbQuestionnaire.getCopyOriginalQuestionnaireId();
		model.addAttribute("beforeQuestionId", beforeQuestionId);
		model.addAttribute("updateRemark", updateRemark);
		model.addAttribute("auditResult", auditResult);
		model.addAttribute("auditRemark", auditRemark);
		//审核人查看的审核人员和审核提交时间
		String auditUser = fbQuestionnaire.getAuditUser();
		String auditCommitTime = fbQuestionnaire.getAuditDate();
		model.addAttribute("auditUser", auditUser);
		model.addAttribute("auditCommitTime", auditCommitTime);
		
		if (createUserCode != null) {
			model.addAttribute("createUserCode", createUserCode);
		} else {
			model.addAttribute("createUserCode", UserUtil.getCurrUser().getUserCode());
		}
		model.addAttribute("auditStatus", auditStatus);// 维护人进入时带入审核状态，和问卷类型一起判断是否修改再新增一份问卷
		model.addAttribute("addDelUpdateFlag", addDelUpdateFlag);
		if ("forAuditFlag".equals(forAuditFlag) && "UPDATEFLAG".equals(addDelUpdateFlag)) {// 修改审核页面
			return "questionaire/viewEditQuestionnaireForAuditUpdate";
		}
		if ("forAuditFlag".equals(forAuditFlag) && "ADDFLAG".equals(addDelUpdateFlag)) {// 新增审核页面
			return "questionaire/viewEditQuestionnaireForAuditAdd";
		}
		if ("forAuditFlag".equals(forAuditFlag) && "DELETEFLAG".equals(addDelUpdateFlag)) {// 删除审核页面
			return "questionaire/viewEditQuestionnaireForAuditAdd";
		} else {
			return "questionaire/viewEditQuestionnaire";// 维护人修改页面
		}
	}

	//修改前问卷
	@RequestMapping("/queryBeforeQuestione")
	public String queryBeforeQuestione(Model model, String beforeQuestionId) {
		FbQuestionnaire fbQuestionnaire = questionnaireService.findById(beforeQuestionId);
		model.addAttribute("flag", "edit");
		model.addAttribute("questionnaireBefore", fbQuestionnaire);
		// 初始化下拉框信息
		setSelectInfo(model);
		String introduceType = fbQuestionnaire.getIntroduceType();
		model.addAttribute("introduceType", introduceType);
		return "questionaire/queryBeforeQuestione";
	}

	//修改問卷
	@RequestMapping("/updateQuestionnaire")
	@ResponseBody
	public Map<String, Object> updateQuestionnaire(Model model, FbQuestionnaire fbQuestionnaire){
		formatName(fbQuestionnaire);
		Map<String, Object> result = new HashMap<>();
		// 相同类型问卷在审核中，不可以再新增相同类型问卷
		List<FbQuestionnaire> qestionnaireList = questionnaireRepository.findInAuditByType(fbQuestionnaire.getQuestionniareType());
		if (qestionnaireList!= null && qestionnaireList.size()>0) {
			result.put("success", true);
			result.put("message", "該問卷類別下已有問卷正在審核中");
			return result;
		}
		try {
			questionnaireService.updateQuestionnaire(fbQuestionnaire);
			result.put("success", true);
			result.put("message", "更新成功");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.put("success", false);
			result.put("message", "更新失敗,請稍後重試");
		}
		return result;
	}

	//審核問卷
	@RequestMapping("/updateQuestionnaireAudit")
	@ResponseBody
	public Map<String, Object> updateQuestionnaireAudit(Model model, String checkResult, String auditRemark,String questionnaireId){
		Map<String, Object> result = new HashMap<>();
		questionnaireService.auditQuestionnaire(checkResult,auditRemark,questionnaireId);
		result.put("success", true);
		result.put("message", "審核完成");
		return result;
	}

	//刪除問卷
	@RequestMapping("/deleteQuestionnaire")
	@ResponseBody
	public Map<String, Object> deleteQuestionnaire(Model model, String questionnaireId) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			FbQuestionnaire fbQuestionnaire = questionnaireRepository.findByQuestionnaireId(questionnaireId);
			fbQuestionnaire.setAuditStatus("02");
			fbQuestionnaire.setAddDelUpdateFlag("DELETEFLAG");
			FbUser curUser = UserUtil.getCurrUser();
			fbQuestionnaire.setCreateUser(curUser.getUserName());// 提交人
			fbQuestionnaire.setCreateUserCode(curUser.getUserCode());
			//审核配置审核人
			String functionCode = "01".equals(fbQuestionnaire.getIntroduceType()) ? "C0001" : "C0002";
	    	String acceptUser = auditConfigurationRepository.queryApproveUserByAgentUser(curUser.getUserCode(),functionCode);
	    	fbQuestionnaire.setAuditConfigUser(acceptUser);
	    	Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	    	fbQuestionnaire.setCommitDate(dateFormat.format(date));
			result.put("success", true);
			result.put("message", "提交審核中");
			questionnaireService.sendMessages(fbQuestionnaire,"temp015",acceptUser);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.put("success", false);
			result.put("message", "删除失敗,請稍後重試");
		}
		return result;
	}
	
	/***
	 * 获取映射栏位
	 * @param model
	 * @param itemName
	 * @param type
	 * @return
	 */
	@RequestMapping("/getMappingItem")
	@ResponseBody
	public Map<String, Object> getMappingItem(Model model, String itemName, String type) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			List<Object> items = questionnaireService.getMappingItem(itemName, type);
			result.put("success", true);
			result.put("data", items);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.put("success", false);
			result.put("message", "操作失敗,請稍後重試");
		}
		return result;
	}

	@RequestMapping("/queryQuestionStatus")
	@ResponseBody
	public Map<String, Object> queryQuestionStatus(Model model, String queId) throws UnsupportedEncodingException {
		Map<String, Object> result = new HashMap<>();
		result.put("success", false);
		try {
			FbQuestionnaire fbQuestionnaire = questionnaireRepository.findByQuestionnaireId(queId);
			if (fbQuestionnaire != null) {
				String auditStatus = fbQuestionnaire.getAuditStatus();
				if (StringUtils.isNoneBlank(auditStatus)) {
					if ("01".equals(auditStatus)) {
						result.put("success", true);
					}
				}
				String flag = fbQuestionnaire.getAddDelUpdateFlag();
				result.put("message", flag);
			}
			if (fbQuestionnaire == null) {
				result.put("success", true);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.put("success", false);
			result.put("message", "提交失敗,請稍後重試");
		}
		return result;
	}

	public void formatName(FbQuestionnaire fbQuestionnaire){
		for (int i = 0; i < fbQuestionnaire.getDetails().size(); i++) {
			String s2 = fbQuestionnaire.getDetails().get(i).getName();
			s2 = s2.replace("%2B","+");
			fbQuestionnaire.getDetails().get(i).setName(s2);
		}
	}

	/**
	 * 初始化下拉框
	 * @param model
	 */
	private void setSelectInfo(Model model) {
		List<Object[]> prods = dataDictService.findProdList();
		model.addAttribute("prods", prods);

		List<Object> rules = questionnaireService.getRules();
		model.addAttribute("rules", rules);

		List<Object> applys = questionnaireService.getApplys();
		model.addAttribute("applys", applys);
	}
}
