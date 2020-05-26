package com.fb.goldencudgel.auditDecisionSystem.controller.systemLogController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.comwave.core.data.domain.QueryPage;
import com.fb.goldencudgel.auditDecisionSystem.repository.SystemLogRepository;
import com.fb.goldencudgel.auditDecisionSystem.schema.getLogFileList.GetLogFileListRq;
import com.fb.goldencudgel.auditDecisionSystem.schema.getLogFileList.GetLogFileListRs;
import com.fb.goldencudgel.auditDecisionSystem.schema.getLogFileList.LogFile;
import com.fb.goldencudgel.auditDecisionSystem.service.IInterfaceService;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.SystemLogServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.utils.LogFileUtil;
import com.fb.goldencudgel.auditDecisionSystem.utils.ResttemplateUtile;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/systemLogs")
public class SystemLogsController {

    private final Logger logger = LoggerFactory.getLogger(SystemLogsController.class);
    private static final Integer PAGE_SIZE = 20;

    @Autowired
    private IInterfaceService interfaceService;
    
    @Autowired
    private SystemLogRepository systemLogRepository;
    

    @RequestMapping("/SystemLogs")
    public String viewSystemLog(Model model){
        return  "systemLog/systemLogs";
    }

    @RequestMapping("/querySystemLogs")
    public String queryItemData (Model model,String operationType, String createStartDate, String createEndDate, String updateStartDate,String updateEndDate,
    		                     String queryFlag,@RequestParam(required = false, defaultValue = "1") Integer curPage){
            try {
            	    if(operationType.equals("WEB")){
            	    int pageSize = 20;
            	    ArrayList list = new ArrayList<>();
            	    String webPath = systemLogRepository.findWebPath();
            	    Map<String, Object> map = LogFileUtil.queryLogFilesByPage(curPage, pageSize, operationType, webPath);
            	    List<String[]> recordList;
            	    if (StringUtils.isNoneBlank(queryFlag)){
            	    	recordList = (List<String[]>) map.get("logFileDetailList");	
            	    }else{
            	        recordList = (List<String[]>) map.get("recordList");
            	    }
            	    for(int i=0;i<recordList.size();i++){
            	    	String[] objArray = recordList .get(i);
            	    	list.add(objArray);
            	    }
            	    List newList2 = new ArrayList<>();
            	    List newList3 = new ArrayList<>();
            	    boolean flag=false;
            	    //类型刷选后，有数据,遍历
            	    if(!list.isEmpty()){
                    if (StringUtils.isNoneBlank(createStartDate)||StringUtils.isNoneBlank(createEndDate)){
                    	 flag=true;
                    	 for(int i=0;i<list.size();i++){
                 	    	String[] str = (String[]) list.get(i);
                 	    	String createStart = str[2].substring(0, 10).replace("-", "/");
                 	    	if(compareCreateTime(createStart,createStartDate,createEndDate)){
                 	    		newList2.add(list.get(i));
                 	    	}
                    	  }
                    	 //创建时间条件刷选没有数据，直接返回
                    	 if(newList2.isEmpty()){
                         	newList3=null;
                         }else{
                        	 //更新时间条件刷选
                        	 if (StringUtils.isNoneBlank(updateStartDate)||StringUtils.isNoneBlank(updateEndDate)){
                            	    for(int i=0;i<newList2.size();i++){
                         	    	String[] str = (String[]) newList2.get(i);
                         	    	String updateStart = str[3].substring(0, 10).replace("-", "/");
                         	    	if(compareCreateTime(updateStart,updateStartDate,updateEndDate)){
                         	    		newList3.add(newList2.get(i));
                         	    	}
                         	    }
                    	    }else{
                    	    	newList3=newList2;
                    	    }
                         }
                 	    }else{
                 	    	if (StringUtils.isNoneBlank(updateStartDate)||StringUtils.isNoneBlank(updateEndDate)){
                           	    for(int i=0;i<list.size();i++){
                        	    	String[] str = (String[]) list.get(i);
                        	    	String updateStart = str[3].substring(0, 10).replace("-", "/");
                        	    	if(compareCreateTime(updateStart,updateStartDate,updateEndDate)){
                        	    		newList3.add(list.get(i));
                        	    	}
                        	    }
                   	    }else{
                   	    	newList3=list;
                   	    }
                 	    }
            	    }else{
            	    	newList3=null;
            	    }
            	    //点击查询，需要把数据重新封装到分页
                    if (StringUtils.isNoneBlank(queryFlag)){
                    	if(newList3!=null){
                        int total = newList3.size();
                        int pageTotal = total / pageSize + (total % pageSize > 0 ? 1 : 0);
                        int startIndex = (curPage - 1) * pageSize;
                        int endIndex = curPage * pageSize;
                        endIndex = endIndex > total ? total : endIndex;
                        List<String[]> queryRecordList = newList3.subList(startIndex, endIndex);
                        ArrayList lists = new ArrayList<>();
                        for(int i=0;i<queryRecordList.size();i++){
                	    	lists.add(queryRecordList.get(i));
                	    }
                        map.put("recordList", queryRecordList);
                        map.put("pageTotal", pageTotal);
                        map.put("total", total);
                        map.put("curPage", curPage);
                        map.put("pageSize", pageSize);
                        map.put("logFileDetailList", newList3);
                    	
                    	QueryPage<Object[]> page = new QueryPage<>();
                	    page.setContent(lists);
                	    page.setPageSize(pageSize);
                	    //Integer totals = (Integer) map.get("total");
                	    //Integer num = (Integer) map.get("curPage");
                	    page.setTotal(total);
                	    page.setNumber(curPage);
                        model.addAttribute("page", page);
                        model.addAttribute("toalPage", pageTotal);
                        //model.addAttribute("toalPage", map.get("pageTotal"));
                    	}else{
                         	QueryPage<Object[]> page = new QueryPage<>();
                     	    page.setContent(new ArrayList<>());
                     	    page.setPageSize(pageSize);
                     	    page.setTotal(0);
                     	    page.setNumber(1);
                            model.addAttribute("page", page);
                             
                            model.addAttribute("toalPage", 0);
                    	}
                    }else{
                    	//初始化，可以直接调用公共方法
                    	QueryPage<Object[]> page = new QueryPage<>();
                	    page.setContent(list);
                	    page.setPageSize(pageSize);
                	    Integer total = (Integer) map.get("total");
                	    Integer num = (Integer) map.get("curPage");
                	    page.setTotal(total);
                	    page.setNumber(num);
                        model.addAttribute("page", page);
                        
                        model.addAttribute("toalPage", map.get("pageTotal"));
                    }
            	    }
            	    
            	    if(operationType.equals("BWCE")){
            	    	queryBwceDataInterface(model,curPage,20,"",operationType,createStartDate,createEndDate,updateStartDate,updateEndDate,queryFlag);
            	    }
            	    if(operationType.equals("FRONT")||operationType.equals("APPWEB")){
            	    	queryFRONT(model,curPage,20,operationType,createStartDate,createEndDate,updateStartDate,updateEndDate,queryFlag);
            	    }
            	    
            	    
            } catch (Exception e) {
                e.printStackTrace();
            }
       return "systemLog/systemLogsList";
    }
    
