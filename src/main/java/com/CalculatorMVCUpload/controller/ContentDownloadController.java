package com.CalculatorMVCUpload.controller;

import com.CalculatorMVCUpload.controller.api.FileController;
import com.CalculatorMVCUpload.service.files.ContentStorageService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/content")
@Log
public class ContentDownloadController {

    @Autowired
    private ContentStorageService contentStorageService;

    @Autowired
    private FileController fileController;


    @CrossOrigin
    @GetMapping("/**")
    public ResponseEntity<Resource> downloadContent(HttpServletRequest request) {
        String contentPath = new AntPathMatcher().extractPathWithinPattern("content/**", request.getRequestURI());
        contentPath = contentPath.replaceAll("%20", " ");
        Resource resource = contentStorageService.loadContentAsResource(contentPath);
        return fileController.downloadFile(resource, request);
    }
}
