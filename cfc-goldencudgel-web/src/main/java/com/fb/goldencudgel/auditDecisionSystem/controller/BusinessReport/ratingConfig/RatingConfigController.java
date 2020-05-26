package com.fb.goldencudgel.auditDecisionSystem.controller.BusinessReport.ratingConfig;

import com.comwave.core.data.domain.QueryPage;
import com.fb.goldencudgel.auditDecisionSystem.domain.ratingConfig.RatingConfig;
import com.fb.goldencudgel.auditDecisionSystem.repository.RatingConfigRepository;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.RatingConfigServiceImpI;
import com.fb.goldencudgel.auditDecisionSystem.utils.CommonUtils;
import com.mysql.fabric.xmlrpc.base.Data;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/ratingConfig")
public class RatingConfigController {

    private final Logger logger = LoggerFactory.getLogger(RatingConfigController.class);
    @Autowired
    RatingConfigRepository ratingConfigRepository;
    @Autowired
    RatingConfigServiceImpI ratingConfigService;

    private static final Integer PAGE_SIZE = 20;

    @RequestMapping("/viewRatingConfig")
    public String viewRatingConfig(Model model,String searchData){
        CommonUtils.setAttribute(model,searchData);
        return "ratingConfig/viewRatingConfig";
    }

    @RequestMapping("/queryRatingConfigConfig")
    public  String  queryApplyIncom  (Model model, String rating,  String startDate, String endDate,
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
        }catch (Exception e){
            logger.info(e.getMessage());
        }
        QueryPage<Object[]> page = ratingConfigService.findByConditions(rating,startDate,endDate,new QueryPage<Object[]>(curPage, PAGE_SIZE),startDates,endDates);

        model.addAttribute("page",page);
        model.addAttribute("viewAbilityList",page.getContent());
        return "ratingConfig/viewRatingList";
    }

    @RequestMapping("/seeRating")
    public  String  seeRating (Model model,String configid,String searchData){
        RatingConfig ratingConfig = ratingConfigRepository.getOne(configid);
        model.addAttribute("ratingConfig",ratingConfig);
        model.addAttribute("searchData",searchData);
        return  "ratingConfig/RatingDetail";
    }
    @RequestMapping("/editRating")
    public  String  editRating (Model model,String configid,String searchData){
        RatingConfig ratingConfig = ratingConfigRepository.getOne(configid);
        model.addAttribute("ratingConfig",ratingConfig);
        model.addAttribute("searchData",searchData);
        return  "ratingConfig/editRatingDetail";
    }

    public Model getData(Model model,String configid,String searchData){
        RatingConfig ratingConfig = ratingConfigRepository.getOne(configid);
        model.addAttribute("ratingConfig",ratingConfig);
        model.addAttribute("searchData",searchData);
        return model;
    }

    @RequestMapping("/saveRating")
    @ResponseBody
    public  String  saveRating (String rating,String ratingDes,String id){
        RatingConfig ratingConfig = ratingConfigRepository.getOne(id);
        Date date = new Date();       
        Timestamp updateTime = new Timestamp(date.getTime());
        ratingConfig.setUpdateTime(updateTime);
        System.out.println(ratingConfig.getUpdateTime());
        try {
            ratingConfig.setRatingDescribe(ratingDes);
            ratingConfigRepository.save(ratingConfig);
            return  "儲存成功";
        } catch (Exception e) {
            e.printStackTrace();
            return  "儲存失敗";
        }
    }
}
