package com.fb.goldencudgel.auditDecisionSystem.controller.missionStroke;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.comwave.core.data.domain.QueryPage;
import com.fb.goldencudgel.auditDecisionSystem.domain.LoanCompany.FbLoanCompany;
import com.fb.goldencudgel.auditDecisionSystem.domain.addreData.ViewCity;
import com.fb.goldencudgel.auditDecisionSystem.domain.addreData.ViewDistrict;
import com.fb.goldencudgel.auditDecisionSystem.domain.addreData.ViewStreet;
import com.fb.goldencudgel.auditDecisionSystem.domain.appointMentRecord.FbAppointmentRecord;
import com.fb.goldencudgel.auditDecisionSystem.domain.itemData.ViewDataDictItem;
import com.fb.goldencudgel.auditDecisionSystem.model.AjaxResut;
import com.fb.goldencudgel.auditDecisionSystem.model.MissionStrokeModel;
import com.fb.goldencudgel.auditDecisionSystem.model.User;
import com.fb.goldencudgel.auditDecisionSystem.repository.AppointmentRecordRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.LoanCompanyRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.ViewCityRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.ViewDictItemRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.ViewDistrictRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.ViewStreetRepository;
import com.fb.goldencudgel.auditDecisionSystem.schema.companyBaseInfo.GetCompanyBaseInfoRq;
import com.fb.goldencudgel.auditDecisionSystem.schema.companyBaseInfo.GetCompanyBaseInfoRs;
import com.fb.goldencudgel.auditDecisionSystem.schema.getCreditCaseList.CaseInfo;
import com.fb.goldencudgel.auditDecisionSystem.schema.getCreditCaseList.GetCreditCaseListReq;
import com.fb.goldencudgel.auditDecisionSystem.schema.getCreditCaseList.GetCreditCaseListResp;
import com.fb.goldencudgel.auditDecisionSystem.schema.getLeaderHomeInfo.GetLeaderHomeInfoRq;
import com.fb.goldencudgel.auditDecisionSystem.schema.getLeaderHomeInfo.GetLeaderHomeInfoRs;
import com.fb.goldencudgel.auditDecisionSystem.schema.getLeaderHomeInfo.ReachingRateInfo;
import com.fb.goldencudgel.auditDecisionSystem.schema.getManagerHoneInfo.GetManagerHomeInfoRq;
import com.fb.goldencudgel.auditDecisionSystem.schema.getManagerHoneInfo.GetManagerHomeInfoRs;
import com.fb.goldencudgel.auditDecisionSystem.schema.getManagerHoneInfo.ProdList;
import com.fb.goldencudgel.auditDecisionSystem.schema.getSaleHomeInfo.GetSaleHomeInfoRq;
import com.fb.goldencudgel.auditDecisionSystem.schema.getSaleHomeInfo.GetSaleHomeInfoRs;
import com.fb.goldencudgel.auditDecisionSystem.service.IInterfaceService;
import com.fb.goldencudgel.auditDecisionSystem.service.IMissionStroke;
import com.fb.goldencudgel.auditDecisionSystem.utils.ObjectUtil;
import com.fb.goldencudgel.auditDecisionSystem.utils.ServiceUtil;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;

/**
 * 行程任务
 * @Auther hu
 */
@Controller
@RequestMapping("/missionStroke")
public class MissionStrokeController {
    
    private static final Logger logger = LoggerFactory.getLogger(MissionStrokeController.class);
    
    @Autowired
    private IMissionStroke missionStroke;

    @Autowired
    private LoanCompanyRepository loanCompanyRepository;

    @Autowired
    private ViewDictItemRepository dictItemRepository;

    @Autowired
    private AppointmentRecordRepository appointmentRecordRepository;

    @Autowired
    private ViewCityRepository viewCityRepository;

    @Autowired
    private ViewDistrictRepository viewDistrictRepository;

    @Autowired
    private ViewStreetRepository viewStreetRepository;

    @Autowired
    private IInterfaceService interfaceService;

    private GetManagerHomeInfoRs getManagerHomeInfoRs;
    
    private GetLeaderHomeInfoRs getLeaderHomeInfoRs;
    
    private GetSaleHomeInfoRs getSaleHomeInfoRs;
    
    private GetCreditCaseListResp getCreditCaseListResp;
    
    /**
     * 进入行程任务页
     * @param model
     * @return
     */
    @RequestMapping("/viewMissionStroke")
    public String index(Model model){

        User user = UserUtil.getCurrUser();
        
        //区域
        Map<String,String> areaList = missionStroke.getAreaList("");
        Map<String,String> groupList = null;
        Map<String,String> userList = null;
        if ("M".equals(user.getUserType())) {
            //组别
            groupList = missionStroke.getGroupList("");
            //业务员
            userList = missionStroke.getUserList("", "");
        } else {
            //组别
            groupList = missionStroke.getGroupList(user.getUserArea());
            //业务员
            userList = missionStroke.getUserList(user.getUserArea(),user.getUserGroup());
        }
        
        
        long interfaceInvokeStart = System.currentTimeMillis();
        if ("M".equals(user.getUserType()) || "A".equals(user.getUserType())) {
            GetManagerHomeInfoRq getManagerHomeInfoRq = new GetManagerHomeInfoRq();
            getManagerHomeInfoRq.setUid(user.getUserUid());
            getManagerHomeInfoRs = interfaceService.getManagerHoneInfo(getManagerHomeInfoRq);
        } else if ("C".equals(user.getUserType())) {
            GetLeaderHomeInfoRq getLeaderHomeInfoRq = new GetLeaderHomeInfoRq();
            getLeaderHomeInfoRq.setUid(user.getUserUid());
            getLeaderHomeInfoRs = interfaceService.getLeaderHomeInfo(getLeaderHomeInfoRq);
        } else if ("S".equals(user.getUserType()) || "Z".equals(user.getUserType())) {
            GetSaleHomeInfoRq getSaleHomeInfoRq = new GetSaleHomeInfoRq();
            getSaleHomeInfoRq.setUid(user.getUserUid());
            getSaleHomeInfoRs = interfaceService.getSaleHomeInfo(getSaleHomeInfoRq);
        }
        long interfaceInvokeEnd = System.currentTimeMillis();
        logger.info("调用接口耗时：{}", (interfaceInvokeEnd - interfaceInvokeStart));
        Map<String, String> productList = new LinkedHashMap<>();
        productList.put("ALL", "全產品合計");
        productList.put("COM", "公司戶貸款小計");
        productList.put("CREDIT", "企業主信貸");
        productList.put("HOUSE", "企業主房貸");
        
        model.addAttribute("areaList",areaList);
        model.addAttribute("groupList",groupList);
        model.addAttribute("userList",userList);
        model.addAttribute("productList",productList);
        
        return "missionStroke/missionStroke";
    }

