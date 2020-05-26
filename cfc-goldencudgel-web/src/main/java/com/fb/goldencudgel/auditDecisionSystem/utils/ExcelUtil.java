package com.fb.goldencudgel.auditDecisionSystem.utils;

/**
 * Created By: Comwave Project Team Created Date: Aug 16, 2011 8:58:12 PM
 */


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;


/**
 * The Class ExcelUtil.
 * 
 * @author Geln Yang
 * @version 1.0
 */
public class ExcelUtil {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

  /**
   * Read excel.
   * 
   * @param file the file
   * @return the XSSF workbook
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static XSSFWorkbook readExcel(File file) throws IOException {
    InputStream inp = new FileInputStream(file);
    // If clearly doesn't do mark/reset, wrap up
    if (!inp.markSupported()) {
      inp = new PushbackInputStream(inp, 8);
    }
    try {
      return new XSSFWorkbook(OPCPackage.open(inp));
    } catch (InvalidFormatException e) {
      return new XSSFWorkbook(new FileInputStream(file));
    }
  }

  /**
   * Read excel2003.
   * 
   * @param file the file
   * @return the HSSF workbook
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static HSSFWorkbook readExcel2003(File file) throws IOException {
    return new HSSFWorkbook(new FileInputStream(file));
  }

  /**
   * 讀取excel檔案內容.
   * 
   * @param workbook the workbook
   * @param sheetIndex the sheet index
   * @param fromRowIndex the from row index
   * @param toRowIndex the to row index
   * @return the list
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static List<List<Object>> readExcelLines(XSSFWorkbook workbook, int sheetIndex,
      int fromRowIndex, int toRowIndex) throws IOException {
    if (fromRowIndex > toRowIndex && toRowIndex >= 0) {
      throw new IOException("The fromRowIndex is great than toRowIndex!");
    }
    XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
    FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
    if (null != sheet) {
      int maxRowNum = sheet.getLastRowNum();
      if (toRowIndex < 0) {
        toRowIndex = maxRowNum;
      }
      if (toRowIndex < 0 || maxRowNum < toRowIndex) {
        toRowIndex = maxRowNum;
        // throw new IOException("The toRowIndex is great than the max row num " + maxRowNum);
      }
      List<List<Object>> lines = new ArrayList<List<Object>>();
      for (int rowIndex = fromRowIndex; rowIndex <= toRowIndex; rowIndex++) {
        if (null != sheet.getRow(rowIndex)) {
          XSSFRow row = sheet.getRow(rowIndex);
          int cellCount = row.getLastCellNum();
          List<Object> line = new ArrayList<Object>();
          lines.add(line);
          for (int cellIndex = 0; cellIndex < cellCount; cellIndex++) {
            XSSFCell cell = row.getCell(cellIndex);
            Object cellVal = "";
            if (null != cell) {
              cellVal = getCellValue(cell, evaluator);
            }
            line.add(cellVal);
          }
        }
      } // END for

      return lines;
    } else {
      logger.warn("no sheet index is " + sheetIndex);
    }
    logger.warn("no sheet found!");
    return null;
  }

  /**
   * 讀取excel檔案內容.
   * 
   * @param workbook the workbook
   * @param sheetIndex the sheet index
   * @param fromRowIndex the from row index
   * @param toRowIndex the to row index
   * @return the list
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static List<List<Object>> readExcelLines2003(HSSFWorkbook workbook, int sheetIndex,
      int fromRowIndex, int toRowIndex) throws IOException {
    if (fromRowIndex > toRowIndex && toRowIndex >= 0) {
      throw new IOException("The fromRowIndex is great than toRowIndex!");
    }
    HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
    FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
    if (null != sheet) {
      int maxRowNum = sheet.getLastRowNum();
      if (toRowIndex < 0) {
        toRowIndex = maxRowNum;
      }
      if (maxRowNum < toRowIndex) {
        toRowIndex = maxRowNum;
        // throw new IOException("The toRowIndex is great than the max row num " + maxRowNum);
      }
      List<List<Object>> lines = new ArrayList<List<Object>>();
      for (int rowIndex = fromRowIndex; rowIndex <= toRowIndex; rowIndex++) {
        if (null != sheet.getRow(rowIndex)) {
          HSSFRow row = sheet.getRow(rowIndex);
          int cellCount = row.getLastCellNum();
          List<Object> line = new ArrayList<Object>();
          lines.add(line);
          for (int cellIndex = 0; cellIndex < cellCount; cellIndex++) {
            HSSFCell cell = row.getCell(cellIndex);
            Object cellVal = "";
            if (null != cell) {
              cellVal = getCellValue2003(cell, evaluator);
            }
            line.add(cellVal);
          }
        }
      } // END for

      return lines;
    } else {
      logger.warn("no sheet index is " + sheetIndex);
    }
    logger.warn("no sheet found!");
    return null;
  }

  /**
   * Gets the cell value.
   * 
   * @param cell the cell
   * @param evaluator the evaluator
   * @return the cell value
   */
  private static Object getCellValue(XSSFCell cell, FormulaEvaluator evaluator) {
    DataFormatter dataFormatter = new DataFormatter();
    int cellType = cell.getCellType();
    Object cellVal = null;
    switch (cellType) {
      case Cell.CELL_TYPE_NUMERIC:
        if (DateUtil.isCellDateFormatted(cell)) {
          cellVal = cell.getDateCellValue();
        } else {
          cellVal = cell.getNumericCellValue();
        }
        break;
      case Cell.CELL_TYPE_STRING:
        cellVal = cell.getStringCellValue();
        break;
      case Cell.CELL_TYPE_BOOLEAN:
        cellVal = cell.getBooleanCellValue();
        break;
      case Cell.CELL_TYPE_FORMULA:
        try {
          cellVal = dataFormatter.formatCellValue(cell, evaluator);
        } catch (Exception e) {
          if (e.getMessage().indexOf("not implemented yet") != -1) {
            logger.warn(e.getMessage());
          } else {
            throw new RuntimeException(e);
          }
        }

        break;
      default:
        cellVal = cell.getStringCellValue();
    }
    return cellVal;
  }

