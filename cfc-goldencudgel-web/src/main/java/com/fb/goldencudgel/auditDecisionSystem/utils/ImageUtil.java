package com.fb.goldencudgel.auditDecisionSystem.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

/*import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;*/

public class ImageUtil {

    public static final int DEFAULT_DPI = 300;//分辨率

    private static final int DEFAULT_WIDTH = 1024;//默认宽度(像素)
    private static final int END_IMG_HEIGHT = 200;//倒二图片面图片的高度(像素)
    private static final int CUT_HEIGHT = 280;//上下边距(微米)
    private static final int CUT_WIDTH = 620;//左右边距(微米)
    private static final int CONV_RATE = 254;//英寸与微米的转换倍率(1英寸=254微米)
    private static final int LINE_WORDS_COUNT = 19;// 每行字数
    private static final int PAGE_LINE_COUNT = 21;//每页容纳正文行数(标题等固定行数除外)
    private static final int MAX_PAGE_LINT_COUNT = 34;//最后图片容纳最大行数
    private static final int LINE_HEIGHT = 70;//每行高度(微米)
    private static final int END_WORDS_DEFAULT_COUNT = 63;//最后图片固定字数
    private static final int END_APPEND_LINE_COUNT = 2;//最后图片追加行数
    private static final int APPEND_LINE_COUNT = 4;//中间图片追加行数

    /**
     * 计算剪切左右边距的像素
     * @return
     */
    private static int getCutWidth(){
       return (int) (CUT_WIDTH * DEFAULT_DPI / CONV_RATE);
    }

    /**
     * 计算剪切上下边距的像素
     * @return
     */
    private static int getCutHeight(){
        return (int) (CUT_HEIGHT * DEFAULT_DPI / CONV_RATE);
    }

    /**
     * 计算每行高度的像素
     * @return
     */
    private static  int getLineHeight(){
        return (int) (LINE_HEIGHT * DEFAULT_DPI / CONV_RATE);
    }

