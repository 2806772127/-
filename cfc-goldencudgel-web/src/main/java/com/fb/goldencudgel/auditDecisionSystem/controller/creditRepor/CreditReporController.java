package com.fb.goldencudgel.auditDecisionSystem.controller.creditRepor;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fb.goldencudgel.auditDecisionSystem.service.impl.AttacmentServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.comwave.core.data.domain.QueryPage;
import com.fb.goldencudgel.auditDecisionSystem.dao.FileContextDao;
import com.fb.goldencudgel.auditDecisionSystem.domain.LoanCompany.FbLoanCompany;
import com.fb.goldencudgel.auditDecisionSystem.domain.appointMentRecord.FbAppointmentRecord;
import com.fb.goldencudgel.auditDecisionSystem.domain.attachment.FbAttachment;
import com.fb.goldencudgel.auditDecisionSystem.domain.itemData.ViewDataDictItem;
import com.fb.goldencudgel.auditDecisionSystem.model.User;
import com.fb.goldencudgel.auditDecisionSystem.repository.AppointmentRecordRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.CreditReportFileResitory;
import com.fb.goldencudgel.auditDecisionSystem.repository.FbAttachmentResitory;
import com.fb.goldencudgel.auditDecisionSystem.repository.LoanCompanyRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.ViewDictItemRepository;
import com.fb.goldencudgel.auditDecisionSystem.schema.fileToRuiyang.FileToRuiyangRequest;
import com.fb.goldencudgel.auditDecisionSystem.schema.fileToRuiyang.FileToRuiyangResponse;
import com.fb.goldencudgel.auditDecisionSystem.service.IInterfaceService;
import com.fb.goldencudgel.auditDecisionSystem.service.IMissionStroke;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.AttacmentConfigServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.CollectionServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.CreditReportImpl;
import com.fb.goldencudgel.auditDecisionSystem.utils.NumberFormatUtils;
import com.fb.goldencudgel.auditDecisionSystem.utils.ObjectUtil;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;

@Controller
@RequestMapping("/CreditReport")
public class CreditReporController {

    private final Logger logger = LoggerFactory.getLogger(CreditReporController.class);
    private static final Integer PAGE_SIZE = 20;

    @Autowired
    private CreditReportImpl creditReportService;

    @Autowired
    private LoanCompanyRepository loanCompanyRepository;

    @Autowired
    private AttacmentConfigServiceImpl attacmentConfigService;

    @Autowired
    private CreditReportFileResitory creditReportFileResitory;

    @Autowired
    private FbAttachmentResitory fbAttachmentResitory;

    @Autowired
    private FileContextDao fileContextDao;

    @Autowired
    private CollectionServiceImpl collectionService;

    @Autowired
    private ViewDictItemRepository dictItemRepository;

    @Autowired
    private AppointmentRecordRepository appointmentRecordRepository;
    
    @Autowired
    private IMissionStroke missionStroke;

    @Autowired
    private IInterfaceService interfaceService;

    @Autowired
    private AttacmentServiceImpl attacmentServiceImpl;


    @RequestMapping("/viewCreditReport")
    public String indexApplyIncom(Model model, String backFlag){
    	
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
         
         model.addAttribute("areaList",areaList);
         model.addAttribute("groupList",groupList);
         model.addAttribute("userList",userList);
         model.addAttribute("backFlag", backFlag);
    	
        return  "creditReport/viewCreditReport";
    }

