package com.fb.goldencudgel.auditDecisionSystem.utils;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 获取AppSetting.Properties值
 */
//@Configuration
//@ConfigurationProperties(prefix = "goldencudgel", ignoreUnknownFields = false)
//@PropertySource("classpath:config/AppSetting.properties")
//@Data
//@Component
public class AppSettingProperties {

    //附件儲存路径
    public String fileSavePath;

    //附件缓存路径（图片）
    public String fileCachePath;

    public String getFileSavePath() {
        return fileSavePath;
    }

    public void setFileSavePath(String fileSavePath) {
        this.fileSavePath = fileSavePath;
    }

    public String getFileCachePath() {
        return fileCachePath;
    }

    public void setFileCachePath(String fileCachePath) {
        this.fileCachePath = fileCachePath;
    }

}
