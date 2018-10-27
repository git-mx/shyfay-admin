package com.shyfay.admin.web.controller;

import com.shyfay.admin.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import com.shyfay.admin.bean.Student;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mx on 2018/8/18 0018.
 */
@Controller
@RequestMapping("/student")
@Api(value = "随机排序服务", description = "随机排序服务")
@Slf4j
public class StudentController {

    @Autowired
    private StudentService studentService;

    @ApiOperation(value="导入学生信息", httpMethod = "POST", notes = "导入学生信息")
    @RequestMapping(value = "/importData", method= RequestMethod.POST)
    public String importData(@RequestParam("file") MultipartFile file) {
        try{
            Workbook hssfWorkbook = WorkbookFactory.create(file.getInputStream());
            Sheet tempSheet = hssfWorkbook.getSheetAt(0);
            List<Student> students = new ArrayList<>();
            for (Row row : tempSheet) {
                if(row.getRowNum() < 1){
                    continue;
                }
                Student student = new Student();
                student.setGroupNo(0);
                row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
                student.setStudentName(row.getCell(0).getStringCellValue());
                student.setParentName(row.getCell(1).getStringCellValue());
                student.setContactPhone(row.getCell(2).getStringCellValue());
                students.add(student);
                studentService.add(student);
            }
            return "success";
        }catch (Exception e){
            log.error("导入学生信息报错：" + e.getMessage());
            return "fail";
        }
    }

    @ApiOperation(value="随机分组", httpMethod = "GET", notes = "随机分组")
    @RequestMapping(value="/randGroup", method= RequestMethod.GET)
    public String randGroup(){
        try{
            int groupNo = 0;
            while(groupNo > -1){
                groupNo++;
                List<Student> students = studentService.getRand();
                if(students.size() > 0){
                    for(int i=0; i<students.size(); i++){
                        Student student = students.get(i);
                        student.setGroupNo(groupNo);
                        studentService.update(student);
                    }
                }else{
                    groupNo = -1;
                }
            }
            return "success";
        }catch (Exception e){
            log.error("随机分组报错：" + e.getMessage());
            return "fail";
        }
    }

    @ApiOperation(value="处理双胞胎数据", httpMethod = "GET", notes = "处理双胞胎数据")
    @RequestMapping(value="/handleTwinsData", method= RequestMethod.GET)
    public String handleTwinsData(
            @ApiParam(required = true, name = "nameOne", value = "双胞胎中第一个小孩姓名") @RequestParam String nameOne,
            @ApiParam(required = true, name = "nameTwo", value = "双胞胎中第二个小孩姓名") @RequestParam String nameTwo
    ){
        try{
            List<Student> twins = studentService.listTwins(nameOne, nameTwo);
            Student one = twins.get(0);
            Student two = twins.get(1);
            Student exchange = studentService.getExchange(one.getGroupNo(), one.getStudentId());
            exchange.setGroupNo(two.getGroupNo());
            two.setGroupNo(one.getGroupNo());
            studentService.update(two);
            studentService.update(exchange);
            return "success";
        }catch (Exception e){
            log.error("处理双胞胎数据报错：" + e.getMessage());
            return "fail";
        }
    }


