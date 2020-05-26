package com.fb.goldencudgel.auditDecisionSystem.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fb.goldencudgel.auditDecisionSystem.repository.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.comwave.core.data.domain.QueryPage;
import com.comwave.core.jpa.BaseJpaDAO;
import com.fb.goldencudgel.auditDecisionSystem.dao.FileContextDao;
import com.fb.goldencudgel.auditDecisionSystem.domain.attachment.FbAttachment;
import com.fb.goldencudgel.auditDecisionSystem.domain.commons.SystemConfig;
import com.fb.goldencudgel.auditDecisionSystem.domain.creditReportingFile.FbCreditReportingFile;
import com.fb.goldencudgel.auditDecisionSystem.domain.visit.FbVisitCompanyDetail;
import com.fb.goldencudgel.auditDecisionSystem.mapper.FileContext;
import com.fb.goldencudgel.auditDecisionSystem.model.User;
import com.fb.goldencudgel.auditDecisionSystem.schema.compress.CompressFileRq;
import com.fb.goldencudgel.auditDecisionSystem.schema.compress.CompressFileRs;
import com.fb.goldencudgel.auditDecisionSystem.service.IInterfaceService;
import com.fb.goldencudgel.auditDecisionSystem.utils.DateTimeUtils;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;

@Service
@Transactional
public class CollectionServiceImpl extends BaseJpaDAO {

    private Logger logger = LoggerFactory.getLogger(CollectionServiceImpl.class);

    @Autowired
    private FbPunchCardRecodeRepository punchCardRecodeRepository;
    @Autowired
    private CreditReportFileResitory creditReportFileResitory;
    @Autowired
    private FileContextDao fileContextDao;
    @Autowired
    private SystemConfigRepository systemConfigRepository;
    @Autowired
    private FbAttachmentResitory fbAttachmentResitory;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private IInterfaceService interfaceService;
    
    @Autowired
    private FbVisitCompanyDetailRepository fbVisitCompanyDetailRepository;

    @Autowired
    private ApplyProcRepository applyProcRepository;

