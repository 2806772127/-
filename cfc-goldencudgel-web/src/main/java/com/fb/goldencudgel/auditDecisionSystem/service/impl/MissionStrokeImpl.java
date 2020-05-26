package com.fb.goldencudgel.auditDecisionSystem.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;

import com.comwave.core.data.domain.QueryPage;
import com.comwave.core.jpa.BaseJpaDAO;
import com.fb.goldencudgel.auditDecisionSystem.domain.LoanCompany.FbLoanCompany;
import com.fb.goldencudgel.auditDecisionSystem.domain.applyProc.ApplyProc;
import com.fb.goldencudgel.auditDecisionSystem.domain.appointMentRecord.FbAppointmentRecord;
import com.fb.goldencudgel.auditDecisionSystem.domain.report.TaskComplishStatistics;
import com.fb.goldencudgel.auditDecisionSystem.model.AjaxResut;
import com.fb.goldencudgel.auditDecisionSystem.model.MissionStrokeModel;
import com.fb.goldencudgel.auditDecisionSystem.model.User;
import com.fb.goldencudgel.auditDecisionSystem.repository.ApplyProcRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.AppointmentRecordRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.FbUserRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.LoanCompanyRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.TaskComplishStatisticsRepository;
import com.fb.goldencudgel.auditDecisionSystem.service.IMissionStroke;
import com.fb.goldencudgel.auditDecisionSystem.utils.CommonUtils;
import com.fb.goldencudgel.auditDecisionSystem.utils.ObjectUtil;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;

/**
 *
 */
@Service
@Transactional
public class MissionStrokeImpl extends BaseJpaDAO implements IMissionStroke {
    
    private static final Logger logger = LoggerFactory.getLogger(MissionStrokeImpl.class);
    
    @Autowired
    private AppointmentRecordRepository appointmentRecordRepository;

    @Autowired
    private LoanCompanyRepository loanCompanyRepository;

    @Autowired
    private TaskComplishStatisticsRepository taskComplishStatisticsRepository;

    @Autowired
    private FbUserRepository fbUserRepository;

    @Autowired
    private ApplyProcRepository applyProcRepository;

    @Override
    public Map<String, String> getAreaList(String userCode) {
        Map<String, String> result = new LinkedHashMap<String, String>();
        StringBuffer sql = new StringBuffer();
        sql.append("select USER_AREA, USER_AREA_NAME ");
        sql.append(" from ORG_LIST OL");
        sql.append(" INNER JOIN VIEW_DATA_DICT_ITEM VDDI ON OL.USER_AREA = VDDI.ITEM_CODE AND VDDI.DICT_ID = 'ARRAY_SCHEMA_SORT_AREA' ");
        sql.append(" where 1 = 1 ");
        sql.append(" and IFNULL(OL.USER_AREA, '') != '' ");
        sql.append(" and IFNULL(OL.USER_AREA, '') not in ('188J', '188K', '188L') ");
        if (StringUtils.isNoneBlank(userCode)) {
            sql.append(" and OL.USER_CODE = '" + userCode + "'");
        }
        sql.append(" group by OL.USER_AREA, OL.USER_AREA_NAME order by VDDI.ITEM_REMARK");
        @SuppressWarnings("unchecked")
        List<Object[]> context = findBySQL(sql).getContent();
        for(Object[] ob : context) {
            String areaCode = ob[0] == null ? "" : ob[0].toString();
            String areaName = ob[1] == null ? "" : ob[1].toString();
            result.put(areaCode,areaName);
        }
        return result;
    }

    @Override
    public Map<String, String> getGroupList(String areaCode) {
        Map<String, String> result = new LinkedHashMap<>();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT OL.USER_GROUP, OL.USER_GROUP_NAME ");
        sql.append(" FROM ORG_LIST OL ");
        sql.append(" INNER JOIN VIEW_DATA_DICT_ITEM VDDI1 ON OL.USER_AREA = VDDI1.ITEM_CODE AND VDDI1.DICT_ID = 'ARRAY_SCHEMA_SORT_AREA' ");
        sql.append(" INNER JOIN VIEW_DATA_DICT_ITEM VDDI2 ON OL.USER_GROUP = VDDI2.ITEM_CODE AND VDDI1.ITEM_CODE = VDDI2.DICT_ID ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND IFNULL(OL.USER_GROUP, '') != '' ");
        sql.append(" AND IFNULL(OL.USER_AREA, '') not in ('188J', '188K', '188L') ");
        if (StringUtils.isNoneBlank(areaCode)) {
          sql.append(" and OL.USER_AREA = '" + areaCode + "'");
        }
        sql.append(" GROUP BY OL.USER_GROUP, OL.USER_GROUP_NAME ");
        sql.append(" ORDER BY VDDI1.ITEM_REMARK, VDDI2.ITEM_REMARK");
        @SuppressWarnings("unchecked")
        List<Object[]> context = findBySQL(sql).getContent();
        for(Object[] ob : context) {
            String groupCode = ob[0] == null ? "" : ob[0].toString();
            String groupName = ob[1] == null ? "" : ob[1].toString();
            result.put(groupCode,groupName);
        }
        return result;
    }

    @Override
    public Map<String, String> getUserList(String areaCode, String groupCode) {
        Map<String, String> result = new LinkedHashMap<>();
        StringBuffer sql = new StringBuffer();
        sql.append("select DISTINCT USER_CODE,USER_NAME from ORG_LIST ");
        sql.append("where IFNULL(USER_CODE,'') != '' ");
        sql.append(" and IFNULL(USER_AREA, '') not in ('188J', '188K', '188L') ");
        if(StringUtils.isNoneBlank(areaCode)) {
            sql.append(" AND USER_AREA = '" + areaCode + "'");
        }
        if(StringUtils.isNoneBlank(groupCode)) {
            sql.append(" AND USER_GROUP = '" + groupCode + "'");
        }
        sql.append(" ORDER BY USER_CODE");
        @SuppressWarnings("unchecked")
        List<Object[]> context = findBySQL(sql).getContent();
        for(Object[] ob : context) {
            String userCode = ob[0] == null ? "" : ob[0].toString();
            String userName = ob[1] == null ? "" : ob[1].toString();
            result.put(userCode,userName);
        }
        return result;
    }

    @Override
    public List<FbAppointmentRecord> getAllAppointmentRecord(String areaCode, String groupCode, String userCode) {
        return appointmentRecordRepository.findAll();
    }

