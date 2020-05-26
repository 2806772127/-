package com.fb.goldencudgel.auditDecisionSystem.controller.report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.comwave.core.data.domain.QueryPage;
import com.fb.goldencudgel.auditDecisionSystem.domain.commons.SystemConfig;
import com.fb.goldencudgel.auditDecisionSystem.domain.itemData.ViewDataDictItem;
import com.fb.goldencudgel.auditDecisionSystem.domain.report.FbReportConfig;
import com.fb.goldencudgel.auditDecisionSystem.model.User;
import com.fb.goldencudgel.auditDecisionSystem.repository.ViewDictItemRepository;
import com.fb.goldencudgel.auditDecisionSystem.service.IMissionStroke;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.ReportServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.utils.ObjectUtil;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;

/**
 * @author mazongjian
 * @createdDate 2019年3月21日 - 下午12:29:02 
 */
@Controller
@RequestMapping("/report")
public class ReportController {
    
    private static final Integer PAGE_SIZE = 20;
    
    @Autowired
    private ReportServiceImpl reportServiceImpl;
    
    @Autowired
    private IMissionStroke missionStroke;
    
    @Autowired
    private ViewDictItemRepository viewDictItemRepository;
    
    @RequestMapping("/reportIndex")
    public String reportIndex(Model model) {
        // 查詢日期標籤配置
        List<ViewDataDictItem> queryDateLabelList = viewDictItemRepository.findByDictId("QUERY_DATE_LABEL");
        // 查詢日期格式配置
        List<ViewDataDictItem> queryDateFormatList = viewDictItemRepository.findByDictId("QUERY_DATE_FORMAT");
        // 是否需要查詢區間
        List<ViewDataDictItem> reportQueryRegionList = viewDictItemRepository.findByDictId("REPORT_QUERY_REGION");
        // 是否需要下載
        List<ViewDataDictItem> reportDownloadList = viewDictItemRepository.findByDictId("REPORT_DOWNLOAD");
        // 是否需要下載
        List<ViewDataDictItem> reportCodeList = viewDictItemRepository.findByDictId("REPORT_CODE");
        
        Map<String, Object> roleMap = reportServiceImpl.queryRoleInfo();
        if (CollectionUtils.isEmpty(queryDateLabelList)) {
            queryDateLabelList = new ArrayList<>();
        }
        
        if (CollectionUtils.isEmpty(queryDateFormatList)) {
            queryDateFormatList = new ArrayList<>();
        }
        SystemConfig spotfireParam = reportServiceImpl.querySystemConfigById("SPOTFIRE_PARAM");
        
        
        model.addAttribute("queryDateLabelList", queryDateLabelList);
        model.addAttribute("queryDateFormatList", queryDateFormatList);
        model.addAttribute("reportQueryRegionList", reportQueryRegionList);
        model.addAttribute("reportDownloadList", reportDownloadList);
        model.addAttribute("spotfireParam", spotfireParam.getKeyvalue());
        model.addAttribute("reportCodeList", reportCodeList);
        model.addAttribute("roleMap", roleMap);
        return "report/reportIndex";
    }
    
    @RequestMapping("/queryReportConfigList")
    public String queryReportConfigList(Model model, String reportName, @RequestParam(required = false, defaultValue = "1") Integer curPage) {
        QueryPage<FbReportConfig> page = reportServiceImpl.findReportConfigByName(reportName, new QueryPage<FbReportConfig>(curPage, PAGE_SIZE));
        model.addAttribute("page", page);
        model.addAttribute("reportConfigList", page.getContent());
        return "report/queryReportConfigList";
    }
    
    @RequestMapping("/saveReportConfig")
    @ResponseBody
    public Map<String, Object> saveReportConfig(String reportCode, String reportName, String reportPath, String reportType, String queryDateLabel, String queryDateFormat,
                                                String reportQueryRegion, String reportDownload, String roleStr) {
        Map<String, Object> result = reportServiceImpl.saveReportConfig(reportCode, reportName, reportPath, reportType, queryDateLabel, queryDateFormat, reportQueryRegion, reportDownload, roleStr);
        return result;
    }
    
