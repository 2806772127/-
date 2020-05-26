package com.fb.goldencudgel.auditDecisionSystem.controller.indexTargetNum;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.comwave.core.data.domain.QueryPage;
import com.fb.goldencudgel.auditDecisionSystem.domain.report.FbTargetConfig;
import com.fb.goldencudgel.auditDecisionSystem.repository.FbTargetConfigRepository;
import com.fb.goldencudgel.auditDecisionSystem.service.IMissionStroke;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.FbTargetConfigServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.VideoConferenceServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.utils.ExcelUtil;

@Controller
@RequestMapping("/indexTarget")
public class IndexTargetNumController {

    private static final Logger logger = LoggerFactory.getLogger(IndexTargetNumController.class);
    private static final Integer PAGE_SIZE = 20;

    @Autowired
    private FbTargetConfigRepository fbTargetConfigRepository;

    @Autowired
    private FbTargetConfigServiceImpl fbTargetConfigService;

    @Autowired
    private IMissionStroke missionStroke;

    @Autowired
    private VideoConferenceServiceImpl videoConferenceServiceImpl;

    @RequestMapping("/viewIndexTarget")
    public String viewIndexTarget(Model model) {
        Map<String, String> areaList = missionStroke.getAreaList("");
        Map<String, String> groupList = videoConferenceServiceImpl.getGroupList("");
        Map<String, String> userList = videoConferenceServiceImpl.getUserList("", "");
        model.addAttribute("areaList", areaList);
        model.addAttribute("groupList", groupList);
        model.addAttribute("userList", userList);
        return "indexTarget/viewIndexTarget";
    }

    @RequestMapping("/saveUpload")
    @ResponseBody
    public Map<String, Object> saveUpload(MultipartHttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<MultipartFile> files = request.getFiles("file");
        if (files != null && files.size() > 0) {
            MultipartFile file = null;
            for (int i = 0; i < files.size(); i++) {
                file = files.get(i);
                if (!file.isEmpty()) {
                    try {
                        List<List<Object>> dataList = new ArrayList<List<Object>>();
                        dataList = ExcelUtil.readFromFile(file);
                        if (dataList == null || dataList.size() < 3) {
                            result.put("message", "請填寫完整信息");
                            result.put("success", false);
                            return result;
                        }
                        String dateOfYear = dataList.get(0).get(1) == null ? "" : dataList.get(0).get(1).toString();
                        dateOfYear = dateOfYear.split("\\.")[0];
                        if (StringUtils.isBlank(dateOfYear)) {
                            result.put("message", "請填寫年份信息");
                            result.put("success", false);
                            return result;
                        }
                        // String loantype = dataList.get(0).get(3) == null ? "" : dataList.get(0).get(3).toString();
                        // loantype = loantype.split("\\.")[0];
                        // if(StringUtils.isBlank(loantype)){
                        // result.put("message","請填寫貸款類型");
                        // result.put("success",false);
                        // return result;
                        // }

                        List<FbTargetConfig> fbTargetConfigs = new ArrayList<FbTargetConfig>();

                        for (int j = 2; j < dataList.size(); j++) {
                            List<Object> obj = dataList.get(j);
                            FbTargetConfig fb = new FbTargetConfig();
                            for (int m = 0; m < 36; m++) {
                                if (obj.size() >= m + 3 && obj.get(m + 2) != null) {
                                    String mon = "";
                                    int yuCon = 0;
                                    if (m < 27) {
                                        yuCon = m / 3 + 1;
                                        mon = "0" + yuCon;
                                    } else {
                                        yuCon = m / 3 + 1;
                                        mon = "" + yuCon;
                                    }
                                    // List<FbTargetConfig> fbs = fbTargetConfigRepository.findbyUserYearMon(obj.get(0).toString(), dateOfYear + mon);
                                    fbTargetConfigRepository.delAccount(dateOfYear + "%");
                                    String[] nums = obj.get(m + 2).toString().split(":");
                                    if (nums.length < 2)
                                        break;

                                    fb.setTargetUserCode(obj.get(0).toString());
                                    fb.setTargetUser(obj.get(1).toString());
                                    fb.setTargetCycle(dateOfYear + mon);
                                    fb.setOperationTime(new Date());
                                    fb.setCreateTime(new Date());

                                    String acount = nums[0] != null ? nums[0] : "0";
                                    String amt = nums[1] != null ? nums[1] : "0";
                                    if (m % 3 == 0) {
                                        fb.setComLoanAcount(new BigDecimal(acount));
                                        fb.setComApproveAmt(new BigDecimal(amt));
                                    } else if (m % 3 == 1) {
                                        fb.setHouseLoanAcount(new BigDecimal(acount));
                                        fb.setHouseApproveAmt(new BigDecimal(amt));
                                    } else if (m % 3 == 2) {
                                        fb.setCreditLoanAcount(new BigDecimal(acount));
                                        fb.setCreditApproveAmt(new BigDecimal(amt));
                                        fbTargetConfigs.add(fb);
                                        fb = new FbTargetConfig();
                                    }
                                }
                            }
                        }
                        fbTargetConfigRepository.saveAll(fbTargetConfigs);
                        result.put("success", true);
                        result.put("message", "導入成功");
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                        result.put("message", "導入失敗");
                        result.put("success", false);
                    }
                }
            }
        } else {
            result.put("message", "文件導入失敗");
            result.put("success", false);
        }
        return result;
    }

