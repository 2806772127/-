package com.fb.goldencudgel.auditDecisionSystem.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.comwave.core.data.domain.QueryPage;
import com.comwave.core.jpa.BaseJpaDAO;
import com.fb.goldencudgel.auditDecisionSystem.dao.FileContextDao;
import com.fb.goldencudgel.auditDecisionSystem.domain.LoanCompany.FbLoanCompany;
import com.fb.goldencudgel.auditDecisionSystem.domain.attachment.FbAttachment;
import com.fb.goldencudgel.auditDecisionSystem.domain.commons.SystemConfig;
import com.fb.goldencudgel.auditDecisionSystem.domain.creditReportingFile.FbCreditReportingFile;
import com.fb.goldencudgel.auditDecisionSystem.mapper.FileContext;
import com.fb.goldencudgel.auditDecisionSystem.model.User;
import com.fb.goldencudgel.auditDecisionSystem.repository.ApplyIncomRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.ApplyProcRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.AppointmentRecordRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.CreditReportFileResitory;
import com.fb.goldencudgel.auditDecisionSystem.repository.FbAttachmentResitory;
import com.fb.goldencudgel.auditDecisionSystem.repository.LoanCompanyRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.SystemConfigRepository;
import com.fb.goldencudgel.auditDecisionSystem.schema.compress.CompressFileRq;
import com.fb.goldencudgel.auditDecisionSystem.schema.compress.CompressFileRs;
import com.fb.goldencudgel.auditDecisionSystem.schema.fileToRuiyang.FileToRuiyangRequest;
import com.fb.goldencudgel.auditDecisionSystem.schema.fileToRuiyang.FileToRuiyangResponse;
import com.fb.goldencudgel.auditDecisionSystem.service.IInterfaceService;
import com.fb.goldencudgel.auditDecisionSystem.utils.DateTimeUtils;
import com.fb.goldencudgel.auditDecisionSystem.utils.ObjectUtil;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;

@Service
@Transactional
public class ApplyIncomServiceImpl  extends  BaseJpaDAO{

    private Logger logger = LoggerFactory.getLogger(ApplyIncomServiceImpl.class);
    @Autowired
    private ApplyIncomRepository applyIncomRepository;
    @Autowired
    private SystemConfigRepository systemConfigRepository;
    @Autowired
    private FileContextDao fileContextDao;
    @Autowired
    private CreditReportFileResitory creditReportFileResitory;
    @Autowired
    private FbAttachmentResitory fbAttachmentResitory;
    @Autowired
    private IInterfaceService interfaceService;
    @Autowired
    private LoanCompanyRepository loanCompanyRepository;
    @Autowired
    private AppointmentRecordRepository appointmentRecordRepository;
    @Autowired
    private ApplyProcRepository applyProcRepository;

