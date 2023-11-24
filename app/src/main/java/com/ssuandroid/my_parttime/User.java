package com.ssuandroid.my_parttime;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

public class User {

    // 유저의 id
    private String id;

    // 유저의 이름
    private String name;

    @ServerTimestamp
    private Timestamp timestamp;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }
}
