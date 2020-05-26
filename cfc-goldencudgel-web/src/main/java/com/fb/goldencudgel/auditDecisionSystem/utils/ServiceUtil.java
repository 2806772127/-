package com.fb.goldencudgel.auditDecisionSystem.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

/**
 * @author mazongjian
 * @createdDate 2019年4月7日 - 下午6:00:44 
 */
public class ServiceUtil {
    /**
     * @description 從數據集合中篩選出同類型的數據
     */
    public static List<Object[]> getByType(List<Object[]> queryList, String type) {
        List<Object[]> returnList = new ArrayList<>();
        for (Object[] objArr : queryList) {
            if (objArr[0].equals(type)) {
                returnList.add(objArr);
            }
        }
        return returnList;
    }
    
    public static Map<String, Object> splitAddress(String fullAddress, List<Object[]> cityList, List<Object[]> districtList, List<Object[]> streetList) {
        Map<String, Object> addressMap = new HashMap<>();
        // 獲取市編碼
        for (Object[] objArr : cityList) {
            String city = ObjectUtil.obj2String(objArr[2]);
            int index = fullAddress.indexOf(city);
            if (index == 0) {
                addressMap.put("cityCode", objArr[1]);
                addressMap.put("cityName", city);
                fullAddress = fullAddress.substring(city.length());
                break;
            }
        }
        
        // 獲取區編碼
        if (StringUtils.isNotBlank(ObjectUtil.obj2String(addressMap.get("cityCode")))) {
            for (Object[] objArr : districtList) {
                String district = ObjectUtil.obj2String(objArr[2]);
                int index = fullAddress.indexOf(district);
                if (index == 0 && objArr[3].equals(addressMap.get("cityCode"))) {
                    addressMap.put("districtCode", objArr[1]);
                    addressMap.put("districtName", district);
                    fullAddress = fullAddress.substring(district.length());
                    break;
                }
            }
        }
        
        // 獲取街道編碼
        if (StringUtils.isNotBlank(ObjectUtil.obj2String(addressMap.get("districtCode")))) {
            for (Object[] objArr : streetList) {
                String street = ObjectUtil.obj2String(objArr[2]);
                int index = fullAddress.indexOf(street);
                if (index == 0 && objArr[3].equals(addressMap.get("districtCode"))) {
                    addressMap.put("streetCode", objArr[1]);
                    addressMap.put("streetName", street);
                    fullAddress = fullAddress.substring(street.length());
                    break;
                }
            }
        }
        
        int index = fullAddress.indexOf("巷");
        if (index != -1) {
            addressMap.put("comRealTunnel", fullAddress.substring(0, index));
            fullAddress = fullAddress.substring(index + 1);
        }
        
        index = fullAddress.indexOf("弄");
        if (index != -1) {
            addressMap.put("comRealLane", fullAddress.substring(0, index));
            fullAddress = fullAddress.substring(index + 1);
        }
        
        index = fullAddress.indexOf("之");
        if (index == 0) {
            fullAddress = fullAddress.substring(index + 1);
        }
        
        
        index = fullAddress.indexOf("號");
        if (index != -1) {
            addressMap.put("comRealAddnumber", fullAddress.substring(0, index));
            fullAddress = fullAddress.substring(index + 1);
        }
        
        index = fullAddress.indexOf("之");
        if (index == 0) {
            fullAddress = fullAddress.substring(index + 1);
        }
        
        
        index = fullAddress.indexOf("樓");
        if (index != -1) {
            addressMap.put("comRealSpace1", fullAddress.substring(0, index));
            fullAddress = fullAddress.substring(index + 1);
        }
        
        
        index = fullAddress.indexOf("之");
        if (index == 0) {
            fullAddress = fullAddress.substring(index + 1);
        }
        
        index = fullAddress.indexOf("室");
        if (index != -1) {
            addressMap.put("comRealSpace2", fullAddress.substring(0, index));
            fullAddress = fullAddress.substring(index + 1);
        }
        
        return addressMap;
    }
    
    public static List<Object[]> getChildrenByParent(String parent, List<Object[]> childList) {
        List<Object[]> returnList = new ArrayList<>();
        if (StringUtils.isNotBlank(parent) && !CollectionUtils.isEmpty(childList)) {
            for (Object[] objArr : childList) {
                if(parent.equals(objArr[3])) {
                    returnList.add(objArr);
                }
            }
        }
        
        return returnList;
    }
    
    public static List<String> setParamList(String paramStr){
        List<String> params = new ArrayList<String>();
        String [] _param = paramStr.split(",");
        for (int i=0; i < _param.length; i++) {
            params.add(_param[i]);
        }
        return params;
    }
}
