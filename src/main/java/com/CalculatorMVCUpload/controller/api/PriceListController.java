package com.CalculatorMVCUpload.controller.api;

import com.CalculatorMVCUpload.entity.PriceListEntity;
import com.CalculatorMVCUpload.entity.users.UserEntity;
import com.CalculatorMVCUpload.exception.FileNotFoundException;
import com.CalculatorMVCUpload.payload.request.FileInfoChangeRequest;
import com.CalculatorMVCUpload.payload.response.UploadFileResponse;
import com.CalculatorMVCUpload.service.files.PriceListStorageService;
import com.CalculatorMVCUpload.service.files.PriceListUploadService;
import com.CalculatorMVCUpload.service.files.UploadFileService;
import com.CalculatorMVCUpload.service.users.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;

@RestController
@Log
@RequestMapping("/api/pricelists")
public class PriceListController {

    @Autowired
    private PriceListStorageService priceListStorageService;

    @Autowired
    private PriceListUploadService priceListUploadService;

    @Autowired
    private FileController fileController;

    @Autowired
    private UserService userService;

    @Autowired
    private UploadFileService uploadFileService;

    private final String markFileForAll = "ALL";

    @CrossOrigin
    @GetMapping("/allFiles")
    public List<PriceListEntity> getAllPrices() {
        List<PriceListEntity> allFiles = priceListUploadService.getAllFiles();
        allFiles.forEach(file -> {
            file.setForClients(uploadFileService.transformForClientsOutput(file.getForClients()));
        });
        return allFiles;
    }

    @CrossOrigin
    @GetMapping("/getFile/{id}")
    public PriceListEntity getPricelistViaId(@PathVariable int id) {
        PriceListEntity fileViaId = priceListUploadService.getFileViaId(id);
        fileViaId.setForClients(uploadFileService.transformForClientsOutput(fileViaId.getForClients()));
        return fileViaId;
    }

    @CrossOrigin
    @PostMapping("/uploadFile")
    public UploadFileResponse uploadPriceFile(@RequestParam("file") MultipartFile file,
                                              @RequestParam("info") String info,
                                              @RequestParam("forClients") String forClients) {
        try {
            String fileName = priceListStorageService.storeFile(file);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/pricelists/downloadFile/")
                    .path(fileName)
                    .toUriString();

            Instant timeNow = Instant.now();

            PriceListEntity priceList = new PriceListEntity();
            priceList.setName(fileName);
            priceList.setPath(priceListStorageService.getFileStorageLocation().toAbsolutePath().normalize() + "\\" + fileName);
            priceList.setUploadTime(timeNow);
            priceList.setUrl(fileDownloadUri);
            priceList.setInfo(info);
            priceList.setForClients(uploadFileService.transformForClientsString(forClients).toLowerCase());

            String author = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity userEntity = userService.findByLogin(author);
            priceList.setAuthorId(userEntity.getId());

            priceListUploadService.addNewFile(priceList);

            return new UploadFileResponse(fileName, fileDownloadUri,
                    file.getContentType(), file.getSize());
        } catch (Exception e) {
            log.warning(e.getMessage());
        }
        return null;
    }

    @CrossOrigin
    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadPriceFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = priceListStorageService.loadPriceFileAsResource(fileName);
        return fileController.downloadFile(resource, request);
    }

    @CrossOrigin
    @DeleteMapping("/deleteFile/{id}")
    @RolesAllowed("ROLE_ADMIN")
    public void deletePriceFile(@PathVariable int id) {
        PriceListEntity priceList = priceListUploadService.getFileViaId(id);
        Resource resource = priceListStorageService.loadPriceFileAsResource(priceList.getName());
        try {
            boolean deleteResult = Files.deleteIfExists(Paths.get(resource.getFile().getAbsolutePath()));
            if (deleteResult) {
                priceListUploadService.deleteFile(id);
            }
        } catch (Exception e) {
            throw new FileNotFoundException("File not found " + priceList.getName());
        }
    }

    @CrossOrigin
    @GetMapping("/lastFile")
    public PriceListEntity getLastUploadedFile() {
        try {
            PriceListEntity lastFile = priceListUploadService.getLastFile();
            lastFile.setForClients(uploadFileService.transformForClientsOutput(lastFile.getForClients()));
            return lastFile;
        } catch (Exception e) {
            throw new FileNotFoundException("No price files uploaded");
        }
    }

    @CrossOrigin
    @GetMapping("/lastFile/{forClients}")
    public PriceListEntity getLastPriceListForClients(@PathVariable String forClients) {
        PriceListEntity priceListForAll = priceListUploadService.getLastPriceListByForClients(markFileForAll);
        PriceListEntity priceListForClients = priceListUploadService
                .getLastPriceListByForClients("+" + forClients.toLowerCase() + "+");
        priceListForAll.setForClients(uploadFileService.transformForClientsOutput(priceListForAll.getForClients()));
        priceListForClients.setForClients(uploadFileService.transformForClientsOutput(priceListForClients.getForClients()));
        if (priceListForAll == null) {
            return priceListForClients;
        } else if (priceListForClients == null) {
            return priceListForAll;
        } else {
            return (priceListForClients.getId() > priceListForAll.getId() ? priceListForClients : priceListForAll);
        }
    }

    @PostMapping("/editFileInfo/{id}")
    public void editFileInfo(@RequestBody FileInfoChangeRequest request,
                             @PathVariable int id) {
        PriceListEntity priceListEntity = priceListUploadService.getFileViaId(id);
        if (request.getInfo() != null) {
            priceListEntity.setInfo(request.getInfo());
        }
        if (request.getForClients() != null) {
            priceListEntity.setForClients(uploadFileService.transformForClientsString(request.getForClients()).toLowerCase());
        }
        priceListUploadService.addNewFile(priceListEntity);
    }
}
