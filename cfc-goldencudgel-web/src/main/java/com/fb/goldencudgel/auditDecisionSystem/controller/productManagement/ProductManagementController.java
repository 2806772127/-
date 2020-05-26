package com.fb.goldencudgel.auditDecisionSystem.controller.productManagement;

import static org.hamcrest.CoreMatchers.nullValue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.comwave.core.data.domain.QueryPage;
import com.fb.goldencudgel.auditDecisionSystem.domain.fbProdQuestion.FbProdQuestion;
import com.fb.goldencudgel.auditDecisionSystem.domain.fbProduct.FbProduct;
import com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.question.FbQuestionnaire;
import com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.question.FbQuestionnaireAnswer;
import com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.question.FbQuestionnaireDetail;
import com.fb.goldencudgel.auditDecisionSystem.domain.message.Message;
import com.fb.goldencudgel.auditDecisionSystem.domain.user.FbUser;
import com.fb.goldencudgel.auditDecisionSystem.repository.AuditConfigurationRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.FbProdQuestionRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.FbProductRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.QuestionnaireAnswerRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.QuestionnaireDetailRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.QuestionnaireRepository;
import com.fb.goldencudgel.auditDecisionSystem.schema.webSendMessage.WebSendMessageRq;
import com.fb.goldencudgel.auditDecisionSystem.schema.webSendMessage.WebSendMessageRs;
import com.fb.goldencudgel.auditDecisionSystem.service.IDataDictService;
import com.fb.goldencudgel.auditDecisionSystem.service.IInterfaceService;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.MessageTemplateServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.ProductManagementServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.QuestionnaireServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;

@Controller
@RequestMapping("/product")
public class ProductManagementController {

    private final Logger logger = LoggerFactory.getLogger(ProductManagementController.class);
    private static final Integer PAGE_SIZE = 20;

    @Autowired
    private ProductManagementServiceImpl productManagementServiceImpl;
    
    @Autowired
    private FbProdQuestionRepository fbProdQuestionRepository;
    
    @Autowired
    private QuestionnaireRepository questionnaireRepository;
    
    @Autowired
    private QuestionnaireDetailRepository questionnaireDetailRepository;
    
    @Autowired
    private QuestionnaireAnswerRepository questionnaireAnswerRepository;
    
    @Autowired
	private IDataDictService dataDictService;
    
    @Autowired
	private MessageTemplateServiceImpl messageTemplateServiceImpl;
    
    @Autowired
	private QuestionnaireServiceImpl questionnaireService;
    
    @Autowired
    private FbProductRepository fbProductRepository;
    
    @Autowired
	private QuestionnaireAnswerRepository answerRepository;
    
	@Autowired
    private IInterfaceService interfaceService;
	
	@Autowired
	private AuditConfigurationRepository auditConfigurationRepository;
    
    

	@RequestMapping("/productManagement")
	public String productManagement(Model model) {
		String flag = "false";
		String user = UserUtil.getCurrUser().getUserCode();
		//审核页面权限控制
		/*List<String> hasProductCheckFlag = auditConfigurationRepository.queryByApproveUserCode(user);
		if (!hasProductCheckFlag.isEmpty()) {
			flag = "true";
		}*/
		model.addAttribute("flag", flag);
		return "productManagement/viewProductManagement";
	}
	@RequestMapping("/queryProductManagement")
	public String queryApplyIncom(Model model, String productName, String setItem, String checkState,
			@RequestParam(required = false, defaultValue = "1") Integer curPage) {
		String user = UserUtil.getCurrUser().getUserCode();
		String url = "productManagement/productManagementList";
		String flag = "false";
		QueryPage<Object[]> page;
		//审核页面控制
		/*List<String> hasProductCheckFlag = auditConfigurationRepository.queryByApproveUserCode(user);
		if (!hasProductCheckFlag.isEmpty()) {
			url = "productManagement/productCheckList";
			flag = "true";
		}*/
		List<String> agenUserCode = auditConfigurationRepository.queryAgentByApproveUserCode(user);
		if (flag.equals("true")) {
			page = productManagementServiceImpl.findByCheck(productName, setItem, checkState,agenUserCode,
					new QueryPage<Object[]>(curPage, PAGE_SIZE));
		} else {
			page = productManagementServiceImpl.findByProductName(productName,
					new QueryPage<Object[]>(curPage, PAGE_SIZE));
		}
		model.addAttribute("userCode", user);
		model.addAttribute("page", page);
		model.addAttribute("productManagementList", page.getContent());
		model.addAttribute("curPage", curPage);
		return url;
	}
	
