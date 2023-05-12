package com.CalculatorMVCUpload.service;

import com.CalculatorMVCUpload.entity.ClaimEntity;
import com.CalculatorMVCUpload.payload.response.ClaimResponse;
import com.CalculatorMVCUpload.repository.ClaimEntityRepository;
import com.CalculatorMVCUpload.service.files.TxtWriterService;
import com.CalculatorMVCUpload.service.users.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Log
public class ClaimService {

    private final String CLAIM_MESSAGE_TEXT_REGEX = "Текст обращения: (.*)";
    @Autowired
    private ClaimEntityRepository claimEntityRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TxtWriterService txtWriterService;

    @Transactional
    public List<ClaimEntity> getAllClaims() {
        return claimEntityRepository.findAll();
    }

    @Transactional
    public List<ClaimEntity> getAllNotSolvedClaims() {
        return claimEntityRepository.selectNotSolvedClaims();
    }

    @Transactional
    public ClaimEntity getClaimViaId(int id) {
        return claimEntityRepository.getById(id);
    }

    @Transactional
    public void addNewClaim(ClaimEntity claimEntity) {
        claimEntityRepository.saveAndFlush(claimEntity);
    }

    @Transactional
    public void deleteFile(int id) {
        claimEntityRepository.deleteById(id);
    }

    public List<ClaimResponse> transferClaimEntitiesToClaimResponse(List<ClaimEntity> entityList) {
        List<ClaimResponse> claimResponses = new ArrayList<>();
        for (ClaimEntity claim : entityList) {
            ClaimResponse claimResponse = transferSingleClaimEntityToClaimResponse(claim);
            claimResponses.add(claimResponse);
        }
        return claimResponses;
    }

    public ClaimResponse transferSingleClaimEntityToClaimResponse(ClaimEntity claimEntity) {
        ClaimResponse claimResponse = new ClaimResponse();
        claimResponse.setTimeDate(claimEntity.getTimeDate());
        claimResponse.setUserId(claimEntity.getUserId());
        claimResponse.setUserName(userService.findById(claimResponse.getUserId()).getFullName());
        claimResponse.setType(claimEntity.getType());
        claimResponse.setName(claimEntity.getName());
        claimResponse.setResponsibleUserId(claimEntity.getResponsibleUserId());
        if (claimResponse.getResponsibleUserId() != 0) {
            claimResponse.setResponsibleUserName(userService.findById(claimResponse.getResponsibleUserId()).getFullName());
        }
        claimResponse.setIsSolved(claimEntity.getIsSolved());
        return claimResponse;
    }

    public String getClaimText(ClaimEntity claimEntity) {
        String messageFromFileText = null;
        Resource resource = txtWriterService.loadFileAsResource(claimEntity.getName());

        try (InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
             BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String fileInsideText = FileCopyUtils.copyToString(reader);
            messageFromFileText = getClaimMessageFromFileText(fileInsideText);
        } catch (IOException e) {
            log.severe("Cannot read claim file for claim with name = " + claimEntity.getName());
        }
        return messageFromFileText;
    }

    public String getClaimMessageFromFileText(String fileText) {
        Pattern pattern = Pattern.compile(CLAIM_MESSAGE_TEXT_REGEX);
        Matcher matcher = pattern.matcher(fileText);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }
}
