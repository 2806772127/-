package com.fb.goldencudgel.auditDecisionSystem.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fb.goldencudgel.auditDecisionSystem.domain.appointMentRecord.FbAppointmentRecord;
import com.fb.goldencudgel.auditDecisionSystem.repository.*;
import com.fb.goldencudgel.auditDecisionSystem.schema.fileToRuiyang.FileToRuiyangRequest;
import org.apache.commons.lang3.StringUtils;
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
import com.fb.goldencudgel.auditDecisionSystem.schema.compress.CompressFileRq;
import com.fb.goldencudgel.auditDecisionSystem.schema.compress.CompressFileRs;
import com.fb.goldencudgel.auditDecisionSystem.service.IInterfaceService;
import com.fb.goldencudgel.auditDecisionSystem.utils.DateTimeUtils;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;

import net.spy.memcached.compat.log.Logger;
import net.spy.memcached.compat.log.LoggerFactory;

@Service
public class CreditReportImpl extends BaseJpaDAO {
    private Logger logger = LoggerFactory.getLogger(CreditReportImpl.class);
    @Autowired
    private IInterfaceService interfaceService;
    @Autowired
    private SystemConfigRepository systemConfigRepository;
    @Autowired
    private FileContextDao fileContextDao;
    @Autowired
    private LoanCompanyRepository loanCompanyRepository;
    @Autowired
    private CreditReportFileResitory creditReportFileResitory;
    @Autowired
    private FbAttachmentResitory fbAttachmentResitory;
    @Autowired
    private AppointmentRecordRepository appointmentRecordRepository;

