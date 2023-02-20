package com.CalculatorMVCUpload.aspect;

import com.CalculatorMVCUpload.entity.ActivityEntity;
import com.CalculatorMVCUpload.entity.PriceListEntity;
import com.CalculatorMVCUpload.entity.UploadedFile;
import com.CalculatorMVCUpload.entity.users.UserEntity;
import com.CalculatorMVCUpload.service.activity.ActivityService;
import com.CalculatorMVCUpload.service.users.UserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Aspect
public class LoggingAspect {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserService userService;

    private final String userTypeActivity = "Пользовательская активность";
    private final String updateFileTypeActivity = "Файл обновления";
    private final String priceListFileTypeActivity = "Прайс-лист";

    @AfterReturning("execution(* com.CalculatorMVCUpload.configuration.jwt.JwtProvider.generateAccessToken(..))"
            + "&&args(id)")
    public void loginUserAdvice(JoinPoint joinPoint, int id) {
        Instant timeStamp = Instant.now();
        ActivityEntity activityEntity = new ActivityEntity(timeStamp, userTypeActivity, id,
                "Пользователь залогинился");
        activityService.saveActivity(activityEntity);
    }

    @Before("execution(* org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler.logout(..))")
    public void logoutUserAdvice(JoinPoint joinPoint) {
        Instant timeStamp = Instant.now();
        String logoutName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userService.findByLogin(logoutName);

        ActivityEntity activityEntity = new ActivityEntity(timeStamp, userTypeActivity, userEntity.getId(),
                "Пользователь разлогинился");
        activityService.saveActivity(activityEntity);
    }

    @AfterReturning("execution(* com.CalculatorMVCUpload.service.users.UserService.saveUser(..))" + "&&args(userEntity,..)")
    public void saveNewUserAdvice(JoinPoint joinPoint, UserEntity userEntity) {
        Instant timeStamp = Instant.now();
        ActivityEntity activityEntity = new ActivityEntity(timeStamp, userTypeActivity, userEntity.getId(),
                "Зарегистрирован новый пользователь с почтой: " + userEntity.getEmail());
        activityService.saveActivity(activityEntity);
    }

    /*@AfterReturning("execution(* com.CalculatorMVCUpload.service.users.UserService.deleteUser(..))" + "&&args(id,..)")
    public void deleteUserAdvice(JoinPoint joinPoint, int id) {
        Instant timeStamp = Instant.now();
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        ActivityEntity activityEntity = new ActivityEntity(timeStamp, userTypeActivity, id,
                "Deleted user with id: " + id);
        activityService.saveActivity(activityEntity);
    }*/

    @AfterReturning("execution(* com.CalculatorMVCUpload.service.users.UserService.updateUser(..))" + "&&args(userEntity,..)")
    public void editUserAdvice(JoinPoint joinPoint, UserEntity userEntity) {
        Instant timeStamp = Instant.now();
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity authorEntity = userService.findByLogin(userName);
        int authorEntityId = 0;
        try {
            authorEntityId = authorEntity.getId();
        } catch (NullPointerException e) {
            authorEntityId = userEntity.getId();
        }
        ActivityEntity activityEntity = new ActivityEntity(timeStamp, userTypeActivity, authorEntityId,
                "Отредактирован пользователь: " + userEntity.getLogin());
        activityService.saveActivity(activityEntity);
    }

    @AfterReturning("execution(public * com.CalculatorMVCUpload.service.files.UploadFileService.addNewFile(..))" + "&&args(uploadedFile)")
    public void addNewFileAdvice(JoinPoint joinPoint, UploadedFile uploadedFile) {
        Instant timeStamp = Instant.now();
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        ActivityEntity activityEntity = new ActivityEntity(timeStamp, updateFileTypeActivity, uploadedFile.getAuthorId(),
                "Новый файл обновления загружен пользователем " + userName);
        activityService.saveActivity(activityEntity);
    }

    @AfterReturning("execution(public * com.CalculatorMVCUpload.service.files.UploadFileService.deleteFile(..))")
    public void deleteFileAdvice() {
        Instant timeStamp = Instant.now();
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userService.findByLogin(userName);
        ActivityEntity activityEntity = new ActivityEntity(timeStamp, updateFileTypeActivity, userEntity.getId(),
                "Файл обновления удален пользователем " + userName);
        activityService.saveActivity(activityEntity);
    }

    @AfterReturning("execution(public * com.CalculatorMVCUpload.service.files.PriceListUploadService.addNewFile(..))" + "&&args(priceListEntity)")
    public void addNewPriceListAdvice(JoinPoint joinPoint, PriceListEntity priceListEntity) {
        Instant timeStamp = Instant.now();
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        ActivityEntity activityEntity = new ActivityEntity(timeStamp, priceListFileTypeActivity, priceListEntity.getAuthorId(),
                "Новый прайс-лист загружен пользователем " + userName);
        activityService.saveActivity(activityEntity);
    }

    @AfterReturning("execution(public * com.CalculatorMVCUpload.service.files.PriceListUploadService.deleteFile(..))")
    public void deletePriceListFileAdvice() {
        Instant timeStamp = Instant.now();
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userService.findByLogin(userName);
        ActivityEntity activityEntity = new ActivityEntity(timeStamp, priceListFileTypeActivity, userEntity.getId(),
                "Прайс-лист удален пользователем " + userName);
        activityService.saveActivity(activityEntity);
    }

}