    @RequestMapping("/saveUploads")
    @ResponseBody
    public Map<String, Object> saveUploads(MultipartHttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<MultipartFile> files = request.getFiles("file");
        if (files != null && files.size() > 0) {
            MultipartFile file = null;
            for (int i = 0; i < files.size(); i++) {
                file = files.get(i);
                if (!file.isEmpty()) {
                    try {
                        List<List<Object>> dataList = new ArrayList<List<Object>>();
                        List<Object> dataLists = new ArrayList<>();
                        List<List<Object>> lists = new ArrayList<>();
                        dataList = ExcelUtil.readFromFile(file);
                        if (dataList == null || dataList.size() < 3) {
                            result.put("message", "請填寫完整信息");
                            result.put("success", false);
                            return result;
                        }

                        for (int j = 2; j < dataList.size(); j++) {
                            if (dataList.get(j).get(0).equals("") && dataList.get(j).get(1).equals("") && dataList.get(j).get(2).equals("") && dataList.get(j).get(3).equals("")
                                    && dataList.get(j).get(4).equals("") && dataList.get(j).get(5).equals("")) {
                                break;
                            }

                            String TARGET_USER = dataList.get(j).get(0) == null ? "" : dataList.get(j).get(0).toString();
                            // TARGET_USER = TARGET_USER.split("\\.")[0].toString();
                            dataLists.add(TARGET_USER);
                            if (StringUtils.isBlank(TARGET_USER)) {
                                result.put("message", "請填業務員");
                                result.put("success", false);
                                return result;
                            }

                            String TARGET_USER_CODE = dataList.get(j).get(1) == null ? "" : dataList.get(j).get(1).toString();
                            TARGET_USER_CODE = TARGET_USER_CODE.split("\\.")[0].toString();
                            dataLists.add(TARGET_USER_CODE);
                            if (StringUtils.isBlank(TARGET_USER_CODE)) {
                                result.put("message", "請填業務員編號");
                                result.put("success", false);
                                return result;
                            }

                            String TARGET_CYCLE = dataList.get(j).get(2) == null ? "" : dataList.get(j).get(2).toString();
                            TARGET_CYCLE = TARGET_CYCLE.split("\\.")[0].toString();
                            dataLists.add(TARGET_CYCLE);
                            if (StringUtils.isBlank(TARGET_CYCLE)) {
                                result.put("message", "請填寫月份信息");
                                result.put("success", false);
                                return result;
                            }

                            String CREDIT_APPROVE_AMT = dataList.get(j).get(3) == null ? "" : dataList.get(j).get(3).toString();
                            // CREDIT_APPROVE_AMT = CREDIT_APPROVE_AMT.split("\\.")[0].toString();
                            dataLists.add(CREDIT_APPROVE_AMT);
                            if (StringUtils.isBlank(CREDIT_APPROVE_AMT)) {
                                result.put("message", "請填寫信貸拨款目標值");
                                result.put("success", false);
                                return result;
                            }
                            String CREDIT_LOAN_ACOUNT = dataList.get(j).get(4) == null ? "" : dataList.get(j).get(4).toString();
                            // CREDIT_LOAN_ACOUNT = CREDIT_LOAN_ACOUNT.split("\\.")[0].toString();
                            dataLists.add(CREDIT_LOAN_ACOUNT);
                            if (StringUtils.isBlank(CREDIT_LOAN_ACOUNT)) {
                                result.put("message", "請填寫信貸進件目標值");
                                result.put("success", false);
                                return result;
                            }

                            String HOUSE_LOAN_ACOUNT = dataList.get(j).get(5) == null ? "" : dataList.get(j).get(5).toString();
                            // HOUSE_LOAN_ACOUNT = HOUSE_LOAN_ACOUNT.split("\\.")[0].toString();
                            dataLists.add(HOUSE_LOAN_ACOUNT);
                            if (StringUtils.isBlank(HOUSE_LOAN_ACOUNT)) {
                                result.put("message", "請填寫房貸進件目標值");
                                result.put("success", false);
                                return result;
                            }
                            String HOUSE_APPROVE_AMT = dataList.get(j).get(6) == null ? "" : dataList.get(j).get(6).toString();
                            // HOUSE_APPROVE_AMT = HOUSE_APPROVE_AMT.split("\\.")[0].toString();
                            dataLists.add(HOUSE_APPROVE_AMT);
                            if (StringUtils.isBlank(HOUSE_APPROVE_AMT)) {
                                result.put("message", "請填寫房貸拨款目標值");
                                result.put("success", false);
                                return result;
                            }

                            String COM_APPROVE_AMT = dataList.get(j).get(7) == null ? "" : dataList.get(j).get(7).toString();
                            // COM_APPROVE_AMT = COM_APPROVE_AMT.split("\\.")[0].toString();
                            dataLists.add(COM_APPROVE_AMT);
                            if (StringUtils.isBlank(COM_APPROVE_AMT)) {
                                result.put("message", "請填寫企貸拨款目標值");
                                result.put("success", false);
                                return result;
                            }
                            String COM_LOAN_ACOUNT = dataList.get(j).get(8) == null ? "" : dataList.get(j).get(8).toString();
                            // COM_LOAN_ACOUNT = COM_LOAN_ACOUNT.split("\\.")[0].toString();
                            dataLists.add(COM_LOAN_ACOUNT);
                            if (StringUtils.isBlank(COM_LOAN_ACOUNT)) {
                                result.put("message", "請填寫企貸進件目標值");
                                result.put("success", false);
                                return result;
                            }

                            List<Object> minList = new ArrayList<>();
                            for (int a = 0; a < dataLists.size(); a++) {
                                minList.add(dataLists.get(a));
                            }
                            lists.add(minList);
                            dataLists.clear();

                        }

                        List<FbTargetConfig> fbTargetConfigs = new ArrayList<FbTargetConfig>();
                        // User user = UserUtil.getCurrUser();
                        FbTargetConfig fb = new FbTargetConfig();
                        // SimpleDateFormat formatDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",Locale.US);
                        // SimpleDateFormat formatNowDate = new SimpleDateFormat("yyyy/MM/dd");
                        for (int h = 0; h < lists.size(); h++) {
                            String TARGET_USER = (String) lists.get(h).get(0);
                            String TARGET_USER_CODE = (String) lists.get(h).get(1);
                            String TARGET_CYCLE = (String) lists.get(h).get(2);
                            String CREDIT_APPROVE_AMT = (String) lists.get(h).get(3);
                            String CREDIT_LOAN_ACOUNT = (String) lists.get(h).get(4);
                            String HOUSE_LOAN_ACOUNT = (String) lists.get(h).get(5);
                            String HOUSE_APPROVE_AMT = (String) lists.get(h).get(6);
                            String COM_APPROVE_AMT = (String) lists.get(h).get(7);
                            String COM_LOAN_ACOUNT = (String) lists.get(h).get(8);
                            fb.setTargetUser(TARGET_USER);
                            fb.setTargetUserCode(TARGET_USER_CODE);
                            fb.setTargetCycle(TARGET_CYCLE);
                            fb.setCreateTime(new Date());
                            fb.setOperationTime(new Date());
                            fb.setCreditApproveAmt(new BigDecimal(CREDIT_APPROVE_AMT));
                            fb.setCreditLoanAcount(new BigDecimal(CREDIT_LOAN_ACOUNT));
                            fb.setHouseApproveAmt(new BigDecimal(HOUSE_APPROVE_AMT));
                            fb.setHouseLoanAcount(new BigDecimal(HOUSE_LOAN_ACOUNT));
                            fb.setComLoanAcount(new BigDecimal(COM_LOAN_ACOUNT));
                            fb.setComApproveAmt(new BigDecimal(COM_APPROVE_AMT));
                            fbTargetConfigs.add(fb);
                        }

                        fbTargetConfigRepository.saveAll(fbTargetConfigs);
                        result.put("success", true);
                        result.put("message", "導入成功");
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                        result.put("message", "導入失敗");
                        result.put("success", false);
                    }
                }
            }
        } else {
            result.put("message", "文件導入失敗");
            result.put("success", false);
        }
        return result;
    }

