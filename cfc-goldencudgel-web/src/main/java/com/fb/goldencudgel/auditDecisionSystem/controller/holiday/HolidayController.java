package com.fb.goldencudgel.auditDecisionSystem.controller.holiday;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.comwave.core.data.domain.QueryPage;
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

import com.fb.goldencudgel.auditDecisionSystem.domain.holiday.Holiday;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.HolidayServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.utils.ExcelUtil;

@Controller
@RequestMapping("/holiday")
public class HolidayController {

  private static final Logger logger = LoggerFactory.getLogger(HolidayController.class);
  private static final Integer PAGE_SIZE = 20;
  @Autowired
  private HolidayServiceImpl holidayService;

  @RequestMapping("/HolidayList")
  public String HolidayList(Model model,@RequestParam(required = false, defaultValue = "1") Integer curPage,String holiday,String years){

    QueryPage<Object[]> page =holidayService.findAll(new QueryPage<Object[]>(curPage,PAGE_SIZE),holiday,years);

    model.addAttribute("page",page);

    return "holiday/HolidayList";
  }
  @RequestMapping("/viewHoliday")
  public String viewHoliday(){

    return "holiday/viewHoliday";
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
    /*        if (dataList == null || dataList.size() < 3) {
              result.put("message", "請填寫日期信息");
              result.put("success", false);
              return result;
            }*/
            String dateOfYear = dataList.get(1).get(0) == null ? "" : dataList.get(1).get(0).toString();
         /*   if (StringUtils.isBlank(dateOfYear)) {
              result.put("message", "請填寫年份信息");
              result.put("success", false);
              return result;
            }*/
            List<Holiday> holidays = holidayService.getHolidayByYear(dateOfYear.trim());
            /*if (holidays != null && holidays.size() > 0) {
              result.put("message", "該年份信息已存在");
              result.put("success", false);
              return result;
            }*/
            holidays = new ArrayList<Holiday>();
            for (int j = 2; j < dataList.size(); j++) {
              List<Object> obj = dataList.get(j);
              Holiday hd = createObj(dateOfYear, obj);
              holidays.add(hd);
            }
            holidayService.deleteAll();
            holidayService.saveList(holidays);
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

  private Holiday createObj(String dateOfYear, List<Object> object) {
    Holiday obj = new Holiday();
    Date date = new Date();
    obj.setDateOfYear(dateOfYear);
    if (object != null && object.size() > 0) {
      obj.setHoliday(object.get(0).toString().trim());
    }
    obj.setImportTime(date);
    return obj;
  }

//下载模板
  @RequestMapping("/excleDownload")
  public String excleDownload(HttpServletRequest request,HttpServletResponse response) {
     
      response.setCharacterEncoding("utf-8");
      String name = "假日管理導入模板";
      response.setContentType("text/html");
      String codedFileName = null;//编码处理后文件名
      try {
          
          //String path="D:";//把假日管理導入模板.xlsx模板复制到D盘下
    	  String path="/app/cache/excel/";//docker容器
          String fileName = "holiday.xlsx";
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
