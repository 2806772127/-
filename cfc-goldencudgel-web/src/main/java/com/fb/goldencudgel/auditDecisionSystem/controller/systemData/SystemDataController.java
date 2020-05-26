package com.fb.goldencudgel.auditDecisionSystem.controller.systemData;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.comwave.core.data.domain.QueryPage;
import com.fb.goldencudgel.auditDecisionSystem.schema.getReportData.GetReportDataRq;
import com.fb.goldencudgel.auditDecisionSystem.schema.getReportData.GetReportDataRs;
import com.fb.goldencudgel.auditDecisionSystem.service.IInterfaceService;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.SystemDataServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.utils.ObjectUtil;
import com.fb.goldencudgel.auditDecisionSystem.utils.ResttemplateUtile;

/**
 * @author mazongjian
 * @createdDate 2019年8月3日 - 下午9:10:59 
 */
@Controller
@RequestMapping("/systemData")
public class SystemDataController {
    
    private final static Logger logger = LoggerFactory.getLogger(SystemDataController.class);
    
    private static final Integer PAGE_SIZE = 18;
    
    @Autowired
    private IInterfaceService interfaceService;
    
    @Autowired
    private SystemDataServiceImpl systemDataServiceImpl;
    
    @RequestMapping("/viewSystemDataList")
    public String viewSystemDataList(Model model, String backFlag) {
        
        model.addAttribute("backFlag", backFlag);
        return "systemData/viewSystemDataList";
    }
    
    @RequestMapping("/querySystemDataList")
    public String querySystemDataList(Model model, String schemaName, String tableName, @RequestParam(required = false, defaultValue = "1") Integer curPage) {
        QueryPage<Object[]> systemDataListPage = systemDataServiceImpl.querySystemDataList(schemaName, tableName, new QueryPage<>(curPage, PAGE_SIZE));
        model.addAttribute("page", systemDataListPage);
        model.addAttribute("systemDataList", systemDataListPage.getContent());
        return "systemData/querySystemDataList";
    }
    
    @RequestMapping("/updateGenerate")
    @ResponseBody
    public Map<String, Object> updateGenerate(String schemaName, String tableName, String status) {
        Map<String, Object> result = new HashMap<>();
        try {
            systemDataServiceImpl.updateGenerateStatus(schemaName, tableName, status);
            result.put("code", true);
            result.put("msg", "更新成功");
        } catch (Exception e) {
            result.put("code", false);
            result.put("msg", "更新失敗");
            e.printStackTrace();
        }
        return result;
    }
    
    @RequestMapping("/generateData")
    @ResponseBody
    public Map<String, Object> generateData(String schemaName, String tableName) {
        Map<String, Object> result = new HashMap<>();
        try {
            GetReportDataRq getReportData = new GetReportDataRq();
            getReportData.setDataSource(schemaName);
            getReportData.setTableName(tableName);
            GetReportDataRs getReportDataRs = interfaceService.getReportData(getReportData);
            String filePath = getReportDataRs.getFilePath();
            
            QueryPage<Object[]> queryPage = systemDataServiceImpl.queryGenerateStatus(schemaName, tableName);
            List<Object[]> contentList = queryPage.getContent();
            String generateStatus = ObjectUtil.obj2String(contentList.get(0)[0]);
            String updateTime = ObjectUtil.obj2String(contentList.get(0)[2]);
            
            if (StringUtils.isEmpty(filePath) && "4".equals(generateStatus)) {
                systemDataServiceImpl.updateGenerateStatus(schemaName, tableName, "2");
                result.put("returnFlag", false);
                result.put("returnCode", generateStatus);
                result.put("returnMsg", "產制數據失敗");
                result.put("updateTime", updateTime);
            } else if (StringUtils.isEmpty(filePath) && "1".equals(generateStatus)) {
                result.put("returnFlag", false);
                result.put("returnCode", generateStatus);
                result.put("returnMsg", "產制數據中");
            } else if (!StringUtils.isEmpty(filePath) && "3".equals(generateStatus)) {
                result.put("returnFlag", true);
                result.put("returnMsg", "產制數據成功");
                result.put("returnCode", generateStatus);
                result.put("updateTime", updateTime);
            }
        } catch (Exception e) {
            result.put("returnFlag", false);
            result.put("returnMsg", "產制數據失敗");
            e.printStackTrace();
        }
        
        return result;
    }
    