    //前置接口日志
    public void queryFRONT(Model model,Integer curPage,Integer pageSize,String operationType, String createStartDate, 
            String createEndDate, String updateStartDate,String updateEndDate,String queryFlag) {
    	RestTemplate restTemplate = new ResttemplateUtile().getRestTemplate("http", "60000");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        String frontPath = systemLogRepository.findFrontPath();
        String myurl = frontPath+"/systemLog/";
        JSONObject obj = new JSONObject();
        obj.put("curPage",curPage);
        obj.put("pageSize", pageSize);
        obj.put("type", operationType);
        HttpEntity request = new HttpEntity(JSON.toJSONString(obj), headers);
        logger.info("---->Front request url:" + myurl);
        logger.info("---->Front request message:" + JSON.toJSONString(obj));
        ResponseEntity<Map> responseData = restTemplate.exchange(myurl, HttpMethod.POST,request,Map.class);
        Map backData = responseData.getBody();
        if(!backData.isEmpty()){
        List<String> recordList = (List) backData.get("recordList");
         ArrayList list = new ArrayList<>();
        for(int i=0;i<recordList.size();i++){
   		 	String[] objArray = recordList.get(i).split(",");
   			list.add(objArray);
	    }
        List newList2 = new ArrayList<>();
	    List newList3 = new ArrayList<>();
	    boolean flag=false;
	    //类型刷选后，有数据,遍历
	    if(!list.isEmpty()){
        if (StringUtils.isNoneBlank(createStartDate)||StringUtils.isNoneBlank(createEndDate)){
        	 flag=true;
        	 for(int i=0;i<list.size();i++){
     	    	String[] str = (String[]) list.get(i);
     	    	String createStart = str[2].substring(0, 10).replace("-", "/");
     	    	if(compareCreateTime(createStart,createStartDate,createEndDate)){
     	    		newList2.add(list.get(i));
     	    	}
        	  }
        	 //创建时间条件刷选没有数据，直接返回
        	 if(newList2.isEmpty()){
             	newList3=null;
             }else{
            	 //更新时间条件刷选
            	 if (StringUtils.isNoneBlank(updateStartDate)||StringUtils.isNoneBlank(updateEndDate)){
                	    for(int i=0;i<newList2.size();i++){
             	    	String[] str = (String[]) newList2.get(i);
             	    	String updateStart = str[3].substring(0, 10).replace("-", "/");
             	    	if(compareCreateTime(updateStart,updateStartDate,updateEndDate)){
             	    		newList3.add(newList2.get(i));
             	    	}
             	    }
        	    }else{
        	    	newList3=newList2;
        	    }
             }
     	    }else{
     	    	if (StringUtils.isNoneBlank(updateStartDate)||StringUtils.isNoneBlank(updateEndDate)){
               	    for(int i=0;i<list.size();i++){
            	    	String[] str = (String[]) list.get(i);
            	    	String updateStart = str[3].substring(0, 10).replace("-", "/");
            	    	if(compareCreateTime(updateStart,updateStartDate,updateEndDate)){
            	    		newList3.add(list.get(i));
            	    	}
            	    }
       	    }else{
       	    	newList3=list;
       	    }
     	    }
	    }else{
	    	newList3=null;
	    }
    
	    //点击查询，需要把数据重新封装到分页
        	if(newList3!=null){
            int total = newList3.size();
            int pageTotal = total / pageSize + (total % pageSize > 0 ? 1 : 0);
            int startIndex = (curPage - 1) * pageSize;
            int endIndex = curPage * pageSize;
            endIndex = endIndex > total ? total : endIndex;
            List queryRecordList =  newList3.subList(startIndex, endIndex);
            List<Object[]> lists = new ArrayList<Object[]>();
            for(int i=0;i<queryRecordList.size();i++){
    	    	Object[] objArray = (Object[]) queryRecordList .get(i);
    	    	lists.add(objArray);
    	    }
        	QueryPage<Object[]> page = new QueryPage<>();
    	    page.setContent(lists);
    	    page.setPageSize(pageSize);
    	    page.setTotal(total);
    	    page.setNumber(curPage);
            model.addAttribute("page", page);
            model.addAttribute("toalPage", pageTotal);
        	}else{
             	QueryPage<Object[]> page = new QueryPage<>();
         	    page.setContent(new ArrayList<>());
         	    page.setPageSize(pageSize);
         	    page.setTotal(0);
         	    page.setNumber(1);
                model.addAttribute("page", page);
                model.addAttribute("toalPage", 0);
        	}
        }
    }
    
    
    //bwcer日志请求接口
	public void queryBwceDataInterface(Model model, Integer curPage, Integer pageSize, String fileDir,
			String operationType, String createStartDate, String createEndDate, String updateStartDate,
			String updateEndDate, String queryFlag) {
		GetLogFileListRq getLogFileListRq = new GetLogFileListRq();
		getLogFileListRq.setCurPage(curPage);
		getLogFileListRq.setPageSize(pageSize);
		getLogFileListRq.setFileDir(fileDir);
		GetLogFileListRs getLogFileListRs = interfaceService.getLogFileList(getLogFileListRq);
		logger.info("---->Data returned from the bwce interface log:"+getLogFileListRs);
		List<LogFile> recordList = getLogFileListRs.getRecordList();
		ArrayList list = new ArrayList<>();
		for (int i = 0; i < recordList.size(); i++) {
			String[] objArray = recordList.get(i).getItem().split(",");
			list.add(objArray);
		}
		ArrayList newList2 = new ArrayList<>();
		ArrayList newList3 = new ArrayList<>();
		boolean flag = false;
		// 类型刷选后，有数据,遍历
		if (!list.isEmpty()) {
			if (StringUtils.isNoneBlank(createStartDate) || StringUtils.isNoneBlank(createEndDate)) {
				flag = true;
				for (int i = 0; i < list.size(); i++) {
					String[] str = (String[]) list.get(i);
					String createStart = str[2].substring(0, 10).replace("-", "/");
					if (compareCreateTime(createStart, createStartDate, createEndDate)) {
						newList2.add(list.get(i));
					}
				}
				// 创建时间条件刷选没有数据，直接返回
				if (newList2.isEmpty()) {
					newList3 = null;
				} else {
					// 更新时间条件刷选
					if (StringUtils.isNoneBlank(updateStartDate) || StringUtils.isNoneBlank(updateEndDate)) {
						for (int i = 0; i < newList2.size(); i++) {
							String[] str = (String[]) newList2.get(i);
							String updateStart = str[3].substring(0, 10).replace("-", "/");
							if (compareCreateTime(updateStart, updateStartDate, updateEndDate)) {
								newList3.add(newList2.get(i));
							}
						}
					} else {
						newList3 = newList2;
					}
				}
			} else {
				if (StringUtils.isNoneBlank(updateStartDate) || StringUtils.isNoneBlank(updateEndDate)) {
					for (int i = 0; i < list.size(); i++) {
						String[] str = (String[]) list.get(i);
						String updateStart = str[3].substring(0, 10).replace("-", "/");
						if (compareCreateTime(updateStart, updateStartDate, updateEndDate)) {
							newList3.add(list.get(i));
						}
					}
				} else {
					newList3 = list;
				}
			}
		} else {
			newList3 = null;
		}
		// 点击查询，需要把数据重新封装到分页
		if (newList3 != null) {
			int total = newList3.size();
			int pageTotal = total / pageSize + (total % pageSize > 0 ? 1 : 0);
			int startIndex = (curPage - 1) * pageSize;
			int endIndex = curPage * pageSize;
			endIndex = endIndex > total ? total : endIndex;
			List<String[]> queryRecordList = newList3.subList(startIndex, endIndex);
			ArrayList lists = new ArrayList<>();
			for (int i = 0; i < queryRecordList.size(); i++) {
				String[] objArray = queryRecordList.get(i);
				lists.add(objArray);
			}
			QueryPage<Object[]> page = new QueryPage<>();
			page.setContent(lists);
			page.setPageSize(pageSize);
			page.setTotal(total);
			page.setNumber(curPage);
			model.addAttribute("page", page);
			model.addAttribute("toalPage", pageTotal);
		} else {
			QueryPage<Object[]> page = new QueryPage<>();
			page.setContent(new ArrayList<>());
			page.setPageSize(pageSize);
			page.setTotal(0);
			page.setNumber(1);
			model.addAttribute("page", page);
			model.addAttribute("toalPage", 0);
		}
	}
			    	