    /**
     * 条件查询拜访笔记
     * compilationNo 公司统编
     * visitName 客户名称
     * startDate 拜访时间
     * endDate 拜访时间
     *
     * **/
    public QueryPage<Object[]> findByConditions(String compilationNo,String visitName, String startDate,
                                                String endDate,QueryPage<Object[]> queryPage,String createUsers,String userGroups,String userAreas){
        //获取当前用户信息 M:處主管，A:區主管, C:組長，S:業務員 Z:征信員
         User user = UserUtil.getCurrUser();
         //根據user_code查詢user_area,user_group,user_type
        Map<String,Object> userparam = new HashMap<>();
        StringBuffer usersql = new StringBuffer("select USER_NAME,USER_AREA,USER_GROUP,USER_OFFICE,USER_TYPE from FB_USER WHERE 1=1 ");
        usersql.append(" and USER_CODE=:usercode");
        userparam.put("usercode",user.getUserCode());
        List<Object[]> userinfos = findBySQL(usersql, userparam).getContent();
        String userRole = "ERROR";
        String userGroup = null;
        String userArea = null;
        if( userinfos != null && userinfos.size()>0){
            userRole = userinfos.get(0)[4]+"";
            userGroup =  user.getUserGroup() == null ? (userinfos.get(0)[2]+"") :user.getUserGroup();
            userArea = user.getUserArea() == null ? (userinfos.get(0)[1]+"") : user.getUserArea();
        }
        Map<String,Object> params = new HashMap<>();
        StringBuffer sb = new StringBuffer();
        // 0-AR.COMPILATION_NO;1-AR.COMPILATION_NAME;2-DDI.ITEM_NAME;3-CD.TRAND_ID;4-AR.APPOINTMENT_DATE
        // 5-APPOINTMENT_TIME_H;6-APPOINTMENT_TIME_M
        // 7-CD.VISIT_ID;8-CD.APPLY_FLAG;9-FU.USER_NAME
        sb.append("SELECT AR.COMPILATION_NO,AR.COMPILATION_NAME,DDI.ITEM_NAME,CD.TRAND_ID,AR.APPOINTMENT_DATE," +
                " case FIND_IN_SET(AR.APPOINTMENT_TIME_H,'un') when 0 then AR.APPOINTMENT_TIME_H else '00' end as APPOINTMENT_TIME_H," +
                " case FIND_IN_SET(AR.APPOINTMENT_TIME_M,'ef') when 0 then AR.APPOINTMENT_TIME_M else '00' end as APPOINTMENT_TIME_M," +
                " CD.VISIT_ID,CD.APPLY_FLAG ,FU.USER_NAME FROM FB_APPOINTMENT_RECORD AR " +
                " LEFT JOIN FB_VISIT_COMPANY_DETAIL CD ON CD.COMPILATION_NO = AR.COMPILATION_NO AND CD.TRAND_ID = AR.TRAND_ID " +
                " LEFT JOIN VIEW_DATA_DICT_ITEM DDI ON CD.COM_INDUSTRY = DDI.ITEM_CODE AND DDI.DICT_ID='BUSINESS_TYPE'  ");
        if("Z".equals(userRole)|| "S".equals(userRole)){
            sb.append(" INNER JOIN FB_USER FU ON AR.CREATE_USER = FU.USER_CODE WHERE 1 = 1 and  AR.CREATE_USER=:createUser ");
            params.put("createUser",user.getUserCode());
        }else if("C".equals(userRole)){
            sb.append(" INNER JOIN FB_USER FU ON AR.CREATE_USER = FU.USER_CODE WHERE 1=1 AND  USER_GROUP =:userGroup ");
            params.put("userGroup",userGroup);
            if(StringUtils.isNotBlank(createUsers)){
                sb.append(" and AR.CREATE_USER=:createUsers");
                params.put("createUsers",createUsers);
            }
        }else if("A".equals(userRole)) {
            sb.append(" INNER JOIN FB_USER FU ON AR.CREATE_USER = FU.USER_CODE WHERE 1=1 AND  USER_AREA =:userArea ");
            params.put("userArea",userArea);

            if(StringUtils.isNotBlank(createUsers)){
                sb.append(" and AR.CREATE_USER=:createUsers");
                params.put("createUsers",createUsers);
            }
            if(StringUtils.isNotBlank(userGroups)){

                sb.append(" and USER_GROUP =:userGroups");
                params.put("userGroups",userGroups);
            }
        }else if("M".equals(userRole)){
          //  sb.append(" WHERE 1=1 ");
            sb.append(" INNER JOIN FB_USER FU ON AR.CREATE_USER = FU.USER_CODE WHERE 1=1");
            if(StringUtils.isNotBlank(createUsers)){
                sb.append(" and AR.CREATE_USER=:createUsers");
                params.put("createUsers",createUsers);
            }
            if(StringUtils.isNotBlank(userGroups)){
                sb.append(" and USER_GROUP =:userGroups");
                params.put("userGroups",userGroups);
            }
            if(StringUtils.isNotBlank(userAreas)){
                sb.append(" and USER_AREA =:userAreas");
                params.put("userAreas",userAreas);
            }
        }else {
            sb.append(" INNER JOIN FB_USER FU ON AR.CREATE_USER = FU.USER_CODE ");
            sb.append(" WHERE 1= 1 AND AR.CREATE_USER='error_input_create_user' ");
        }
        if(StringUtils.isNoneBlank(compilationNo)){
            sb.append(" and AR.COMPILATION_NO=:compilationNo ");
            params.put("compilationNo",compilationNo);
        }
        //授信戶名稱
        if (StringUtils.isNotBlank(visitName)){
            sb.append(" and AR.COMPILATION_NAME like :visitName");
            params.put("visitName","%"+visitName+"%");
        }
        if(StringUtils.isNotBlank(startDate)){
            sb.append(" and CONCAT(AR.APPOINTMENT_DATE,' ',(case FIND_IN_SET(AR.APPOINTMENT_TIME_H,'un') when 0 then AR.APPOINTMENT_TIME_H else '00' end),':', " +
                    "     (case FIND_IN_SET(AR.APPOINTMENT_TIME_M,'ef') when 0 then AR.APPOINTMENT_TIME_M else '00' end)) >= :startDate");
            params.put("startDate",startDate);
        }
        if(StringUtils.isNotBlank(endDate)){
            sb.append(" and CONCAT(AR.APPOINTMENT_DATE,' ',(case FIND_IN_SET(AR.APPOINTMENT_TIME_H,'un') when 0 then AR.APPOINTMENT_TIME_H else '00' end),':', " +
                    "     (case FIND_IN_SET(AR.APPOINTMENT_TIME_M,'ef') when 0 then AR.APPOINTMENT_TIME_M else '00' end)) <= :endDate");
            params.put("endDate",endDate);
        }
        sb.append(" and APPOINTMENT_TYPE = '1'");
        sb.append(" ORDER BY STR_TO_DATE(CONCAT(AR.APPOINTMENT_DATE,' ',(case FIND_IN_SET(AR.APPOINTMENT_TIME_H,'un') when 0 then AR.APPOINTMENT_TIME_H else '00' end),':', " +
                " (case FIND_IN_SET(AR.APPOINTMENT_TIME_M,'ef') when 0 then AR.APPOINTMENT_TIME_M else '00' end)),'%Y-%m-%d %H:%i') DESC" );
        return findBySQL(sb, queryPage, params);
    }
    /***
     * 查询拜访笔记页面所有信息
     *  compilationNo 公司统编
     * trandId 流水号
     * */
    public QueryPage<Object[]> findVCInfo(String compilationNo,String  trandId, String appointmentDate){
        Map<String,Object> params = new HashMap<>();
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT FAR.COMPILATION_NO  "); // 0统编
        sb.append(" ,FAR.COMPILATION_NAME   "); // 1公司名称
        sb.append(" ,DATE_FORMAT(LC.COM_ESTABDATE,'%Y-%m-%d') AS COM_ESTABDATE "); // 2公司创立日期
        sb.append(" ,VCD.COM_INDUSTRY "); // 3行业别
        sb.append(" ,VDDI.ITEM_NAME AS INDUSTRY_NAME  "); // 4行业别名称
        sb.append(" ,IFNULL(VCD.COM_RECYEAR_REVENUE,0.00) AS RECYEAR_REVENUE"); // 5近一年营收
        sb.append(" ,case VCD.INCUR_DEBTS_AMOUNTS WHEN NULL THEN 0 ELSE VCD.INCUR_DEBTS_AMOUNTS END AS INCUR_DEBTS_AMOUNTS "); // 6負債(NT$)
        sb.append(" ,VDDA.ITEM_NAME AS MARRIAGE_STATU_NAME  "); // 7婚姻狀態
        sb.append(" ,VCD.COM_STAFF_NUM "); // 8員工人數
        sb.append(" ,VDDB.ITEM_NAME AS HAS_HOURSES_FLAG_NAME    "); // 9有無不動產
        sb.append(" ,VDDC.ITEM_NAME  AS BUSINESS_PLACE_TYPE_NAME  "); // 10公司營業場所狀況
        sb.append(" ,case VCD.BANK_LOAN_BALANCE WHEN NULL THEN 0 ELSE VCD.BANK_LOAN_BALANCE END AS BANK_LOAN_BALANCE "); // 11 銀行週轉借款餘額
        sb.append(" ,VDDD.ITEM_NAME AS EDUCATION_NAME "); // 12負責人學歷
        sb.append(" ,CASE VCD.ASSURER_RELATION WHEN '1' THEN '有' WHEN '0' THEN '無' ELSE '' END AS ASSURER_RELATION_NAME "); // 13 有無關係戶
        sb.append(" ,VDDF.ITEM_NAME AS REGIETER_REAL_NAME  "); // 14公司登記負責人與實際負責人關係
        sb.append(" ,case VCD.THREE_MONTHS_AMOUNTS WHEN NULL THEN 0 ELSE VCD.THREE_MONTHS_AMOUNTS END AS  THREE_MONTHS_AMOUNTS "); // 15公司/負責人/負責人配偶近三個月存款績數
        sb.append(" ,VCD.APPLY_PRODUCT   "); // 16想要產品
        sb.append(" ,VDDG.ITEM_NAME AS RECYCLY_AMOUNT_NAME "); // 17收款方式
        sb.append(" ,VCD.VISIT_DESC "); // 18备注
        sb.append(" , ( CASE FAR.COM_CUSTOMER_TYPE WHEN '01' THEN '新戶' WHEN '02' THEN '舊戶' ELSE '无' END) AS COMPANY_TYPE_NAME "); // 19客戶屬性
        sb.append(" ,VDDH.ITEM_NAME AS ENTER_SOURCE_NAME  "); // 20案件來源
        sb.append(" ,VDDJ.ITEM_NAME AS COM_ORGANIZATION_NAME  "); // 21企業性質
        sb.append(" ,case LC.COM_ACTUAL_CAPITAL WHEN NULL THEN 0 ELSE LC.COM_ACTUAL_CAPITAL END AS COM_ACTUAL_CAPITAL "); // 22資本額（NT$）
        sb.append(" ,LC.COM_ADDRESS "); // 23公司登記地址
        sb.append(" ,FAR.BUSINESS_ADDRESS  "); // 24實際營業地址
        sb.append(" ,FAR.APPOINTMENT_NAME  "); // 25拜訪對象
        sb.append(" ,IF(FAR.APPOINTMENT_POSITION='99',FAR.APPOI_POSITION_OTHER,VDDK.ITEM_NAME) AS POSITION_NAME  "); // 26職稱
        sb.append(" ,CONCAT((CASE ISNULL(FAR.COM_PHONE_AREACODE) || LENGTH(trim(FAR.COM_PHONE_AREACODE)) < 1 WHEN TRUE THEN '' ELSE CONCAT('',FAR.COM_PHONE_AREACODE,'-') END) " +
                ",FAR.COM_PHONE_NUM,(CASE ISNULL(FAR.COM_PHONE_EXTEN) || LENGTH(trim(FAR.COM_PHONE_EXTEN)) < 1 WHEN TRUE THEN '' ELSE CONCAT('#', FAR.COM_PHONE_EXTEN) END)) AS CONNECTION_PHONG "); // 27公司聯絡電話
        sb.append(" , CONCAT((case FIND_IN_SET(FAR.APPOINTMENT_TIME_H,'un') when 0 then FAR.APPOINTMENT_TIME_H else '00' end),':'," +
                "(case FIND_IN_SET(FAR.APPOINTMENT_TIME_M,'ef') when 0 then FAR.APPOINTMENT_TIME_M else '00' end)) AS APPOINMENT_TIME"); // 28預定拜訪時間
        sb.append(" ,FAR.REMARK "); // 29備註
        sb.append(" ,VCD.PUNCH_CARD_RECODE_ID "); // 30打卡歷史
        sb.append(" ,VCD.VISIT_RESULT_CODE  "); // 31 拜訪結果
        sb.append(" ,CASE VCD.REJECT_CODE WHEN '99' THEN VCD.REJECT_REASON ELSE  VDDL.ITEM_NAME END AS REJECT_REASON "); // 32拒絕原因
        sb.append(" ,AR.APPOINTMENT_DATE AS NEXT_APPOINTMENT_DATE"); // 33預約下次拜訪日期
        sb.append(" ,VCD.APPLY_FLAG "); // 34 申請進件狀態 0 未進件 1 已進件
        sb.append(" ,VCD.CHECK_SUBMIT_STATU "); // 35拜訪提交狀態 01:未提交  02:已提交
        sb.append(" ,FAR.ENTER_SOURCE "); // 36 案件来源
        sb.append(" ,FAR.OTHER_INTRODUCE_ID "); //37
        sb.append(" ,FAR.OTHER_INTRODUCE_NAME "); //38案源轉介人姓名
        sb.append(" ,FAR.OTHER_INTRODUCE_ADDRESS "); //39案源轉介人單位
        sb.append(" ,FAR.OTHER_INTRODUCE_PHONE "); //40案源轉介人電話
        sb.append(" ,VCD.TRAND_ID "); //41流水号
        sb.append(" ,FAR.APPOINTMENT_DATE  ");//42 預約拜訪日期
        sb.append(" , CONCAT((case FIND_IN_SET(AR.APPOINTMENT_TIME_H,'un') when 0 then AR.APPOINTMENT_TIME_H else '00' end),':'," +
                "(case FIND_IN_SET(AR.APPOINTMENT_TIME_M,'ef') when 0 then AR.APPOINTMENT_TIME_M else '00' end)) AS NEXT_APPOINMENT_TIME"); // 43預約下次拜訪時間
        sb.append(" , FAR.CHARGE_PERSON,VCD.VISIT_ID "); // 44負責人
        sb.append(" , FAR.MOBILE "); // 46行动电话
        sb.append(" , FAR.EMAIL "); // 47邮箱
        sb.append(" FROM FB_VISIT_COMPANY_DETAIL VCD ");
        sb.append(" RIGHT JOIN FB_APPOINTMENT_RECORD FAR ON VCD.COMPILATION_NO = FAR.COMPILATION_NO AND FAR.TRAND_ID = VCD.TRAND_ID");
        sb.append(" LEFT JOIN FB_APPOINTMENT_RECORD AR ON VCD.NEXT_APPIONTMENT_ID = AR.APPOINTMENT_ID ");
        sb.append(" LEFT JOIN FB_LOAN_COMPANY LC ON FAR.COMPILATION_NO = LC.COMPILATION_NO");
        sb.append(" LEFT JOIN VIEW_DATA_DICT_ITEM VDDI ON VCD.COM_INDUSTRY = VDDI.ITEM_CODE AND VDDI.DICT_ID='BUSINESS_TYPE' ");
        sb.append(" LEFT JOIN VIEW_DATA_DICT_ITEM VDDA ON VCD.APPOINTMENT_MARY_STATUS = VDDA.ITEM_CODE AND VDDA.DICT_ID='MARRIAGE_STATU'        ");
        sb.append(" LEFT JOIN VIEW_DATA_DICT_ITEM VDDB ON VCD.HAS_HOURSES_FLAG  = VDDB.ITEM_CODE AND VDDB.DICT_ID='HAS_HOURSES_FLAG'      ");
        sb.append(" LEFT JOIN VIEW_DATA_DICT_ITEM VDDC ON VCD.BUSINESS_PLACE_TYPE = VDDC.ITEM_CODE AND VDDC.DICT_ID='BUSINESS_PLACE_TYPE'   ");
        sb.append(" LEFT JOIN VIEW_DATA_DICT_ITEM VDDD ON VCD.VISIT_EDU = VDDD.ITEM_CODE AND VDDD.DICT_ID='EDUCATION' ");
        sb.append(" LEFT JOIN VIEW_DATA_DICT_ITEM VDDE ON VCD.ASSURER_RELATION = VDDE.ITEM_CODE AND VDDE.DICT_ID='ASSURER_RELATION'      ");
        sb.append(" LEFT JOIN VIEW_DATA_DICT_ITEM VDDF ON VCD.REGIETER_REAL_RELATION = VDDF.ITEM_CODE AND VDDF.DICT_ID='REGIETER_REAL_RELATION'");
        sb.append(" LEFT JOIN VIEW_DATA_DICT_ITEM VDDG ON VCD.RECYCLY_AMOUNT_TYPE  = VDDG.ITEM_CODE AND VDDG.DICT_ID='RECYCLY_AMOUNT_TYPE'   ");
        sb.append(" LEFT JOIN VIEW_DATA_DICT_ITEM VDDH ON FAR.ENTER_SOURCE = VDDH.ITEM_CODE AND VDDH.DICT_ID='ENTER_SOURCE'  ");
        sb.append(" LEFT JOIN VIEW_DATA_DICT_ITEM VDDJ ON LC.COM_ORGANIZATION = VDDJ.ITEM_CODE AND VDDJ.DICT_ID='COM_ORGANIZATION'      ");
        sb.append(" LEFT JOIN VIEW_DATA_DICT_ITEM VDDK ON FAR.APPOINTMENT_POSITION = VDDK.ITEM_CODE AND VDDK.DICT_ID='POSITION'              ");
        sb.append(" LEFT JOIN VIEW_DATA_DICT_ITEM VDDL ON VDDL.DICT_ID='VISIT_REJECT_REASON' AND VCD.REJECT_CODE=VDDL.ITEM_CODE ");
        sb.append(" LEFT JOIN VIEW_CITY VC ON VC.CITY_CODE = LC.COM_REG_CITY_CODE ");
        sb.append(" LEFT JOIN VIEW_DISTRICT VD ON VD.DISTRICT_CODE = LC.COM_REG_DISTRICT_CODE ");
        sb.append(" LEFT JOIN VIEW_STREET VS ON VS.STREET_CODE  = LC.COM_REG_STREET_CODE WHERE 1=1  ");
        if(StringUtils.isNotBlank(compilationNo)){
            sb.append(" and FAR.COMPILATION_NO=:compilationNo");
            params.put("compilationNo",compilationNo);
        }
        if(StringUtils.isNotBlank(trandId)){
            sb.append(" and VCD.TRAND_ID=:trandId");
            params.put("trandId",trandId);
        }
        
        if (StringUtils.isNotBlank(appointmentDate)) {
            sb.append(" and FAR.APPOINTMENT_DATE = :appointmentDate");
            params.put("appointmentDate", appointmentDate);
        }
        sb.append(" AND FAR.APPOINTMENT_TYPE = '1'");
        sb.append(" ORDER BY VCD.CREATE_TIME DESC");
        return findBySQL(sb,params) ;
    }

