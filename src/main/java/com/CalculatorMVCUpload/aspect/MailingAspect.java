package com.CalculatorMVCUpload.aspect;

import com.CalculatorMVCUpload.entity.EmailContext;
import com.CalculatorMVCUpload.payload.request.RegistrationRequest;
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
    public void loginUserAdvice(JoinPoint joinPoint, RegistrationRequest registrationRequest) {
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
            log.info("Sended welcome letter to email " + registrationRequest.getEmail());
        } catch (MessagingException e) {
            log.severe("Error while sending out email — " + e.getLocalizedMessage());
        }
    }
}
