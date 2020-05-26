package com.fb.goldencudgel.auditDecisionSystem.service.impl;

import com.comwave.core.data.domain.QueryPage;
import com.comwave.core.jpa.BaseJpaDAO;
import com.fb.goldencudgel.auditDecisionSystem.domain.applyProc.ApplyProc;
import com.fb.goldencudgel.auditDecisionSystem.domain.caseAllotProc.CaseAllotProc;
import com.fb.goldencudgel.auditDecisionSystem.domain.custCaseInfo.CustCaseInfo;
import com.fb.goldencudgel.auditDecisionSystem.domain.messageTemplate.MessageTemplate;
import com.fb.goldencudgel.auditDecisionSystem.domain.user.UserList;
import com.fb.goldencudgel.auditDecisionSystem.model.AjaxResut;
import com.fb.goldencudgel.auditDecisionSystem.model.User;
import com.fb.goldencudgel.auditDecisionSystem.repository.*;
import com.fb.goldencudgel.auditDecisionSystem.schema.webSendMessage.WebSendMessageRq;
import com.fb.goldencudgel.auditDecisionSystem.service.ICaseAllocationService;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 * hu
 */
@Service
@Transactional
public class CaseAllocationServiceImpl extends BaseJpaDAO implements ICaseAllocationService {


    private Logger logger = LoggerFactory.getLogger(CaseAllocationServiceImpl.class);

    @Autowired
    private ApplyProcRepository applyProcRepository;

    @Autowired
    private CustCaseInfoRepository custCaseInfoRepository;

    @Autowired
    private CaseAllotProcRepository caseAllotProcRepository;

    @Autowired
    private InterfaceServiceImpl interfaceServiceImpl;

    @Autowired
    private MessageTemplateRepository messageTemplateRepository;

    @Autowired
    private UserListRepository userListRepository;

    @Override
    public Map<String, String> getCityList() {
        Map<String, String> result = new LinkedHashMap<String, String>();
        StringBuffer sql = new StringBuffer();
        User user = UserUtil.getCurrUser();
        String userType = user.getUserType();
        sql.append("select distinct CITY_CODE,CITY_NAME");
        sql.append(" from FB_CUST_CASE_INFO");
        if("S".equals(userType) || "Z".equals(userType)) {
            sql.append(" where AGENT_USER_CODE = '"+user.getUserCode()+ "'");
        } else if("C".equals(userType)) {
            sql.append(" where AGENT_TEAM_CODE = '"+user.getUserCode()+ "'");
        }
        QueryPage<Object[]> queryPage = findBySQL(sql);
        if(queryPage != null) {
            for (Object[] ob : queryPage.getContent()) {
                String cityCode = ob[0] == null ? "" : ob[0].toString();
                String cityName = ob[1] == null ? "" : ob[1].toString();
                result.put(cityCode, cityName);
            }
        }
        return result;
    }

    @Override
    public Map<String, String> getUserList() {
        return getUserList(UserUtil.getCurrUser().getUserGroup(),"'S','Z'");
    }

    @Override
    public List<Object[]> queryCase(String allocationStatus,String cityCode,String areaCode,String compCode
            ,String compName,String agent,String startDate,String endDate) {
        QueryPage<Object[]> queryPage = "C".equals(UserUtil.getCurrUser().getUserType()) ? queryUserCase(allocationStatus,
                cityCode,areaCode,compCode,compName,agent,startDate.trim(),endDate.trim()) : queryTeamCase(allocationStatus,
                cityCode,areaCode,compCode,compName,agent,startDate.trim(),endDate.trim());
        return queryPage.getContent();
    }

    @Override
    public Map<String, String> getAreaList() {
        Map<String, String> result = new LinkedHashMap<String, String>();
        StringBuffer sql = new StringBuffer();
        sql.append(" select distinct ul.USER_AREA, ul.USER_AREA_NAME ");
        sql.append("   from USER_LIST ul");
        sql.append("   left join VIEW_DATA_DICT_ITEM ddi on ddi.ITEM_CODE = ul.USER_AREA and ddi.DICT_ID = 'ARRAY_SCHEMA_SORT_AREA'");
        sql.append("  where IFNULL(ul.USER_AREA, ' ') != ' ' ");
        sql.append("    and IFNULL(ul.USER_AREA, '') not in ('188J', '188K', '188L') ");
        sql.append("  order by cast(ifnull(ddi.ITEM_REMARK,999) as signed integer)");
        List<Object[]> context = findBySQL(sql).getContent();
        for(Object[] ob : context) {
            String areaCode = ob[0] == null ? "" : ob[0].toString();
            String areaName = ob[1] == null ? "" : ob[1].toString();
            result.put(areaCode,areaName);
        }
        return result;
    }