    /***
     * 查询打卡历史
     * punchCardId 打卡id
     *
     * */
    public List<Object[]> findPunchRecodes(String compilationNo,String trandId, String appointmentType){
        StringBuffer sql = new StringBuffer();
        sql.append("select PCR.PUNCH_START_TIME");
        sql.append(", PCR.PUNCH_START_ADDRESS");
        sql.append(", PCR.PUNCH_START_ADDRESS_LONGITUDE");
        sql.append(", PCR.PUNCH_START_ADDRESS_LATITUDE");
        sql.append(", PCR.PUNCH_END_TIME");
        sql.append(", PCR.PUNCH_END_ADDRESS");
        sql.append(", PCR.PUNCH_END_ADDRESS_LONGITUDE");
        sql.append(", PCR.PUNCH_END_ADDRESS_LATITUDE");
        sql.append(" from FB_PUNCH_CARD_RECODE PCR ");
        sql.append(" INNER JOIN FB_APPOINTMENT_RECORD AR ON PCR.PUNCH_CARD_ID = AR.PUNCH_CARD_RECODE_ID AND AR.APPOINTMENT_TYPE = '" + appointmentType + "'");
        sql.append(" WHERE PCR.COMPILATION_NO = :compilationNo");
        sql.append(" AND PCR.TRAND_ID = :trandId");
        Map<String, Object> params = new HashMap<>();
        params.put("compilationNo", compilationNo);
        params.put("trandId", trandId);
        @SuppressWarnings("unchecked")
        QueryPage<Object[]> pushCardResult = findBySQL(sql, params);
        return pushCardResult.getContent();
    }

