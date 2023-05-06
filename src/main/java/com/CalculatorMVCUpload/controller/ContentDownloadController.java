package com.CalculatorMVCUpload.controller;

import com.CalculatorMVCUpload.controller.api.FileController;
import com.CalculatorMVCUpload.service.files.ContentStorageService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/content")
@Log
public class ContentDownloadController {

    @Autowired
    private ContentStorageService contentStorageService;

    @Autowired
    private FileController fileController;

    private final static String windowsCharset = "Windows-1251";
    private final static String linuxCharset = "UTF-8";


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