    @Override
    public List<Object> findAllocationUser(String key,String type) {
        Map<String, String> result = new LinkedHashMap<String, String>();
        List<Object> list = new ArrayList<>();
        result = "area".equals(type) ? getGroupList(key) : getUserList(key,"'C'");
        for(Map.Entry<String,String> participant : result.entrySet()) {
            list.add(participant);
        }
        return list;
    }

    @Override
    public boolean saveAllocation(String userCode, String caseList) {
        List<String> cases = Arrays.asList(caseList.split(","));
        User user = UserUtil.getCurrUser();
        String userType = UserUtil.getCurrUser().getUserType();
        String userName ="";
        StringBuffer sql = new StringBuffer();
        sql.append(" select distinct USER_NAME,USER_CODE ");
        sql.append("   from USER_LIST ");
        sql.append("  where USER_CODE = '"+ userCode + "'");
        List<Object[]> context = findBySQL(sql).getContent();
        if(context!= null && context.size()>0) {
            userName = context.get(0)[0].toString();
        }
        List<UserList> userList = userListRepository.findByUserCode(userCode);
        if(userList!= null && userList.size()>0) {
            userName = userList.get(0).getUserName();
        }

        for(String caseNum : cases) {
            CustCaseInfo custCaseInfo = custCaseInfoRepository.findByCaseInfoId(caseNum);
            ApplyProc applyProc = new ApplyProc();
            List<ApplyProc> applyProcList = applyProcRepository.findByCompCode(custCaseInfo.getCompCode());
            if(applyProcList == null || applyProcList.size()<=0) {
                applyProc.setCompCode(custCaseInfo.getCompCode());
                applyProc.setCompName(custCaseInfo.getCompName());
                applyProc.setApplyType("P0001".equals(custCaseInfo.getProdName()) ? "01" : "02");//商戶貸
                applyProc.setApplyProdCode(custCaseInfo.getProdName());
                applyProc.setApplyProdName(custCaseInfo.getProdTypeName());
                applyProc.setProcStatus("01");
                applyProc.setCreateUser(user.getUserCode());
                applyProc.setCreateTime(new Date());
                applyProc = applyProcRepository.saveAndFlush(applyProc);
            } else {
                applyProc = applyProcList.get(0);
            }
            custCaseInfo.setApplyProcNum(applyProc.getAppyProcNum());
            custCaseInfo.setAllotType("2");
            custCaseInfo.setAllotStatus("C".equals(userType) ? "3" : "2");
            if("C".equals(userType)) {
                custCaseInfo.setAllotTime(new Date());
                custCaseInfo.setAgentUserCode(userCode);
                custCaseInfo.setAgentUserName(userName);
                custCaseInfo.setAgentTeamCode(user.getUserCode());
                custCaseInfo.setAgentTeamName(user.getUserName());
            } else {
                custCaseInfo.setAllotGroupCode(userCode);//设置分配到组
                custCaseInfo.setAllotUser(user.getUserCode());//用户管理员
                custCaseInfo.setAllotTeamTime(new Date());
            }
            custCaseInfo = custCaseInfoRepository.saveAndFlush(custCaseInfo);
            //保存案件分配歷程表
            saveCaseAllotProc(custCaseInfo,"C".equals(userType) ? "2" : "1",userCode,userName);
        }
        //发送消息
        //消息接受者
        List<String> acceptUserList = new ArrayList<>();
        if("C".equals(userType)) {
            acceptUserList.add(userCode);
        } else {
            List<UserList> teamList = userListRepository.findTeamUser(userCode);
            for (UserList acceptUser : teamList)
                acceptUserList.add(acceptUser.getUserCode());
        }
        for(String acceptUser : acceptUserList) {
            WebSendMessageRq message = new WebSendMessageRq();
            MessageTemplate messageTemplate = messageTemplateRepository.findByTemplateId("C".equals(userType) ? "temp014" : "temp013");
            message.setMessageType("03");//系统通知
            message.setMessageTitle(messageTemplate.getTemplateTheme());
            message.setMessageContext(messageTemplate.getTemplateContent());
            message.setSendUser(user.getUserCode());
            message.setAcceptUser(acceptUser);
            message.setIsPush("0");
            interfaceServiceImpl.webSendMessage(message);
        }
        return true;
    }

