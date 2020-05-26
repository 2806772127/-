package com.fb.goldencudgel.auditDecisionSystem.controller.measureWord;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.comwave.core.data.domain.QueryPage;
import com.fb.goldencudgel.auditDecisionSystem.domain.commons.SystemConfig;
import com.fb.goldencudgel.auditDecisionSystem.domain.fbZjxJcic.FbZjxJcic;
import com.fb.goldencudgel.auditDecisionSystem.domain.letterConsent.FbLetterConsentDetail;
import com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.FbMeasureWord;
import com.fb.goldencudgel.auditDecisionSystem.domain.queryJcicRecored.QueryJcicRecored;
import com.fb.goldencudgel.auditDecisionSystem.model.AjaxResut;
import com.fb.goldencudgel.auditDecisionSystem.model.User;
import com.fb.goldencudgel.auditDecisionSystem.repository.FbZjxJcicRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.MeasureWordRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.QueryJcicRecoredRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.SystemConfigRepository;
import com.fb.goldencudgel.auditDecisionSystem.schema.callZyjService.ZYJRq;
import com.fb.goldencudgel.auditDecisionSystem.schema.callZyjService.ZYJRs;
import com.fb.goldencudgel.auditDecisionSystem.service.IInterfaceService;
import com.fb.goldencudgel.auditDecisionSystem.service.IMissionStroke;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.LetterConsentServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.MeasureWordServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.utils.FileUtil;
import com.fb.goldencudgel.auditDecisionSystem.utils.NumberFormatUtils;
import com.fb.goldencudgel.auditDecisionSystem.utils.ObjectUtil;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;

@Controller
@RequestMapping("/measureWord")
public class MeasureWordController {
	
	private static final Logger logger = LoggerFactory.getLogger(MeasureWordController.class);
	
	private static final Integer PAGE_SIZE = 15;

	@Autowired
	private MeasureWordServiceImpl measureWordService;
	
	@Autowired
	private LetterConsentServiceImpl letterConsentService;

	@Autowired
	private IMissionStroke missionStroke;

	@Autowired
	private MeasureWordRepository measureWordRepository;

	@Autowired
	private FbZjxJcicRepository fbZjxJcicRepository;

	@Autowired
	private SystemConfigRepository systemConfigRepository;

	@Autowired
	private QueryJcicRecoredRepository queryJcicRecoredRepository;
	
	@Autowired
	private IInterfaceService interfaceService;
	
	@RequestMapping("/viewMeasureWords")
	public String viewMeasureWords(Model model,String compilationNo, String backFlag) {
		model.addAttribute("compilationNo", compilationNo);
		//权限
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

		return "measureWord/viewQueryMeasureWord";
	}
	