    //获取检查项下所有附件名称
    public  Boolean checkFileIdExsit(String id){
        Boolean flag = true;
        FbCreditReportingFile cfile = creditReportFileResitory.findByAttachId(id);
        if(null == cfile ){
            flag = false;
        }
        return flag ;
    }

     /**附件上传儲存到Mongodb*/
    public Map<String,Object> saveMongoFile(String[] fileStrs){
        Map<String,Object> map = new HashMap<>();
        String[] attchmentInfo = fileStrs[0].split("\\|");
        SystemConfig systemConfig = systemConfigRepository.findByID("ATTACH_FILE_PATH");
        if(attchmentInfo.length != 9){
        /*if(attchmentInfo.length!= 7){*/
            map.put("flag",false);
            map.put("msg","必填字段缺失，附件上傳失敗");
        }else{
            String uuid = attchmentInfo[0];
            String fileType = attchmentInfo[1];
            String fileFacode = attchmentInfo[2];
            String fileFaName = attchmentInfo[3];
            String fileChCode = attchmentInfo[4];
            String fileChName = attchmentInfo[5];
            String complicationNo = attchmentInfo[6];
            String file = fileStrs[1];
            String fileText=attchmentInfo[8];
            String attachurl =systemConfig == null?("fileService/"+uuid):(systemConfig.getKeyvalue()+uuid);
            String fileCachePath = "/app/cache/jgb/" + complicationNo + "/" + DateTimeUtils.formatDateToStr(new Date(), "yyyy/MM/dd") + "/cache/" + uuid + ".jpeg";
            String fileName="";
            if("2".equals(fileType)){
               /* fileName=fileChName+"_"+DateTimeUtils.getStringDate()+".jpeg";*/
                if(fileText.equals("pdf")){
                    fileName=fileChName+"_"+DateTimeUtils.formatDateToStr(new Date(), "yyyyMMdd")+".pdf";
                }else {
                    fileName = fileChName + "_" + DateTimeUtils.formatDateToStr(new Date(), "yyyyMMdd") + ".jpeg";
                }
            }else{
                fileName=fileChName;
            }
            try {
                Date date = new Date();
                FileContext fileContext = new FileContext();
                fileContext.setFileId(uuid);
                fileContext.setFileContext(file);
                fileContextDao.insertOne(fileContext);//儲存到Mongodb
                FbCreditReportingFile cfile = new FbCreditReportingFile();
                cfile.setNodeCode("1");
                cfile.setAttachId(uuid);
                cfile.setCreateTime(date);
                FbAttachment fafile = new FbAttachment();
                fafile.setAttachId(uuid);
                fafile.setFileContextId(uuid);
                fafile.setCompilationNo(complicationNo);
                fafile.setAttachTypeCode(fileFacode);
                fafile.setAttachTypeName(fileFaName);
                fafile.setAttactNameCode(fileChCode);
                fafile.setAttactName(fileChName);
                fafile.setFileCategory((double)Integer.parseInt(fileType));
                fafile.setCreateTime(date);
                fafile.setFileName(fileName);
                fafile.setFilePath(fileCachePath);
                fafile.setAttachUrl(attachurl);
                creditReportFileResitory.save(cfile);//儲存附件關聯關係
                fbAttachmentResitory.save(fafile);//儲存附件信息
                map.put("flag",true);
                map.put("msg","附件上傳成功");
                String respDate =DateTimeUtils.dateToString(date);
                map.put("respDate",respDate);
            } catch (Exception e) {
                map.put("flag",false);
                map.put("msg","附件上傳失敗");
            }
        }
        return map;
    }

