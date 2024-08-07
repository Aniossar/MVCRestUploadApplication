package com.CalculatorMVCUpload.controller;

import com.CalculatorMVCUpload.configuration.CustomUserDetailsService;
import com.CalculatorMVCUpload.configuration.jwt.JwtProvider;
import com.CalculatorMVCUpload.entity.EmailContext;
import com.CalculatorMVCUpload.entity.users.UserEntity;
import com.CalculatorMVCUpload.exception.BadAuthException;
import com.CalculatorMVCUpload.exception.ExistingLoginEmailRegisterException;
import com.CalculatorMVCUpload.exception.WrongPasswordUserMovesException;
import com.CalculatorMVCUpload.payload.request.SingleMessageRequest;
import com.CalculatorMVCUpload.payload.request.users.AuthentificationRequest;
import com.CalculatorMVCUpload.payload.request.users.PasswordResetRequest;
import com.CalculatorMVCUpload.payload.request.users.RefreshTokenRequest;
import com.CalculatorMVCUpload.payload.request.users.RegistrationRequest;
import com.CalculatorMVCUpload.payload.response.AuthentificationResponse;
import com.CalculatorMVCUpload.payload.response.MeResponse;
import com.CalculatorMVCUpload.service.EmailService;
import com.CalculatorMVCUpload.service.users.AuthService;
import com.CalculatorMVCUpload.service.users.UserService;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.util.StringUtils.hasText;


@RestController
@NoArgsConstructor
@Log
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtProvider jwtProvider;

    @Value("${server.actual.address}")
    private String productionContextPath;

    @CrossOrigin
    @PostMapping("/register")
    public String registerUser(@RequestBody @Valid RegistrationRequest registrationRequest) {
        if (userService.findByLogin(registrationRequest.getLogin()) != null
                || userService.findByEmail(registrationRequest.getEmail()) != null
                || userService.findByLogin(registrationRequest.getEmail()) != null
                || userService.findByEmail(registrationRequest.getLogin()) != null) {
            log.warning("Trying to register user with existing email or login");
            throw new ExistingLoginEmailRegisterException("This login or email is already registered");
        }
        UserEntity userEntity = userService.formNewUser(registrationRequest);
        userService.saveUser(userEntity, registrationRequest.getDesiredRole());
        return "OK";
    }

    @CrossOrigin
    @PostMapping("/auth")
    public ResponseEntity<AuthentificationResponse> auth(@RequestBody AuthentificationRequest request) {
        AuthentificationResponse authResponse = authService.authenticate(request);
        return ResponseEntity.ok(authResponse);
    }

    @CrossOrigin
    @PostMapping("/token")
    public ResponseEntity<AuthentificationResponse> getNewAccessToken(@RequestBody RefreshTokenRequest request) {
        AuthentificationResponse authResponse = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(authResponse);
    }

    @CrossOrigin
    @GetMapping("/me")
    public MeResponse checkUserAuth(@RequestHeader(name = "Authorization") String bearer) {
        String token = null;
        if (hasText(bearer) && bearer.startsWith("Bearer ")) {
            token = bearer.substring(7);
        }
        if (token != null && jwtProvider.validateAccessToken(token)) {
            String userLogin = jwtProvider.getLoginFromAccessToken(token);
            int userId = jwtProvider.getIdFromAccessToken(token);
            String roleFromToken = jwtProvider.getRoleFromAccessToken(token);
            UserEntity userEntity = userService.findById(userId);
            String appAccess = userEntity.getAppAccess();
            return new MeResponse(userId, userLogin, roleFromToken, appAccess);
        } else throw new BadAuthException("No user is authorized");
    }

    @CrossOrigin
    @PostMapping("/forgottenPassword")
    public String situationWithTheForgottenPassword(@RequestBody SingleMessageRequest request) {
        String usersMail = request.getMessage();
        UserEntity userEntity = userService.findByEmail(usersMail);
        if (userEntity != null) {
            String restoringToken = authService.generateRestoringPasswordToken(userEntity.getId());
            String restorePasswordUrl = productionContextPath + "/changePassword?token=" + restoringToken;
            try {
                EmailContext emailContext = new EmailContext();
                emailContext.setFrom("noreply@koreanika.ru");
                emailContext.setSubject("Восстановление пароля на портале KOREANIKA");
                emailContext.setTo(usersMail);
                emailContext.setTemplateLocation("passwordRestoreLetter");
                Map<String, Object> context = new HashMap<>();
                context.put("restorePwLink", restorePasswordUrl);
                emailContext.setContext(context);
                emailService.sendMail(emailContext);
                log.info("Sent restoring letter to email " + usersMail);
                return "OK";
            } catch (MessagingException e) {
                log.severe("Error while sending out email — " + e.getLocalizedMessage());
            }
        } else {
            throw new BadAuthException("User mail not found");
        }
        return "NOK";
    }

    @CrossOrigin
    @PutMapping("/resetPassword")
    public void resetPassword(@RequestBody PasswordResetRequest request) {
        int idFromRestoreToken = authService.getIdFromRestoreToken(request.getRestoreToken());
        if (idFromRestoreToken != -1) {
            UserEntity userEntity = userService.findById(idFromRestoreToken);
            if (userEntity != null) {
                userEntity.setPassword(request.getNewPassword());
                userService.updateUserPassword(userEntity);
            } else throw new WrongPasswordUserMovesException("Wrong old password");
        } else throw new BadAuthException("Need authorization");
    }

}