    /* public QueryPage<Object[]> findByConditions(String compilationNo, String companyName, String startDate, String endDate,
                                                QueryPage<Object[]> queryPage) {*/
	 public QueryPage<Object[]> findByConditions(String compilationNo, String companyName, String startDate, String endDate,
             QueryPage<Object[]> queryPage,String areaCode,String groupCode,String userCode) {

         // 查选用户角色、所在组、所在区域信息
         User user = UserUtil.getCurrUser();
         Map<String, Object> userparam = new HashMap<>();
         StringBuffer usersql = new StringBuffer("select USER_NAME,USER_AREA,USER_GROUP,USER_OFFICE,USER_TYPE from FB_USER WHERE 1=1 ");
         usersql.append(" and USER_CODE=:usercode ");
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
         Map<String, Object> userparams = new HashMap<>();
         StringBuffer usersqls = new StringBuffer("select USER_CODE from FB_USER_AGENT WHERE AGENT_CODE=:usercode and ( now() BETWEEN AGENT_START_DATE and AGENT_END_DATA)");
         userparams.put("usercode", user.getUserCode());
         List<String > usercodes = findBySQL(usersqls, userparams).getContent();
         StringBuffer usercode = new StringBuffer("");
         if (usercodes.size()>0){
             usercode.append(",");
             for (int i = 0; i < usercodes.size(); i++) {

                 usercode.append("'"+usercodes.get(i)+"'");
                 if (i!=usercodes.size()-1){
                     usercode.append(",");
                 }else {

                 }
             }
         }

        Map<String,Object> params =  new HashMap<String,Object>();
        // 0-CR.REPORTING_ID; 1-CR.COMPILATION_NO; 2-AR.COMPILATION_NAME
        // 3-INDUSTRY_NAME; 4-AR.APPOINTMENT_DATE; 5-AR.APPOINTMENT_TIME_H; 6-AR.APPOINTMENT_TIME_M
        // 7-CR.TRAND_ID; 8-COM_INDUSTRY; 9-fu.USER_NAME; 10-STATUS; 11-LAST_FLAG
        StringBuffer sql = new StringBuffer("SELECT CR.REPORTING_ID,CR.COMPILATION_NO,AR.COMPILATION_NAME ");
        //sql.append(", IF(IFNULL(VDDI2.ITEM_NAME,'')='',IF(IFNULL(VDDI.ITEM_NAME, '' ) = '', VDDI1.ITEM_NAME, VDDI.ITEM_NAME),VDDI2.ITEM_NAME) AS INDUSTRY_NAME");
        sql.append(", (CASE WHEN IF(IFNULL(VDDI2.ITEM_NAME, '') = '',IF(IFNULL(VDDI.ITEM_NAME, '') = '',VDDI1.ITEM_NAME,VDDI.ITEM_NAME),VDDI2.ITEM_NAME)='零售業' THEN '零售&餐飲業' ELSE IF(IFNULL(VDDI2.ITEM_NAME, '') = '',IF(IFNULL(VDDI.ITEM_NAME, '') = '',VDDI1.ITEM_NAME,VDDI.ITEM_NAME),VDDI2.ITEM_NAME) END) INDUSTRY_NAME");//3
        sql.append(", AR.APPOINTMENT_DATE");
        sql.append(", AR.APPOINTMENT_TIME_H");
        sql.append(", AR.APPOINTMENT_TIME_M");
        sql.append(", CR.TRAND_ID ");
//        sql.append(", VCD.COM_INDUSTRY ");
        sql.append(", IF(IFNULL(CR.INDUSTRY_TYPE, '') = '',IF(IFNULL(VCD.COM_INDUSTRY, '') = '', LC.COM_INDUSTRY, VCD.COM_INDUSTRY),CR.INDUSTRY_TYPE) AS COM_INDUSTRY"); // 8
        sql.append(", fu.USER_NAME ");
         sql.append(", (CASE WHEN CR.CREATE_USER in   ('"+user.getUserCode()+"'"+ usercode+") THEN '1'  ELSE '0' END ) as STATUS "); // 10
        sql.append(", CASE WHEN IFNULL(M.REPORTING_ID, '') = '' THEN '0' ELSE '1' END AS LAST_FLAG "); // 11
         sql.append(" FROM FB_CREDIT_REPORTING CR ");
        sql.append(" LEFT JOIN FB_APPOINTMENT_RECORD AR ON CR.COMPILATION_NO = AR.COMPILATION_NO AND CR.TRAND_ID = AR.TRAND_ID AND AR.APPOINTMENT_TYPE = '2' ");
        sql.append(" LEFT JOIN (");
        sql.append(" SELECT VCDA.COMPILATION_NO, VCDA.COM_INDUSTRY FROM FB_VISIT_COMPANY_DETAIL VCDA INNER JOIN ");
        sql.append(" (SELECT COMPILATION_NO, MAX(CREATE_TIME) AS CREATE_TIME FROM FB_VISIT_COMPANY_DETAIL WHERE IFNULL( COM_INDUSTRY, '' ) != '' GROUP BY COMPILATION_NO) ");
        sql.append(" VCDB ON VCDA.COMPILATION_NO = VCDB.COMPILATION_NO AND VCDA.CREATE_TIME = VCDB.CREATE_TIME ");
        sql.append(" ) VCD ON AR.COMPILATION_NO = VCD.COMPILATION_NO ");
        sql.append(" LEFT JOIN VIEW_DATA_DICT_ITEM VDDI ON VCD.COM_INDUSTRY = VDDI.ITEM_CODE AND VDDI.DICT_ID = 'BUSINESS_TYPE' ");
        sql.append(" LEFT JOIN FB_LOAN_COMPANY LC ON CR.COMPILATION_NO = LC.COMPILATION_NO ");
        sql.append(" LEFT JOIN VIEW_DATA_DICT_ITEM VDDI1 ON LC.COM_INDUSTRY = VDDI1.ITEM_CODE AND VDDI1.DICT_ID = 'BUSINESS_TYPE' ");
        sql.append(" LEFT JOIN VIEW_DATA_DICT_ITEM VDDI2 ON CR.INDUSTRY_TYPE = VDDI2.ITEM_CODE AND VDDI2.DICT_ID = 'BUSINESS_TYPE' ");
        // 用于判断是否是最新一笔征信实访
        sql.append(" LEFT JOIN (" );
        sql.append(" SELECT I.REPORTING_ID, J.CREATE_TIME FROM FB_CREDIT_REPORTING I INNER JOIN FB_APPOINTMENT_RECORD J ON I.COMPILATION_NO = J.COMPILATION_NO AND I.TRAND_ID = J.TRAND_ID ");
        sql.append(" WHERE J.CREATE_TIME = ( SELECT MAX(L.CREATE_TIME) FROM FB_CREDIT_REPORTING K INNER JOIN FB_APPOINTMENT_RECORD L ON L.COMPILATION_NO = K.COMPILATION_NO AND L.TRAND_ID = K.TRAND_ID WHERE I.COMPILATION_NO = K.COMPILATION_NO ) ");
        sql.append(" ) M ON CR.REPORTING_ID = M.REPORTING_ID AND AR.CREATE_TIME = M.CREATE_TIME ");
        
        // 数据过滤
       /* if ("Z".equals(userRole) || "S".equals(userRole)) {
            sql.append(" where 1=1 and CR.CREATE_USER = :createUser ");
            params.put("createUser", user.getUserCode());
        } else if ("C".equals(userRole)) {
            sql.append(" INNER JOIN FB_USER fu ON CR.CREATE_USER = fu.USER_CODE WHERE 1=1 AND fu.USER_GROUP = :userGroup ");
            params.put("userGroup", userGroup);
        } else if ("A".equals(userRole)) {
            sql.append(" INNER JOIN FB_USER fu ON CR.CREATE_USER = fu.USER_CODE WHERE 1=1 AND  fu.USER_AREA = :userArea ");
            params.put("userArea", userArea);
        } else if ("M".equals(userRole)) {
            sql.append(" where 1=1 ");
        } else {
            sql.append(" where 1=1 AND CR.CREATE_USER = 'error_input_create_user' ");
        }*/
       /* if ("Z".equals(userRole) || "S".equals(userRole)) {
        	sql.append(" INNER JOIN FB_USER fu ON CR.CREATE_USER = fu.USER_CODE where 1=1 and CR.CREATE_USER = :createUser ");
            //sql.append(" where 1=1 and CR.CREATE_USER = :createUser ");
            params.put("createUser", user.getUserCode());
           
           
        } else if ("C".equals(userRole)) {
            sql.append(" INNER JOIN FB_USER fu ON CR.CREATE_USER = fu.USER_CODE WHERE 1=1 AND fu.USER_GROUP = :userGroup ");
            params.put("userGroup", userGroup);
            
            if(StringUtils.isNoneBlank(userCode)) {
                sql.append(" and fu.USER_CODE =:userCode");
                params.put("userCode", userCode);
            }
        } else if ("A".equals(userRole)) {
            sql.append(" INNER JOIN FB_USER fu ON CR.CREATE_USER = fu.USER_CODE WHERE 1=1 AND  fu.USER_AREA = :userArea ");
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
        	 sql.append(" INNER JOIN FB_USER fu ON CR.CREATE_USER = fu.USER_CODE WHERE 1=1 ");
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
        } else {sql.append(" where 1=1 AND CR.CREATE_USER = 'error_input_create_user' ");
        }*/


        sql.append(" INNER JOIN FB_USER fu ON CR.CREATE_USER = fu.USER_CODE WHERE 1=1 AND CR.CREDIT_STATU = '1' ");
        
        if (StringUtils.isNoneBlank(compilationNo)){
            sql.append(" AND CR.COMPILATION_NO = :compilationNo");
            params.put("compilationNo", compilationNo);
        }
        if (StringUtils.isNoneBlank(companyName)){
            sql.append(" AND AR.COMPILATION_NAME like :companyName ");
            params.put("companyName", "%" + companyName + "%");
        }
        if (StringUtils.isNoneBlank(startDate)){
            sql.append(" AND AR.APPOINTMENT_DATE >= :startDate ");
            params.put("startDate",startDate);
        }
        if (StringUtils.isNoneBlank(endDate)){
            sql.append(" AND AR.APPOINTMENT_DATE <= :endDate");
            params.put("endDate",endDate);
        }
        
    
        
        
        sql.append(" ORDER BY AR.APPOINTMENT_DATE desc, AR.APPOINTMENT_TIME_H desc, AR.APPOINTMENT_TIME_M desc");
        queryPage = findBySQL(sql,queryPage,params);

        return  queryPage;
    }
    
