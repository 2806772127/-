package com.fb.goldencudgel.auditDecisionSystem.controller.attachment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.comwave.core.data.domain.QueryPage;
import com.fb.goldencudgel.auditDecisionSystem.domain.attachmentConfig.FbAttachmentConfig;
import com.fb.goldencudgel.auditDecisionSystem.repository.AttachmentConfigRepository;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.AttacmentConfigServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.DataDictServiceImpl;

@Controller
@RequestMapping("/attachment")
public class attachmentController {

    private static final Integer PAGE_SIZE = 20;

    @Autowired
    private DataDictServiceImpl dataDictService;

    @Autowired
    private AttacmentConfigServiceImpl attacmentConfigService;

    @Autowired
    private AttachmentConfigRepository attachmentConfigRepository;

    @RequestMapping("/viewAttachment")
    public String viewItemData(Model model, String backFlag){

        QueryPage<Object[]> dataList2 = dataDictService.findByDictid("BUSINESS_TYPE",new QueryPage<Object[]>(1,9999));

        QueryPage<Object[]> dataList3 = dataDictService.findByDictid("ANNEX_TYPE",new QueryPage<Object[]>(1,9999));

        model.addAttribute("dataLi",dataList2.getContent());
        model.addAttribute("dataLis",dataList3.getContent());
        model.addAttribute("backFlag", backFlag);


        return  "attachment/attachmentData";
    }

    @RequestMapping("/queryAttachment")
    public String queryItemData (Model model,String industryType,String nodeCode,@RequestParam(required = false, defaultValue = "1") Integer curPage){

        QueryPage<Object[]> page = attacmentConfigService.findByInTypeAndNodeCo(industryType,nodeCode,new QueryPage<Object[]>(curPage,PAGE_SIZE));

        model.addAttribute("page",page);
        model.addAttribute("number",page.getContent().size());
        model.addAttribute("datas",page.getContent());
        return "attachment/attachmentList";
    }

    @ResponseBody
    @RequestMapping("/deleteAttachment")
    public String deleteItemData(String attachTypeId){
        attachmentConfigRepository.deleteById(attachTypeId);
        return "success";
    }

    @ResponseBody
    @RequestMapping("/saveAttachment")
    public Map<String, Object> saveItemData(FbAttachmentConfig config,String flag){
        Map<String, Object> result = new HashMap<>();
        //String showOrder = "";
        if ("new".equals(flag)) {
            FbAttachmentConfig isConfig = attachmentConfigRepository.findByConditions(config.getNodeCode(), config.getIndustryType(), config.getAttachTypeCode(), config.getAttactNameCode());
            if (isConfig == null) {
                if (config.getNodeCode().equals("3") || config.getNodeCode().equals("2")) {
                } else {
                    config.setIndustryType("");
                }
             /*   showOrder = attacmentConfigService.findMaxShowOrder();
                config.setShowOrder(ObjectUtil.obj2Integer(showOrder)+1);*/
                attachmentConfigRepository.save(config);
                result.put("flag", true);
                result.put("msg", "儲存成功");
            } else {
                result.put("flag", false);
                result.put("msg", "已存在相同的配置");
            }
        } else {
            if (config != null) {
                FbAttachmentConfig isConfig = attachmentConfigRepository.findByAttachTypeId(config.getAttachTypeId());
                if (config.getNodeCode().equals("3") || "2".equals(config.getNodeCode())) {
                    //config.setShowOrder(isConfig.getShowOrder());
                    attachmentConfigRepository.save(config);
                    result.put("flag", true);
                    result.put("msg", "儲存成功");
                } else {
                    config.setIndustryType("");
                    //config.setShowOrder(isConfig.getShowOrder());
                    attachmentConfigRepository.save(config);
                    result.put("flag", true);
                    result.put("msg", "儲存成功");
                }
            } else {
                result.put("flag", false);
                result.put("msg", "該附件配置不存在");
            }
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/changeName")
    public  List<Object[]>  changeName(String dictId,Model model){
        List<Object[]> result = null;
        QueryPage<Object[]> childFi =  dataDictService.findByChildDictid(dictId,new QueryPage<Object[]>(1,9999));
        if(childFi.getContent().size()>0)
        { result = childFi.getContent();}
        return result;
    }

}
