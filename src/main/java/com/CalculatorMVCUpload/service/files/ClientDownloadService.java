package com.CalculatorMVCUpload.service.files;

import com.CalculatorMVCUpload.exception.FileStorageException;
import com.CalculatorMVCUpload.payload.response.DirectoryCheckResponse;
import com.CalculatorMVCUpload.property.FileStorageProperties;
import com.CalculatorMVCUpload.service.GeneralFileService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Log
public class ClientDownloadService {

    @Autowired
    private GeneralFileService generalFileService;

    private Path fileStorageLocation;

    public Path getFileStorageLocation() {
        return fileStorageLocation;
    }

    @Autowired
    public ClientDownloadService(FileStorageProperties fileStorageProperties) {

        String fileUploadDir = new File("").getAbsolutePath() + fileStorageProperties.getUploadDir() + "/Clients/";
        this.fileStorageLocation = Paths.get(fileUploadDir);

        try {
            Files.createDirectories(this.fileStorageLocation);
            log.info("Client distributive will be stored in directory: " + fileStorageLocation.toAbsolutePath().normalize());
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where client distributive will be stored.", ex);
        }
    }

    public List<DirectoryCheckResponse> displayAllFilesInDirectory() {
        List<DirectoryCheckResponse> responseList = new ArrayList<>();
        String filePath = this.fileStorageLocation.toString();
        File currentDirectory = new File(filePath);
        try {
            File[] files = currentDirectory.listFiles();
            for (File file : files) {
                DirectoryCheckResponse response = new DirectoryCheckResponse();
                response.setName(file.getName());
                response.setSize(file.length());
                response.setDateModified(file.lastModified());
                responseList.add(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseList;
    }

    public Resource loadClientAsResource(String fileName) {
        return generalFileService.loadFileAsResource(this.fileStorageLocation, fileName);
    }
}