   /* @RequestMapping("/queryCreditReport")
    public  String  queryApplyIncom  ( Model model, String compilationNo, String companyName, String startDate, String endDate,
                                     @RequestParam(required = false, defaultValue = "1") Integer curPage){*/
    @RequestMapping("/queryCreditReport")
    public  String  queryApplyIncom  ( Model model, String compilationNo, String companyName, String startDate, String endDate,
                                     @RequestParam(required = false, defaultValue = "1") Integer curPage,
                                     String areaCode,String groupCode,String userCode){
        
        try {
            if(StringUtils.isNoneBlank(startDate)){
                 startDate =startDate.replace("/","-").trim();
            }
            if(StringUtils.isNoneBlank(endDate)){
                 endDate = endDate.replace("/","-").trim();
            }
        }catch (Exception e)
        {
            logger.info(e.getMessage());
        }
        
       /* QueryPage<Object[]> page = creditReportService.findByConditions(compilationNo,companyName,startDate,
            endDate,new QueryPage<Object[]>(curPage, PAGE_SIZE));*/
        QueryPage<Object[]> page = creditReportService.findByConditions(compilationNo,companyName,startDate,
                endDate,new QueryPage<Object[]>(curPage, PAGE_SIZE),areaCode,groupCode,userCode);


        model.addAttribute("page",page);
        model.addAttribute("creditReportList",page.getContent());

        return "creditReport/creditReportList";
    }

    @RequestMapping("/seeCreditRepor")
    public  String  seeApplyIncom (Model model,String compilationNo,String trandId,String flag,String industryType,String reportingId){

        FbLoanCompany  company =  loanCompanyRepository.findByComplicationNo(compilationNo);
        if(null == company){
            company = new FbLoanCompany();
        }
        String comActualCapital = "0";
        if (company != null && company.getComActualCapital() != null) {
            comActualCapital = NumberFormatUtils.addThousands(company.getComActualCapital());
        }
        model.addAttribute("comActualCapital", comActualCapital);

        Object[] fbAppointmentRecord =  creditReportService.findByCompilationNoAndTrandId(compilationNo, trandId);

        // 查询图片
        List<Object[]> imgList = creditReportService.findImgFiles(compilationNo,trandId);
        // 查询音频
        List<Object[]> muslist = creditReportService.findVedioFiles(compilationNo,trandId);
        List<String> imgAttachTypeList = new ArrayList<String>();
        Map<String, Object> imgAttachTypeNameMap = new HashMap<>();
        Map<String,List<Object[]>> imgMap = new HashMap<String, List<Object[]>>();
        if (!CollectionUtils.isEmpty(imgList)) {
            for (int i = imgList.size() - 1; i >= 0; i--) {
                Object[] img = imgList.get(i);
                // 将附件类型编码去重放到List中
                String imgAttachType = ObjectUtil.obj2String(img[5]);
                // 将附件类型名称与附件类型编码对应放到一个Map中
                String imgAttachTypeName = ObjectUtil.obj2String(img[3]);
                String imgAttachTypeAndName = imgAttachType + imgAttachTypeName;
                if (!imgAttachTypeList.contains(imgAttachType) && StringUtils.isNotBlank(imgAttachType) && StringUtils.isNotBlank(imgAttachTypeName)) {
                    imgAttachTypeList.add(imgAttachTypeAndName);
                    imgAttachTypeNameMap.put(imgAttachTypeAndName, imgAttachTypeName);
                }
                
            }
        }
            
        for (String imgAttachTypeAneName : imgAttachTypeList) {
            List<Object[]> imgGroupByAttachType = new ArrayList<>();
            for (Object[] img : imgList) {
                String imgAttachType = ObjectUtil.obj2String(img[5]);
                String imgAttachTypeName = ObjectUtil.obj2String(img[3]);
                String attachTypeAndName = imgAttachType + imgAttachTypeName;
                if (img != null && StringUtils.isNotBlank(imgAttachType) && StringUtils.isNotBlank(imgAttachTypeName) && imgAttachTypeAneName.equals(attachTypeAndName)) {
                    imgGroupByAttachType.add(img);
                }
            }
            imgMap.put(imgAttachTypeAneName, imgGroupByAttachType);
        }
        QueryPage<Object[]> inty = attacmentConfigService.findByIndustryType(industryType,new QueryPage<Object[]>(1,9999));
        
        //打卡历史
        QueryPage<Object[]> report = creditReportService.findDetailByNoAndTrandid(compilationNo,trandId,new QueryPage<Object[]>(1, 9999));
        List<Object[]> details = report.getContent();
        Object[] detail = details.get(0);
        List<Object[]> recodes = new ArrayList<>();
        if (null != details) {
            recodes = collectionService.findPunchRecodes(compilationNo, trandId, "2");
        }

        List<ViewDataDictItem> enterSourceList = dictItemRepository.findByDictId("ENTER_SOURCE");//案件來源
        List<ViewDataDictItem> organizationList = dictItemRepository.findByDictId("COM_ORGANIZATION");//企業性質
        
        //公司详情（公司登记地址）
        FbLoanCompany vcInfo = loanCompanyRepository.findByComplicationNo(compilationNo);
        if(null == vcInfo) {
            vcInfo = new FbLoanCompany();
        }
        BigDecimal actualCapital = new BigDecimal(StringUtils.isNoneBlank(vcInfo.getComActualCapital())? vcInfo.getComActualCapital().replaceAll(",","") : "0");
        DecimalFormat df = new DecimalFormat(",###,##0"); //没有小数  
        vcInfo.setComActualCapital(df.format(actualCapital));

        //职务数据获取
        List<ViewDataDictItem> positionList = dictItemRepository.findByDictId("POSITION");//职位
        model.addAttribute("positionList",positionList);
        
        model.addAttribute("vcInfo", vcInfo);
        model.addAttribute("organizationList", organizationList);
        model.addAttribute("enterSourceList", enterSourceList);
        model.addAttribute("recodeList", recodes);
        model.addAttribute("imglist", imgMap);
        model.addAttribute("muslist",muslist);
        model.addAttribute("flag",flag);
        model.addAttribute("industryType",industryType);
        model.addAttribute("industryTypeList",inty);
        model.addAttribute("company",company);
        model.addAttribute("fbAppointment",fbAppointmentRecord);
        model.addAttribute("detail", detail);
        model.addAttribute("attachNameList", imgAttachTypeNameMap);
        return  "creditReport/creditReportDetail";
    }


