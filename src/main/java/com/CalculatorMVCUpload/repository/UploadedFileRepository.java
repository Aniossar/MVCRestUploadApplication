package com.CalculatorMVCUpload.repository;

import com.CalculatorMVCUpload.entity.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadedFileRepository extends JpaRepository<UploadedFile, Integer> {
    UploadedFile findTopByOrderByIdDesc();

}
