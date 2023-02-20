package com.CalculatorMVCUpload.controller.api;

import com.CalculatorMVCUpload.entity.UploadedFile;
import com.CalculatorMVCUpload.entity.users.UserEntity;
import com.CalculatorMVCUpload.exception.FileNotFoundException;
import com.CalculatorMVCUpload.payload.request.FileInfoChangeRequest;
import com.CalculatorMVCUpload.payload.response.UploadFileResponse;
import com.CalculatorMVCUpload.service.files.FileStorageService;
import com.CalculatorMVCUpload.service.files.UploadFileService;
import com.CalculatorMVCUpload.service.users.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;

@RestController
@Log
@RequestMapping("/api/updatefiles")
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private UserService userService;

    private final String markFileForAll = "ALL";

    @GetMapping("/allFiles")
    public List<UploadedFile> getAllFiles() {
        List<UploadedFile> allFiles = uploadFileService.getAllFiles();
        return allFiles;
    }

    @GetMapping("/getFile/{id}")
    public UploadedFile getFileViaId(@PathVariable int id) {
        UploadedFile uploadedFile = uploadFileService.getFileViaId(id);
        return uploadedFile;
    }

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file,
                                         @RequestParam("info") String info,
                                         @RequestParam("forClients") String forClients) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/updatefiles/downloadFile/")
                .path(fileName)
                .toUriString();

        Instant dateNow = Instant.now();

        UploadedFile uploadedFile = new UploadedFile(
                fileName,
                fileStorageService.getFileStorageLocation().toAbsolutePath().normalize() + "\\" + fileName,
                fileDownloadUri,
                dateNow,
                file.getSize(),
                file.hashCode());

        String author = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userService.findByLogin(author);
        uploadedFile.setAuthorId(userEntity.getId());
        uploadedFile.setInfo(info);
        uploadedFile.setForClients(forClients);
        uploadFileService.addNewFile(uploadedFile);

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFileController(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = fileStorageService.loadFileAsResource(fileName);
        return downloadFile(resource, request);
    }


    public ResponseEntity<Resource> downloadFile(Resource resource, HttpServletRequest request) {
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


    @DeleteMapping("/deleteFile/{id}")
    @RolesAllowed("ROLE_ADMIN")
    public void deleteFile(@PathVariable int id) {
        UploadedFile uploadedFile = uploadFileService.getFileViaId(id);
        Resource resource = fileStorageService.loadFileAsResource(uploadedFile.getName());
        try {
            boolean deleteResult = Files.deleteIfExists(Paths.get(resource.getFile().getAbsolutePath()));
            if (deleteResult) {
                uploadFileService.deleteFile(id);
            }
        } catch (Exception e) {
            throw new FileNotFoundException("File not found " + uploadedFile.getName());
        }
    }

    @GetMapping("/lastFile")
    public UploadedFile getLastUploadedFile() {
        try {
            UploadedFile lastFile = uploadFileService.getLastFile();
            return lastFile;
        } catch (Exception e) {
            throw new FileNotFoundException("No files uploaded");
        }
    }

    @GetMapping("/lastFile/{forClients}")
    public UploadedFile getLastUploadedFileForClients(@PathVariable String forClients) {
        UploadedFile lastFileForAll = uploadFileService.getLastFileByForClients(markFileForAll);
        UploadedFile lastFileForClients = uploadFileService.getLastFileByForClients(forClients);
        if (lastFileForAll == null) {
            return lastFileForClients;
        } else if (lastFileForClients == null) {
            return lastFileForAll;
        } else {
            return (lastFileForClients.getId() > lastFileForAll.getId() ? lastFileForClients : lastFileForAll);
        }
    }

    @PostMapping("/editFileInfo/{id}")
    public void editFileInfo(@RequestBody FileInfoChangeRequest request,
                             @PathVariable int id) {
        UploadedFile uploadedFile = uploadFileService.getFileViaId(id);
        if (request.getInfo() != null) {
            uploadedFile.setInfo(request.getInfo());
        }
        if (request.getForClients() != null) {
            uploadedFile.setForClients(request.getForClients());
        }
        uploadFileService.addNewFile(uploadedFile);
    }

}
