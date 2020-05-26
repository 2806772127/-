package com.fb.goldencudgel.auditDecisionSystem.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class LogFileUtil {

    /**
     * 分页获取日志文件
     */
    public static Map<String, Object> queryLogFilesByPage(int curPage, int pageSize, String type,
                                                          String dirPath) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<String[]> logFileDetailList = queryLogFiles(type, dirPath);
        int total = logFileDetailList.size();
        int pageTotal = total / pageSize + (total % pageSize > 0 ? 1 : 0);
        int startIndex = (curPage - 1) * pageSize;
        int endIndex = curPage * pageSize;
        endIndex = endIndex > total ? total : endIndex;
        List<String[]> recordList = logFileDetailList.subList(startIndex, endIndex);
        map.put("recordList", recordList);
        map.put("pageTotal", pageTotal);
        map.put("total", total);
        map.put("curPage", curPage);
        map.put("pageSize", pageSize);
        map.put("logFileDetailList", logFileDetailList);
        // System.out.println("pageTotal:" + pageTotal + ",total:" + total + ",curPage:" + curPage
        // + ",pageSize:" + pageSize + ",startIndex:" + startIndex + ",endIndex:" + endIndex);
        // for (String[] arr : recordList) {
        // System.out
        // .println(arr[0] + ",\t" + arr[1] + ",\t" + arr[2] + ",\t" + arr[3] + ",\t" + arr[4]);
        // }
        return map;
    }

    /**
     * 获取日志文件
     */
    public static List<String[]> queryLogFiles(String type, String dirPath) {
        List<String[]> logFileDetailList = new ArrayList<String[]>();
        List<File> logFileList = new ArrayList<File>();
        getLogFileList(logFileList, dirPath);
        for (File file : logFileList) {
            String name = file.getName();
            String createTime = getFileCreateTime(file);
            String updateTime = getFileUpdateTiem(file);
            String absolutePath = file.getAbsolutePath();
            String[] logFileDetail = {type, name, createTime, updateTime, absolutePath};
            logFileDetailList.add(logFileDetail);
        }
        // 根据修改时间逆序排序
        Collections.sort(logFileDetailList, new Comparator<String[]>() {
            @Override
            public int compare(String[] detail1, String[] detail2) {
                return detail2[3].compareTo(detail1[3]);
            }
        });
        // for (String[] arr : logFileDetailList) {
        // System.out
        // .println(arr[0] + ",\t" + arr[1] + ",\t" + arr[2] + ",\t" + arr[3] + ",\t" + arr[4]);
        // }
        return logFileDetailList;
    }

    /**
     * 递归获取所有LOG文件
     */
    private static void getLogFileList(List<File> logFileList, String dirPath) {
        File dir = new File(dirPath);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (null == files || files.length == 0) {
                System.out.println("文件夹是空的!");
                return;
            } else {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // System.out.println("文件夹:" + file.getAbsolutePath());
                        getLogFileList(logFileList, file.getAbsolutePath());
                    } else if (file.getName().endsWith(".log")) {
                        logFileList.add(file);
                        // System.out.println("文件:" + file.getAbsolutePath());
                    }
                }
            }
        }
    }

    /**
     * 获取文件创建时间
     */
    public static String getFileCreateTime(File file) {
        String createTime = "";
        try {
            BasicFileAttributes attributes =
                    Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            LocalDateTime fileCreationTime =
                    LocalDateTime.ofInstant(attributes.creationTime().toInstant(), ZoneId.systemDefault());
            int monthValue = fileCreationTime.getMonthValue();
            int dayValue = fileCreationTime.getDayOfMonth();
            int hor = fileCreationTime.getHour();
            int mint = fileCreationTime.getMinute();
            int sec = fileCreationTime.getSecond();
            createTime =
                    fileCreationTime.getYear() + "-" + (monthValue < 10?("0"+monthValue):(""+monthValue)) + "-"
                            + (dayValue < 10?("0"+dayValue):(""+dayValue)) + " " + (hor < 10?("0"+hor):(""+hor)) + ":"
                            + (mint < 10?("0"+mint):(""+mint)) + ":" + (sec < 10?("0"+sec):(""+sec));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return createTime;
    }

    /**
     * 获取文件修改时间
     */
    public static String getFileUpdateTiem(File file) {
        long lastTime = file.lastModified();
        return DateTimeUtils.dateToString(new Date(lastTime));
    }

    public static void main(String[] args) {
        // LogFileUtil.queryLogFiles("WEB", "D:/logTest");
        LogFileUtil.queryLogFilesByPage(1, 3, "WEB", "D:/logTest");
    }

}