    @RequestMapping("/editCreditRepor")
    public  String  editCreditRepor (Model model,String compilationNo,String trandId
            ,String flag,String industryType, String reportingId,@RequestParam(required = false, defaultValue = "1") Integer curPage){

        FbLoanCompany  company =  loanCompanyRepository.findByComplicationNo(compilationNo);
        if(null == company){
            company = new FbLoanCompany();
        }
        String comActualCapital = "0";
        if (company != null && company.getComActualCapital() != null) {
            comActualCapital = NumberFormatUtils.addThousands(company.getComActualCapital());
        }
        model.addAttribute("comActualCapital", comActualCapital);

        Object[] fbAppointmentRecord =  creditReportService.findByCompilationNoAndTrandId(compilationNo, trandId);

        List<Object[]> imgList = creditReportService.findImgFiles(compilationNo,trandId);
        List<Object[]> muslist = creditReportService.findVedioFiles(compilationNo,trandId);
        List<String> imgAttachTypeList = new ArrayList<String>();
        Map<String, Object> imgAttachTypeNameMap = new HashMap<>();
        Map<String,List<Object[]>> imgMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(imgList)) {
            for (int i = imgList.size() - 1; i >= 0; i--) {
                Object[] img = imgList.get(i);
                // 将附件类型编码去重放到List中
                String imgAttachType = ObjectUtil.obj2String(img[5]);
                // 将附件类型名称与附件类型编码对应放到一个Map中
                String imgAttachTypeName = ObjectUtil.obj2String(img[3]);
                String imgAttachTypeAndName = imgAttachType + imgAttachTypeName;
                if (!imgAttachTypeList.contains(imgAttachType) && StringUtils.isNotBlank(imgAttachType) && StringUtils.isNotBlank(imgAttachTypeName)) {
                    imgAttachTypeList.add(imgAttachTypeAndName);
                    imgAttachTypeNameMap.put(imgAttachTypeAndName, imgAttachTypeName);
                }
                
            }
        }
            
