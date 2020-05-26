package com.fb.goldencudgel.auditDecisionSystem.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName WordToPdf
 * @Author panha
 * @Date 2019/5/14 17:51
 * @Version 1.0
 **/
public class WordToPdf {
    
    private static final Logger logger = LoggerFactory.getLogger(WordToPdf.class);
    
    public static String wordToPdf(String wordUrl,String pdfurl){
        InputStream source;
        OutputStream target;
        try {
            logger.info("======>word轉pdf開始，word url: " + wordUrl + ", pdf url: " + pdfurl);
            source = new FileInputStream(wordUrl);
            target = new FileOutputStream(pdfurl);
//            PdfOptions options = PdfOptions.create();
            DocxToPDFConverter converter = new DocxToPDFConverter(source, target, true, true);
            converter.convert();
            logger.info("======>word轉pdf結束");
            return pdfurl;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
