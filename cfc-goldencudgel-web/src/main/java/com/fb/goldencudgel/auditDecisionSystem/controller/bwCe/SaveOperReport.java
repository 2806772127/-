package com.fb.goldencudgel.auditDecisionSystem.controller.bwCe;

import com.fb.goldencudgel.auditDecisionSystem.repository.SystemConfigRepository;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.OperReportServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @ClassName SaveOperReport
 * @Author panha
 * @Date 2019/5/13 19:31
 * @Version 1.0
 **/

@Controller
@RequestMapping("/saveOperReport")
public class SaveOperReport {

    private final Logger logger = LoggerFactory.getLogger(SaveOperReport.class);

    @Autowired
    private OperReportServiceImpl operReportService;
//    @Autowired
//    private Environment env;

    @Autowired
    private SystemConfigRepository systemConfigRepository;

    @ResponseBody
    @RequestMapping("/saveOperReport")
    public Map<String,String> saveOperReport(String compCode,String cgrePaymentAmt,String cRepaymentAmt,String cgCycleAmt,String cCycleAmt) {
        logger.info("start saveOperReport===compCode:"+compCode+",cgrePaymentAmt:"+cgrePaymentAmt+",cRepaymentAmt:"+cRepaymentAmt+",cgCycleAmt:"+cgCycleAmt+",cCycleAmt:"+cCycleAmt);
        long startTime = System.currentTimeMillis();
        Map<String, String> params = new HashMap<String, String>();
        Map<String,String> map = new HashMap<String,String>();
        //1.通过传递的企业代码查询替换word的值
        String isTest = systemConfigRepository.findByID("IS_TEST").getKeyvalue();
        if("1".equals(isTest)){//测试模式
            logger.info("======>test mode");
            params.put("{1}", "前20%，可謂稱雄四方，業界翹楚");
            params.put("{2}", "優於同業");
            params.put("{3}", "雖處草創之期，營收表現耀眼，可謂明日之星。可適時提高財務槓桿，將如鯤鵬展翅，一飛衝天。");
            params.put("{4}", "與同業相當");
            params.put("{5}", "備足資金，蓄勢待發。可提高財務槓桿，再創事業高峰。未來業界之翹楚，指日可待");
            params.put("{6}", "優於同業");
            params.put("{7}", "適時操作財務槓桿，發揮最大效用，切莫過於保守。草創之期，營收成長，流動資金宜備足，台北富邦是您最佳的成長夥伴");
            params.put("{8}", "展業家");
            params.put("{9}", "信保還本5,000仟");
            params.put("{10}", "另老闆您還可以活用您的不動產，台北富邦銀行目前有推出超值優惠");
            params.put("{11}", "展");
            params.put("{12}", "輝龍鋁窗玻璃行");
        }else{
            //将传递的四个参数相加，得到金额总数
            double allAmt = 0;
            cgrePaymentAmt = cgrePaymentAmt==null?"0":cgrePaymentAmt;
            cRepaymentAmt = cRepaymentAmt==null?"0":cRepaymentAmt;
            cgCycleAmt = cgCycleAmt==null?"0":cgCycleAmt;
            cCycleAmt = cCycleAmt==null?"0":cCycleAmt;
            allAmt = Double.parseDouble(cgrePaymentAmt)+Double.parseDouble(cRepaymentAmt)+Double.parseDouble(cgCycleAmt)+Double.parseDouble(cCycleAmt);
            if(StringUtils.isBlank(compCode)){
                map.put("error","compCode為空，產製企業經營報告書失敗！");
                logger.error("compCode為空，產製企業經營報告書失敗！");
                return map;
            }
            params = this.operReportService.saveOperReport(compCode,allAmt);
            logger.info("======>params size: " + String.valueOf(params.size()));
            if(params.size() <12){
                map.put("error","產製企業經營報告書失敗！");
                logger.error("產製企業經營報告書失敗！");
                return map;
            }
        };

//        System.out.println("0===time:"+(System.currentTimeMillis()-startTime));
        try {
            //模板路径
            String wordPath = systemConfigRepository.findByID("FILE_OPER_REPORT_WORD").getKeyvalue();
            String imgPath = systemConfigRepository.findByID("FILE_OPER_REPORT_IMG").getKeyvalue();
            //文件生成路径
            String pdfPath = this.operReportService.findSystemConfig("FILE_BUFF_PATH");
//        System.out.println("1===time:"+(System.currentTimeMillis()-startTime));
            //2.调用word替换方法进行替换
            String wordName = compCode + new Date().getTime() + ".docx";
            String wordUrl = WordUpdate.UpdateWord(wordPath, pdfPath + wordName, params);
            logger.info("======>word url: " + wordUrl);
//        System.out.println("2===time:"+(System.currentTimeMillis()-startTime));
            //3.将word转换为pdf
            String pdfName = compCode + new Date().getTime() + ".pdf";
            String pdfUrl = WordToPdf.wordToPdf(wordUrl, pdfPath + pdfName);
            logger.info("======>pdf url: " + pdfUrl);
            String pafBackUrl = pdfPath + compCode + new Date().getTime() + ".pdf";
            logger.info("======>pdf back url: " + pafBackUrl);
//        System.out.println("3.1===time:"+(System.currentTimeMillis()-startTime));
            //获取合成图片的首尾图片路径
            String stateImg = systemConfigRepository.findByID("OPER_REPORT_FIRST").getKeyvalue();
            String endImg = systemConfigRepository.findByID("OPER_REPORT_END").getKeyvalue();
//        System.out.println("3.2===time:"+(System.currentTimeMillis()-startTime));
            String pdfBackImgUrl = SetBackgroundImage.setBackgroundImage(pdfUrl, imgPath, endImg, pafBackUrl);
            logger.info("======>pdf back img url: " + pdfBackImgUrl);
//        System.out.println("3===time:"+(System.currentTimeMillis()-startTime));
            //4.将pdf转换为图片,并将图片流儲存到mongodb中，返回id和图片路径
//        List<String> urlsList = new ArrayList<String>();
//        urlsList.add(stateImg);
//        urlsList.add(endImg);
            String imgUrl = PDF2IMAGE.pdf2Image(pdfBackImgUrl, pdfPath, ImageUtil.DEFAULT_DPI, stateImg, endImg, params);
            logger.info("======>img url: " + imgUrl);
//        System.out.println("4===time:"+(System.currentTimeMillis()-startTime));
            //5.将图片流儲存到mongdb中
            map = this.operReportService.saveMongodbFile(imgUrl);
//        System.out.println("5===time:"+(System.currentTimeMillis()-startTime));
            //删除temp文件
            FileUtil.deleteFile(wordUrl);
            FileUtil.deleteFile(pdfUrl);
            FileUtil.deleteFile(imgUrl);
            FileUtil.deleteFile(pafBackUrl);
            long endTime = System.currentTimeMillis();
            logger.info("saveOperReport time:" + (endTime - startTime));
        }catch(Exception e){
            map.put("error","產製企業經營報告書失敗！");
            logger.error("產製企業經營報告書失敗！"+e.getMessage(),e);
            return map;
        }
        return map;
    }
}
