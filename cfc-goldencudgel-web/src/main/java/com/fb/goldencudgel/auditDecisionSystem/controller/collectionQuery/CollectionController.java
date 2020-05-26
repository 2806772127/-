package com.fb.goldencudgel.auditDecisionSystem.controller.collectionQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fb.goldencudgel.auditDecisionSystem.service.impl.AttacmentServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.comwave.core.data.domain.QueryPage;
import com.fb.goldencudgel.auditDecisionSystem.model.User;
import com.fb.goldencudgel.auditDecisionSystem.repository.FbProductRepository;
import com.fb.goldencudgel.auditDecisionSystem.service.IMissionStroke;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.CollectionServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.utils.NumberFormatUtils;
import com.fb.goldencudgel.auditDecisionSystem.utils.ObjectUtil;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;
/**
 * 拜访笔记控制类
 * @auther David
 */
@Controller
@RequestMapping("/collectionQuery")
public class CollectionController {
    private static final Integer PAGE_SIZE = 20;

    @Autowired
    private IMissionStroke missionStroke;

    @Autowired
    private CollectionServiceImpl collectionService;
    
    @Autowired
    private FbProductRepository fbProductRepository;

    @Autowired
    private AttacmentServiceImpl attacmentServiceImpl;
    
    /**
     * @description: 拜访笔记首页
     * @param model
     * @param backFlag
     * @return  
     * @date 2019年8月2日
     */
    @RequestMapping("/queryCollectionRecords")
    public String queryCollectionRecords(Model model, String backFlag) {

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

        Map<String, String> productList = new LinkedHashMap<>();
        productList.put("ALL", "全產品合計");
        productList.put("COM", "公司戶貸款小計");
        productList.put("CREDIT", "企業主信貸");
        productList.put("HOUSE", "企業主房貸");

        model.addAttribute("areaList",areaList);
        model.addAttribute("groupList",groupList);
        model.addAttribute("userList",userList);
        model.addAttribute("productList",productList);
        model.addAttribute("backFlag", backFlag);

        return "collection/collectionQuery";
    }

    /**
     * @description: 拜访笔记首页里的列表
     * @param model
     * @param compilationNo
     * @param visitName
     * @param startDate
     * @param endDate
     * @param curPage
     * @param createUsers
     * @param userGroups
     * @param userAreas
     * @return  
     * @date 2019年8月2日
     */
    @RequestMapping("/viewQueryVisit")
    public String viewQueryVisit(Model model, String compilationNo, String visitName,  @RequestParam(required = false) String startDate,
                                 @RequestParam(required = false) String endDate,
                                 @RequestParam(required = false, defaultValue = "1") Integer curPage,String createUsers,String userGroups,String userAreas) {
        startDate =startDate.replace("/","-");
        startDate = startDate.trim();
        endDate = endDate.replace("/","-");
        endDate = endDate.trim();
        if (!StringUtils.isBlank(endDate)) {
            endDate = endDate + " 23:59";
        }
        
        QueryPage<Object[]> page =
                collectionService.findByConditions(compilationNo, visitName, startDate, endDate, new QueryPage<>(curPage, PAGE_SIZE),createUsers,userGroups,userAreas);

        model.addAttribute("page", page);
        model.addAttribute("collectionList", page.getContent());
        
        model.addAttribute("curPage",curPage);
        
        return "collection/collectionQueryList";
    }

