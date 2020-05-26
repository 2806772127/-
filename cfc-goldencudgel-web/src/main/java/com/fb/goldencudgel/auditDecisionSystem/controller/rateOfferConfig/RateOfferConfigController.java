package com.fb.goldencudgel.auditDecisionSystem.controller.rateOfferConfig;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fb.goldencudgel.auditDecisionSystem.domain.holiday.Holiday;
import com.fb.goldencudgel.auditDecisionSystem.domain.rateOfferConfig.RateOfferConfig;
import com.fb.goldencudgel.auditDecisionSystem.repository.RateOfferConfigRepository;
import com.fb.goldencudgel.auditDecisionSystem.utils.ExcelUtil;


@Controller
@RequestMapping("/rateOfferConfig")
public class RateOfferConfigController {

  private static final Logger logger = LoggerFactory.getLogger(RateOfferConfigController.class);
  
 
   
   @Autowired
   private RateOfferConfigRepository rateOfferConfigRepository;


  @RequestMapping("/viewRateOfferConfig")
  public String viewRateOfferCOnfig(){

    return "rateOfferConfig/viewRateOfferConfig";
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

            //从第四行开始读取数据
            for (int j = 3; j < dataList.size(); j++) {
                // 判断是否已读取完excle数据
                if (dataList.get(j).get(0).equals("") && dataList.get(j).get(1).equals("") 
                        && dataList.get(j).get(2).equals("") && dataList.get(j).get(3).equals("")
                        && dataList.get(j).get(4).equals("") && dataList.get(j).get(5).equals("")) {
                    break;
                }
                
                for(int a=2; a<dataList.get(2).size();a++) {
                	
                	//如果参考报价是空，就不需要进行保存
                	if(!dataList.get(j).get(a).equals("")) {
                		String B_PROD_CATEGORY = dataList.get(0).get(a).toString();
                		String B_PROD_TYPE = dataList.get(1).get(a).toString();
                		String B_GRADE = dataList.get(j).get(0).toString();
                		String B_PERIODS = dataList.get(j).get(1).toString().split("\\.")[0];
                		String B_QUOTE_PRICE;
                		if(dataList.get(j).get(a).toString().equals("0.0")) {
                			B_QUOTE_PRICE = "0";
                		}else {
                		//从excle读取参考报价，它是百分数读取，没有把%一起读取进来，读进来的是doube，需要转换
                		String B_QUOTE_PRICE_STRING = dataList.get(j).get(a).toString();
                		// 读取excle数值太大，会出现科学计数法，解决处理
                        BigDecimal bigDecimal = new BigDecimal(B_QUOTE_PRICE_STRING);
                        BigDecimal num = new BigDecimal(100);
                        BigDecimal TWO_DECIMAL_PLACES = bigDecimal.multiply(num);
                        //小数点保留两位小数
                		DecimalFormat df = new DecimalFormat("0.00");
                	    B_QUOTE_PRICE = df.format(TWO_DECIMAL_PLACES)+"%";
                		}
                	    
                		dataLists.add(B_PROD_CATEGORY);
                		dataLists.add(B_PROD_TYPE);
                		dataLists.add(B_GRADE);
                		dataLists.add(B_PERIODS);
                		dataLists.add(B_QUOTE_PRICE);
                		
                	    List<Object> minList = new ArrayList<>();
                        for (int z = 0; z < dataLists.size(); z++) {
                             minList.add(dataLists.get(z));
                        }
                        lists.add(minList);
                        dataLists.clear();
                	}
                }
            }
                rateOfferConfigRepository.deleteAll();
	            for (int h = 0; h < lists.size(); h++) {
	            	
	                String PROD_CATEGORY = (String) lists.get(h).get(0);
	                String PROD_TYPE = (String) lists.get(h).get(1);
	                String GRADE = (String) lists.get(h).get(2);
	                String PERIODS = (String) lists.get(h).get(3);
	                String QUOTE_PRICE = (String) lists.get(h).get(4);
	
	                RateOfferConfig roc = new RateOfferConfig();
	                roc.setProdCategory(PROD_CATEGORY);
	                roc.setProdType(PROD_TYPE);
	                roc.setGrade(GRADE);
	                roc.setPeriods(PERIODS);
	                roc.setQuotePrice(QUOTE_PRICE);
	                roc.setCreateTime(new Date());
	                rateOfferConfigRepository.save(roc);
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