    public Object[] findByCompilationNoAndTrandId(String compilationNo, String trandId) {
        Map<String,Object> params =  new HashMap<String,Object>();
        StringBuffer sql = new StringBuffer("SELECT CR.REPORTING_ID");// 0
        sql.append(", CR.COMPILATION_NO"); // 1
        sql.append(", AR.COMPILATION_NAME"); // 2
        //sql.append(", IF(IFNULL(VDDI.ITEM_NAME, '' ) = '', VDDI1.ITEM_NAME, VDDI.ITEM_NAME) AS INDUSTRY_NAME"); // 3
        //sql.append(", IF(IFNULL(VDDI2.ITEM_NAME,'')='',IF(IFNULL(VDDI.ITEM_NAME, '' ) = '', VDDI1.ITEM_NAME, VDDI.ITEM_NAME),VDDI2.ITEM_NAME) AS INDUSTRY_NAME");//3
        sql.append(", (CASE WHEN IF(IFNULL(VDDI2.ITEM_NAME, '') = '',IF(IFNULL(VDDI.ITEM_NAME, '') = '',VDDI1.ITEM_NAME,VDDI.ITEM_NAME),VDDI2.ITEM_NAME)='零售業' THEN '零售&餐飲業' ELSE IF(IFNULL(VDDI2.ITEM_NAME, '') = '',IF(IFNULL(VDDI.ITEM_NAME, '') = '',VDDI1.ITEM_NAME,VDDI.ITEM_NAME),VDDI2.ITEM_NAME) END) INDUSTRY_NAME");//3
        sql.append(", AR.APPOINTMENT_DATE"); // 4
        sql.append(", AR.APPOINTMENT_TIME_H"); // 5
        sql.append(", AR.APPOINTMENT_TIME_M"); // 6
        sql.append(", CR.TRAND_ID "); // 7
        //sql.append(", IF(IFNULL(VCD.COM_INDUSTRY, '') = '', LC.COM_INDUSTRY, VCD.COM_INDUSTRY) AS COM_INDUSTRY"); // 8
        sql.append(", IF(IFNULL(CR.INDUSTRY_TYPE, '') = '',IF(IFNULL(VCD.COM_INDUSTRY, '') = '', LC.COM_INDUSTRY, VCD.COM_INDUSTRY),CR.INDUSTRY_TYPE) AS COM_INDUSTRY");//8
        sql.append(", AR.COM_CUSTOMER_TYPE "); // 9
        sql.append(" FROM FB_CREDIT_REPORTING CR ");
        sql.append(" LEFT JOIN FB_APPOINTMENT_RECORD AR ON CR.COMPILATION_NO = AR.COMPILATION_NO AND CR.TRAND_ID = AR.TRAND_ID AND AR.APPOINTMENT_TYPE = '2' ");
        sql.append(" LEFT JOIN (");
        sql.append(" SELECT VCDA.COMPILATION_NO, VCDA.COM_INDUSTRY FROM FB_VISIT_COMPANY_DETAIL VCDA INNER JOIN ");
        sql.append(" (SELECT COMPILATION_NO, MAX(CREATE_TIME) AS CREATE_TIME FROM FB_VISIT_COMPANY_DETAIL WHERE IFNULL( COM_INDUSTRY, '' ) != '' GROUP BY COMPILATION_NO) ");
        sql.append(" VCDB ON VCDA.COMPILATION_NO = VCDB.COMPILATION_NO AND VCDA.CREATE_TIME = VCDB.CREATE_TIME ");
        sql.append(" ) VCD ON AR.COMPILATION_NO = VCD.COMPILATION_NO ");
        sql.append(" LEFT JOIN VIEW_DATA_DICT_ITEM VDDI ON VCD.COM_INDUSTRY = VDDI.ITEM_CODE AND VDDI.DICT_ID = 'BUSINESS_TYPE' ");
        sql.append(" LEFT JOIN FB_LOAN_COMPANY LC ON CR.COMPILATION_NO = LC.COMPILATION_NO ");
        sql.append(" LEFT JOIN VIEW_DATA_DICT_ITEM VDDI1 ON LC.COM_INDUSTRY = VDDI1.ITEM_CODE AND VDDI1.DICT_ID = 'BUSINESS_TYPE' ");
        sql.append(" LEFT JOIN VIEW_DATA_DICT_ITEM VDDI2 ON CR.INDUSTRY_TYPE = VDDI2.ITEM_CODE AND VDDI2.DICT_ID = 'BUSINESS_TYPE' ");
        sql.append(" WHERE 1 = 1");
        sql.append(" AND CR.COMPILATION_NO = :compilationNo");
        params.put("compilationNo", compilationNo);
        sql.append(" AND CR.TRAND_ID = :trandId");
        params.put("trandId", trandId);
        @SuppressWarnings("unchecked")
        QueryPage<Object[]> result = findBySQL(sql, params);
        if (result == null || result.getContent() == null) {
            return new Object[] {};
        }
        return  result.getContent().get(0);
    }


