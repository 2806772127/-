package com.fb.goldencudgel.auditDecisionSystem.service;


import com.comwave.core.data.domain.QueryPage;
import com.fb.goldencudgel.auditDecisionSystem.domain.custCaseInfo.CustCaseInfo;
import com.fb.goldencudgel.auditDecisionSystem.model.AjaxResut;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

public interface ICaseAllocationService {

    //获取所有的城市
    Map<String,String> getCityList();

    //获取所有的城市
    Map<String,String> getUserList();

    //查询待分配的案件
    List<Object[]> queryCase(String allocationStatus, String cityCode, String areaCode, String compCode
            , String compName, String agent, String startDate, String endDate);

    //查询所有区
    Map<String, String> getAreaList();

    List<Object> findAllocationUser(String key, String type);

    boolean saveAllocation(String userCode, String caseList);

    List<Map<String, String>> choseCity(String cityCode);

    boolean saveBlackList(String remark, String caseList);

    AjaxResut caseRecycling(String caseList);

    CustCaseInfo showCaseDetail(String caseNum);

    QueryPage<Object[]> queryPendingCase(String cityCode, String areaCode, String compCode, String compName, String allocationStartDate,String allocationEndDate,
                                         String applyStartDate, String applyEndDate,String handleStartDate,String handleEndDate,String handleFlag,String allocationType, QueryPage<Object[]> curPage);

    boolean handleCase(String caseNum);

    public List<Object[]> exportCaseData(String allocationType,String cityCode, String areaCode, String compCode, String compName, String allocationStartDate, String allocationEndDate, String applyStartDate, String applyEndDate);

}
