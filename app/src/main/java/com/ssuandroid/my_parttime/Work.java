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
    private float workTime;

    @ServerTimestamp
    private Timestamp timestamp;

    public Work( long userId, String participationCode, Date date, float workTime) {
        this.userId = userId;
        this.participationCode = participationCode;
        this.date = date;
        this.workTime = workTime;
    }

    public long getUserId() {
        return userId;
    }

    public String getParticipationCode() {
        return participationCode;
    }

    public float getWorkTime() {
        return workTime;
    }

    public Date getDate() {
        return date;
    }
}