  /**
   * @param src 原始图像
   * @param resizeTimes 倍数,比如0.5就是缩小一半,0.98等等double类型
   * @return 返回处理后的图像
   */
  public static BufferedImage zoomImage(String src, float resizeTimes) {
    BufferedImage result = null;
    try {
      File srcfile = new File(src);
      if (!srcfile.exists()) {
        System.out.println("文件不存在");
      }
      BufferedImage im = ImageIO.read(srcfile);

      /* 原始图像的宽度和高度 */
      int width = im.getWidth();
      int height = im.getHeight();

      // 压缩计算
      // float resizeTimes = 0.5f; /* 这个参数是要转化成的倍数,如果是1就是转化成1倍 */

      /* 调整后的图片的宽度和高度 */
      int toWidth = (int) (width * resizeTimes);
      int toHeight = (int) (height * resizeTimes);

      /* 新生成结果图片 */
      result = new BufferedImage(toWidth, toHeight, BufferedImage.TYPE_INT_RGB);

      result.getGraphics().drawImage(
              im.getScaledInstance(toWidth, toHeight, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
    } catch (Exception e) {
      System.out.println("创建缩略图发生异常" + e.getMessage());
    }
    return result;
  }

  /**
   * @param im 原始图像
   * @param resizeTimes 倍数,比如0.5就是缩小一半,0.98等等double类型
   * @return 返回处理后的图像
   */
  public static BufferedImage zoomImage(BufferedImage im, float resizeTimes) {
    BufferedImage result = null;
    try {

      /* 原始图像的宽度和高度 */
      int width = im.getWidth();
      int height = im.getHeight();

      // 压缩计算
      // float resizeTimes = 0.5f; /* 这个参数是要转化成的倍数,如果是1就是转化成1倍 */

      /* 调整后的图片的宽度和高度 */
      int toWidth = (int) (width * resizeTimes);
      int toHeight = (int) (height * resizeTimes);

      /* 新生成结果图片 */
      result = new BufferedImage(toWidth, toHeight, BufferedImage.TYPE_INT_RGB);

      result.getGraphics().drawImage(
              im.getScaledInstance(toWidth, toHeight, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
    } catch (Exception e) {
      System.out.println("创建缩略图发生异常" + e.getMessage());
    }
    return result;
  }

  /**
   * 中间图片剪切
   */
  private static BufferedImage cutImage(BufferedImage image, Map<String, String> params) {
    try {
        int newW = image.getWidth() - getCutWidth() * 2;
        int lineCount = APPEND_LINE_COUNT;
        // 根据字数算行数
        if(params != null){
            List<Integer> sList = new ArrayList<Integer>();
            sList.add(15 + params.get("{1}").length());
            sList.add(params.get("{3}").length());
            sList.add(params.get("{5}").length());
            sList.add(params.get("{7}").length());
            sList.add(params.get("{12}").length());
            for (int sLength:sList) {
                lineCount += sLength / LINE_WORDS_COUNT + (sLength % LINE_WORDS_COUNT == 0 ? 0 : 1);
            }
        }
//        System.out.println("lineCount:"+lineCount);
        int newH = image.getHeight() - getCutHeight() * 2 - (PAGE_LINE_COUNT - lineCount) * getLineHeight();
        BufferedImage newImage = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, -getCutWidth(), -getCutHeight(), image.getWidth(), image.getHeight(),
                null);
        g.dispose();
        return newImage;
    } catch (Exception e) {
        e.printStackTrace();
    }
      return null;
  }

    /**
     * 最后图片剪切
     */
    private static BufferedImage cutLastImage(BufferedImage image, Map<String, String> params) {
        try {
            int newW = image.getWidth() - getCutWidth() * 2;
            int lineCount = 7;
            // 根据字数算行数
            if(params != null){
                String s8 = params.get("{8}");
                String s9 = params.get("{9}");
                String s10 = params.get("{10}");
                int wordsCount = END_WORDS_DEFAULT_COUNT + s8.length() + s9.length() + s10.length();
//                System.out.println("wordsCount:"+wordsCount);
                lineCount = wordsCount / LINE_WORDS_COUNT + (wordsCount % LINE_WORDS_COUNT == 0 ? 0 : 1);
                lineCount += END_APPEND_LINE_COUNT;
            }
//            System.out.println("lineCount:"+lineCount);
            int newH = image.getHeight() - getCutHeight() * 2 - (MAX_PAGE_LINT_COUNT - lineCount) * getLineHeight();
            BufferedImage newImage = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = newImage.createGraphics();
            g.drawImage(image, -getCutWidth(), -getCutHeight(), image.getWidth(), image.getHeight(),
                    null);
            g.dispose();
            return newImage;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 倒二图片剪切
     */
    private static BufferedImage cutEndImage(BufferedImage image) {
        try {
            // 去除图片头尾空白
            int newW = image.getWidth();
            int newH = END_IMG_HEIGHT;
            BufferedImage newImage = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = newImage.createGraphics();
            g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(),
                    null);
            g.dispose();
            return newImage;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

  /**
   * 导入本地图片到缓冲区
   */
  private static BufferedImage loadImageLocal(String imgName, boolean cutFlag) {
    try {
      if (cutFlag) {
        // 去除图片头尾空白
        BufferedImage image = ImageIO.read(new File(imgName));
        return cutImage(image,null);
      } else {
        return ImageIO.read(new File(imgName));
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  /**
   * 合并多张图片
   */
  private static BufferedImage modifyImagetogeter(List<BufferedImage> imgBuffList, int defaultWidth) {
    int width = defaultWidth;
    int heightTotal = 0;
    List<BufferedImage> imgList = new ArrayList<BufferedImage>();
    for (BufferedImage img : imgBuffList) {
      int w = img.getWidth();
      if (width != img.getWidth()) {
        // 根据第一张图片的宽度等比缩放
        DecimalFormat df = new DecimalFormat("0.00");// 设置保留位数
        String rateStr = df.format((float) width / w);
        int newHight = (int) (img.getHeight() * Float.parseFloat(rateStr));
        BufferedImage newImg = new BufferedImage(width, newHight, BufferedImage.TYPE_INT_RGB);
        long startTime = System.currentTimeMillis();
        newImg.getGraphics().drawImage(
                img.getScaledInstance(width, newHight, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
        img = newImg;
        long endTime = System.currentTimeMillis();
        System.out.println("===time:" + (endTime - startTime));
      }
      heightTotal += img.getHeight();
      imgList.add(img);
    }
    // 定义图片的画布大小
    BufferedImage newImage = new BufferedImage(width, heightTotal, BufferedImage.TYPE_INT_RGB);
    try {
      Graphics2D g = newImage.createGraphics();
      int startHeight = 0;
      // 逐一将图片写入到画布
      for (BufferedImage img : imgList) {
        long startTime = System.currentTimeMillis();
        int w = img.getWidth();
        int h = img.getHeight();
        g.drawImage(img, 0, startHeight, w, h, null);
        startHeight += img.getHeight();
        long endTime = System.currentTimeMillis();
        System.out.println("*****time:" + (endTime - startTime));
      }
      g.dispose();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return newImage;
  }

  /**
   * 合并多张图片
   */
  private static BufferedImage modifyImagetogeter(List<String> imgPathList, boolean cutFlag) {
    int width = 0;
    int heightTotal = 0;
    List<BufferedImage> imgList = new ArrayList<BufferedImage>();
    for (String imgPath : imgPathList) {
      BufferedImage img = loadImageLocal(imgPath, cutFlag);
      int w = img.getWidth();
      width = width == 0 ? w : width;
      if (width != img.getWidth()) {
        // 根据第一张图片的宽度等比缩放
        DecimalFormat df = new DecimalFormat("0.00");// 设置保留位数
        String rateStr = df.format((float) width / w);
        int newHight = (int) (img.getHeight() * Float.parseFloat(rateStr));
        BufferedImage newImg = new BufferedImage(width, newHight, BufferedImage.TYPE_INT_RGB);
        newImg.getGraphics().drawImage(
                img.getScaledInstance(width, newHight, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
        img = newImg;
      }
      heightTotal += img.getHeight();
      imgList.add(img);
    }
    // 定义图片的画布大小
    BufferedImage newImage = new BufferedImage(width, heightTotal, BufferedImage.TYPE_INT_RGB);
    try {
      Graphics2D g = newImage.createGraphics();
      int startHeight = 0;
      // 逐一将图片写入到画布
      for (BufferedImage img : imgList) {
        int w = img.getWidth();
        int h = img.getHeight();
        g.drawImage(img, 0, startHeight, w, h, null);
        startHeight += img.getHeight();
      }
      g.dispose();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return newImage;
  }

  /**
   * 生成新图片到本地
   *
   * @param newImage 图片生成路径
   * @param imgPathList 合成的所有图片路径
   * @param cutFlag 是否剪切
   * @param compressFlag 是否压缩
   */
  public static void writeImageLocal(String newImage, List<String> imgPathList, boolean cutFlag,
                                     boolean compressFlag) {
    BufferedImage img = modifyImagetogeter(imgPathList, cutFlag);
    // img = compressFlag ? zoomImage(img, 0.5f) : img;
    if (newImage != null && img != null) {
      try {
        File outputfile = new File(newImage);
        String[] newImgArr = newImage.split("\\.");
        ImageIO.write(img, newImgArr[1], outputfile);
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * 生成企业经营报告书
   */
  public static void createOperReportImg(String newImagePath, String firstImgPath,
                                         String endImgPath, List<BufferedImage> middImgList, Map<String, String> params) {
    long startTime = System.currentTimeMillis();
    List<BufferedImage> imgList = new ArrayList<BufferedImage>();
    BufferedImage startImgBuff = loadImageLocal(firstImgPath, false);
    imgList.add(startImgBuff);

    for(int i = 0;i < middImgList.size();i++){
        BufferedImage middImage = middImgList.get(i);
        if(i==middImgList.size()-1){
            BufferedImage endImgBuff = loadImageLocal(endImgPath, false);
            imgList.add(cutEndImage(endImgBuff));
            imgList.add(cutLastImage(middImage,params));
        }else{
            imgList.add(cutImage(middImage,params));
        }
    }
//    for (BufferedImage middImage : middImgList) {
//      imgList.add(cutImage(middImage));
//    }
//    if(StringUtils.isNotBlank(endImgPath)){
//        BufferedImage endImgBuff = loadImageLocal(endImgPath, false);
//        imgList.add(endImgBuff);
//    }
    BufferedImage newImgBuff = modifyImagetogeter(imgList, DEFAULT_WIDTH);
    try {
      File outputfile = new File(newImagePath);
      ImageIO.write(newImgBuff, "jpeg", outputfile);
    } catch (Exception e) {
      e.printStackTrace();
    }
    long endTime = System.currentTimeMillis();
    System.out.println("createOperReportImg time:" + (endTime - startTime));
  }

}
