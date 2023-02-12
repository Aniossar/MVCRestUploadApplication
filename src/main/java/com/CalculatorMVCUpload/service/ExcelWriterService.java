package com.CalculatorMVCUpload.service;

import com.CalculatorMVCUpload.entity.CalculatorActivityEntity;
import com.CalculatorMVCUpload.exception.FileNotFoundException;
import com.CalculatorMVCUpload.exception.FileStorageException;
import com.CalculatorMVCUpload.property.FileStorageProperties;
import lombok.extern.java.Log;
import org.apache.poi.xssf.usermodel.XSSFCell;
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

    public String writeExcelFile(List<CalculatorActivityEntity> list) {
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

    public XSSFWorkbook writeFilteredCalculatorService(List<CalculatorActivityEntity> list) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Sheet1");

        CalculatorActivityEntity entity = new CalculatorActivityEntity();
        List<String> classFieldsName = entity.getClassFieldsName();

        int column = 0;
        XSSFRow titleRow = sheet.createRow(0);
        for (String x : classFieldsName) {
            XSSFCell cell = titleRow.createCell(column);
            cell.setCellValue(x);
            column++;
        }
        int rowNum = 1;
        for (CalculatorActivityEntity activity : list) {
            XSSFRow row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(activity.getId());
            row.createCell(1).setCellValue(activity.getActivityTime().toString());
            row.createCell(2).setCellValue(activity.getUserId());
            row.createCell(4).setCellValue(activity.getCompanyName());
            row.createCell(5).setCellValue(activity.getCertainPlaceAddress());
            row.createCell(6).setCellValue(activity.getType());
            row.createCell(7).setCellValue(activity.getMaterials());
            row.createCell(8).setCellValue(activity.getMaterialPrice());
            row.createCell(9).setCellValue(activity.getAddPrice());
            row.createCell(10).setCellValue(activity.getAllPrice());
            row.createCell(11).setCellValue(activity.getMainCoeff());
            row.createCell(12).setCellValue(activity.getMaterialCoeff());
            row.createCell(13).setCellValue(activity.getSlabs());
            row.createCell(14).setCellValue(activity.getProductSquare());
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
