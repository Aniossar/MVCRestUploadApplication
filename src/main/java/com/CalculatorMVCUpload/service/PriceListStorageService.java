package com.CalculatorMVCUpload.service;

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

@Service
@Log
public class PriceListStorageService {
}