    /**
     * 获取组列表
     * @param model
     * @param areaCode
     * @return
     */
    @RequestMapping("/getGroupList")
    @ResponseBody
    public List<Map<String,String>> getGroupList(Model model ,String areaCode) {
        List<Map<String, String>> resultList = new ArrayList<>();
        Map<String,String> groupList = missionStroke.getGroupList(areaCode);
        Map<String, String> userList = missionStroke.getUserList(areaCode, "");
        resultList.add(groupList);
        resultList.add(userList);
        return resultList;
    }

    /**
     * 获取业务员列表
     * @param model
     * @param areaCode
     * @param groupCode
     * @return
     */
    @RequestMapping("/getUserList")
    @ResponseBody
    public Map<String,String> getUserList(Model model ,String areaCode,String groupCode){
        Map<String,String> groupList = missionStroke.getUserList(areaCode,groupCode);
        return groupList;
    }

    @RequestMapping("/searchStroke")
    public String searchStroke(Model model ,String areaCode,String groupCode ,String userCode,String comName,String appointmentDate){
        QueryPage<Object[]> strokeDetialQueryPage= missionStroke.searchStroke(areaCode,groupCode,userCode,comName,appointmentDate);
        List<Object[]> strokeDetialList =  strokeDetialQueryPage.getContent();
        model.addAttribute("appointmentRecordList", CollectionUtils.isEmpty(strokeDetialList) ? new ArrayList<Object[]>() : strokeDetialList);
        // 用于判断页面的预约拜访时间是展示时分，还是展示年月日 D-展示时分，M-展示年月日
        if (!StringUtils.isBlank(comName)) {
            model.addAttribute("queryType", "M");
        } else {
            model.addAttribute("queryType", "D");
        }
        
        return  "missionStroke/viewStrokeDetialList";
    }
    
    @RequestMapping("/searchStrokeByMonth")
    public String searchStrokeByMonth(Model model , String areaCode, String groupCode , String userCode, String comName, String appointmentDate){
        QueryPage<Object[]> strokeDetialQueryPage = missionStroke.searchStrokeByMonth(areaCode,groupCode, userCode, comName, appointmentDate);
        List<Object[]> strokeDetialList =  strokeDetialQueryPage.getContent();
        model.addAttribute("appointmentRecordList", CollectionUtils.isEmpty(strokeDetialList) ? new ArrayList<Object[]>() : strokeDetialList);
        // 用于判断页面的预约拜访时间是展示时分，还是展示年月日 D-展示时分，M-展示年月日
        model.addAttribute("queryType", "M");
        return  "missionStroke/viewStrokeDetialList";
    }
    
    @RequestMapping("/searchStrokeByComName")
    public String searchStrokeByComName(Model model , String areaCode, String groupCode , String userCode, String comName, String appointmentDate){
        QueryPage<Object[]> strokeDetialQueryPage = missionStroke.searchStrokeByMonth(areaCode,groupCode,userCode,comName,appointmentDate);
        List<Object[]> strokeDetialList =  strokeDetialQueryPage.getContent();
        model.addAttribute("appointmentRecordList", CollectionUtils.isEmpty(strokeDetialList) ? new ArrayList<Object[]>() : strokeDetialList);
        // 用于判断页面的预约拜访时间是展示时分，还是展示年月日 D-展示时分，M-展示年月日
        model.addAttribute("queryType", "D");
        return  "missionStroke/viewStrokeDetialList";
    }

    @RequestMapping("/showStrokeDetial")
    public  String showStrokeDetial(Model model ,String compilationNo,String trandId,String appointmentType){

        //公司详情
        FbLoanCompany vcInfo = loanCompanyRepository.findByComplicationNo(compilationNo);
        String comActualCapital = vcInfo.getComActualCapital() == null ? "" : vcInfo.getComActualCapital();
        BigDecimal a=new BigDecimal(comActualCapital.replaceAll(",", ""));
        DecimalFormat df=new DecimalFormat(",###,##0"); //没有小数  
        String b=df.format(a);
        vcInfo.setComActualCapital(b);

        //拜访详情
        FbAppointmentRecord appointmentRecord = appointmentRecordRepository.findByIds(compilationNo,trandId,appointmentType);
        if(null == appointmentRecord){
            appointmentRecord = new FbAppointmentRecord();
        }
        //数据字典
        List<ViewDataDictItem> businessTypeList = dictItemRepository.findByDictId("BUSINESS_TYPE");//行業別
        List<ViewDataDictItem> enterSourceList = dictItemRepository.findByDictId("ENTER_SOURCE");//案件來源
        List<ViewDataDictItem> organizationList = dictItemRepository.findByDictId("COM_ORGANIZATION");//企業性質
        List<ViewDataDictItem> positionList = dictItemRepository.findByDictId("POSITION");//职位
        List<ViewCity> viewCityList= viewCityRepository.findAll();//市
        List<ViewDistrict> viewDistrictList=viewDistrictRepository.findAll();
        List<ViewStreet> viewStreetList=viewStreetRepository.findAll();
        model.addAttribute("vcInfo",vcInfo);
        model.addAttribute("appointmentRecord",appointmentRecord);
        model.addAttribute("businessTypeList",businessTypeList);
        model.addAttribute("enterSourceList",enterSourceList);
        model.addAttribute("organizationList",organizationList);
        model.addAttribute("positionList",positionList);
        model.addAttribute("viewCityList",viewCityList);
        model.addAttribute("viewDistrictList",viewDistrictList);
        model.addAttribute("viewStreetList",viewStreetList);
        return "missionStroke/missionStrokeDetial";
    }

