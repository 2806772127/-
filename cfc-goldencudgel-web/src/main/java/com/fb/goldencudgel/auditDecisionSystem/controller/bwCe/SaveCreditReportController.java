package com.fb.goldencudgel.auditDecisionSystem.controller.bwCe;

import com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.FbMeasureWord;
import com.fb.goldencudgel.auditDecisionSystem.repository.MeasureWordRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.SystemConfigRepository;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.SaveCreditReportServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.utils.WordToPdf;
import com.fb.goldencudgel.auditDecisionSystem.utils.WordUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CreditReportController
 * @Author panha
 * @Date 2019/5/17 14:27
 * @Version 1.0
 **/
@Controller
@RequestMapping("/saveCreditReport")
public class SaveCreditReportController {

    @Autowired
    private Environment env;
    @Autowired
    private SaveCreditReportServiceImpl saveCreditReportService;

    @Autowired
    private SystemConfigRepository systemConfigRepository;

    @Autowired
    private MeasureWordRepository measureWordRepository;

    @RequestMapping("/saveCreditReport")
    @ResponseBody
    public Map<String,String> saveCreditReport(String compCode,String measureId,String type){
        String isTest = systemConfigRepository.findByID("IS_TEST").getKeyvalue();
        Map<String,String> map = new HashMap<String,String>();
        /*Map<String,String> params = new HashMap<String,String>();
        //1.获取到需要替换到word的值
        if("1".equals(isTest)){
            params.put("{key_1}","公司設立於民國 100年 12月");
            params.put("{key_2}","公司股東結構:自營/獨資");
            params.put("{key_3}","是否曾停業?:否");
            params.put("{key_4}","是否曾經變更負責人?:否");
            params.put("{key_5}","是否有轉投資/關係企業?:否");
            params.put("{key_6}","目前主要經營行業(產品)：111");
            params.put("{key_7}","近一年是否有變更主力產品或銷售模式?:否");
            params.put("{key_8}","是否有提供營業地租約或謄本?");
            params.put("{key_9}","登記負責人是否為實際負責人?");
            params.put("{key_10}","實際經營者婚姻狀況");
            params.put("{key_11}","實際經營者是否有子女");
            params.put("{key_12}","連帶保證人名字?");
            params.put("{key_13}","連帶保證人年次?");
            params.put("{key_14}","連帶保證人是否任職於公司?");
            params.put("{key_15}","連帶保證人與實際負責人關係?");
            params.put("{key_16}","法學網是否正常?");
            params.put("{key_17}","AML等級?");
            params.put("{key_18}","補充說明");

            params.put("{key2_1}","產銷流程");
            params.put("{key2_2}","主要銷售產品");
            params.put("{key2_3}","主要銷售客戶");
            params.put("{key2_4}","購料");
            params.put("{key2_5}","銷售");
            params.put("{key2_6}","近一年集團營收?");
            params.put("{key2_7}","近一年借戶/集團營收是否較去年營收成長或下滑達20%");
            params.put("{key2_8}","近三年借戶/集團企業營收合計?(仟元)");
            params.put("{key2_9}","公司內帳(或受訪者口述)平均月營收?(仟元)");
            params.put("{key2_10}","近3個月以存摺認定營收入帳平均金額?(仟元)");
            params.put("{key2_11}","公司內帳與近3個月以存摺認定營收入帳平均金額是否差異20%");
            params.put("{key2_12}","去年度財報銀行借款金額與聯徵借款年底餘額是否一致?");
            params.put("{key2_13}","近期是否有接獲或洽談中大額訂單？");

            params.put("{key3_1}","公司、負責人及其配偶目前金融負債餘額?(仟元)");
            params.put("{key3_2}","公司、負責人及其配偶去年同期之金融負債餘額?(仟元)");
            params.put("{key3_3}","公司、負責人及其配偶目前負債較去年同期增減數");
            params.put("{key3_4}","公司及關係企業是否有租賃借款");
            params.put("{key3_5}","公司及關係企業是否有其他借款");
            params.put("{key3_6}","公司營運是否達損益兩平?");
            params.put("{key3_7}","存款實績(仟元)");
            params.put("{key3_8}","存款活期或支存是否有大額整數金額");
            params.put("{key3_9}","是否有存貨");
            params.put("{key3_10}","是否在外有履保/保固金");
            params.put("{key3_11}","借款理由/用途");
            params.put("{key3_12}","補充說明?");

            params.put("{key4_1}","初估市價總值?");
            params.put("{key4_2}","說明總類/市值");
            params.put("{key4_3}","請說明，幾次");
            params.put("{key4_4}","銀行借款?");
            params.put("{key4_5}","信用卡循環金額");
            params.put("{key4_6}","預借現金金額??");
            params.put("{key4_7}","初估資產總市值?");

        }else{
            params = this.saveCreditReportService.getCreditReports(measureId,type);
            if(params.size() < 10){
                map.put("error","生成征信报告失败");
                return map;
            }
        }
        //通过行业别不同，替换不同的模板类型
        if("07".equals(type) || "08".equals(type) || "09".equals(type) || "10".equals(type) || "11".equals(type) || "12".equals(type)){
            String wordTemplate = "FILE_CREDIT_REPORT_WORD_"+type;
            //2.替换word文档
            String wordUrl = systemConfigRepository.findByID(wordTemplate).getKeyvalue();
            String buffPath = systemConfigRepository.findByID("FILE_BUFF_PATH").getKeyvalue();
            String returnUrl  =  compCode+new Date().getTime()+".docx";
            String newWordUrl = WordUpdate.UpdateWord(wordUrl,buffPath+returnUrl,params);
            //3.将word文档转换为pdf
            String pdfUrl =compCode+new Date().getTime()+".pdf";
            String newPdfUrl = WordToPdf.wordToPdf(newWordUrl,buffPath+pdfUrl);
            map = this.saveCreditReportService.saveMongodbFile(newPdfUrl);

            //this.saveCreditReportService.FileToRuiyang("11111111111111","2222222222222222",compCode);
            return map;
        }else{
            map.put("error","生成征信报告失败,行业别错误");
            return map;
        }*/
        //查询该测字id下所有的问卷类型（5种问卷）
        List list =  this.saveCreditReportService.findTypeByMeasureId(measureId);
        if(list.size()<5){
            map.put("error","问卷没有答写完毕");
            return map;
        }
        //获取到问卷的行业别类型
        for(Object s :list){
            String t = s.toString();
            if(!t.equals("04") && !t.equals("05") && !t.equals("06") && !t.equals("13")){
                type = t;
            }
        }
        String buffPath= systemConfigRepository.findByID("FILE_BUFF_PATH").getKeyvalue();
        String newPdfUrl = this.saveCreditReportService.findQuestionByMeasureId2(measureId,type);
        map = this.saveCreditReportService.saveMongodbFile(newPdfUrl);
        /*//通过测字id查询到行程id
        FbMeasureWord fbMeasureWord = this.measureWordRepository.findByMeasureId(measureId);
        this.saveCreditReportService.fileToRuiyang(fbMeasureWord.getSchedulerId(),compCode);*/
        return map;
    }