    @RequestMapping("/queryIndexTarget")
    public String queryIndexTarget(Model model, String dictId, String areaId, String groupId, String userId, String mon, String targetId,
            @RequestParam(required = false, defaultValue = "1") Integer curPage) {

        QueryPage<Object[]> page = fbTargetConfigService.findByIds(dictId, areaId, groupId, userId, mon, targetId, new QueryPage<Object[]>(curPage, PAGE_SIZE));
        model.addAttribute("page", page);
        model.addAttribute("datas", page.getContent());
        return "indexTarget/indexTargetList";
    }
    
    /**
     * @description: 根据区代码获取组、业务员
     * @author: mazongjian
     * @param model
     * @param areaCode
     * @return  
     * @date 2019年4月21日
     */
    @RequestMapping("/getGroupList")
    @ResponseBody
    public List<Map<String,String>> getGroupList(Model model, String areaId) {
        List<Map<String, String>> resultList = new ArrayList<>();
        if (areaId.equals("allarea")) {
            areaId = "";
        }
        Map<String,String> groupList = missionStroke.getGroupList(areaId);
        Map<String, String> userList = missionStroke.getUserList(areaId, "");
        resultList.add(groupList);
        resultList.add(userList);
        return resultList;
    }
    
    /**
     * @description: 根据区代码、组代码获取业务员
     * @author: mazongjian
     * @param model
     * @param areaCode
     * @param groupCode
     * @return  
     * @date 2019年4月21日
     */
    @RequestMapping("/getUserList")
    @ResponseBody
    public Map<String,String> getUserList(Model model, String areaId, String groupId){
        if (areaId.equals("allarea")) {
            areaId = "";
        }
        if (groupId.equals("allgroup")) {
            groupId = "";
        }
        Map<String,String> groupList = missionStroke.getUserList(areaId, groupId);
        return groupList;
    }
    