    @RequestMapping("getDistrict")
    @ResponseBody
    public List<ViewDistrict> getDistrict(Model model,String comRealCityCode){
        List<ViewDistrict> viewDistrictList= new ArrayList<ViewDistrict>();
        viewDistrictList = viewDistrictRepository.findByCityCode(comRealCityCode);
        return viewDistrictList;
    }

    @RequestMapping("getStreet")
    @ResponseBody
    public List<ViewStreet> getStreet(Model model, String comRealDistrictCode){
        List<ViewStreet> viewStreetList= new ArrayList<ViewStreet>();
        viewStreetList = viewStreetRepository.findByDistrictCode(comRealDistrictCode);
        return viewStreetList;
    }

    /**
     * 新增任务行程页面
     * @param model
     * @return
     */
    @RequestMapping("viewAddStrokeView")
    public String viewAddStrokeView(Model model) {
        List<ViewDataDictItem> enterSourceList = dictItemRepository.findByDictId("ENTER_SOURCE");//案件來源
        List<ViewDataDictItem> positionList = dictItemRepository.findByDictId("POSITION");//职位
        List<ViewDataDictItem> organizationList = dictItemRepository.findByDictId("COM_ORGANIZATION");//企業性質
        List<ViewCity> viewCityList= viewCityRepository.findAll();//市
        model.addAttribute("organizationList",organizationList);
        model.addAttribute("enterSourceList",enterSourceList);
        model.addAttribute("positionList",positionList);
        model.addAttribute("viewCityList",viewCityList);
        return "missionStroke/viewAddStrokeView";
    }

    /**
     * @description 根據統編獲取公司基本信息
     */
    @RequestMapping("/searchComDetail")
    @ResponseBody
    public AjaxResut searchComDetail(Model model, String compilationNo){
        AjaxResut ajaxResut = new AjaxResut();
        ajaxResut.setReturnCode(true);
        GetCompanyBaseInfoRq companyBaseInfoRq = new GetCompanyBaseInfoRq();
        companyBaseInfoRq.setCompCode(compilationNo);
        companyBaseInfoRq.setFunctionType("0");
        GetCompanyBaseInfoRs companyBaseInfo = interfaceService.getCompanyBaseInfo(companyBaseInfoRq);
        ajaxResut.setReturnResult(companyBaseInfo);
        return ajaxResut;
    }

        @RequestMapping("saveStroke")
        @ResponseBody
        public AjaxResut saveStroke(Model model, MissionStrokeModel missionStrokeModel,String mobile,String email,String caseNo){
            missionStrokeModel.setComActualCapital(missionStrokeModel.getComActualCapital().replaceAll(",", ""));
            AjaxResut ajaxResut = missionStroke.saveStrokeSpd(missionStrokeModel,mobile,email,caseNo);
            return ajaxResut;
        }

        @RequestMapping("updateStroke")
        @ResponseBody
        public AjaxResut updateStroke(Model model, MissionStrokeModel missionStrokeModel,String mobile,String email){
            missionStrokeModel.setComActualCapital(missionStrokeModel.getComActualCapital().replaceAll(",", ""));
//            if (missionStrokeModel.getIntroduceId().length() > 0) {
//                missionStrokeModel.setIntroduceId(missionStrokeModel.getIntroduceId().substring(0, missionStrokeModel.getIntroduceId().length()-1));
//            }
//            if (missionStrokeModel.getIntroduceAddress().length() > 0) {
//                missionStrokeModel.setIntroduceAddress(missionStrokeModel.getIntroduceAddress().substring(0, missionStrokeModel.getIntroduceAddress().length()-1));
//            }
//            if (missionStrokeModel.getIntroduceName().length() > 0) {
//                missionStrokeModel.setIntroduceName(missionStrokeModel.getIntroduceName().substring(0, missionStrokeModel.getIntroduceName().length()-1));
//            }
            AjaxResut ajaxResut = missionStroke.updateStrokeSpd(missionStrokeModel,mobile,email);
        return ajaxResut;
    }

    @RequestMapping("searchTaskProgress")
    public String searchTaskProgress(Model model,@RequestParam(required = false, defaultValue = "1") Integer curPage){
        QueryPage<Object[]> queryPage = new QueryPage<>(curPage, 5);
        QueryPage<Object[]> result =  missionStroke.fingTravelSchedule(queryPage,"");
        model.addAttribute("travelScheduleList",result.getContent());
        model.addAttribute("page", result);
        return "missionStroke/viewTaskProgress";
    }

    @RequestMapping("changeProdCode")
    @ResponseBody
    public Map<String,String> changeProdCode(Model model, String areaCode, String prodCode) {
        return getReachingRateInfo(areaCode, prodCode);
    }

    @RequestMapping("getEnterSource")
    @ResponseBody
    public List<ViewDataDictItem> getEnterSource(Model model,String comCustomerType){
        List<ViewDataDictItem> enterSourceList = new ArrayList<ViewDataDictItem>();
        if("01".equals(comCustomerType))
            enterSourceList = dictItemRepository.findNotItemCode("ENTER_SOURCE","JH" + "%");//企業性質
        else
            enterSourceList = dictItemRepository.findItemCode("ENTER_SOURCE","JH" + "%");//企業性質
        return enterSourceList;
    }
    
    /**
     * @description 獲取案件列表
     */
    @RequestMapping("/getCreditCaseList")
    public String getCreditCaseList(Model model) {
        User user = UserUtil.getCurrUser();
        GetCreditCaseListReq getCreditCaseListReq = new GetCreditCaseListReq();
        getCreditCaseListReq.setUid(user.getUserUid());
        getCreditCaseListResp = interfaceService.getCreditCaseList(getCreditCaseListReq);
        List<CaseInfo> caseInfoList = getCreditCaseListResp.getCaseInfo();
        model.addAttribute("caseInfoList", caseInfoList);
        return "missionStroke/viewAddStrokeDialog";
    }
    
