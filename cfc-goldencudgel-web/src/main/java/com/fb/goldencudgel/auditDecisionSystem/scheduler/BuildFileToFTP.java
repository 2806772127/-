package com.fb.goldencudgel.auditDecisionSystem.scheduler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fb.goldencudgel.auditDecisionSystem.service.impl.ApplyIncomServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.CollectionServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.MeasureWordServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.utils.CsvFileUtil;
import com.fb.goldencudgel.auditDecisionSystem.utils.FileUtil;
import com.fb.goldencudgel.auditDecisionSystem.utils.FtpConnectionFactory;

/**
 * hu
 * 定时生成附件上传到ftp
 */
@Component

public class BuildFileToFTP  {

    private static final Logger logger = LoggerFactory.getLogger(BuildFileToFTP.class);

    @Autowired
    private ApplyIncomServiceImpl applyIncomServiceImpl;

    @Autowired
    private FtpConnectionFactory ftpConnectionFactory;

    @Autowired
    private MeasureWordServiceImpl measureWordServiceImpl;
    
    @Autowired
    private CollectionServiceImpl collectionServiceImpl;


    //CSV文件上傳目錄
    private static String apply_file_path = "/165U001COMM";
    //CSV模板路径
    private static String apply_temp_file_path = "/app/cache/excel/";
    //進件申請CSV模板名称
    private static String apply_temp_file_name = "APPLY_RECORD.csv";
    //測字館-評等CSV模板名称
    private static String measure_level_temp_file_name = "MEASURE_WORD_RECORD_LEVEL.csv";
    //測字館-額度問卷CSV模板名称
    private static String measure_prod_temp_file_name = "MEASURE_WORD_RECORD_PROD.csv";
    //拜访笔记CSV模板名称
    private static String visitRecord_temp_file_name = "COMPANY_VISIT_RECORD_TEMP.csv";


    /**
     * 進件申請定时器,每天凌晨0点启动
     */
    @Scheduled(cron = "0 0 0 * * ?")
    private void BuildApplyFileToFTP(){
        logger.info("進件申請上CSV文件传定时器开始");
        CsvFileUtil csvFileUtil = new CsvFileUtil();
        FileUtil fileUtil = new FileUtil();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyyMM");
        String fileName = apply_temp_file_name.split("\\.")[0] + "_" + dateFormat.format(new Date()) + "." + apply_temp_file_name.split("\\.")[1];

        ArrayList<ArrayList<String>> writeData = new ArrayList<ArrayList<String>>();

        //ArrayList<ArrayList<String>> data = csvFileUtil.readCSV(apply_temp_file_path + measure_level_temp_file_name,true);
        //获取标题
        String[] headStr = {"區域","組","姓名","員編","MA編號","授信戶統編","授信戶名稱","進件日期","拍照上傳","提交成功"};
        ArrayList<String> headList=new ArrayList<String>();
        Collections.addAll(headList,headStr);
        writeData.add(headList);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        writeData.addAll(applyIncomServiceImpl.getApplyData(monthFormat.format(calendar.getTime())));
        //上传附件
        fileUtil.makeDirectory(apply_temp_file_path);
        csvFileUtil.writeCSV(writeData,apply_temp_file_path + fileName);
        ftpConnectionFactory.uploadFile(apply_temp_file_path + fileName ,apply_file_path,fileName);
        //删除中间文件
        fileUtil.deleteFile(apply_temp_file_path + fileName);
        logger.info("進件申請上CSV文件传定时器结束");
    }

    /**
     * 進件申請定时器(六月份数据),每天凌晨0点启动
     */
    @Scheduled(cron = "0 0 4 5 7 ?")
    private void BuildApplyFileToFTP6M(){
        logger.info("進件申請上6月份数据CSV文件传定时器开始");
        CsvFileUtil csvFileUtil = new CsvFileUtil();
        FileUtil fileUtil = new FileUtil();

        String fileName = apply_temp_file_name.split("\\.")[0] + "_" + 20190630 + "." + apply_temp_file_name.split("\\.")[1];

        ArrayList<ArrayList<String>> writeData = new ArrayList<ArrayList<String>>();

        //ArrayList<ArrayList<String>> data = csvFileUtil.readCSV(apply_temp_file_path + measure_level_temp_file_name,true);
        //获取标题
        String[] headStr = {"區域","組","姓名","員編","MA編號","授信戶統編","授信戶名稱","進件日期","拍照上傳","提交成功"};
        ArrayList<String> headList=new ArrayList<String>();
        Collections.addAll(headList,headStr);
        writeData.add(headList);
        writeData.addAll(applyIncomServiceImpl.getApplyData("201906"));
        //上传附件
        fileUtil.makeDirectory(apply_temp_file_path);
        csvFileUtil.writeCSV(writeData,apply_temp_file_path + fileName);
        ftpConnectionFactory.uploadFile(apply_temp_file_path + fileName ,apply_file_path,fileName);
        //删除中间文件
        fileUtil.deleteFile(apply_temp_file_path + fileName);
        logger.info("進件申請上6月份数据CSV文件传定时器结束");
    }

