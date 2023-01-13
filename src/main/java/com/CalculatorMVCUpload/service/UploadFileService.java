package com.CalculatorMVCUpload.service;

import com.CalculatorMVCUpload.entity.UploadedFile;
import com.CalculatorMVCUpload.repository.UploadedFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        return uploadedFileRepository.findTopByForClientsOrderByIdDesc(forClients);
    }

    @Transactional
    public void editFileInfo(UploadedFile uploadedFile) {
        uploadedFileRepository.save(uploadedFile);
    }

}