    public  List<Object[]> findVedioFiles(String compilationNo, String trandId) {
        Map<String,Object> params =  new HashMap<>();
        StringBuffer sql = new StringBuffer("SELECT A.FILE_NAME,DATE_FORMAT(A.CREATE_TIME, '%Y-%c-%d %H:%i:%s') AS CREATE_TIME,A.ATTACH_ID,A.ATTACH_TYPE_NAME,A.FILE_CATEGORY  FROM FB_ATTACHMENT A ");
        sql.append(" INNER JOIN  FB_CREDIT_REPORTING_FILE CRF ON A.FILE_CONTEXT_ID = CRF.ATTACH_ID ");
        sql.append(" INNER JOIN  FB_CREDIT_REPORTING CR ON  CRF.COMPILATION_NO = CR.COMPILATION_NO AND CRF.TRAND_ID = CR.TRAND_ID ");
        sql.append(" WHERE A.FILE_CATEGORY='1' ");
        sql.append(" AND CR.COMPILATION_NO = :compilationNo");
        params.put("compilationNo", compilationNo);
        sql.append(" AND CR.TRAND_ID = :trandId");
        params.put("trandId", trandId);
        sql.append(" AND CRF.NODE_CODE = '3'");
        sql.append("ORDER BY A.CREATE_TIME ASC");
        List<Object[]> result = findBySQL(sql,params).getContent();
        if(result == null){
            result= new ArrayList<>();
        }
        return result;

    }