    //下载模板
    @RequestMapping("/logDownload")
    public String excleDownload(HttpServletRequest request,HttpServletResponse response,String logName,String logType,String logFileDir) {
        
    	if(logType.equals("WEB")){
        response.setCharacterEncoding("utf-8");
        String name = logName;
        response.setContentType("text/html");
        String codedFileName = null;//编码处理后文件名
        try {
            
            //String path="D:\\logTest";//
            String path = systemLogRepository.findWebPath();
            String fileName = logName;
            InputStream inputStream = new FileInputStream(new File(path+ File.separator + fileName));
            response.reset();
            
            String agent = request.getHeader("USER-AGENT");  
            if (null != agent && -1 != agent.indexOf("MSIE") || null != agent && -1 != agent.indexOf("Trident")) {// ie  
                 codedFileName = java.net.URLEncoder.encode(name, "UTF8");  
                 
            } else if (null != agent && -1 != agent.indexOf("Mozilla")) {// 火狐,chrome等  
          	  
          	  codedFileName = new String(name.getBytes("UTF-8"), "iso-8859-1");  
            }  
            response.setHeader("Content-disposition", "attachment; filename=" + codedFileName /*+ ".log"*/);
            response.setContentType("application/x-download");
            OutputStream os = response.getOutputStream();
            byte[] uft8bom = {( byte) 0xef, (byte) 0xbb, (byte) 0xbf};
        	os.write(uft8bom);
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
    }
    	if(logType.equals("BWCE")){
    		bwceDownload(request,response,logName,logType,logFileDir);
    	}
    	if(logType.equals("FRONT")||logType.equals("APPWEB")){
    		frontDownload(request,response,logName,logType,logFileDir);
    	}
        return null;
    }
    

	//bwce下载
    public String bwceDownload(HttpServletRequest request,HttpServletResponse response,String logName,String logType,String logFileDir) {
    	
    	try {
			logFileDir = java.net.URLDecoder.decode(logFileDir,"UTF-8");
			logName = java.net.URLDecoder.decode(logName,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
        response.setCharacterEncoding("utf-8");
        String name = logName;
        response.setContentType("text/html");
        String codedFileName = null;//编码处理后文件名
        try {
        	String bwceIp = systemLogRepository.findBwceIp();
        	String path=bwceIp+logFileDir;
            String fileName = logName;
            response.reset();
            
            String agent = request.getHeader("USER-AGENT");  
            if (null != agent && -1 != agent.indexOf("MSIE") || null != agent && -1 != agent.indexOf("Trident")) {// ie  
                 codedFileName = java.net.URLEncoder.encode(name, "UTF8");  
                 
            } else if (null != agent && -1 != agent.indexOf("Mozilla")) {// 火狐,chrome等  
          	  
          	  codedFileName = new String(name.getBytes("UTF-8"), "iso-8859-1");  
            }  
            response.setHeader("Content-disposition", "attachment; filename=" + codedFileName /*+ ".log"*/);
            response.setContentType("application/x-download");
            
            RestTemplate restTemplate = new ResttemplateUtile().getRestTemplate("http", "60000");
            ResponseEntity<byte[]> responseData = restTemplate.getForEntity(path, byte[].class);
            
            OutputStream os = response.getOutputStream();
            byte[] uft8bom = {( byte) 0xef, (byte) 0xbb, (byte) 0xbf};
            os.write(uft8bom);
            os.write(responseData.getBody());
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return null;
    }
    
    //前置下载
    public String frontDownload(HttpServletRequest request, HttpServletResponse response, String logName, String logType,String logFileDir) {
    	try {
			logFileDir = java.net.URLDecoder.decode(logFileDir,"UTF-8");
			logName = java.net.URLDecoder.decode(logName,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
        response.setCharacterEncoding("utf-8");
        String name = logName;
        response.setContentType("text/html");
        String codedFileName = null;//编码处理后文件名
        try {
            response.reset();
            String agent = request.getHeader("USER-AGENT");  
            if (null != agent && -1 != agent.indexOf("MSIE") || null != agent && -1 != agent.indexOf("Trident")) {// ie  
                 codedFileName = java.net.URLEncoder.encode(name, "UTF8");  
                 
            } else if (null != agent && -1 != agent.indexOf("Mozilla")) {// 火狐,chrome等  
          	  
          	  codedFileName = new String(name.getBytes("UTF-8"), "iso-8859-1");  
            }  
            response.setHeader("Content-disposition", "attachment; filename=" + codedFileName /*+ ".log"*/);
            response.setContentType("application/x-download");
            
            RestTemplate restTemplate = new ResttemplateUtile().getRestTemplate("http", "60000");
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json; charset=utf-8");
            String frontPath = systemLogRepository.findFrontPath();
            String myurl = frontPath+"/downloadSystemLog/";
            JSONObject obj = new JSONObject();
            obj.put("path",logFileDir);
            HttpEntity requestEntity = new HttpEntity(JSON.toJSONString(obj), headers);
            ResponseEntity<byte[]> responseData = restTemplate.exchange(myurl, HttpMethod.POST,requestEntity,byte[].class);
            byte[] backData = responseData.getBody();
            OutputStream os = response.getOutputStream();
            byte[] uft8bom = {( byte) 0xef, (byte) 0xbb, (byte) 0xbf};
            os.write(uft8bom);
            os.write(responseData.getBody());
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
	}
    
    //比较时间
    public boolean compareCreateTime(String time,String StartDate,String EndDate){
    	try {
    		    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
				Long updateTime = dateFormat.parse(time).getTime();
				Long updateStartTime = dateFormat.parse(StartDate).getTime();
				Long updateEndTime = dateFormat.parse(EndDate).getTime();
				if(updateStartTime<=updateTime&&updateTime<=updateEndTime){
					return true;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		return false;
    }
    
    @RequestMapping("/systemLogsForIt")
    public String SystemLogsForIt(Model model){
        return  "it/systemLogsForIt";
    }
}