    public String getAttachUrl(String fileContextId) {
        Map<String,Object> params =  new HashMap<>();
        StringBuffer sql = new StringBuffer("SELECT ATTACH_URL,FILE_CONTEXT_ID FROM FB_ATTACHMENT ");
        sql.append(" WHERE 1=1 ");
        sql.append(" AND FILE_CONTEXT_ID= :contextId");
        params.put("contextId", fileContextId);
        List<Object[]> result = findBySQL(sql,params).getContent();
        if(result == null ){
            return "";
        }else{
            String url = result.get(0)[0].toString();
            return url;
        }
    }

    public String changeAttachDeleteFlag(String fileContextId){
        StringBuilder jql = new StringBuilder("SELECT ATTACH_ID,COMPILATION_NO from FB_ATTACHMENT WHERE FILE_CONTEXT_ID=?");
        List<Object> jparams = new ArrayList<>();
        jparams.add(fileContextId);
        List<Object[]> attach = findBySQL(jql, jparams).getContent();
        if(attach != null && attach.size() > 0){
            String sql = "UPDATE FB_ATTACHMENT SET DELETE_FLAG=1 WHERE FILE_CONTEXT_ID=:contextId";
            Map<String, Object> params = new HashMap<>();
            params.put("contextId", fileContextId);
            executeSQL(sql, params);
            return "success";
        }else{
            return "fail";
        }
    }