    /**
     * @description 征信員新增任務行程
     */
    @RequestMapping("/viewAddStrokeViewByZXY")
    public String viewAddStrokeViewByZXY(Model model, String caseNo) {
        if (getCreditCaseListResp == null) {
            User user = UserUtil.getCurrUser();
            GetCreditCaseListReq getCreditCaseListReq = new GetCreditCaseListReq();
            getCreditCaseListReq.setUid(user.getUserUid());
            getCreditCaseListResp = interfaceService.getCreditCaseList(getCreditCaseListReq);
        }
        List<CaseInfo> caseInfoList = getCreditCaseListResp.getCaseInfo();
        for (CaseInfo caseInfo : caseInfoList) {
            if (caseNo.equals(caseInfo.getCaseNo())) {
                model.addAttribute("caseInfo", caseInfo);
                break;
            }
        }
        
        List<ViewDataDictItem> enterSourceList = dictItemRepository.findByDictId("ENTER_SOURCE");//案件來源
        List<ViewDataDictItem> positionList = dictItemRepository.findByDictId("POSITION");//职位
        List<ViewDataDictItem> organizationList = dictItemRepository.findByDictId("COM_ORGANIZATION");//企業性質
        List<ViewCity> viewCityList= viewCityRepository.findAll();//市
        model.addAttribute("organizationList",organizationList);
        model.addAttribute("enterSourceList",enterSourceList);
        model.addAttribute("positionList",positionList);
        model.addAttribute("viewCityList",viewCityList);
        model.addAttribute("caseNo",caseNo);
        
        return "missionStroke/viewAddStrokeViewByZXY";
    }
    
    /**
     * @description 根據統編查詢案件
     */
    @RequestMapping("/searchCaseByCompilationNo")
    public String searchCaseByCompilationNo(Model model, String compilationNo) {
        List<CaseInfo> allCaseInfoList = getCreditCaseListResp.getCaseInfo();
        List<CaseInfo> returnCaseInfoLift = new ArrayList<>();
        if (StringUtils.isBlank(compilationNo)) {
            model.addAttribute("caseInfoList", allCaseInfoList);
        } else {
            for (CaseInfo caseInfo : allCaseInfoList) {
                if (compilationNo.equals(caseInfo.getCompCode())) {
                    returnCaseInfoLift.add(caseInfo);
                    model.addAttribute("caseInfoList", returnCaseInfoLift);
                }
            }
        }
        
        return "missionStroke/viewAddStrokeDialog";
    }
    
    /**
     * @description 刪除任務行程
     */
    @RequestMapping("/delMissionStroke")
    @ResponseBody
    public Map<String, Object> delMissionStroke(Model model, String compilationNo, String trandId, String appointmentType, String createUser) {
        Map<String, Object> result = missionStroke.delMissionStroke(compilationNo, trandId, appointmentType, createUser);
        return result;
    }
    