	@RequestMapping("/queryMeasureWords")
	public String queryMeasureWords(Model model,String compilationNo,String companyName,String startDate,String endDate,
			@RequestParam(required = false, defaultValue = "1") Integer curPage, String createUsers,String userGroups,String userAreas,@RequestParam(required = false, defaultValue = "0") int opcount,
									@RequestParam(required = false, defaultValue = "0") int qucount,String productName) {
		//权限
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

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			Date sDate = null;
			Date eDate = null;
			if(StringUtils.isNoneBlank(startDate)) {
				sDate = sdf.parse(startDate);
			}
			if(StringUtils.isNoneBlank(endDate)) {
				eDate = sdf.parse(endDate);
				// 结束日期设置为所选日期最后一秒
				Calendar endCalendar = Calendar.getInstance();
				endCalendar.setTime(eDate);
				endCalendar.add(Calendar.DAY_OF_MONTH, 1);
				endCalendar.add(Calendar.SECOND, -1);
				eDate = endCalendar.getTime();
			}
			QueryPage<Object[]> page = measureWordService.findByConditions(compilationNo,companyName,sDate,
					eDate,new QueryPage<Object[]>(curPage, PAGE_SIZE),createUsers,userGroups,userAreas,opcount,qucount,productName);

            List list=  page.getContent();

			for (int i = 0; i <list.size() ; i++) {
				Object[] obj= page.getContent().get(i);
				if (obj[5]==""||obj[5]==null){
					String ids=obj[6].toString();
					List meaword=measureWordRepository.findQuest(ids);
					obj[5]="有緣";
					for (int j = 0; j <meaword.size() ; j++) {
						Object[] mw= (Object[]) meaword.get(j);
						if("1".equals(mw[0].toString())){
							if (mw[1]!=""&&mw[1]!=null) {
								String val1 = mw[1].toString();
								try {
									if (Integer.parseInt(val1)>=4) {
										obj[5] = "無緣";
										continue;
									}
								}catch (Exception e){
									continue;
								}
							}
						}
						else if ("2".equals(mw[0].toString())) {
							if ("其他/個人/非供應商".equals(mw[1].toString())) {
								obj[5] = "無緣";
								continue;
							}
						}else if ("3".equals(mw[0].toString())) {
								if ("否".equals(mw[1].toString())) {
									obj[5] = "無緣";
									continue;
								}
						}else if ("4".equals(mw[0].toString())){
							if (mw[1]!=""&&mw[1]!=null){
								try {
								String val1=mw[1].toString();
								if(val1.equals(">=2次")) {
									obj[5] = "無緣";
									continue;
								}
							}catch (Exception e){
									continue;
							}
							}
						}
					}
				}
			}
			page.setContent(list);
			SystemConfig systemConfig = systemConfigRepository.findByID("TRUSTED_SITES");//受信任站點
			model.addAttribute("page", page);
			model.addAttribute("trustedSites", systemConfig == null ? "" : systemConfig.getKeyvalue());
			model.addAttribute("measureWordList", page.getContent());

		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		 
		return "measureWord/viewQueryMeasureWordList";
	}
	@RequestMapping("/viewMeasureWordDetail")
	public String viewMeasureWordDetail(Model model,String measureId,String produceName) {
		FbMeasureWord detail =measureWordService.findById(measureId);
		Date measureDate = detail.getMeasureDate();
		List<FbLetterConsentDetail> baseInfos = letterConsentService.findLetterDetail(detail.getCompilationNo(), measureDate);
		model.addAttribute("baseInfos", baseInfos);
		model.addAttribute("detail", detail);
		List<Object[]> list = new ArrayList<Object[]>();
		List<Object[]> list2 = new ArrayList<Object[]>();
		List<Object[]> list3 = new ArrayList<Object[]>();
//		for(int i=0;i<9;i++) {
//			Object[] obj = new Object[2];
//			obj[0]="問題"+i;
//			obj[1]="答案"+i;
//			list.add(obj);
//		}
		//初塞问卷，先查初篩問卷1，如果沒有則再查初篩問卷2
		list = letterConsentService.findQuestions(measureId,"01");
		List<String> questionTypes = new ArrayList<String>();
		 questionTypes.add("01");
		if (CollectionUtils.isEmpty(list)) {
		    list2 = letterConsentService.findQuestions(measureId,"03");
		    questionTypes.add("03");
		    list3 = letterConsentService.findQuestions(measureId,"14");
		    questionTypes.add("14");
		}else{
			list2 = letterConsentService.findQuestions(measureId,"03");
			 questionTypes.add("03");
			 list3 = letterConsentService.findQuestions(measureId,"14");
			 questionTypes.add("14");
		}
		// 将初筛问卷1中的金额部分格式为千分位
		if (!CollectionUtils.isEmpty(list)) {
            for (Object[] measure : list) {
                if (measure.length >= 2 && NumberFormatUtils.isNumber(measure[1])) {
                    measure[1] = NumberFormatUtils.formatNumStrToThousands(ObjectUtil.obj2String(measure[1]));
                }
            }
        }
		
		// 将初筛问卷2中的金额部分格式为千分位
		if (!CollectionUtils.isEmpty(list2)) {
            for (Object[] measure : list2) {
                if (measure.length >= 2 && NumberFormatUtils.isNumber(measure[1])) {
                    measure[1] = NumberFormatUtils.formatNumStrToThousands(ObjectUtil.obj2String(measure[1]));
                }
            }
        }
		
		// 将初筛问卷3中的金额部分格式为千分位
		if (!CollectionUtils.isEmpty(list3)) {
			for (Object[] measure : list3) {
				if (measure.length >= 2 && NumberFormatUtils.isNumber(measure[1])) {
					measure[1] = NumberFormatUtils.formatNumStrToThousands(ObjectUtil.obj2String(measure[1]));
				}
			}
		}

		model.addAttribute("list", list);
		model.addAttribute("list2", list2);
		model.addAttribute("list3", list3);
		model.addAttribute("questionTypes", questionTypes);

		// 测字问卷
		List<Object[]> measureList = letterConsentService.findQuestions(measureId,"02");
		// 将测字问卷中的金额部分格式为千分位
		if (!CollectionUtils.isEmpty(measureList)) {
		    for (Object[] measure : measureList) {
		        if (measure.length >= 2 && NumberFormatUtils.isNumber(measure[1])) {
		            measure[1] = NumberFormatUtils.formatNumStrToThousands(ObjectUtil.obj2String(measure[1]));
		        }
		    }
		}
		model.addAttribute("measureList", measureList);
		if(!StringUtils.isNoneBlank(produceName)||"undefined".equals(produceName)) {
			produceName = "P0001";
		}
		model.addAttribute("produceName", produceName);
		return "measureWord/viewMeasureWordDetail";
	}

