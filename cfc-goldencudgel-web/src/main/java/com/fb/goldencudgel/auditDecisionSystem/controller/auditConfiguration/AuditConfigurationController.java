package com.fb.goldencudgel.auditDecisionSystem.controller.auditConfiguration;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.comwave.core.data.domain.QueryPage;
import com.fb.goldencudgel.auditDecisionSystem.domain.auditConfiguration.AuditConfiguration;
import com.fb.goldencudgel.auditDecisionSystem.domain.itemData.ViewDataDictItem;
import com.fb.goldencudgel.auditDecisionSystem.repository.AuditConfigurationRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.QuestionnaireRepository;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.AuditConfigurationServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.MessageServiceImpl;

@Controller
@RequestMapping("/auditCOnfiguration")
public class AuditConfigurationController {

    private static final Integer PAGE_SIZE = 20;

    @Autowired
    private AuditConfigurationServiceImpl auditConfigurationServiceImpl;

    @Autowired
	private MessageServiceImpl messageService;
    
    @Autowired
   	private AuditConfigurationRepository auditConfigurationRepository;
	
	@Autowired
	private QuestionnaireRepository questionnaireRepository;
	
    @RequestMapping("/viewAuditCOnfiguration")
    public String viewNegativeIndustry(Model model){
    	Map<String, String> areaList = messageService.getAreaList();
        model.addAttribute("areaList", areaList);
        List<ViewDataDictItem> messageTypes = messageService.getMessageType();
        model.addAttribute("messageTypes", messageTypes);
        Map<String,String> functionList = auditConfigurationServiceImpl.getProductFunction();//功能从数据字典读取
        model.addAttribute("functionList", functionList);
        return  "auditConfiguration/viewAuditConfiguration";
    }

    @RequestMapping("/queryAuditConfiguration")
    public String queryItemData (Model model,String productCode,String agentUserCode,String agentUserName,String approveUserCode,String approveUserName,@RequestParam(required = false, defaultValue = "1") Integer curPage){
    	QueryPage<Object[]> page = auditConfigurationServiceImpl.findByConditions(productCode,agentUserCode,
    			agentUserName,approveUserCode,approveUserName,new QueryPage<Object[]>(curPage, PAGE_SIZE));
    	model.addAttribute("page", page);
		model.addAttribute("curPage", curPage);
		model.addAttribute("messageList", page.getContent());
       return "auditConfiguration/viewAuditConfigurationList";
    }
    
	// 新增
	@ResponseBody
	@RequestMapping("/saveNewAuditConfiguration")
	public String saveNewAuditConfiguration(String approveId, String functionCode, String functionName,
			String agentUserCode, String agentUserName, String approveUserCode, String approveUserName,
			String agentUserArea, String agentUserGroup, String approveUserArea, String approveUserGroup) {
		String result = "success";
		String auditState = "AuditState";
		try {
		    // 用传入的经办人参数作为审核人条件查询审核配置表，有记录则说明该经办人已经作为审核人存在记录了
			List<String> aprproveList = auditConfigurationRepository.queryByApproveUserCode(agentUserCode);
			// 用传入的审核人参数作为经办人条件查询审核配置表，有记录则说明该审核人已经作为经办人存在记录了
			List<String> agentList = auditConfigurationRepository.queryByAgentCode(approveUserCode);
			
			// 用经办人和功能类型查询审核配置表，当存在记录时
			// 1、新增配置时，提示配置已存在，因为同一个经办人同一个功能只能有一条记录
			// 2、修改配置时，判断此次修改的是否就是该经办人的记录。如果是，则允许修改。如果不是则提示配置已存在。
			String agentString = auditConfigurationRepository.queryApproveUserByAgentUser(agentUserCode, functionCode);
			if (StringUtils.isNotBlank(approveId)) {
			    AuditConfiguration oriAuditConfig = auditConfigurationRepository.querybyApproveId(approveId);
			    if (agentUserCode.equals(oriAuditConfig.getAgentUserCode())) {
			        agentString = "";
			    }
			}
			
			if (!StringUtils.isNoneBlank(approveId)) {
				if (!CollectionUtils.isEmpty(aprproveList) || !CollectionUtils.isEmpty(agentList) || !StringUtils.isEmpty(agentString)) {
					result = "error";
				} else {
					comMethod(approveId, functionCode, functionName, agentUserCode, agentUserName, approveUserCode,
							approveUserName, agentUserArea, agentUserGroup, approveUserArea, approveUserGroup);
				}
			} else {
				AuditConfiguration auditConfiguration1 = auditConfigurationRepository.querybyApproveId(approveId);
				String agent = auditConfiguration1.getAgentUserCode();// 原经办人
				String queType ="";
				if(functionCode.equals("C0001")) {
					queType = "01";
				}else{
					queType = "02";
				}
				List<String> state = questionnaireRepository.findByCreateUserCode(agent,queType);
				if (state.contains("02")) {
					result = "error"+auditState;
				}else{
					if (!CollectionUtils.isEmpty(aprproveList) || !CollectionUtils.isEmpty(agentList) || !StringUtils.isEmpty(agentString)) {
						result = "error";
					} else {
						comMethod(approveId, functionCode, functionName, agentUserCode, agentUserName, approveUserCode,
								approveUserName, agentUserArea, agentUserGroup, approveUserArea, approveUserGroup);
					}	
				}
			}
		} catch (Exception e) {
			result = "error";
			e.printStackTrace();
		}
		return result;
	}
    
    //删除
	@ResponseBody
	@RequestMapping("/deleteAuditCOnfiguration")
	public String deleteAuditCOnfiguration(String approveId,String agentUserCode) {
		String result = "success";
		try {
			AuditConfiguration auditConfiguration = auditConfigurationRepository.querybyApproveId(approveId);
			String approCode = auditConfiguration.getAgentUserCode();
			String functionCode = auditConfiguration.getFunctionCode();
			String queType ="";
			if(functionCode.equals("C0001")) {
				queType = "01";
			}else{
				queType = "02";
			}
			List<String> state = questionnaireRepository.findByCreateUserCode(approCode,queType);
			if(state.contains("02")){
				result = "error";
			}else {
				auditConfigurationRepository.deleteByApproveId(approveId);
			}
		} catch (Exception e) {
			result = "error";
			e.printStackTrace();
		}
		return result;
	}
	//公共方法
	public void comMethod(String approveId, String functionCode, String functionName,
			String agentUserCode, String agentUserName, String approveUserCode, String approveUserName,
			String agentUserArea, String agentUserGroup, String approveUserArea, String approveUserGroup){
		AuditConfiguration auditConfiguration = StringUtils.isNoneBlank(approveId)
				? auditConfigurationRepository.querybyApproveId(approveId) : new AuditConfiguration();
		auditConfiguration.setFunctionCode(functionCode);
		auditConfiguration.setFunctionName(functionName);
		auditConfiguration.setAgentUserCode(agentUserCode);
		auditConfiguration.setAgentUserName(agentUserName);
		auditConfiguration.setApproveUserCode(approveUserCode);
		auditConfiguration.setApproveUserName(approveUserName);
		auditConfiguration.setAgentUserArea(agentUserArea);
		auditConfiguration.setAgentUserGroup(agentUserGroup);
		auditConfiguration.setApproveUserArea(approveUserArea);
		auditConfiguration.setApproveUserGroup(approveUserGroup);
		auditConfiguration.setCreateTime(new Date());
		auditConfigurationRepository.saveAndFlush(auditConfiguration);
	}
}