        for (String imgAttachTypeAneName : imgAttachTypeList) {
            List<Object[]> imgGroupByAttachType = new ArrayList<>();
            for (Object[] img : imgList) {
                String imgAttachType = ObjectUtil.obj2String(img[5]);
                String imgAttachTypeName = ObjectUtil.obj2String(img[3]);
                String attachTypeAndName = imgAttachType + imgAttachTypeName;
                if (img != null && StringUtils.isNotBlank(imgAttachType) && StringUtils.isNotBlank(imgAttachTypeName) && imgAttachTypeAneName.equals(attachTypeAndName)) {
                    imgGroupByAttachType.add(img);
                }
            }
            imgMap.put(imgAttachTypeAneName, imgGroupByAttachType);
        }

        QueryPage<Object[]> inty = attacmentConfigService.findByIndustryType(industryType,new QueryPage<Object[]>(1,9999));

        //打卡历史
        QueryPage<Object[]> report = creditReportService.findDetailByNoAndTrandid(compilationNo,trandId,new QueryPage<Object[]>(1, 9999));
        List<Object[]> details = report.getContent();
        Object[] detail = details.get(0);
        List<Object[]> recodes = new ArrayList<>();
        if (null != details) {
            recodes = collectionService.findPunchRecodes(compilationNo, trandId, "2");
        }

        List<ViewDataDictItem> enterSourceList = dictItemRepository.findByDictId("ENTER_SOURCE");//案件來源
        List<ViewDataDictItem> organizationList = dictItemRepository.findByDictId("COM_ORGANIZATION");//企業性質

        //公司详情（公司登记地址）
        FbLoanCompany vcInfo = loanCompanyRepository.findByComplicationNo(compilationNo);
        if(null == vcInfo) {
            vcInfo = new FbLoanCompany();
        }
        BigDecimal actualCapital = new BigDecimal(StringUtils.isNoneBlank(vcInfo.getComActualCapital())? vcInfo.getComActualCapital().replaceAll(",","") : "0");
        DecimalFormat df = new DecimalFormat(",###,##0"); //没有小数  
        vcInfo.setComActualCapital(df.format(actualCapital));

        //职务数据获取
        List<ViewDataDictItem> positionList = dictItemRepository.findByDictId("POSITION");//职位
        model.addAttribute("positionList",positionList);

        model.addAttribute("vcInfo", vcInfo);
        model.addAttribute("organizationList", organizationList);
        model.addAttribute("enterSourceList", enterSourceList);
        model.addAttribute("recodeList", recodes);
        model.addAttribute("imglist", imgMap);
        model.addAttribute("muslist",muslist);
        model.addAttribute("flag",2);
        model.addAttribute("industryType",industryType);
        model.addAttribute("industryTypeList",inty);
        model.addAttribute("company",company);
        model.addAttribute("fbAppointment",fbAppointmentRecord);
        model.addAttribute("detail", detail);
        model.addAttribute("trandId",trandId);
        model.addAttribute("curPage",curPage);
        model.addAttribute("compilationNo",compilationNo);
        //附件类型（弹窗）
        List<Object[]> checkItems = attacmentServiceImpl.findCheckItems("3",industryType,compilationNo);
        if (null == checkItems) {
            checkItems = new ArrayList<>();
        }
        model.addAttribute("checkItems", checkItems);
        model.addAttribute("attachNameList", imgAttachTypeNameMap);

