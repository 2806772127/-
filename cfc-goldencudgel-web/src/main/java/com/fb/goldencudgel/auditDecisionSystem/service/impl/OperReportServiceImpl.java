package com.fb.goldencudgel.auditDecisionSystem.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.comwave.core.data.domain.QueryPage;
import com.comwave.core.jpa.BaseJpaDAO;
import com.fb.goldencudgel.auditDecisionSystem.dao.FileContextDao;
import com.fb.goldencudgel.auditDecisionSystem.domain.LoanCompany.FbLoanCompany;
import com.fb.goldencudgel.auditDecisionSystem.domain.abilityCompareConfig.AbilityCompareConfig;
import com.fb.goldencudgel.auditDecisionSystem.domain.abilityConfig.AbilityConfig;
import com.fb.goldencudgel.auditDecisionSystem.domain.amountRecode.AmountRecode;
import com.fb.goldencudgel.auditDecisionSystem.domain.attachment.FbAttachment;
import com.fb.goldencudgel.auditDecisionSystem.domain.commons.SystemConfig;
import com.fb.goldencudgel.auditDecisionSystem.domain.incatorsConfig.IncatorsConfig;
import com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.FbMeasureWord;
import com.fb.goldencudgel.auditDecisionSystem.domain.ratingConfig.RatingConfig;
import com.fb.goldencudgel.auditDecisionSystem.domain.visit.FbVisitCompanyDetail;
import com.fb.goldencudgel.auditDecisionSystem.mapper.FileContext;
import com.fb.goldencudgel.auditDecisionSystem.repository.FbAttachmentResitory;
import com.fb.goldencudgel.auditDecisionSystem.utils.DateTimeUtils;

import sun.misc.BASE64Encoder;

/**
 * @ClassName OperReportServiceImpl
 * @Author panha
 * @Date 2019/5/14 10:04
 * @Version 1.0
 **/
@Service
@Transactional
public class OperReportServiceImpl extends BaseJpaDAO {

    private final Logger logger = LoggerFactory.getLogger(OperReportServiceImpl.class);

    @Autowired
    private FileContextDao fileContextDao;
    @Autowired
    private FbAttachmentResitory fbAttachmentResitory;
    @Autowired
    private Environment env;

