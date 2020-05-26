package com.fb.goldencudgel.auditDecisionSystem.utils;

import com.fb.goldencudgel.auditDecisionSystem.domain.commons.SystemConfig;
import com.fb.goldencudgel.auditDecisionSystem.repository.SystemConfigRepository;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * hu
 */
@Component
@Service
public class FtpConnectionFactory {

    private static final Logger logger = LoggerFactory.getLogger(FtpConnectionFactory.class);

    @Autowired
    private SystemConfigRepository systemConfigRepository;

    public FTPClient ftpClient = null;

    public FTPClient getFtpClient() {
        //用户名
        SystemConfig userName = systemConfigRepository.findByID("FTP_USER_NAME");
        String userNameStr = userName == null ?  "" : userName .getKeyvalue();
        //密码
        SystemConfig password = systemConfigRepository.findByID("FTP_USER_PASSWORD");
        String passwordStr = password == null ?  "" : password .getKeyvalue();
        //url
        SystemConfig url = systemConfigRepository.findByID("FTP_URL");
        String urlStr = url == null ?  "" : url .getKeyvalue();

        ftpClient = new FTPClient();
        try {
            ftpClient.setControlEncoding("GBK");//设置编码格式
            ftpClient.connect(urlStr,21);
            ftpClient.login(userNameStr,passwordStr);
            ftpClient.enterLocalPassiveMode();//设置为被动模式
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                ftpClient.disconnect();
                System.out.println("连接ftp服务器失败");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.warn(e.getMessage());
        }
        return ftpClient;
    }

    public void closeFtpConnection(FTPClient ftpConnection) {
        try {
            ftpClient.logout();
        } catch (IOException e) {
            e.printStackTrace();
            logger.warn(e.getMessage());
        }finally{
            if(ftpClient.isConnected()){
                try{
                    ftpClient.disconnect();
                }catch(IOException e){
                    e.printStackTrace();
                    logger.warn(e.getMessage());
                }
            }
        }
    }

    public boolean uploadFile(String soureFilePath, String filePath, String fileName) {
        FTPClient ftpConnection = getFtpClient();

        FileInputStream inputStream = null;
        try {
            ftpConnection.setFileType(ftpConnection.BINARY_FILE_TYPE);
            //判断文件夹是否存在 不存在则创建
            createDirecroty(filePath);
            ftpClient.makeDirectory(filePath);
            ftpClient.changeWorkingDirectory(filePath);
            //上传文件
            inputStream = new FileInputStream(new File(soureFilePath));
            logger.info("start upload file [" + fileName + "]");
            if(ftpClient.storeFile(fileName, inputStream))
                logger.info("upload file [" + fileName + "] success");
            else
                logger.info("upload file [" + fileName + "] fail");
            //关闭ftp连接
            closeFtpConnection(ftpConnection);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.warn(e.getMessage());
        }
        return true;
    }

    //改变目录路径
    public boolean changeWorkingDirectory(String directory) {
        boolean flag = true;
        try {
            flag = ftpClient.changeWorkingDirectory(directory);
            if (flag) {
                logger.info("进入文件夹" + directory + " 成功！");
            } else {
                logger.info("进入文件夹" + directory + " 失败！开始创建文件夹");
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.warn(e.getMessage());
        }
        return flag;
    }

    public void createDirecroty(String remote) throws IOException {
        String directory = remote + "/";
        // 如果远程目录不存在，则递归创建远程服务器目录
        if (!directory.equalsIgnoreCase("/") && !changeWorkingDirectory(new String(directory))) {
            int start = 0;
            int end = 0;
            if (directory.startsWith("/")) {
                start = 1;
            } else {
                start = 0;
            }
            end = directory.indexOf("/", start);
            String path = "";
            String paths = "";
            while (true) {
                String subDirectory = new String(remote.substring(start, end));
                path = path + "/" + subDirectory;
                if (!existFile(path)) {
                    if (makeDirectory(subDirectory)) {
                        changeWorkingDirectory(subDirectory);
                    } else {
                        logger.info("创建目录[" + subDirectory + "]失败");
                        changeWorkingDirectory(subDirectory);
                    }
                } else {
                    changeWorkingDirectory(subDirectory);
                }

                paths = paths + "/" + subDirectory;
                start = end + 1;
                end = directory.indexOf("/", start);
                // 检查所有目录是否创建完毕
                if (end <= start) {
                    break;
                }
            }
        }
    }

    //判断ftp服务器文件是否存在
    public boolean existFile(String path) throws IOException {
        boolean flag = false;
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);
        if (ftpFileArr.length > 0) {
            flag = true;
        }
        return flag;
    }

    //创建目录
    public boolean makeDirectory(String dir) {
        boolean flag = true;
        try {
            flag = ftpClient.makeDirectory(dir);
            if (flag) {
                logger.info("创建文件夹" + dir + " 成功！");
            } else {
                logger.info("创建文件夹" + dir + " 失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn(e.getMessage());
        }
        return flag;
    }

}
