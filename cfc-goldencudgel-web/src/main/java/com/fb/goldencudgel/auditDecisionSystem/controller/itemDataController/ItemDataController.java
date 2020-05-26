package com.fb.goldencudgel.auditDecisionSystem.controller.itemDataController;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.comwave.core.data.domain.QueryPage;
import com.fb.goldencudgel.auditDecisionSystem.domain.attachmentConfig.FbAttachmentConfig;
import com.fb.goldencudgel.auditDecisionSystem.domain.itemData.ViewDataDict;
import com.fb.goldencudgel.auditDecisionSystem.domain.itemData.ViewDataDictItem;
import com.fb.goldencudgel.auditDecisionSystem.repository.DataDictRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.FbAttachmentConfigRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.ViewDictItemRepository;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.DataDictServiceImpl;

@Controller
@RequestMapping("/itemData")
public class ItemDataController {

    private final Logger logger = LoggerFactory.getLogger(ItemDataController.class);
    private static final Integer PAGE_SIZE = 20;

    @Autowired
    private DataDictServiceImpl dataDictService;

    @Autowired
    private ViewDictItemRepository viewDictItemRepository;

    @Autowired
    private DataDictRepository dataDictRepository;
    
    @Autowired
    private FbAttachmentConfigRepository attachmentConfigRepository;

    @RequestMapping("/viewItemData")
    public String viewItemData(Model model){

//        QueryPage<Object[]> dataList = dataDictService.findByDictid("DATA_FA");
        QueryPage<Object[]> dataList = dataDictService.findDateDice(null);
        model.addAttribute("dataList",dataList.getContent());
        model.addAttribute("page",new QueryPage<Object[]>() );
        return  "itemData/viewItemData";
    }

    @RequestMapping("/queryItemData")
    public String queryItemData (Model model,String dictId,@RequestParam(required = false, defaultValue = "1") Integer curPage){

       QueryPage<Object[]> page = dataDictService.findByDictid(dictId,new QueryPage<Object[]>(curPage,PAGE_SIZE));

       model.addAttribute("page",page);
       model.addAttribute("number",page.getContent().size());
       model.addAttribute("datas",page.getContent());
       model.addAttribute("curPage",curPage);
       return "itemData/itemDataList";
    }

    @ResponseBody
    @RequestMapping("/deleteItemData")
    public String deleteItemData(String dictId,String itemCode){
        viewDictItemRepository.deleteByIdAndCode(dictId,itemCode);

        //刪除上級數據
        List<ViewDataDict> dataDictlist = dataDictRepository.findByDictId(itemCode);
        dataDictRepository.deleteAll(dataDictlist);
        //刪除下級數據
        viewDictItemRepository.deleteByDictId(itemCode);
        return "success";
    }
    
    /**
     * @description: 新增选项管理时保存数据
     * @author: mazongjian
     * @param item
     * @return  
     * @date 2019年8月10日
     */
    @ResponseBody
    @RequestMapping("/saveNewItemData")
    public String saveNewItemData(ViewDataDictItem item) {
        String result = "success";
        try {
            ViewDataDictItem isItem = viewDictItemRepository.findViewDataDictItemCode(item.getDictId(), item.getItemCode());
            if (isItem == null) {
                viewDictItemRepository.save(item);
                if ("ANNEX_TYPE".equals(item.getDictId())) {
                    ViewDataDict newDataDict = new ViewDataDict();
                    newDataDict.setDictId(item.getItemCode());
                    newDataDict.setDictName(item.getItemName());
                    dataDictRepository.saveAndFlush(newDataDict);
                }
            } else {
                result = "error";
            }
        } catch (Exception e) {
            result = "error";
            e.printStackTrace();
        }
        
        return result;
    }

    /**
     * @description: 修改选项管理时保存数据
     * @author: mazongjian
     * @param item
     * @param olditemCode
     * @return  
     * @date 2019年8月10日
     */
    @ResponseBody
    @RequestMapping("/saveItemData")
    public String saveItemData(ViewDataDictItem item, String olditemCode){
        String result = "success";
        try {
            // 查询数据字典细项
            ViewDataDictItem dataDicttem = viewDictItemRepository.findViewDataDictItemCode(item.getDictId(), item.getItemCode());
            dataDicttem.setItemCode(item.getItemCode());
            dataDicttem.setItemName(item.getItemName());
            viewDictItemRepository.save(dataDicttem);
            
            if ("ANNEX_TYPE".equals(item.getDictId())) {
                List<ViewDataDict> dataDictlist = dataDictRepository.findByDictId(item.getItemCode());
                ViewDataDict dataDict = dataDictlist.get(0);
                dataDict.setDictName(item.getItemName());
                dataDictRepository.saveAndFlush(dataDict);
                
                // 更新数据字典的附件类型名称时，更新附件配置里的附件类型名称
                List<FbAttachmentConfig> attachmentConfigList = attachmentConfigRepository.findByAttachTypeCode(item.getItemCode());
                for (FbAttachmentConfig attachmentConfig : attachmentConfigList) {
                    attachmentConfig.setAttachTypeName(item.getItemName());
                }
                attachmentConfigRepository.saveAll(attachmentConfigList);
            }
        } catch (Exception e) {
            result = "error";
            e.printStackTrace();
        }
        return result;
    }
    
}
