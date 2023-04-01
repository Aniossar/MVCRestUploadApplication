package com.CalculatorMVCUpload.controller.api;

import com.CalculatorMVCUpload.configuration.SystemParametersConfig;
import com.CalculatorMVCUpload.entity.StatisticsEntity;
import com.CalculatorMVCUpload.payload.request.StatisticsRequest;
import com.CalculatorMVCUpload.payload.response.StatisticsResponse;
import com.CalculatorMVCUpload.repository.StatisticsEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
public class SystemStatisticsController {

    @Autowired
    private SystemParametersConfig systemParametersConfig;

    @Autowired
    private StatisticsEntityRepository statisticsEntityRepository;

    private final String patternFormat = "dd.MM.yyyy hh:mm:ss";

    @CrossOrigin
    @GetMapping("/getApplicationStart")
    public String getApplicationStart() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(patternFormat).withZone(ZoneId.systemDefault());
        String formattedApplicationStart = formatter.format(systemParametersConfig.getApplicationStarted());
        return formattedApplicationStart;
    }

    @CrossOrigin
    @GetMapping("/getApplicationWorkingTime")
    public String getApplicationWorkingTime() {
        Instant timeNow = Instant.now();
        Instant applicationStartTime = systemParametersConfig.getApplicationStarted();
        long secondPassed = timeNow.getEpochSecond() - applicationStartTime.getEpochSecond();
        long days = secondPassed / 86400;
        long hours = secondPassed / 3600 % 24;
        long minutes = secondPassed / 60 % 60;
        long seconds = secondPassed - days * 86400 - hours * 3600 - minutes * 60;
        String dayStr = (days != 0) ? days + " дней " : "";
        String hourStr = (hours != 0) ? hours + " часов " : "";
        return dayStr + hourStr + minutes + " минут " + seconds + " секунд";
    }

    //returns used memory by heap / max memory available for jvm
    @CrossOrigin
    @GetMapping("/getMemoryInfo")
    public String getMemoryInfo() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        double usedMemoryMb = (double) memoryMXBean.getHeapMemoryUsage().getUsed() / 1048576;
        double maxMemoryMb = (double) memoryMXBean.getHeapMemoryUsage().getMax() / 1048576;
        return String.format("%.2f MB / %.2f MB", usedMemoryMb, maxMemoryMb);
    }

    //returns free space available for using / total space
    @CrossOrigin
    @GetMapping("/getHDDSpace")
    public String getHDDSpace() {
        String volume = "/";
        File file = new File(volume);
        double totalSpaceMb = (double) file.getTotalSpace() / 1048576;
        double usableSpaceMb = (double) file.getUsableSpace() / 1048576;
        return String.format("%.2f MB / %.2f MB", usableSpaceMb, totalSpaceMb);
    }

    @CrossOrigin
    @PostMapping("/getUserStatistics")
    public StatisticsResponse getUserStatistics(@RequestBody StatisticsRequest statisticsRequest) {

        Instant startDateInstant = Instant.parse(statisticsRequest.getStartDateTime());
        Instant endDateInstant = Instant.parse(statisticsRequest.getEndDateTime());
        StatisticsResponse statisticsResponse = new StatisticsResponse();
        statisticsResponse.setType(statisticsRequest.getType());
        statisticsResponse.setStartDateTime(statisticsRequest.getStartDateTime());
        statisticsResponse.setEndDateTime(statisticsRequest.getEndDateTime());

        List<StatisticsEntity> statisticsEntityList = statisticsEntityRepository
                .selectByPeriodOfTime(startDateInstant, endDateInstant);

        int tPeriod = statisticsEntityList.size() / statisticsRequest.getPrecession();
        int newReceiptsCounter = 0;
        int usersOnlineCounter = 0;
        int newUsersCounter = 0;
        List<Integer> newReceiptsArray = new ArrayList<>();
        List<Integer> usersOnlineArray = new ArrayList<>();
        List<Integer> newUsersArray = new ArrayList<>();

        for (int i = 0; i < statisticsEntityList.size(); i++) {
            if ((i + 1) % tPeriod != 0) {
                newReceiptsCounter += statisticsEntityList.get(i).getNewReceipts();
                usersOnlineCounter += statisticsEntityList.get(i).getUsersOnline();
                newUsersCounter += statisticsEntityList.get(i).getNewUsers();
            } else {
                newReceiptsArray.add((int) Math.ceil((float) newReceiptsCounter / tPeriod));
                usersOnlineArray.add((int) Math.ceil((float) usersOnlineCounter / tPeriod));
                newUsersArray.add((int) Math.ceil((float) newUsersCounter / tPeriod));
                newReceiptsCounter = 0;
                usersOnlineCounter = 0;
                newUsersCounter = 0;
            }
        }

        Pattern pattern = Pattern.compile(",");
        String typeStrings[] = pattern.split(statisticsRequest.getType());

        for (String typeSingle : typeStrings) {
            if (typeSingle.equals("onlineUsers")) statisticsResponse.setUsersOnline(usersOnlineArray);
            else if (typeSingle.equals("newUsers")) statisticsResponse.setNewUsers(newUsersArray);
            else if (typeSingle.equals("receipts")) statisticsResponse.setNewReceipts(newReceiptsArray);
        }

        return statisticsResponse;
    }

}
