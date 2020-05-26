package com.fb.goldencudgel.auditDecisionSystem.controller.creditQues;


import com.comwave.core.data.domain.QueryPage;
import com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.FbMeasureWord;
import com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.FbQuestionRecord;
import com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.question.FbQuestionnaire;
import com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.question.FbQuestionnaireAnswer;
import com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.question.FbQuestionnaireDetail;
import com.fb.goldencudgel.auditDecisionSystem.domain.user.FbUser;
import com.fb.goldencudgel.auditDecisionSystem.repository.FbQuestionRecordRepository;
import com.fb.goldencudgel.auditDecisionSystem.schema.fileToRuiyang.FileToRuiyangResponse;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.CreditQuesImpl;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.InterfaceServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.MeasureWordServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.SaveCreditReportServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.fb.goldencudgel.auditDecisionSystem.utils.NumberUtil.formatMoney;

@Controller
@RequestMapping("/CreditQues")
public class CreditQuesController {

    private final Logger logger = LoggerFactory.getLogger(CreditQuesController.class);
    private static final Integer PAGE_SIZE = 20;

    @Autowired
    private CreditQuesImpl creditQuesImpl;

    @Autowired
    private SaveCreditReportServiceImpl saveCreditReportService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAutoGrowCollectionLimit(2000);
    }

    /**
     * 征信问卷报告查询
     */
    @RequestMapping("/viewCreditQues")
    public String quesIndex(Model model, String backFlag) {
        model.addAttribute("backFlag", backFlag);
        return "creditQues/viewCreditQues";
    }


    @RequestMapping("/queryCreditQues")
    /*public String queryCreditQues(Model model, String compilationNo, String companyName, String questionnaireName, String startDate, String endDate,
                                  @RequestParam(required = false, defaultValue = "1") Integer curPage) {*/
    public String queryCreditQues(Model model, String compilationNo, String companyName, String questionnaireName, String startDate, String endDate,String creditName,
            @RequestParam(required = false, defaultValue = "1") Integer curPage) throws ParseException {
        try {
            if (StringUtils.isNoneBlank(startDate)) {
                startDate = startDate.replace("/", "-").trim();
            }
            if (StringUtils.isNoneBlank(endDate)) {
                endDate = endDate.replace("/", "-").trim();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date dd = df.parse(endDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dd);
                calendar.add(Calendar.DAY_OF_MONTH, 1);//加一天
                System.out.println("增加一天之后：" + df.format(calendar.getTime()));
                endDate=df.format(calendar.getTime());
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        }

        QueryPage<Object[]> page = creditQuesImpl.findByConditions(compilationNo, companyName, questionnaireName, startDate, endDate,creditName, new QueryPage<Object[]>(curPage, PAGE_SIZE));
        //修改權限：用戶只能修改自己答的問卷。征信員非當前登錄用戶的記錄，則不顯示【修改】按鈕
        FbUser curUser = UserUtil.getCurrUser();
        String curUserCode = curUser.getUserCode();
        model.addAttribute("curUserCode",curUserCode);
        
        List<Object[]> objects  = page.getContent();
        
        for(int i=0;i<objects.size();i++){
        	Object[] object = objects.get(i);
            //问卷名称
            object[3] = creditQuesImpl.getIndustryName(object[1].toString(),object[0].toString(),object[7].toString());
        }
        model.addAttribute("page", page);
        model.addAttribute("creditQuesList", page.getContent());
        return "creditQues/creditQuesList";
    }

    @RequestMapping("/viewReport")
    public String viewReport(Model model, String measureId,String compilationNanme) {
        //
        String type = creditQuesImpl.findQuestionType(measureId);
        Map<String,List> map= saveCreditReportService.findQuestionNameAndList(measureId,type);
        List lists=map.get("lists");
        List nameList=map.get("nameList");
        //增加名称
        for (int i = 0; i <lists.size() ; i++) {
            List listes= (List) lists.get(i);
            String name= (String) nameList.get(i);
            if(i==1){
                Object[] obj= {"營收及產銷貨狀況",false,0,0,0,0,0};
                listes.add(0,obj);
            }else{
                Object[] obj= {name,false,0,0,0,0,0};
                listes.add(0,obj);
            }


        }

        model.addAttribute("nameList", nameList);
        model.addAttribute("lists", lists);

        QueryPage<Object[]> page = creditQuesImpl.findByMeasureId(measureId);
        QueryPage<Object[]> page1 = creditQuesImpl.findByMId(measureId);
        List<Object[]> listes = page.getContent();
        List<Object[]> list6 = page1.getContent();
        list6.get(0)[3]=new SimpleDateFormat("yyyy-MM-dd HH:mm").format(list6.get(0)[3]);
        list6.get(0)[4]=new SimpleDateFormat("yyyy-MM-dd HH:mm").format(list6.get(0)[4]);
        model.addAttribute("viewReportList", listes);
        model.addAttribute("reportList", list6.get(0));

        try {
            compilationNanme =java.net.URLDecoder.decode(compilationNanme,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        model.addAttribute("compilationNanme", compilationNanme);
        model.addAttribute("measureId", measureId);
        return "creditQues/viewReport";
    }


    @RequestMapping("/editReport")
   /* public String editReport(Model model, String measureId,String compilationNanme) {*/
    public String editReport(Model model, String measureId,String compilationNanme,String compilationNo,String questionnaireName,String creditName,String startDate,
    		String endDate,String description,String questionType) {
        try {
            String alertMsg = creditQuesImpl.getSchedulerBaseInfo(measureId);
            if(StringUtils.isNotBlank(alertMsg)){
                model.addAttribute("alertMsg", alertMsg);
            }
        } catch (Exception e){
            model.addAttribute("alertMsg", "請求獲取展業金資料失敗！");
            logger.error("請求獲取展業金資料失敗！"+e.getMessage(),e);
        }
        List<FbQuestionRecord> viewReportList = creditQuesImpl.findRecordByMeasureId(measureId);
        String questionnaireType = creditQuesImpl.findQuestionTypeByCompilationNo(compilationNo);
        questionnaireType = StringUtils.isBlank(questionnaireType)?"99":questionnaireType;
        if(questionnaireType.equals("01")) {
        	questionnaireType = "07";
        }else if(questionnaireType.equals("02")) {
        	questionnaireType = "08";
        }else if(questionnaireType.equals("03")) {
        	questionnaireType = "09";
        }else if(questionnaireType.equals("04")||questionnaireType.equals("99")) {
        	questionnaireType = "11";
        }else if(questionnaireType.equals("05")) {
        	questionnaireType = "10";
        }else if(questionnaireType.equals("06")) {
        	questionnaireType = "12";
        }
     
        List<String> questionIds = new ArrayList<>();
        questionIds.add("04");
        questionIds.add(questionnaireType);
        questionIds.add("05");
        questionIds.add("06");
        questionIds.add("13");
        
//        // 测试时加上以下类型  做完后删除
//        questionIds.add("00");
//        questionIds.add("01");
//        questionIds.add("02");
//        questionIds.add("03");
        List<FbQuestionnaire> lstFbQuestionnaire = creditQuesImpl.findTypes(questionIds);
        //答题记录信息
//        model.addAttribute("viewReportList", viewReportList);
        //头部信息
  
        model.addAttribute("questionnaireName", questionnaireName);
        model.addAttribute("creditName", creditName);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("compilationNo", compilationNo);
        model.addAttribute("description", description);
        model.addAttribute("questionType", questionType);
        model.addAttribute("questionnaireType", questionnaireType);
        model.addAttribute("compilationNanme", compilationNanme);
       
        // 全部问题的list
        List<FbQuestionnaireDetail> questionnaireList = creditQuesImpl.ques2ListDetail(lstFbQuestionnaire);
        model.addAttribute("questionnaireList", questionnaireList);
        // 全部问题的map
//        model.addAttribute("questionnaireMap", creditQuesImpl.list2Map(lstFbQuestionnaire));
        //全部答题记录的map
        Map<String,FbQuestionRecord> questionRecordMap = creditQuesImpl.listRecord2Map(viewReportList,questionnaireList,measureId,compilationNanme);
        model.addAttribute("questionRecordMap",questionRecordMap);

        Map<String, FbQuestionnaireDetail> noNeedQuesIdMap = creditQuesImpl.getNoNeedQuesDetailIds(questionnaireList, questionRecordMap);
        model.addAttribute("noNeedQuesIdMap",noNeedQuesIdMap);

        return "creditQues/editReport";
    }

    @RequestMapping("/updateQuestionRecord")
    @ResponseBody
    public Map<String,Object> updateQuestionnaire(Model model, FbMeasureWord measureWord) throws UnsupportedEncodingException {
        Map<String,Object> result = new HashMap<String,Object>();
        try {
            Map<String, String> map = creditQuesImpl.updateQuestionRecord(measureWord.getRecords());
            result = fileToRuiyang(map.get("compCode"), map.get("schedulerId"));
        }catch(Exception e) {
            logger.error(e.getMessage(), e);
            result.put("success",false);
            result.put("message","更新失敗,請稍後重試");
        }
        return result;
    }
    
    @RequestMapping("/temporaryStorage")
    @ResponseBody
    public Map<String,Object> temporaryStorage(Model model, FbMeasureWord measureWord) throws UnsupportedEncodingException {
        Map<String,Object> result = new HashMap<String,Object>();
            try {
            Map<String, String> map = creditQuesImpl.temporaryStorage(measureWord.getRecords());
            String successFlag = map.get("success");
            if(successFlag!="true"){
                result.put("success",false);
                result.put("message","更新失敗,請稍後重試");
            }else{
                result.put("success",true);
                result.put("message","更新成功");
            }
        }catch(Exception e) {
            logger.error(e.getMessage(), e);
            result.put("success",false);
            result.put("message","更新失敗,請稍後重試");
        }
        return result;
    }

    private Map<String,Object> fileToRuiyang(String compCode,String schedulerId){
        Map<String,Object> result = new HashMap<String,Object>();
        try{
            //FileToRuiyang
            FileToRuiyangResponse fileToRuiyangResponse = saveCreditReportService.fileToRuiyang(schedulerId, compCode);
            if(fileToRuiyangResponse != null && StringUtils.isNotBlank(fileToRuiyangResponse.getEMSGID())){
                if("0000".equals(fileToRuiyangResponse.getEMSGID())){
                    result.put("success",true);
                    result.put("message","更新成功！");
                    logger.info("附件同步到睿陽成功！");
                }else{
                    result.put("success",false);
                    result.put("message","附件同步到睿陽失敗！"+fileToRuiyangResponse.getEMSGID()+","+fileToRuiyangResponse.getEMSGTXT());
                    logger.error("附件同步到睿陽失敗！"+fileToRuiyangResponse.getEMSGID()+","+fileToRuiyangResponse.getEMSGTXT());
                }
            }else{
                result.put("success",false);
                result.put("message","附件同步到睿陽失敗！請求失敗！");
                logger.error("附件同步到睿陽失敗！請求失敗！");
            }
        }catch (Exception e){
            result.put("success",false);
            result.put("message","附件同步到睿陽失敗！");
            logger.error("附件同步到睿陽失敗！"+e.getMessage(),e);
        }
        return result;
    }

    @RequestMapping("/chengeAnswer")
    @ResponseBody
    public Map<String,Object> chengeAnswer(Model model, String questionId,String questionnaireId,String answer,String oldAnswer,String measureId) throws UnsupportedEncodingException {

        Map<String,Object> result = new HashMap<String,Object>();
        try {
            FbQuestionnaireDetail fbQuestionnaireDetail = creditQuesImpl.findDetailById(questionId);
            if(null == fbQuestionnaireDetail){
                result.put("message","獲取當前問題失敗，暫不重繪題目！");
                result.put("isChange",false);
                return result;
            }
            //判断原有答案与现在的答案指向题目是否相同
            String currAnswerSort = null,oldAnswerSort = null;
            for (FbQuestionnaireAnswer currAnswer:fbQuestionnaireDetail.getAnswers()) {
                if (answer.equals(currAnswer.getAnswer())){
                    currAnswerSort = currAnswer.getNextQuestion();
                }
                if (oldAnswer.equals(currAnswer.getAnswer())){
                    oldAnswerSort = currAnswer.getNextQuestion();
                }
            }
            if (null != currAnswerSort && null != oldAnswerSort && oldAnswerSort.equals(currAnswerSort)){
                result.put("message","當前答案指向題目與原來相同，無需重繪！");
                result.put("isChange",false);
                return result;
            }
            String sortNo = fbQuestionnaireDetail.getSortNo();
            //判断这题是否为主问题
            if (!fbQuestionnaireDetail.getQuestionLevel().equals("1")){
                //如果为副问题则需先获取这个主问题
                FbQuestionnaireDetail fatherQuestionDetail = creditQuesImpl.findDetailsBySort(questionnaireId, fbQuestionnaireDetail.getFatherQuestion());
                if (null == fatherQuestionDetail) {
                    result.put("message", "獲取當前主問題失敗，暫不重繪題目！");
                    result.put("isChange", false);
                    return result;
                }
                sortNo = fatherQuestionDetail.getSortNo();
            }

            List<FbQuestionnaireDetail> detailAllList =  creditQuesImpl.findDetailsByQId(questionnaireId);
            List<FbQuestionnaireDetail> detailLevelOneList = new ArrayList<FbQuestionnaireDetail>();
            for (FbQuestionnaireDetail detail:detailAllList) {
                if(sortNo.equals(detail.getFatherQuestion())){
                    detailLevelOneList.add(detail);
                }
            }
            if (CollectionUtils.isEmpty(detailLevelOneList)){
                result.put("message","該問題沒有子問題，不需重繪題目！");
                result.put("isChange",false);
                return result;
            }
            List<FbQuestionnaireDetail> detailChildList = new ArrayList<FbQuestionnaireDetail>();
            List<FbQuestionnaireDetail> detailLevelList = addDetailChildList(detailAllList, detailLevelOneList);
            detailChildList.addAll(detailLevelOneList);
            detailChildList.addAll(detailLevelList);
            //删除题号小于等于当前题号的问题
            for (int i = 0; i < detailChildList.size(); i++) {
                if (Integer.valueOf(detailChildList.get(i).getSortNo()) <= Integer.valueOf(fbQuestionnaireDetail.getSortNo())){
                    detailChildList.remove(i);
                    i--;
                }
            }
            //获取要删除的子问题id与需要新增的问卷题目
            result = creditQuesImpl.getChangeAnswerMap(fbQuestionnaireDetail,answer,detailChildList,measureId);
        }catch(Exception e) {
            logger.error(e.getMessage(), e);
            result.put("isChange",false);
            result.put("message","暫不重繪題目！");
        }
        return result;
    }

    /**
     * 递归获取问题的所有子问题
     * @param detailAllList
     * @param detailLevelList
     * @return
     */
    private List<FbQuestionnaireDetail> addDetailChildList(List<FbQuestionnaireDetail> detailAllList, List<FbQuestionnaireDetail> detailLevelList){
        int addCount = 0;
        List<FbQuestionnaireDetail> detailNextLevelList = new ArrayList<FbQuestionnaireDetail>();
        for (FbQuestionnaireDetail detail:detailAllList) {
            for (FbQuestionnaireDetail detailNext:detailLevelList) {
                if(detailNext.getSortNo().equals(detail.getFatherQuestion())){
                    detailNextLevelList.add(detail);
                    addCount++;
                }
            }
        }
        if(addCount>0){
            List<FbQuestionnaireDetail> detailNextChildList = addDetailChildList(detailAllList, detailNextLevelList);
            detailNextLevelList.addAll(detailNextChildList);
        }
        return detailNextLevelList;
    }

    @RequestMapping("/downloadPdf")
    @ResponseBody
    public void downloadPdf(Model model, HttpServletResponse response, String measureId) {
        creditQuesImpl.downloadPdf(response,measureId);
    }
    public void setObject(Object[] o,String first,String second,String third){
        o[0] = first;
        o[1] = second;
        o[2] = third;
        o[3] = 1;
        o[4] = 1;
        o[5] = 0;
        o[6] = 0;
    }
    //将数据放到list中
    public void setList(String type,Object[] o,String industryType,List<List> list){//1.问卷类型 3.问卷行业别类型
        if(type.equals("04")){
            list.get(0).add(o);
        }
        if(type.equals(industryType)){
            list.get(1).add(o);
        }
        if(type.equals("05")){
            list.get(2).add(o);
        }
        if(type.equals("06")){
            list.get(3).add(o);
        }
        if(type.equals("13")){
            list.get(4).add(o);
        }
    }
}