    @RequestMapping("/showSpotfire")
    public String showSpotfire(Model model, String reportPath, String reportCode, String reportName, String queryDateLabel, String queryDateFormat,
                               String reportQueryRegion, String reportDownload) {
        User user = UserUtil.getCurrUser();
        // 區域、組別、查詢下拉框
        Map<String,String> areaList = reportServiceImpl.getAreaList("");
        Map<String,String> groupList = null;
        Map<String,String> userList = null;
        
        // 如果是指标达成率报表，因为需要区长、组长，所以从USER_LIST中取值
        if ("ZBDCL".equals(reportCode)) {
            if ("M".equals(user.getUserType())) {
                //组别
                groupList = reportServiceImpl.getGroupList("");
                //业务员
                userList = reportServiceImpl.getUserList("", "");
            } else {
                //组别
                groupList = reportServiceImpl.getGroupList(user.getUserArea());
                //业务员
                userList = reportServiceImpl.getUserList(user.getUserArea(),user.getUserGroup());
            }
        } else {
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
        }
        
        model.addAttribute("areaList",areaList);
        model.addAttribute("groupList",groupList);
        model.addAttribute("userList",userList);
        
        ViewDataDictItem queryDateLabelDict = viewDictItemRepository.findViewDataDictItemCode("QUERY_DATE_LABEL", queryDateLabel);
        if (queryDateLabelDict != null) {
            model.addAttribute("queryDateLabelName", queryDateLabelDict.getItemName());
        } else {
            model.addAttribute("queryDateLabelName", "資料日期");
        }
        
        
        // 根據不同的角色傳給spotfire不同的條件
        String userType = user.getUserType();
        if ("Z".equals(userType) || "S".equals(userType)) {
            model.addAttribute("whereSql", user.getUserCode());
        } else if ("C".equals(userType)) {
            model.addAttribute("whereSql", user.getUserGroup());
        } else if ("A".equals(userType)) {
            model.addAttribute("whereSql", user.getUserArea());
        } else {
            model.addAttribute("whereSql", "");
        }
        
        // 報表path
        model.addAttribute("reportPath", reportPath);
        // 報表root地址
        SystemConfig spotfireServerRootUrlSystemConfig = reportServiceImpl.querySystemConfigById("SPOTFIRE_SERVER_ROOT_URL");
        // 報表版本
        SystemConfig spotfireApiVersionSystemConfig = reportServiceImpl.querySystemConfigById("SPOTFIRE_API_VERSION");
        model.addAttribute("spotfireServerRootUrl", spotfireServerRootUrlSystemConfig.getKeyvalue());
        model.addAttribute("spotfireApiVersion", spotfireApiVersionSystemConfig.getKeyvalue());
        
        // 日期查詢框的默認顯示時間
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date startDate = new Date();
        Date endDate = new Date();
        // 前一日
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        
        String startDateStr = formatter.format(calendar.getTime());
        String endDateStr = formatter.format(endDate);
        model.addAttribute("compareDate", startDateStr);
        model.addAttribute("startDateTmp", startDateStr);
        model.addAttribute("endDateTmp", startDateStr);
        
        // pdf名稱
        String reportPdfName = Math.abs(reportName.hashCode()) + user.getUserCode() + ObjectUtil.obj2String(user.getUserGroup()) + ObjectUtil.obj2String(user.getUserArea());
        String reportPdfPath = "/RecordFile/web/" + reportPdfName + ".pdf";
        String reportExcelPath = "/RecordFile/web/" + reportPdfName + ".xlsx";
        model.addAttribute("reportPdfName", reportPdfName);
        model.addAttribute("reportPdfPath", reportPdfPath);
        model.addAttribute("reportExcelPath", reportExcelPath);
        
        // 日期查詢格式
        model.addAttribute("queryDateFormat", queryDateFormat);
        String queryDateFormatter = "yyyy/MM/dd";
        if ("1".equals(queryDateFormat)) {
            queryDateFormatter = "yyyy";
        } else if ("2".equals(queryDateFormat)) {
            queryDateFormatter = "yyyy/MM";
        } else if ("3".equals(queryDateFormat)) {
            queryDateFormatter = "yyyy/MM/dd";
        }  else {
            queryDateFormatter = "yyyy/MM/dd";
            model.addAttribute("queryDateFormat", "3");
        }
        model.addAttribute("queryDateFormatter", queryDateFormatter);
        
        if ("null".equals(reportQueryRegion) || "undefined".equals(reportQueryRegion) || StringUtils.isBlank(reportQueryRegion)) {
            reportQueryRegion = "Y";
        }
        
        if ("null".equals(reportDownload) || "undefined".equals(reportDownload) || StringUtils.isBlank(reportDownload)) {
            reportDownload = "Y";
        }
        model.addAttribute("reportQueryRegion", reportQueryRegion);
        model.addAttribute("reportDownload", reportDownload);
        model.addAttribute("reportCode", reportCode);
        // 審查批保逾放率
        if ("SCPBYF".equals(reportCode)) {
            List<String> batchNoList = reportServiceImpl.queryBatchNoList();
            model.addAttribute("batchNoList", batchNoList);
            model.addAttribute("daiChanRate", "1.23");
            return "report/showSpotfireSCPBYF";
        }
        // 核准率
        if ("HZL".equals(reportCode)) {
            List<String> prgCodeList = reportServiceImpl.queryPrgCode();
            model.addAttribute("prgCodeList", prgCodeList);
            return "report/showSpotfireHZL";
        }
        
        // 案件进度房信贷
        if ("JDFXD".equals(reportCode)) {
            return "report/showSpotfireJDFXD";
        }
        
        // 指标达成率
        if ("ZBDCL".equals(reportCode)) {
            return "report/showSpotfireZBDCL";
        }
        
        // 工時警示表
        if ("GSJSB".equals(reportCode)) {
            Map<String, Object> stepMap = reportServiceImpl.queryStepMapFromGSJSB();
            model.addAttribute("stepMap", stepMap);
            return "report/showSpotfireGSJSB";
        }
        
        // 平均工時統計表
        if ("PJGSTJB".equals(reportCode)) {
            Map<String, Object> stepMap = reportServiceImpl.queryStepMapFromPJGSTJB();
            model.addAttribute("stepMap", stepMap);
            return "report/showSpotfirePJGSTJB";
        }
        
        if ("JDQD".equals(reportCode) || "JSQDY".equals(reportCode) || "JSQDN".equals(reportCode)) {
            return "report/showSpotfireQD";
        }

        return "report/showSpotfire";
    }
    
