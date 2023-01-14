package com.CalculatorMVCUpload.aspect;

import com.CalculatorMVCUpload.entity.ActivityEntity;
import com.CalculatorMVCUpload.entity.UserEntity;
import com.CalculatorMVCUpload.service.ActivityService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Aspect
public class LoggingAspect {

    @Autowired
    private ActivityService activityService;

    private final String userTypeActivity = "User Activity";
    private final String updateFileTypeActivity = "Update file";
    private final String priceListFileTypeActivity = "Price list file";

    @AfterReturning("execution(* com.CalculatorMVCUpload.configuration.jwt.JwtProvider.generateAccessToken(String))"
            + "&&args(loginString)")
    public void loginUserAdvice(JoinPoint joinPoint, String loginString) {
        Instant timeStamp = Instant.now();
        ActivityEntity activityEntity = new ActivityEntity(timeStamp, userTypeActivity, loginString,
                "User logins into system");
        activityService.saveActivity(activityEntity);
    }

    @Before("execution(* org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler.logout(..))")
    public void logoutUserAdvice(JoinPoint joinPoint) {
        Instant timeStamp = Instant.now();
        String logoutName = SecurityContextHolder.getContext().getAuthentication().getName();
        ActivityEntity activityEntity = new ActivityEntity(timeStamp, userTypeActivity, logoutName,
                "User logout from system");
        activityService.saveActivity(activityEntity);
    }

    @AfterReturning("execution(* com.CalculatorMVCUpload.service.users.UserService.saveUser(..))" + "&&args(userEntity,..)")
    public void saveNewUserAdvice(JoinPoint joinPoint, UserEntity userEntity) {
        Instant timeStamp = Instant.now();
        ActivityEntity activityEntity = new ActivityEntity(timeStamp, userTypeActivity, userEntity.getLogin(),
                "New user registered with email: " + userEntity.getEmail());
        activityService.saveActivity(activityEntity);
    }

    @AfterReturning("execution(* com.CalculatorMVCUpload.service.users.UserService.deleteUser(..))" + "&&args(login,..)")
    public void deleteUserAdvice(JoinPoint joinPoint, String login) {
        Instant timeStamp = Instant.now();
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        ActivityEntity activityEntity = new ActivityEntity(timeStamp, userTypeActivity, userName,
                "Deleted user: " + login);
        activityService.saveActivity(activityEntity);
    }

    @AfterReturning("execution(public * com.CalculatorMVCUpload.service.files.UploadFileService.addNewFile(..))")
    public void addNewFileAdvice() {
        Instant timeStamp = Instant.now();
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        ActivityEntity activityEntity = new ActivityEntity(timeStamp, updateFileTypeActivity, userName,
                "New update file uploaded by " + userName);
        activityService.saveActivity(activityEntity);
    }

    @AfterReturning("execution(public * com.CalculatorMVCUpload.service.files.UploadFileService.deleteFile(..))")
    public void deleteFileAdvice() {
        Instant timeStamp = Instant.now();
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        ActivityEntity activityEntity = new ActivityEntity(timeStamp, updateFileTypeActivity, userName,
                "Update file deleted by " + userName);
        activityService.saveActivity(activityEntity);
    }

    @AfterReturning("execution(public * com.CalculatorMVCUpload.service.files.PriceListUploadService.addNewFile(..))")
    public void addNewPriceListAdvice() {
        Instant timeStamp = Instant.now();
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        ActivityEntity activityEntity = new ActivityEntity(timeStamp, priceListFileTypeActivity, userName,
                "New price list file uploaded by " + userName);
        activityService.saveActivity(activityEntity);
    }

    @AfterReturning("execution(public * com.CalculatorMVCUpload.service.files.PriceListUploadService.deleteFile(..))")
    public void deletePriceListFileAdvice() {
        Instant timeStamp = Instant.now();
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        ActivityEntity activityEntity = new ActivityEntity(timeStamp, priceListFileTypeActivity, userName,
                "Price list file deleted by " + userName);
        activityService.saveActivity(activityEntity);
    }

}
