package com.fb.goldencudgel.auditDecisionSystem.controller.BusinessReport.abilityConfig;
import com.comwave.core.data.domain.QueryPage;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.AbilityConfigServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/abilityConfig")
public class AbilityConfigContronller {

    private final Logger logger = LoggerFactory.getLogger(AbilityConfigContronller.class);

    @Autowired
    AbilityConfigServiceImpl abilityConfigService;

    private final static Integer PAGE_SIZE = 15;


    /**企業能力配置页面**/
    /*@RequestMapping("/viewAbilityConfig")
    public String viewAbilityConfig(){ return "abilityConfig/viewAbilityConfig"; }*/
    
    @RequestMapping("/viewAbilityConfig")
    public String viewAbilityConfig(Model model,String configId,@RequestParam(required = false, defaultValue = "1") Integer curPage
    		,String type,String business,String debit,String financial,String conpanyYear,String startDate,String endDate,String startDates,String endDates){
    	
    	model.addAttribute("type",type);
        model.addAttribute("business",business);
        model.addAttribute("debit",debit);
        model.addAttribute("financial",financial);
        model.addAttribute("conpanyYear",conpanyYear);
        model.addAttribute("startDate",startDate);
        model.addAttribute("endDate",endDate);
        model.addAttribute("startDates",startDates);
        model.addAttribute("endDates",endDates);
        model.addAttribute("page_select",curPage);
    	
    	
    	return "abilityConfig/viewAbilityConfig"; 
    	
    }


    /**查询企業能力配置**/
    @RequestMapping("/abilityConfigList")
    public String abilityConfigList(Model model,String type, String financial,
                                    String debit,String business,String conpanyYear,String startDate, String endDate,String startDates, String endDates,@RequestParam(required = false, defaultValue = "1") Integer curPage){

        QueryPage<Object[]> page = abilityConfigService.queryAbilityConfig( type,  financial,
                debit, business, conpanyYear, startDate, endDate, startDates, endDates,new QueryPage<Object[]>(curPage, PAGE_SIZE));
        model.addAttribute("abilityConfigList",page.getContent());
        model.addAttribute("page",page);

        return "abilityConfig/abilityConfigList";
    }


    /**查询企業能力配置**/
   /* @RequestMapping("/lookAbilityConfig")
    public String lookAbilityConfig(Model model,String configId,String operate){

        QueryPage<Object[]> page = abilityConfigService.lookAbilityConfig(configId);
        model.addAttribute("ability",page.getContent().get(0));
        model.addAttribute("page",page);

        //operate:"1"为查看,"2为修改"
        if ("1".equals(operate)){
            return "abilityConfig/lookAbilityConfig";
        }else{
            return "abilityConfig/updataAbilityConfig";
        }

    }*/
    
    @RequestMapping("/lookAbilityConfig")
    public String lookAbilityConfig(Model model,String configId,String operate,String type,String business,String debit,String financial,String conpanyYear,String startDate,String endDate,
    		String startDates,String endDates,String page_select,@RequestParam(required = false, defaultValue = "1") Integer curPage){

        QueryPage<Object[]> page = abilityConfigService.lookAbilityConfig(configId);
        model.addAttribute("ability",page.getContent().get(0));
        model.addAttribute("page",page);
        
        
        model.addAttribute("type",type);
        model.addAttribute("business",business);
        model.addAttribute("debit",debit);
        model.addAttribute("financial",financial);
        model.addAttribute("conpanyYear",conpanyYear);
        model.addAttribute("startDate",startDate);
        model.addAttribute("endDate",endDate);
        model.addAttribute("startDates",startDates);
        model.addAttribute("endDates",endDates);
        model.addAttribute("page_select",page_select);

        //operate:"1"为查看,"2为修改"
        if ("1".equals(operate)){
            return "abilityConfig/lookAbilityConfig";
        }else{
            return "abilityConfig/updataAbilityConfig";
        }

    }


    /**查询企業能力配置**/
    @RequestMapping("/updataAbilityConfig")
    @ResponseBody
    public boolean updataAbilityConfig(Model model,String configId,String abiDescribe){
        //修改数据
        boolean bo = abilityConfigService.updataAbilityConfig(configId, abiDescribe);
        return bo;


    }



}