    @RequestMapping("/saveCreditReportWeb")
    @ResponseBody
    public Map<String,String> saveCreditReportWeb(String compCode,String measureId,String type,String schedulerId){
//        String isTest = systemConfigRepository.findByID("IS_TEST").getKeyvalue();
//        Map<String,String> map = new HashMap<String,String>();
        /*Map<String,String> params = new HashMap<String,String>();
        //1.获取到需要替换到word的值
        if("1".equals(isTest)){
            params.put("{key_1}","公司設立於民國 100年 12月");
            params.put("{key_2}","公司股東結構:自營/獨資");
            params.put("{key_3}","是否曾停業?:否");
            params.put("{key_4}","是否曾經變更負責人?:否");
            params.put("{key_5}","是否有轉投資/關係企業?:否");
            params.put("{key_6}","目前主要經營行業(產品)：111");
            params.put("{key_7}","近一年是否有變更主力產品或銷售模式?:否");
            params.put("{key_8}","是否有提供營業地租約或謄本?");
            params.put("{key_9}","登記負責人是否為實際負責人?");
            params.put("{key_10}","實際經營者婚姻狀況");
            params.put("{key_11}","實際經營者是否有子女");
            params.put("{key_12}","連帶保證人名字?");
            params.put("{key_13}","連帶保證人年次?");
            params.put("{key_14}","連帶保證人是否任職於公司?");
            params.put("{key_15}","連帶保證人與實際負責人關係?");
            params.put("{key_16}","法學網是否正常?");
            params.put("{key_17}","AML等級?");
            params.put("{key_18}","補充說明");

            params.put("{key2_1}","產銷流程");
            params.put("{key2_2}","主要銷售產品");
            params.put("{key2_3}","主要銷售客戶");
            params.put("{key2_4}","購料");
            params.put("{key2_5}","銷售");
            params.put("{key2_6}","近一年集團營收?");
            params.put("{key2_7}","近一年借戶/集團營收是否較去年營收成長或下滑達20%");
            params.put("{key2_8}","近三年借戶/集團企業營收合計?(仟元)");
            params.put("{key2_9}","公司內帳(或受訪者口述)平均月營收?(仟元)");
            params.put("{key2_10}","近3個月以存摺認定營收入帳平均金額?(仟元)");
            params.put("{key2_11}","公司內帳與近3個月以存摺認定營收入帳平均金額是否差異20%");
            params.put("{key2_12}","去年度財報銀行借款金額與聯徵借款年底餘額是否一致?");
            params.put("{key2_13}","近期是否有接獲或洽談中大額訂單？");

            params.put("{key3_1}","公司、負責人及其配偶目前金融負債餘額?(仟元)");
            params.put("{key3_2}","公司、負責人及其配偶去年同期之金融負債餘額?(仟元)");
            params.put("{key3_3}","公司、負責人及其配偶目前負債較去年同期增減數");
            params.put("{key3_4}","公司及關係企業是否有租賃借款");
            params.put("{key3_5}","公司及關係企業是否有其他借款");
            params.put("{key3_6}","公司營運是否達損益兩平?");
            params.put("{key3_7}","存款實績(仟元)");
            params.put("{key3_8}","存款活期或支存是否有大額整數金額");
            params.put("{key3_9}","是否有存貨");
            params.put("{key3_10}","是否在外有履保/保固金");
            params.put("{key3_11}","借款理由/用途");
            params.put("{key3_12}","補充說明?");

            params.put("{key4_1}","初估市價總值?");
            params.put("{key4_2}","說明總類/市值");
            params.put("{key4_3}","請說明，幾次");
            params.put("{key4_4}","銀行借款?");
            params.put("{key4_5}","信用卡循環金額");
            params.put("{key4_6}","預借現金金額??");
            params.put("{key4_7}","初估資產總市值?");

        }else{
            params = this.saveCreditReportService.getCreditReports(measureId,type);
            if(params.size() < 10){
                map.put("error","產製徵信報告失敗！");
                return map;
            }
        }
        //通过行业别不同，替换不同的模板类型
        if("07".equals(type) || "08".equals(type) || "09".equals(type) || "10".equals(type) || "11".equals(type) || "12".equals(type)){
            String wordTemplate = "FILE_CREDIT_REPORT_WORD_"+type;
            //2.替换word文档
            String wordUrl = systemConfigRepository.findByID(wordTemplate).getKeyvalue();
            String buffPath = systemConfigRepository.findByID("FILE_BUFF_PATH").getKeyvalue();
            String returnUrl  =  compCode+new Date().getTime()+".docx";
            String newWordUrl = WordUpdate.UpdateWord(wordUrl,buffPath+returnUrl,params);
            //3.将word文档转换为pdf
            String pdfUrl =compCode+new Date().getTime()+".pdf";
            String newPdfUrl = WordToPdf.wordToPdf(newWordUrl,buffPath+pdfUrl);
            map = this.saveCreditReportService.saveMongodbFile(newPdfUrl);
            map.put("schedulerId",schedulerId);
            map.put("compCode",compCode);
            //FileToRuiyang
//            this.saveCreditReportService.fileToRuiyang(schedulerId,compCode);
            return map;
        }else{
            map.put("error","產製徵信報告失敗,行業別錯誤！");
            return map;
        }*/
//        String buffPath= systemConfigRepository.findByID("FILE_BUFF_PATH").getKeyvalue();
//        String newPdfUrl = this.saveCreditReportService.findQuestionByMeasureId(measureId,type);
//        map = this.saveCreditReportService.saveMongodbFile(newPdfUrl);
//        map.put("schedulerId",schedulerId);
//        map.put("compCode",compCode);
        Map<String,String> map =  saveCreditReportService.saveCreditReportWeb(compCode,measureId,type,schedulerId);
        this.saveCreditReportService.fileToRuiyang(schedulerId,compCode);
        return map;
    }
    @RequestMapping("/saveTestCreditReportWeb")
    @ResponseBody
    public String saveTestCreditReportWeb(){
        this.saveCreditReportService.findQuestionByMeasureId("01f1bcb7-5767-44f0-bf4a-9a6dd231511c","04");
        return "1";
    }

}