    /**
     * @description: 拜访笔记详情页面
     * @param model
     * @param compilationNo
     * @param trandId
     * @param appointmentDate
     * @param type
     * @return  
     * @date 2019年8月2日
     */
    @RequestMapping("/visitDetail")
    public String visitDetail(Model model, String compilationNo, String trandId, String appointmentDate, @RequestParam(required = false, defaultValue = "1")String type) {

        String url =  "collection/visitDetail";
        if("2".equals(type)){
            url = "collection/editDetail";
        }
        //基本信息
        QueryPage<Object[]> page = collectionService.findVCInfo(compilationNo,trandId, appointmentDate);
        if(page.getContent().size() == 0){
            return  "collection/visitDetail";
        }
        Object[] visitInfos = page.getContent().get(0);
        model.addAttribute("visitInfos",visitInfos);
         //打卡历史
        List<Object[]> recodes =  collectionService.findPunchRecodes(compilationNo,trandId, "1");
        if(recodes == null){
            recodes = new ArrayList<>();
        }
        model.addAttribute("recodes",recodes);
        //附件
        List<Object[]> imgList = collectionService.findImgFiles(compilationNo, trandId);//图片
        List<Object[]> muslist = collectionService.findVedioFiles(compilationNo, trandId);//音频
        List<String> imgAttachTypeList = new ArrayList<>();
        Map<String, Object> imgAttachTypeNameMap = new HashMap<>(); 
        Map<String, List<Object[]>> imgMap = new HashMap<>();
        if (null != imgList) {
            for (int i = imgList.size() - 1; i >= 0; i--) {
                Object[] img = imgList.get(i);
                String imgAttachType = ObjectUtil.obj2String(img[3]);
                String imgAttachTypeName = ObjectUtil.obj2String(img[6]);
                String imgAttachTypeAndName = imgAttachType + imgAttachTypeName;
                if (!imgAttachTypeList.contains(imgAttachTypeAndName) && StringUtils.isNotBlank(imgAttachType) && StringUtils.isNotBlank(imgAttachTypeName)) {
                    imgAttachTypeList.add(imgAttachTypeAndName);
                    imgAttachTypeNameMap.put(imgAttachTypeAndName, imgAttachTypeName);
                }
            }
            for (String imgAttachTypeAndName : imgAttachTypeList) {
                List<Object[]> imgGroupByAttachType = new ArrayList<>();
                for (Object[] img : imgList) {
                    String imgAttachType = ObjectUtil.obj2String(img[3]);
                    String imgAttachTypeName = ObjectUtil.obj2String(img[6]);
                    String attachTypeAndName = imgAttachType + imgAttachTypeName;
                    if (img != null && StringUtils.isNotBlank(imgAttachType) && StringUtils.isNotBlank(imgAttachTypeName) && imgAttachTypeAndName.equals(attachTypeAndName)) {
                        imgGroupByAttachType.add(img);
                    }
                }
                imgMap.put(imgAttachTypeAndName, imgGroupByAttachType);
            }
        }
        model.addAttribute("imglist", imgMap);
        model.addAttribute("muslist", muslist);
        model.addAttribute("voiceLenth",muslist.size());
        //附件类型（弹窗）
        List<Object[]> checkItems = attacmentServiceImpl.findCheckItems("1","",compilationNo);
        if (null == checkItems) {
            checkItems = new ArrayList<>();
        }
        model.addAttribute("checkItems", checkItems);
        model.addAttribute("attachNameList", imgAttachTypeNameMap);

        //近一年营收
        String lastYearIncome = (visitInfos==null || visitInfos[5] == null) ? "": NumberFormatUtils.formatNumStrToThousands(visitInfos[5].toString());
        if (lastYearIncome.equals("0")) {
        	lastYearIncome="";
        }
        //公司目前在銀行週轉借款餘額
        String bankLoanBalance = (visitInfos==null || visitInfos[6]==null)?"":NumberFormatUtils.formatNumStrToThousands(visitInfos[6].toString());
        //資本額（NT$）
        String comActualCapital = (visitInfos==null ||visitInfos[22]==null)?"":NumberFormatUtils.addThousands(visitInfos[22].toString());
        System.out.println(comActualCapital);
        
        //从fb_product表获取产品名称
        List<String> prodName = fbProductRepository.queryProdName();
        model.addAttribute("prodName",prodName);
        model.addAttribute("comActualCapital",comActualCapital);
        model.addAttribute("bankLoanBalance", bankLoanBalance);
        model.addAttribute("lastYearIncome",lastYearIncome);
        
        return url;
    }

    /**
     * 地图定位
     **/
    @RequestMapping("/openLocation")
    public String openLocation(Model model, String locationTime, String locationAddress, String longitude, String latitude) {
        model.addAttribute("locationTime", locationTime);
        model.addAttribute("locationAddress", locationAddress);
        model.addAttribute("longitude", longitude);
        model.addAttribute("latitude", latitude);
        return "collection/googleMapApi";
    }

    /**
     * 附件名称
     */
    @RequestMapping("/changeAttachName")
    @ResponseBody
    public String changeAttachName(Model model, String checkItem,String compCode) {
        //根据检查项查找附件名称
        String result = attacmentServiceImpl.findByCheckItems(checkItem,"",compCode,"1");
        return result;
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

    /**附件上传*/
    @ResponseBody
    @RequestMapping(value = "/upload",method = {RequestMethod.POST})
    public Map<String,Object> upload(@RequestBody String[] fileStrs) throws IOException {
        Map<String,Object> map = new HashMap<>();
        if(null == fileStrs){
            map.put("flag",false);
            map.put("msg","附件上傳失敗");
        }else{
            map = collectionService.saveMongoFile(fileStrs);
        }
        
        return map;
    }

    @ResponseBody
    @RequestMapping("/updateCreditReport")
    public  Map<String,Object> updateSeeApplyIncom (Model model,String compilationNo,String compilationName,String trandId,
                                        String imgIds,String deleImgIds,String visitId,String applyProductList){
        Map<String,Object> map = collectionService.saveAttachChange(compilationNo,compilationName,trandId,imgIds,deleImgIds,visitId,applyProductList);
        return map;
    }

    /**查詢附件內容*/
    @ResponseBody
    @RequestMapping("/showView")
    public  String showView(String id){
        String url = collectionService.getAttachUrl(id);
        int relativeUrlIndex = url.indexOf("/fileService");
        String relativeUrl = url.substring(relativeUrlIndex);
        return relativeUrl;
    }

    /**
     * 修改附件刪除標誌
     * @param  id 附件id
     * */
    @ResponseBody
    @RequestMapping("/deleteFilsFlag")
    public  String deleteFileFlag(String id){
       String result = collectionService.changeAttachDeleteFlag(id);
        return result;
    }

}
