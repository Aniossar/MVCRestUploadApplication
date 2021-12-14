package com.CalculatorMVCUpload.repository;

import com.CalculatorMVCUpload.entity.UploadedFile;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class UploadFileRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public List<UploadedFile> getAllFiles() {
        Session session = sessionFactory.getCurrentSession();
        Query<UploadedFile> query = session.createQuery("from UploadedFile", UploadedFile.class);
        List<UploadedFile> allUploadedFiles = query.getResultList();

        return allUploadedFiles;
    }

    public void addNewFile(UploadedFile uploadedFile) {
        Session session = sessionFactory.getCurrentSession();
        session.save(uploadedFile);
    }

    public UploadedFile getLastFile() {
        Session session = sessionFactory.getCurrentSession();
        UploadedFile lastUploadedFile = (UploadedFile)session.createSQLQuery("SELECT LAST_INSERT_ID()").uniqueResult();
        return lastUploadedFile;
    }

    public void deleteFile() {

    }
}
