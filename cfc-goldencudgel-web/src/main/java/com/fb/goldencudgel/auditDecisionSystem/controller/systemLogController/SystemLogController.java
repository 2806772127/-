package com.fb.goldencudgel.auditDecisionSystem.controller.systemLogController;

import com.comwave.core.data.domain.QueryPage;
import com.fb.goldencudgel.auditDecisionSystem.repository.SystemLogRepository;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.SystemLogServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Controller
@RequestMapping("/systemLog")
public class SystemLogController {

    private final Logger logger = LoggerFactory.getLogger(SystemLogController.class);
    private static final Integer PAGE_SIZE = 20;

    @Autowired
    private SystemLogRepository systemLogRepository;

    @Autowired
    private SystemLogServiceImpl systemLogService;

    @RequestMapping("/viewSystemLog")
    public String viewSystemLog(Model model){
        return  "systemLog/viewSystemLog";
    }

    @RequestMapping("/querySystemLog")
    public String queryItemData (Model model,String operationType, String operationUser, String startDate, String endDate,@RequestParam(required = false, defaultValue = "1") Integer curPage){

       if(!"nothing".equals(operationType)){
         operationType= '0'+operationType;
       }


            DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            try {
                if(endDate==""){
                    QueryPage<Object[]> page = systemLogService.findByConditions(operationType, operationUser, startDate,endDate, new QueryPage<Object[]>(curPage, PAGE_SIZE));
                    model.addAttribute("page", page);
                    model.addAttribute("datas", page.getContent());
                }else {

                    Date dd = df.parse(endDate);

                    Calendar calendar = Calendar.getInstance();

                    calendar.setTime(dd);

                    calendar.add(Calendar.DAY_OF_MONTH, 1);//加一天

                    QueryPage<Object[]> page = systemLogService.findByConditions(operationType, operationUser, startDate, df.format(calendar.getTime()), new QueryPage<Object[]>(curPage, PAGE_SIZE));
                    model.addAttribute("page", page);
                    model.addAttribute("datas", page.getContent());
                }

            } catch (ParseException e) {

                e.printStackTrace();

            }



       return "systemLog/systemLogList";
    }

}
