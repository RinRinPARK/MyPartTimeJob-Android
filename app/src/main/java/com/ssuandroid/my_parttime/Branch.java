package com.ssuandroid.my_parttime;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

// 지점 관련 클래스
public class Branch {
    // 지점 이름
    private String name;
    // 지점 참여 코드
    private String participationCode;

    @ServerTimestamp
    private Timestamp timestamp;

    public Branch(String name, String participationCode) {
        this.name = name;
        this.participationCode = participationCode;
    }

    public String getName() {
        return name;
    }

    public String getParticipationCode() {
        return participationCode;
    }
}
