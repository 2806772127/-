package com.fb.goldencudgel.auditDecisionSystem.utils;
import com.fb.goldencudgel.auditDecisionSystem.repository.LoanCompanyRepository;
import com.fb.goldencudgel.auditDecisionSystem.scheduler.BuildFileToFTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Controller

public class SchedulerTask {
    //CSV模板路径
    private static String apply_temp_file_path = "/app/cache/excel/";
    //CSV文件上傳目錄
    private static String apply_file_path = "/165U001COMM";
    //任务行程CSV模板名称
    private static String apply_temp_file_name = "SCHEDULER_RECORD_TEMP.csv";
    @Autowired
    LoanCompanyRepository loanCompanyRepository;
    private static final Logger logger = LoggerFactory.getLogger(SchedulerTask.class);

    @Autowired
    private FtpConnectionFactory ftpConnectionFactory;


//    @Scheduled(cron = "0 */10 * * * ?")
    public void SchedulerTask(){
        logger.info("任务行程上CSV文件传定时器开始");
        CsvFileUtil csvFileUtil = new CsvFileUtil();
        FileUtil fileUtil = new FileUtil();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String fileName = apply_temp_file_name.split("\\.")[0] + "_" + dateFormat.format(new Date()) + "." + apply_temp_file_name.split("\\.")[1];
      //ArrayList<ArrayList<String>> data = CsvFileUtil.readCSV("E:/SCHEDULER_RECORD_TEMP.csv",true);
        String[] headStr = {"區域","組","姓名","員編","角色名稱(業務人員/徵信員)","新戶/舊戶","授信戶統編","授信戶名稱","負責人","案件來源","案源轉介人姓名","案源轉介人員編","案源轉介人電話","案源轉介人單位","公司設立日期","組織形態","資本額(仟元)","公司登記地址","實際營業地址","拜訪對象","職稱","公司聯絡電話","預定拜訪日期","預定拜訪時間","備註","MA編號","提交成功","進件提交成功日期","行程提交成功日期"};
        ArrayList<Object[]> list =loanCompanyRepository.findByATT();
        list.add(0,headStr);
        ArrayList<ArrayList<String>> datas= new ArrayList<>();
        for(Object[] obj:list){
            ArrayList<String> arrayList=new ArrayList<String>();
            for(Object s:obj){
                if (s==null){
                    s="";
                }
                arrayList.add(s.toString());
            }
            datas.add(arrayList);
        }
        //上传附件
        fileUtil.makeDirectory(apply_temp_file_path);
        csvFileUtil.writeCSV(datas,apply_temp_file_path + fileName);
        ftpConnectionFactory.uploadFile(apply_temp_file_path + fileName ,apply_file_path,fileName);
        //删除中间文件
        fileUtil.deleteFile(apply_temp_file_path + fileName);
        logger.info("任务行程上CSV文件传定时器结束");
    }
}