    public Map<String,String> saveOperReport(String compCode,double allAmt){
        Map<String, String> params = new HashMap<>();
        try {
            //1.1通过企业统编查询企业评等
            FbMeasureWord fbMeasureWord = this.findFbMeasureWord(compCode);
            //1.2通过企业评等获取到企业描述
            RatingConfig ratingConfig = this.findRatingConfig(fbMeasureWord.getMeasureResult());
            if (ratingConfig != null) {
                params.put("{1}", ratingConfig.getRatingDescribe());
            }
            //2.通过企业统编查询企业公司成立年数，公司行业别
            FbLoanCompany fbLoanCompany = this.findFbLoanCompany(compCode);
            String year = "";
            if (fbLoanCompany != null) {
                int years = 0;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String dateString = sdf.format(fbLoanCompany.getComEstabdate());
                    years = DateTimeUtils.yearsBetweenNow(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //成立年数的区间
                year = this.getYear(years);
            }
            //3.通过企业统编查询app拜訪筆記-近一年營收，是否有不动产（COM_RECYEAR_REVENUE,HAS_HOURSES_FLAG ）
            FbVisitCompanyDetail fbVisitCompanyDetail = this.findFbVisitCompanyDetail(compCode);
            //4.通过测字id查询产品和额度（C_GRE_PAYMENT_AMT,C_REPAYMENT_AMT,CG_CYCLE_AMT,C_CYCLE_AMT）
            //AmountRecode amountRecode = this.findAmountRecode(fbMeasureWord.getMeasureId());
            //5.通过企业统编查询问卷答案
            Map<String, String> questions = this.findFbQuestionRecord(compCode);
            //获取到当前年份转换的民国年份
            int republicYear = getRepublicYear();
            //6.营销成长率中位数
            if (fbVisitCompanyDetail.getComRecyearRevenue() == null ||
                    questions.get(String.valueOf(republicYear - 2) + "年度報稅營收(仟元)") == null ||
                    questions.get(String.valueOf(republicYear - 1) + "年度報稅營收(仟元)") == null ||
                    questions.get("公司+負責人+負責人配偶近三個月存款績數(仟元)") == null ||
                    questions.get("授信戶目前在銀行週轉借款餘額(扣除存單質借、出口押匯、應收保證、長放、長擔、全額結匯)(仟元)") == null) {
                return params;
            }
            double marketing = (Double.parseDouble(fbVisitCompanyDetail.getComRecyearRevenue()) - Double.parseDouble(questions.get(String.valueOf(republicYear - 1) + "年度報稅營收(仟元)"))) / Double.parseDouble(questions.get(String.valueOf(republicYear - 1) + "年度報稅營收(仟元)"));
            //7.存积营收比中位数
            double deposited = Double.parseDouble(questions.get("公司+負責人+負責人配偶近三個月存款績數(仟元)")) / Double.parseDouble(fbVisitCompanyDetail.getComRecyearRevenue());
            //营8.授比中位数
            double battalionGranted = Double.parseDouble(questions.get("授信戶目前在銀行週轉借款餘額(扣除存單質借、出口押匯、應收保證、長放、長擔、全額結匯)(仟元)")) / Double.parseDouble(fbVisitCompanyDetail.getComRecyearRevenue());
            //9.获取到三种类型的比例
            //9.1判断fbLoanCompany是否存在行业别
            String comIndustry = "";
            if (fbLoanCompany.getComIndustry() == null) {
                comIndustry = fbVisitCompanyDetail.getComIndustry();
            } else {
                comIndustry = fbLoanCompany.getComIndustry();
            }
            List<IncatorsConfig> list = this.findIncatorsConfig(comIndustry, year);
            //10.对三种类型进行对比，然后等到三种类型的评级
            Map<String, String> powerConfig = this.getPowerConfig(list, marketing * 100, deposited * 100, battalionGranted * 100);
            //11.获取到三种类型的描述
            List<AbilityConfig> abilityConfigList = this.findAbilityConfig(powerConfig.get("03"), powerConfig.get("02"), powerConfig.get("01"), year);
            //12.經營能力类型评级和描述
            params.put("{2}", findAbliltyType("01", powerConfig.get("01")).get(0).getAbilityDescribe());
            params.put("{3}", abilityConfigList.get(0).getAbilityDescribe());
            //13.償債能力类型评级和描述
            params.put("{4}", findAbliltyType("02", powerConfig.get("02")).get(0).getAbilityDescribe());
            params.put("{5}", abilityConfigList.get(1).getAbilityDescribe());
            //14.財務能力类型评级和描述
            params.put("{6}", findAbliltyType("03", powerConfig.get("03")).get(0).getAbilityDescribe());
            params.put("{7}", abilityConfigList.get(2).getAbilityDescribe());
            //15.企业类型
            params.put("{8}", getEnterpriseType(fbMeasureWord.getMeasureResult()));
            //16.企业可贷金额
            String allAmtStr = NumberFormat.getNumberInstance().format(allAmt);
            params.put("{9}", allAmtStr);
            //17.是否拥有不动产
            params.put("{10}", getRealEstate(fbVisitCompanyDetail.getHasHoursesFlag()));
            //18.测字结果
            params.put("{11}", getEnterpriseType(fbMeasureWord.getMeasureResult()).substring(0, 1));
            params.put("{12}", fbLoanCompany.getComName());
        }catch(Exception e){
            logger.error(e.getMessage(),e);
        }
        return params;
    }

    //查询企业评等
    public FbMeasureWord findFbMeasureWord(String compCode){
        FbMeasureWord fbMeasureWord = new FbMeasureWord();
        StringBuffer sql = new StringBuffer();
        Map<String, Object> params = new HashMap<>();
        sql.append("from FbMeasureWord where IFNULL(prodCode,'P0001') = 'P0001'");
        if (!StringUtils.isBlank(compCode)) {
            sql.append(" AND compilationNo = :compCode");
            sql.append(" AND IFNULL(measureResult,'') != ''");
            sql.append(" ORDER BY measureDate DESC ");
            params.put("compCode", compCode);
        }
        QueryPage<FbMeasureWord> queryPage = findByJQL(sql,new QueryPage<>(1, 15), params);
        List<FbMeasureWord> list = queryPage.getContent();
        if(list.size() !=0){
            fbMeasureWord = list.get(0);
        }
        return fbMeasureWord;
    }
    //查询询企业公司成立年数，公司行业别
    public FbLoanCompany findFbLoanCompany(String compCode){
        FbLoanCompany fbLoanCompany = null;
        StringBuffer sql = new StringBuffer();
        Map<String, Object> params = new HashMap<>();
        sql.append("from FbLoanCompany where 1=1");
        if (!StringUtils.isBlank(compCode)) {
            sql.append(" AND compilationNo = :compCode");
            params.put("compCode", compCode);
        }
        sql.append(" ORDER BY createTime");
        QueryPage<FbLoanCompany> queryPage = findByJQL(sql, params);
        List<FbLoanCompany> list = queryPage.getContent();
        if(list.size()>0){
            fbLoanCompany = list.get(0);
        }
        return fbLoanCompany;
    }
    //查询app拜訪筆記-近一年營收，是否有不动产
    public FbVisitCompanyDetail findFbVisitCompanyDetail(String compCode){
        FbVisitCompanyDetail fbVisitCompanyDetail = null;
        StringBuffer sql = new StringBuffer();
        Map<String, Object> params = new HashMap<>();
        sql.append("from  FbVisitCompanyDetail where 1=1");
        if (!StringUtils.isBlank(compCode)) {
            sql.append(" AND compilationNo = :compCode");
            params.put("compCode", compCode);
        }
        sql.append(" ORDER BY createTime desc");
        QueryPage<FbVisitCompanyDetail> queryPage = findByJQL(sql, params);
        List<FbVisitCompanyDetail> list = queryPage.getContent();
        if(list.size()>0){
            fbVisitCompanyDetail = list.get(0);
        }
        return fbVisitCompanyDetail;
    }
    //查询产品及额度
    public AmountRecode findAmountRecode(String measurdId){
        AmountRecode amountRecode = null;
        StringBuffer sql = new StringBuffer();
        Map<String, Object> params = new HashMap<>();
        sql.append("from AmountRecode where 1=1");
        if (!StringUtils.isBlank(measurdId)) {
            sql.append(" AND measurdId = :measurdId");
            params.put("measurdId", measurdId);
        }
        sql.append(" ORDER BY createTime");
        QueryPage<AmountRecode> queryPage = findByJQL(sql, params);
        List<AmountRecode> list = queryPage.getContent();
        if(list.size()>0){
            amountRecode = list.get(0);
        }
        return amountRecode;
    }
    //查询问卷答案
    public Map<String,String> findFbQuestionRecord(String compCode){
        Map<String,String> map = new HashMap<String,String>();
        StringBuffer sql = new StringBuffer();
        Map<String, Object> params = new HashMap<>();
        int republicYear = getRepublicYear();
        sql.append("select fq.QUESTUIB_ANSWER,fq.QUESTION_NAME from FB_MEASURE_WORD fm");
        sql.append(" left join FB_QUESTIONRECORD fq on fm.MEASURE_ID = fq.MEASURE_ID");
        sql.append(" where fq.QUESTION_TYPE = '02'and fq.QUESTION_NAME in ('"+String.valueOf(republicYear-2)+"年度報稅營收(仟元)','"+String.valueOf(republicYear-1)+"年度報稅營收(仟元)','公司+負責人+負責人配偶近三個月存款績數(仟元)','授信戶目前在銀行週轉借款餘額(扣除存單質借、出口押匯、應收保證、長放、長擔、全額結匯)(仟元)')");
        if (!StringUtils.isBlank(compCode)) {
            sql.append(" AND fm.COMPILATION_NO = :compCode");
            params.put("compCode", compCode);
        }
        sql.append(" order by CREATE_TIME");
        QueryPage<Object[]> queryPage = findBySQL(sql, params);
        List<Object[]> objectList = queryPage.getContent();
        for(Object[] object :objectList){
            map.put(object[1].toString(),object[0].toString());
        }
        return map;
    }

    //通过评等等级查询评等描述
    public RatingConfig findRatingConfig(String rating){
        RatingConfig ratingConfig = null;
        StringBuffer sql = new StringBuffer();
        Map<String, Object> params = new HashMap<>();
        sql.append("from RatingConfig where 1=1");
        if (!StringUtils.isBlank(rating)) {
            sql.append(" AND rating = :rating");
            params.put("rating", rating);
        }
        QueryPage<RatingConfig> queryPage = findByJQL(sql, params);
        List<RatingConfig> list = queryPage.getContent();
        if(list.size()>0){
            ratingConfig = list.get(0);
        }
        return ratingConfig;
    }

    //获取公司指标信息
    public List<IncatorsConfig> findIncatorsConfig(String industryType, String conpanyYearType){
        IncatorsConfig incatorsConfig = null;
        StringBuffer sql = new StringBuffer();
        Map<String, Object> params = new HashMap<>();
        sql.append("from IncatorsConfig where 1=1");
        if (!StringUtils.isBlank(industryType)) {
            sql.append(" AND industryType = :industryType");
            params.put("industryType", industryType);
        }
        if (!StringUtils.isBlank(conpanyYearType)) {
            sql.append(" AND conpanyYearType = :conpanyYearType");
            params.put("conpanyYearType", conpanyYearType);
        }
        sql.append(" order by powerType");
        QueryPage<IncatorsConfig> queryPage = findByJQL(sql, params);
        List<IncatorsConfig> list = queryPage.getContent();
        return list;
    }

    //获取到财务能力、经营能力、偿债能力三个页签的描述
    public List<AbilityConfig> findAbilityConfig(String financialAblity, String debitAblity, String businseeAblity, String conpanyYearType){
        AbilityConfig abilityConfig = null;
        StringBuffer sql = new StringBuffer();
        Map<String, Object> params = new HashMap<>();
        sql.append("from AbilityConfig where 1=1");
        if (!StringUtils.isBlank(financialAblity)) {
            sql.append(" AND financialAbility = :financialAblity");
            params.put("financialAblity", financialAblity);
        }
        if (!StringUtils.isBlank(debitAblity)) {
            sql.append(" AND debitAbility = :debitAblity");
            params.put("debitAblity", debitAblity);
        }
        if (!StringUtils.isBlank(businseeAblity)) {
            sql.append(" AND businessAbility = :businseeAblity");
            params.put("businseeAblity", businseeAblity);
        }
        if (!StringUtils.isBlank(conpanyYearType)) {
            sql.append(" AND conpanyYearType = :conpanyYearType");
            params.put("conpanyYearType", conpanyYearType);
        }
        sql.append(" order by type");
        QueryPage<AbilityConfig> queryPage = findByJQL(sql, params);
        List<AbilityConfig> list = queryPage.getContent();
        return list;
    }


    //通过年限得到企业是属于哪个阶段
    private String getYear(int year){
        if(year <= 0){
            return "";
        }
        if(year < 6 ){
            return "01";
        }
        if(year < 11){
            return "02";
        }
        if(year >=11){
            return "03";
        }
        return "";
    }
    //通过企业能力评级获取到能力描述
    private String getPowerRate(String powerConfig){
        if("01".equals(powerConfig)){
            return "優於同業";
        }
        if("02".equals(powerConfig)){
            return "與同業相當";
        }
        if("03".equals(powerConfig)){
            return "劣於同業";
        }
        return "";
    }
    //通过企业评等判断企业类型
    private String getEnterpriseType(String MeasureResult){
        if("A".equals(MeasureResult)){
            return "展業家";
        }
        if("B".equals(MeasureResult)){
            return "實業家";
        }
        if("C".equals(MeasureResult)){
            return "勤業家";
        }
        if("D".equals(MeasureResult)){
            return "興業家";
        }
        if("E".equals(MeasureResult)){
            return "創業家";
        }
        if("F".equals(MeasureResult)){
            return "宏業家";
        }
        return "";
    }

    private String getProductLines(String cGrePaymentAmt,String cPaymentAmt,String cgCycleAmt,String cCycleAmt){
        StringBuffer sb = new StringBuffer();
        sb.append("可提供");
        if(cGrePaymentAmt != "0"){
            sb.append("信保還本"+cGrePaymentAmt+"仟");
        }
        if(cPaymentAmt != "0"){
            sb.append("信用還本"+cPaymentAmt+"仟");
        }
        if(cgCycleAmt != "0"){
            sb.append("信保循環"+cgCycleAmt+"仟");
        }
        if(cCycleAmt != "0"){
            sb.append("信用循環"+cCycleAmt+"仟");
        }
        sb.append("資金給貴公司");
        return sb.toString();
    }

    //判断不动产
    private String getRealEstate(String hasHourseFlag){
        if("02".equals(hasHourseFlag) ||  "03".equals(hasHourseFlag)){
            return "另老闆您還可以活用您的不動產，台北富邦銀行目前有推出超值優惠，";
        }
        return "";
    }
    //通过获取的企业能力和企业指标获取到企业等级
    private Map<String,String> getPowerConfig(List<IncatorsConfig> list ,double marketing,double deposited,double battalionGranted){
        Map<String,String> map = new HashMap<String,String>();
        //01经营能力评级
        String marketingConfig = "";
        String depositedConfig = "";
        String battalionGrantedConfig = "";
        if(list.size()>2){
            marketingConfig = getPowerCongifType("01",marketing,list.get(0).getRate());
            depositedConfig = getPowerCongifType("02",deposited,list.get(1).getRate());
            battalionGrantedConfig = getPowerCongifType("03",battalionGranted,list.get(2).getRate());
        }
        map.put("01",marketingConfig);
        map.put("02",depositedConfig);
        map.put("03",battalionGrantedConfig);
        return map;
    }
    //返回能力评级
    private String getPowerCongifType(String type,double power,double rate){
        double config = power -rate;
        //通过类型查询该企业能力的配置
        List<AbilityCompareConfig> abilityCompareConfigList = findAbliltyType(type,"");
        for(AbilityCompareConfig abilityCompareConfig:abilityCompareConfigList){
            //首先判断开始范围是否为0，如果是0，则代表是从无穷小开始,说明不需要有大于的判断条件，如果不是0，则是从该值开始进行判断
            double state=0;
            double end=0;
            boolean stateFlag = true;
            boolean endFlag = true;
            if(abilityCompareConfig.getStartRange() != null){
                state = abilityCompareConfig.getStartRange();
            }else{
                stateFlag = false;
            }
            if(abilityCompareConfig.getEndRange() != null){
                end = abilityCompareConfig.getEndRange();
            }else{
                endFlag = false;
            }
            //总共有以下这些情况
            //1.只有大于的条件
            if(stateFlag && !endFlag){
                if(config>state){
                    return abilityCompareConfig.getAbilityType();
                }
                //判断大于的条件是否包含等于(0代表否,1代表是)
                if(abilityCompareConfig.getStartIncludeFlag()=="1"){
                    if(config==state){
                        return abilityCompareConfig.getAbilityType();
                    }
                }
            }
            //2.只有小于的条件
            if(endFlag && !stateFlag){
                if(config<end){
                    return abilityCompareConfig.getAbilityType();
                }
                //判断小于的条件是否包含等于(0代表否,1代表是)
                if(abilityCompareConfig.getStartIncludeFlag()=="1"){
                    if(config==end){
                        return abilityCompareConfig.getAbilityType();
                    }
                }
            }
            //3.两个条件都有
            if(stateFlag && endFlag){
                if(config > state && config<end){
                    return abilityCompareConfig.getAbilityType();
                }
                //判断大于的条件是否包含等于(0代表否,1代表是)
                if(abilityCompareConfig.getStartIncludeFlag()=="1"){
                    if(config==state){
                        return abilityCompareConfig.getAbilityType();
                    }
                }
                //判断小于的条件是否包含等于(0代表否,1代表是)
                if(abilityCompareConfig.getStartIncludeFlag()=="1"){
                    if(config==end){
                        return abilityCompareConfig.getAbilityType();
                    }
                }
            }

        }
        return "";
    }

    //查询公司能力对比配置表 通过类型获取到能力对比配置列表
    public List<AbilityCompareConfig> findAbliltyType(String type,String abilityType){
        AbilityCompareConfig abilityCompareConfig = new AbilityCompareConfig();
        StringBuffer sql = new StringBuffer();
        Map<String, Object> params = new HashMap<>();
        sql.append("from AbilityCompareConfig where 1=1");
        if (!StringUtils.isBlank(type)) {
            sql.append(" AND type = :type");
            params.put("type", type);
        }
        if (!StringUtils.isBlank(abilityType)) {
            sql.append(" AND abilityType = :abilityType");
            params.put("abilityType", abilityType);
        }
        QueryPage<AbilityCompareConfig> queryPage = findByJQL(sql, params);
        List<AbilityCompareConfig> list = queryPage.getContent();
        return list;
    }
    //将文件保存到Mongodb
    public Map<String,String> saveMongodbFile(String imgUrl){
        logger.info("======>保存Mongodb開始，img url: " + imgUrl);
        String fileConfigPath = this.findSystemConfig("ATTACH_FILE_PATH");
        Map<String, String> params = new HashMap<String, String>();
        FileInputStream ips = null;
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        FileContext fileContext = new FileContext();
        fileContext.setFileId(uuid);
        String fileContextString ="data:image/jpeg;base64,"+image2byte(imgUrl).replaceAll("\r\n","");
        fileContextString = fileContextString.replaceAll("\n","");
        fileContext.setFileContext(fileContextString);
        fileContextDao.insertOne(fileContext);//儲存到Mongodb
        FbAttachment fafile = new FbAttachment();
        String filePath = fileConfigPath+ DateTimeUtils.formatDateToStr(new Date(), "yyyy/MM/dd") + "/cache/" + uuid + ".jpeg";
        fafile.setAttachId(uuid);
        fafile.setFileContextId(uuid);
        fafile.setFileName("企業經營報告書.jpeg");
        fafile.setFilePath(filePath);
        Date date = new Date();
        fafile.setCreateTime(date);
        fbAttachmentResitory.save(fafile);//儲存附件信息
        params.put("operReportUrl",fileConfigPath+uuid);
        params.put("operReportAttachId",uuid);
        logger.info("======>保存Mongodb結束");
        return params;
    }

    public  String image2byte(String path){
        logger.info("======>圖片轉字節開始");
        byte[] data = null;
        FileInputStream input = null;
        try {
            input = new FileInputStream(new File(path));
            data = new byte[input.available()];
            input.read(data);
            input.close();
        }
        catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        }
        catch (IOException ex1) {
            ex1.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        logger.info("======>圖片字節數：" + (data == null ? 0 : data.length));
        return encoder.encode(data);
    }

    //查询系统配置通过key查询value
    public String findSystemConfig(String keyname){
        SystemConfig systemConfig = new SystemConfig();
        StringBuffer sql = new StringBuffer();
        Map<String, Object> params = new HashMap<>();
        sql.append("from SystemConfig where 1=1");
        if (!StringUtils.isBlank(keyname)) {
            sql.append(" AND keyname = :keyname");
            params.put("keyname", keyname);
        }
        QueryPage<SystemConfig> queryPage = findByJQL(sql, params);
        List<SystemConfig> list = queryPage.getContent();
        if(list.size() !=0){
            systemConfig = list.get(0);
        }
        return systemConfig.getKeyvalue();
    }
    //获取民国年份
    public int getRepublicYear(){
        //获取到当前时间的年份
        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR);
        //获取到转换的民国年份
        int republicYear = year-1911;
        return republicYear;
    }

}
