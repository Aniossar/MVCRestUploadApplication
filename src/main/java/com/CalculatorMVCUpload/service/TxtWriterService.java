package com.CalculatorMVCUpload.service;

import com.CalculatorMVCUpload.exception.FileNotFoundException;
import com.CalculatorMVCUpload.exception.FileStorageException;
import com.CalculatorMVCUpload.property.FileStorageProperties;
import com.CalculatorMVCUpload.service.users.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Service
@Log
public class TxtWriterService {

    @Autowired
    private UserService userService;

    private Path fileStorageLocation;

    public Path getFileStorageLocation() {
        return fileStorageLocation;
    }

    public TxtWriterService(FileStorageProperties fileStorageProperties) {
        String fileUploadDir = new File("").getAbsolutePath() + fileStorageProperties.getUploadDir()
                + "/UserClaims/";
        this.fileStorageLocation = Paths.get(fileUploadDir);
        try {
            Files.createDirectories(this.fileStorageLocation);
            log.info("User claims will be stored in directory: "
                    + fileStorageLocation.toAbsolutePath().normalize());
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String saveTxtReportWithClaim(String claimType, Instant dateTime, String claimText, int userId) {
        String userLogin = userService.findById(userId).getLogin();
        StringBuilder resultString = new StringBuilder();
        resultString.append("Пользователь: id = ").append(userId).append(", ")
                .append("login = ").append(userLogin).append("\n")
                .append("Тип обращения: ").append(claimType).append("\n")
                .append("Дата: ").append(dateTime.toString()).append("\n")
                .append("Текст обращения: ").append(claimText);

        LocalDateTime localDateTime = LocalDateTime.ofInstant(dateTime, ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss");
        String dateTimeStr = formatter.format(localDateTime);

        String stringToFile = resultString.toString();
        String fileName = claimType + "_" + userLogin + "_" + dateTimeStr + "UTC";
        try {
            Files.write(Path.of(getFileStorageLocation().toString() + "/" + fileName + ".txt"),
                    stringToFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
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
