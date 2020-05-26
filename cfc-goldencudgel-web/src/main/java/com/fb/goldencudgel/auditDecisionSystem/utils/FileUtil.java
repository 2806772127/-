package com.fb.goldencudgel.auditDecisionSystem.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;


@Component
public class FileUtil {

    public static String upload(String conText,String fileName){

        try{
            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            if(!path.exists()) path = new File("");
            System.out.println("path:"+path.getAbsolutePath());
            File upload = new File(path.getAbsolutePath(),"static/images/viewFile");
            if(!upload.exists()) upload.mkdirs();
            System.out.println("upload url:"+upload.getAbsolutePath());
           

            byte[] con = hex2byte(conText);
            InputStream in = new ByteArrayInputStream(con);
            File f = new File(upload.getAbsolutePath()+"\\\\"+fileName);
            if(!f.exists())f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            byte[] b = new byte[1024];
            int n = 0;
            while((n=in.read(b))!=-1){
                fo.write(b,0,n);
            }
            fo.flush();
            fo.close();
            in.close();

        }catch (Exception e){
            e.printStackTrace();
            return fileName;
        }

        return fileName;
    }


        /**
         * 二进制转字符串
         * @param b
         * @return
         */
        public static String byte2hex(byte[] b) // 二进制转字符串
        {
            StringBuffer sb = new StringBuffer();
            String stmp = "";
            for (int n = 0; n < b.length; n++) {
                stmp = Integer.toHexString(b[n] & 0XFF);
                if (stmp.length() == 1) {
                    sb.append("0" + stmp);
                } else {
                    sb.append(stmp);
                }

            }
            return sb.toString();
        }

        /**
         * 字符串转二进制
         * @param str 要转换的字符串
         * @return  转换后的二进制数组
         */
        public static byte[] hex2byte(String str) { // 字符串转二进制
            if (str == null)
                return null;
            str = str.trim();
            int len = str.length();
            if (len == 0 || len % 2 == 1)
                return null;
            byte[] b = new byte[len / 2];
            try {
                for (int i = 0; i < str.length(); i += 2) {
                    b[i / 2] = (byte) Integer
                            .decode("0X" + str.substring(i, i + 2)).intValue();
                }
                return b;
            } catch (Exception e) {
                return null;
            }
        }

        /**
         * 删除单个文件
         *
         * @param sPath 被删除文件的文件名
         * @return 单个文件删除成功返回true，否则返回false
         */
        public static boolean deleteFile(String sPath) {
            boolean flag = false;
            File file = new File(sPath);
            // 路径为文件且不为空则进行删除
            if (file.isFile() && file.exists()) {
                file.getAbsoluteFile().delete();
                flag = true;
            }
            return flag;
        }

    /**
     * 判断文件是否存在 不存在创建
     * @param filePath
     */
    public void makeDirectory(String filePath) {
        File file = new File(filePath);
        if(!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }

    //通过url获取附件
    public Map<String,Object> readFileByUrl(String fileUrl) {
        URLConnection connection;
        HttpURLConnection httpConnection;
        Map<String,Object> returnResult = new HashMap<>();
        try {
            URL url = new URL(fileUrl);
            connection = url.openConnection();
            httpConnection = (HttpURLConnection)connection;

            //设置连接超时
            httpConnection.setConnectTimeout(30000);
            httpConnection.setReadTimeout(30000);
            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty("charset","GBK");
            httpConnection.connect();

            InputStream inputStream = httpConnection.getInputStream();
            int fileSize = httpConnection.getContentLength();
            url.openConnection();
            httpConnection.disconnect();
            returnResult.put("fileSize",fileSize);
            returnResult.put("fileContent",inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnResult;
    }
}