  //下载模板
    @RequestMapping("/excleDownload")
    public String excleDownload(HttpServletRequest request,HttpServletResponse response) {
       
        response.setCharacterEncoding("utf-8");
        String name = "指標目標數上傳導入模板";
        response.setContentType("text/html");
        String codedFileName = null;//编码处理后文件名
        try {
            
            //String path="D:";//把指標目標數上傳導入模板.xlsx模板复制到D盘下
        	String path="/app/cache/excel/";//docker容器
            String fileName = "indexTarget.xlsx";
            InputStream inputStream = new FileInputStream(new File(path+ File.separator + fileName));
            response.reset();
            
            String agent = request.getHeader("USER-AGENT");  
	          if (null != agent && -1 != agent.indexOf("MSIE") || null != agent && -1 != agent.indexOf("Trident")) {// ie  
	               codedFileName = java.net.URLEncoder.encode(name, "UTF8");  
	               
	          } else if (null != agent && -1 != agent.indexOf("Mozilla")) {// 火狐,chrome等  
	        	  
	        	  codedFileName = new String(name.getBytes("UTF-8"), "iso-8859-1");  
	          }  
	          response.setHeader("Content-disposition", "attachment; filename=" + codedFileName + ".xlsx");
	          response.setContentType("application/x-download");
	          
            //response.setHeader("Content-disposition", "attachment; filename=" + new String(name.getBytes(),"iso-8859-1") + ".xlsx");
            //response.setContentType("application/x-download");

            OutputStream os = response.getOutputStream();
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }
            os.close();

            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