    @RequestMapping("/delReportConfig")
    @ResponseBody
    public Map<String, Object> delReportConfig(String reportId) {
        Map<String, Object> result = reportServiceImpl.delReportConfigById(reportId);
        return result;
    }
    
    @RequestMapping("/updateReportConfig")
    @ResponseBody
    public Map<String, Object> updateReportConfig(String reportId, String reportCode, String reportName, String reportPath, String reportType, String queryDateLabel, String queryDateFormat,
                                                  String reportQueryRegion, String reportDownload, String roleStr) {
        Map<String, Object> result = reportServiceImpl.updateReportConfigById(reportId, reportCode, reportName, reportPath, reportType, queryDateLabel, 
            queryDateFormat, reportQueryRegion, reportDownload, roleStr);
        return result;
    }
    
    /**
     * 获取组列表
     * @param model
     * @param areaCode
     * @return
     */
    @RequestMapping("/getGroupList")
    @ResponseBody
    public List<Map<String,String>> getGroupList(Model model, String areaCode) {
        List<Map<String, String>> resultList = new ArrayList<>();
        Map<String,String> groupList = reportServiceImpl.getGroupList(areaCode);
        Map<String, String> userList = reportServiceImpl.getUserList(areaCode, "");
        resultList.add(groupList);
        resultList.add(userList);
        return resultList;
    }
    
    /**
     * 获取业务员列表
     * @param model
     * @param areaCode
     * @param groupCode
     * @return
     */
    @RequestMapping("/getUserList")
    @ResponseBody
    public Map<String,String> getUserList(Model model ,String areaCode,String groupCode){
        Map<String,String> groupList = reportServiceImpl.getUserList(areaCode, groupCode);
        return groupList;
    }
}