    @SuppressWarnings("unchecked")
    @Override
    public QueryPage<Object[]> fingTravelSchedule(QueryPage<Object[]> queryPage, String userCode) {
        List<String> userList = new ArrayList<>();
        String userStr = "";
        User user = UserUtil.getCurrUser();
        if(User.USER_TYPE_S.equals(user.getUserType()) || User.USER_TYPE_Z.equals(user.getUserType())) {
            userList.add(user.getUserCode());
        } else if(User.USER_TYPE_C.equals(user.getUserType())) {
            userList = fbUserRepository.getUserCodeByGroup(user.getUserArea(),user.getUserGroup());
        } else if(User.USER_TYPE_A.equals(user.getUserType())) {
            userList = fbUserRepository.getUserCodeByArea(user.getUserArea());
        } else if(User.USER_TYPE_M.equals(user.getUserType())) {
            userList = fbUserRepository.getUserCodeByAll();
        }
        userStr = "'" + StringUtils.join(userList.toArray(), "','") + "'";
        StringBuffer sql = new StringBuffer();
        sql.append("select DISTINCT temp.COMPILATION_NO, ");
        sql.append("       STR_TO_DATE(GREATEST(IFNULL(ai.TRAND_ID,''),IFNULL(vcd.TRAND_ID,''),IFNULL(cr.TRAND_ID,''),IFNULL(mw.TRAND_ID,'')),'%Y%m%d') TRAND_DAY, ");
        sql.append("       lc.COM_NAME, ");
        sql.append("       vcd.COMPILATION_NO visit, ");
        sql.append("       mw.COMPILATION_NO measureWord, ");
        sql.append("       ai.COMPILATION_NO applyIncome, ");
        sql.append("       cr.COMPILATION_NO creditReport, ");
        sql.append("       mw.MEASURE_ID  measureId ");
        sql.append("  from (select COMPILATION_NO from FB_APPLY_INCOM where CREATE_USER in ("+userStr +") and APPLY_STATUS = '1' union ");
        sql.append("        select COMPILATION_NO from FB_VISIT_COMPANY_DETAIL where CREATE_USER in ("+userStr +") and CHECK_SUBMIT_STATU = '1' union ");
        sql.append("        select COMPILATION_NO from FB_CREDIT_REPORTING where CREATE_USER in ("+userStr +") and CREDIT_STATU = '1' union ");
        sql.append("        select COMPILATION_NO from FB_MEASURE_WORD where CREATE_USER in ("+userStr +") and STATUS = '1') temp ");
        sql.append("  inner join FB_LOAN_COMPANY lc on temp.COMPILATION_NO = lc.COMPILATION_NO ");
        sql.append("  left join FB_APPLY_INCOM ai on ai.COMPILATION_NO = temp.COMPILATION_NO and ai.TRAND_ID = (select max(TRAND_ID) from FB_APPLY_INCOM where COMPILATION_NO = temp.COMPILATION_NO and APPLY_STATUS = '1') ");
        sql.append("  left join FB_VISIT_COMPANY_DETAIL vcd on vcd.COMPILATION_NO = temp.COMPILATION_NO and vcd.TRAND_ID = (select max(TRAND_ID) from FB_VISIT_COMPANY_DETAIL where COMPILATION_NO = temp.COMPILATION_NO and CHECK_SUBMIT_STATU = '1')");
        sql.append("  left join FB_CREDIT_REPORTING cr on cr.COMPILATION_NO = temp.COMPILATION_NO and cr.TRAND_ID = (select max(TRAND_ID) from FB_CREDIT_REPORTING where COMPILATION_NO = temp.COMPILATION_NO and CREDIT_STATU = '1') ");
        sql.append("  left join FB_MEASURE_WORD mw on mw.COMPILATION_NO = temp.COMPILATION_NO and mw.TRAND_ID = (select max(TRAND_ID) from FB_MEASURE_WORD where COMPILATION_NO = temp.COMPILATION_NO and STATUS = '1')");
        sql.append(" order by TRAND_DAY desc");
        QueryPage<Object[]> page = findBySQL(sql);
        QueryPage<Object[]> context = findBySQL(sql,queryPage);
        context.setTotal(page.getContent() == null ? 0 : page.getContent().size());
        return context;
    }
    