    @RequestMapping("/downloadData")
    @ResponseBody
    public void downloadData(HttpServletRequest request, HttpServletResponse response, String schemaName, String tableName) {
        QueryPage<Object[]> systemConfigPage = systemDataServiceImpl.querySystemConfig("('GET_REPORT_FILE_URL', 'GET_REPORT_FILE_PATH')");
        List<Object[]> systemConfigRows = systemConfigPage.getContent();
        if (CollectionUtils.isEmpty(systemConfigRows) || systemConfigRows.size() != 2) {
            logger.info("===>" + tableName + "匯出路徑獲取失敗");
        } else {
            Object[] urlObj = systemConfigRows.get(1);
            String url = ObjectUtil.obj2String(urlObj[2]);
            String[] urlArr = url.split(":");
            String ip = urlArr[0] + ":" + urlArr[1] + ":" + urlArr[2];
            
            Object[] pathObj = systemConfigRows.get(0);
            String path = ObjectUtil.obj2String(pathObj[2]);
            
            String attachmentUrl = ip + path + tableName + ".csv";
            
            logger.info("===>Request ip: " + ip);
            logger.info("===>Request path: " + path);
            logger.info("===>Request tableName: " + tableName + ".csv");
            logger.info("===>Request entiry url: " + attachmentUrl);
            
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename=" + tableName + ".csv");
            response.setContentType("application/x-download");
            
            RestTemplate restTemplate = new ResttemplateUtile().getRestTemplate("http", "60000");
            ResponseEntity<byte[]> responseData = restTemplate.getForEntity(attachmentUrl, byte[].class);
            OutputStream os;
            
            try {
                os = response.getOutputStream();
                byte[] uft8bom = {( byte) 0xef, (byte) 0xbb, (byte) 0xbf};
                os.write(uft8bom);
                os.write(responseData.getBody());
                os.close();
            } catch (IOException e) {
                logger.info("===>" + tableName + "報表資料匯出失敗");
                e.printStackTrace();
            }
            
            logger.info("===>" + tableName + "匯出成功");
        }
        
    }
    
    @RequestMapping("/queryGenerateStatus")
    @ResponseBody
    public Map<String, Object> queryGenerateStatus(String schemaName, String tableName) {
        Map<String, Object> result = new HashMap<>();
        QueryPage<Object[]> queryPage = systemDataServiceImpl.queryGenerateStatus(schemaName, tableName);
        List<Object[]> systemConfigRows = queryPage.getContent();
        if (CollectionUtils.isEmpty(systemConfigRows) || systemConfigRows.size() != 1) {
            result.put("returnFlag", false);
            result.put("returnCode", "0");
            result.put("returnMsg", "查詢失敗");
        } else {
            result.put("returnFlag", true);
            result.put("returnCode", systemConfigRows.get(0)[0]);
            result.put("returnMsg", systemConfigRows.get(0)[1]);
        }
        
        return result;
    }
    
    @RequestMapping("/updateSystemData")
    @ResponseBody
    public Map<String, Object> updateSystemData(@RequestBody Map<String, Object> systemDataMap) {
        Map<String, Object> result = new HashMap<>();
        try {
            result = systemDataServiceImpl.addSystemData(systemDataMap);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("returnFlag", false);
            result.put("returnCode", "9999");
            result.put("returnMsg", "操作數據庫失敗");
        }
        
        return result;
    }
    
    @RequestMapping("/deleteSystemData")
    @ResponseBody
    public Map<String, Object> deleteSystemData(String systemDataId) {
        Map<String, Object> result = new HashMap<>();
        try {
            result = systemDataServiceImpl.deleteSystemDataById(systemDataId);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("returnFlag", false);
            result.put("returnCode", "9999");
            result.put("returnMsg", "操作數據庫失敗");
        }
        
        return result;
    }
    
    @RequestMapping("/indexForIT")
    public String indexForIT(Model model, String backFlag) {
        model.addAttribute("backFlag", backFlag);
        return "it/indexForIT";
    }
    
    @RequestMapping("/viewSystemDataListForIT")
    public String viewSystemDataListForIT(Model model, String backFlag) {
        model.addAttribute("backFlag", backFlag);
        return "it/viewSystemDataListForIT";
    }
}