    /**
     * @description 修改任務行程
     */
    @RequestMapping("/editMissionStroke")
    public String editMissionStroke(Model model, String compilationNo, String trandId, String appointmentType) {
        // 任務行程信息
        QueryPage<Object[]> queryPage = missionStroke.queryByConditions(compilationNo, trandId, appointmentType);
        List<String> dictParamList = new ArrayList<>();
        dictParamList.add("ENTER_SOURCE");
        dictParamList.add("POSITION");
        dictParamList.add("COM_ORGANIZATION");
        QueryPage<Object[]> dictPage = missionStroke.queryDictByConditions(dictParamList);
        List<Object[]> dictList = dictPage.getContent();
        // 案件來源下拉列表
        List<Object[]> enterSourceList = ServiceUtil.getByType(dictList, "ENTER_SOURCE");
        // 职位下拉列表
        List<Object[]> positionList = ServiceUtil.getByType(dictList, "POSITION");
        // 企業性質下拉列表
        List<Object[]> organizationList = ServiceUtil.getByType(dictList, "COM_ORGANIZATION");
        
        QueryPage<Object[]> addressPage = missionStroke.queryCityAndDistrictAndStreet();
        List<Object[]> addressList = addressPage.getContent();
        // 縣市下拉列表
        List<Object[]> viewCityList = ServiceUtil.getByType(addressList, "CITY");
        // 鄉鎮市區下拉列表
        List<Object[]> viewDistrictList = ServiceUtil.getByType(addressList, "DISTRICT");
        // 道路或街名或村里名稱下拉列表
        List<Object[]> viewStreetList = ServiceUtil.getByType(addressList, "STREET");
        Map<String, Object> missionStrokeMap = new HashMap<>();
        if (queryPage != null) {
            List<Object[]> queryList = queryPage.getContent();
            if (!CollectionUtils.isEmpty(queryList)) {
                Object[] queryObject = queryList.get(0);
                missionStrokeMap.put("appointmentId", queryObject[0]);
                // 客戶屬性
                missionStrokeMap.put("comCustomerType", queryObject[9]);
                // 授信戶統編
                missionStrokeMap.put("compilationNo", queryObject[1]);
                // 授信戶名稱
                missionStrokeMap.put("compilationName", queryObject[10]);
                // 負責人
                missionStrokeMap.put("princilpalName", queryObject[24]);
                // 案件來源
                missionStrokeMap.put("enterSource", queryObject[18]);
                // 案源轉介人姓名
                missionStrokeMap.put("introduceName", queryObject[20]);
                // 案源轉介人員编/ID
                missionStrokeMap.put("introduceId", queryObject[19]);
                
                String introducePhoneAll = ObjectUtil.obj2String(queryObject[21]);
                String introducePhoneExt = "";
                String introducePhone = "";
                if (introducePhoneAll.indexOf(")") != -1) {
                    String[] introducePhoneArr = introducePhoneAll.split("\\)");
                    if (introducePhoneArr.length > 1) {
                        introducePhoneExt = introducePhoneArr[0].replaceAll("\\(", "");
                        introducePhone = introducePhoneArr[1];
                    }
                } else {
                    String[] introducePhoneArr = introducePhoneAll.split("-");
                    if (introducePhoneArr.length > 1) {
                        introducePhoneExt = introducePhoneArr[0];
                        introducePhone = introducePhoneArr[1];
                    }
                }
                
                // 案源轉介人電話
                missionStrokeMap.put("introducePhoneExt", introducePhoneExt);
                // 案源轉介人電話
                missionStrokeMap.put("introducePhone", introducePhone);
                // 案源轉介人單位
                missionStrokeMap.put("introduceAddress", queryObject[22]);
                // 公司設立日期
                missionStrokeMap.put("comEstabdate", ObjectUtil.obj2String(queryObject[25]).substring(0, 10));
                // 組織型態
                missionStrokeMap.put("comOrganization", queryObject[26]);
                // 資本額(千元)
                missionStrokeMap.put("comActualCapital", queryObject[27]);
                // 实际营业地址
                missionStrokeMap.put("comAddress", queryObject[11]);
                // 公司登記地址
                missionStrokeMap.put("compRegAddress", queryObject[28]);
                Map<String, Object> addressMap = ServiceUtil.splitAddress(ObjectUtil.obj2String(queryObject[11]), viewCityList, viewDistrictList, viewStreetList);
                model.addAttribute("viewDistrictList", ServiceUtil.getChildrenByParent(ObjectUtil.obj2String(addressMap.get("cityCode")), viewDistrictList));
                model.addAttribute("viewStreetList", ServiceUtil.getChildrenByParent(ObjectUtil.obj2String(addressMap.get("districtCode")), viewStreetList));
                // 實際營業地址-縣市
                missionStrokeMap.put("comRealCityCode", addressMap.get("cityCode"));
                // 實際營業地址-鄉鎮市區
                missionStrokeMap.put("comRealDistrictCode", addressMap.get("districtCode"));
                // 實際營業地址-道路或街名或村里名稱
                missionStrokeMap.put("comRealStreetCode", addressMap.get("streetCode"));
                // 實際營業地址-巷
                missionStrokeMap.put("comRealTunnel", addressMap.get("comRealTunnel"));
                // 實際營業地址-弄
                missionStrokeMap.put("comRealLane", addressMap.get("comRealLane"));
                // 實際營業地址-號
                missionStrokeMap.put("comRealAddnumber", addressMap.get("comRealAddnumber"));
                // 實際營業地址-樓
                missionStrokeMap.put("comRealSpace1", addressMap.get("comRealSpace1"));
                // 實際營業地址-室
                missionStrokeMap.put("comRealSpace2", addressMap.get("comRealSpace2"));
                // 拜訪對象
                missionStrokeMap.put("appointmentUser", queryObject[12]);
                // 職稱
                missionStrokeMap.put("appointmentPosition", queryObject[13]);
                // 職稱其他
                missionStrokeMap.put("appoipositionOther", queryObject[36]);
                // 公司聯絡電話區號
                missionStrokeMap.put("comPhoneAreaCode", queryObject[15]);
                // 公司聯絡電話號碼
                missionStrokeMap.put("comPhoneNum", queryObject[16]);
                // 公司聯絡電話分機
                missionStrokeMap.put("comPhoneExten", queryObject[17]);
                // 預定拜訪日期
                missionStrokeMap.put("appointmentDate", queryObject[3]);
                // 預定拜訪時間
                missionStrokeMap.put("appointmentTimeH", ObjectUtil.obj2Integer(queryObject[4]));
                // 預定拜訪時間
                missionStrokeMap.put("appointmentTimeM", queryObject[5]);
                // 備註
                missionStrokeMap.put("remark", queryObject[23]);
                // 行動電話
                missionStrokeMap.put("mobile", queryObject[37]);
                // 電子郵件信箱
                missionStrokeMap.put("email", queryObject[38]);
                
            }
        }
        model.addAttribute("missionStrokeMap", missionStrokeMap);
        model.addAttribute("organizationList", organizationList);
        model.addAttribute("enterSourceList", enterSourceList);
        model.addAttribute("positionList", positionList);
        model.addAttribute("viewCityList", viewCityList);
        
        return "missionStroke/viewEditStrokeView";
    }
    