    public QueryPage<Object[]> findByConditions(String compilationNo, String companyName, String startDate, String endDate,
            QueryPage<Object[]> queryPage,String areaCode,String groupCode,String userCode) {
    
        Map<String,Object> params =  new HashMap<String,Object>();
        // 0-COMPILATION_NO; 1-TRAND_ID; 2-CUSTOMER_NAME; 3-ITEM_NAME; 4-CREATE_TIME;
        // 5-AI.COM_INDUSTRY_CODE; 6-fu.USER_NAME; 7-AI.APPLY_NUMBER
        StringBuffer sql = new StringBuffer("SELECT COMPILATION_NO,TRAND_ID,CUSTOMER_NAME,VDDI.ITEM_NAME AS ITEM_NAME, DATE_FORMAT(AI.APPLY_DATE, '%Y-%m-%d %H:%i:%s') as CREATE_TIME ");
        sql.append(", AI.COM_INDUSTRY_CODE,fu.USER_NAME,AI.APPLY_NUMBER FROM FB_APPLY_INCOM AI ");
        sql.append(" LEFT JOIN VIEW_DATA_DICT_ITEM VDDI ON AI.COM_INDUSTRY_CODE=VDDI.ITEM_CODE AND VDDI.DICT_ID='BUSINESS_TYPE' ");
        
        // 查选用户角色、所在组、所在区域信息
        User user = UserUtil.getCurrUser();
        Map<String, Object> userparam = new HashMap<>();
        StringBuffer usersql = new StringBuffer("select USER_NAME,USER_AREA,USER_GROUP,USER_OFFICE,USER_TYPE from FB_USER WHERE 1=1 ");
        usersql.append(" and USER_CODE=:usercode");
        userparam.put("usercode", user.getUserCode());
        List<Object[]> userinfos = findBySQL(usersql, userparam).getContent();
        String userRole = "ERROR";
        String userGroup = null;
        String userArea = null;
        if ( userinfos != null && userinfos.size() > 0) {
            userRole = userinfos.get(0)[4]+"";
            userGroup =  user.getUserGroup() == null ? (userinfos.get(0)[2] + "") : user.getUserGroup();
            userArea = user.getUserArea() == null ? (userinfos.get(0)[1] + "") : user.getUserArea();
        }
        
        // 数据过滤
     /*   if ("Z".equals(userRole) || "S".equals(userRole)) {
            sql.append(" where 1=1 and AI.CREATE_USER = :createUser ");
            params.put("createUser", user.getUserCode());
        } else if ("C".equals(userRole)) {
            sql.append(" INNER JOIN FB_USER fu ON AI.CREATE_USER = fu.USER_CODE WHERE 1=1 AND fu.USER_GROUP = :userGroup ");
            params.put("userGroup", userGroup);
        } else if ("A".equals(userRole)) {
            sql.append(" INNER JOIN FB_USER fu ON AI.CREATE_USER = fu.USER_CODE WHERE 1=1 AND  fu.USER_AREA = :userArea ");
            params.put("userArea", userArea);
        } else if ("M".equals(userRole)) {
            sql.append(" where 1=1 ");
        
        } else {
            sql.append(" where 1=1 AND AI.CREATE_USER = 'error_input_create_user' ");
        }*/
        
        if ("Z".equals(userRole) || "S".equals(userRole)) {
            sql.append(" INNER JOIN FB_USER fu ON AI.CREATE_USER = fu.USER_CODE where 1=1 and AI.CREATE_USER = :createUser ");
            params.put("createUser", user.getUserCode());
            
        } else if ("C".equals(userRole)) {
            sql.append(" INNER JOIN FB_USER fu ON AI.CREATE_USER = fu.USER_CODE WHERE 1=1 AND fu.USER_GROUP = :userGroup ");
            params.put("userGroup", userGroup);
            
            if(StringUtils.isNoneBlank(userCode)) {
                sql.append(" and fu.USER_CODE =:userCode");
                params.put("userCode", userCode);
            }
        } else if ("A".equals(userRole)) {
            sql.append(" INNER JOIN FB_USER fu ON AI.CREATE_USER = fu.USER_CODE WHERE 1=1 AND  fu.USER_AREA = :userArea ");
            params.put("userArea", userArea);
            if(StringUtils.isNoneBlank(groupCode)) {
                sql.append(" and fu.USER_GROUP =:groupCode");
                params.put("groupCode", groupCode);
            }
            if(StringUtils.isNoneBlank(userCode)) {
                sql.append(" and fu.USER_CODE =:userCode");
                params.put("userCode", userCode);
            }
        } else if ("M".equals(userRole)) {
            sql.append(" INNER JOIN FB_USER fu ON AI.CREATE_USER = fu.USER_CODE WHERE 1=1 ");
            if(StringUtils.isNoneBlank(areaCode)) {
                sql.append(" and fu.USER_AREA=:areaCode");
                params.put("areaCode", areaCode);
            }
            if(StringUtils.isNoneBlank(groupCode)) {
                sql.append(" and fu.USER_GROUP =:groupCode");
                params.put("groupCode", groupCode);
            }
            if(StringUtils.isNoneBlank(userCode)) {
                sql.append(" and fu.USER_CODE =:userCode");
                params.put("userCode", userCode);
            }
        } else {
            sql.append(" INNER JOIN FB_USER fu ON AI.CREATE_USER = fu.USER_CODE ");
            sql.append(" where 1=1 AND AI.CREATE_USER = 'error_input_create_user' ");
        }
        
        sql.append(" AND APPLY_STATUS = '1' ");
        if (StringUtils.isNoneBlank(compilationNo)){
            sql.append(" AND AI.COMPILATION_NO = :compilationNo");
            params.put("compilationNo", compilationNo);
        }
        if (StringUtils.isNoneBlank(companyName)){
            sql.append(" AND AI.CUSTOMER_NAME  like  :companyName");
            params.put("companyName","%" + companyName + "%");
        }
       /* if (null!=startDate ){*/
        if (StringUtils.isNoneBlank(startDate)){
            sql.append(" AND Date(AI.APPLY_DATE) >= :startDate");
            params.put("startDate",startDate);
        }
      /*  if (null != endDate ){*/
        if (StringUtils.isNoneBlank(endDate)){
            sql.append(" AND Date(AI.APPLY_DATE) <= :endDate");
            params.put("endDate",endDate);
        }
        
        
        
        
        sql.append(" ORDER BY AI.APPLY_DATE DESC ");
        queryPage = findBySQL(sql,queryPage,params);

        return  queryPage;
    }
    
    
    
