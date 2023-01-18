package com.CalculatorMVCUpload.service;

import com.CalculatorMVCUpload.entity.CalculatorActivityEntity;
import lombok.extern.java.Log;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Locale;

@Service
@Log
public class ExcelWriterService {

    public void writeExcelFile() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Instant dateTime = Instant.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                .withLocale(Locale.ENGLISH)
                .withZone(ZoneOffset.UTC);
        String dateTimeStr = formatter.format(dateTime);
        String fileName = "Users-Receipt-Summary-Filtered " + dateTimeStr;
        try (FileOutputStream outputStream = new FileOutputStream(fileName + ".xlsx")) {
            workbook.write(outputStream);
            log.info("File " + fileName + " has been created");
        } catch (IOException e) {
            log.warning(e.getMessage());
            log.warning("Problem with writing " + fileName);
        }
    }

    /*public void writeFilteredCalculatorService(XSSFWorkbook workbook, ArrayList<CalculatorActivityEntity> list) {
        XSSFSheet sheet = workbook.createSheet("Sheet1");
        XSSFRow titleRow = sheet.createRow(0);

        ArrayList listOfFields = new ArrayList();
        Field[] fields = CalculatorActivityEntity.class.getFields();

        int i = 0;
        for (CalculatorActivityEntity x : list) {
            XSSFCell cell = titleRow.createCell(i);
            cell.setCellValue();
        }
    }*/
}
