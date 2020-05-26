package com.fb.goldencudgel.auditDecisionSystem.controller.applyIncom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fb.goldencudgel.auditDecisionSystem.service.impl.AttacmentServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.comwave.core.data.domain.QueryPage;
import com.fb.goldencudgel.auditDecisionSystem.dao.FileContextDao;
import com.fb.goldencudgel.auditDecisionSystem.domain.LoanCompany.FbLoanCompany;
import com.fb.goldencudgel.auditDecisionSystem.model.User;
import com.fb.goldencudgel.auditDecisionSystem.repository.LoanCompanyRepository;
import com.fb.goldencudgel.auditDecisionSystem.service.IMissionStroke;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.ApplyIncomServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.AttacmentConfigServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.CreditReportImpl;
import com.fb.goldencudgel.auditDecisionSystem.utils.ObjectUtil;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;

@Controller
@RequestMapping("/ApplyIncom")
public class ApplyIncomController {

    private final Logger logger = LoggerFactory.getLogger(ApplyIncomController.class);
    private static final Integer PAGE_SIZE = 20;

    @Autowired
    private ApplyIncomServiceImpl applyIncomService;

    @Autowired
    private LoanCompanyRepository loanCompanyRepository;

    @Autowired
    private FileContextDao fileContextDao;

    @Autowired
    private AttacmentConfigServiceImpl attacmentConfigService;

    @Autowired
    private CreditReportImpl creditReportService;
    
    @Autowired
    private IMissionStroke missionStroke;

    @Autowired
    private AttacmentServiceImpl attacmentServiceImpl;

    @RequestMapping("/viewApplyIncom")
    public String indexApplyIncom(Model model, String backFlag){
    	
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

        return  "applyIncom/viewApplyIncom";
    }

    @RequestMapping("/queryApplyIncom")
    public  String  queryApplyIncom  ( Model model, String compilationNo, String companyName, String startDate, String endDate,
                                     @RequestParam(required = false, defaultValue = "1") Integer curPage,
                                     String areaCode,String groupCode,String userCode,String success){
    	try {
            if(StringUtils.isNoneBlank(startDate)){
            	 startDate =startDate.replace("/","-");
            }
            if(StringUtils.isNoneBlank(endDate)){
            	 endDate = endDate.replace("/","-");
            }
        }catch (Exception e)
        {
            logger.info(e.getMessage());
        }
    	QueryPage<Object[]> page = applyIncomService.findByConditions(compilationNo,companyName,startDate,
        		endDate,new QueryPage<Object[]>(curPage, PAGE_SIZE),areaCode,groupCode,userCode);


        model.addAttribute("page",page);
        model.addAttribute("applyIncomList",page.getContent());
        model.addAttribute("curPage",curPage);
        return "applyIncom/applyIncomList";
    }

    @RequestMapping("/seeApplyIncom")
    public  String  seeApplyIncom (Model model,String compilationNo,String trandId,String comIndustryCode,
    		@RequestParam(required = false, defaultValue = "1") Integer curPage){

        FbLoanCompany company = loanCompanyRepository.findByComplicationNo(compilationNo);
        if(null == company){
            company = new FbLoanCompany();
        }
        
        //进件详情
        QueryPage<Object[]> pages = applyIncomService.findIndustryNameByConditions(compilationNo,trandId,new QueryPage<Object[]>(curPage, PAGE_SIZE));
        //进件附件
        Map<Object,List<Object[]>> result = attacmentServiceImpl.findAttach(compilationNo,trandId,"2",comIndustryCode);
        model.addAttribute("musList",result.get("musList"));
        model.addAttribute("otherList",result.get("docList"));
        //去除图片格式外的附件
        result.remove("musList");
        result.remove("docList");
        model.addAttribute("imgList",result);
        model.addAttribute("comIndustryCode",comIndustryCode);
        model.addAttribute("company",company);
        model.addAttribute("pages",pages);
        model.addAttribute("applyIncomList",pages.getContent());

        return  "applyIncom/applyIncomDetail";
    }

