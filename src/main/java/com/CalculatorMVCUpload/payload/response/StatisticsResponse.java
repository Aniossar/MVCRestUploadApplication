package com.CalculatorMVCUpload.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsResponse {

    private String type;

    private String startDateTime;

    private String endDateTime;

    private List<Integer> usersOnline;

    private List<Integer> newReceipts;

    private List<Integer> newUsers;
}
