package com.CalculatorMVCUpload.controller.api;

import com.CalculatorMVCUpload.entity.PriceListEntity;
import com.CalculatorMVCUpload.exception.FileNotFoundException;
import com.CalculatorMVCUpload.payload.response.UploadFileResponse;
import com.CalculatorMVCUpload.service.PriceListStorageService;
import com.CalculatorMVCUpload.service.PriceListUploadService;
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
@RequestMapping("/api/pricelists")
public class PriceListController {

    @Autowired
    private PriceListStorageService priceListStorageService;

    @Autowired
    private PriceListUploadService priceListUploadService;

    @GetMapping("/allFiles")
    public List<PriceListEntity> getAllPrices() {
        return priceListUploadService.getAllFiles();
    }

    @GetMapping("/getFile/{id}")
    public PriceListEntity getPricelistViaId(@PathVariable int id) {
        return priceListUploadService.getFileViaId(id);
    }

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadPriceFile(@RequestParam("file") MultipartFile file) {
        try{
            String fileName = priceListStorageService.storeFile(file);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFile/")
                    .path(fileName)
                    .toUriString();

            Instant timeNow = Instant.now();

            PriceListEntity priceList = new PriceListEntity();
            priceList.setName(fileName);
            priceList.setPath(priceListStorageService.getFileStorageLocation().toAbsolutePath().normalize() + "\\" + fileName);
            priceList.setUploadTime(timeNow);
            priceList.setUrl(fileDownloadUri);
            priceList.setUserAuthor(SecurityContextHolder.getContext().getAuthentication().getName());

            priceListUploadService.addNewFile(priceList);

            return new UploadFileResponse(fileName, fileDownloadUri,
                    file.getContentType(), file.getSize());
        } catch (Exception e){
            log.warning(e.getMessage());
        }
        return null;
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadPriceFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = priceListStorageService.loadPriceFileAsResource(fileName);

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


    @GetMapping("/lastFile")
    public PriceListEntity getLastUploadedFile() {
        try {
            return priceListUploadService.getLastFile();
        } catch (Exception e) {
            throw new FileNotFoundException("No price files uploaded");
        }
    }
}
