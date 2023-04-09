package com.CalculatorMVCUpload.controller.api;

import com.CalculatorMVCUpload.payload.response.DirectoryCheckResponse;
import com.CalculatorMVCUpload.service.files.ClientDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/client")
public class ClientDownloadController {

    @Autowired
    private FileController fileController;

    @Autowired
    private ClientDownloadService clientDownloadService;

    @CrossOrigin
    @GetMapping("/getAllFiles")
    public List<DirectoryCheckResponse> getAllClientFiles() {
        return clientDownloadService.displayAllFilesInDirectory();
    }

    @CrossOrigin
    @GetMapping("/downloadClient/{fileName:.+}")
    public ResponseEntity<Resource> downloadClient(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = clientDownloadService.loadClientAsResource(fileName);
        return fileController.downloadFile(resource, request);
    }
}