    @Override
    public List<Map<String, String>> choseCity(String cityCode) {
        List<Map<String, String>>  result = new ArrayList<>();
        StringBuffer sql = new StringBuffer();
        String userType = UserUtil.getCurrUser().getUserType();
        String userCode = UserUtil.getCurrUser().getUserCode();
        sql.append("select distinct AREA_CODE,AREA_NAME");
        sql.append("  from FB_CUST_CASE_INFO");
        sql.append(" where 1=1 ");
        if(StringUtils.isNoneBlank(cityCode)) {
            sql.append(" and CITY_CODE = '" + cityCode + "'");
        }
        if("S".equals(userType) || "Z".equals(userType)) {
            sql.append(" and AGENT_USER_CODE = '"+userCode+ "'");
        } else if("C".equals(userType)) {
            sql.append(" and AGENT_TEAM_CODE = '"+userCode+ "'");
        }
        QueryPage<Object[]> queryPage = findBySQL(sql);
        if(queryPage != null) {
            for (Object[] ob : queryPage.getContent()) {
                Map<String, String> area = new HashMap<>();
                String areaCode = ob[0] == null ? "" : ob[0].toString();
                String areaName = ob[1] == null ? "" : ob[1].toString();
                area.put(areaCode, areaName);
                result.add(area);
            }
        }
        return result;
    }

    @Override
    public boolean saveBlackList(String remark, String caseList) {
        boolean returnResult = true;
        try{
            List<String> cases = Arrays.asList(caseList.split(","));
            for(String caseNum : cases) {
                CustCaseInfo custCaseInfo = custCaseInfoRepository.findByCaseInfoId(caseNum);
                custCaseInfo.setAllotStatus("4");
                custCaseInfo.setBlackRemark(remark);
                custCaseInfoRepository.saveAndFlush(custCaseInfo);
                //將該筆的流程設為已完成
                ApplyProc applyProc = applyProcRepository.findAppyProcNum(custCaseInfo.getApplyProcNum());
                applyProc.setApplyStatus("03");
                applyProcRepository.saveAndFlush(applyProc);
                //保存案件分配歷程表
                saveCaseAllotProc(custCaseInfo,"4","","");
            }
        } catch (Exception e) {
            returnResult = false;
            e.printStackTrace();
        }
        return returnResult;
    }

    @Override
    public AjaxResut caseRecycling(String caseList) {
        AjaxResut result = new AjaxResut();
        result.setReturnCode(true);
        List<String> cases = Arrays.asList(caseList.split(","));
        List<ApplyProc> applyProcList = applyProcRepository.findInProcessCase(cases);
        if(applyProcList!= null && applyProcList.size()>0) {
            result.setReturnCode(false);
            String compCodeList = "";
            for(ApplyProc applyProc : applyProcList) {
                compCodeList += applyProc.getCompCode() + ";";
            }
            result.setReturnMessage(compCodeList);
            return result;
        }
        for(String caseNum : cases) {
            CustCaseInfo custCaseInfo = custCaseInfoRepository.findByCaseInfoId(caseNum);
            custCaseInfo.setAgentUserCode("");
            custCaseInfo.setAgentUserName("");
            custCaseInfo.setAgentTeamCode("");
            custCaseInfo.setAgentTeamName("");
            custCaseInfo.setAllotGroupCode("");
            custCaseInfo.setAllotTime(null);
            custCaseInfo.setAllotStatus("1");
            saveCaseAllotProc(custCaseInfo,"3","","");
            custCaseInfoRepository.saveAndFlush(custCaseInfo);
        }
        return result;
    }

