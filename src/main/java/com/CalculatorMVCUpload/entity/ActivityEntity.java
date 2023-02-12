package com.CalculatorMVCUpload.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "activity_table")
@Getter
@Setter
@NoArgsConstructor
public class ActivityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "activity_time")
    private Instant activityTime;

    @Column(name = "activity_type")
    private String activityType;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "activity_message")
    private String activityMessage;

    public ActivityEntity(Instant activityTime, String activityType, int userId, String activityMessage) {
        this.activityTime = activityTime;
        this.activityType = activityType;
        this.userId = userId;
        this.activityMessage = activityMessage;
    }
}
