package com.fb.goldencudgel.auditDecisionSystem.controller.videoConference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.fb.goldencudgel.auditDecisionSystem.repository.VideoConferenceRepository;
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
import com.fb.goldencudgel.auditDecisionSystem.domain.commons.SystemConfig;
import com.fb.goldencudgel.auditDecisionSystem.domain.fbWebexConfig.FbWebexConfig;
import com.fb.goldencudgel.auditDecisionSystem.domain.message.Message;
import com.fb.goldencudgel.auditDecisionSystem.domain.videoConference.VideoConference;
import com.fb.goldencudgel.auditDecisionSystem.repository.FbWebexConfigRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.SystemConfigRepository;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.MessageServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.VideoConferenceServiceImpl;


@Controller
@RequestMapping("/videoConference")
public class VideoConferenceController {
	private static final Logger logger = LoggerFactory.getLogger(VideoConferenceController.class);
	private static final Integer PAGE_SIZE = 15;
	@Autowired
	private VideoConferenceServiceImpl videoConferenceService; 
    
    @Autowired
    private SystemConfigRepository systemConfigRepository;
    
    @Autowired
	private MessageServiceImpl messageService;

    @Autowired
	private FbWebexConfigRepository fbWebexConfigRepository;

	@Autowired
	private VideoConferenceRepository videoConferenceRepository;
	@RequestMapping("/viewVideoConference")
	public String viewVideoConference(Model model, String backFlag) {
	    model.addAttribute("backFlag", backFlag);
		return "videoConference/viewVideoConference";
	}
	
	@RequestMapping("/joinVideoConference") 
    public String joinVideoConference(Model model) {
        SystemConfig sysConfig = systemConfigRepository.findByID("WEBEX_URL");
        if (sysConfig == null) {
            model.addAttribute("webexUrl", "https://TFB-CMLF.webex.com");
        } else {
            model.addAttribute("webexUrl", sysConfig.getKeyvalue());
        }
        return "videoConference/joinVideoConference";
    }	
	@RequestMapping("/queryVideoConferences")
	public String queryVideoConferences(Model model,String keyNote,String startDate,String endDate,
			@RequestParam(required = false, defaultValue = "1") Integer curPage) {
		try { 
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			Date sDate = null;
			Date eDate = null;
			if(StringUtils.isNoneBlank(startDate)) {
				sDate = sdf.parse(startDate);
			}
			if(StringUtils.isNoneBlank(endDate)) {
				eDate = sdf.parse(endDate);
			}

		    QueryPage<VideoConference> page = videoConferenceService.findByConditions(keyNote,sDate,
		    		eDate,new QueryPage<VideoConference>(curPage, PAGE_SIZE));

		    model.addAttribute("page", page);
			model.addAttribute("curPage", curPage);
			model.addAttribute("videoList", page.getContent());
		}catch(Exception e) {
			logger.error(e.getMessage(), e);
		}

		return "videoConference/viewVideoConferenceList";

	}
	
	@RequestMapping("/viewAddVideoConference")
	public String viewAddVideoConference(Model model) {
	    logger.info("start add video conference");
		Map<String, String> areaList = messageService.getAreaList();
		List<FbWebexConfig> fbWebexConfigs=fbWebexConfigRepository.findAll();
		model.addAttribute("fbWebexConfigs", fbWebexConfigs);
        model.addAttribute("areaList", areaList);
        logger.info("end add video conference");
		return "videoConference/viewAddVideoConference";
	}
	
	
	@RequestMapping("/getParticipant")
	@ResponseBody
	public LinkedHashMap<String,Object> getParticipant(String type, String pType, String userArea){
		LinkedHashMap<String,Object> result = new LinkedHashMap<String,Object>();
		try {
			LinkedHashMap<String,String> participantList = videoConferenceService.getParticipant(type,pType,userArea);
			List<Object> list = new ArrayList<>();
			for(Map.Entry<String,String> participant : participantList.entrySet()) {
				list.add(participant);
			}
			result.put("success",true);
			result.put("data",list);
		}catch(Exception e) {
			logger.error(e.getMessage(), e);
			result.put("success",false);
			result.put("message","获取信息失败,請稍後重試");
		}
		return result;
	}