    @Override
    public CustCaseInfo showCaseDetail(String caseNum) {
        return custCaseInfoRepository.findByCaseInfoId(caseNum);
    }

    @Override
    public QueryPage<Object[]> queryPendingCase(String cityCode, String areaCode, String compCode, String compName, String allocationStartDate, String allocationEndDate, String applyStartDate, String applyEndDate,String handleStartDate,String handleEndDate,String handleFlag,String allocationType,QueryPage<Object[]> curPage) {
        StringBuffer sql = new StringBuffer();
        String userType  = UserUtil.getCurrUser().getUserType();
        String allotStatus =  null;//分配狀態
        String allotTime = "S".equals(userType) || "Z".equals(userType) || "1".equals(allocationType) ? "ALLOT_TIME" : "ALLOT_TEAM_TIME";//分配时间
        switch (userType) {
            case "S": allotStatus = "'3'"; break;
            case "Z": allotStatus = "'3'"; break;
            case "C": allotStatus = "'3'"; break;
            default:    allotStatus = "'2','3'";
        }
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append("select DATE_FORMAT("+ allotTime +",'%Y-%m-%d %H:%i'),DATE_FORMAT(APPLY_TIME,'%Y-%m-%d %H:%i'),COMP_CODE,COMP_NAME,CITY_NAME,AREA_NAME,CASE_INFO_ID,HANDLE_FLAG,DATE_FORMAT(HANDLE_TIME,'%Y-%m-%d %H:%i'), ");
        sql.append("       ifnull(ul.USER_GROUP_NAME,ul2.USER_GROUP_NAME),cci.AGENT_USER_NAME ");
        sql.append("  from FB_CUST_CASE_INFO cci ");
        sql.append("  left join USER_LIST ul on ul.USER_CODE = cci.AGENT_USER_CODE ");
        sql.append("  left join USER_LIST ul2 on ul2.USER_GROUP = cci.ALLOT_GROUP_CODE ");
        sql.append(" where ifnull(ALLOT_STATUS,'1') in (" + allotStatus + ")");
        if("S".equals(userType) || "Z".equals(userType)) {
            sql.append("   and AGENT_USER_CODE =:userCode ");
            params.put("userCode", UserUtil.getCurrUser().getUserCode());
            sql.append("   and ifnull(HANDLE_FLAG,'1') =:handleFlag ");
            params.put("handleFlag", handleFlag);
        } else if("C".equals(userType)){
            sql.append("   and AGENT_TEAM_CODE =:userCode ");
            params.put("userCode", UserUtil.getCurrUser().getUserCode());
        }
        if (StringUtils.isNoneBlank(cityCode)) {
            sql.append(" and CITY_CODE =:cityCode");
            params.put("cityCode", cityCode);
        }
        if (StringUtils.isNoneBlank(areaCode)) {
            sql.append(" and AREA_CODE =:areaCode");
            params.put("areaCode", areaCode);
        }
        if (StringUtils.isNoneBlank(compCode)) {
            sql.append(" and COMP_CODE =:compCode");
            params.put("compCode", compCode);
        }
        if (StringUtils.isNoneBlank(compName)) {
            sql.append(" and COMP_NAME like '%" + compName + "%'");
        }
        if (StringUtils.isNoneBlank(allocationStartDate)) {
            sql.append(" and DATE_FORMAT("+ allotTime +",'%Y-%m-%d %H:%i') >= STR_TO_DATE('" + allocationStartDate + "','%Y/%m/%d %H:%i')");
        }
        if (StringUtils.isNoneBlank(allocationEndDate)) {
            sql.append(" and DATE_FORMAT("+ allotTime +",'%Y-%m-%d %H:%i') <= STR_TO_DATE('" + allocationEndDate + " 23:59','%Y/%m/%d %H:%i')");
        }
        if (StringUtils.isNoneBlank(applyStartDate)) {
            sql.append(" and DATE_FORMAT(APPLY_TIME,'%Y-%m-%d %H:%i') >= STR_TO_DATE('" + applyStartDate + "','%Y/%m/%d %H:%i')");
        }
        if (StringUtils.isNoneBlank(applyEndDate)) {
            sql.append(" and DATE_FORMAT(APPLY_TIME,'%Y-%m-%d %H:%i') <= STR_TO_DATE('" + applyEndDate + " 23:59','%Y/%m/%d %H:%i')");
        }
        if (StringUtils.isNoneBlank(handleStartDate)) {
            sql.append(" and DATE_FORMAT(HANDLE_TIME,'%Y-%m-%d %H:%i') >= STR_TO_DATE('" + handleStartDate + "','%Y/%m/%d %H:%i')");
        }
        if (StringUtils.isNoneBlank(handleEndDate)) {
            sql.append(" and DATE_FORMAT(HANDLE_TIME,'%Y-%m-%d %H:%i') <= STR_TO_DATE('" + handleEndDate + " 23:59','%Y/%m/%d %H:%i')");
        }
        if (StringUtils.isNoneBlank(allocationType)) {
            sql.append(" and ALLOT_TYPE =:allocationType");
            params.put("allocationType", allocationType);
        }
        sql.append(" group by ul2.USER_GROUP,CASE_INFO_ID ");
        sql.append(" order by "+ allotTime +" desc,APPLY_TIME desc,HANDLE_TIME desc ");
        return findBySQL(sql,curPage,params);
    }