    public Map<String,Object> saveAttachChange(String compilationNo,String compilationName,String trandId,
                                               String imgIds,String deleImgIds,String visitId,String applyProductList){
        Map<String,Object> map = new HashMap<>();
        try {
            User user = UserUtil.getCurrUser();
            String[] ids = imgIds.split(",");
            for (int i = 0; i < ids.length; i++) {
                if(null != ids[i]){
                    FbCreditReportingFile f = creditReportFileResitory.findByAttachId(ids[i]);
                    if (null != f) {
                        f.setCompilationNo(compilationNo);
                        f.setCompilationName(compilationName);
                        f.setTrandId(trandId);
                        f.setCreateUser(user.getUsername());
                        creditReportFileResitory.save(f);
                    }
                    FbAttachment fba = fbAttachmentResitory.findByFileContextId(ids[i]);
                    if (null != fba) {
                        fba.setCompilationNo(compilationNo);
                        fba.setUpdateTime(new Date());
                        fbAttachmentResitory.save(fba);
                        
                        // 调用bwce接口压缩图片
                        if ("2".equals(String.format("%.0f", fba.getFileCategory()))) {
                            try {
                                CompressFileRq compressFileRq = new CompressFileRq();
                                compressFileRq.setAttachId(fba.getAttachId());
                                CompressFileRs compressFileRs = interfaceService.compressFile(compressFileRq);
                                if (compressFileRs == null || StringUtils.isBlank(compressFileRs.getStatus()) || "F".equalsIgnoreCase(compressFileRs.getStatus())) {
                                    map.put("flag", false);
                                    map.put("msg", "圖片壓縮失敗");
                                }
                            } catch (Exception e) {
                                map.put("flag", false);
                                map.put("msg", "圖片壓縮失敗");
                            }
                        }
                    }
                }
            }
            String[] deIds = deleImgIds.split(",");
            for (int j = 0; j < deIds.length; j++) {
                if(deIds[j] !=null && !"".equals(deIds[j])){
                    deleteAttachment(deIds[j]);
                }
            }
            //把商品贷类型保存到FB_VISIT_COMPANY_DETAIL
            FbVisitCompanyDetail fbVisitCompanyDetail = fbVisitCompanyDetailRepository.findByVisitId(visitId);
            fbVisitCompanyDetail.setApplyProduct(applyProductList);
            fbVisitCompanyDetailRepository.save(fbVisitCompanyDetail);
            map.put("flag",true);
        }catch (Exception e){
            map.put("flag",false);
            logger.error("刪除mongodb附件信息失敗",e.getMessage());
        }
        return map;
    }

