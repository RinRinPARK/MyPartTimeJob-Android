package com.ssuandroid.my_parttime;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

// 대타 관련 클래스
public class Daeta {
    
    // 참여코드, 지점 코드
    private String participationCode;
    // 대타 구하는 날짜
    private Date date;
    // 대타 구하는 시간 (ex: "13:00 - 15:00" 이런 양식으로 넣어야 함.)
    private String time;
    // 대타 구하기 설명
    private String description;
    // 대타 구인하는 작성자 id
    private long writerId;
    // 대타 신청한 대타 id, 구인 중일 때는 null -> 구인 완료 시 대타의 id
    private long applicantId;
    // 외부 구인 여부
    private boolean externalTF;

    @ServerTimestamp
    private Timestamp timestamp;

    public Daeta(String participationCode, Date date, String time, String description, long writerId, long applicantId, boolean externalTF) {
        this.participationCode = participationCode;
        this.date = date;
        this.time = time;
        this.description = description;
        this.writerId = writerId;
        this.applicantId = applicantId;
        this.externalTF = externalTF;
    }

    public String getParticipationCode() {
        return participationCode;
    }

    public Date getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public long getWriterId() {
        return writerId;
    }

    public long getApplicantId() {
        return applicantId;
    }

    public boolean getExternalTF() {
        return externalTF;
    }
}