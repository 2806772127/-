package com.fb.goldencudgel.auditDecisionSystem.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lowagie.text.pdf.PdfReader;

/**
 * @ClassName PDF2IMAGE
 * @Author panha
 * @Date 2019/5/14 18:13
 * @Version 1.0
 **/
public class PDF2IMAGE {
    private final static Logger logger = LoggerFactory.getLogger(PDF2IMAGE.class);

    public static String pdf2Image(String PdfFilePath, String dstImgFolder, int dpi, String firstImg, String endImg, Map<String, String> params) {
        File file = new File(PdfFilePath);
        PDDocument pdDocument;
        try {
            String imgPDFPath = file.getParent();
            int dot = file.getName().lastIndexOf('.');
            String imagePDFName = file.getName().substring(0, dot); // 获取图片文件名
            String imgFolderPath = null;
            if (dstImgFolder.equals("")) {
                imgFolderPath = imgPDFPath;// 获取图片存放的文件夹路径
            } else {
                imgFolderPath = dstImgFolder;
            }
            StringBuffer imgFilePath = null;
            String imgFilePathResult = "";
            String imgPathResult = "";
            List<BufferedImage> middImgList = new ArrayList<BufferedImage>();
            if (createDirectory(imgFolderPath)) {
                logger.info("======>PDF文檔轉PNG圖片開始");
                pdDocument = PDDocument.load(file);
                PDFRenderer renderer = new PDFRenderer(pdDocument);
                /* dpi越大转换后越清晰，相对转换速度越慢 */
                PdfReader reader = new PdfReader(PdfFilePath);
                int pages = reader.getNumberOfPages();
                List<String> urlList = new ArrayList<String>();
                for (int i = 0; i < pages; i++) {
//                    String imgFilePathPrefix = imgFolderPath + File.separator + imagePDFName;
//                    imgFilePath = new StringBuffer();
//                    imgFilePath.append(imgFilePathPrefix);
//                    imgFilePath.append("_");
//                    imgFilePath.append(String.valueOf(i + 1));
//                    imgFilePath.append(".jpeg");
//                    File dstFile = new File(imgFilePath.toString());
                    BufferedImage image = renderer.renderImageWithDPI(i, dpi);
//                    ImageIO.write(image, "jpeg", dstFile);
//                    urlList.add(imgFilePath.toString());
                    middImgList.add(image);
                }
//                imgFilePathResult = imgFolderPath + File.separator + imagePDFName+".jpeg";
//                ImageUtil.writeImageLocal(imgFilePathResult,urlList,true,false);
                // System.out.println("PDF文档转PNG图片成功！");
                imgPathResult = imgFolderPath + File.separator + imagePDFName+".jpeg";
//                urlsList.add(1,imgFilePathResult);
//                ImageUtil.writeImageLocal(imgPathResult,urlsList,false,false);
                ImageUtil.createOperReportImg(imgPathResult,firstImg,endImg,middImgList,params);
                logger.info("======>PDF文檔轉PNG圖片結束");
            } else {
                logger.info("======>PDF文檔轉PNG圖片失敗：" + "創建" + imgFolderPath + "失敗");
            }
            return imgPathResult;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static boolean createDirectory(String folder) {
        File dir = new File(folder);
        if (dir.exists()) {
            return true;
        } else {
            return dir.mkdirs();
        }
    }
}
