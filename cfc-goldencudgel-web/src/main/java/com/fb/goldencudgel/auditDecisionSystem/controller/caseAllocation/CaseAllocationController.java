package com.fb.goldencudgel.auditDecisionSystem.controller.caseAllocation;

import com.comwave.core.data.domain.QueryPage;
import com.fb.goldencudgel.auditDecisionSystem.domain.custCaseInfo.CustCaseInfo;
import com.fb.goldencudgel.auditDecisionSystem.domain.itemData.ViewDataDictItem;
import com.fb.goldencudgel.auditDecisionSystem.model.AjaxResut;
import com.fb.goldencudgel.auditDecisionSystem.model.User;
import com.fb.goldencudgel.auditDecisionSystem.repository.FbProductRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.ViewDictItemRepository;
import com.fb.goldencudgel.auditDecisionSystem.service.ICaseAllocationService;
import com.fb.goldencudgel.auditDecisionSystem.utils.ExcelUtil;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/caseAllocation")
public class CaseAllocationController {

    @Autowired
    private ICaseAllocationService caseAllocationService;

    @Autowired
    private ViewDictItemRepository viewDictItemRepository;

    @Autowired
    private FbProductRepository productRepository;

    //案件分配页面
    @RequestMapping("/viewCaseAllocation")
    public String viewCaseAllocation(Model model){
        Map<String,String> cityList = caseAllocationService.getCityList();//縣市別
        Map<String, String> userAreaList = caseAllocationService.getAreaList();//經辦人區域
        Map<String, String> userList = caseAllocationService.getUserList();//經辦人列表
        model.addAttribute("cityList",cityList);
        model.addAttribute("userList",userList);
        model.addAttribute("userAreaList",userAreaList);
        model.addAttribute("handleFlag","1");//處理標註  1為處理 2已處理
        String userType = UserUtil.getCurrUser().getUserType();
        //判断不同角色显示不同页面
        return ("S".equals(userType) || "Z".equals(userType)) ? "caseAllocation/viewPendingCase" : "caseAllocation/viewCaseAllocation";
    }

    //选择縣市
    @RequestMapping("/choseCity")
    @ResponseBody
    public List<Map<String, String>> choseCity(Model model,String cityCode){
        List<Map<String, String>> areaList = caseAllocationService.choseCity(cityCode);//區域
        return areaList;
    }

    @RequestMapping("/queryCase")
    public String queryCase(Model model,String allocationStatus,String cityCode,String areaCode,String compCode
            ,String compName,String agent,String startDate,String endDate){
        String returnUrl = "0".equals(allocationStatus) ? "caseAllocation/viewCaseList" : "caseAllocation/viewAllocationCaseList";
        List<Object[]> caseList = caseAllocationService.queryCase(allocationStatus,cityCode,areaCode,compCode,compName,agent,startDate,endDate);
        model.addAttribute("caseList",caseList);
        return returnUrl;
    }

    @RequestMapping("/findAllocationUser")
    @ResponseBody
    public List<Object> findAllocationUser(String key,String type){
        List<Object> resultList = caseAllocationService.findAllocationUser(key,type);
        return resultList;
    }

    @RequestMapping("/saveAllocation")
    @ResponseBody
    public boolean saveAllocation(String userCode,String caseList){
        return caseAllocationService.saveAllocation(userCode,caseList);
    }

    @RequestMapping("/saveBlackList")
    @ResponseBody
    public boolean saveBlackList(String remark,String caseList){
        return caseAllocationService.saveBlackList(remark,caseList);
    }

    @RequestMapping("/caseRecycling")
    @ResponseBody
    public AjaxResut caseRecycling(String caseList){
        return caseAllocationService.caseRecycling(caseList);
    }

    @RequestMapping("/showCaseDetail")
    public String showCaseDetail(Model model,String caseNum){
        CustCaseInfo custCaseInfo =caseAllocationService.showCaseDetail(caseNum);
        List<ViewDataDictItem> organizationList = viewDictItemRepository.findByDictId("COM_ORGANIZATION");//組織形態
        List<ViewDataDictItem> positionList = viewDictItemRepository.findByDictId("POSITION");//稱謂
        List<ViewDataDictItem> marriageList = viewDictItemRepository.findByDictId("MARRIAGE_STATU");//負責人婚姻狀況
        List<ViewDataDictItem> hasHoursesFlagList = viewDictItemRepository.findByDictId("HAS_HOURSES_FLAG");//持有情形有無不動產
        List<ViewDataDictItem> sourceTypeList = viewDictItemRepository.findByDictId("SOURCE_TYPE");//請問你從哪裡得知本產品訊息
        List<ViewDataDictItem> enterSourceList = viewDictItemRepository.findByDictId("ENTER_SOURCE");//推荐方式
        model.addAttribute("organizationList",organizationList);
        model.addAttribute("positionList",positionList);
        model.addAttribute("marriageList",marriageList);
        model.addAttribute("hasHoursesFlagList",hasHoursesFlagList);
        model.addAttribute("sourceTypeList",sourceTypeList);
        model.addAttribute("enterSourceList",enterSourceList);
        model.addAttribute("custCaseInfo",custCaseInfo);
        return "caseAllocation/viewCaseDetail";
    }

    //案件待處理页面
    @RequestMapping("/viewPendingCase")
    public String viewPendingCase(Model model){
        Map<String,String> cityList = caseAllocationService.getCityList();//縣市別
        Map<String, String> userAreaList = caseAllocationService.getAreaList();//經辦人區域
        model.addAttribute("cityList",cityList);
        model.addAttribute("userAreaList",userAreaList);
        model.addAttribute("handleFlag","2");
        return "caseAllocation/viewPendingCase";
    }

    @RequestMapping("/queryPendingCase")
    public String queryPendingCase(Model model,String cityCode,String areaCode,String compCode,String compName,String allocationStartDate,
                                   String allocationEndDate,String applyStartDate,String applyEndDate,String handleStartDate,String handleEndDate,
                                   String handleFlag,String allocationType,@RequestParam(required = false, defaultValue = "1") Integer curPage){
        QueryPage<Object[]> page = caseAllocationService.queryPendingCase(cityCode,areaCode,compCode,compName,allocationStartDate,allocationEndDate,
                applyStartDate,applyEndDate,handleStartDate,handleEndDate,handleFlag,allocationType,new QueryPage<>(curPage,15));
        model.addAttribute("page",page);
        model.addAttribute("caseList",page.getContent());
        model.addAttribute("handleFlag",handleFlag);//1未處理 2已處理
        return "caseAllocation/viewPendingCaseList";
    }

    @RequestMapping("/handleCase")
    @ResponseBody
    public boolean handleCase(Model model,String caseNum){
        return caseAllocationService.handleCase(caseNum);
    }

    @RequestMapping("/exportCase")
    @ResponseBody
    public void exportCase(HttpServletResponse response, String allocationType, String cityCode, String areaCode, String compCode, String compName, String allocationStartDate, String allocationEndDate, String applyStartDate, String applyEndDate){
        List<Object[]> dataList = caseAllocationService.exportCaseData(allocationType,cityCode,areaCode,compCode,compName,allocationStartDate,allocationEndDate,
                applyStartDate,applyEndDate);
        String[] title1s = {"分配時間","進件時間","組別","經辦人","授信戶統編","授信戶名稱"};
        String[] title2s = {"分配時間","進件時間","組別","授信戶統編","授信戶名稱"};
        ExcelUtil.exportExcel(response,"案件查詢","案件查詢","2".equals(allocationType) ? title1s : title2s,dataList);
    }
}