    @Override
    public AjaxResut saveStroke(MissionStrokeModel missionStrokeModel,String caseNo) {
        AjaxResut ajaxResut = new AjaxResut();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        User user= UserUtil.getCurrUser();
        try {
            FbAppointmentRecord oldAppointmentRecord = appointmentRecordRepository.findByIds(missionStrokeModel.getCompilationNo(),missionStrokeModel.getAppointmentDate().replaceAll("-",""),"S".equals(user.getUserType()) ? "1" : "2");
            if(oldAppointmentRecord!= null) {
                ajaxResut.setReturnCode(false);
                ajaxResut.setReturnMessage("該天已存在預約");
            } else {
                //更新公司信息
                FbLoanCompany loanCompany = loanCompanyRepository.findByComplicationNo(missionStrokeModel.getCompilationNo());
                if (loanCompany == null) {
                    loanCompany = new FbLoanCompany();
                    loanCompany.setCompilationNo(missionStrokeModel.getCompilationNo());
                }
                // 公司名稱
                loanCompany.setComName(missionStrokeModel.getCompilationName());
                // 負責人
                loanCompany.setPrincipalName(missionStrokeModel.getPrincilpalName());
                // 公司設立日期
                loanCompany.setComEstabdate(sdf.parse(missionStrokeModel.getComEstabdate()));
                // 組織型態
                loanCompany.setComOrganization(missionStrokeModel.getComOrganization());
                // 組織型態名稱
                loanCompany.setComOrganizationName(missionStrokeModel.getComOrganizationName());
                // 實際資本額
                loanCompany.setComActualCapital(missionStrokeModel.getComActualCapital());
                // 公司登記地址
                loanCompany.setComAddress(missionStrokeModel.getCompRegAddress());

                loanCompanyRepository.saveAndFlush(loanCompany);
                //儲存预约记录
                FbAppointmentRecord appointmentRecord = new FbAppointmentRecord();
                BeanUtils.copyProperties(missionStrokeModel,appointmentRecord);
                appointmentRecord.setTrandId(CommonUtils.generateTrandId(missionStrokeModel.getCompilationNo()));
                appointmentRecord.setAppointmentTimeH(missionStrokeModel.getAppointmentTimeH().length() == 1 ? ("0" +missionStrokeModel.getAppointmentTimeH()) :missionStrokeModel.getAppointmentTimeH());
                appointmentRecord.setAppointmentTimeM(missionStrokeModel.getAppointmentTimeM().length() == 1 ? ("0" +missionStrokeModel.getAppointmentTimeM()) :missionStrokeModel.getAppointmentTimeM());
                //职位为负责人时添加到负责人栏位
//                if("FZR".equals(missionStrokeModel.getAppointmentPosition()))
//                    appointmentRecord.setChargePerson(missionStrokeModel.getAppointmentUser());
                appointmentRecord.setChargePerson(missionStrokeModel.getPrincilpalName());
                appointmentRecord.setAppointmentUserCode(user.getUserCode());
                appointmentRecord.setAppointmentUserName(user.getUserName());
                appointmentRecord.setAppointmentType("S".equals(user.getUserType()) ? "1" : "2");
                appointmentRecord.setCreateUser(user.getUserCode());
                appointmentRecord.setCreateTime(new Date());
                appointmentRecord.setZjCreditNum(caseNo);
                appointmentRecordRepository.save(appointmentRecord);
                ajaxResut.setReturnCode(true);
                ajaxResut.setReturnMessage("新增任務成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResut.setReturnCode(false);
            ajaxResut.setReturnMessage("儲存數據失敗");
        }
        return ajaxResut;
    }

    @Override
    public AjaxResut saveStrokeSpd(MissionStrokeModel missionStrokeModel,String mobile,String email,String caseNo) {
        AjaxResut ajaxResut = new AjaxResut();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        User user= UserUtil.getCurrUser();
        try {
            FbAppointmentRecord oldAppointmentRecord = appointmentRecordRepository.findByIds(missionStrokeModel.getCompilationNo(),missionStrokeModel.getAppointmentDate().replaceAll("-",""),"S".equals(user.getUserType()) ? "1" : "2");
            if(oldAppointmentRecord!= null) {
                ajaxResut.setReturnCode(false);
                ajaxResut.setReturnMessage("該天已存在預約");
            } else {
                //更新公司信息
                FbLoanCompany loanCompany = loanCompanyRepository.findByComplicationNo(missionStrokeModel.getCompilationNo());
                if (loanCompany == null) {
                    loanCompany = new FbLoanCompany();
                    loanCompany.setCompilationNo(missionStrokeModel.getCompilationNo());
                }
                // 公司名稱
                loanCompany.setComName(missionStrokeModel.getCompilationName());
                // 負責人
                loanCompany.setPrincipalName(missionStrokeModel.getPrincilpalName());
                // 公司設立日期
                loanCompany.setComEstabdate(sdf.parse(missionStrokeModel.getComEstabdate()));
                // 組織型態
                loanCompany.setComOrganization(missionStrokeModel.getComOrganization());
                // 組織型態名稱
                loanCompany.setComOrganizationName(missionStrokeModel.getComOrganizationName());
                // 實際資本額
                loanCompany.setComActualCapital(missionStrokeModel.getComActualCapital());
                // 公司登記地址
                loanCompany.setComAddress(missionStrokeModel.getCompRegAddress());

                loanCompanyRepository.saveAndFlush(loanCompany);
                //更新申請流程
                ApplyProc applyProc = new ApplyProc();
                List<ApplyProc> applyProcList = applyProcRepository.findByCompCode(missionStrokeModel.getCompilationNo());
                if(applyProcList == null || applyProcList.size()<=0) {
                    applyProc.setCompCode(missionStrokeModel.getCompilationNo());
                    applyProc.setCompName(missionStrokeModel.getCompilationName());
                    applyProc.setCreateUser(user.getUserCode());
                    applyProc.setCreateTime(new Date());
                } else {
                    applyProc = applyProcList.get(0);
                    applyProc.setUpdateTime(new Date());
                    applyProc.setUpdateUser(user.getUserCode());
                }
                applyProc.setProcStatus("02");
                applyProc.setProcNode("S".equals(user.getUserType()) ? "01" : "06");
                if("S".equals(user.getUserType())){
                    applyProc.setVistStatus("01");
                } else {
                    applyProc.setCreditStatus("01");
                }
                applyProc = applyProcRepository.saveAndFlush(applyProc);
                //儲存预约记录
                FbAppointmentRecord appointmentRecord = new FbAppointmentRecord();
                BeanUtils.copyProperties(missionStrokeModel,appointmentRecord);
                appointmentRecord.setTrandId(missionStrokeModel.getAppointmentDate().replaceAll("-",""));
                appointmentRecord.setAppointmentTimeH(missionStrokeModel.getAppointmentTimeH().length() == 1 ? ("0" +missionStrokeModel.getAppointmentTimeH()) :missionStrokeModel.getAppointmentTimeH());
                appointmentRecord.setAppointmentTimeM(missionStrokeModel.getAppointmentTimeM().length() == 1 ? ("0" +missionStrokeModel.getAppointmentTimeM()) :missionStrokeModel.getAppointmentTimeM());
                //职位为负责人时添加到负责人栏位
//                if("FZR".equals(missionStrokeModel.getAppointmentPosition()))
//                    appointmentRecord.setChargePerson(missionStrokeModel.getAppointmentUser());
                appointmentRecord.setChargePerson(missionStrokeModel.getPrincilpalName());
                appointmentRecord.setAppointmentUserCode(user.getUserCode());
                appointmentRecord.setAppointmentUserName(user.getUserName());
                appointmentRecord.setAppointmentType("S".equals(user.getUserType()) ? "1" : "2");
                appointmentRecord.setCreateUser(user.getUserCode());
                appointmentRecord.setCreateTime(new Date());
                appointmentRecord.setZjCreditNum(caseNo);
                appointmentRecord.setMobile(mobile);
                appointmentRecord.setEmail(email);
                appointmentRecord.setApplyProcNum(applyProc.getAppyProcNum());
                appointmentRecordRepository.save(appointmentRecord);
                ajaxResut.setReturnCode(true);
                ajaxResut.setReturnMessage("新增任務成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResut.setReturnCode(false);
            ajaxResut.setReturnMessage("儲存數據失敗");
        }
        return ajaxResut;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public AjaxResut updateStroke(MissionStrokeModel missionStrokeModel) {
        AjaxResut ajaxResut = new AjaxResut();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        User user= UserUtil.getCurrUser();
        try {
            
            StringBuffer selSql = new StringBuffer();
            selSql.append(" SELECT COUNT(*) FROM FB_APPOINTMENT_RECORD AR");
            selSql.append(" WHERE 1 = 1");
            selSql.append(" AND AR.COMPILATION_NO = :compilationNo");
            selSql.append(" AND AR.TRAND_ID = :trandId");
            selSql.append(" AND AR.APPOINTMENT_TYPE = :appointmentType");
            selSql.append(" AND AR.APPOINTMENT_ID != :appointmentId");
            Map<String, Object> selParams = new HashMap<>();
            selParams.put("compilationNo", missionStrokeModel.getCompilationNo());
            selParams.put("trandId", missionStrokeModel.getAppointmentDate().replaceAll("-",""));
            selParams.put("appointmentType", "S".equals(user.getUserType()) ? "1" : "2");
            selParams.put("appointmentId", missionStrokeModel.getAppointmentId());
            QueryPage<Object[]> queryPage = findBySQL(selSql, selParams);
            if (queryPage != null) {
                List<Object[]> queryList = queryPage.getContent();
                if (!CollectionUtils.isEmpty(queryList)) {
                    Object selResult = queryList.get(0);
                    if (!"0".equals(ObjectUtil.obj2String(selResult))) {
                        ajaxResut.setReturnCode(false);
                        ajaxResut.setReturnMessage("該天已存在預約");
                        return ajaxResut;
                    }
                }
            }
            // 更新公司信息
            FbLoanCompany loanCompany = loanCompanyRepository.findByComplicationNo(missionStrokeModel.getCompilationNo());
            if (loanCompany == null) {
                loanCompany = new FbLoanCompany();
                loanCompany.setCompilationNo(missionStrokeModel.getCompilationNo());
            }
            // 公司名稱
            loanCompany.setComName(missionStrokeModel.getCompilationName());
            // 負責人
            loanCompany.setPrincipalName(missionStrokeModel.getPrincilpalName());
            // 公司設立日期
            loanCompany.setComEstabdate(sdf.parse(missionStrokeModel.getComEstabdate()));
            // 組織型態
            loanCompany.setComOrganization(missionStrokeModel.getComOrganization());
            // 組織型態名稱
            loanCompany.setComOrganizationName(missionStrokeModel.getComOrganizationName());
            // 實際資本額
            loanCompany.setComActualCapital(missionStrokeModel.getComActualCapital());
            // 公司登記地址
            loanCompany.setComAddress(missionStrokeModel.getCompRegAddress());

            loanCompanyRepository.saveAndFlush(loanCompany);
            //儲存预约记录
            FbAppointmentRecord appointmentRecord = appointmentRecordRepository.findByAppointmentId(missionStrokeModel.getAppointmentId());
            BeanUtils.copyProperties(missionStrokeModel,appointmentRecord);
//            appointmentRecord.setTrandId(missionStrokeModel.getAppointmentDate().replaceAll("-",""));
            appointmentRecord.setAppointmentTimeH(missionStrokeModel.getAppointmentTimeH().length() == 1 ? ("0" +missionStrokeModel.getAppointmentTimeH()) :missionStrokeModel.getAppointmentTimeH());
            appointmentRecord.setAppointmentTimeM(missionStrokeModel.getAppointmentTimeM().length() == 1 ? ("0" +missionStrokeModel.getAppointmentTimeM()) :missionStrokeModel.getAppointmentTimeM());
            appointmentRecord.setChargePerson(missionStrokeModel.getPrincilpalName());
            appointmentRecord.setAppointmentUserCode(user.getUserCode());
            appointmentRecord.setAppointmentUserName(user.getUserName());
            appointmentRecord.setAppointmentType("S".equals(user.getUserType()) ? "1" : "2");
            appointmentRecord.setUpdateUser(user.getUserCode());
            appointmentRecord.setUpdateTime(new Date());
            appointmentRecordRepository.save(appointmentRecord);
            ajaxResut.setReturnCode(true);
            ajaxResut.setReturnMessage("更新任務成功");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResut.setReturnCode(false);
            ajaxResut.setReturnMessage("儲存數據失敗");
        }
        return ajaxResut;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public AjaxResut updateStrokeSpd(MissionStrokeModel missionStrokeModel,String mobile,String email) {
        AjaxResut ajaxResut = new AjaxResut();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        User user= UserUtil.getCurrUser();
        try {
            
            StringBuffer selSql = new StringBuffer();
            selSql.append(" SELECT COUNT(*) FROM FB_APPOINTMENT_RECORD AR");
            selSql.append(" WHERE 1 = 1");
            selSql.append(" AND AR.COMPILATION_NO = :compilationNo");
            selSql.append(" AND AR.TRAND_ID = :trandId");
            selSql.append(" AND AR.APPOINTMENT_TYPE = :appointmentType");
            selSql.append(" AND AR.APPOINTMENT_ID != :appointmentId");
            Map<String, Object> selParams = new HashMap<>();
            selParams.put("compilationNo", missionStrokeModel.getCompilationNo());
            selParams.put("trandId", missionStrokeModel.getAppointmentDate().replaceAll("-",""));
            selParams.put("appointmentType", "S".equals(user.getUserType()) ? "1" : "2");
            selParams.put("appointmentId", missionStrokeModel.getAppointmentId());
            QueryPage<Object[]> queryPage = findBySQL(selSql, selParams);
            if (queryPage != null) {
                List<Object[]> queryList = queryPage.getContent();
                if (!CollectionUtils.isEmpty(queryList)) {
                    Object selResult = queryList.get(0);
                    if (!"0".equals(ObjectUtil.obj2String(selResult))) {
                        ajaxResut.setReturnCode(false);
                        ajaxResut.setReturnMessage("該天已存在預約");
                        return ajaxResut;
                    }
                }
            }
            // 更新公司信息
            FbLoanCompany loanCompany = loanCompanyRepository.findByComplicationNo(missionStrokeModel.getCompilationNo());
            if (loanCompany == null) {
                loanCompany = new FbLoanCompany();
                loanCompany.setCompilationNo(missionStrokeModel.getCompilationNo());
            }
            // 公司名稱
            loanCompany.setComName(missionStrokeModel.getCompilationName());
            // 負責人
            loanCompany.setPrincipalName(missionStrokeModel.getPrincilpalName());
            // 公司設立日期
            loanCompany.setComEstabdate(sdf.parse(missionStrokeModel.getComEstabdate()));
            // 組織型態
            loanCompany.setComOrganization(missionStrokeModel.getComOrganization());
            // 組織型態名稱
            loanCompany.setComOrganizationName(missionStrokeModel.getComOrganizationName());
            // 實際資本額
            loanCompany.setComActualCapital(missionStrokeModel.getComActualCapital());
            // 公司登記地址
            loanCompany.setComAddress(missionStrokeModel.getCompRegAddress());

            loanCompanyRepository.saveAndFlush(loanCompany);
            //儲存预约记录
            FbAppointmentRecord appointmentRecord = appointmentRecordRepository.findByAppointmentId(missionStrokeModel.getAppointmentId());
            BeanUtils.copyProperties(missionStrokeModel,appointmentRecord);
            //appointmentRecord.setTrandId(missionStrokeModel.getAppointmentDate().replaceAll("-",""));
            appointmentRecord.setAppointmentTimeH(missionStrokeModel.getAppointmentTimeH().length() == 1 ? ("0" +missionStrokeModel.getAppointmentTimeH()) :missionStrokeModel.getAppointmentTimeH());
            appointmentRecord.setAppointmentTimeM(missionStrokeModel.getAppointmentTimeM().length() == 1 ? ("0" +missionStrokeModel.getAppointmentTimeM()) :missionStrokeModel.getAppointmentTimeM());
            appointmentRecord.setChargePerson(missionStrokeModel.getPrincilpalName());
            appointmentRecord.setAppointmentUserCode(user.getUserCode());
            appointmentRecord.setAppointmentUserName(user.getUserName());
            appointmentRecord.setAppointmentType("S".equals(user.getUserType()) ? "1" : "2");
            appointmentRecord.setUpdateUser(user.getUserCode());
            appointmentRecord.setUpdateTime(new Date());
            appointmentRecord.setMobile(mobile);
            appointmentRecord.setEmail(email);
            appointmentRecordRepository.save(appointmentRecord);
            ajaxResut.setReturnCode(true);
            ajaxResut.setReturnMessage("更新任務成功");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResut.setReturnCode(false);
            ajaxResut.setReturnMessage("儲存數據失敗");
        }
        return ajaxResut;
    }

    /**
     * 查询任务行程
     * @param areaCode
     * @param groupCode
     * @param userCode
     * @param comName
     * @param appointmentDate
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public QueryPage<Object[]> searchStroke(String areaCode, String groupCode, String userCode, String comName,String appointmentDate) {
        StringBuffer sb = new StringBuffer();
        Map<String,Object> params = new HashMap<String,Object>();
        
        sb.append("SELECT ");
        sb.append(" AR.APPOINTMENT_TIME_H "); // 0
        sb.append(" , AR.APPOINTMENT_TIME_M "); // 1
        sb.append(" , AR.APPOINTMENT_DATE "); // 2
        sb.append(" , AR.COMPILATION_NAME "); // 3
        sb.append(" , AR.BUSINESS_ADDRESS "); // 4
        sb.append(" , AR.APPOINTMENT_NAME "); // 5
        sb.append(" , AR.COM_PHONE_AREACODE "); // 6
        sb.append(" , AR.COM_PHONE_NUM "); // 7
        sb.append(" , AR.COM_PHONE_EXTEN "); // 8
        sb.append(" , AR.APPOINTMENT_USER_NAME "); // 9
        sb.append(" , AR.COMPILATION_NO "); // 10
        sb.append(" , AR.TRAND_ID "); // 11
        sb.append(" , AR.APPOINTMENT_TYPE "); // 12
        sb.append(" , IFNULL(VCD.CHECK_SUBMIT_STATU, '-1') CHECK_SUBMIT_STATU"); // 13
        sb.append(" , CR.CREDIT_STATU"); // 14
        sb.append(" , AR.CREATE_USER"); // 15
        
        sb.append(" FROM FB_APPOINTMENT_RECORD AR ");
        sb.append(" INNER JOIN FB_USER U ON AR.APPOINTMENT_USER_CODE = U.USER_CODE ");
        // 关联拜访笔记表
        sb.append(" LEFT JOIN (SELECT T1.COMPILATION_NO, T1.TRAND_ID, IFNULL(T1.CHECK_SUBMIT_STATU, '0') CHECK_SUBMIT_STATU FROM FB_VISIT_COMPANY_DETAIL T1 ");
        sb.append(" WHERE T1.CREATE_TIME = (SELECT MAX(CREATE_TIME) FROM FB_VISIT_COMPANY_DETAIL T2 WHERE T1.VISIT_ID = T2.VISIT_ID)) VCD ");
        sb.append(" ON AR.COMPILATION_NO = VCD.COMPILATION_NO AND AR.TRAND_ID = VCD.TRAND_ID ");
        // 关联征信实访表
        sb.append(" LEFT JOIN (SELECT T1.COMPILATION_NO, T1.TRAND_ID, T1.CREDIT_STATU FROM FB_CREDIT_REPORTING T1 ");
        sb.append(" WHERE T1.CREATE_TIME = (SELECT MAX(CREATE_TIME) FROM FB_CREDIT_REPORTING T2 WHERE T1.REPORTING_ID = T2.REPORTING_ID)) CR ");
        sb.append(" ON AR.COMPILATION_NO = CR.COMPILATION_NO AND AR.TRAND_ID = CR.TRAND_ID ");
        sb.append(" WHERE 1 = 1");
        
        if (StringUtils.isNoneBlank(areaCode)) {
            sb.append(" AND U.USER_AREA = :areaCode");
            params.put("areaCode", areaCode);
        }
        if (StringUtils.isNoneBlank(groupCode)) {
            sb.append(" AND U.USER_GROUP = :groupCode");
            params.put("groupCode", groupCode);
        }
        if (StringUtils.isNoneBlank(userCode)) {
            sb.append(" AND U.USER_CODE = :userCode");
            params.put("userCode", userCode);
        }
        if (StringUtils.isNoneBlank(comName)) {
            sb.append(" AND (AR.COMPILATION_NAME LIKE :comName");
            params.put("comName", "%" + comName + "%");
            sb.append(" OR AR.COMPILATION_NO = :compilationNo)");
            params.put("compilationNo", comName );
            sb.append(" ORDER BY AR.APPOINTMENT_DATE ASC");
        } else {
            sb.append(" AND AR.APPOINTMENT_DATE = :appointmentDate ");
            params.put("appointmentDate", appointmentDate);
            sb.append(" ORDER BY AR.APPOINTMENT_TIME_H ASC, AR.APPOINTMENT_TIME_M ASC");
        }
        
        return findBySQL(sb , params);
    }
 
    
    @SuppressWarnings("unchecked")
    public QueryPage<Object[]> searchStrokeByMonth(String areaCode, String groupCode, String userCode, String comName, String appointmentDate) {
        StringBuffer sb = new StringBuffer();
        Map<String,Object> params = new HashMap<String,Object>();

        sb.append("SELECT ");
        sb.append(" AR.APPOINTMENT_TIME_H "); // 0
        sb.append(" , AR.APPOINTMENT_TIME_M "); // 1
        sb.append(" , AR.APPOINTMENT_DATE "); // 2
        sb.append(" , AR.COMPILATION_NAME "); // 3
        sb.append(" , AR.BUSINESS_ADDRESS "); // 4
        sb.append(" , AR.APPOINTMENT_NAME "); // 5
        sb.append(" , AR.COM_PHONE_AREACODE "); // 6
        sb.append(" , AR.COM_PHONE_NUM "); // 7
        sb.append(" , AR.COM_PHONE_EXTEN "); // 8
        sb.append(" , AR.APPOINTMENT_USER_NAME "); // 9
        sb.append(" , AR.COMPILATION_NO "); // 10
        sb.append(" , AR.TRAND_ID "); // 11
        sb.append(" , AR.APPOINTMENT_TYPE "); // 12
        sb.append(" , IFNULL(VCD.CHECK_SUBMIT_STATU, '-1') CHECK_SUBMIT_STATU"); // 13
        sb.append(" , CR.CREDIT_STATU"); // 14
        sb.append(" , AR.CREATE_USER"); // 15
        
        sb.append(" FROM FB_APPOINTMENT_RECORD AR ");
        sb.append(" INNER JOIN FB_USER U ON AR.APPOINTMENT_USER_CODE = U.USER_CODE ");
        // 关联拜访笔记表
        sb.append(" LEFT JOIN (SELECT T1.COMPILATION_NO, T1.TRAND_ID, IFNULL(T1.CHECK_SUBMIT_STATU, '0') CHECK_SUBMIT_STATU FROM FB_VISIT_COMPANY_DETAIL T1 ");
        sb.append(" WHERE T1.CREATE_TIME = (SELECT MAX(CREATE_TIME) FROM FB_VISIT_COMPANY_DETAIL T2 WHERE T1.VISIT_ID = T2.VISIT_ID)) VCD ");
        sb.append(" ON AR.COMPILATION_NO = VCD.COMPILATION_NO AND AR.TRAND_ID = VCD.TRAND_ID ");
        // 关联征信实访表
        sb.append(" LEFT JOIN (SELECT T1.COMPILATION_NO, T1.TRAND_ID, T1.CREDIT_STATU FROM FB_CREDIT_REPORTING T1 ");
        sb.append(" WHERE T1.CREATE_TIME = (SELECT MAX(CREATE_TIME) FROM FB_CREDIT_REPORTING T2 WHERE T1.REPORTING_ID = T2.REPORTING_ID)) CR ");
        sb.append(" ON AR.COMPILATION_NO = CR.COMPILATION_NO AND AR.TRAND_ID = CR.TRAND_ID ");
        sb.append(" WHERE 1 = 1");
        
        if (StringUtils.isNoneBlank(areaCode)) {
            sb.append(" AND U.USER_AREA = :areaCode");
            params.put("areaCode", areaCode);
        }
        if (StringUtils.isNoneBlank(groupCode)) {
            sb.append(" AND U.USER_GROUP = :groupCode");
            params.put("groupCode", groupCode);
        }
        if (StringUtils.isNoneBlank(userCode)) {
            sb.append(" AND U.USER_CODE = :userCode");
            params.put("userCode", userCode);
        }
        
        if (StringUtils.isNoneBlank(comName)) {
            sb.append(" AND (AR.COMPILATION_NAME LIKE :comName");
            params.put("comName", "%" + comName + "%");
            sb.append(" OR AR.COMPILATION_NO = :compilationNo)");
            params.put("compilationNo", comName );
        } else {
            sb.append(" AND SUBSTRING(AR.APPOINTMENT_DATE, 1, 7) = :appointmentDate ");
            params.put("appointmentDate", appointmentDate.substring(0, 7));
        }
        sb.append(" ORDER BY AR.APPOINTMENT_DATE ASC");
        
        return findBySQL(sb , params);
    }
    
    public Map<String,String> findRate(Model model,String prodCode) {
        Map<String,String> returnMap = new HashMap<>();
        User user = UserUtil.getCurrUser();
        Calendar cal = Calendar.getInstance();
        String year = cal.get(Calendar.YEAR) + "";
        int m = cal.get(Calendar.MONTH )+1;
        String month = m < 10 ? "0" + m : m +"";
        DecimalFormat df = new DecimalFormat("0.00");
        List<TaskComplishStatistics> taskComplishStatisticsList = new ArrayList<TaskComplishStatistics>();
        if(User.USER_TYPE_S.equals(user.getUserType()) || User.USER_TYPE_Z.equals(user.getUserType()))
            taskComplishStatisticsList = taskComplishStatisticsRepository.findByUser(user.getUserCode(),year,month);
        else if (User.USER_TYPE_C.equals(user.getUserType()))
            taskComplishStatisticsList = taskComplishStatisticsRepository.findByGroup(user.getUserGroup(),year,month);
        else if (User.USER_TYPE_A.equals(user.getUserType()))
            taskComplishStatisticsList = taskComplishStatisticsRepository.findByArea(user.getUserArea(),year,month);
        else
            taskComplishStatisticsList = taskComplishStatisticsRepository.findAll(year,month);
        int comLoanAcount = 0;//进件数
        int comApproAcount = 0;//放款数
        int comGoalAcount = 0;//进件数目标值
        int comApproGoalAcount = 0;//放款数目标值
        for(TaskComplishStatistics tcs : taskComplishStatisticsList) {
            if("PD001".equals(prodCode)) {
                comLoanAcount += tcs.getComLoanAcount() != null ? tcs.getComLoanAcount().intValue() : 0;
                comApproAcount += tcs.getComApproAcount() != null ? tcs.getComApproAcount().intValue() : 0;
                comGoalAcount += tcs.getComGoalAcount() != null ? tcs.getComGoalAcount().intValue() : 0;
                comApproGoalAcount += tcs.getComApproGoalAcount() != null ? tcs.getComApproGoalAcount().intValue() : 0;
            } else if("PD002".equals(prodCode)) {
                comLoanAcount += tcs.getCreditLoanAcount() != null ? tcs.getCreditLoanAcount().intValue() : 0;
                comApproAcount += tcs.getCreditApproAcount() != null ? tcs.getCreditApproAcount().intValue() : 0;
                comGoalAcount += tcs.getCreditGoalAcount() != null ? tcs.getCreditGoalAcount().intValue() : 0;
                comApproGoalAcount += tcs.getCreditApproGoalAcount() != null ? tcs.getCreditApproGoalAcount().intValue() : 0;
            } else if("PD003".equals(prodCode)) {
                comLoanAcount += tcs.getHouseLoanAcount() != null ? tcs.getHouseLoanAcount().intValue() : 0;
                comApproAcount += tcs.getHouseApproAcount() != null ? tcs.getHouseApproAcount().intValue() : 0;
                comGoalAcount += tcs.getHouseGoalAcount() != null ? tcs.getHouseGoalAcount().intValue() : 0;
                comApproGoalAcount += tcs.getHouseApproGoalAcount() != null ? tcs.getHouseApproGoalAcount().intValue() : 0;
            }
        }
        //進件達成率
        returnMap.put("entry", comLoanAcount +"," + ((comGoalAcount-comLoanAcount) > 0 ? (comGoalAcount-comLoanAcount) : 0 ));
        returnMap.put("entryRate", (comLoanAcount == 0 ? 100 : df.format((float)comLoanAcount/comGoalAcount*100)) + "%");
        //業績達成率
        returnMap.put("achievement", comApproAcount +"," + ((comApproGoalAcount-comApproAcount) > 0 ? (comApproGoalAcount-comApproAcount) : 0 ));
        returnMap.put("achievementRate", (comApproGoalAcount == 0 ? 100 : df.format((float)comApproAcount/comApproGoalAcount*100)) + "%");
        return returnMap;
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, Object> delMissionStroke(String compilationNo, String trandId, String appointmentType, String createUser) {
        Map<String, Object> result = new HashMap<>();
        User user = UserUtil.getCurrUser();
        
        try {
            logger.info("任务行程删除开始，当前登录用户：" + user.getUserCode() + "，任务行程统编：" + compilationNo 
                + "，任务行程TrandId：" + trandId + "，预约类型：" + appointmentType);
            
            FbAppointmentRecord appointmentRecord = appointmentRecordRepository.findByIds(compilationNo, trandId, appointmentType); 
            if (!user.getUserCode().equals(createUser)) {
                // 如果當前登錄用戶與該任務行程業務員不一致，不能刪除任務行程
                result.put("flag", false);
                result.put("msg", "當前登錄用戶與任務行程業務員不一致，不能刪除任務行程");
                logger.info("當前登錄用戶與任務行程業務員不一致，當前登錄用戶：" + user.getUserCode() + "，任務行程業務員：" + createUser);
                return result;
            }
            
            if ("1".equals(appointmentType)) {
                // 业务员的任务行程删除，需要先判斷拜訪筆記是否完成
                // 查询拜访笔记
                StringBuffer selSql = new StringBuffer();
                selSql.append(" SELECT ");
                selSql.append("    VISIT_ID, ");
                selSql.append("    CHECK_SUBMIT_STATU, ");
                selSql.append("    CREATE_USER ");
                selSql.append(" FROM ");
                selSql.append("    FB_VISIT_COMPANY_DETAIL ");
                selSql.append(" WHERE ");
                selSql.append("    COMPILATION_NO = :compilationNo ");
                selSql.append("    AND TRAND_ID = :trandId ");
                selSql.append("    AND CREATE_USER = :createUser");
                Map<String, Object> params = new HashMap<>();
                params.put("compilationNo", compilationNo);
                params.put("trandId", trandId);
                params.put("createUser", createUser);
                QueryPage<Object[]> selQueryPage = findBySQL(selSql, params);
                if (selQueryPage != null) {
                    List<Object[]> selList = selQueryPage.getContent();
                    if (!CollectionUtils.isEmpty(selList)) {
                        Object[] selRow = selList.get(0);
                        String visitId = ObjectUtil.obj2String(selRow[0]);
                        String visitStatus = ObjectUtil.obj2String(selRow[1]);
                        logger.info("拜訪筆記ID：" + visitId + "，拜訪筆記狀態：" + visitStatus + "，拜訪筆記業務員：" + createUser);
                        
                        // 如果拜访笔记已完成，不能删除任務行程
                        if ("1".equals(visitStatus)) {
                            result.put("flag", false);
                            result.put("msg", "拜訪筆記已完成，不能刪除任務行程");
                            logger.info("拜訪筆記已完成，不能刪除任務行程");
                            return result;
                        }
                        
                        // 刪除拜訪筆記
                        logger.info("開始刪除拜訪筆記，拜訪筆記ID：" + visitId);
                        StringBuffer visitDelSql = new StringBuffer();
                        visitDelSql.append("DELETE FROM FB_VISIT_COMPANY_DETAIL WHERE VISIT_ID = :visitId");
                        Map<String, Object> visitDelParams = new HashMap<>();
                        visitDelParams.put("visitId", visitId);
                        int visitDelCount = executeSQL(visitDelSql, visitDelParams);
                        if (visitDelCount > 0) {
                            logger.info("拜訪筆記刪除成功，刪除條數：" + visitDelCount);
                        } else {
                            logger.info("拜訪筆記刪除失敗，刪除條數：" + visitDelCount);
                        }
                    }
                }
            } else if ("2".equals(appointmentType)) {
                // 征信員任務行程刪除，需要先判斷征信實訪實訪完成
                // 查询征信實訪
                StringBuffer selSql = new StringBuffer();
                selSql.append(" SELECT ");
                selSql.append("     REPORTING_ID, ");
                selSql.append("     CREDIT_STATU, ");
                selSql.append("     CREATE_USER ");
                selSql.append(" FROM ");
                selSql.append("     FB_CREDIT_REPORTING ");
                selSql.append(" WHERE ");
                selSql.append("     COMPILATION_NO = :compilationNo ");
                selSql.append("     AND TRAND_ID = :trandId ");
                selSql.append("     AND CREATE_USER = :createUser ");
                Map<String, Object> params = new HashMap<>();
                params.put("compilationNo", compilationNo);
                params.put("trandId", trandId);
                params.put("createUser", createUser);
                QueryPage<Object[]> selQueryPage = findBySQL(selSql, params);
                if (selQueryPage != null) {
                    List<Object[]> selList = selQueryPage.getContent();
                    if (!CollectionUtils.isEmpty(selList)) {
                        Object[] selRow = selList.get(0);
                        String reportingId = ObjectUtil.obj2String(selRow[0]);
                        String creditStatus = ObjectUtil.obj2String(selRow[1]);
                        logger.info("征信實訪ID：" + reportingId + "，征信實訪狀態：" + creditStatus + "，征信實訪業務員：" + createUser);
                        
                        // 如果征信實訪已完成，不能删除任務行程
                        if ("1".equals(creditStatus)) {
                            result.put("flag", false);
                            result.put("msg", "征信實訪已完成，不能刪除任務行程");
                            logger.info("征信實訪已完成，不能刪除任務行程");
                            return result;
                        }
                        
                        // 刪除拜訪筆記
                        logger.info("開始刪除征信實訪，征信實訪ID：" + reportingId);
                        StringBuffer creditDelSql = new StringBuffer();
                        creditDelSql.append("DELETE FROM FB_CREDIT_REPORTING WHERE REPORTING_ID = :reportingId");
                        Map<String, Object> creditDelParams = new HashMap<>();
                        creditDelParams.put("reportingId", reportingId);
                        int creditDelCount = executeSQL(creditDelSql, creditDelParams);
                        if (creditDelCount > 0) {
                            logger.info("征信實訪刪除成功，刪除條數：" + creditDelCount);
                        } else {
                            logger.info("征信實訪刪除失敗，刪除條數：" + creditDelCount);
                        }
                    }
                }
            } else {
                logger.info("任務行程預約類型非拜訪筆記和征信實訪，当前登录用户：" + user.getUserCode() + "，任务行程统编：" + compilationNo 
                    + "，任务行程TrandId：" + trandId + "，预约类型：" + appointmentType);
            }
            
            // 刪除任務行程
            StringBuffer delSql = new StringBuffer();
            Map<String, Object> delParams = new HashMap<>();
            delSql.append(" DELETE FROM FB_APPOINTMENT_RECORD ");
            delSql.append(" WHERE 1 = 1 ");
            delSql.append(" AND COMPILATION_NO = :compilationNo ");
            delSql.append(" AND TRAND_ID = :trandId ");
            delSql.append(" AND APPOINTMENT_TYPE = :appointmentType ");
            delSql.append(" AND CREATE_USER = :userCode");
            delParams.put("compilationNo", compilationNo);
            delParams.put("trandId", trandId);
            delParams.put("appointmentType", appointmentType);
            delParams.put("userCode", user.getUserCode());
            int delCount = executeSQL(delSql, delParams);
            
            if (delCount != 1) {
                logger.info("任務行程刪除失敗，刪除條數：" + delCount);
                result.put("flag", false);
                result.put("msg", "刪除失敗");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return result;
            } 
            
            // 如果任務行程刪除成功，則更新申請流程表記錄
            String applyProcNum = appointmentRecord.getApplyProcNum();
            if (!StringUtils.isBlank(applyProcNum)) {
                List<FbAppointmentRecord> fbAppointmentRecords = appointmentRecordRepository.findByApplyProcNum(applyProcNum);
                if (CollectionUtils.isEmpty(fbAppointmentRecords)) {
                    logger.info("更新申請流程，申請流程ID：" + applyProcNum);
                    ApplyProc applyProc = applyProcRepository.findAppyProcNum(applyProcNum);
                    if (applyProc != null) {
                        applyProc.setProcStatus("01");
                        applyProc.setProcNode("");
                        applyProc.setVistStatus("");
                        applyProc.setCreditStatus("");
                        applyProc.setUpdateTime(new Date());
                        applyProc.setUpdateUser(user.getUserCode());
                        applyProcRepository.saveAndFlush(applyProc);
                        logger.info("更新申請流程结束");
                    }
                }
            }
            
            logger.info("任務行程刪除成功");
            result.put("flag", true);
            result.put("msg", "刪除成功");
            return result;
        } catch (Exception e) {
            logger.error(e.toString());
            e.printStackTrace(System.out);
            result.put("flag", false);
            result.put("msg", "刪除失敗");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        
        return result;
    }
    
    /**
     * @description 根據統編、交易流水號、預約類型查找任務行程
     */
    @SuppressWarnings("unchecked")
    public QueryPage<Object[]> queryByConditions(String compilationNo, String trandId, String appointmentType) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT AR.APPOINTMENT_ID");
        sql.append(", AR.COMPILATION_NO");
        sql.append(", AR.TRAND_ID");
        sql.append(", AR.APPOINTMENT_DATE");
        sql.append(", AR.APPOINTMENT_TIME_H");
        sql.append(", AR.APPOINTMENT_TIME_M"); // 5
        sql.append(", AR.APPOINTMENT_USER_CODE");
        sql.append(", AR.APPOINTMENT_USER_NAME");
        sql.append(", AR.APPOINTMENT_TYPE");
        sql.append(", AR.COM_CUSTOMER_TYPE"); 
        sql.append(", AR.COMPILATION_NAME"); // 10
        sql.append(", AR.BUSINESS_ADDRESS");
        sql.append(", AR.APPOINTMENT_NAME");
        sql.append(", AR.APPOINTMENT_POSITION");
        sql.append(", AR.APPOINTMENT_MARY_STATUS");
        sql.append(", AR.COM_PHONE_AREACODE"); // 15
        sql.append(", AR.COM_PHONE_NUM");
        sql.append(", AR.COM_PHONE_EXTEN");
        sql.append(", AR.ENTER_SOURCE"); 
        sql.append(", AR.OTHER_INTRODUCE_ID");
        sql.append(", AR.OTHER_INTRODUCE_NAME"); // 20
        sql.append(", AR.OTHER_INTRODUCE_PHONE");
        sql.append(", AR.OTHER_INTRODUCE_ADDRESS");
        sql.append(", AR.REMARK");
        sql.append(", AR.CHARGE_PERSON");
        sql.append(", LC.COM_ESTABDATE"); // 25
        sql.append(", LC.COM_ORGANIZATION");
        sql.append(", LC.COM_ACTUAL_CAPITAL");
        sql.append(", LC.COM_ADDRESS");
        sql.append(", AR.CREATE_TIME");
        sql.append(", AR.CREATE_USER"); // 30
        sql.append(", AR.UPDATE_TIME");
        sql.append(", AR.UPDATE_USER");
        sql.append(", AR.PUNCH_CARD_RECODE_ID");
        sql.append(", AR.ZJ_ENTER_CASE_NUM");
        sql.append(", AR.ZJ_CREDIT_NUM"); // 35
        sql.append(", AR.APPOI_POSITION_OTHER");
        
        sql.append(", AR.MOBILE"); // 37
        sql.append(", AR.EMAIL");

        sql.append(" FROM FB_APPOINTMENT_RECORD AR");
        sql.append(" INNER JOIN FB_LOAN_COMPANY LC ON AR.COMPILATION_NO = LC.COMPILATION_NO");
        sql.append(" WHERE 1 = 1");
        sql.append(" AND AR.COMPILATION_NO = :compilationNo");
        sql.append(" AND AR.TRAND_ID = :trandId");
        sql.append(" AND AR.APPOINTMENT_TYPE = :appointmentType");
        
        Map<String, Object> params = new HashMap<>();
        params.put("compilationNo", compilationNo);
        params.put("trandId", trandId);
        params.put("appointmentType", appointmentType);
        
        QueryPage<Object[]> queryPage = findBySQL(sql, params);
        return queryPage;
    }
    
    /**
     * @description 根據DICT_ID查詢數據字典
     */
    @SuppressWarnings("unchecked")
    public QueryPage<Object[]> queryDictByConditions(List<String> paramList) {
        String params = "'" + StringUtils.join(paramList.toArray(), "','") + "'";
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT DICT_ID ");
        sql.append(", ITEM_CODE");
        sql.append(", ITEM_NAME");
        sql.append(" FROM VIEW_DATA_DICT_ITEM ");
        sql.append(" WHERE 1 = 1");
        sql.append(" AND DICT_ID IN (").append(params).append(")");
        return findBySQL(sql);
    }
    
    /**
     * @description 查詢所有市、區、街道信息
     */
    @SuppressWarnings("unchecked")
    public QueryPage<Object[]> queryCityAndDistrictAndStreet() {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT 'CITY' AS TYPE, CITY_CODE AS CODE, CITY_NAME AS NAME, PROVINCE_CODE AS PARENT  FROM VIEW_CITY");
        sql.append(" UNION ALL SELECT 'DISTRICT' AS TYPE, DISTRICT_CODE AS CODE, DISTRICT_NAME AS NAME, CITY_CODE AS PARENT FROM VIEW_DISTRICT");
        sql.append(" UNION ALL SELECT 'STREET' AS TYPE, STREET_CODE AS CODE, STREET_NAME AS NAME, CITY_CODE AS PARENT FROM VIEW_STREET");
        return findBySQL(sql);
    }
}