    public QueryPage<Object[]> findFilesByCode(String compilationNo, String trandId,String nodeCode, QueryPage<Object[]> queryPage) {
        Map<String,Object> params =  new HashMap<>();
        StringBuffer sql = new StringBuffer("SELECT A.FILE_NAME,DATE_FORMAT(A.CREATE_TIME, '%Y-%c-%d %H:%i:%s') AS CREATE_TIME,A.ATTACH_ID,IFNULL(A.ATTACH_TYPE_NAME,''),A.FILE_CATEGORY,concat('/',substring_index(A.ATTACH_URL, '/', -2)) FROM FB_ATTACHMENT A ");
        sql.append(" INNER JOIN  FB_CREDIT_REPORTING_FILE CRF ON A.FILE_CONTEXT_ID = CRF.ATTACH_ID ");
        sql.append(" WHERE 1=1 ");
        sql.append(" AND CRF.COMPILATION_NO = :compilationNo");
        params.put("compilationNo", compilationNo);
        if (StringUtils.isNoneBlank(trandId) && "2".equals(nodeCode)) {
            sql.append(" AND CRF.TRAND_ID = :trandId");
            params.put("trandId", trandId);
        }
        
        sql.append(" AND CRF.NODE_CODE = :nodeCode");
        params.put("nodeCode", nodeCode);
        sql.append(" ORDER BY A.CREATE_TIME ASC");
        return findBySQL(sql,queryPage,params);

    }

    public QueryPage<Object[]> findFilesByCodes(String compilationNo, String trandId,String nodeCode, QueryPage<Object[]> queryPage) {
        Map<String,Object> params =  new HashMap<>();
        StringBuffer sql = new StringBuffer("SELECT A.FILE_NAME,DATE_FORMAT(A.CREATE_TIME, '%Y-%c-%d %H:%i:%s') AS CREATE_TIME,A.ATTACH_ID,IFNULL(A.ATTACH_TYPE_NAME,''),A.FILE_CATEGORY ,A.ATTACH_TYPE_CODE,concat('/',substring_index(A.ATTACH_URL, '/', -2)) FROM FB_ATTACHMENT A ");
        sql.append(" INNER JOIN  FB_CREDIT_REPORTING_FILE CRF ON A.FILE_CONTEXT_ID = CRF.ATTACH_ID ");
        sql.append(" WHERE 1=1 ");
        sql.append(" AND CRF.COMPILATION_NO = :compilationNo");
        params.put("compilationNo", compilationNo);
        if (StringUtils.isNoneBlank(trandId) && "2".equals(nodeCode)) {
            sql.append(" AND CRF.TRAND_ID = :trandId");
            params.put("trandId", trandId);
        }
        
        sql.append(" AND CRF.NODE_CODE = :nodeCode");
        params.put("nodeCode", nodeCode);

        return findBySQL(sql,queryPage,params);

    }

