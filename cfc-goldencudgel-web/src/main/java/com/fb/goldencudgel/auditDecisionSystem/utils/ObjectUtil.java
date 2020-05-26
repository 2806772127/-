package com.fb.goldencudgel.auditDecisionSystem.utils;

import java.math.BigDecimal;

/**
 * @author mazongjian
 * @createdDate 2019年3月22日 - 下午11:10:03 
 */
public class ObjectUtil {
    
    /**
     * @description 将对象转为字符串
     */
    public static String obj2String(Object obj) {
        if (obj == null) {
            return "";
        }
        
        return obj.toString();
    }
    
    /**
     * @description 将对象转为BigDecimal
     */
    public static BigDecimal obj2BigDecimal(Object obj) {
        if (obj == null || "".equals(obj)) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(obj2String(obj));
    }
    
    /**
     * @description 将对象转为Short
     */
    public static Short obj2Short(Object obj) {
        if (obj == null || "".equals(obj)) {
            return Short.valueOf("0");
        }
        return Short.valueOf(obj2String(obj));
    }
    /**
     * @description 将对象转为Integer
     */
    public static Integer obj2Integer(Object obj) {
        if (obj == null || "".equals(obj)) {
            return Integer.valueOf("0");
        }
        return Integer.valueOf(obj2String(obj));
    }
    
    /**
     * @description 将对象转为Short
     */
    public static Long obj2Long(Object obj) {
        if (obj == null || "".equals(obj)) {
            return Long.valueOf("0");
        }
        return Long.valueOf(obj2String(obj));
    }
    
    public static String double2String(Double obj) {
        if (obj == null) {
            return "0";
        } else {
            return String.valueOf(obj);
        }
    }
    
    public static Double obj2Double(Object obj) {
        if (obj == null || "".equals(obj)) {
            return Double.valueOf("0");
        }
        return Double.valueOf(obj2String(obj));
    }
    
    public static void main(String[] args) {
        System.out.println(obj2String(obj2BigDecimal("0.4013").multiply(new BigDecimal(100)).setScale(2)));
    }
}