    @Override
    public List<Object[]> exportCaseData(String allocationType,String cityCode, String areaCode, String compCode, String compName, String allocationStartDate, String allocationEndDate, String applyStartDate, String applyEndDate){
        StringBuffer sql = new StringBuffer();
        Map<String, Object> params = new HashMap<String, Object>();
        String userType  = UserUtil.getCurrUser().getUserType();
        String allotTime = "S".equals(userType) || "Z".equals(userType) || "1".equals(allocationType) ? "ALLOT_TIME" : "ALLOT_TEAM_TIME";//分配时间
        sql.append("select DATE_FORMAT("+allotTime+",'%Y-%m-%d %H:%i'),DATE_FORMAT(APPLY_TIME,'%Y-%m-%d %H:%i'),ifnull(ul.USER_GROUP_NAME,ul2.USER_GROUP_NAME),");
        sql.append("       "+ ("2".equals(allocationType) ? "AGENT_USER_NAME," : "") +"COMP_CODE,COMP_NAME");
        sql.append("  from FB_CUST_CASE_INFO cci ");
        sql.append("  left join USER_LIST ul on ul.USER_CODE = cci.AGENT_USER_CODE ");
        sql.append("  left join USER_LIST ul2 on ul2.USER_GROUP = cci.ALLOT_GROUP_CODE ");
        sql.append(" where ifnull(ALLOT_STATUS,'1') in ('2','3')");
        if (StringUtils.isNoneBlank(allocationType)) {
            sql.append(" and ALLOT_TYPE =:allocationType");
            params.put("allocationType", allocationType);
        }
        if (StringUtils.isNoneBlank(cityCode)) {
            sql.append(" and CITY_CODE =:cityCode");
            params.put("cityCode", cityCode);
        }
        if (StringUtils.isNoneBlank(areaCode)) {
            sql.append(" and AREA_CODE =:areaCode");
            params.put("areaCode", areaCode);
        }
        if (StringUtils.isNoneBlank(compCode)) {
            sql.append(" and COMP_CODE =:compCode");
            params.put("compCode", compCode);
        }
        if (StringUtils.isNoneBlank(compName)) {
            sql.append(" and COMP_NAME like '%" + compName + "%'");
        }
        if (StringUtils.isNoneBlank(allocationStartDate)) {
            sql.append(" and DATE_FORMAT("+allotTime+",'%Y-%m-%d %H:%i') >= STR_TO_DATE('" + allocationStartDate + "','%Y/%m/%d %H:%i')");
        }
        if (StringUtils.isNoneBlank(allocationEndDate)) {
            sql.append(" and DATE_FORMAT("+allotTime+",'%Y-%m-%d %H:%i') <= STR_TO_DATE('" + allocationEndDate + " 23:59','%Y/%m/%d %H:%i')");
        }
        if (StringUtils.isNoneBlank(applyStartDate)) {
            sql.append(" and DATE_FORMAT(APPLY_TIME,'%Y-%m-%d %H:%i') >= STR_TO_DATE('" + applyStartDate + "','%Y/%m/%d %H:%i')");
        }
        if (StringUtils.isNoneBlank(applyEndDate)) {
            sql.append(" and DATE_FORMAT(APPLY_TIME,'%Y-%m-%d %H:%i') <= STR_TO_DATE('" + applyEndDate + " 23:59','%Y/%m/%d %H:%i')");
        }
        sql.append(" group by ul2.USER_GROUP,CASE_INFO_ID ");
        sql.append(" order by ALLOT_TEAM_TIME desc,APPLY_TIME desc,HANDLE_TIME desc ");
        QueryPage<Object[]> page = findBySQL(sql,params);
        return  page.getContent();
    }