    private void deleteAttachment(String fileContextId){
        //刪除關聯表信息
        StringBuilder sql1 = new StringBuilder("DELETE FROM FB_CREDIT_REPORTING_FILE WHERE ATTACH_ID=:attachId");
        Map<String, Object> params1 = new HashMap<>();
        params1.put("attachId", fileContextId);
        executeSQL(sql1, params1);
        //刪除附件表信息
        String sql = "DELETE FROM FB_ATTACHMENT WHERE FILE_CONTEXT_ID=:contextId";
        Map<String, Object> params = new HashMap<>();
        params.put("contextId", fileContextId);
        executeSQL(sql, params);
        //刪除MongoDB數據
//        fileContextDao.deleteById(fileContextId);
    }

    //查询音频附件
    public List<Object[]> findVedioFiles(String compilationNo, String trandId) {
        Map<String,Object> params =  new HashMap<>();
        StringBuffer sql = new StringBuffer("SELECT A.ATTACH_NAME, DATE_FORMAT(A.CREATE_TIME,'%Y-%m-%d %H:%i:%S')  AS CREATE_TIME,A.FILE_CONTEXT_ID,A.ATTACH_TYPE_NAME,A.FILE_CATEGORY,A.FILE_NAME  FROM FB_ATTACHMENT A ");
        sql.append(" INNER JOIN  FB_CREDIT_REPORTING_FILE CRF ON A.FILE_CONTEXT_ID = CRF.ATTACH_ID ");
        sql.append(" INNER JOIN  FB_VISIT_COMPANY_DETAIL CR ON  CRF.COMPILATION_NO = CR.COMPILATION_NO AND CRF.TRAND_ID = CR.TRAND_ID ");
        sql.append(" WHERE A.FILE_CATEGORY='1' ");
        sql.append(" AND CR.COMPILATION_NO = :compilationNo");
        params.put("compilationNo", compilationNo);
        sql.append(" AND CR.TRAND_ID = :trandId");
        params.put("trandId", trandId);
        sql.append(" AND CRF.NODE_CODE = '1'");
        sql.append(" ORDER BY A.CREATE_TIME");
        List<Object[]> result = findBySQL(sql,params).getContent();
        if(result == null){
            result= new ArrayList<>();
        }
        return result;
    }

    //查询图片附件
    public List<Object[]> findImgFiles(String compilationNo, String trandId) {
        Map<String,Object> params =  new HashMap<>();
            StringBuffer sql = new StringBuffer("SELECT A.ATTACH_NAME_CODE, DATE_FORMAT(A.CREATE_TIME,'%Y-%m-%d %H:%i:%S') AS CREATE_TIME ,A.FILE_CONTEXT_ID," +
                "A.ATTACH_TYPE_CODE,A.FILE_CATEGORY,A.FILE_NAME ,A.ATTACH_TYPE_NAME,A.ATTACH_NAME  FROM FB_ATTACHMENT A ");
        sql.append(" INNER JOIN  FB_CREDIT_REPORTING_FILE CRF ON A.FILE_CONTEXT_ID = CRF.ATTACH_ID ");
        sql.append(" INNER JOIN  FB_VISIT_COMPANY_DETAIL CR ON  CRF.COMPILATION_NO = CR.COMPILATION_NO AND CRF.TRAND_ID = CR.TRAND_ID ");
        sql.append(" WHERE A.FILE_CATEGORY='2' AND LENGTH(A.ATTACH_TYPE_CODE)>=1");
        sql.append(" AND CR.COMPILATION_NO = :compilationNo");
        params.put("compilationNo", compilationNo);
        sql.append(" AND CR.TRAND_ID = :trandId");
        params.put("trandId", trandId);
        sql.append(" AND CRF.NODE_CODE = '1'");
        sql.append(" ORDER BY A.CREATE_TIME");
        List<Object[]> result = findBySQL(sql,params).getContent();
        if(result == null){
            result= new ArrayList<>();
        }
        return result;
    }