	// 查看配置
	@RequestMapping("viewProductDetail")
	public String viewProductDetail(Model model, String productVersion, String prodName, String questionnaireType,
			String estimatedLaunchTime,String prodId) {
		String questionnaireTypeString = initializeMidTable(prodId,productVersion,estimatedLaunchTime);
		List<String> questionIds = fbProdQuestionRepository.queryProductVersion(productVersion);
		Map<String, String> questionNames = new LinkedHashMap<>();
		if (prodId.equals("P0001")) {
			String id1 = questionnaireRepository.findPcode("01");
			String id2 = questionnaireRepository.findPcode("03");
			String id3 = questionnaireRepository.findPcode("02");
			questionNames.put(id1, "初篩問卷1");
			questionNames.put(id2, "初篩問卷2");
			questionNames.put(id3, "額度問卷");
		} else {
			String id4 = questionnaireRepository.findPcode("14");
			questionNames.put(id4, "初篩問卷3");
		}
		model.addAttribute("questionNames", questionNames);
		List<String> questionType = fbProdQuestionRepository.queryTypes(productVersion);
		model.addAttribute("introduceType", questionType);
		try {
			prodName =java.net.URLDecoder.decode(prodName,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		model.addAttribute("prodName", prodName);
		model.addAttribute("questionnaireType", questionnaireTypeString);
		model.addAttribute("estimatedLaunchTime", estimatedLaunchTime);
		model.addAttribute("prodId", prodId);
		model.addAttribute("productVersion", productVersion);
		// 初始化下拉框信息
		setSelectInfo(model);
		return "productManagement/viewProductConfig";
	}
	
	// 修改配置
	@RequestMapping("EditProductDetail")
	public String EditProductDetail(Model model, String productVersion, String prodName, String questionnaireType,
			String estimatedLaunchTime, String prodId, String editFlag) {
		String questionnaireTypeString = initializeMidTable(prodId,productVersion,estimatedLaunchTime);
		List<String> questionIds = fbProdQuestionRepository.queryProductVersion(productVersion);
		Map<String, String> questionNames = new LinkedHashMap<>();
		if (prodId.equals("P0001")) {
			String midIdOne = fbProdQuestionRepository.findQuestionIdByType("01");
			String midIdTwo = fbProdQuestionRepository.findQuestionIdByType("03");
			String midIdThree = fbProdQuestionRepository.findQuestionIdByType("02");
			String id1 = questionnaireRepository.findPcode("01");
			String id2 = questionnaireRepository.findPcode("03");
			String id3 = questionnaireRepository.findPcode("02");
			String endIdOne;
			String endIdTwo;
			String endIdThree;
			if(midIdOne==null){
				endIdOne=id1;
			}else{
				endIdOne=midIdOne;
			}
			if(midIdTwo==null){
				endIdTwo=id2;
			}else{
				endIdTwo=midIdTwo;
			}
			if(midIdThree==null){
				endIdThree=id3;
			}else{
				endIdThree=midIdThree;
			}
			questionNames.put(endIdOne, "初篩問卷1");
			questionNames.put(endIdTwo, "初篩問卷2");
			questionNames.put(endIdThree, "額度問卷");
		} else {
			String midIdFour = fbProdQuestionRepository.findQuestionIdByType("14");
			String id4 = questionnaireRepository.findPcode("14");
			String endIdFour;
			if(midIdFour==null){
				endIdFour=id4;
			}else{
				endIdFour=midIdFour;
			}
			questionNames.put(endIdFour, "初篩問卷3");
		}
		model.addAttribute("questionNames", questionNames);
		List<String> questionType = fbProdQuestionRepository.queryTypes(productVersion);
		model.addAttribute("introduceType", questionType);
		try {
			prodName =java.net.URLDecoder.decode(prodName,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		model.addAttribute("prodName", prodName);
		model.addAttribute("questionnaireType", questionnaireTypeString);
		model.addAttribute("estimatedLaunchTime", estimatedLaunchTime);
		model.addAttribute("prodId", prodId);
		model.addAttribute("productVersion", productVersion);
		model.addAttribute("editFlag", editFlag);
		// 初始化下拉框信息
		setSelectInfo(model);
		return "productManagement/editProductDetail";
	}
	
	// 点击加载问卷
	// 修改配置
	@RequestMapping("loadFbQuestionnaire")
	public String loadFbQuestionnaire(Model model, String questionId) {
		Map<String, String> questionName = productManagementServiceImpl.getQuestionName(questionId);
		model.addAttribute("questionNames", questionName);
		FbQuestionnaire fbQuestionnaire = questionnaireService.findById(questionId);
		model.addAttribute("questionnaire", fbQuestionnaire);
		model.addAttribute("introduceType", "01");
		// 初始化下拉框信息
		setSelectInfo(model);
		return "productManagement/configCommon";
	}
	
	// 提交修改配置
	@RequestMapping("/updateProduct")
	@ResponseBody
	public Map<String, Object> updateProduct(Model model, FbQuestionnaire fbQuestionnaire, String prodName,
			String estimatedLaunchTime, String prodId, String productVersion, String queId, String LaunchTime,
			String commitId, String prodType) throws UnsupportedEncodingException {
		List<String> prodTypesList = fbProdQuestionRepository.findQuestionTypeStoredFalg(prodId);//储存的问卷类别
		List<String> queIdsList = fbProdQuestionRepository.findQuestionIdStoredFalg(prodId);
		//String[] LaunchTimes = LaunchTime.split(",");
		String[] commitIds = commitId.split(","); // 提交的问卷id
		List<String> commitIdsList = Arrays.asList(commitIds);// 提交的问卷id
		List<String> commitIdsArrryList = new ArrayList(commitIdsList);
		List<String> commitTypeList = new ArrayList<>();// 提交的问卷对应的类别 01 03
		for (int i = 0; i < commitIds.length; i++) {
			String commitType = commitIds[i];
			commitTypeList.add(questionnaireRepository.findTypeById(commitType));// 取出提交的问卷类别与储存的类别比较
		}
		
		for (int j = 0; j < prodTypesList.size(); j++) {//去除储存过的问卷类型id
			Iterator<String> it = commitIdsArrryList.iterator();
			while (it.hasNext()) {
				String item = it.next();
				if (commitTypeList.contains(prodTypesList.get(j))) {
					it.remove();
					break;
				}
			}
		}
		
		
		// 提交，把没有储存，但有勾选问卷的，进行类别复制一份一样的问卷
		//此方法是提交新增问卷，现在改成提交也不新增问卷
		/*for (int e = 0; e < commitIdsArrryList.size(); e++) {
			String primaryQueId = commitIdsArrryList.get(e);
			FbQuestionnaire fbQuestion = questionnaireRepository.findByQuestionnaireId(primaryQueId);
			List<FbQuestionnaireDetail> fbQuestionDetail = questionnaireDetailRepository
					.findDetailsByQId(fbQuestion.getId());
			FbQuestionnaire newFbQuestionnaire = new FbQuestionnaire();
			newFbQuestionnaire.setName(fbQuestion.getName());
			newFbQuestionnaire.setQuestionniareType(fbQuestion.getQuestionniareType());
			newFbQuestionnaire.setDescription(fbQuestion.getDescription());
			newFbQuestionnaire.setCreateDate(new Date());
			newFbQuestionnaire.setIsEnable("0");// 未审核通过置为未启用
			newFbQuestionnaire.setProdCode(fbQuestion.getProdCode());
			newFbQuestionnaire.setIntroduceType(fbQuestion.getIntroduceType());
			FbQuestionnaire back = questionnaireRepository.saveAndFlush(newFbQuestionnaire);
			String backId= back.getId();
			//深拷贝，复制一份一样的问卷
			List<FbQuestionnaireDetail> fbQuestionDetailClone = (List<FbQuestionnaireDetail>) deepClone(fbQuestionDetail);
			for(int i=0;i<fbQuestionDetailClone.size();i++){
				FbQuestionnaireDetail detail = fbQuestionDetailClone.get(i);
				String detailId = detail.getId();
				detail.setId(UUID.randomUUID().toString().replaceAll("-", ""));
				detail.setQuestionnaire(back);
				FbQuestionnaireDetail backDetail = questionnaireDetailRepository.saveAndFlush(detail);
				List<FbQuestionnaireAnswer> allAnswerList = answerRepository.findByQuestionId(detailId);
				List<FbQuestionnaireAnswer> allAnswerListClone = (List<FbQuestionnaireAnswer>) deepClone(allAnswerList);
				for(int j=0;j<allAnswerListClone.size();j++){
					FbQuestionnaireAnswer answer = allAnswerListClone.get(j);
					answer.setQuestion(backDetail);
					answer.setId(UUID.randomUUID().toString().replaceAll("-", ""));
					FbQuestionnaireAnswer fbQuestionnaireAnswer = questionnaireAnswerRepository.saveAndFlush(answer);
				}
			}
			String midCommitQuestionId = newFbQuestionnaire.getId();//新增的问卷的id
			String questionniareType = fbQuestion.getQuestionniareType();
			FbProdQuestion fbProdQuestion = fbProdQuestionRepository.findByQuestionniareType(questionniareType);
			if (fbProdQuestion == null) {
				FbProdQuestion info = new FbProdQuestion();
				info.setPqId(UUID.randomUUID().toString().replaceAll("-", ""));
				info.setVersionNum(productVersion);// 暂时还有问题
				info.setQuestionId(midCommitQuestionId);
				info.setQuestionType(newFbQuestionnaire.getQuestionniareType());
				info.setProdId(prodId);
				fbProdQuestionRepository.save(info);
			} else {
				FbProdQuestion info = new FbProdQuestion();
				info.setPqId(UUID.randomUUID().toString().replaceAll("-", ""));
				info.setVersionNum(productVersion);// 暂时还有问题
				info.setQuestionId(midCommitQuestionId);
				info.setQuestionType(newFbQuestionnaire.getQuestionniareType());
				info.setProdId(prodId);
				fbProdQuestionRepository.deleteByProdId(prodId);
				fbProdQuestionRepository.saveAndFlush(info);
			}
		}*/
		
		List<String> zanMindList = new ArrayList<>();// 储存的，有被选中的，在中间表插入数据
		for(int y=0;y<prodTypesList.size();y++){
			if(commitTypeList.contains(prodTypesList.get(y))){
				zanMindList.add(queIdsList.get(y));
			}
		}
		
		// 提交，把储存，有勾选问卷的，保存到中间表
		for (int e = 0; e < zanMindList.size(); e++) {
			String primaryQueId = zanMindList.get(e);
			FbQuestionnaire fbQuestion = questionnaireRepository.findByQuestionnaireId(primaryQueId);
			String questionniareType = fbQuestion.getQuestionniareType();
			FbProdQuestion fbProdQuestion = fbProdQuestionRepository.findByQuestionniareType(questionniareType);
			if (fbProdQuestion == null) {
				FbProdQuestion info = new FbProdQuestion();
				info.setPqId(UUID.randomUUID().toString().replaceAll("-", ""));
				info.setVersionNum(productVersion);// 暂时还有问题
				info.setQuestionId(primaryQueId);
				info.setQuestionType(questionniareType);
				info.setProdId(prodId);
				fbProdQuestionRepository.save(info);
			} else {
				FbProdQuestion info = new FbProdQuestion();
				info.setPqId(UUID.randomUUID().toString().replaceAll("-", ""));
				info.setVersionNum(productVersion);// 暂时还有问题
				info.setQuestionId(primaryQueId);
				info.setQuestionType(questionniareType);
				info.setProdId(prodId);
				fbProdQuestionRepository.deleteByProdId(prodId);
				fbProdQuestionRepository.save(fbProdQuestion);
			}
		}
		
		
		// 把产品表的审核状态，审核结果，备注置成null；需要重新审核
		FbProduct fbProduct = fbProductRepository.findProductByProdId(prodId);
		fbProduct.setAuditState("02");
		fbProduct.setCreateTime(new java.sql.Timestamp(new Date().getTime()));
		//fbProduct.setEstimatedLaunchTime(Timestamp.valueOf(estimatedLaunchTime.replace("/", "-") + ":00"));
		fbProduct.setCheckResult(null);
		fbProduct.setAuditRemark(null);
		String user = UserUtil.getCurrUser().getUserCode();
		fbProduct.setCreateUser(user);
		fbProductRepository.save(fbProduct);
		Map<String, Object> result = new HashMap<>();
		try {
			result.put("success", true);
			result.put("message", "提交成功");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.put("success", false);
			result.put("message", "提交失敗,請稍後重試");
		}
		return result;
	}

	// 修改配置储存
	@RequestMapping("/saveProduct")
	@ResponseBody
	public Map<String, Object> saveProduct(Model model, FbQuestionnaire fbQuestionnaire, String estimatedLaunchTime,
			String productVersion, String prodId,String zanFlag,String zanTemporaryStorage) throws UnsupportedEncodingException {
		for (int i = 0; i < fbQuestionnaire.getDetails().size(); i++) {
			String s2 = fbQuestionnaire.getDetails().get(i).getName();
			s2 = s2.replace("%2B", "+");
			fbQuestionnaire.getDetails().get(i).setName(s2);
		}
		Map<String, Object> result = new HashMap<>();
		try {
			String saveType = fbQuestionnaire.getQuestionniareType();
			if(zanFlag.equals("")){
				String questionId;
				String prodCode;
				String storedFlag = fbProdQuestionRepository.findStoredFlagByType(saveType);
				if(storedFlag==null){
					String[] questionIdAndProdCode = StorageProduct(fbQuestionnaire, estimatedLaunchTime, productVersion,//新增一份问卷，返回问卷id和类型
							prodId).split(",");
					 questionId = questionIdAndProdCode[0];
					 prodCode = questionIdAndProdCode[1];
					 
					//把新增的问卷id保存到中间表，有相同类型的问卷，更新问卷id
						FbProdQuestion midFbProdQuestion = fbProdQuestionRepository.findByQuestionniareType(prodCode);
						midFbProdQuestion.setQuestionId(questionId);
						midFbProdQuestion.setStoredFlag("1");
						fbProdQuestionRepository.save(midFbProdQuestion);
				}
				//储存过，但没有提交，只新增一份问卷
				String midQuestionId = fbProdQuestionRepository.findQuestionIdByType(saveType);
				FbQuestionnaire info = questionnaireRepository.findByQuestionnaireId(midQuestionId);
				info.setName(fbQuestionnaire.getName());
				info.setQuestionniareType(fbQuestionnaire.getQuestionniareType());
				info.setDescription(fbQuestionnaire.getDescription());
				info.setCreateDate(new Date());
				info.setIsEnable("0");// 未审核通过先把该储存的问卷禁用
				info.setProdCode(fbQuestionnaire.getQuestionniareType());
				info.setIntroduceType(fbQuestionnaire.getIntroduceType());
				info.setId(midQuestionId);
				FbQuestionnaire back = questionnaireRepository.saveAndFlush(info);
				back.setId(midQuestionId);
				back.setDetails(null);
				questionnaireDetailRepository.deleteByQuestionId(midQuestionId);
				/*questionnaireService.saveDetail(fbQuestionnaire.getDetails(), back);*/
				//報錯注釋 後續又用到再處理
				//questionnaireService.saveDetailForProduct(fbQuestionnaire.getDetails(), back);
				result.put("success", true);
				result.put("firstInsert", true);
				result.put("questionId", midQuestionId);
				result.put("prodCode", saveType);
				result.put("estimatedLaunchTime", estimatedLaunchTime);
				result.put("message", "儲存成功");	
			}else{
				String nowType = fbQuestionnaire.getQuestionniareType();
				String[] s = zanTemporaryStorage.split(",");
				List<String> prodTypesList = Arrays.asList(s);// 暂存过的问卷id和类型
				for(int j=0;j<prodTypesList.size();j++){
					String[] kStrings = prodTypesList.get(j).split("-");
					String kStringsId = kStrings[0];
					String kStringsType = kStrings[1];
					if(nowType.equals(kStringsType)){
						FbQuestionnaire info = new FbQuestionnaire();
						FbQuestionnaire info2 = questionnaireRepository.findByQuestionnaireId(kStringsId);
						info2.setName(fbQuestionnaire.getName());
						info2.setQuestionniareType(fbQuestionnaire.getQuestionniareType());
						info2.setDescription(fbQuestionnaire.getDescription());
						info2.setCreateDate(new Date());
						info2.setIsEnable("0");// 未审核通过先把该储存的问卷禁用
						info2.setProdCode(fbQuestionnaire.getProdCode());
						info2.setIntroduceType(fbQuestionnaire.getIntroduceType());
						info2.setId(kStringsId);
						FbQuestionnaire back = questionnaireRepository.saveAndFlush(info2);
						back.setId(kStringsId);
						back.setDetails(null);
						questionnaireDetailRepository.deleteByQuestionId(kStringsId);
						/*questionnaireService.saveDetail(fbQuestionnaire.getDetails(), back);*/
						//報錯注釋 後續又用到再處理
						//questionnaireService.saveDetailForProduct(fbQuestionnaire.getDetails(), back);
					}
				}
				result.put("success", true);
				result.put("estimatedLaunchTime", estimatedLaunchTime);
				result.put("message", "儲存成功");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.put("success", false);
			result.put("message", "儲存失敗,請稍後重試");
		}
		return result;
	}
	
	// 储存保存问卷ID ++++++//
	public String StorageProduct(FbQuestionnaire fbQuestionnaire, String estimatedLaunchTime, String productVersion,
			String prodId) {
		FbQuestionnaire info = new FbQuestionnaire();
		info.setName(fbQuestionnaire.getName());
		info.setQuestionniareType(fbQuestionnaire.getQuestionniareType());
		info.setDescription(fbQuestionnaire.getDescription());
		info.setCreateDate(new Date());
		info.setIsEnable("0");// 未审核通过先把该储存的问卷禁用
		info.setProdCode(fbQuestionnaire.getProdCode());
		info.setIntroduceType(fbQuestionnaire.getIntroduceType());
		FbQuestionnaire back = questionnaireRepository.saveAndFlush(info);
		/*questionnaireService.saveDetail(fbQuestionnaire.getDetails(), back);*/
		//報錯注釋 後續又用到再處理
		//questionnaireService.saveDetailForProduct(fbQuestionnaire.getDetails(), back);
		String questionId = back.getId();
		String prodCode = back.getQuestionniareType();
		return questionId + "," + prodCode;
	}
	
	// 更新上线时间
	@RequestMapping("/updateOnlineTime")
	public String updateOnlineTime(Model model, String updateTime, String productId) {
		FbProduct fbProduct = fbProductRepository.findProductByProdId(productId);
		fbProduct.setAuditState("02");
		List<FbProdQuestion> fbProdQuestionList =  fbProdQuestionRepository.findQuestionByProdId(productId);
		for(FbProdQuestion obj:fbProdQuestionList){
			 Timestamp launchTime = obj.getLaunchTime();
			  if(launchTime==null){
				  obj.setLaunchTime(Timestamp.valueOf(updateTime.replace("/", "-") + ":00"));
				  fbProdQuestionRepository.save(obj);
			  }
		}
		String checkResult = fbProduct.getCheckResult();
		if (checkResult.equals("01")) {
			fbProduct.setEstimatedLaunchTime(Timestamp.valueOf(updateTime.replace("/", "-") + ":00"));
			fbProduct.setOnlineState("01");
			fbProductRepository.save(fbProduct);
		}
		return "productManagement/viewProductManagement";
	}

	/**
	 * 初始化下拉框
	 * 
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
	
	// 审核
	@RequestMapping("viewAdudit")
	public String viewAdudit(Model model, String productVersion, String prodName, String questionnaireType,
			String estimatedLaunchTime, String editFlag, String prodId,String commitName) {
		//initializeMidTable(prodId,productVersion,estimatedLaunchTime);
		String questionnaireTypeString = initializeMidTable(prodId,productVersion,estimatedLaunchTime);
		List<String> questionIds = fbProdQuestionRepository.queryProductVersion(productVersion);
		Map<String, String> questionNames = productManagementServiceImpl.getQuestionNames(questionIds);
		model.addAttribute("questionNames", questionNames);
		List<String> questionType = fbProdQuestionRepository.queryTypes(productVersion);
		model.addAttribute("introduceType", questionType);
		try {
			prodName =java.net.URLDecoder.decode(prodName,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		model.addAttribute("prodName", prodName);
		model.addAttribute("questionnaireType", questionnaireTypeString);
		//model.addAttribute("questionnaireType", questionnaireType);
		model.addAttribute("estimatedLaunchTime", estimatedLaunchTime);
		model.addAttribute("CheckEditFlag", editFlag);
		model.addAttribute("authorityFlag", "authorityFlag");
		model.addAttribute("prodId", prodId);
		model.addAttribute("commitName", commitName);
		// 初始化下拉框信息
		setSelectInfo(model);
		return "productManagement/viewAdudit";
	}
	
	// 审核人查看
	@RequestMapping("viewAduitDetail")
	public String viewAduitDetail(Model model, String productVersion, String prodName, String questionnaireType,
			String estimatedLaunchTime, String editFlag, String prodId) {
		//initializeMidTable(prodId,productVersion,estimatedLaunchTime);
		String questionnaireTypeString = initializeMidTable(prodId,productVersion,estimatedLaunchTime);
		
		List<String> questionIds = fbProdQuestionRepository.queryProductVersion(productVersion);
		Map<String, String> questionNames = productManagementServiceImpl.getQuestionNames(questionIds);
		model.addAttribute("questionNames", questionNames);
		List<String> questionType = fbProdQuestionRepository.queryTypes(productVersion);
		FbProduct fbProduct = fbProductRepository.findProductByProdId(prodId);
		String checkResult = fbProduct.getCheckResult();
		String auditRemark = fbProduct.getAuditRemark();
		model.addAttribute("checkResult", checkResult);
		model.addAttribute("auditRemark", auditRemark);
		model.addAttribute("introduceType", questionType);// 需要修改一下，类别获取有问题
		try {
			prodName =java.net.URLDecoder.decode(prodName,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		model.addAttribute("prodName", prodName);
		model.addAttribute("questionnaireType", questionnaireTypeString);
		//model.addAttribute("questionnaireType", questionnaireType);
		model.addAttribute("estimatedLaunchTime", estimatedLaunchTime);
		model.addAttribute("authorityFlag", "authorityFlag");
		// 初始化下拉框信息
		setSelectInfo(model);
		return "productManagement/viewAdudit";
	}
	
	// 审核提交
	@RequestMapping("commitForCheckPage")
	public String commitForCheckPage(Model model, String checkResult, String remark,String prodId,String commitName) {
		List<String> types = fbProdQuestionRepository.queryQuestionType(prodId);//中间表数据的问卷类别
		//这部分逻辑在定时器那边处理
		FbProduct fbProduct = fbProductRepository.findProductByProdId(prodId);
		fbProduct.setCheckResult(checkResult);
		try {
			remark =java.net.URLDecoder.decode(remark,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		fbProduct.setAuditRemark(remark);
		String typesString="";
		for(int i=0;i<types.size();i++) {
			typesString+=types.get(i)+",";
		}
		fbProduct.setQuestionnaireType(typesString.substring(0, typesString.length()-1));
		fbProduct.setAuditState("01");
		fbProductRepository.save(fbProduct);
		//消息推送
        FbUser curUser = UserUtil.getCurrUser();
        String templateId = "temp015";
        String key="";
        String value="";
        String resultValue = "";
        Map<String, String> messageTemplate = messageTemplateServiceImpl.getMessageTemplate(templateId);
        for(Entry<String, String> vo : messageTemplate.entrySet()){
        	 key = vo.getKey();
        	 value = vo.getValue();
        }
        if(value.contains("{companName}")){
        	if("01".equals(checkResult)) {
        	 resultValue = value.replace("{companName}", "已");	
        	}else {
        	 resultValue = value.replace("{companName}", "不");	
        	}
 		}
        WebSendMessageRq webSendMessageRq = new WebSendMessageRq();
        webSendMessageRq.setMessageTitle(key);
        webSendMessageRq.setMessageContext(resultValue);
        webSendMessageRq.setMessageType("03");
        webSendMessageRq.setSendUser(curUser.getUserCode());
        webSendMessageRq.setAcceptUser(commitName);
        webSendMessageRq.setIsPush("1");
        logger.info("問卷審核消息推送請求報文：" + JSON.toJSONString(webSendMessageRq));
        WebSendMessageRs webSendMessageRs = interfaceService.webSendMessage(webSendMessageRq);
        if (webSendMessageRs != null) {
            String returnCode = webSendMessageRs.getReturnCode();
            String returnMessage = webSendMessageRs.getReturnMessage();
            logger.info("問卷審核消息推送響應編碼：" + returnCode);
            logger.info("問卷審核消息推送響應內容：" + returnMessage);
        }
		return "productManagement/viewProductManagement";
	}
	
	// 点击加载问卷
	// 修改配置
	@RequestMapping("loadFbQuestionnaireForCheck")
	public String loadFbQuestionnaireForCheck(Model model, String questionId) {
		Map<String, String> questionName = productManagementServiceImpl.getQuestionName(questionId);
		model.addAttribute("questionNames", questionName);
		FbQuestionnaire fbQuestionnaire = questionnaireRepository.findByQuestionnaireId(questionId);
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
		model.addAttribute("questionnaire", fbQuestionnaire);
		model.addAttribute("introduceType", "01");
		// 初始化下拉框信息
		setSelectInfo(model);
		return "productManagement/configCommonForCheck";
	}
	
	//初始化中间表
	public String initializeMidTable(String prodId,String productVersion,String estimatedLaunchTime){
		List<String> hasMinQuestions = fbProdQuestionRepository.queryQuestionType(prodId);
		//还未提交配置问卷，中间表保存的是最初在用的问卷
		if(prodId.equals("P0001")){
			List<String> IntelligentType = Arrays.asList("01", "03","02");
			if(hasMinQuestions.isEmpty()){
			List<FbQuestionnaire> IntelligentList = questionnaireRepository.findByTypes(IntelligentType);
			for(FbQuestionnaire obj:IntelligentList){
				FbProdQuestion fbProdQuestion = new FbProdQuestion();
				fbProdQuestion.setPqId(UUID.randomUUID().toString().replaceAll("-", ""));
				fbProdQuestion.setVersionNum(productVersion);
				fbProdQuestion.setQuestionId(obj.getId());
				fbProdQuestion.setQuestionType(obj.getQuestionniareType());
				fbProdQuestion.setProdId(prodId);
				fbProdQuestion.setLaunchTime(Timestamp.valueOf(estimatedLaunchTime.replace("/", "-") + ":00"));
				fbProdQuestionRepository.save(fbProdQuestion);
			}
			}
		}else if(prodId.equals("P0002")){
			List<String> MerchantType = Arrays.asList("14");
			if(hasMinQuestions.isEmpty()){
			List<FbQuestionnaire> IntelligentList = questionnaireRepository.findByTypes(MerchantType);
			for(FbQuestionnaire obj:IntelligentList){
				FbProdQuestion fbProdQuestion = new FbProdQuestion();
				fbProdQuestion.setPqId(UUID.randomUUID().toString().replaceAll("-", ""));
				fbProdQuestion.setVersionNum(productVersion);
				fbProdQuestion.setQuestionId(obj.getId());
				fbProdQuestion.setQuestionType(obj.getProdCode());
				fbProdQuestion.setProdId(prodId);
				fbProdQuestion.setLaunchTime(Timestamp.valueOf(estimatedLaunchTime.replace("/", "-") + ":00"));
				fbProdQuestionRepository.save(fbProdQuestion);
			}
			}
		}
		//下次进来直接勾选中通过的文件类型
		List<String> midType = fbProdQuestionRepository.queryQuestionType(prodId);
		List<String> questionnaireTypeList = new ArrayList<>();
		String questionnaireTypeString="";
		for(int i=0;i<midType.size();i++){
			if(midType.get(i).equals("01")){
				questionnaireTypeList.add("初篩問卷1");
			}else if(midType.get(i).equals("02")){
				questionnaireTypeList.add("額度問卷");
			}else if(midType.get(i).equals("03")){
				questionnaireTypeList.add("初篩問卷2");
			}else if(midType.get(i).equals("14")){
				questionnaireTypeList.add("初篩問卷3");
			}
		}
		for(int j=0;j<questionnaireTypeList.size();j++){
			questionnaireTypeString+=questionnaireTypeList.get(j)+",";
		}
		questionnaireTypeString = questionnaireTypeString.substring(0, questionnaireTypeString.length()-1);
		return questionnaireTypeString;
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
	
    public void sendMessage(Message msg, String sendUser, String acceptUser) {
       
    }
	
}
