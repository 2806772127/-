package com.fb.goldencudgel.auditDecisionSystem.controller.achiRate365;

import com.comwave.core.data.domain.QueryPage;
import com.fb.goldencudgel.auditDecisionSystem.domain.fbYearAppropriationRate.FbYearAppropriationRate;
import com.fb.goldencudgel.auditDecisionSystem.model.User;
import com.fb.goldencudgel.auditDecisionSystem.repository.FbYearAppropriationRateRepository;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.FbTargetConfigServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.utils.ExcelUtil;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/achiRate")
public class AchiRateController {

  private static final Logger logger = LoggerFactory.getLogger(AchiRateController.class);
  private static final Integer PAGE_SIZE = 20;
  
  @Autowired
  private FbYearAppropriationRateRepository fbYearAppropriationRateRepository;
  
  @Autowired
  private FbTargetConfigServiceImpl fbTargetConfigService;
  

  @RequestMapping("/viewAchiRate")
  public String viewHoliday() {
    return "achiRate/viewAchiRate";
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
            List<Object> dataLists = new ArrayList<>();
            List<List<Object>> lists = new ArrayList<>();
            dataList = ExcelUtil.readFromFile(file);
            if (dataList == null || dataList.size() < 3) {
              result.put("message", "請填寫信息");
              result.put("success", false);
              return result;
            }
            
            for(int j=2;j<dataList.size();j++) {
            	
            	 //判断是否已读取完excle数据
	        if(dataList.get(j).get(0).equals("")&&dataList.get(j).get(1).equals("")&&dataList.get(j).get(2).equals("")&&
	           dataList.get(j).get(3).equals("")) {
	            break;
	        }	
           
     /*       String USER_CUSTOMER_ID = dataList.get(j).get(0) == null ? "" : dataList.get(j).get(0).toString();
            USER_CUSTOMER_ID = USER_CUSTOMER_ID.split("\\.")[0].toString();
            dataLists.add(USER_CUSTOMER_ID);
            if (StringUtils.isBlank(USER_CUSTOMER_ID)) {
              result.put("message", "請填員工身份證號");
              result.put("success", false);
              return result;
            }
            
            String USER_CODE = dataList.get(j).get(1) == null ? "" : dataList.get(j).get(1).toString();
            USER_CODE = USER_CODE.split("\\.")[0].toString();
            dataLists.add(USER_CODE);
            if (StringUtils.isBlank(USER_CODE)) {
              result.put("message", "請填寫員工編號");
              result.put("success", false);
              return result;
            }*/
            String COM_RATE_GOAL = dataList.get(j).get(0) == null ? "" : dataList.get(j).get(0).toString();
            dataLists.add(COM_RATE_GOAL);
            if (StringUtils.isBlank(COM_RATE_GOAL)) {
              result.put("message", "請填寫企貸達成率");
              result.put("success", false);
              return result;
            }
            String CREDIT_RATE_GOAL = dataList.get(j).get(1) == null ? "" : dataList.get(j).get(1).toString();
            dataLists.add(CREDIT_RATE_GOAL);
            if (StringUtils.isBlank(CREDIT_RATE_GOAL)) {
              result.put("message", "請填寫信貸達成率");
              result.put("success", false);
              return result;
            }
            String HOUSE_RATE_GOAL = dataList.get(j).get(2) == null ? "" : dataList.get(j).get(2).toString();
            dataLists.add(HOUSE_RATE_GOAL);
            if (StringUtils.isBlank(HOUSE_RATE_GOAL)) {
              result.put("message", "請填寫房貸達成率");
              result.put("success", false);
              return result;
            }
            
            String RATE_DATE = dataList.get(j).get(3) == null ? "" : dataList.get(j).get(3).toString();
            RATE_DATE = RATE_DATE.split("\\.")[0].toString();
            dataLists.add(RATE_DATE);
            if (StringUtils.isBlank(RATE_DATE)) {
              result.put("message", "請填寫日期");
              result.put("success", false);
              return result;
            }
            
            List<Object> minList = new ArrayList<>();
            for(int a=0;a<dataLists.size();a++) {
            	minList.add(dataLists.get(a));
            }
            lists.add(minList);
            dataLists.clear();
            
            }
            fbYearAppropriationRateRepository.delBeforeRecord();
            for(int h=0;h<lists.size();h++) {
	       /*     String USER_CUSTOMER_ID = (String)lists.get(h).get(0);
	            String USER_CODE = (String)lists.get(h).get(1);*/
	            String COM_RATE_GOAL = (String)lists.get(h).get(0);
	            String CREDIT_RATE_GOAL = (String)lists.get(h).get(1);
	            String HOUSE_RATE_GOAL = (String)lists.get(h).get(2);
	            String RATE_DATE = (String)lists.get(h).get(3);
            
            List<FbYearAppropriationRate> fbs = new ArrayList<FbYearAppropriationRate>();
            User user = UserUtil.getCurrUser();
            SimpleDateFormat formatDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",Locale.US);
            SimpleDateFormat formatNowDate = new SimpleDateFormat("yyyy/MM/dd");
            FbYearAppropriationRate f = new FbYearAppropriationRate();
            f.setUserCustomerId("test");//c測試用
            f.setUserCode(user.getUserCode());
            f.setCreateTime(new Date());
            f.setComRateGoal(COM_RATE_GOAL);
            f.setCreditRateGoal(CREDIT_RATE_GOAL);
            f.setHouseRateGoal(HOUSE_RATE_GOAL);
            f.setRateDate(formatNowDate.format(formatDate.parse(RATE_DATE)));
            //f.setRateDate(RATE_DATE);
            fbs.add(f);
            fbYearAppropriationRateRepository.saveAll(fbs);
            result.put("success", true);
            result.put("message", "導入成功");
          }
          }catch (Exception e) {
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
  
  
  //list页面数据方法
  @RequestMapping("/queryAchiRateList")
  public  String queryAchiRateList (Model model,String employeeIdNum, String employeeNum,
                                   String startDate,String endDate,@RequestParam(required = false, defaultValue = "1") Integer curPage){

      QueryPage<Object[]> page =  fbTargetConfigService.findByConditions(employeeIdNum,employeeNum,startDate,endDate,new QueryPage<Object[]>(curPage,PAGE_SIZE));
      model.addAttribute("page",page);
      model.addAttribute("datas",page.getContent());
      return "achiRate/viewAchiRateList";
  }
  
  //下载模板
  @RequestMapping("/excleDownload")
  public String excleDownload(HttpServletRequest request,HttpServletResponse response) {
     
      response.setCharacterEncoding("utf-8");
      String name = "365達成率導入模板";
      response.setContentType("text/html");
      String codedFileName = null;//编码处理后文件名
      try {
          //String path="D:";//把365.xlsx模板复制到D盘下
    	  String path="/app/cache/excel/";//docker容器
          String fileName = "365AchiRate.xlsx";
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
