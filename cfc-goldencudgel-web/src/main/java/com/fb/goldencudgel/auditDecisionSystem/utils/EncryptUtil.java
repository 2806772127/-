package com.fb.goldencudgel.auditDecisionSystem.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 加解密工具
 * 
 */
@Service
@SuppressWarnings("restriction")
public class EncryptUtil {

    private static final Logger logger = LoggerFactory.getLogger(EncryptUtil.class);

    private static final String ALGORITHM = "AES/CBC/NoPadding";

    private static final String UTF8 = "UTF-8";

    private static final String AES = "AES";

    // 不足16的倍數的時候，补空格
    private static final int BYTE_BLANK = 32;
    private static final int FIEXD_SIZE = 16;

    /**
     * 
     * 加密算法是：AES - 128 - CBC
     * 
     * @param srcData 明文
     * @param key 密钥
     * @param iv 初始向量
     * @return
     */
    private static byte[] AES_cbc_encrypt(byte[] srcData, byte[] key, byte[] iv) {
        if (srcData == null || srcData.length == 0) {
            return new byte[0];
        }
        int offset = FIEXD_SIZE - srcData.length % 16;
        if (offset != 0) {
            byte[] temp = new byte[srcData.length + offset];
            System.arraycopy(srcData, 0, temp, 0, srcData.length);
            for (int i = srcData.length; i < temp.length; i++) {
                temp[i] = BYTE_BLANK;
            }
            srcData = temp;
        }
        SecretKeySpec keySpec = new SecretKeySpec(key, AES);
        Cipher cipher;
        byte[] encData = null;
        try {
            cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv));
            encData = cipher.doFinal(srcData);
        } catch (Exception e) {
            logger.error("AES加密异常，" + e.getMessage(), e);
        }
        return encData;
    }

    /**
     * @param encData 密文
     * @param key 密钥
     * @param iv 初始向量
     * @return
     */
    private static byte[] AES_cbc_decrypt(byte[] encData, byte[] key, byte[] iv) {
        SecretKeySpec keySpec = new SecretKeySpec(key, AES);
        Cipher cipher;
        byte[] decbbdt = null;
        try {
            cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv));
            decbbdt = cipher.doFinal(encData);
        } catch (Exception e) {
            logger.error("AES解密异常，" + e.getMessage(), e);
        }
        return decbbdt;
    }

    /**
     * 转码
     * 
     * @param data
     * @return
     */
    private static String safeUrlBase64Encode(byte[] data) {
        String encodeBase64 = new BASE64Encoder().encode(data);
        return encodeBase64;
    }

    /**
     * 解码
     * 
     * @param safeBase64Str
     * @return
     * @throws IOException
     */
    private static byte[] safeUrlBase64Decode(final String safeBase64Str) {
        byte[] result = null;
        try {
            result = new BASE64Decoder().decodeBuffer(safeBase64Str);
        } catch (Exception e) {
            logger.error("BASE64编码异常," + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 加密
     * 
     * @param srcData 明文
     * @param key 密钥
     * @param iv 初始向量
     * @return
     */
    public static String encode(String source, String keyStr, String ivStr) throws Exception {
        if (StringUtils.isEmpty(source) || StringUtils.isEmpty(keyStr) || StringUtils.isEmpty(ivStr)) {
            return null;
        }
        byte[] key = keyStr.getBytes();
        byte[] iv = ivStr.getBytes();
        byte[] sourceByte = null;
        try {
            sourceByte = source.getBytes("UTF-8");
        } catch (Exception e) {
            logger.error("UTF8格式化异常," + e.getMessage(), e);
            throw e;
        }
        byte[] temp = AES_cbc_encrypt(sourceByte, key, iv);
        String base64Str = safeUrlBase64Encode(temp);
        logger.info("AES --> BASE64 encode result is: " + base64Str);
        try {
            base64Str = URLEncoder.encode(base64Str, UTF8);
        } catch (Exception e) {
            logger.error("UFL编码异常, " + e.getMessage(), e);
            throw e;
        }
        return base64Str;
    }

    /**
     * 解密
     * 
     * @param source
     * @param key
     * @param iv
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String decode(String source, String keyStr, String ivStr) throws Exception {
        if (StringUtils.isEmpty(source) || StringUtils.isEmpty(keyStr) || StringUtils.isEmpty(ivStr)) {
            return null;
        }
        byte[] key = keyStr.getBytes();
        byte[] iv = ivStr.getBytes();
        String urlDecoderStr = URLDecoder.decode(source);
        byte[] base64Decode = safeUrlBase64Decode(urlDecoderStr);
        byte[] aesDecode = AES_cbc_decrypt(base64Decode, key, iv);
        String target = null;
        try {
            target = new String(aesDecode, UTF8);
        } catch (Exception e) {
            logger.error("UTF8格式化异常," + e.getMessage(), e);
            throw e;
        }
        return StringUtils.isEmpty(target) ? null : target.trim();
    }

    /**
     * 加密：AES ---> BASE64 ----> URLEncoder<br/>
     * 解密：URLDecoder----> BASE64 ----> AES
     * 
     * @param args
     * @throws UnsupportedEncodingException
     */

    public static void main(String[] args) throws Exception {
        String source ="admin";
        String key = "dc3./Dcwv32dL,cq";
        String iv = "di32c3S,42d.,d32";
        String encodeStr = EncryptUtil.encode(source, key, iv);
        System.out.println("加密报文:" + encodeStr);
        String str = "zxp635VbN36MEqUSCxDpMa90HobKS%2F%2FWsWTJlxN%2FcYI%3D";
        String sourceStr = EncryptUtil.decode(encodeStr, key, iv);
        System.out.println("解密报文:" + sourceStr);

    }
}
