package com.CalculatorMVCUpload.service;

import com.CalculatorMVCUpload.controller.api.OnlineUserCheckController;
import com.CalculatorMVCUpload.entity.StatisticsEntity;
import com.CalculatorMVCUpload.repository.StatisticsEntityRepository;
import com.CalculatorMVCUpload.service.activity.CalculatorActivityService;
import com.CalculatorMVCUpload.service.users.OnlineUserService;
import com.CalculatorMVCUpload.service.users.UserService;
import lombok.Getter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@EnableScheduling
@Log
@Getter
public class StatisticsService {

    @Autowired
    private StatisticsEntityRepository statisticsEntityRepository;

    @Autowired
    private OnlineUserService onlineUserService;

    @Autowired
    private CalculatorActivityService calculatorActivityService;

    @Autowired
    private UserService userService;

    @Autowired
    private OnlineUserCheckController onlineUserCheckController;

    private final long statisticSlicePeriodInMinutes = 60;

    @Transactional
    public void saveStatisticSlice(StatisticsEntity statisticsEntity) {
        statisticsEntityRepository.saveAndFlush(statisticsEntity);
    }

    @Scheduled(fixedDelay = 3600000L)
    public void makeStatisticSlice() {
        log.info("Time slice cut!");
        int onlineUsersCounter = onlineUserCheckController.getAllUserOnlineStats().size();

        Instant timeNow = Instant.now();
        Instant startTime = timeNow.minus(statisticSlicePeriodInMinutes, ChronoUnit.MINUTES);
        int showReceiptsCounter = calculatorActivityService
                .getActivitiesByTypeAndTime(startTime, timeNow, "show receipt").size();
        int registeredUsersCounter = userService.getNewRegisteredUsersByTime(startTime, timeNow).size();

        StatisticsEntity statisticsEntity = new StatisticsEntity();
        statisticsEntity.setTimeSlice(timeNow);
        statisticsEntity.setUsersOnline(onlineUsersCounter);
        statisticsEntity.setNewReceipts(showReceiptsCounter);
        statisticsEntity.setNewUsers(registeredUsersCounter);

        saveStatisticSlice(statisticsEntity);
    }
}

