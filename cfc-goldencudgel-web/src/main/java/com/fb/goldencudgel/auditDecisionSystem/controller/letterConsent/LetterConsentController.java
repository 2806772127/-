package com.fb.goldencudgel.auditDecisionSystem.controller.letterConsent;



import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.comwave.core.data.domain.QueryPage;
import com.fb.goldencudgel.auditDecisionSystem.domain.letterConsent.FbLetterConsent;
import com.fb.goldencudgel.auditDecisionSystem.domain.letterConsent.FbLetterConsentDetail;
import com.fb.goldencudgel.auditDecisionSystem.model.User;
import com.fb.goldencudgel.auditDecisionSystem.service.IMissionStroke;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.CollectionServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.LetterConsentServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;

@Controller
@RequestMapping("/letterConsent")
public class LetterConsentController {

	private static final Logger logger = LoggerFactory.getLogger(LetterConsentController.class);
	
	private static final Integer PAGE_SIZE = 15;
	
	@Autowired
	private LetterConsentServiceImpl  letterConsentService;
	
    @Autowired
    private CollectionServiceImpl collectionService;

	@Autowired
	private IMissionStroke missionStroke;

	@RequestMapping("/viewLetterConsents")
	public String viewMeasureWords(Model model, String backFlag) {

		//权限控制
		User user = UserUtil.getCurrUser();

		//区域
		Map<String,String> areaList = missionStroke.getAreaList("");
		Map<String,String> groupList = null;
		Map<String,String> userList = null;
		if ("M".equals(user.getUserType())) {
			//组别
			groupList = missionStroke.getGroupList("");
			//业务员
			userList = missionStroke.getUserList("", "");
		} else {
			//组别
			groupList = missionStroke.getGroupList(user.getUserArea());
			//业务员
			userList = missionStroke.getUserList(user.getUserArea(),user.getUserGroup());
		}

		model.addAttribute("areaList",areaList);
		model.addAttribute("groupList",groupList);
		model.addAttribute("userList",userList);
		model.addAttribute("backFlag", backFlag);
		return "letterConsent/viewLetterConsents";
	}
	
	@RequestMapping("/queryLetterConsents")
	public String queryLetterConsents(Model model,String compilationNo,String customerName,String searchType,
			@RequestParam(required = false, defaultValue = "1") Integer curPage,String createUsers,String userGroups,String userAreas,String account ) {
		 QueryPage<FbLetterConsent> page = 
				 letterConsentService.findByConditions(compilationNo,customerName,searchType,
						 new QueryPage<FbLetterConsent>(curPage, PAGE_SIZE),createUsers,userGroups,userAreas,account);
		model.addAttribute("page", page);
		model.addAttribute("letteConsentList", page.getContent());

		return "letterConsent/viewLetterConsentList";
	}
	
	@RequestMapping("/queryLetterConsentDetail")
	public String queryLetterConsents(Model model,String letterId,
			@RequestParam(required = false, defaultValue = "1") Integer curPage) {
		 QueryPage<FbLetterConsentDetail> page = 
				 letterConsentService.findDetailByLetterId(letterId,
						 new QueryPage<FbLetterConsentDetail>(curPage, PAGE_SIZE));
		model.addAttribute("page", page);
		model.addAttribute("detailList", page.getContent());
		User user = UserUtil.getCurrUser();

		//区域
		Map<String,String> areaList = missionStroke.getAreaList("");
		Map<String,String> groupList = null;
		Map<String,String> userList = null;
		if ("M".equals(user.getUserType())) {
			//组别
			groupList = missionStroke.getGroupList("");
			//业务员
			userList = missionStroke.getUserList("", "");
		} else {
			//组别
			groupList = missionStroke.getGroupList(user.getUserArea());
			//业务员
			userList = missionStroke.getUserList(user.getUserArea(),user.getUserGroup());
		}

		model.addAttribute("areaList",areaList);
		model.addAttribute("groupList",groupList);
		model.addAttribute("userList",userList);

		return "letterConsent/viewLetterConsentDetailList";
	}
	
	@RequestMapping("/showImg")
	@ResponseBody
	public Map<String,Object> showImg(FbLetterConsentDetail detail,String attachId,String letterDetailId) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
		    String url = collectionService.getAttachUrl(attachId);
//			FileContext imgString = fileContextDao.findById(attachId);
			if(StringUtils.isBlank(url)) {
				result.put("message", "未找到附件信息!");
				result.put("success", false);
			}else {
//				String fileName = UUID.randomUUID().toString().replace("-", "").toLowerCase()+".jpg";
//		        FileUtil.upload(imgString.getFileContext(),fileName);
		        result.put("success", true);
//		        result.put("fileName", fileName);
		        FbLetterConsentDetail back = 
		        		letterConsentService.findByLetterDetailId(letterDetailId);
		        back.setAttachment(null);
		        result.put("data", back);
		        int relativeUrlIndex = url.indexOf("/fileService");
		        String relativeUrl = url.substring(relativeUrlIndex);
		        result.put("imgUrl", relativeUrl);
			}
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.put("message", "查找附件失敗!");
			result.put("success", false);
		}
		return result;
	}
}