    @Override
    public boolean handleCase(String caseNum) {
        boolean flag = true;
        try {
            CustCaseInfo custCaseInfo = custCaseInfoRepository.findByCaseInfoId(caseNum);
            custCaseInfo.setHandleFlag("2");//已處理
            custCaseInfo.setHandleTime(new Date());
            custCaseInfoRepository.saveAndFlush(custCaseInfo);
        } catch (Exception e) {
            flag = false;
            logger.warn(e.getMessage());
        }
        return flag;
    }

    //保存案件分配歷程表
    public void saveCaseAllotProc(CustCaseInfo custCaseInfo,String allotType,String agentCode,String angetName) {
        CaseAllotProc caseAllotProc = new CaseAllotProc();
        caseAllotProc.setCaseInfoId(custCaseInfo.getCaseInfoId());
        caseAllotProc.setCompCode(custCaseInfo.getCompCode());
        caseAllotProc.setCompName(custCaseInfo.getCompName());
        caseAllotProc.setAgentUserCode(agentCode);
        caseAllotProc.setAgentUserName(angetName);
        caseAllotProc.setAllotUser(UserUtil.getCurrUser().getUserCode());
        caseAllotProc.setAllotType(allotType);
        caseAllotProc.setAllotTime(new Date());
        String showOrder = caseAllotProcRepository.getMaxShowOrder(custCaseInfo.getCaseInfoId());
        int order = StringUtils.isNoneBlank(showOrder) ? (Integer.valueOf(showOrder) + 1) : 1;
        caseAllotProc.setShowOrder(order + "");
        caseAllotProcRepository.saveAndFlush(caseAllotProc);
    }

    //查詢區底下所有組
    public LinkedHashMap<String,String> getGroupList(String areaCode) {
        LinkedHashMap<String,String> result = new LinkedHashMap<String,String>();
        StringBuffer sql = new StringBuffer();
        sql.append(" select distinct ul.USER_GROUP, ul.USER_GROUP_NAME ");
        sql.append("   from USER_LIST ul");
        sql.append("   left join VIEW_DATA_DICT_ITEM ddi on ddi.ITEM_CODE = ul.USER_GROUP and ddi.DICT_ID = ul.USER_AREA");
        sql.append("  where ifnull(ul.USER_AREA, '') not in ('188J', '188K', '188L') ");
        sql.append("    and ul.USER_TYPE not in('A','Z') ");
        sql.append("    and ifnull(ul.USER_GROUP,'') != '' ");
        sql.append("    and ul.USER_AREA = '" + areaCode + "'");
        sql.append("  order by cast(ifnull(ddi.ITEM_REMARK,999) as signed integer) ");
        List<Object[]> context = findBySQL(sql).getContent();
        for(Object[] ob : context) {
            String groupCode = ob[0] == null ? "" : ob[0].toString();
            String groupName = ob[1] == null ? "" : ob[1].toString();
            result.put(groupCode,groupName);
        }
        return result;
    }

