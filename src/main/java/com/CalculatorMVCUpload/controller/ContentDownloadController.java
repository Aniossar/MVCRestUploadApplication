package com.CalculatorMVCUpload.controller;

import com.CalculatorMVCUpload.controller.api.FileController;
import com.CalculatorMVCUpload.service.files.ContentStorageService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
        try {
            contentPath = URLDecoder.decode(contentPath, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            log.warning("UnsupportedEncodingException in Content controller");
        }

        Resource resource = contentStorageService.loadContentAsResource(contentPath);
        return fileController.downloadFile(resource, request);

    }
}
