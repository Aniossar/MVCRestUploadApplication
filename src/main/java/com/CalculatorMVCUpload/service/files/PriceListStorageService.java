package com.CalculatorMVCUpload.service.files;

import com.CalculatorMVCUpload.exception.BadNamingException;
import com.CalculatorMVCUpload.exception.FileNotFoundException;
import com.CalculatorMVCUpload.exception.FileStorageException;
import com.CalculatorMVCUpload.property.FileStorageProperties;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
@Log
public class PriceListStorageService {

    private final String fileNamePrefix = "KPR_";
    private final String fileNameSuffixA = "xls";
    private final String fileNameSuffixB = "lsx";

    private Path fileStorageLocation;

    public Path getFileStorageLocation() {
        return fileStorageLocation;
    }

    @Autowired
    public PriceListStorageService(FileStorageProperties fileStorageProperties) {

        String fileUploadDir = new File("").getAbsolutePath() + fileStorageProperties.getUploadDir() + "/Prices/";
        this.fileStorageLocation = Paths.get(fileUploadDir);

        try {
            Files.createDirectories(this.fileStorageLocation);
            log.info("Files will be stored in directory: " + fileStorageLocation.toAbsolutePath().normalize());
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                throw new BadNamingException("Filename contains invalid path sequence " + fileName);
            }

            String actualFilePrefix = fileName.substring(0, 4);
            String actualFileSuffix = fileName.substring(fileName.length() - 3);

            if (!actualFilePrefix.equals(fileNamePrefix) ||
                    !(actualFileSuffix.equals(fileNameSuffixA) || actualFileSuffix.equals(fileNameSuffixB))) {
                throw new BadNamingException
                        ("Filename should be in Excel format and start with 'KPR_*'. Rename the file " + fileName);
            }

            Instant timeNow = Instant.now();
            String patternFormat = "dd.MM.yyyy_hh-mm-ss";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(patternFormat).withZone(ZoneId.systemDefault());

            fileName = StringUtils.cleanPath(formatter.format(timeNow) + "_" + file.getOriginalFilename());

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadPriceFileAsResource(String fileName) {
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
