package com.CalculatorMVCUpload.service.files;

import com.CalculatorMVCUpload.entity.UploadedFile;
import com.CalculatorMVCUpload.repository.UploadedFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UploadFileService {

    @Autowired
    private UploadedFileRepository uploadedFileRepository;

    @Transactional
    public List<UploadedFile> getAllFiles() {
        return uploadedFileRepository.findAll();
    }

    @Transactional
    public UploadedFile getFileViaId(int id) {
        return uploadedFileRepository.getById(id);
    }

    @Transactional
    public void addNewFile(UploadedFile uploadedFile) {
        uploadedFileRepository.saveAndFlush(uploadedFile);
    }

    @Transactional
    public void deleteFile(int id) {
        uploadedFileRepository.deleteById(id);
    }

    @Transactional
    public UploadedFile getLastFile() {
        return uploadedFileRepository.findTopByOrderByIdDesc();
    }

    @Transactional
    public UploadedFile getLastFileByForClients(String forClients) {
        return uploadedFileRepository.findTopByForClientsContainingOrderByIdDesc(forClients);
    }

    @Transactional
    public void editFileInfo(UploadedFile uploadedFile) {
        uploadedFileRepository.save(uploadedFile);
    }

    public String transformForClientsString(String forClients) {
        Pattern pattern = Pattern.compile(",");
        String[] str = pattern.split(forClients);
        String newForClients = "";
        for (String strElement : str) {
            newForClients = newForClients + "+" + strElement + "+";
        }
        return newForClients;
    }

    public String transformForClientsOutput(String forClientsFromDb) {
        String result = "";
        Pattern pattern = Pattern.compile("[a-zA-Z]+");
        Matcher matcher = pattern.matcher(forClientsFromDb);
        while (matcher.find()) {
            result += forClientsFromDb.substring(matcher.start(), matcher.end()) + ",";
        }
        if (result.length() > 0) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
}