	//给视讯会议新增数据同时以咨询方式通知对方在咨询显示
	@RequestMapping("/addConference")
	@ResponseBody
	public Map<String,Object> addConference(HttpServletRequest request, VideoConference conf, String areaStr, String groupStr, String userStr, String webex) {
		Map<String,Object>  result = new HashMap<String,Object>();

		try {
			//判斷會議時間區間是否重複
			List<VideoConference> videoConferenceList = videoConferenceRepository.findByWebex(webex, conf.getStartDate());
			Boolean flag = videoConferenceList.stream().
					allMatch(
							(e) -> {
								if (Long.valueOf(
										e.getStartTime().replace(":", "")
								) <= Long.valueOf(
										conf.getStartTime().replace(":", ""))
										&& Long.valueOf(e.getEndTime().replace(":", ""))
										>Long.valueOf(
										conf.getStartTime().replace(":", ""))
										) {
									return false;
								}else if(Long.valueOf(
										e.getStartTime().replace(":", "")
								) <Long.valueOf(
										conf.getEndTime().replace(":", ""))
										&& Long.valueOf(e.getEndTime().replace(":", ""))
										>=Long.valueOf(
										conf.getEndTime().replace(":", ""))
										){
									return false;
								}
								else if(Long.valueOf(
										e.getStartTime().replace(":", "")
								) <Long.valueOf(
										conf.getEndTime().replace(":", ""))
										&& Long.valueOf(e.getStartTime().replace(":", ""))
										>=Long.valueOf(
										conf.getStartTime().replace(":", ""))
										){
									return false;
								}
								else if(Long.valueOf(
										e.getEndTime().replace(":", "")
								) <=Long.valueOf(
										conf.getEndTime().replace(":", ""))
										&& Long.valueOf(e.getEndTime().replace(":", ""))
										>Long.valueOf(
										conf.getStartTime().replace(":", ""))
										){
									return false;
								}
								return true;
							}
					);
			if (flag==false){
				result.put("success",false);
				result.put("message","該賬號在該時段內已經預約會議，不能重複預約");
				return result;
			}


			//接收人
			String accepts = videoConferenceService.addConference(conf, areaStr, groupStr, userStr, webex);
			result.put("success",true);
			result.put("message","提交成功");

			//日期格式化
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String meetStartDate = sdf.format(conf.getStartDate());
			String meetStartTime = conf.getStartTime();
			String contents = conf.getConferencecontext();

			//给咨询也新增数据
			Message msg = new Message();
			msg.setAccpectUser(accepts);
			msg.setSendTime(new Date());
			msg.setMessageType("03");
			msg.setMessageTypeName("系統通知");
			msg.setMessageKeyNote(conf.getConferenceKeyNote());
			msg.setMessageContext("您有一個會議通知，請於 "+meetStartDate+" "+meetStartTime+" 準時加入視訊會議 ");
			
			if (groupStr == "" || groupStr == null) {
				groupStr=messageService.getUsersizeArea(areaStr);
			}
			
			if (StringUtils.isBlank(userStr)) {
			    userStr = messageService.getUsersizeFromArea(areaStr, groupStr);
			}
			
			messageService.addMessage(msg, areaStr, groupStr, userStr);
			
			QueryPage<Object[]> pageContent = messageService.findByIsHasReadTime(new QueryPage<Object[]>(1, PAGE_SIZE));
			Object contentCount;
			if(pageContent.getContent().isEmpty()) {
				contentCount =0;
			}else{
				contentCount = pageContent.getContent().get(0);
			}
			result.put("contentCount", contentCount);
			setMessageCount(request, contentCount);
		}catch(Exception e) {
			logger.error(e.getMessage(), e);
			result.put("success",false);
			result.put("message","提交失敗,請稍後重試");
		}
		return result;
	}
	
	private void setMessageCount(HttpServletRequest request, Object contentCount) {
	HttpSession session = request.getSession(true);
	session.setAttribute("contentCount", contentCount);
    }
	
	@RequestMapping("/deleteVideoConference")
	@ResponseBody
	public Map<String,Object>  deleteVideoConference(String conferenceId){
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			videoConferenceService.deleteVideoConference(conferenceId);
			result.put("success",true);
			result.put("message","刪除成功");
		}catch(Exception e) {
			logger.error(e.getMessage(), e);
			result.put("success",false);
			result.put("message","刪除失敗,請稍後重試");
		}
		return result;
	}
	
	@RequestMapping("/attendVideoConference")
	public String attendVideoConference() {
		return "videoConference/attendVideoConference";
	}
}
