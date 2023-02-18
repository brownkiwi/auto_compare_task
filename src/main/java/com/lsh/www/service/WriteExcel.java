package com.lsh.www.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.handler.WorkbookWriteHandler;
import com.alibaba.excel.write.handler.context.WorkbookWriteHandlerContext;
import com.alibaba.fastjson.JSON;
import com.lsh.www.constant.AAAA;
import com.lsh.www.entity.ChayifenxiData;
import com.lsh.www.entity.DmodelData;
import com.lsh.www.entity.ZhongjianbiaoData;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.*;


public class WriteExcel {

    static String Path = "D:\\Ideaworkspace\\auto_compare_task\\";

    /**
     * 最简单的写
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link DmodelData}
     * <p>
     * 2. 直接写即可
     */

    public static void appendWrite() {

        String fileName = Path + "zhongjianbiao" + ".xlsx";

        File file = new File(fileName);
        File tempFile = new File(fileName + "temp.xlsx");
        if (file.exists()) {
            // 第二次按照原有格式，不需要表头，追加写入
            //withTemplate()指定模板文件
            //.file() 指定目标文件，不能与模板文件是同一个文件
            EasyExcel.write(file, DmodelData.class).needHead(false).
                    withTemplate(file).file(tempFile).sheet().doWrite(AAAA.dmodel_list);
        } else {
            // 第一次写入需要表头
            EasyExcel.write(file, DmodelData.class).sheet().doWrite(AAAA.dmodel_list);
        }

        if (tempFile.exists()) {
            //删除原模板文件，新生成的文件(tempFile)变成新的模板文件(file)
            file.delete();
            tempFile.renameTo(file);
        }
    }

    @Test
    public void bbb() {
        simpleWrite();
    }

    public static void simpleWrite() {
        // 注意 simpleWrite在数据量不大的情况下可以使用（5000以内，具体也要看实际情况），数据量大参照 重复多次写入

        String fileName = Path + "zhongjianbiao" + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(fileName, DmodelData.class).sheet("模板").doWrite(data1());
    }

    public static List<DmodelData> data1() {
        DmodelData dmodelData = new DmodelData();
        dmodelData.setTable_en("oneone");
        dmodelData.setTable_zh("表111");

        ArrayList<DmodelData> list = new ArrayList<>();
        list.add(dmodelData);
        return list;

    }

    //实现插入1行，需要传入插入的位置（插入的位置的行数）
    public static void insert_row(int lineno) throws Exception {
        String fileName = Path + "chayifenxi" + ".xlsx";
        FileInputStream fs = new FileInputStream(fileName);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fs);
        XSSFSheet insertSheet = xssfWorkbook.getSheetAt(0);
        //插入之前先下移行数，参数是（开始索引，结束索引，下移格数）
        if (lineno - 1 <= insertSheet.getLastRowNum()) {   //这行的意思：只有要插入的行数小于本sheet页的最后一行才能下移
            insertSheet.shiftRows(lineno - 1, insertSheet.getLastRowNum(), 1);
        }

        //创建 行 和 单元格
        XSSFRow row = insertSheet.createRow(lineno - 1);
        row.createCell(0).setCellValue(111);

