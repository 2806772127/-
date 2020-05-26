package com.fb.goldencudgel.auditDecisionSystem.security;

import com.fb.goldencudgel.auditDecisionSystem.utils.AesEncryptUtils;
import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyResolver;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class EncryptionPropertyConfig {


    @Bean(name = "encryptablePropertyResolver")
    public EncryptablePropertyResolver encryptablePropertyResolver() {
        return new EncryptionPropertyResolver();
    }

    class EncryptionPropertyResolver implements EncryptablePropertyResolver {

        @Override
        public String resolvePropertyValue(String s) {
            if (StringUtils.isBlank(s)) {
                return s;
            }
            if (s.startsWith("DES@")) {
                return resolveDESValue(s.substring(4));
            }
            return s;
        }

        private String resolveDESValue(String value) {
            // 自定义DES密文解密
            return AesEncryptUtils.decrypt(value, AesEncryptUtils.KEY);
        }
    }
}