  /**
   * Gets the cell value2003.
   * 
   * @param cell the cell
   * @param evaluator the evaluator
   * @return the cell value2003
   */
  private static Object getCellValue2003(HSSFCell cell, FormulaEvaluator evaluator) {
    DataFormatter dataFormatter = new DataFormatter();
    int cellType = cell.getCellType();
    Object cellVal = null;
    switch (cellType) {
      case Cell.CELL_TYPE_NUMERIC:
        if (DateUtil.isCellDateFormatted(cell)) {
          cellVal = cell.getDateCellValue();
        } else {
          cellVal = cell.getNumericCellValue();
        }
        break;
      case Cell.CELL_TYPE_STRING:
        cellVal = cell.getStringCellValue();
        break;
      case Cell.CELL_TYPE_BOOLEAN:
        cellVal = cell.getBooleanCellValue();
        break;
      case Cell.CELL_TYPE_FORMULA:
        try {
          cellVal = dataFormatter.formatCellValue(cell, evaluator);
        } catch (Exception e) {
          if (e.getMessage().indexOf("Could not resolve external workbook name") != -1
              || e.getMessage().indexOf("Unexpected celltype (5)") != -1) {
            logger.warn(e.getMessage());
          } else {
            throw new RuntimeException(e);
          }
        }
        break;
      default:
        cellVal = cell.getStringCellValue();
    }
    return cellVal;
  }