        FileOutputStream out = new FileOutputStream(fileName);
        out.flush();
        xssfWorkbook.write(out);
        out.close();

    }

    public static void delete_row(int lineno) throws Exception {
        String fileName = Path + "chayifenxi" + ".xlsx";
        FileInputStream fs = new FileInputStream(fileName);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fs);
        XSSFSheet sheet = xssfWorkbook.getSheetAt(0);

        if (lineno <= sheet.getLastRowNum()) {
            sheet.shiftRows(lineno, sheet.getLastRowNum(), -1);
        }
        if (lineno > sheet.getLastRowNum()) {//这行的意思就是用空白行覆盖
            XSSFRow row = sheet.createRow(lineno - 1);
            row.createCell(0);
        }

        FileOutputStream out = new FileOutputStream(fileName);
        out.flush();
        xssfWorkbook.write(out);
        out.close();
        System.out.println("删除成功----------" + lineno + "行");
    }

    //插入一行并set数据，set颜色
    public static void insert_row1(ZhongjianbiaoData zhongjianbiaoData) throws Exception {
        String fileName = Path + "chayifenxi" + ".xlsx";
        FileInputStream fs = new FileInputStream(fileName);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fs);

        CellStyle style = xssfWorkbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFSheet insertSheet = xssfWorkbook.getSheetAt(0);
        //插入之前先下移行数，参数是（开始索引，结束索引，下移格数）
        if (zhongjianbiaoData.getNum() - 1 <= insertSheet.getLastRowNum()) {   //这行的意思：只有要插入的行数小于本sheet页的最后一行才能插
            insertSheet.shiftRows(zhongjianbiaoData.getNum() - 1, insertSheet.getLastRowNum(), 1);
        }

        //创建 行 和 单元格
        XSSFRow row = insertSheet.createRow(zhongjianbiaoData.getNum() - 1);
        //todo:set数据和set颜色
        XSSFCell zh_cell = row.createCell(4);
        if (!AAAA.changyifenxi_field_zh.contains(zhongjianbiaoData.getField_zh())) {   //中间表的属性在差异分析里没有，才设置颜色
            zh_cell.setCellStyle(style);
        }
        zh_cell.setCellValue(zhongjianbiaoData.getField_zh());

        row.createCell(0).setCellValue(zhongjianbiaoData.getSerial_num());
        row.createCell(2).setCellValue(zhongjianbiaoData.getTable_en());
        row.createCell(3).setCellValue(zhongjianbiaoData.getTable_zh());
        row.createCell(5).setCellValue(zhongjianbiaoData.getField_en());
        row.createCell(10).setCellValue(zhongjianbiaoData.getNull_flag());
        row.createCell(6).setCellValue(zhongjianbiaoData.getType_length());
        row.createCell(7).setCellValue(zhongjianbiaoData.getBusiness_value_description());
        row.createCell(8).setCellValue(zhongjianbiaoData.getField_value_description());
        row.createCell(9).setCellValue(zhongjianbiaoData.getPrimary_key_flag());

        FileOutputStream out = new FileOutputStream(fileName);
        out.flush();
        xssfWorkbook.write(out);
        out.close();
        System.out.println("插入成功-------------第" + zhongjianbiaoData.getNum() + "行");

    }

    //写PK
    public static void write_Primary_key_flag(ZhongjianbiaoData zhongjianbiaoData) throws Exception {
        String fileName = Path + "chayifenxi" + ".xlsx";
        FileInputStream fs = new FileInputStream(fileName);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fs);

        CellStyle style = xssfWorkbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
        XSSFRow row = sheet.getRow(zhongjianbiaoData.getNum() - 1);
        XSSFCell pk_cell = row.createCell(9);
        pk_cell.setCellStyle(style);
        pk_cell.setCellValue(zhongjianbiaoData.getPrimary_key_flag());

        FileOutputStream out = new FileOutputStream(fileName);
        out.flush();
        xssfWorkbook.write(out);
        out.close();
        System.out.println("pk修改成功-------------第" + zhongjianbiaoData.getNum() + "行");
    }

    //写null_flag
    public static void write_Null_flag(ZhongjianbiaoData zhongjianbiaoData) throws Exception {
        String fileName = Path + "chayifenxi" + ".xlsx";
        FileInputStream fs = new FileInputStream(fileName);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fs);

        CellStyle style = xssfWorkbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
        XSSFRow row = sheet.getRow(zhongjianbiaoData.getNum() - 1);
        XSSFCell pk_cell = row.createCell(10);
        pk_cell.setCellStyle(style);
        pk_cell.setCellValue(zhongjianbiaoData.getNull_flag());

        FileOutputStream out = new FileOutputStream(fileName);
        out.flush();
        xssfWorkbook.write(out);
        out.close();
        System.out.println("null_flag修改成功-------------第" + zhongjianbiaoData.getNum() + "行");
    }

    //写type_length
    public static void write_Type_length(ZhongjianbiaoData zhongjianbiaoData) throws Exception {
        String fileName = Path + "chayifenxi" + ".xlsx";
        FileInputStream fs = new FileInputStream(fileName);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fs);

        CellStyle style = xssfWorkbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
        XSSFRow row = sheet.getRow(zhongjianbiaoData.getNum() - 1);
        XSSFCell pk_cell = row.createCell(6);
        pk_cell.setCellStyle(style);
        pk_cell.setCellValue(zhongjianbiaoData.getType_length());

        FileOutputStream out = new FileOutputStream(fileName);
        out.flush();
        xssfWorkbook.write(out);
        out.close();
        System.out.println("type_length修改成功-------------第" + zhongjianbiaoData.getNum() + "行");
    }


    @Test
    public void aaa() throws Exception {
        insert_row(9);
    }


}
