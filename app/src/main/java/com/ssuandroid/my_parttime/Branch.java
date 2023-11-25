package com.ssuandroid.my_parttime;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

// 지점 관련 클래스
public class Branch {
    // 지점 이름
    private String branchName;
    // 지점 참여 코드
    private String participationCode;

    @ServerTimestamp
    private Timestamp timestamp;
    public Branch(){
        //no-argument constructor
    }
    public Branch(String branchName, String participationCode) {
        this.branchName = branchName;
        this.participationCode = participationCode;
    }

    public String getbranchName() {
        return branchName;
    }

    public String getParticipationCode() {
        return participationCode;
    }
}