    /**
     * 測字館-評等定时器,每天凌晨0点启动
     */
//    @Scheduled(cron = "0 */5 * * * ?")
    private void BuildMeasureLevelFileToFTP(){
        logger.info("測字館-評等上CSV文件传定时器开始");
        CsvFileUtil csvFileUtil = new CsvFileUtil();
        FileUtil fileUtil = new FileUtil();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String fileName = measure_level_temp_file_name.split("\\.")[0] + "_" + dateFormat.format(new Date()) + "." + measure_level_temp_file_name.split("\\.")[1];

        ArrayList<ArrayList<String>> writeData = new ArrayList<ArrayList<String>>();

        //获取标题
        String[] headStr = {"區域","組","姓名","員編","授信戶統編","授信戶名稱","評等","額度金額","測字館產生評等時間"};
        ArrayList<String> headList=new ArrayList<String>();
        Collections.addAll(headList,headStr);
        //获取问卷题目
        List<String> questionList = measureWordServiceImpl.getQuestionName("03");
        questionList.addAll(measureWordServiceImpl.getQuestionName("02"));
        headList.addAll(questionList);
        writeData.add(headList);
        writeData.addAll(measureWordServiceImpl.getMeasureLevelData(questionList));
        //上传附件
        fileUtil.makeDirectory(apply_temp_file_path);
        csvFileUtil.writeCSV(writeData,apply_temp_file_path + fileName);
        ftpConnectionFactory.uploadFile(apply_temp_file_path + fileName ,apply_file_path,fileName);
        //删除中间文件
        fileUtil.deleteFile(apply_temp_file_path + fileName);
        logger.info("測字館-評等上CSV文件传定时器结束");
    }

    /**
     * 測字館-額度問卷定时器,每天凌晨0点启动
     */
//    @Scheduled(cron = "0 */5 * * * ?")
    private void BuildMeasureProdFileToFTP(){
        logger.info("測字館-額度問卷上CSV文件传定时器开始");
        CsvFileUtil csvFileUtil = new CsvFileUtil();
        FileUtil fileUtil = new FileUtil();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String fileName = measure_prod_temp_file_name.split("\\.")[0] + "_" + dateFormat.format(new Date()) + "." + measure_prod_temp_file_name.split("\\.")[1];

        ArrayList<ArrayList<String>> writeData = new ArrayList<ArrayList<String>>();

        //获取标题
        String[] headStr = {"區域","組","姓名","員編","授信戶統編","授信戶名稱","評等","額度金額","額度問卷提交時間"};
        ArrayList<String> headList=new ArrayList<String>();
        Collections.addAll(headList,headStr);
        //获取问卷题目
        List<String> questionList = measureWordServiceImpl.getQuestionName("03");
        questionList.addAll(measureWordServiceImpl.getQuestionName("02"));
        headList.addAll(questionList);
        writeData.add(headList);
        writeData.addAll(measureWordServiceImpl.getMeasureProdData(questionList));
        //上传附件
        fileUtil.makeDirectory(apply_temp_file_path);
        csvFileUtil.writeCSV(writeData,apply_temp_file_path + fileName);
        ftpConnectionFactory.uploadFile(apply_temp_file_path + fileName ,apply_file_path,fileName);
        //删除中间文件
        fileUtil.deleteFile(apply_temp_file_path + fileName);
        logger.info("測字館-額度問卷上CSV文件传定时器结束");
    }

    
    /**
     * 拜访笔记CSV文件上传定时器,每天凌晨0点启动
     */
//    @Scheduled(cron = "0 */1 * * * ?")
    private void BuildVisitRecordFileToFTP(){
        logger.info("拜访笔记CSV文件上传定时器开始");
        CsvFileUtil csvFileUtil = new CsvFileUtil();
        FileUtil fileUtil = new FileUtil();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String fileName = visitRecord_temp_file_name.split("\\.")[0] + "_" + dateFormat.format(new Date()) + "." + visitRecord_temp_file_name.split("\\.")[1];

        ArrayList<ArrayList<String>> writeData = new ArrayList<ArrayList<String>>();

        //ArrayList<ArrayList<String>> data = csvFileUtil.readCSV(apply_temp_file_path + visitRecord_temp_file_name,true);
        //获取标题
        //writeData.add(data.get(0));
        String[] headStr = {"區域","組","姓名","員編","授信戶統編","授信戶名稱","授信戶設立日期","實收資本額(仟元)","授信戶行業別","營業場所狀況","最近一年營收(仟元)","銀行週轉借款餘額(仟元)","授信戶有無關係戶","負責人婚姻狀況","授信戶員工人數","授信戶、負責人、配偶及其子女不動產持有情形有無不動產","希望申請產品類別","備註","拜訪筆記提交時間"};
        ArrayList<String> headList=new ArrayList<String>();
        Collections.addAll(headList,headStr);
        writeData.add(headList);
        writeData.addAll(collectionServiceImpl.getVisitRecordData());
        //上传附件
        fileUtil.makeDirectory(apply_temp_file_path);
        csvFileUtil.writeCSVTwo(writeData,apply_temp_file_path + fileName);
        ftpConnectionFactory.uploadFile(apply_temp_file_path + fileName ,apply_file_path,fileName);
        //删除中间文件
        fileUtil.deleteFile(apply_temp_file_path + fileName);
        logger.info("拜访笔记CSV文件上传定时器结束");
    }
}