    public List<Object[]> findImgFiles(String compilationNo, String trandId) {
        Map<String,Object> params =  new HashMap<>();
        StringBuffer sql = new StringBuffer("SELECT A.FILE_NAME,DATE_FORMAT(A.CREATE_TIME, '%Y-%c-%d %H:%i:%s') AS CREATE_TIME,A.ATTACH_ID,A.ATTACH_TYPE_NAME,A.FILE_CATEGORY  ,A.ATTACH_TYPE_CODE FROM FB_ATTACHMENT A ");
        sql.append(" INNER JOIN  FB_CREDIT_REPORTING_FILE CRF ON A.FILE_CONTEXT_ID = CRF.ATTACH_ID ");
        sql.append(" INNER JOIN  FB_CREDIT_REPORTING CR ON  CRF.COMPILATION_NO = CR.COMPILATION_NO AND CRF.TRAND_ID = CR.TRAND_ID ");
        sql.append(" WHERE A.FILE_CATEGORY='2' ");
        sql.append(" AND CR.COMPILATION_NO = :compilationNo");
        params.put("compilationNo", compilationNo);
        sql.append(" AND CR.TRAND_ID = :trandId");
        params.put("trandId", trandId);
        sql.append(" AND CRF.NODE_CODE = '3'");
        sql.append("ORDER BY A.CREATE_TIME ASC");
        List<Object[]> result = findBySQL(sql,params).getContent();
        if(result == null){
            result= new ArrayList<>();
        }
        return result;

    }
    
    //从首页进去的征信实访方法
    public QueryPage<Object[]> findDetailByNoAndTrandid(String compilationNo, String trandId, QueryPage<Object[]> queryPage) {
        Map<String,Object> params =  new HashMap<>();
        StringBuffer sql = new StringBuffer("SELECT  A.COMPILATION_NO, A.REPORTING_ID,A.TRAND_ID,A.VISITING_TIME,A.VISITING_USER,A.COMPILATION_NAME,A.CREATE_TIME,A.CREATE_USER,"
        		+ " A.INDUSTRY_TYPE,A.PUNCH_CARD_RECODE_ID,A.ENTER_SOURCE,A.COMPANY_ADDRESS,A.BUSINESS_ADDRESS as BUSINESS_ADDR,A.VISIT_DESC,A.VISIT_NAME,A.VISIT_POSITION,A.RESERVE_VISIT_TIME,"
        		+ " FAR.BUSINESS_ADDRESS, FAR.APPOINTMENT_NAME,FAR.APPOINTMENT_DATE,FAR.APPOINTMENT_TIME_H,FAR.APPOINTMENT_TIME_M,FAR.COM_PHONE_AREACODE,"
                + " FAR.COM_PHONE_NUM,FAR.COM_PHONE_EXTEN,FAR.APPOINTMENT_POSITION,FAR.APPOI_POSITION_OTHER,FAR.COMPILATION_NO as COMPILATION_NUM,FAR.COMPILATION_NAME as FAR_COMPILATION_NAME,"
                + " FAR.REMARK ");
        sql.append(", FAR.CHARGE_PERSON,MOBILE,EMAIL ");
        sql.append(" FROM FB_CREDIT_REPORTING A ");
        sql.append(" LEFT JOIN FB_APPOINTMENT_RECORD FAR ON FAR.COMPILATION_NO=A.COMPILATION_NO AND FAR.TRAND_ID=A.TRAND_ID AND FAR.APPOINTMENT_TYPE = '2' ");
        sql.append(" WHERE 1=1 ");
        sql.append(" AND A.COMPILATION_NO = :compilationNo");
        params.put("compilationNo", compilationNo);
        sql.append(" AND A.TRAND_ID = :trandId");
        params.put("trandId", trandId);
        return findBySQL(sql,queryPage,params);
    }
//viewfile
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