  /**
   * Read excel lines.
   * 
   * @param file the file
   * @param sheetIndex the sheet index
   * @param fromRowIndex the from row index
   * @param toRowIndex the to row index
   * @return the list
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static List<List<Object>> readExcelLines(File file, int sheetIndex, int fromRowIndex,
      int toRowIndex) throws IOException {
    Workbook excel;
    try {
      excel = readExcel2003(file);
    } catch (Exception e) {
      logger.debug(e.getMessage());
      excel = readExcel(file);
    }
    if (excel instanceof XSSFWorkbook) {
      return readExcelLines((XSSFWorkbook) excel, sheetIndex, fromRowIndex, toRowIndex);
    } else if (excel instanceof HSSFWorkbook) {
      return readExcelLines2003((HSSFWorkbook) excel, sheetIndex, fromRowIndex, toRowIndex);
    }
    return null;
  }

  /**
   * Read excel lines.
   * 
   * @param workbook the workbook
   * @param sheetIndex the sheet index
   * @param fromRowIndex the from row index
   * @return the list
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static List<List<Object>> readExcelLines(File workbook, int sheetIndex, int fromRowIndex)
      throws IOException {
    return readExcelLines(workbook, sheetIndex, fromRowIndex, -1);
  }

  /**
   * Creates the.
   * 
   * @param inp the inp
   * @return the workbook
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static Workbook create(InputStream inp) throws IOException {
    try {
      // If clearly doesn't do mark/reset, wrap up
      if (!inp.markSupported()) {
        inp = new PushbackInputStream(inp, 8);
      }

      if (POIFSFileSystem.hasPOIFSHeader(inp)) {
        return new HSSFWorkbook(inp);
      }
      if (POIXMLDocument.hasOOXMLHeader(inp)) {
        return new XSSFWorkbook(OPCPackage.open(inp));
      }
      throw new IllegalArgumentException(
          "Your InputStream was neither an OLE2 stream, nor an OOXML stream");
    } catch (Exception e) {
      throw new IOException(e.toString());
    }
  }

  public static List<List<Object>> readFromFile(String path) {
    path = path.replaceAll("\\\\", "/");
    File file = new File(path);

    XSSFWorkbook xss = null;
    HSSFWorkbook hss = null;
    try {
      String fileName = path.substring(path.lastIndexOf("/") + 1);
      String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
      List<List<Object>> dataList = new ArrayList<List<Object>>();
      InputStream input = new FileInputStream(file);
      if ("xlsx".equalsIgnoreCase(suffix)) { // Excel2007
        if (!input.markSupported()) {
          input = new PushbackInputStream(input, 8);
        }
        try {
          xss = new XSSFWorkbook(OPCPackage.open(input));
        } catch (InvalidFormatException e) {
          xss = new XSSFWorkbook(input);
        }
        dataList = readExcelLines(xss, 0, 0, -1);
      } else if ("xls".equals(suffix)) { // Excel2003
        hss = new HSSFWorkbook(input);
        dataList = readExcelLines2003(hss, 0, 0, -1);
      }
      return dataList;
    } catch (Exception e) {
      // e.printStackTrace();
      logger.error(e.getMessage(), e);
    }
    return null;
  }

  public static List<List<Object>> readFromFile(MultipartFile file) {
    XSSFWorkbook xss = null;
    HSSFWorkbook hss = null;
    try {
      String fileName = file.getOriginalFilename();
      String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
      List<List<Object>> dataList = new ArrayList<List<Object>>();
      InputStream input = file.getInputStream();
      if ("xlsx".equalsIgnoreCase(suffix)) { // Excel2007
        if (!input.markSupported()) {
          input = new PushbackInputStream(input, 8);
        }
        try {
          xss = new XSSFWorkbook(OPCPackage.open(input));
        } catch (InvalidFormatException e) {
          xss = new XSSFWorkbook(file.getInputStream());
        }
        dataList = readExcelLines(xss, 0, 0, -1);
      } else if ("xls".equals(suffix)) { // Excel2003
        hss = new HSSFWorkbook(input);
        dataList = readExcelLines2003(hss, 0, 0, -1);
      }
      return dataList;
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return null;
  }

  /**
   * 导出EXCEL报表
   * 
   * @param cellValues2
   */
  public static void fillExcel(HttpServletResponse response, String fileName, String titleName,
      String[] cellValues, List<Object[]> dataList, String[] dataIndexs, Integer[] cellWidths,
      String[] cellValues2) {
    HSSFWorkbook workbook = new HSSFWorkbook();
    try {
      HSSFSheet worksheet = workbook.createSheet(titleName);
      worksheet.setDefaultColumnWidth(20);
      Font font = workbook.createFont();
      font.setFontHeightInPoints((short) 12);
      font.setFontName("宋体");
      font.setBoldweight(Font.BOLDWEIGHT_BOLD);
      HSSFCellStyle cellStyle = workbook.createCellStyle();
      cellStyle.setFont(font);
      cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
      cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
      int rowIndex = 0;
      // 创建名称
      HSSFRow row = worksheet.createRow(rowIndex);
      rowIndex++;
      row.setHeightInPoints((float) 14.25);

      // 标题
      HSSFCell cell0 = row.createCell(0);
      cell0.setCellValue(titleName);
      cell0.setCellStyle(cellStyle);
      worksheet.addMergedRegion(new CellRangeAddress(0, 0, 0, cellValues.length - 1));

      row = worksheet.createRow(rowIndex);
      rowIndex++;

      // 创建header
      cellStyle.setWrapText(true);
      // 栏位名称
      for (int i = 0; i < cellValues.length; i++) {
        HSSFCell cell = row.createCell(i);
        cell.setCellValue(cellValues[i]);
        cell.setCellStyle(cellStyle);
        if (cellWidths != null) {
          worksheet.setColumnWidth(i, cellWidths[i] * 256);
        }
      }

      if (cellValues2 != null) {
        row = worksheet.createRow(rowIndex);
        rowIndex++;
        for (int i = 0; i < cellValues2.length; i++) {
          HSSFCell cell = row.createCell(i);
          cell.setCellValue(cellValues2[i]);
          cell.setCellStyle(cellStyle);
        }
        worksheet.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));
        worksheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 1));
        worksheet.addMergedRegion(new CellRangeAddress(1, 2, 2, 2));
        worksheet.addMergedRegion(new CellRangeAddress(1, 2, 3, 3));
        worksheet.addMergedRegion(new CellRangeAddress(1, 2, 4, 4));
        worksheet.addMergedRegion(new CellRangeAddress(1, 2, 11, 11));
        worksheet.addMergedRegion(new CellRangeAddress(1, 2, 12, 12));

        worksheet.addMergedRegion(new CellRangeAddress(1, 1, 5, 6));
        worksheet.addMergedRegion(new CellRangeAddress(1, 1, 7, 8));
        worksheet.addMergedRegion(new CellRangeAddress(1, 1, 9, 10));
      }
      // 设置数据格式
      Font fontBody = workbook.createFont();
      fontBody.setFontHeightInPoints((short) 11);
      fontBody.setFontName("宋体");
      HSSFCellStyle cellStyleBody = workbook.createCellStyle();
      cellStyleBody.setFont(fontBody);
      cellStyleBody.setWrapText(true);
      cellStyleBody.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
      // 填充数据
      for (int i = 0; i < dataList.size(); i++) {
        row = worksheet.createRow(rowIndex);
        rowIndex++;
        Object[] datas = dataList.get(i);
        int endIndex = cellValues.length < datas.length ? cellValues.length : datas.length;
        for (int j = 0; j < endIndex; j++) {
          HSSFCell celli0 = row.createCell(j);
          int dataIndex = j;
          if (dataIndexs != null) {
            dataIndex = Integer.valueOf(dataIndexs[j]);
          }
          if (datas[dataIndex] != null) {
            celli0.setCellValue(datas[dataIndex].toString());
          }
          celli0.setCellStyle(cellStyleBody);
        }
      }

      response.reset();
      response.setContentType("application/octet-stream");
      // 设置response的Header
      if (StringUtils.isBlank(fileName)) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHMMSS");
        fileName = df.format(new Date());
      }
      response.addHeader("Content-Disposition",
          "attachment;filename=" + URLEncoder.encode(fileName + ".xls", "UTF-8"));
      response.addHeader("Content-Type", "application/octet-stream;charset=UTF-8");
      OutputStream ouputStream = response.getOutputStream();
      workbook.write(ouputStream);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    } finally {
      try {
        workbook.close();
      } catch (IOException e) {
        logger.error(e.getMessage(), e);
      }
    }
  }

  @SuppressWarnings("static-access")
  public static List<List<Object>> readExcel(MultipartFile file, String[] titles) throws Exception {
    List<List<Object>> dataList = new ArrayList<List<Object>>();
    XSSFWorkbook xss = null;
    HSSFWorkbook hss = null;
    String fileName = file.getOriginalFilename();
    String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
    ExcelUtil excelUtil = new ExcelUtil();
    InputStream input = file.getInputStream();
    if ("xlsx".equalsIgnoreCase(suffix)) { // Excel2007
      if (!input.markSupported()) {
        input = new PushbackInputStream(input, 8);
      }
      try {
        xss = new XSSFWorkbook(OPCPackage.open(input));
      } catch (InvalidFormatException e) {
        xss = new XSSFWorkbook(file.getInputStream());
      }
      dataList = excelUtil.readExcelLines(xss, 0, 0, -1);
    } else if ("xls".equals(suffix)) { // Excel2003
      hss = new HSSFWorkbook(input);
      dataList = excelUtil.readExcelLines2003(hss, 0, 0, -1);
    }
    if (titles != null && titles.length > 0) {
      List<Object> obj = dataList.get(0);
      if (CollectionUtils.isEmpty(obj) || obj.size() != titles.length) {
        throw new Exception("模版错误！");
      }
      for (int i = 0; i < titles.length; i++) {
        if (obj.get(i).toString().trim().indexOf(titles[i]) == -1) {
          throw new Exception("模版列对应不上！");
        }
      }
    }
    dataList.remove(0);// 去除标题
    return dataList;
  }
  public static void fillSheetsToExcel(HttpServletResponse response, HSSFWorkbook workbook,
      String fileName, String sheetName, String[] cellValues, List<Object[]> dataList,
      String[] dataIndexs, Integer[] cellWidths, boolean endFlag) {
    try {
      HSSFSheet worksheet = workbook.createSheet(sheetName);
      worksheet.setDefaultColumnWidth(20);
      Font font = workbook.createFont();
      font.setFontHeightInPoints((short) 12);
      font.setFontName("宋体");
      font.setBoldweight(Font.BOLDWEIGHT_BOLD);
      HSSFCellStyle cellStyle = workbook.createCellStyle();
      cellStyle.setFont(font);
      cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
      cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
      int rowIndex = 0;
      // 创建名称
      HSSFRow row = worksheet.createRow(rowIndex);
      rowIndex++;
      row.setHeightInPoints((float) 14.25);

      // 标题
      HSSFCell cell0 = row.createCell(0);
      cell0.setCellValue(sheetName);
      cell0.setCellStyle(cellStyle);
      worksheet.addMergedRegion(new CellRangeAddress(0, 0, 0, cellValues.length - 1));

      row = worksheet.createRow(rowIndex);
      rowIndex++;

      // 创建header
      cellStyle.setWrapText(true);
      // 栏位名称
      for (int i = 0; i < cellValues.length; i++) {
        HSSFCell cell = row.createCell(i);
        cell.setCellValue(cellValues[i]);
        if (cellWidths != null) {
          worksheet.setColumnWidth(i, cellWidths[i] * 256);
        }
      }

      // 设置数据格式
      Font fontBody = workbook.createFont();
      fontBody.setFontHeightInPoints((short) 11);
      fontBody.setFontName("宋体");
      HSSFCellStyle cellStyleBody = workbook.createCellStyle();
      cellStyleBody.setFont(fontBody);
      cellStyleBody.setWrapText(true);
      cellStyleBody.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
      // 填充数据
      for (int i = 0; i < dataList.size(); i++) {
        row = worksheet.createRow(rowIndex);
        rowIndex++;
        Object[] datas = dataList.get(i);
        int endIndex = cellValues.length < datas.length ? cellValues.length : datas.length;
        for (int j = 0; j < endIndex; j++) {
          HSSFCell celli0 = row.createCell(j);
          int dataIndex = j;
          if (dataIndexs != null) {
            dataIndex = Integer.valueOf(dataIndexs[j]);
          }
          if (datas[dataIndex] != null) {
            celli0.setCellValue(datas[dataIndex].toString());
          }
          celli0.setCellStyle(cellStyleBody);
        }
      }
      if (endFlag) {
        response.reset();
        response.setContentType("application/octet-stream");
        // 设置response的Header
        if (StringUtils.isBlank(fileName)) {
          SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHMMSS");
          fileName = df.format(new Date());
        }
        response.addHeader("Content-Disposition",
            "attachment;filename=" + URLEncoder.encode(fileName + ".xls", "UTF-8"));
        response.addHeader("Content-Type", "application/octet-stream;charset=UTF-8");
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        workbook.close();
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }
  
    /**
     * @description: 
     * @author: mazongjian
     * @param file
     * @return  
     * @date 2019年5月23日
     */
    public static List<List<Object>> readFromExcelFile(MultipartFile file) {
        XSSFWorkbook xss = null;
        HSSFWorkbook hss = null;
        try {
            String fileName = file.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            List<List<Object>> dataList = new ArrayList<List<Object>>();
            InputStream input = file.getInputStream();
            // Excel-2007
            if ("xlsx".equalsIgnoreCase(suffix)) {
                if (!input.markSupported()) {
                    input = new PushbackInputStream(input, 8);
                }
                try {
                    xss = new XSSFWorkbook(OPCPackage.open(input));
                } catch (InvalidFormatException e) {
                    xss = new XSSFWorkbook(file.getInputStream());
                }
                dataList = readExcelRows(xss, 0, 0, -1);
            } else if ("xls".equals(suffix)) {
                // Excel-2003
                hss = new HSSFWorkbook(input);
                dataList = readExcelRows2003(hss, 0, 0, -1);
            }
            return dataList;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    
    public static List<List<Object>> readExcelRows(XSSFWorkbook workbook, int sheetIndex, int fromRowIndex, int toRowIndex) throws IOException {
        if (fromRowIndex > toRowIndex && toRowIndex >= 0) {
            throw new IOException("The fromRowIndex is great than toRowIndex!");
        }
        XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        if (null != sheet) {
            int maxRowNum = sheet.getLastRowNum();
            if (toRowIndex < 0) {
                toRowIndex = maxRowNum;
            }
            if (toRowIndex < 0 || maxRowNum < toRowIndex) {
                toRowIndex = maxRowNum;
            }
            List<List<Object>> lines = new ArrayList<List<Object>>();
            for (int rowIndex = fromRowIndex; rowIndex <= toRowIndex; rowIndex++) {
                if (null != sheet.getRow(rowIndex)) {
                    XSSFRow row = sheet.getRow(rowIndex);
                    int cellCount = row.getLastCellNum();
                    List<Object> line = new ArrayList<Object>();
                    boolean rowHasValue = false;
                    for (int cellIndex = 0; cellIndex < cellCount; cellIndex++) {
                        XSSFCell cell = row.getCell(cellIndex);
                        Object cellVal = "";
                        if (null != cell) {
                            cellVal = getCellValue(cell, evaluator);
                        }
                        if (!rowHasValue && cellVal != null && !"".equals(cellVal)) {
                            rowHasValue = true;
                        }
                        line.add(cellVal);
                    }
                    if (rowHasValue) {
                        lines.add(line);
                    }
                }
            }

            return lines;
        } else {
            logger.warn("no sheet index is " + sheetIndex);
        }
        logger.warn("no sheet found!");
        return null;
    }
    
    /**
     * 讀取excel檔案內容.
     * 
     * @param workbook the workbook
     * @param sheetIndex the sheet index
     * @param fromRowIndex the from row index
     * @param toRowIndex the to row index
     * @return the list
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static List<List<Object>> readExcelRows2003(HSSFWorkbook workbook, int sheetIndex, int fromRowIndex, int toRowIndex) throws IOException {
        if (fromRowIndex > toRowIndex && toRowIndex >= 0) {
            throw new IOException("The fromRowIndex is great than toRowIndex!");
        }
        HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        if (null != sheet) {
            int maxRowNum = sheet.getLastRowNum();
            if (toRowIndex < 0) {
                toRowIndex = maxRowNum;
            }
            if (maxRowNum < toRowIndex) {
                toRowIndex = maxRowNum;
            }
            List<List<Object>> lines = new ArrayList<List<Object>>();
            for (int rowIndex = fromRowIndex; rowIndex <= toRowIndex; rowIndex++) {
                if (null != sheet.getRow(rowIndex)) {
                    HSSFRow row = sheet.getRow(rowIndex);
                    int cellCount = row.getLastCellNum();
                    List<Object> line = new ArrayList<Object>();
                    boolean rowHasValue = false;
                    for (int cellIndex = 0; cellIndex < cellCount; cellIndex++) {
                        HSSFCell cell = row.getCell(cellIndex);
                        Object cellVal = "";
                        if (null != cell) {
                            cellVal = getCellValue2003(cell, evaluator);
                        }
                        if (!rowHasValue && cellVal != null && !"".equals(cellVal)) {
                            rowHasValue = true;
                        }
                        line.add(cellVal);
                    }
                    if (rowHasValue) {
                        lines.add(line);
                    }
                }
            }

            return lines;
        } else {
            logger.warn("no sheet index is " + sheetIndex);
        }
        logger.warn("no sheet found!");
        return null;
    }

  /**
   * 导出Excel
   * @param response
   * @param fileName 附件名称
   * @param sheetName sheet名称
   * @param headerNames 标题名称
   * @param dataList 数据
   */
    public static void exportExcel(HttpServletResponse response,String fileName, String sheetName, String[] headerNames, List<Object[]> dataList) {
        //声明一个工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        //生成一个表格，设置表格名称为sheetName
        HSSFSheet sheet = workbook.createSheet(sheetName);
        //设置表格列宽度为10个字节
        sheet.setDefaultColumnWidth(25);
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setFontName("宋体");
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        int rowIndex = 0;
        //创建第一行表头
        HSSFRow headrow = sheet.createRow(rowIndex);
        rowIndex++;
        //设置标题
        for (int i = 0; i < headerNames.length; i++) {
          //创建一个单元格
          HSSFCell cell = headrow.createCell(i);
          //创建一个内容对象
          HSSFRichTextString text = new HSSFRichTextString(headerNames[i]);
          //将内容对象的文字内容写入到单元格中
          cell.setCellValue(text);
          //设置样式
          cell.setCellStyle(cellStyle);
        }

        // 设置数据格式
        Font fontBody = workbook.createFont();
        fontBody.setFontHeightInPoints((short) 11);
        fontBody.setFontName("宋体");
        HSSFCellStyle cellStyleBody = workbook.createCellStyle();
        cellStyleBody.setFont(fontBody);
        cellStyleBody.setWrapText(true);
        cellStyleBody.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        //遍历数据
        for(Object[] obj : dataList) {
            HSSFRow row1 = sheet.createRow(rowIndex);
            rowIndex++;
            for (int i = 0; i < obj.length; i++) {
              HSSFCell cell = row1.createCell(i);
              HSSFRichTextString text = new HSSFRichTextString(ObjectUtil.obj2String(obj[i]));
              cell.setCellValue(text);
              //设置样式
              cell.setCellStyle(cellStyleBody);
            }
        }
        try {
            response.reset();
            response.setContentType("application/octet-stream");
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHMMSS");
            fileName = StringUtils.isBlank(fileName) ? df.format(new Date()) : (fileName+ "_"+df.format(new Date()));
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xls", "UTF-8"));
            response.addHeader("Content-Type", "application/octet-stream;charset=UTF-8");
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
          logger.warn(e.getMessage());
        } finally {
          try {
            workbook.close();
          } catch (IOException e) {
            logger.error(e.getMessage(), e);
          }
        }
    }
}