    public Map<String, String> getReachingRateInfo(String areaCode, String prodCode) {
        Map<String, String> reachingRateInfoMap = new HashMap<>();
        User user = UserUtil.getCurrUser();
        
        if ("M".equals(user.getUserType())) {
            List<com.fb.goldencudgel.auditDecisionSystem.schema.getManagerHoneInfo.ReachingRateInfo> reachingRateInfoList = getManagerHomeInfoRs.getReachingRateInfo();
            if (!CollectionUtils.isEmpty(reachingRateInfoList)) {
                if (StringUtils.isBlank(areaCode)) {
                    com.fb.goldencudgel.auditDecisionSystem.schema.getManagerHoneInfo.ReachingRateInfo reachingRateInfo = reachingRateInfoList.get(0);
                    List<ProdList> prodList = reachingRateInfo.getProdList();
                    if (!CollectionUtils.isEmpty(prodList)) {
                        if (StringUtils.isBlank(prodCode)) {
                            ProdList prodInfo = prodList.get(0);
                            reachingRateInfoMap.put("applyCount", prodInfo.getApplyCount());
                            reachingRateInfoMap.put("applyAmount", prodInfo.getApplyAmount());
                            reachingRateInfoMap.put("applyRate", prodInfo.getApplyRate());
                            reachingRateInfoMap.put("achieveCount", prodInfo.getAchieveCount());
                            reachingRateInfoMap.put("loanAmount", prodInfo.getLoanAmount());
                            reachingRateInfoMap.put("achieveRate", prodInfo.getAchieveRate());
                            reachingRateInfoMap.put("prodCode", prodInfo.getProdCode());
                            reachingRateInfoMap.put("prodName", prodInfo.getProdName());
                            if(prodInfo.getStatisticsTime()=="0"){
                                reachingRateInfoMap.put("statisticsTime","");
                            }else{
                                reachingRateInfoMap.put("statisticsTime", prodInfo.getStatisticsTime());
                            }
                            reachingRateInfoMap.put("areaCode", reachingRateInfo.getAreaCode());
                            reachingRateInfoMap.put("areaTxt", reachingRateInfo.getAreaTxt());
                        } else {
                            for (ProdList prodInfo : prodList) {
                                if (prodCode.equals(prodInfo.getProdCode())) {
                                    reachingRateInfoMap.put("applyCount", prodInfo.getApplyCount());
                                    reachingRateInfoMap.put("applyAmount", prodInfo.getApplyAmount());
                                    reachingRateInfoMap.put("applyRate", prodInfo.getApplyRate());
                                    reachingRateInfoMap.put("achieveCount", prodInfo.getAchieveCount());
                                    reachingRateInfoMap.put("loanAmount", prodInfo.getLoanAmount());
                                    reachingRateInfoMap.put("achieveRate", prodInfo.getAchieveRate());
                                    reachingRateInfoMap.put("prodCode", prodInfo.getProdCode());
                                    reachingRateInfoMap.put("prodName", prodInfo.getProdName());
                                    if(prodInfo.getStatisticsTime()=="0"){
                                        reachingRateInfoMap.put("statisticsTime","");
                                    }else{
                                        reachingRateInfoMap.put("statisticsTime", prodInfo.getStatisticsTime());
                                    }
                                    reachingRateInfoMap.put("areaCode", reachingRateInfo.getAreaCode());
                                    reachingRateInfoMap.put("areaTxt", reachingRateInfo.getAreaTxt());
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    for (com.fb.goldencudgel.auditDecisionSystem.schema.getManagerHoneInfo.ReachingRateInfo reachingRateInfo : reachingRateInfoList) {
                        if (areaCode.equals(reachingRateInfo.getAreaCode())) {
                            List<ProdList> prodList = reachingRateInfo.getProdList();
                            if (!CollectionUtils.isEmpty(prodList)) {
                                if (StringUtils.isBlank(prodCode)) {
                                    ProdList prodInfo = prodList.get(0);
                                    reachingRateInfoMap.put("applyCount", prodInfo.getApplyCount());
                                    reachingRateInfoMap.put("applyAmount", prodInfo.getApplyAmount());
                                    reachingRateInfoMap.put("applyRate", prodInfo.getApplyRate());
                                    reachingRateInfoMap.put("achieveCount", prodInfo.getAchieveCount());
                                    reachingRateInfoMap.put("loanAmount", prodInfo.getLoanAmount());
                                    reachingRateInfoMap.put("achieveRate", prodInfo.getAchieveRate());
                                    reachingRateInfoMap.put("prodCode", prodInfo.getProdCode());
                                    reachingRateInfoMap.put("prodName", prodInfo.getProdName());
                                    if(prodInfo.getStatisticsTime()=="0"){
                                        reachingRateInfoMap.put("statisticsTime","");
                                    }else{
                                        reachingRateInfoMap.put("statisticsTime", prodInfo.getStatisticsTime());
                                    }
                                    reachingRateInfoMap.put("areaCode", reachingRateInfo.getAreaCode());
                                    reachingRateInfoMap.put("areaTxt", reachingRateInfo.getAreaTxt());
                                } else {
                                    for (ProdList prodInfo : prodList) {
                                        
                                        if (prodCode.equals(prodInfo.getProdCode())) {
                                            reachingRateInfoMap.put("applyCount", prodInfo.getApplyCount());
                                            reachingRateInfoMap.put("applyAmount", prodInfo.getApplyAmount());
                                            reachingRateInfoMap.put("applyRate", prodInfo.getApplyRate());
                                            reachingRateInfoMap.put("achieveCount", prodInfo.getAchieveCount());
                                            reachingRateInfoMap.put("loanAmount", prodInfo.getLoanAmount());
                                            reachingRateInfoMap.put("achieveRate", prodInfo.getAchieveRate());
                                            reachingRateInfoMap.put("prodCode", prodInfo.getProdCode());
                                            reachingRateInfoMap.put("prodName", prodInfo.getProdName());
                                            if(prodInfo.getStatisticsTime()=="0"){
                                                reachingRateInfoMap.put("statisticsTime","");
                                            }else{
                                                reachingRateInfoMap.put("statisticsTime", prodInfo.getStatisticsTime());
                                            }
                                            reachingRateInfoMap.put("areaCode", reachingRateInfo.getAreaCode());
                                            reachingRateInfoMap.put("areaTxt", reachingRateInfo.getAreaTxt());
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if ("A".equals(user.getUserType())) {
            List<com.fb.goldencudgel.auditDecisionSystem.schema.getManagerHoneInfo.ReachingRateInfo> reachingRateInfoList = getManagerHomeInfoRs.getReachingRateInfo();
            if (!CollectionUtils.isEmpty(reachingRateInfoList)) {
                if (StringUtils.isBlank(areaCode)) {
                    com.fb.goldencudgel.auditDecisionSystem.schema.getManagerHoneInfo.ReachingRateInfo reachingRateInfo = reachingRateInfoList.get(0);
                    List<ProdList> prodList = reachingRateInfo.getProdList();
                    if (!CollectionUtils.isEmpty(prodList)) {
                        if (StringUtils.isBlank(prodCode)) {
                            ProdList prodInfo = prodList.get(0);
                            reachingRateInfoMap.put("applyCount", prodInfo.getApplyCount());
                            reachingRateInfoMap.put("applyAmount", prodInfo.getApplyAmount());
                            reachingRateInfoMap.put("applyRate", prodInfo.getApplyRate());
                            reachingRateInfoMap.put("achieveCount", prodInfo.getAchieveCount());
                            reachingRateInfoMap.put("loanAmount", prodInfo.getLoanAmount());
                            reachingRateInfoMap.put("achieveRate", prodInfo.getAchieveRate());
                            reachingRateInfoMap.put("prodCode", prodInfo.getProdCode());
                            reachingRateInfoMap.put("prodName", prodInfo.getProdName());
                            if(prodInfo.getStatisticsTime()=="0"){
                                reachingRateInfoMap.put("statisticsTime","");
                            }else{
                                reachingRateInfoMap.put("statisticsTime", prodInfo.getStatisticsTime());
                            }
                            reachingRateInfoMap.put("areaCode", reachingRateInfo.getAreaCode());
                            reachingRateInfoMap.put("areaTxt", reachingRateInfo.getAreaTxt());
                        } else {
                            for (ProdList prodInfo : prodList) {
                                if (prodCode.equals(prodInfo.getProdCode())) {
                                    reachingRateInfoMap.put("applyCount", prodInfo.getApplyCount());
                                    reachingRateInfoMap.put("applyAmount", prodInfo.getApplyAmount());
                                    reachingRateInfoMap.put("applyRate", prodInfo.getApplyRate());
                                    reachingRateInfoMap.put("achieveCount", prodInfo.getAchieveCount());
                                    reachingRateInfoMap.put("loanAmount", prodInfo.getLoanAmount());
                                    reachingRateInfoMap.put("achieveRate", prodInfo.getAchieveRate());
                                    reachingRateInfoMap.put("prodCode", prodInfo.getProdCode());
                                    reachingRateInfoMap.put("prodName", prodInfo.getProdName());
                                    if(prodInfo.getStatisticsTime()=="0"){
                                        reachingRateInfoMap.put("statisticsTime","");
                                    }else{
                                        reachingRateInfoMap.put("statisticsTime", prodInfo.getStatisticsTime());
                                    }
                                    reachingRateInfoMap.put("areaCode", reachingRateInfo.getAreaCode());
                                    reachingRateInfoMap.put("areaTxt", reachingRateInfo.getAreaTxt());
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    for (com.fb.goldencudgel.auditDecisionSystem.schema.getManagerHoneInfo.ReachingRateInfo reachingRateInfo : reachingRateInfoList) {
                        if (areaCode.equals(reachingRateInfo.getAreaCode())) {
                            List<ProdList> prodList = reachingRateInfo.getProdList();
                            if (!CollectionUtils.isEmpty(prodList)) {
                                if (StringUtils.isBlank(prodCode)) {
                                    ProdList prodInfo = prodList.get(0);
                                    reachingRateInfoMap.put("applyCount", prodInfo.getApplyCount());
                                    reachingRateInfoMap.put("applyAmount", prodInfo.getApplyAmount());
                                    reachingRateInfoMap.put("applyRate", prodInfo.getApplyRate());
                                    reachingRateInfoMap.put("achieveCount", prodInfo.getAchieveCount());
                                    reachingRateInfoMap.put("loanAmount", prodInfo.getLoanAmount());
                                    reachingRateInfoMap.put("achieveRate", prodInfo.getAchieveRate());
                                    reachingRateInfoMap.put("prodCode", prodInfo.getProdCode());
                                    reachingRateInfoMap.put("prodName", prodInfo.getProdName());
                                    if(prodInfo.getStatisticsTime()=="0"){
                                        reachingRateInfoMap.put("statisticsTime","");
                                    }else{
                                        reachingRateInfoMap.put("statisticsTime", prodInfo.getStatisticsTime());
                                    }
                                    reachingRateInfoMap.put("areaCode", reachingRateInfo.getAreaCode());
                                    reachingRateInfoMap.put("areaTxt", reachingRateInfo.getAreaTxt());
                                } else {
                                    for (ProdList prodInfo : prodList) {
                                        if (prodCode.equals(prodInfo.getProdCode())) {
                                            reachingRateInfoMap.put("applyCount", prodInfo.getApplyCount());
                                            reachingRateInfoMap.put("applyAmount", prodInfo.getApplyAmount());
                                            reachingRateInfoMap.put("applyRate", prodInfo.getApplyRate());
                                            reachingRateInfoMap.put("achieveCount", prodInfo.getAchieveCount());
                                            reachingRateInfoMap.put("loanAmount", prodInfo.getLoanAmount());
                                            reachingRateInfoMap.put("achieveRate", prodInfo.getAchieveRate());
                                            reachingRateInfoMap.put("prodCode", prodInfo.getProdCode());
                                            reachingRateInfoMap.put("prodName", prodInfo.getProdName());
                                            if(prodInfo.getStatisticsTime()=="0"){
                                                reachingRateInfoMap.put("statisticsTime","");
                                            }else{
                                                reachingRateInfoMap.put("statisticsTime", prodInfo.getStatisticsTime());
                                            }
                                            reachingRateInfoMap.put("areaCode", reachingRateInfo.getAreaCode());
                                            reachingRateInfoMap.put("areaTxt", reachingRateInfo.getAreaTxt());
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if ("C".equals(user.getUserType())) {
            List<com.fb.goldencudgel.auditDecisionSystem.schema.getLeaderHomeInfo.ReachingRateInfo> reachingRateInfoList = getLeaderHomeInfoRs.getReachingRateInfo();
            if (!CollectionUtils.isEmpty(reachingRateInfoList)) {
                if (StringUtils.isBlank(prodCode)) {
                    ReachingRateInfo prodInfo = reachingRateInfoList.get(0);
                    reachingRateInfoMap.put("applyCount", prodInfo.getApplyCount());
                    reachingRateInfoMap.put("applyAmount", prodInfo.getApplyAmount());
                    reachingRateInfoMap.put("applyRate", prodInfo.getApplyRate());
                    reachingRateInfoMap.put("achieveCount", prodInfo.getAchieveCount());
                    reachingRateInfoMap.put("loanAmount", prodInfo.getLoanAmount());
                    reachingRateInfoMap.put("achieveRate", prodInfo.getAchieveRate());
                    reachingRateInfoMap.put("prodCode", prodInfo.getProdCode());
                    reachingRateInfoMap.put("prodName", prodInfo.getProdName());
                    if(prodInfo.getStatisticsTime()=="0"){
                        reachingRateInfoMap.put("statisticsTime","");
                    }else{
                        reachingRateInfoMap.put("statisticsTime", prodInfo.getStatisticsTime());
                    }
                } else {
                    for (ReachingRateInfo prodInfo : reachingRateInfoList) {
                        if (prodCode.equals(prodInfo.getProdCode())) {
                            reachingRateInfoMap.put("applyCount", prodInfo.getApplyCount());
                            reachingRateInfoMap.put("applyAmount", prodInfo.getApplyAmount());
                            reachingRateInfoMap.put("applyRate", prodInfo.getApplyRate());
                            reachingRateInfoMap.put("achieveCount", prodInfo.getAchieveCount());
                            reachingRateInfoMap.put("loanAmount", prodInfo.getLoanAmount());
                            reachingRateInfoMap.put("achieveRate", prodInfo.getAchieveRate());
                            reachingRateInfoMap.put("prodCode", prodInfo.getProdCode());
                            reachingRateInfoMap.put("prodName", prodInfo.getProdName());
                            if(prodInfo.getStatisticsTime()=="0"){
                                reachingRateInfoMap.put("statisticsTime","");
                            }else{
                                reachingRateInfoMap.put("statisticsTime", prodInfo.getStatisticsTime());
                            }
                            break;
                        }
                    }
                }
            }
        } else if ("S".equals(user.getUserType()) || "Z".equals(user.getUserType())) {
            List<com.fb.goldencudgel.auditDecisionSystem.schema.getSaleHomeInfo.ReachingRateInfo> reachingRateInfoList = getSaleHomeInfoRs.getReachingRateInfo();
            if (!CollectionUtils.isEmpty(reachingRateInfoList)) {
                if (StringUtils.isBlank(prodCode)) {
                    com.fb.goldencudgel.auditDecisionSystem.schema.getSaleHomeInfo.ReachingRateInfo prodInfo = reachingRateInfoList.get(0);
                    reachingRateInfoMap.put("applyCount", prodInfo.getApplyCount());
                    reachingRateInfoMap.put("applyAmount", prodInfo.getApplyAmount());
                    reachingRateInfoMap.put("applyRate", ObjectUtil.double2String(prodInfo.getApplyRate()));
                    reachingRateInfoMap.put("achieveCount", prodInfo.getAchieveCount());
                    reachingRateInfoMap.put("loanAmount", prodInfo.getLoanAmount());
                    reachingRateInfoMap.put("achieveRate", ObjectUtil.double2String(prodInfo.getAchieveRate()));
                    reachingRateInfoMap.put("prodCode", prodInfo.getProdCode());
                    reachingRateInfoMap.put("prodName", prodInfo.getProdName());
                    if(prodInfo.getStatisticsTime()=="0"){
                        reachingRateInfoMap.put("statisticsTime","");
                    }else{
                        reachingRateInfoMap.put("statisticsTime", prodInfo.getStatisticsTime());
                    }
                } else {
                    for (com.fb.goldencudgel.auditDecisionSystem.schema.getSaleHomeInfo.ReachingRateInfo prodInfo : reachingRateInfoList) {
                        if (prodCode.equals(prodInfo.getProdCode())) {
                            reachingRateInfoMap.put("applyCount", prodInfo.getApplyCount());
                            reachingRateInfoMap.put("applyAmount", prodInfo.getApplyAmount());
                            reachingRateInfoMap.put("applyRate", ObjectUtil.double2String(prodInfo.getApplyRate()));
                            reachingRateInfoMap.put("achieveCount", prodInfo.getAchieveCount());
                            reachingRateInfoMap.put("loanAmount", prodInfo.getLoanAmount());
                            reachingRateInfoMap.put("achieveRate", ObjectUtil.double2String(prodInfo.getAchieveRate()));
                            reachingRateInfoMap.put("prodCode", prodInfo.getProdCode());
                            reachingRateInfoMap.put("prodName", prodInfo.getProdName());
                            if(prodInfo.getStatisticsTime()=="0"||prodInfo.getStatisticsTime()==null){
                                reachingRateInfoMap.put("statisticsTime","");
                            }else{
                                reachingRateInfoMap.put("statisticsTime", prodInfo.getStatisticsTime());
                            }
                            break;
                        }
                    }
                }
            }
        }
        
        if (reachingRateInfoMap == null || reachingRateInfoMap.size() == 0) {
            reachingRateInfoMap.put("applyCount", "0");
            reachingRateInfoMap.put("applyAmount", "0");
            reachingRateInfoMap.put("applyRate", "0");
            reachingRateInfoMap.put("achieveCount", "0");
            reachingRateInfoMap.put("loanAmount", "0");
            reachingRateInfoMap.put("achieveRate", "0");
            reachingRateInfoMap.put("prodCode", "ALL");
            reachingRateInfoMap.put("prodName", "全產品合計");
        } else {
            Set<String> keySet = reachingRateInfoMap.keySet();
            for (String key : keySet) {
                if (StringUtils.isBlank(reachingRateInfoMap.get(key)) && !key.equals("prodCode") && !key.equals("prodName") 
                        && !key.equals("areaCode") && !key.equals("areaTxt")) {
                    reachingRateInfoMap.put(key, "0");
                }
            }
            
        }
        
        String applyRateStr = ObjectUtil.obj2String(ObjectUtil.obj2BigDecimal(reachingRateInfoMap.get("applyRate")).multiply(new BigDecimal(100)).setScale(2));
        String achieveRateStr = ObjectUtil.obj2String(ObjectUtil.obj2BigDecimal(reachingRateInfoMap.get("achieveRate")).multiply(new BigDecimal(100)).setScale(2));
        reachingRateInfoMap.put("applyRate", applyRateStr);
        reachingRateInfoMap.put("achieveRate", achieveRateStr);
        return reachingRateInfoMap;
    }
    
}