    //查看详情获取行业名称，不是获取行业代码
    
    public QueryPage<Object[]> findIndustryNameByConditions(String compilationNo, String trandId,
            QueryPage<Object[]> queryPage) {
    
        Map<String,Object> params =  new HashMap<String,Object>();
        StringBuffer sql = new StringBuffer("SELECT COMPILATION_NO,TRAND_ID,CUSTOMER_NAME,VDDI.ITEM_NAME AS ITEM_NAME, DATE_FORMAT(CREATE_TIME, '%Y-%m-%d %H:%i:%s') as CREATE_TIME ");
        sql.append(", AI.COM_INDUSTRY_CODE FROM FB_APPLY_INCOM AI ");
        sql.append(" LEFT JOIN VIEW_DATA_DICT_ITEM VDDI ON AI.COM_INDUSTRY_CODE=VDDI.ITEM_CODE AND VDDI.DICT_ID='BUSINESS_TYPE' ");
        sql.append("  WHERE 1=1  ");
        sql.append(" AND APPLY_STATUS = '1' ");
        if (StringUtils.isNoneBlank(compilationNo)){
            sql.append(" AND AI.COMPILATION_NO = :compilationNo");
            params.put("compilationNo", compilationNo);
        }
        if (StringUtils.isNoneBlank(trandId)){
            sql.append(" AND AI.TRAND_ID  =  :trandId");
            params.put("trandId", trandId );
        }
      
        queryPage = findBySQL(sql,queryPage,params);

        return  queryPage;
    }