    //查詢組底下的用戶
    public LinkedHashMap<String,String> getUserList(String groupCode,String userType) {
        LinkedHashMap<String,String> result = new LinkedHashMap<String,String>();
        StringBuffer sql = new StringBuffer();
        sql.append(" select distinct ul.USER_CODE,ul.USER_NAME ");
        sql.append("   from USER_LIST ul");
        sql.append("   left join VIEW_DATA_DICT_ITEM ddi on ddi.ITEM_CODE = ul.USER_GROUP and ddi.DICT_ID = ul.USER_AREA");
        sql.append("  where IFNULL(ul.USER_AREA, '') not in ('188J', '188K', '188L') ");
        sql.append("    and  ul.USER_TYPE in(" + userType + ")");
        sql.append("    and ifnull(ul.LEAVE_FLAG,'0') != '1'");
        sql.append("    and IFNULL(ul.USER_GROUP, '')  in (select '" +groupCode + "' from dual ");
        if("'S','Z'".equals(userType)) {
            sql.append("  union select ul.USER_GROUP from FB_ENTRUST fe");
            sql.append("  inner join USER_LIST ul on fe.PRINCIPAL_CODE = ul.USER_CODE ");
            sql.append("  where locate('CMLF_TEA',fe.ENTRUST_JURISDICTION) and fe.ENTRUST_USER_CODE = '" + UserUtil.getCurrUser().getUserCode() + "'");
            sql.append("    and DATE_FORMAT(NOW(),'%Y-%m-%d %H:%i') >= DATE_FORMAT(fe.ENTRUST_START_TIME,'%Y-%m-%d %H:%i') and DATE_FORMAT(NOW(),'%Y-%m-%d %H:%i') < DATE_FORMAT(fe.ENTRUST_END_TIME,'%Y-%m-%d %H:%i')");
        }
        sql.append(" )order by cast(ifnull(ddi.ITEM_REMARK,999) as signed integer), ul.USER_CODE ");
        List<Object[]> context = findBySQL(sql).getContent();
        for(Object[] ob : context) {
            String userCode = ob[0] == null ? "" : ob[0].toString();
            String userName = ob[1] == null ? "" : ob[1].toString();
            if(result.get(userCode) == null)
                result.put(userCode,userName);
        }
        return result;
    }

    public QueryPage<Object[]> queryTeamCase(String allocationStatus,String cityCode,String areaCode,String compCode
            ,String compName,String agent,String startDate,String endDate) {
        StringBuffer sql = new StringBuffer();
        Map<String, Object> params = new HashMap<String, Object>();
        String allotStatus =  "0".equals(allocationStatus) ? "'1'" : "'2','3'";//分配狀態
        sql.append("select cci.CASE_INFO_ID,DATE_FORMAT(cci.APPLY_TIME,'%Y-%m-%d %H:%i'),cci.COMP_CODE,cci.COMP_NAME,cci.CITY_NAME,cci.AREA_NAME, ");
        sql.append("       DATE_FORMAT(cci.ALLOT_TEAM_TIME,'%Y-%m-%d %H:%i'),GROUP_CONCAT(ul.USER_NAME SEPARATOR '/') ");
        sql.append("  from FB_CUST_CASE_INFO cci ");
        sql.append("  left join USER_LIST ul on cci.ALLOT_GROUP_CODE = ul.USER_GROUP and ul.USER_TYPE = 'C'");
        sql.append(" where ifnull(cci.ALLOT_STATUS,'1') in (" + allotStatus + ")");
        if (StringUtils.isNoneBlank(cityCode)) {
            sql.append(" and cci.CITY_CODE =:cityCode");
            params.put("cityCode", cityCode);
        }
        if (StringUtils.isNoneBlank(areaCode)) {
            sql.append(" and cci.AREA_CODE =:areaCode");
            params.put("areaCode", areaCode);
        }
        if (StringUtils.isNoneBlank(compCode)) {
            sql.append(" and cci.COMP_CODE =:compCode");
            params.put("compCode", compCode);
        }
        if (StringUtils.isNoneBlank(compName)) {
            sql.append(" and cci.COMP_NAME like '%" + compName + "%'");
        }
        if (StringUtils.isNoneBlank(agent)) {
            sql.append(" and cci.AGENT_TEAM_NAME like '%" + agent + "%'");
        }
        if (StringUtils.isNoneBlank(startDate)) {
            sql.append(" and DATE_FORMAT(cci.ALLOT_TEAM_TIME,'%Y-%m-%d %H:%i') >= STR_TO_DATE('" + startDate + "','%Y/%m/%d %H:%i')");
        }
        if (StringUtils.isNoneBlank(endDate)) {
            sql.append(" and DATE_FORMAT(cci.ALLOT_TEAM_TIME,'%Y-%m-%d %H:%i') <= STR_TO_DATE('" + endDate + " 23:59','%Y/%m/%d %H:%i')");
        }
        sql.append(" group by cci.CASE_INFO_ID ");
        sql.append(" order by cci.APPLY_TIME desc");
        return findBySQL(sql, params);
    }

