package com.fb.goldencudgel.auditDecisionSystem.controller.BusinessReport.indicatorsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import com.comwave.core.data.domain.QueryPage;
import com.fb.goldencudgel.auditDecisionSystem.domain.abilityCompareConfig.AbilityCompareConfig;
import com.fb.goldencudgel.auditDecisionSystem.domain.incatorsConfig.IncatorsConfig;
import com.fb.goldencudgel.auditDecisionSystem.repository.IncatorsConfigRepository;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.IndicatorsConfigServiceImpI;
import com.fb.goldencudgel.auditDecisionSystem.utils.CommonUtils;

@Controller
@RequestMapping("/indicatorsConfig")
public class IndicatorsConfigController {
    private final Logger logger = LoggerFactory.getLogger(IndicatorsConfigController.class);

    @Autowired
    IndicatorsConfigServiceImpI indicatorsConfigService;
    
    @Autowired
    IncatorsConfigRepository incatorsConfigRepository;
    
    private static final Integer PAGE_SIZE = 20;
    
    @RequestMapping("viewIndicatorsConfig")
    public String viewIndicatorsConfig(Model model,String searchData){
    	 CommonUtils.setAttribute(model,searchData);
    	return "indicatorsConfig/viewIndicatorsConfig";
    }
    

    
    @RequestMapping("/queryIndicatorsConfig")
    public  String  queryApplyIncom  (Model model, String type, String industryType, String establishYear, String startDate, String endDate,
                                      @RequestParam(required = false, defaultValue = "1") Integer curPage,
                                      String startDates, String endDates){
                try {
                    if(StringUtils.isNoneBlank(startDate)){
                        startDate =startDate.replace("/","-");
                    }
                    if(StringUtils.isNoneBlank(endDate)){
                        endDate = endDate.replace("/","-");
                    }
                    if(StringUtils.isNoneBlank(startDates)){
                    	startDates =startDates.replace("/","-");
                    }
                    if(StringUtils.isNoneBlank(endDates)){
                    	endDates = endDates.replace("/","-");
                    }
                }catch (Exception e)
                {
                    logger.info(e.getMessage());
                }

        QueryPage<Object[]> page = indicatorsConfigService.findByConditions(type,industryType,establishYear,startDate,
        endDate,new QueryPage<Object[]>(curPage, PAGE_SIZE),startDates,endDates);
        model.addAttribute("page",page);
        model.addAttribute("viewIndicatorsList",page.getContent());
        model.addAttribute("curPage",curPage);
        return "indicatorsConfig/viewIndicatorsConfigList";
    }
    
    @RequestMapping("/seeIndicators")
    public  String  seeIndicators (Model model,String configid,@RequestParam(required = false, defaultValue = "1") Integer curPage,String searchData){
        QueryPage<Object[]> page=indicatorsConfigService.findById(configid,new QueryPage<Object[]>(curPage, PAGE_SIZE));
        model.addAttribute("searchData",searchData);
        model.addAttribute("pages",page);
        model.addAttribute("viewIndicatorsList",page.getContent());
        return  "indicatorsConfig/IndicatorsDetail";
    }

    
    @RequestMapping("/editIndicators")
    public  String  editIndicators (Model model,String configid,@RequestParam(required = false, defaultValue = "1") Integer curPage,String searchData){
        QueryPage<Object[]> page=indicatorsConfigService.findUpdateById(configid,new QueryPage<Object[]>(curPage, PAGE_SIZE));
        model.addAttribute("searchData",searchData);
        model.addAttribute("pages",page);
        model.addAttribute("viewIndicatorsList",page.getContent());
        return  "indicatorsConfig/editIndicators";
    }
    
   
    @RequestMapping("/saveIndicators")
    @ResponseBody
    public  String  saveIndicators (String type, String industryType, String establishYear, String rate, String id,Timestamp creatTime){
    	IncatorsConfig incatorsConfig=new IncatorsConfig();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);

        try {

        	incatorsConfig.setConfigId(id);
        	incatorsConfig.setPowerType(type);
        	incatorsConfig.setIndustryType(industryType);
        	incatorsConfig.setConpanyYearType(establishYear);
        	incatorsConfig.setRate(Double.parseDouble(rate));
        	incatorsConfig.setCreateTime(creatTime);
            Date date = new Date();
            Timestamp updateTime = new Timestamp(date.getTime());
            incatorsConfig.setUpdateTime(updateTime);
        	incatorsConfigRepository.save(incatorsConfig);
            return  "儲存成功";
        } catch (Exception e) {
            e.printStackTrace();
            return  "儲存失敗";
        }



    }
    
    
}
