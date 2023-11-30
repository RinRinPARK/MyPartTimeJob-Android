package com.ssuandroid.my_parttime;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

// 대타 관련 클래스
public class Daeta  {
    //Daeta 객체를 만들어 db에 넣을 때 id 형식: "participationCode+" "+Date타입 date+" "+time
    //예시: PARME456 Mon Nov 20 17:00:00 GMT+09:00 2023 13:00 - 15:30
    // 참여코드, 지점 코드
    private String participationCode;
    //branchName: db에 여러번 접근하는 것을 막기 위해 처음에 대타 리스트를 띄울 때 participationCode와 일치하는 branchName을 찾아 setBranchName하도록 함.
    private String branchName;
    //대타 등록한 사람의 해당 알바에서의 시급 : 이 시급으로 대타가 벌게 될 돈을 계산: wage * 일하는 시간
    private long wage;
    // 대타 구하는 날짜
    private Date date;
    // 대타 구하는 시간 (ex: "13:00 - 15:00" 이런 양식으로 넣어야 함.)
    private String time;
    // 대타 구하기 설명
    private String description;
    // 대타 구인하는 작성자 id
    // 대타 구인하는 작성자 id로부터 wage를 얻어와
    private String writerId;
    // 대타 신청한 대타 id, 구인 중일 때는 null -> 구인 완료 시 대타의 id
    private String applicantId;
    // 외부 구인 여부
    private boolean externalTF;
    private boolean covered;

    @ServerTimestamp
    private Timestamp timestamp;

    public Daeta(){
        //no-argument constructor 필요
    }

    public Daeta(String participationCode, long wage , Date date, String time, String description, String writerId, String applicantId, boolean externalTF) {
        this.participationCode = participationCode;
        this.wage= wage;
        this.date = date;
        this.time = time;
        this.description = description;
        this.writerId = writerId;
        this.applicantId = applicantId;
        this.externalTF = externalTF;
        this.covered= covered; //초기엔 false로
    }

    public String getParticipationCode() {
        return participationCode;
    }

    public String getBranchName() {
        return branchName;
    }

    public long getWage() { return wage;}

    public Date getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getWriterId() {
        return writerId;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public boolean getExternalTF() {
        return externalTF;
    }
    public boolean getCovered() {return covered;}

    public void setBranchName(String branchName) {
        this.branchName = branchName; //초기엔 설정할 필요 없는 부분, 대타 공고를 받아오며 자동으로 set되도록 만듦
    }
    public void setCovered(){
        this.covered=true;
    }
    public void cancelSetCovered() {this.covered=false;}
    public void setApplicantId(String applicantId){
        this.applicantId = applicantId;
    }
    public void cancelSetApplicantId() {this.applicantId = null;}
}
