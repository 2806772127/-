package com.fb.goldencudgel.auditDecisionSystem.utils;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class ResttemplateUtile {

    /**
     *
     * @param sendType http/https
     * @return
     */
    public RestTemplate getRestTemplate(String sendType,String timeOut){
        RestTemplate restTemplate = null;
        int timeOuts = StringUtils.isEmpty(timeOut) ? 1000 : Integer.parseInt(timeOut);
        SimpleClientHttpRequestFactory requestFactory = null;
        if("https".equals(sendType.toLowerCase())) {
            requestFactory = new HttpsClientRequestFactory();
        } else {
            requestFactory = new SimpleClientHttpRequestFactory();
        }
        requestFactory.setConnectTimeout(timeOuts);//连接超时
        requestFactory.setReadTimeout(timeOuts);//等待超时
        restTemplate = new RestTemplate(requestFactory);
        // 解决(响应数据可能)中文乱码 的问题
        List<HttpMessageConverter<?>> converterList = restTemplate.getMessageConverters();
        converterList.remove(1); // 移除原来的转换器
        HttpMessageConverter<?> converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);//设置字符编码为utf-8
        converterList.add(1, converter); // 添加新的转换器(注:convert顺序错误会导致失败)
        restTemplate.setMessageConverters(converterList);

        return restTemplate;
    }
}