    /**附件配置类型*/
    public  List<Object[]> findVisitCheckItems(String comIndustryCode){
        Map<String,Object> params = new HashMap<>();
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT DISTINCT ATTACH_TYPE_CODE,ATTACH_TYPE_NAME FROM FB_ATTACHMENT_CONFIG WHERE 1=1");
        sb.append(" AND  NODE_CODE=:nodecode");
        sb.append(" AND  INDUSTRY_TYPE=:comIndustryCode");
        params.put("nodecode","3");
        params.put("comIndustryCode",comIndustryCode);
        sb.append(" ORDER BY ATTACH_TYPE_CODE");
        return findBySQL(sb,params).getContent();
    }
    /**附件类型名称*/
    public  List<Object[]> findVisitAttach(String compilationNo, String trandId){
        Map<String,Object> params = new HashMap<>();
        StringBuffer sb = new StringBuffer();
        //查询笔记节点下所有检查项
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

    /**附件上传儲存到Mongodb*/
    public Map<String,Object> saveMongoFile(String[] fileStrs){
        Map<String,Object> map = new HashMap<>();
        String[] attchmentInfo = fileStrs[0].split("\\|");
        SystemConfig systemConfig = systemConfigRepository.findByID("ATTACH_FILE_PATH");
        if(attchmentInfo.length!= 9){
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
            String trandId = attchmentInfo[7];
            String fileText = attchmentInfo[8];
            String file = fileStrs[1];
            String attachurl =systemConfig == null?("fileService/"+uuid):(systemConfig.getKeyvalue()+uuid);
            String fileCachePath = "/app/cache/jgb/" + complicationNo + "/" + DateTimeUtils.formatDateToStr(new Date(), "yyyy/MM/dd") + "/cache/" + uuid + ".jpeg";
            String fileName="";
            if("2".equals(fileType)){
                /*fileName=fileChName+"_"+DateTimeUtils.getStringDate()+".jpeg";*/
                if(fileText.equals("pdf")) {
                    fileName = fileChName + "_" + DateTimeUtils.formatDateToStr(new Date(), "yyyyMMdd") + ".pdf";
                }else {
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
                cfile.setNodeCode("3");
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
        @Transactional
    public Map<String,Object> saveAttachChange(String compilationNo,String trandId,
                                               String imgIds,String deleImgIds){
        Map<String,Object> map = new HashMap<>();
        try {
            User user = UserUtil.getCurrUser();
            String[] ids = imgIds.split(",");
            for (int i = 0; i < ids.length; i++) {
                if(null != ids[i]){
                    FbCreditReportingFile f = creditReportFileResitory.findByAttachId(ids[i]);
                    if (null != f) {
                        f.setCompilationNo(compilationNo);
                        FbLoanCompany fbLoanCompany=   loanCompanyRepository.findByComplicationNo(compilationNo);
                        f.setCompilationName(fbLoanCompany.getComName());
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
                  //  deleteAttachment(deIds[j]);
                    logger.info(deIds[j]);

                    creditReportFileResitory.deleteById(deIds[j]);
//                    //刪除附件表信息
                    String deId=deIds[j];
                  fbAttachmentResitory.deleteByFileId(deId);
              //FbAttachment fb=new FbAttachment();
               /*   fb.setFileContextId(deIds[j]);
                   fbAttachmentResitory.delete(fb);*/
            }
            }
            //上传附件到瑞阳
//            FbAppointmentRecord fbAppointmentRecord = appointmentRecordRepository.findByIds(compilationNo,trandId,"2");
//            FileToRuiyangRequest fileToRuiyangRq = new FileToRuiyangRequest();
//            fileToRuiyangRq.setAppointmentid(fbAppointmentRecord.getAppointmentId());
//            fileToRuiyangRq.setCompilationno(compilationNo);
//            fileToRuiyangRq.setZjcreditnum(fbAppointmentRecord.getZjCreditNum());
//            interfaceService.fileToRuiyang(fileToRuiyangRq);
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
}
