package com.ssuandroid.my_parttime;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;

// 사용자의 알바 목록 관련 클래스
public class Alba implements Serializable {
    //Serializable 추가: Alba 객체를 bundle로 건네기 위함
    // 유저 id
    private String userId;

    // 지점 이름
    private String branchName;
    // 지점 참여 코드
    private String participationCode;
    // 시급
    private long wage;

    @ServerTimestamp
    private Timestamp timestamp;

    public Alba(String userId, String branchName, String participationCode, long wage) {
        this.userId =userId;
        this.branchName = branchName;
        this.participationCode = participationCode;
        this.wage = wage;
    }

    public Alba(){
        //no-argument constructor missing 에러 해결
    }

    public String getUserId() {
        return userId;
    }

    public long getWage() {
        return wage;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getParticipationCode() {
        return participationCode;
    }
}
