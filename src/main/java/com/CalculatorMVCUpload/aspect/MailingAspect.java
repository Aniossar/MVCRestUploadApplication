package com.CalculatorMVCUpload.aspect;

import com.CalculatorMVCUpload.entity.EmailContext;
import com.CalculatorMVCUpload.entity.users.UserEntity;
import com.CalculatorMVCUpload.payload.request.users.RegistrationRequest;
import com.CalculatorMVCUpload.service.EmailService;
import lombok.extern.java.Log;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

@Component
@Aspect
@Log
public class MailingAspect {

    @Autowired
    private EmailService emailService;

    @AfterReturning("execution(* com.CalculatorMVCUpload.controller.AuthController.registerUser(..))"
            + "&&args(registrationRequest)")
    public void registerUserAdvice(JoinPoint joinPoint, RegistrationRequest registrationRequest) {
        try {
            EmailContext emailContext = new EmailContext();
            emailContext.setFrom("noreply@koreanika.ru");
            emailContext.setSubject("Добро пожаловать на портал KOREANIKA");
            emailContext.setTo(registrationRequest.getEmail());
            emailContext.setTemplateLocation("greetingLetter");
            Map<String, Object> context = new HashMap<>();
            context.put("email", registrationRequest.getEmail());
            emailContext.setContext(context);
            emailService.sendMail(emailContext);
            log.info("Sent welcome letter to email " + registrationRequest.getEmail());
        } catch (Exception e) {
            log.severe("Error while sending out email — " + e.getLocalizedMessage());
        }
    }

    @AfterReturning("execution(* com.CalculatorMVCUpload.service.users.UserManagementService.editUserFields(..))"
            + "&&args(userEntity)")
    public void editUserAdvice(JoinPoint joinPoint, UserEntity userEntity) {
        try {
            EmailContext emailContext = new EmailContext();
            emailContext.setFrom("noreply@koreanika.ru");
            emailContext.setSubject("Уведомление о изменении данных на портале");
            emailContext.setTo(userEntity.getEmail());
            emailContext.setTemplateLocation("notificationUserLetter");
            emailService.sendMail(emailContext);
            log.info("Sent notification user letter to email " + userEntity.getEmail());
        } catch (Exception e) {
            log.severe("Error while sending out email — " + e.getLocalizedMessage());
        }
    }
}
