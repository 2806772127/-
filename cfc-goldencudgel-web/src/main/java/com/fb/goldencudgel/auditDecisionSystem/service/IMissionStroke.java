package com.fb.goldencudgel.auditDecisionSystem.service;

import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;

import com.comwave.core.data.domain.QueryPage;
import com.fb.goldencudgel.auditDecisionSystem.domain.appointMentRecord.FbAppointmentRecord;
import com.fb.goldencudgel.auditDecisionSystem.model.AjaxResut;
import com.fb.goldencudgel.auditDecisionSystem.model.MissionStrokeModel;

/**
 * 任务行程
 * @Auther hu
 */
public interface IMissionStroke {

   public Map<String, String> getAreaList(String userCode);

   public Map<String, String> getGroupList(String areaCode);

   public Map<String, String> getUserList(String areaCode,String groupCode);

   public List<FbAppointmentRecord> getAllAppointmentRecord(String areaCode,String groupCode,String userCode);

   public QueryPage<Object[]> fingTravelSchedule(QueryPage<Object[]> queryPage,String userCode);

   //public AjaxResut saveStroke(MissionStrokeModel missionStrokeModel);

   public QueryPage<Object[]> searchStroke(String areaCode, String groupCode , String userCode, String comName,String appointmentDate);
   
   public QueryPage<Object[]> searchStrokeByMonth(String areaCode, String groupCode, String userCode, String comName, String appointmentDate);
   
   public Map<String,String> findRate(Model model,String prodCode);
   
   public Map<String, Object> delMissionStroke(String compilationNo, String trandId, String appointmentType, String createUser);
   
   public QueryPage<Object[]> queryByConditions(String compilationNo, String trandId, String appointmentType);
   
   public QueryPage<Object[]> queryDictByConditions(List<String> paramList);
   
   public QueryPage<Object[]> queryCityAndDistrictAndStreet();
   
   public AjaxResut updateStroke(MissionStrokeModel missionStrokeModel);

   public AjaxResut saveStroke(MissionStrokeModel missionStrokeModel, String caseNo);
   //商品貸
   public AjaxResut updateStrokeSpd(MissionStrokeModel missionStrokeModel, String mobile, String email);
   
   public AjaxResut saveStrokeSpd(MissionStrokeModel missionStrokeModel, String mobile, String email,String caseNo); 
}
