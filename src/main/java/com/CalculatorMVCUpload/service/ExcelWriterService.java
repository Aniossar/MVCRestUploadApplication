package com.CalculatorMVCUpload.service;

import com.CalculatorMVCUpload.entity.CalculatorActivityEntity;
import com.CalculatorMVCUpload.exception.FileNotFoundException;
import com.CalculatorMVCUpload.exception.FileStorageException;
import com.CalculatorMVCUpload.payload.response.CalculatorActivityResponse;
import com.CalculatorMVCUpload.property.FileStorageProperties;
import lombok.extern.java.Log;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Log
public class ExcelWriterService {

    private Path fileStorageLocation;

    public static final class FilteredCalculatorTitles{
        public static final String ACTIVITY_TIME="Дата";
        public static final String USER_ID="ID пользователя";
        public static final String USER_LOGIN="Пользователь";
        public static final String COMPANY_NAME="Салон";
        public static final String CERTAIN_PLACE_ADDRESS="Адрес салона продаж";
        public static final String TYPE="Тип события";
        public static final String MATERIALS="Материал";
        public static final String MATERIAL_PRICE="Цена материала (₽)";
        public static final String ADD_PRICE="Цена доп. работ (₽)";
        public static final String ALL_PRICE="Цена счета (₽)";
        public static final String MAIN_COEFF="Основной коэффициент";
        public static final String MATERIAL_COEFF="Коэффициент материала";
        public static final String SLABS="Кол-во слэбов";
        public static final String PRODUCT_SQUARE="Площадь изделия (м2)";
    }

    public Path getFileStorageLocation() {
        return fileStorageLocation;
    }

    public ExcelWriterService(FileStorageProperties fileStorageProperties) {
        String fileUploadDir = new File("").getAbsolutePath() + fileStorageProperties.getUploadDir()
                + "/FilteredRecipes/";
        this.fileStorageLocation = Paths.get(fileUploadDir);
        try {
            Files.createDirectories(this.fileStorageLocation);
            log.info("Filtered Recipes will be stored in directory: "
                    + fileStorageLocation.toAbsolutePath().normalize());
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String writeExcelFile(List<CalculatorActivityResponse> list) {
        XSSFWorkbook workbook = writeFilteredCalculatorService(list);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss");
        LocalDateTime dateTime = LocalDateTime.now();
        String dateTimeStr = formatter.format(dateTime);
        String fileName = "Users-Receipt-Summary-Filtered_" + dateTimeStr;

        try (FileOutputStream outputStream
                     = new FileOutputStream(getFileStorageLocation().toString() + "/" + fileName + ".xlsx")) {
            workbook.write(outputStream);
            log.info("File " + fileName + " has been created");
            return fileName;
        } catch (IOException e) {
            log.warning(e.getMessage());
            log.warning("Problem with writing " + fileName);
        }
        return null;
    }

    public XSSFWorkbook writeFilteredCalculatorService(List<CalculatorActivityResponse> list) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Sheet1");

        CalculatorActivityEntity entity = new CalculatorActivityEntity();
        List<String> classFieldsName = entity.getClassFieldsName();

        int column = 0;
        XSSFRow titleRow = sheet.createRow(0);
        titleRow.createCell(0).setCellValue(FilteredCalculatorTitles.ACTIVITY_TIME);
        titleRow.createCell(1).setCellValue(FilteredCalculatorTitles.USER_ID);
        titleRow.createCell(2).setCellValue(FilteredCalculatorTitles.USER_LOGIN);
        titleRow.createCell(3).setCellValue(FilteredCalculatorTitles.COMPANY_NAME);
        titleRow.createCell(4).setCellValue(FilteredCalculatorTitles.CERTAIN_PLACE_ADDRESS);
        titleRow.createCell(5).setCellValue(FilteredCalculatorTitles.TYPE);
        titleRow.createCell(6).setCellValue(FilteredCalculatorTitles.MATERIALS);
        titleRow.createCell(7).setCellValue(FilteredCalculatorTitles.MATERIAL_PRICE);
        titleRow.createCell(8).setCellValue(FilteredCalculatorTitles.ADD_PRICE);
        titleRow.createCell(9).setCellValue(FilteredCalculatorTitles.ALL_PRICE);
        titleRow.createCell(10).setCellValue(FilteredCalculatorTitles.MAIN_COEFF);
        titleRow.createCell(11).setCellValue(FilteredCalculatorTitles.MATERIAL_COEFF);
        titleRow.createCell(12).setCellValue(FilteredCalculatorTitles.SLABS);
        titleRow.createCell(13).setCellValue(FilteredCalculatorTitles.PRODUCT_SQUARE);

        int rowNum = 1;
        for (CalculatorActivityResponse activity : list) {
            XSSFRow row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(activity.getActivityTime().toString());
            row.createCell(1).setCellValue(activity.getUserId());
            row.createCell(2).setCellValue(activity.getUserLogin());
            row.createCell(3).setCellValue(activity.getCompanyName());
            row.createCell(4).setCellValue(activity.getCertainPlaceAddress());
            row.createCell(5).setCellValue(activity.getType());
            row.createCell(6).setCellValue(activity.getMaterials());
            row.createCell(7).setCellValue(activity.getMaterialPrice());
            row.createCell(8).setCellValue(activity.getAddPrice());
            row.createCell(9).setCellValue(activity.getAllPrice());
            row.createCell(10).setCellValue(activity.getMainCoeff());
            row.createCell(11).setCellValue(activity.getMaterialCoeff());
            row.createCell(12).setCellValue(activity.getSlabs());
            row.createCell(13).setCellValue(activity.getProductSquare());
            rowNum++;
        }
        return workbook;
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName, ex);
        }
    }
}
