package com.CalculatorMVCUpload.controller.api;

import com.CalculatorMVCUpload.configuration.jwt.JwtProvider;
import com.CalculatorMVCUpload.entity.users.OnlineUserEntity;
import com.CalculatorMVCUpload.service.users.OnlineUserService;
import com.CalculatorMVCUpload.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class OnlineUserCheckController {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private OnlineUserService onlineUserService;

    private final long activityPeriodInMinutes = 15;


    @GetMapping("/pingAlive")
    public void pingAliveUser(@RequestHeader(name = "Authorization") String bearer) {
        String token = jwtProvider.getTokenFromBearer(bearer);
        int userId = jwtProvider.getIdFromAccessToken(token);
        if (userId != 0) {
            OnlineUserEntity userEntity = onlineUserService.getUserViaId(userId);
            Instant timePing = Instant.now();
            if (userEntity != null) {
                userEntity.setLastPingTime(timePing);
            } else {
                userEntity = new OnlineUserEntity();
                userEntity.setUserId(userId);
                userEntity.setLastPingTime(timePing);
            }
            onlineUserService.saveUserLine(userEntity);
        }
    }

    @GetMapping("/showUserStats")
    public List<OnlineUserEntity> getAllUserOnlineStats(){
        List<OnlineUserEntity> allUsers = onlineUserService.getAllUsers();
        Instant now = Instant.now();
        List<OnlineUserEntity> allOnlineUsers = allUsers.stream()
                .filter(item -> item.getLastPingTime().plus(activityPeriodInMinutes, ChronoUnit.MINUTES).isAfter(now))
                .collect(Collectors.toList());
        return allOnlineUsers;
    }
}