    @ApiOperation(value="下载分好组的数据", httpMethod = "GET", notes = "下载数据")
    @RequestMapping(value="/dwonloadData", method = RequestMethod.GET)
    public String dwonloadData(HttpServletResponse response){
        try{
            String sheetName = "Sheet1";
            String titleName = "幼儿园学生分组信息表";
            String fileName = URLEncoder.encode("幼儿园学生分组信息表.xls");
            int columnNumber = 4;
            int[] columnWidth = { 20, 20, 20, 50 };
            String[] columnName = {"组号", "学生姓名", "父母姓名", "联系电话"};
            List<Student> currList = studentService.list();
            ExportWithResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, currList, response);
            return "success";
        }catch (Exception e){
            log.error("下载分好组的数据报错：" + e.getMessage());
            return "fail";
        }
    }

    //将数据库记录导出到EXCEL输出流
    private void ExportWithResponse(String sheetName, String titleName,
                                    String fileName, int columnNumber, int[] columnWidth,
                                    String[] columnName, List<Student> datas,
                                    HttpServletResponse response) throws Exception {
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = workbook.createSheet(sheetName);
        // sheet.setDefaultColumnWidth(15); //统一设置列宽
        for (int i = 0; i < columnNumber; i++)
        {
            for (int j = 0; j <= i; j++)
            {
                if (i == j)
                {
                    sheet.setColumnWidth(i, columnWidth[j] * 256); // 单独设置每列的宽
                }
            }
        }
        // 创建第0行 也就是标题
        HSSFRow row1 = sheet.createRow((int) 0);
        row1.setHeightInPoints(50);// 设备标题的高度
        // 第三步创建标题的单元格样式style2以及字体样式headerFont1
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style2.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        HSSFFont headerFont1 = (HSSFFont) workbook.createFont(); // 创建字体样式
        headerFont1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体加粗
        headerFont1.setFontName("黑体"); // 设置字体类型
        headerFont1.setFontHeightInPoints((short) 15); // 设置字体大小
        style2.setFont(headerFont1); // 为标题样式设置字体样式

        HSSFCell cell1 = row1.createCell(0);// 创建标题第一列
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0,
                columnNumber - 1)); // 合并列标题
        cell1.setCellValue(titleName); // 设置值标题
        cell1.setCellStyle(style2); // 设置标题样式

        // 创建第1行 也就是表头
        HSSFRow row = sheet.createRow((int) 1);
        row.setHeightInPoints(37);// 设置表头高度

        // 第四步，创建表头单元格样式 以及表头的字体样式
        HSSFCellStyle style = workbook.createCellStyle();
        style.setWrapText(true);// 设置自动换行
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 创建一个居中格式

        style.setBottomBorderColor(HSSFColor.BLACK.index);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);

        HSSFFont headerFont = (HSSFFont) workbook.createFont(); // 创建字体样式
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体加粗
        headerFont.setFontName("黑体"); // 设置字体类型
        headerFont.setFontHeightInPoints((short) 10); // 设置字体大小
        style.setFont(headerFont); // 为标题样式设置字体样式

        // 第四.一步，创建表头的列
        for (int i = 0; i < columnNumber; i++)
        {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(columnName[i]);
            cell.setCellStyle(style);
        }

        // 第五步，创建单元格，并设置值
        if(datas != null && datas.size() > 0){
            for (int i = 0; i < datas.size(); i++)
            {
                row = sheet.createRow((int) i + 2);
                // 为数据内容设置特点新单元格样式2 自动换行 上下居中左右也居中
                HSSFCellStyle dataCellStyle = workbook.createCellStyle();
                dataCellStyle.setWrapText(true);// 设置自动换行
                dataCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 创建一个上下居中格式
                dataCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中

                // 设置边框
                dataCellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
                dataCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                dataCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                dataCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                dataCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
                HSSFCell datacell = null;
                datacell = row.createCell(0);
                datacell.setCellValue(datas.get(i).getGroupNo());
                datacell.setCellStyle(dataCellStyle);
                datacell = row.createCell(1);
                datacell.setCellValue(datas.get(i).getStudentName());
                datacell.setCellStyle(dataCellStyle);
                datacell = row.createCell(2);
                datacell.setCellValue(datas.get(i).getParentName());
                datacell.setCellStyle(dataCellStyle);
                datacell = row.createCell(3);
                datacell.setCellValue(datas.get(i).getContactPhone());
                datacell.setCellStyle(dataCellStyle);
            }
        }
        for(int i=2; i <= datas.size(); i=i+2){
            sheet.addMergedRegion(new CellRangeAddress(i, i+1, 0,
                    0));
        }
        fileName = new String(fileName.getBytes("GB2312"), "ISO-8859-1");
        // 第六步，将文件存到浏览器设置的下载位置
        response.reset();
        response.setContentType("application/vnd.ms-excel; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.setCharacterEncoding("utf-8");
        OutputStream out = response.getOutputStream();
        try {
            workbook.write(out);
            out.flush();
        } catch (Exception e) {
            log.error("导出到EXCEL报错：" + e.getMessage());
            e.printStackTrace();
        } finally {
            if(null!=out){
                out.close();
            }
        }
    }

}