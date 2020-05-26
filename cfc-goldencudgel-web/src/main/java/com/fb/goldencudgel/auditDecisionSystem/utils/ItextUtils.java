package com.fb.goldencudgel.auditDecisionSystem.utils;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.rtf.RtfWriter2;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItextUtils {
    /**
     * @param list
     */
    public static void itexts(List list,String wordPath,String title) {
        // 创建word文档,并设置纸张的大小
        Document document = new Document(PageSize.A4);

        //获取微软雅黑外部字体
       /* String path= System.getProperty("user.dir");
       String fontPath =path+ "\\src\\main\\resources\\static\\font\\msyh.ttf";*/
        String fontPath = "/usr/share/fonts/win/MSJH.TTF";
        try {
            RtfWriter2.getInstance(document,
                    new FileOutputStream(wordPath));

            document.open();

            //设置合同头

            com.lowagie.text.Paragraph ph = new com.lowagie.text.Paragraph();
            Font f  = new Font();
            try {
                BaseFont baseFont1 = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                Font yahei12 = new Font(baseFont1, 18f);

                Paragraph p = new com.lowagie.text.Paragraph(title,
                        new Font(yahei12));
                p.setAlignment(1);
                document.add(p);
                ph.setFont(f);

                // 设置中文字体

                BaseFont bfFont =
          /*     BaseFont.createFont("STSongStd-Light",
               "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);*/
                        BaseFont.createFont(fontPath,
                                BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Font chinaFont = new Font();
            /*
             * 创建有三列的表格
             */
            Table table = new Table(3);

            table.setBorderWidth(1);
            table.setBorderColor(Color.BLACK);
            table.setPadding(0);
            table.setSpacing(0);

            /*
             * 添加表头的元素
             */
            int end1=0;
            int end2=0;
            for (int i = 0; i <list.size() ; i++) {
                Object [] objs= (Object[]) list.get(i);
                int size=Integer.parseInt(objs[3].toString());//第三行行数
                if (objs[2]==null){
                    table.addCell(cells((objs[0]).toString(),1,1));
                    table.addCell(cells((objs[1]).toString(),1,2));
                    continue;
                }
                if (size==1){
                    table.addCell(cells((objs[0]).toString(),1,1));
                    table.addCell(cells((objs[1]).toString(),1,1));
                    table.addCell(cells((objs[2]).toString(),1,1));
                    continue;
                }
                int index=Integer.parseInt(objs[4].toString());//第二行行数

                if (i>=end1){
                    end1=i+size;//第三行总循环数
                    table.addCell(cells((objs[0]).toString(),size,1));
                }
                if (i>=end2){
                    end2=i+index;//第二行总循环数
                    table.addCell(cells((objs[1]).toString(),index,1));
                }

                table.addCell(cells((objs[2]).toString(),1,1));

            }
    /*        document.add(new com.lowagie.text.Paragraph("生成表格"));
            document.add(new com.lowagie.text.Paragraph("用java生成words文件")); */
            document.add(table);
            document.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Cell cells(String msg,int row,int col){

        Cell cell= new Cell(msg);//单元格
        cell.setRowspan(row);//设置表格为三行
        cell.setColspan(col);//设置表格为三列
        cell.setUseAscender(true);
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
        return  cell;
    }

}