    public ArrayList<ArrayList<String>> getVisitRecordData() {
        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select ul.USER_AREA_NAME,ul.USER_GROUP_NAME,ul.USER_NAME,ul.USER_CODE,FAR.COMPILATION_NO,FAR.COMPILATION_NAME,DATE_FORMAT(LC.COM_ESTABDATE, '%Y/%m/%d') AS COM_ESTABDATE, ");
        sql.append(" CASE LC.COM_ACTUAL_CAPITAL WHEN NULL THEN 0 ELSE LC.COM_ACTUAL_CAPITAL END AS COM_ACTUAL_CAPITAL,VDDI.ITEM_NAME AS INDUSTRY_NAME,VDDC.ITEM_NAME AS BUSINESS_PLACE_TYPE_NAME, ");
        sql.append(" IFNULL(VCD.COM_RECYEAR_REVENUE, 0.00) AS RECYEAR_REVENUE,CASE VCD.INCUR_DEBTS_AMOUNTS WHEN NULL THEN 0 ELSE VCD.INCUR_DEBTS_AMOUNTS END AS INCUR_DEBTS_AMOUNTS, ");
        sql.append(" CASE VCD.ASSURER_RELATION  WHEN '1' THEN '有' WHEN '0' THEN '無' ELSE '' END AS ASSURER_RELATION_NAME,VDDA.ITEM_NAME AS MARRIAGE_STATU_NAME, ");
        sql.append(" VCD.COM_STAFF_NUM,VDDB.ITEM_NAME AS HAS_HOURSES_FLAG_NAME,VCD.APPLY_PROD_NAME,VCD.VISIT_DESC,DATE_FORMAT(VCD.SUBMIT_TIME,'%Y/%m/%d') AS SUBMIT_TIME ");
        sql.append(" FROM FB_VISIT_COMPANY_DETAIL VCD ");
        sql.append(" RIGHT JOIN FB_APPOINTMENT_RECORD FAR  ON VCD.COMPILATION_NO = FAR.COMPILATION_NO  AND FAR.TRAND_ID = VCD.TRAND_ID  ");
        sql.append(" LEFT JOIN FB_LOAN_COMPANY LC  ON FAR.COMPILATION_NO = LC.COMPILATION_NO  ");
        sql.append(" LEFT JOIN VIEW_DATA_DICT_ITEM VDDI ON VCD.COM_INDUSTRY = VDDI.ITEM_CODE  AND VDDI.DICT_ID = 'BUSINESS_TYPE'  ");
        sql.append(" LEFT JOIN VIEW_DATA_DICT_ITEM VDDA  ON VCD.APPOINTMENT_MARY_STATUS = VDDA.ITEM_CODE  AND VDDA.DICT_ID = 'MARRIAGE_STATU'  ");
        sql.append(" LEFT JOIN VIEW_DATA_DICT_ITEM VDDB  ON VCD.HAS_HOURSES_FLAG = VDDB.ITEM_CODE  AND VDDB.DICT_ID = 'HAS_HOURSES_FLAG' ");
        sql.append(" LEFT JOIN VIEW_DATA_DICT_ITEM VDDC  ON VCD.BUSINESS_PLACE_TYPE = VDDC.ITEM_CODE AND VDDC.DICT_ID = 'BUSINESS_PLACE_TYPE'  ");
        sql.append(" LEFT JOIN FB_USER ul ON FAR.CREATE_USER = ul.USER_CODE ");
        sql.append(" WHERE 1 = 1 AND FAR.APPOINTMENT_TYPE = '1' AND DATE_FORMAT(VCD.SUBMIT_TIME,'%Y-%m')=DATE_FORMAT(NOW(),'%Y-%m') ORDER BY VCD.CREATE_TIME DESC ");
        List<Object[]> content = findBySQL(sql).getContent();
        for(Object[] ob : content ) {
            ArrayList<String> rowData = new ArrayList<String>();
            for(int i=0 , len = 18 ; i<= len ;i++) {
                rowData.add(ob[i] == null ? "" : ob[i].toString());
            }
            data.add(rowData);
        }
        return data;
    }

}
