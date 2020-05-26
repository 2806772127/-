package com.fb.goldencudgel.auditDecisionSystem.utils;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public class Download {

    public Boolean upload(@RequestParam("file") MultipartFile file,String location) {
        if (file.isEmpty()) {
            return  false;
        }

        String fileName = new Date().getTime() + file.getOriginalFilename();
        try {
            Path path = Paths.get(location);
            if(Files.notExists(path)) {
                Files.createDirectory(path);
            }
            Files.copy(file.getInputStream(), Paths.get(location, fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }



//下载
    public HttpServletResponse down(String name , HttpServletResponse response,String paths){
        //name是文件名加后缀
        String relativelyPath=System.getProperty("user.dir");
        //String path=relativelyPath+"\\src\\main\\resources\\static\\courseware\\"+name;
        // path是指欲下载的文件的路径。
        String path=relativelyPath+paths+name;

        try {

            File file = new File(path);
            // 取得文件名。
            //String filename = file.getName();
            String filename =new String(file.getName().getBytes("UTF-8"), "ISO-8859-1");
            // 取得文件的后缀名。
            //String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream out = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            out.write(buffer);
            out.flush();
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    //删除指定文件夹下所有文件
//param path 文件夹完整绝对路径
    public  boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                flag = true;
            }
        }
        return flag;
    }



}
