package com.fb.goldencudgel.auditDecisionSystem.controller.negativeIndustry;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

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
import com.fb.goldencudgel.auditDecisionSystem.domain.fbYearAppropriationRate.FbYearAppropriationRate;
import com.fb.goldencudgel.auditDecisionSystem.domain.holiday.Holiday;
import com.fb.goldencudgel.auditDecisionSystem.domain.negativeIndustry.NegativeIndustry;
import com.fb.goldencudgel.auditDecisionSystem.model.User;
import com.fb.goldencudgel.auditDecisionSystem.repository.NegativeIndustryRepository;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.NegativeIndustryServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.utils.ExcelUtil;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;

@Controller
@RequestMapping("/negativeIndustry")
public class NegativeIndustryController {

    private final Logger logger = LoggerFactory.getLogger(NegativeIndustryController.class);
    private static final Integer PAGE_SIZE = 20;

    @Autowired
    private NegativeIndustryServiceImpl negativeIndustryService;

    @Autowired
    private NegativeIndustryRepository negativeIndustryRepository;

    @RequestMapping("/viewNegativeIndustry")
    public String viewNegativeIndustry(Model model){
    	Map<String,String> industryNameList = negativeIndustryService.getIndustryNameList();//行业别名称从字典表里查询
    	model.addAttribute("industryNameList",industryNameList);
        return  "negativeIndustry/viewNegativeIndustry";
    }

    @RequestMapping("/queryNegativeIndustry")
    public String queryItemData (Model model,String negativeType,String negativeName,@RequestParam(required = false, defaultValue = "1") Integer curPage){
       QueryPage<Object[]> page = negativeIndustryService.findByTypeAndName(negativeType,negativeName,new QueryPage<Object[]>(curPage,PAGE_SIZE));
       model.addAttribute("page",page);
       model.addAttribute("number",page.getContent().size());
       model.addAttribute("datas",page.getContent());
       model.addAttribute("curPage",curPage);
       return "negativeIndustry/negativeIndustryList";
    }

    //删除
    @ResponseBody
    @RequestMapping("/deleteNegativeIndustry")
    public String deleteItemData(String negativeId){
        negativeIndustryRepository.deleteByNegativeId(negativeId);
        return "success";
    }
    
    //新增
	@ResponseBody
	@RequestMapping("/saveNewNegative")
	public String saveNewNegative(String negativeId, String newNegativeName, String newNegativeScore) {
		String result = "success";
		try {
			NegativeIndustry hasNegativeIndustry = negativeIndustryRepository.findByNegativeId(negativeId);
			if (hasNegativeIndustry != null) {
				result = "error";
			} else {
				NegativeIndustry negativeIndustry = new NegativeIndustry();
				// negativeIndustry.setNegativeId(UUID.randomUUID().toString().replaceAll("-",""));
				negativeIndustry.setNegativeId(negativeId);
				negativeIndustry.setCreateTime(new Date());
				negativeIndustry.setNegativeType("T0001");
				negativeIndustry.setNegativeName(newNegativeName);
				negativeIndustry.setNegativeScore(newNegativeScore);
				negativeIndustryRepository.saveAndFlush(negativeIndustry);
			}
		} catch (Exception e) {
			result = "error";
			e.printStackTrace();
		}
		return result;
	}

    //修改
    @ResponseBody
    @RequestMapping("/saveNegativeIndustry")
    public String saveItemData(String negativeId, String negativeType,String negativeName,String negativeScore){
        String result = "success";
        try {
        	NegativeIndustry negativeIndustry = negativeIndustryRepository.findByNegativeId(negativeId);
        	negativeIndustry.setNegativeName(negativeName);
        	negativeIndustry.setNegativeType(negativeType);
        	negativeIndustry.setNegativeScore(negativeScore);
        	negativeIndustryRepository.save(negativeIndustry);
        } catch (Exception e) {
            result = "error";
            e.printStackTrace();
        }
        return result;
    }
    
  //下载模板
    @RequestMapping("/excleDownload")
    public String excleDownload(HttpServletRequest request,HttpServletResponse response) {
       
        response.setCharacterEncoding("utf-8");
        String name = "負面表列行業導入模板";
        response.setContentType("text/html");
        String codedFileName = null;//编码处理后文件名
        try {
            
            //String path="D:";//把假日管理導入模板.xlsx模板复制到D盘下
      	    String path="/app/cache/excel/";//docker容器
            String fileName = "negativeIndustry.xlsx";
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
						List<List<Object>> lists = new ArrayList<>();
						List<Object> dataLists = new ArrayList<>();
						dataList = ExcelUtil.readFromFile(file);
						//格式校验
						String firstTitle = dataList.get(0).get(0) == null ? ""
								: dataList.get(0).get(0).toString();
						if(!"負面行業名稱".equals(firstTitle)){
							result.put("message", "格式有誤");
							result.put("success", false);
							return result;
						}
						if (StringUtils.isBlank(firstTitle)) {
							result.put("message", "請填寫負面行業名稱");
							result.put("success", false);
							return result;
						}
						String secondTitle = dataList.get(0).get(1) == null ? ""
								: dataList.get(0).get(1).toString();
						if(!"中華民國稅務行業標準分類(細類)".equals(secondTitle)){
							result.put("message", "格式有誤");
							result.put("success", false);
							return result;
						}
						if (StringUtils.isBlank(secondTitle)) {
							result.put("message", "請填寫中華民國稅務行業標準分類(細類)");
							result.put("success", false);
							return result;
						}
						// 从第二行开始读取数据
						for (int j = 1; j < dataList.size(); j++) {
							// 判断是否已读取完excle数据
							if (dataList.get(j).get(0).equals("") && dataList.get(j).get(1).equals("")) {
								break;
							}
							String NEGATIVE_ID = UUID.randomUUID().toString();
							dataLists.add(NEGATIVE_ID);
							String NEGATIVE_TYPE = "T0001";
							dataLists.add(NEGATIVE_TYPE);
							String NEGATIVE_NAME = dataList.get(j).get(0) == null ? ""
									: dataList.get(j).get(0).toString();
							dataLists.add(NEGATIVE_NAME);
							
							String NEGATIVE_SCORE = dataList.get(j).get(1) == null ? ""
									: dataList.get(j).get(1).toString();
							NEGATIVE_SCORE = NEGATIVE_SCORE.split("\\.")[0].toString();
							dataLists.add(NEGATIVE_SCORE);
							
							List<Object> minList = new ArrayList<>();
							for (int a = 0; a < dataLists.size(); a++) {
								minList.add(dataLists.get(a));
							}
							lists.add(minList);
							dataLists.clear();
						}
						negativeIndustryRepository.deleteByAll();

						for (int h = 0; h < lists.size(); h++) {
							List<NegativeIndustry> fbs = new ArrayList<NegativeIndustry>();
							NegativeIndustry negativeIndustry = new NegativeIndustry();
							// negativeIndustry.setNegativeId(UUID.randomUUID().toString().replaceAll("-",""));
							negativeIndustry.setNegativeId((String) lists.get(h).get(0));
							negativeIndustry.setCreateTime(new Date());
							negativeIndustry.setNegativeType((String) lists.get(h).get(1));
							negativeIndustry.setNegativeName((String) lists.get(h).get(2));
							negativeIndustry.setNegativeScore((String) lists.get(h).get(3));
							fbs.add(negativeIndustry);
							negativeIndustryRepository.saveAll(fbs);
						}
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
    
}