    /**附件上传儲存到Mongodb*/
    public Map<String,Object> saveMongoFile(String[] fileStrs){
        Map<String,Object> map = new HashMap<>();
        String[] attchmentInfo = fileStrs[0].split("\\|");
        SystemConfig systemConfig = systemConfigRepository.findByID("ATTACH_FILE_PATH");
        if(attchmentInfo.length != 9){
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
            String fileText=attchmentInfo[8];
//            String trandId = attchmentInfo[7];
            String file = fileStrs[1];
            String attachurl =systemConfig == null?("fileService/"+uuid):(systemConfig.getKeyvalue()+uuid);
            String fileCachePath = "/app/cache/jgb/" + complicationNo + "/" + DateTimeUtils.formatDateToStr(new Date(), "yyyy/MM/dd") + "/cache/" + uuid + ".jpeg";
            String fileName="";
            if("2".equals(fileType)){
               /* fileName=fileChName+"_"+DateTimeUtils.getStringDate()+".jpeg";*/
                if(fileText.equals("pdf")){
                    fileName=fileChName+"_"+DateTimeUtils.formatDateToStr(new Date(), "yyyyMMdd")+".pdf";
                }else{
                    fileName=fileChName+"_"+DateTimeUtils.formatDateToStr(new Date(), "yyyyMMdd")+".jpeg";
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
                cfile.setNodeCode("2");
                cfile.setAttachId(uuid);
             /*   cfile.setCompilationNo(complicationNo);
                cfile.setTrandId(trandId);*/
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

    /**附件类型名称*/
    public  List<Object[]> findVisitAttach(String compilationNo, String trandId){
        Map<String,Object> params = new HashMap<>();
        StringBuffer sb = new StringBuffer();
        //查询拜访笔记节点下所有检查项
        sb.append(" SELECT DISTINCT A.ATTACH_TYPE_CODE ,A.ATTACH_TYPE_NAME  FROM FB_ATTACHMENT A " +
                " INNER JOIN  FB_CREDIT_REPORTING_FILE CRF ON A.FILE_CONTEXT_ID = CRF.ATTACH_ID " +
                " INNER JOIN  FB_VISIT_COMPANY_DETAIL CR ON  CRF.COMPILATION_NO = CR.COMPILATION_NO AND CRF.TRAND_ID = CR.TRAND_ID " +
                "WHERE A.FILE_CATEGORY='2' AND LENGTH(A.ATTACH_TYPE_CODE)>=1");
        sb.append(" AND CR.COMPILATION_NO = :compilationNo");
        params.put("compilationNo", compilationNo);
        sb.append(" AND CR.TRAND_ID = :trandId");
        params.put("trandId", trandId);
        sb.append(" order by A.CREATE_TIME");
        return findBySQL(sb,params).getContent();
    }


    public Map<String,Object> saveAttachChange(String compilationNo,String trandId, String imgIds,String deleImgIds){
        Map<String,Object> map = new HashMap<>();
        try {
            User user = UserUtil.getCurrUser();
            FbLoanCompany fbLoanCompany =  loanCompanyRepository.findByComplicationNo(compilationNo);
            String[] ids = imgIds.split(",");
            for (int i = 0; i < ids.length; i++) {
                if(null != ids[i]){
                    FbCreditReportingFile f = creditReportFileResitory.findByAttachId(ids[i]);
                    if (null != f) {
                        f.setCompilationNo(compilationNo);
                        f.setCompilationName(fbLoanCompany.getComName());
                        f.setTrandId(trandId);
                        f.setCreateUser(user.getUsername());
                        creditReportFileResitory.saveAndFlush(f);
                    }
                    FbAttachment fba = fbAttachmentResitory.findByFileContextId(ids[i]);
                    if (null != fba) {
                        fba.setCompilationNo(compilationNo);
                        fba.setUpdateTime(new Date());
                        fbAttachmentResitory.saveAndFlush(fba);

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
            map.put("flag",true);
            map.put("flag",true);
        }catch (Exception e){
            map.put("flag",false);
            logger.error("刪除mongodb附件信息失敗："+e.getMessage());
        }
        return map;
    }

    public Map<String, Object> uploadAttach(String compilationNo,String trandId, String applyNumber) {
        Map<String,Object> result = new HashMap<String,Object>();
        try {
            //上传附件到瑞阳
            String appointmentId = getAppointmentId(compilationNo, applyNumber);
            FileToRuiyangRequest fileToRuiyangRq = new FileToRuiyangRequest();
            fileToRuiyangRq.setAppointmentid(appointmentId);
            fileToRuiyangRq.setCompilationno(compilationNo);
            fileToRuiyangRq.setZjcreditnum(applyNumber);
            FileToRuiyangResponse fileToRuiyangResponse = interfaceService.fileToRuiyang(fileToRuiyangRq);
            if (fileToRuiyangResponse != null && StringUtils.isNotBlank(fileToRuiyangResponse.getEMSGID())) {
                if ("0000".equals(fileToRuiyangResponse.getEMSGID())) {
                    result.put("flag", true);
                    result.put("msg", "附件同步到睿陽成功！");
                    logger.info("進件申請附件同步到睿陽成功！");
                } else {
                    result.put("flag", false);
                    result.put("msg","附件同步到睿陽失敗！" + fileToRuiyangResponse.getEMSGID() + "," + fileToRuiyangResponse.getEMSGTXT());
                    logger.error("進件申請附件同步到睿陽失敗！" + fileToRuiyangResponse.getEMSGID() + "," + fileToRuiyangResponse.getEMSGTXT());
                }
            } else {
                result.put("flag", false);
                result.put("msg", "附件同步到睿陽失敗！請求失敗！");
                logger.error("進件申請附件同步到睿陽失敗！請求失敗！");
            }
        } catch (Exception e) {
            result.put("flag", false);
            result.put("msg", "附件同步到睿陽失敗！");
            logger.error("進件申請附件同步到睿陽失敗！" + e.getMessage(), e);
        }
        
        return result;
    }

    /**
     * @description: 根据统编和案编从征信实访任务行程获取最新一笔行程ID
     * @author: mazongjian
     * @param compilationNo
     * @param applyNumber
     * @return  
     * @date 2019年11月26日
     */
    private String getAppointmentId(String compilationNo, String applyNumber) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append("    T.APPOINTMENT_ID ");
        sql.append(" FROM ");
        sql.append("    FB_APPOINTMENT_RECORD T ");
        sql.append(" WHERE ");
        sql.append("    T.COMPILATION_NO = :compilationNo ");
        sql.append("    AND T.ZJ_CREDIT_NUM = :applyNumber ");
        sql.append("    AND T.APPOINTMENT_TYPE = '2' ");
        sql.append("    AND T.CREATE_TIME = ( SELECT MAX( CREATE_TIME ) FROM FB_APPOINTMENT_RECORD I WHERE I.COMPILATION_NO = T.COMPILATION_NO AND I.ZJ_CREDIT_NUM = T.ZJ_CREDIT_NUM )");
        Map<String, Object> params = new HashMap<>();
        params.put("compilationNo", compilationNo);
        params.put("applyNumber", applyNumber);
        @SuppressWarnings("unchecked")
        QueryPage<Object[]> queryPage = findBySQL(sql,params);
        List<Object[]> queryList = queryPage.getContent();
        if (CollectionUtils.isEmpty(queryList)) {
            logger.info("統編: " + compilationNo + ", 案件編號: " + applyNumber + ", 未查到征信實訪任務行程ID");
            return "";
        }
        String appointmentId = ObjectUtil.obj2String(queryList.get(0));
        logger.info("統編: " + compilationNo + ", 案件編號: " + applyNumber + ", 查到征信實訪任務行程ID: " + appointmentId);
        return appointmentId;
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

    public ArrayList<ArrayList<String>> getApplyData(String queryDate) {
        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ul.USER_AREA_NAME,");
        sql.append("       ul.USER_GROUP_NAME,");
        sql.append("       ul.USER_NAME,");
        sql.append("       fai.CREATE_USER,");
        sql.append("       fai.APPLY_NUMBER,");
        sql.append("       fai.COMPILATION_NO,");
        sql.append("       fai.CUSTOMER_NAME,");
        sql.append("       date_format(fai.APPLY_DATE, '%Y/%m/%d'),");
        sql.append("       CASE");
        sql.append("         WHEN (SELECT COUNT(fa.ATTACH_ID)");
        sql.append("                 FROM FB_CREDIT_REPORTING_FILE crf");
        sql.append("                INNER JOIN FB_ATTACHMENT fa");
        sql.append("                   ON fa.ATTACH_ID = crf.ATTACH_ID");
        sql.append("                  AND fa.FILE_CATEGORY = '2'");
        sql.append("                WHERE crf.COMPILATION_NO = fai.COMPILATION_NO");
        sql.append("                  AND crf.TRAND_ID = fai.TRAND_ID");
        sql.append("                  AND crf.NODE_CODE = '2') > 0 THEN");
        sql.append("          'Y'");
        sql.append("         ELSE");
        sql.append("          'N'");
        sql.append("       END AS isUpload,");
        sql.append("       CASE");
        sql.append("         WHEN (SELECT COUNT(1)");
        sql.append("                 FROM FB_APPLY_INCOM t");
        sql.append("                INNER JOIN FB_VISIT_COMPANY_DETAIL t1");
        sql.append("                   on t.TRAND_ID = t1.TRAND_ID");
        sql.append("                WHERE t1.CHECK_SUBMIT_STATU = '1'");
        sql.append("                  AND t1.COMPILATION_NO = fai.COMPILATION_NO) >= 1 AND");
        sql.append("               IFNULL(fai.APPLY_NUMBER, '') !='' THEN");
        sql.append("          'Y'");
        sql.append("         ELSE");
        sql.append("          'N'");
        sql.append("       END AS submitFlag");
        sql.append("  FROM FB_APPLY_INCOM fai");
        sql.append("  LEFT JOIN FB_USER ul");
        sql.append("    ON fai.CREATE_USER = ul.USER_CODE");
        sql.append(" WHERE fai.APPLY_STATUS = '1'");
        sql.append("   AND date_format(fai.APPLY_DATE, '%Y%m') = '"+queryDate+"'");
        sql.append(" ORDER BY fai.APPLY_DATE DESC");

        List<Object[]> content = findBySQL(sql).getContent();
        for(Object[] ob : content ) {
            ArrayList<String> rowData = new ArrayList<String>();
            for(int i=0 , len = 9 ; i<= len ;i++) {
                rowData.add(ob[i] == null ? "" : ob[i].toString());
            }
            data.add(rowData);
        }
        return data;
    }
}
