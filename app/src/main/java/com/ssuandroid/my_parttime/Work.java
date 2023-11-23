package com.ssuandroid.my_parttime;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

// 사용자의 근무 현황 관련 클래스
public class Work {
    // 유저 id
    private long userId;
    // 지점 참여 코드
    private String participationCode;
    // 일한 날짜
    private Date date;
    // 일한 시간 (ex: 3.5, 4 .. etc)
    private double workTime;

    private String branchName;

    private long wage;

    @ServerTimestamp
    private Timestamp timestamp;

    public Work(){
        //no-argument constructor 필요
    }

    public Work( long userId, String participationCode, Date date, double workTime, String branchName, long wage) {
        this.userId = userId;
        this.participationCode = participationCode;
        this.date = date;
        this.workTime = workTime;
        this.branchName = branchName;
        this.wage = wage;
    }

    public long getUserId() {
        return userId;
    }

    public String getParticipationCode() {
        return participationCode;
    }

    public double getWorkTime() {
        return workTime;
    }

    public Date getDate() {
        return date;
    }

    public String getBranchName() {
        return branchName;
    }

    public long getWage() {
        return wage;
    }
}
