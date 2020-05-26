package com.fb.goldencudgel.auditDecisionSystem.controller.BusinessReport.abilityCompareConfig;
import com.comwave.core.data.domain.QueryPage;
import com.fb.goldencudgel.auditDecisionSystem.domain.abilityCompareConfig.AbilityCompareConfig;
import com.fb.goldencudgel.auditDecisionSystem.repository.AbiliyCompareConfigRepository;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.AbilibtyConmpareConfigServiceImpI;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/abilityCompareConfig")
public class AbilityCompareConfigController {
    private final Logger logger = LoggerFactory.getLogger(AbilityCompareConfigController.class);
    @Autowired
    AbiliyCompareConfigRepository abiliyCompareConfigRepository;
    @Autowired
    AbilibtyConmpareConfigServiceImpI abilibtyConmpareConfigService;
    private static final Integer PAGE_SIZE = 20;
    @RequestMapping("/viewAbilityCompareConfig")
    public String viewAbilityCompareConfig( Model model, @RequestParam(required = false, defaultValue = "1") Integer curPage,String type, String abilityType, String startDate, String endDate, String startDates, String endDates){

        model.addAttribute("type",type);
        model.addAttribute("abilityType",abilityType);
        model.addAttribute("startDate",startDate);
        model.addAttribute("startDates",startDates);
        model.addAttribute("endDate",endDate);
        model.addAttribute("endDates",endDates);
        model.addAttribute("curPage",curPage);

        return "abilityCompareConfig/viewAbilityCompareConfig";
    }





    @RequestMapping("/queryabilityCompareConfig")
    public  String  queryApplyIncom  (Model model, String type, @RequestParam(required = false, defaultValue = "")String abilityType, String startDate, String endDate,
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

        QueryPage<Object[]> page = abilibtyConmpareConfigService.findByConditions(type,abilityType,startDate,
                endDate,new QueryPage<Object[]>(curPage, PAGE_SIZE),startDates,endDates);
        model.addAttribute("abilityType",abilityType);
        model.addAttribute("startDate",startDate);
        model.addAttribute("startDates",startDates);
        model.addAttribute("endDate",endDate);
        model.addAttribute("endDates",endDates);
        model.addAttribute("type",type);
        model.addAttribute("page",page);
        model.addAttribute("viewAbilityList",page.getContent());
        model.addAttribute("curPage",curPage);
        return "abilityCompareConfig/viewAbilityList";
    }

    @RequestMapping("/seeAbility")
    public  String  seeAbility (Model model,String configid,@RequestParam(required = false, defaultValue = "1") Integer curPage,
                                String type, String abilityType, String startDate, String endDate, String startDates, String endDates){

        QueryPage<Object[]> page=abilibtyConmpareConfigService.findById(configid,new QueryPage<Object[]>(curPage, PAGE_SIZE));
        model.addAttribute("abilityType",abilityType);
        model.addAttribute("startDate",startDate);
        model.addAttribute("startDates",startDates);
        model.addAttribute("endDate",endDate);
        model.addAttribute("endDates",endDates);
        model.addAttribute("type",type);
        model.addAttribute("pages",page);
        model.addAttribute("viewAbilityList",page.getContent());
        model.addAttribute("curPage",curPage);
        return  "abilityCompareConfig/AbilityDetail";
    }

    @RequestMapping("/editAbility")
    public  String  editAbility (Model model,String configid,@RequestParam(required = false, defaultValue = "1") Integer curPage,
                                 String type, String abilityType, String startDate, String endDate, String startDates, String endDates){
        QueryPage<Object[]> page=abilibtyConmpareConfigService.findByIdEdit(configid,new QueryPage<Object[]>(curPage, PAGE_SIZE));
        model.addAttribute("abilityType",abilityType);
        model.addAttribute("startDate",startDate);
        model.addAttribute("startDates",startDates);
        model.addAttribute("endDate",endDate);
        model.addAttribute("endDates",endDates);
        model.addAttribute("type",type);
        model.addAttribute("pages",page);
        model.addAttribute("viewAbilityList",page.getContent());
        model.addAttribute("curPage",curPage);
        return  "abilityCompareConfig/editAbility";
    }


    @RequestMapping("/saveAbility")
    @ResponseBody
    public  String  saveAbility (Double startRange, Double endRange, String ability
            , String starts, String ends, String id, String abilityType, String type,Timestamp creatTime){
    AbilityCompareConfig abilityCompareConfig=new AbilityCompareConfig();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);

        try {
            abilityCompareConfig.setCreatetime(creatTime);
            abilityCompareConfig.setAbilityDescribe(ability);
            abilityCompareConfig.setAbilityType(abilityType);
            abilityCompareConfig.setConfigId(id);
            abilityCompareConfig.setEndIncludeFlag(ends);
            abilityCompareConfig.setStartRange(startRange);
            abilityCompareConfig.setEndRange(endRange);
            abilityCompareConfig.setStartIncludeFlag(starts);
            abilityCompareConfig.setType(type);
            Date date = new Date();
            Timestamp updateTime = new Timestamp(date.getTime());
            abilityCompareConfig.setUpdatetime(updateTime);
            abiliyCompareConfigRepository.save(abilityCompareConfig);
            return  "儲存成功";
        } catch (Exception e) {
// TODO Auto-generated catch block
            e.printStackTrace();
            return  "儲存失敗";
        }



    }
}