    public QueryPage<Object[]> queryUserCase(String allocationStatus,String cityCode,String areaCode,String compCode
            ,String compName,String agent,String startDate,String endDate) {
        StringBuffer sql = new StringBuffer();
        Map<String,Object> params =  new HashMap<String,Object>();
        User user = UserUtil.getCurrUser();
        sql.append("select CASE_INFO_ID,DATE_FORMAT(APPLY_TIME,'%Y-%m-%d %H:%i'),COMP_CODE,COMP_NAME,CITY_NAME,AREA_NAME, ");
        sql.append("   DATE_FORMAT(ALLOT_TIME,'%Y-%m-%d %H:%i'),AGENT_USER_NAME ");
        sql.append("  from FB_CUST_CASE_INFO ");
        sql.append(" where ifnull(ALLOT_STATUS,'1') in (" + ("0".equals(allocationStatus) ? "'2'" : "'3'") + ")");
        if(StringUtils.isNoneBlank(cityCode)) {
            sql.append(" and CITY_CODE =:cityCode");
            params.put("cityCode",cityCode);
        }
        if(StringUtils.isNoneBlank(areaCode)) {
            sql.append(" and AREA_CODE =:areaCode");
            params.put("areaCode",areaCode);
        }
        if(StringUtils.isNoneBlank(compCode)) {
            sql.append(" and COMP_CODE =:compCode");
            params.put("compCode",compCode);
        }
        if(StringUtils.isNoneBlank(compName)) {
            sql.append(" and COMP_NAME like '%" + compName + "%'");
        }
        if(StringUtils.isNoneBlank(agent)) {
            sql.append(" and AGENT_USER_NAME like '%" + agent + "%'");
        }
        if(StringUtils.isNoneBlank(startDate)) {
            sql.append(" and DATE_FORMAT(ALLOT_TIME,'%Y-%m-%d %H:%i') >= STR_TO_DATE('" + startDate + "','%Y/%m/%d %H:%i')");
        }
        if(StringUtils.isNoneBlank(endDate)) {
            sql.append(" and DATE_FORMAT(ALLOT_TIME,'%Y-%m-%d %H:%i') <= STR_TO_DATE('" + endDate + " 23:59','%Y/%m/%d %H:%i')");
        }
        if("1".equals(allocationStatus)) {
            sql.append(" and AGENT_TEAM_CODE in (select '" + user.getUserCode() + "' from dual ");
            sql.append(" union select PRINCIPAL_CODE from FB_ENTRUST where locate(ENTRUST_JURISDICTION,'CMLF_TEA') and ENTRUST_USER_CODE = '" + user.getUserCode() + "')");
            sql.append(" and DATE_FORMAT(NOW(),'%Y-%m-%d %H:%i') >= DATE_FORMAT(ENTRUST_START_TIME,'%Y-%m-%d %H:%i') and DATE_FORMAT(NOW(),'%Y-%m-%d %H:%i') < DATE_FORMAT(ENTRUST_END_TIME,'%Y-%m-%d %H:%i') ");
        } else {
            sql.append(" and ALLOT_GROUP_CODE in (select '" + user.getUserGroup() + "' from dual union ");
            sql.append(" select ul.USER_GROUP from FB_ENTRUST fe");
            sql.append("  inner join USER_LIST ul on fe.PRINCIPAL_CODE = ul.USER_CODE ");
            sql.append("  where locate('CMLF_TEA',fe.ENTRUST_JURISDICTION) and fe.ENTRUST_USER_CODE = '" + user.getUserCode() + "'");
            sql.append("    and DATE_FORMAT(NOW(),'%Y-%m-%d %H:%i') >= DATE_FORMAT(fe.ENTRUST_START_TIME,'%Y-%m-%d %H:%i') and DATE_FORMAT(NOW(),'%Y-%m-%d %H:%i') < DATE_FORMAT(fe.ENTRUST_END_TIME,'%Y-%m-%d %H:%i') )");
        }
        sql.append(" order by APPLY_TIME desc");
        return findBySQL(sql,params);
    }
}
