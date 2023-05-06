package com.CalculatorMVCUpload.service.files;

import com.CalculatorMVCUpload.exception.FileStorageException;
import com.CalculatorMVCUpload.property.FileStorageProperties;
import com.CalculatorMVCUpload.service.GeneralFileService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Log
public class ContentStorageService {

    @Autowired
    private GeneralFileService generalFileService;

    private Path fileStorageLocation;

    public Path getFileStorageLocation() {
        return fileStorageLocation;
    }

    @Autowired
    public ContentStorageService(FileStorageProperties fileStorageProperties) {

        String fileUploadDir = new File("").getAbsolutePath() + fileStorageProperties.getUploadDir() + "/Content/";
        this.fileStorageLocation = Paths.get(fileUploadDir);

        try {
            Files.createDirectories(this.fileStorageLocation);
            log.info("Content files will be stored in directory: " + fileStorageLocation.toAbsolutePath().normalize());
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where content files will be stored.", ex);
        }
    }

    public Resource loadContentAsResource(String fileName) {
        return generalFileService.loadFileAsResource(this.fileStorageLocation, fileName);
    }
}
