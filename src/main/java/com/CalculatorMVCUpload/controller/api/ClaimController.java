package com.CalculatorMVCUpload.controller.api;

import com.CalculatorMVCUpload.configuration.jwt.JwtProvider;
import com.CalculatorMVCUpload.entity.ClaimEntity;
import com.CalculatorMVCUpload.exception.FileNotFoundException;
import com.CalculatorMVCUpload.payload.request.ClaimRequest;
import com.CalculatorMVCUpload.payload.request.SingleIdRequest;
import com.CalculatorMVCUpload.payload.response.ClaimResponse;
import com.CalculatorMVCUpload.payload.response.UploadFileResponse;
import com.CalculatorMVCUpload.service.ClaimService;
import com.CalculatorMVCUpload.service.files.TxtWriterService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;

@RestController
@Log
@RequestMapping("/api/claims")
public class ClaimController {

    @Autowired
    private ClaimService claimService;

    @Autowired
    private TxtWriterService txtWriterService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private FileController fileController;

    @CrossOrigin
    @GetMapping("/allClaims")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public List<ClaimResponse> getAllClaims() {
        List<ClaimEntity> allClaims = claimService.getAllClaims();
        List<ClaimResponse> claimResponses = claimService.transferClaimEntitiesToClaimResponse(allClaims);
        return claimResponses;
    }

    @CrossOrigin
    @GetMapping("/allNotSolvedClaims")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public List<ClaimResponse> getAllNotSolvedClaims() {
        List<ClaimEntity> notSolvedClaims = claimService.getAllNotSolvedClaims();
        List<ClaimResponse> claimResponses = claimService.transferClaimEntitiesToClaimResponse(notSolvedClaims);
        return claimResponses;
    }

    @CrossOrigin
    @GetMapping("/getClaim/{id}")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public ClaimResponse getClaimViaId(@PathVariable int id) {
        ClaimEntity claim = claimService.getClaimViaId(id);
        ClaimResponse claimResponse = claimService.transferSingleClaimEntityToClaimResponse(claim);
        claimResponse.setClaimText(claimService.getClaimText(claim));
        return claimResponse;
    }

    @CrossOrigin
    @PutMapping("/setResponsibleForClaim/{id}")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public void setResponsibleForClaim(@PathVariable int id,
                                       @RequestBody SingleIdRequest request) {
        ClaimEntity claim = claimService.getClaimViaId(id);
        if (request.getSomeId() != 0 && claim != null) {
            claim.setResponsibleUserId(request.getSomeId());
        }
        claimService.addNewClaim(claim);
    }

    @CrossOrigin
    @GetMapping("/markClaimAsSolved/{id}")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public void markClaimAsSolved(@PathVariable int id) {
        ClaimEntity claim = claimService.getClaimViaId(id);
        if (claim != null) {
            claim.setIsSolved(true);
        }
        claimService.addNewClaim(claim);
    }

    @CrossOrigin
    @PostMapping("/saveClaim")
    public UploadFileResponse saveClaim(@RequestBody ClaimRequest request,
                                        @RequestHeader(name = "Authorization") String bearer) {
        String token = jwtProvider.getTokenFromBearer(bearer);
        int userId = jwtProvider.getIdFromAccessToken(token);

        ClaimEntity claim = new ClaimEntity();
        claim.setUserId(userId);
        claim.setTimeDate(Instant.now());
        claim.setType(request.getClaimType().toString());
        claim.setIsSolved(false);

        String fileName = txtWriterService.saveTxtReportWithClaim(claim.getType(), claim.getTimeDate(),
                request.getClaimText(), userId);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/claims/downloadFile/")
                .path(fileName)
                .toUriString();

        claim.setName(fileName);
        claim.setUrl(fileDownloadUri);
        claim.setPath(txtWriterService.getFileStorageLocation().toAbsolutePath().normalize() + "\\" + fileName);
        claimService.addNewClaim(claim);

        return new UploadFileResponse(fileName, fileDownloadUri, null, 0);
    }

    @CrossOrigin
    @GetMapping("/downloadClaim/{fileName:.+}")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public ResponseEntity<Resource> downloadClaimController(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = txtWriterService.loadFileAsResource(fileName);
        return fileController.downloadFile(resource, request);
    }

    @CrossOrigin
    @DeleteMapping("/deleteClaim/{id}")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public void deleteClaim(@PathVariable int id) {
        ClaimEntity claim = claimService.getClaimViaId(id);
        Resource resource = txtWriterService.loadFileAsResource(claim.getName());
        try {
            boolean deleteResult = Files.deleteIfExists(Paths.get(resource.getFile().getAbsolutePath()));
            if (deleteResult) {
                claimService.deleteFile(id);
            }
        } catch (Exception e) {
            throw new FileNotFoundException("File not found " + claim.getName());
        }
    }


}
