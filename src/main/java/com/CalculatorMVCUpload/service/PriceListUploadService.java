package com.CalculatorMVCUpload.service;

import com.CalculatorMVCUpload.entity.PriceListEntity;
import com.CalculatorMVCUpload.entity.UploadedFile;
import com.CalculatorMVCUpload.repository.PriceListEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PriceListUploadService {

    @Autowired
    private PriceListEntityRepository priceListEntityRepository;

    @Transactional
    public List<PriceListEntity> getAllFiles() {
        return priceListEntityRepository.findAll();
    }

    @Transactional
    public PriceListEntity getFileViaId(int id) {
        return priceListEntityRepository.getById(id);
    }

    @Transactional
    public void addNewFile(PriceListEntity priceListEntity) {
        priceListEntityRepository.saveAndFlush(priceListEntity);
    }

    @Transactional
    public void deleteFile(int id) {
        priceListEntityRepository.deleteById(id);
    }

    @Transactional
    public PriceListEntity getLastFile() {
        return priceListEntityRepository.findTopByOrderByIdDesc();
    }

    @Transactional
    public PriceListEntity getLastPriceListByForClients(String forClients) {
        return priceListEntityRepository.findTopByForClientsContainingOrderByIdDesc(forClients);
    }

    @Transactional
    public void editFileInfo(PriceListEntity priceListEntity) {
        priceListEntityRepository.save(priceListEntity);
    }
}
