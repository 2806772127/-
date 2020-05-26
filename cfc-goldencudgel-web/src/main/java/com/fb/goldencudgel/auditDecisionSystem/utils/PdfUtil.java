package com.fb.goldencudgel.auditDecisionSystem.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

/**
 * @ClassName PdfUtil
 * @Author panha
 * @Date 2019/6/13 16:45
 * @Version 1.0
 **/
public class PdfUtil {

    public static void createPdf(List<List> lists, String wordPath, List nameList, Map<String,String> map) {
        BaseFont bf;
        Font font = null;
        try {
        /*    bf = BaseFont.createFont( "STSong-Light", "UniGB-UCS2-H",
                    BaseFont.NOT_EMBEDDED);//创建字体
            font = new Font(bf,18f);//使用字体*/
            String fontPath = "/usr/share/fonts/win/MSJH.TTF";
            /*String path= System.getProperty("user.dir");
            String fontPath =path+ "\\src\\main\\resources\\static\\font\\msyh.ttf";*/

            BaseFont baseFont1 = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            font = new Font(baseFont1, 18f);
            font.setStyle(Font.BOLD);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Document document = new Document();
        try {
            int indentationValue = -15;
            PdfWriter.getInstance(document, new FileOutputStream(wordPath));
            document.open();
            String fontPath = "/usr/share/fonts/win/MSJH.TTF";
            /*String path= System.getProperty("user.dir");
            String fontPath =path+ "\\src\\main\\resources\\static\\font\\msyh.ttf";*/
            BaseFont baseFont1 = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font yahei12 = new Font(baseFont1, 12f);

            Paragraph p = new Paragraph("新興企業貸款實地訪查暨簡易徵信表", new Font(font));
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);
            //访查时间
            Paragraph pft = new Paragraph("訪查日期:"+map.get("APPOINTMENT_DATE"), new Font(yahei12));
            pft.setAlignment(Element.ALIGN_RIGHT);
            pft.setIndentationRight(indentationValue);
            pft.setSpacingBefore(10);
            document.add(pft);
            //换行
            Paragraph ps = new Paragraph("", new Font(font));
            document.add(ps);
            /*
             * 添加表头的元素
             */
            for(int j = 0; j <lists.size() ; j++){
                int end1=0;
                int end2=0;
                List list = lists.get(j);
                PdfPTable table = new PdfPTable(3);// 建立一个pdf表格
                table.setLockedWidth(true);
                table.setTotalWidth(560.0f);
                float[] widthArr = {170.0f,140.0f,250.0f};
                table.setWidths(widthArr);
                int x = 1;
                for (int i = 0; i <list.size() ; i++) {
                    Object [] objs= (Object[]) list.get(i);
                    int size=Integer.parseInt(objs[3].toString());//第三行行数

                    if (objs[2]==null || objs[2]=="" && objs[3].toString().equals("1")){
                        table.addCell(cells(x+"."+(objs[0]).toString(),1,1,yahei12));
                        x++;
                        table.addCell(cells((objs[1]).toString(),1,2,yahei12));
                        continue;
                    }
                    if (size==1){
                        table.addCell(cells(x+"."+(objs[0]).toString(),1,1,yahei12));
                        x++;
                        table.addCell(cells((objs[1]).toString(),1,1,yahei12));
                        table.addCell(cells((objs[2]).toString(),1,1,yahei12));
                        continue;
                    }
                    int index=Integer.parseInt(objs[4].toString());//第二行行数

                    if (i>=end1){
                        end1=i+size;//第三行总循环数
                        table.addCell(cells((x+"."+objs[0]).toString(),size,1,yahei12));
                        x++;
                    }
                    if (i>=end2){
                        end2=i+index;//第二行总循环数
                        if(objs[2]==null || objs[2]==""){
                            table.addCell(cells((objs[1]).toString(),1,2,yahei12));
                            continue;
                        }else{
                            table.addCell(cells((objs[1]).toString(),index,1,yahei12));
                        }

                    }

                    table.addCell(cells((objs[2]).toString(),1,1,yahei12));

                }
                String name = nameList.get(j).toString();

                if(1==j){
                    name= "二、營收及產銷貨狀況";
                }else{
                    name = numberToCH(j+1)+"、"+name;
                }
                //添加标题
                Paragraph p1 = new Paragraph(name, new Font(font));
                p1.setSpacingBefore(10);
                p1.setSpacingAfter(20);
                p1.setIndentationLeft(indentationValue);
                //p.setAlignment(1);
                //p.setIndentationLeft(1);
                document.add(p1);
                //添加空格
                document.add(ps);
                document.add(table);
            }
            Paragraph put = new Paragraph("徵信員:"+map.get("APPOINTMENT_USER_NAME")+"   日期:"+map.get("NOW_DATE"), new Font(yahei12));
            put.setIndentationRight(indentationValue);
            put.setAlignment(Element.ALIGN_RIGHT);
            put.setSpacingBefore(10);
            document.add(put);
            document.close();
        }catch (Exception e) {
            System.out.println("file create exception");
        }

    }

    public static PdfPCell cells(String msg, int row, int col,Font font){

        PdfPCell cell= new PdfPCell(new Paragraph(msg,font));//单元格
        cell.setMinimumHeight(15);
        cell.setPaddingLeft(10);
        //cell.setFixedHeight(30);
        cell.setPaddingTop(3);
        cell.setPaddingBottom(3);
        cell.setRowspan(row);//设置表格为三行
        cell.setColspan(col);//设置表格为三列
        cell.setUseAscender(true);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
        return  cell;
    }


    public static String numberToCH(int intInput) {
        String si = String.valueOf(intInput);
        String sd = "";
        if (si.length() == 1) // 個
        {
            sd += GetCH(intInput);
            return sd;
        } else if (si.length() == 2)// 十
        {
            if (si.substring(0, 1).equals("1"))
                sd += "十";
            else
                sd += (GetCH(intInput / 10) + "十");
            sd += numberToCH(intInput % 10);
        } else if (si.length() == 3)// 百
        {
            sd += (GetCH(intInput / 100) + "百");
            if (String.valueOf(intInput % 100).length() < 2)
                sd += "零";
            sd += numberToCH(intInput % 100);
        } else if (si.length() == 4)// 千
        {
            sd += (GetCH(intInput / 1000) + "千");
            if (String.valueOf(intInput % 1000).length() < 3)
                sd += "零";
            sd += numberToCH(intInput % 1000);
        } else if (si.length() == 5)// 萬
        {
            sd += (GetCH(intInput / 10000) + "萬");
            if (String.valueOf(intInput % 10000).length() < 4)
                sd += "零";
            sd += numberToCH(intInput % 10000);
        }

        return sd;
    }

    private static String GetCH(int input) {
        String sd = "";
        switch (input) {
            case 1:
                sd = "一";
                break;
            case 2:
                sd = "二";
                break;
            case 3:
                sd = "三";
                break;
            case 4:
                sd = "四";
                break;
            case 5:
                sd = "五";
                break;
            case 6:
                sd = "六";
                break;
            case 7:
                sd = "七";
                break;
            case 8:
                sd = "八";
                break;
            case 9:
                sd = "九";
                break;
            default:
                break;
        }
        return sd;
    }

}