        return  "creditReport/editcreditReport";
    }

    @ResponseBody
    @RequestMapping("/updateCreditReport")
    public  Map<String,Object> updateCreditReport (Model model,String compilationNo,String trandId,
                                                    String imgIds,String deleImgIds){
        Map<String,Object> result = new HashMap<String,Object>();
        try{
            if(trandId.equals("undefined")) {
                trandId="";
            }
            result = creditReportService.saveAttachChange(compilationNo,trandId,imgIds,deleImgIds);
            Boolean flag = (Boolean)result.get("flag");
            if(flag != null && flag){
                //上传附件到瑞阳
                result = fileToRuiyang(compilationNo, trandId);
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            result.put("flag",false);
            result.put("msg","更新失敗,請稍後重試");
        }
        return result;
    }

    private Map<String,Object> fileToRuiyang(String compilationNo,String trandId){
        Map<String,Object> result = new HashMap<String,Object>();
        try{
            //FileToRuiyang
            FbAppointmentRecord fbAppointmentRecord = appointmentRecordRepository.findByIds(compilationNo,trandId,"2");
            FileToRuiyangRequest fileToRuiyangRq = new FileToRuiyangRequest();
            fileToRuiyangRq.setAppointmentid(fbAppointmentRecord.getAppointmentId());
            fileToRuiyangRq.setCompilationno(compilationNo);
            fileToRuiyangRq.setZjcreditnum(fbAppointmentRecord.getZjCreditNum());
            FileToRuiyangResponse fileToRuiyangResponse = interfaceService.fileToRuiyang(fileToRuiyangRq);
            if(fileToRuiyangResponse != null && StringUtils.isNotBlank(fileToRuiyangResponse.getEMSGID())){
                if("0000".equals(fileToRuiyangResponse.getEMSGID())){
                    result.put("flag",true);
                    result.put("msg","附件同步到睿陽成功！");
                    logger.info("附件同步到睿陽成功！");
                }else{
                    result.put("flag",false);
                    result.put("msg","附件同步到睿陽失敗！"+fileToRuiyangResponse.getEMSGID()+","+fileToRuiyangResponse.getEMSGTXT());
                    logger.error("附件同步到睿陽失敗！"+fileToRuiyangResponse.getEMSGID()+","+fileToRuiyangResponse.getEMSGTXT());
                }
            }else{
                result.put("flag",false);
                result.put("msg","附件同步到睿陽失敗！請求失敗！");
                logger.error("附件同步到睿陽失敗！請求失敗！");
            }
        }catch (Exception e){
            result.put("flag",false);
            result.put("msg","附件同步到睿陽失敗！");
            logger.error("附件同步到睿陽失敗！"+e.getMessage(),e);
        }
        return result;
    }

   /* @ResponseBody
    @RequestMapping("/updateCreditReport")
    public  String updateSeeApplyIncom (Model model,String compilationNo,String trandId,String imgIds,String deleImgIds){

        FbLoanCompany  company =  loanCompanyRepository.findByComplicationNo(compilationNo);

        String[] ids = imgIds.split(",");
        for(int i=0;i<ids.length;i++){
          Optional<FbCreditReportingFile> cf = creditReportFileResitory.findById(ids[1]);
          FbCreditReportingFile f = cf.get();
          if(null != f){
              f.setCompilationNo(compilationNo);
              f.setCompilationName(company.getComName());
              f.setTrandId(trandId);
              creditReportFileResitory.save(f);
          }
          Optional<FbAttachment> fa = fbAttachmentResitory.findById(ids[i]);
          FbAttachment fba = fa.get();
          if(null!=fba){
              fba.setCompilationNo(compilationNo);
              fba.setUpdateTime(new Date());
              fbAttachmentResitory.save(fba);
          }
        }
        String[] deIds = deleImgIds.split(",");
        for(int j=0;j<deIds.length;j++){
            creditReportFileResitory.deleteById(deIds[j]);
            fbAttachmentResitory.deleteById(deIds[j]);
            fileContextDao.deleteById(deIds[j]);
        }

        return  "success";
    }*/

/*

    @ResponseBody
    @RequestMapping(value = "/upload")
    public  String upload(MultipartHttpServletRequest request,String fileName,String fileType,String fileFacode,String fileFaName,
                         String fileChCode,String fileChName){

        //1.解决跨域访问domain  localhost
        String damain = "localhost";
        String setDomain = "<script >document.domain = \""+damain+"\";</script>";

        String resultids = "";
        String jsonStr ="";
        Map<String,String> resMap = new HashMap<String, String>();
        List<MultipartFile> files = request.getFiles("file");
        MultipartFile file = null;
        for (int i = 0; i < files.size(); ++i) {
                file = files.get(i);
                if (!file.isEmpty()) {
                    try {
                        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
                        byte[] bytes = file.getBytes();
                        String te =  FileUtil.byte2hex(bytes);
                        FileContext fileContext = new FileContext();
                        fileContext.setFileId(uuid);
                        fileContext.setFileContext(te);
                        fileContextDao.insertOne(fileContext);
                        FbCreditReportingFile cfile = new FbCreditReportingFile();
                        FbAttachment fafile = new FbAttachment();
                        cfile.setAttachId(uuid);
                        cfile.setCompilationNo("common");//预设
                        cfile.setTrandId("common");//预设
                        cfile.setCreateTime(new Date());
                        fafile.setAttachId(uuid);
                        fafile.setFileContextId(uuid);
                        fafile.setCompilationNo("common");//预设
                        fafile.setAttachTypeCode(fileFacode);
                        fafile.setAttachTypeName(fileFaName);
                        fafile.setAttactNameCode(fileChCode);
                        fafile.setAttactName(fileChName);
                        fafile.setFileCategory((double)Integer.parseInt(fileType));
                        fafile.setCreateTime(new Date());
                        creditReportFileResitory.save(cfile);
                        fbAttachmentResitory.save(fafile);

                       resultids += uuid+",";
                    } catch (Exception e) {

                        return "You failed to upload " + i + " => "
                                + e.getMessage();
                    }
                } else {
                    return "You failed to upload " + i
                            + " because the file was empty.";
                }
        }
        resMap.put("id",resultids);
        return    setDomain+jsonStr;
    }
*/
    /**附件上传*/
    @ResponseBody
    @RequestMapping(value = "/upload",method = {RequestMethod.POST})
    public Map<String,Object> upload(@RequestBody String[] fileStrs) throws IOException {
        Map<String,Object> map = new HashMap<>();
        if(null == fileStrs){
            map.put("flag",false);
            map.put("msg","附件上傳失敗");
        }else{
            map = creditReportService.saveMongoFile(fileStrs);
        }

        return map;
    }

    @ResponseBody
    @RequestMapping("/showView")
    public  String showView(String id,String type){

        String url = creditReportService.getAttachUrl(id);
        int relativeUrlIndex = url.indexOf("/fileService");
        String relativeUrl = url.substring(relativeUrlIndex);
        return relativeUrl;
    }

    @ResponseBody
    @RequestMapping("/changeName")
    public String changeName(String industryType,String fileFacode,String compCode){
        String result =  attacmentServiceImpl.findByCheckItems(fileFacode,industryType,compCode,"3");
        return result;
    }

    @ResponseBody
    @RequestMapping("/deleteFilsFlag")
    public  String deleteFileFlag(String id){
       Optional<FbAttachment> fba = fbAttachmentResitory.findById(id);
       FbAttachment f = fba.get();
       if(f!=null){
           f.setDeleteFlag((double)1);
           fbAttachmentResitory.save(f);
           return "success";
       }
       return "fail";
    }

    /**
     * 校验附件是否上传成功
     */
    @RequestMapping("/checkFileIdExsit")
    @ResponseBody
    public Map<String, Object> checkFileIdExsit(Model model, String id) {
        //根据检查项查找附件名称
        Boolean flag  = collectionService.checkFileIdExsit(id);
        Map<String, Object> map = new HashMap<>();
        map.put("flag", flag);
        return map;
    }

}