    @RequestMapping("/editApplyIncom")
    public  String  editApplyIncom (Model model,String compilationNo,String trandId,String comIndustryCode, String applyNumber, @RequestParam(required = false, defaultValue = "1") Integer curPage){

        FbLoanCompany company =  loanCompanyRepository.findByComplicationNo(compilationNo);
        if( null == company ){
            company = new FbLoanCompany();
        }

        //需修改
        QueryPage<Object[]> pages = applyIncomService.findIndustryNameByConditions(compilationNo,trandId,new QueryPage<Object[]>(curPage, PAGE_SIZE));
        //进件附件
        Map<Object,List<Object[]>> result = attacmentServiceImpl.findAttach(compilationNo,trandId,"2",comIndustryCode);
        model.addAttribute("musList",result.get("musList"));
        model.addAttribute("otherList",result.get("docList"));
        //去除图片格式外的附件
        result.remove("musList");
        result.remove("docList");
        model.addAttribute("imgList",result);
        model.addAttribute("comIndustryCode",comIndustryCode);
        model.addAttribute("company",company);
        model.addAttribute("pages",pages);
        model.addAttribute("applyIncomList",pages.getContent());
        model.addAttribute("voiceLenth",result.get("musList") == null ? 0 : result.get("musList").size());
        model.addAttribute("trandId",trandId);
        model.addAttribute("applyNumber", applyNumber);
        model.addAttribute("curPage",curPage);
        //附件类型（弹窗）
        List<Object[]> checkItems = attacmentServiceImpl.findCheckItems("2",comIndustryCode,compilationNo);
        if (null == checkItems) {
            checkItems = new ArrayList<>();
        }
        model.addAttribute("checkItems", checkItems);
        return "applyIncom/editApplyIncom";
    }

    @ResponseBody
    @RequestMapping("/showView")
    public  String showView(String id){
        //test
        String url = creditReportService.getAttachUrl(id);
        int relativeUrlIndex = url.indexOf("/fileService");
        String relativeUrl = url.substring(relativeUrlIndex);
        return relativeUrl;
    }

    @RequestMapping("/changeAttachName")
    @ResponseBody
    public String changeAttachName(Model model, String checkItem,String comIndustryCode,String compCode) {
        //根据检查项查找附件名称
        String result = attacmentServiceImpl.findByCheckItems(checkItem,comIndustryCode,compCode,"2");
        return result;
    }
    

    /**附件上传*/
    @ResponseBody
    @RequestMapping(value = "/upload",method = {RequestMethod.POST})
    public Map<String,Object> upload(@RequestBody String[] fileStrs) throws IOException {
        Map<String,Object> map = new HashMap<>();
        if(null == fileStrs){
            map.put("flag",false);
            map.put("msg","附件上傳失敗");
        }else{
            map = applyIncomService.saveMongoFile(fileStrs);
        }

        return map;
    }


    @RequestMapping("/queryApplyRecords")
    public String queryCollectionRecords(Model model,Integer curPage) {
        model.addAttribute("curPage",curPage);
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


        return "applyIncom/viewApplyIncom";
    }

    @ResponseBody
    @RequestMapping("/updateCreditReport")
    public  Map<String,Object> updateSeeApplyIncom (Model model,String compilationNo,String trandId,
                                                    String imgIds,String deleImgIds, String applyNumber){
        if (trandId.equals("undefined")) {
            trandId="";
        }
        Map<String,Object> map = applyIncomService.saveAttachChange(compilationNo,trandId,imgIds,deleImgIds);
        if ("true".equals(map.get("flag").toString())) {
            map = applyIncomService.uploadAttach(compilationNo,trandId, applyNumber);
        }
            
        return map;
    }

    @RequestMapping("/viewOtherFile")
    public String  viewOtherFile(Model model,String otherDocName,String otherDocDate,String filesrc){
        model.addAttribute("otherDocName",otherDocName);
        model.addAttribute("otherDocDate",otherDocDate);
        model.addAttribute("filesrc",filesrc);
        return "applyIncom/viewOtherFile";
    }
}