	@RequestMapping("/showJcicDetail")
	public String showJcicDetail(Model model, HttpServletRequest request, String zixId){

		List<FbZjxJcic> fbZjxJcicList = fbZjxJcicRepository.findByzjxId(zixId);
		List<QueryJcicRecored> queryJcicRecoredList = new ArrayList<QueryJcicRecored>();
		User user = UserUtil.getCurrUser();
		String ip = request.getHeader("x-forwarded-for");
		List<FbMeasureWord> fbMeasureWordlist = measureWordRepository.findByZjxId(zixId);
		String companyCode = "";
		if(fbMeasureWordlist!=null && fbMeasureWordlist.size()>0)
			companyCode = fbMeasureWordlist.get(0).getCompilationNo();
		for(FbZjxJcic jcic : fbZjxJcicList) {
			int start = StringUtils.ordinalIndexOf(jcic.getUrl(),"/",3);
			jcic.setShowUrl(jcic.getUrl().substring(start));
			//查詢聯征數據log
			QueryJcicRecored queryJcicRecored = new QueryJcicRecored();
			queryJcicRecored.setAccount(user.getAccount());
			queryJcicRecored.setUserCode(user.getUserCode());
			queryJcicRecored.setUserName(user.getUserName());
			queryJcicRecored.setUserUnitCode("");
			queryJcicRecored.setCompilationNo(companyCode);
			queryJcicRecored.setZjxId(jcic.getZjxId());
			queryJcicRecored.setQitem(jcic.getqItem());
			queryJcicRecored.setOperationTime(new Date());
			queryJcicRecored.setIp(ip);
			queryJcicRecored.setCreateUser(user.getUserCode());
			queryJcicRecored.setCreateTime(new Date());
			queryJcicRecoredList.add(queryJcicRecored);
		}
		//添加log
		queryJcicRecoredRepository.saveAll(queryJcicRecoredList);

		model.addAttribute("fbZjxJcicList",fbZjxJcicList);
		return "measureWord/viewJcicDetail";
	}

	@RequestMapping("/checkImgByUrl")
	@ResponseBody
	public AjaxResut checkImgByUrl(Model model){
		AjaxResut result = new AjaxResut();
		FileUtil fileUtil = new FileUtil();
		User user = UserUtil.getCurrUser();
		//TB_SDATA/JCIC/WMIMAGE/[user單位代號]_[user員編].JPG
		SystemConfig systemConfig = systemConfigRepository.findByID("WATER_MARK_URL");//浮水印url
		String fileUrl = systemConfig.getKeyvalue()+ user.getMainUnit() + "_" + user.getUserCode() + ".JPG";
		Map<String,Object> fileMap = fileUtil.readFileByUrl(fileUrl);
		if((Integer)fileMap.get("fileSize")>0) {
			result.setReturnCode(true);
			result.setReturnMessage(fileUrl);
		}
		return result;
	}
	
    @RequestMapping("/resendMeasure")
    @ResponseBody
    public Map<String, Object> resendMeasure(Model model, String compilationNo, String compilationName, String trandId, String measureId, String userCode) {
        Map<String, Object> result = new HashMap<>();
        ZYJRq zyjRq = new ZYJRq();
        zyjRq.setCompCode(compilationNo);
        zyjRq.setCompName(compilationName);
        zyjRq.setTrandId(trandId);
        zyjRq.setMesaureId(measureId);
        zyjRq.setUserCode(userCode);
        ZYJRs zyjRs = interfaceService.measureWordReSend(zyjRq);
        if (zyjRs == null || StringUtils.isBlank(zyjRs.getCaseNo())) {
            result.put("returnCode", "9999");
            result.put("returnMsg", "測字館重送失敗");
        } else {
            result.put("returnCode", "0000");
            result.put("returnMsg", "測字館重送成功");
        }
        
        return result;
    }
}