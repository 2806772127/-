package com.fb.goldencudgel.auditDecisionSystem.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class NumberFormatUtils {

    /**
     * 对数字除以1000，并增加百分位
     * */
    public static String addThousands(String formatNum){
        if(null == formatNum || formatNum.length() == 0){
            return "0.00";
        }else {
            DecimalFormat df = null;
            if (formatNum.indexOf(".") > 0) {
                if (formatNum.length() - formatNum.indexOf(".") - 1 == 0) {
                    df = new DecimalFormat("###,##0.");
                } else if (formatNum.length() - formatNum.indexOf(".") - 1 == 1) {
                    df = new DecimalFormat("###,##0.0");
                } else {
                    df = new DecimalFormat("###,##0.00");
                }
            } else {
                df = new DecimalFormat("###,##0");
            }
            double number = 0.0;
            try {
                number = Double.parseDouble(formatNum);
            } catch (Exception e) {
                number = 0.0;
            }
            return df.format(number);
        }
    }
    /**
     * 对数字除以1000，并增加百分位
     * */
    public static String addThousands(Integer formatNum){
        if(null == formatNum ){
            return "0.00";
        }else {
            String formatNumber = String.valueOf(formatNum);
            DecimalFormat df = null;
            if (formatNumber.indexOf(".") > 0) {
                if (formatNumber.length() - formatNumber.indexOf(".") - 1 == 0) {
                    df = new DecimalFormat("###,##0.");
                } else if (formatNumber.length() - formatNumber.indexOf(".") - 1 == 1) {
                    df = new DecimalFormat("###,##0.0");
                } else {
                    df = new DecimalFormat("###,##0.00");
                }
            } else {
                df = new DecimalFormat("###,##0");
            }
            double number = 0.0;
            try {
                number = Double.parseDouble(formatNumber)*0.001;
            } catch (Exception e) {
                number = 0.0;
            }
            return df.format(number);
        }
    }
    
    /**
     * @description: 格式字符串数字为千分位
     * @author: mazongjian
     * @param formatNum
     * @return  
     * @date 2019年5月18日
     */
    public static String formatNumStrToThousands(String numStr){
        if (StringUtils.isBlank(numStr)){
            return "0";
        } else {
            DecimalFormat df = new DecimalFormat("###,##0");
            double number = 0.0;
            try {
                number = Double.parseDouble(numStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return df.format(number);
        }
    }
    
    /**
     * @description: 判断对象是否是数字
     * @author: mazongjian
     * @param obj
     * @return  
     * @date 2019年6月21日
     */
    public static boolean isNumber(Object obj) {
        String numStr = ObjectUtil.obj2String(obj);
        Pattern pattern = Pattern.compile("(^[1-9]([0-9])*)|^[0]$|((^[0-9]([0-9])*)(\\.)([0-9])*)");
        Matcher matcher = pattern.matcher(numStr);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }
    
    /**
     * @description: 判斷對象是否是純數字
     * @author: mazongjian
     * @param obj
     * @return  
     * @date 2019年8月20日
     */
    public static boolean isDigital(Object obj) {
        String numStr = ObjectUtil.obj2String(obj);
        Pattern pattern = Pattern.compile("(^-?[0-9]+)");
        Matcher matcher = pattern.matcher(numStr);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }
}
