package com.fb.goldencudgel.auditDecisionSystem.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.Model;

public class CommonUtils {

    public static void setAttribute(Model model,String searchData){
        model.addAttribute("searchData",searchData);
        if(StringUtils.isNotBlank(searchData)){
            String[] searchDataArr = searchData.split("&");
            for(String searchDataObj:searchDataArr){
                String[] arr = searchDataObj.split("=");
                if(arr.length>1){
                    model.addAttribute(arr[0],arr[1].replaceAll("%2F", "/").replaceAll("\\+", ""));
                    System.out.println(arr[0]+":"+arr[1].replaceAll("%2F", "/").replaceAll("\\+", ""));
                }
            }
        }
    }
    
    /**
     * @description: 生成trandId，年月日时分秒毫秒+统编
     * @author: mazongjian
     * @param compilationNo
     * @return  
     * @date 2019年8月22日
     */
    public static String generateTrandId(String compilationNo) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Date currTime = new Date();
        String trandId = dateFormat.format(currTime);
        trandId = trandId + compilationNo;
        return trandId;
    }
}
