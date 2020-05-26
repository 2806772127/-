package com.fb.goldencudgel.auditDecisionSystem.utils;

/**
 * @ClassName NumberUtil
 * @Author panha
 * @Date 2019/7/19 16:49
 * @Version 1.0
 **/
public class NumberUtil {
    public static String formatMoney(String title,String str) {
        if (str.indexOf("仟元") < 0 && title.indexOf("仟元") < 0 && str.indexOf("千元") < 0 && title.indexOf("千元") < 0) {
            return str;
        }
        int count = 0;
        String newStr = "";
        for (int i = str.length() - 1; i >= 0; i--) {
            String dh = "";
            String c1 = str.charAt(i) + "";
            boolean isNum = c1.matches("[0-9]");
            if (isNum) {
                if (count == 3) {
                    dh = ",";
                    count = 0;
                }
                count++;
            } else {
                count = 0;
            }
            newStr = c1 + dh + newStr;
        }
        return newStr;
    }
}
