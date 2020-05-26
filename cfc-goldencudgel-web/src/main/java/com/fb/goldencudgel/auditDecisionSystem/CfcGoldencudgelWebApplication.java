package com.fb.goldencudgel.auditDecisionSystem;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
@EnableEncryptableProperties
@EnableScheduling
public class CfcGoldencudgelWebApplication {
    //@Bean
   // public LocaleResolver localeResolver() {
     //   SessionLocaleResolver slr =new SessionLocaleResolver();
        //设置默认区域,
     //   slr.setDefaultLocale(Locale.CHINA);
    //    return slr;
   // }
    public static void main(String[] args) {
        SpringApplication.run(CfcGoldencudgelWebApplication.class, args);
    }

}

