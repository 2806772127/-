package com.fb.goldencudgel.auditDecisionSystem.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * hu
 */
public class CsvFileUtil {

    private static final Logger logger = LoggerFactory.getLogger(CsvFileUtil.class);

    /**
     * 写入到CSV附件
     * @param data
     * @param path
     */
    public static void writeCSV(ArrayList<ArrayList<String>> data, String path)
    {
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path),"UTF-8"));
            out.write(new String(new byte[] { (byte) 0xEF, (byte) 0xBB,(byte) 0xBF }));
            for (int i = 0; i < data.size(); i++)
            {
                ArrayList<String> onerow = data.get(i);
                for (int j = 0; j < onerow.size(); j++)
                {
                    out.write("\t" + onerow.get(j) + "\t");
                    out.write(",");
                }
                out.newLine();
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.error("写入CSV附件内容异常：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 读取CSV内容
     * @param path
     * @param isReadHead
     * @return
     */
    public static ArrayList<ArrayList<String>> readCSV(String path , boolean isReadHead)
    {
        try {
            File file = new File(path);
            if(!file.isFile() || !file.exists()) {
                logger.info("file [" + path + "] isn't exist");
                return new ArrayList<ArrayList<String>>();
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(path),"GBK"));
            ArrayList<ArrayList<String>> alldata = new ArrayList<ArrayList<String>>();
            String line;
            String[] onerow;
            if(!isReadHead) {
                in.readLine();//第一行信息，为标题信息，不用，如果需要，注释掉
            }
            while ((line=in.readLine())!=null) {
                onerow = line.split(",");  //默认分割符为逗号，可以不使用逗号
                List<String> onerowlist = Arrays.asList(onerow);
                ArrayList<String> onerowaArrayList = new ArrayList<String>(onerowlist);
                alldata.add(onerowaArrayList);
            }
            in.close();
            return alldata;
        } catch (Exception e) {
            logger.error("获取CSV附件内容异常：" + e.getMessage());
            return null;
        }
    }
    
    /**
     * 写入到CSV附件
     * @param data
     * @param path
     */
    public static void writeCSVTwo(ArrayList<ArrayList<String>> data, String path)
    {
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path),"GBK"));
            for (int i = 0; i < data.size(); i++)
            {
                ArrayList<String> onerow = data.get(i);
               
                for (int j = 0; j < onerow.size(); j++)
                {
                	//csv写入以","写入，如果从数据库读取的数据包含","，处理
                	int length = onerow.get(j).split(",").length;
                	String onerowData = onerow.get(j);
                	if (length>1) {
                		onerowData = onerowData.replace(",", "，");
					}
                    out.write("\t" + onerowData + "\t");
                    out.write(",");
                }
                out.newLine();
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.error("写入CSV附件内容异常：" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ArrayList<ArrayList<String>> data = readCSV("G:/APPLY_RECORD_TEMP.csv",true);
        writeCSV(data,"G:/APPLY_RECORD_TEMP_20190618.csv");
        logger.error("获取附件内容：" + data);
    }
}
