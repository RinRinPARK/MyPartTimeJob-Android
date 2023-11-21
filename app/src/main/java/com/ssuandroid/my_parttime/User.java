package com.ssuandroid.my_parttime;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

public class User {

    // 유저의 id
    private long id;

    // 유저의 이름
    private String name;

    @ServerTimestamp
    private Timestamp timestamp;

    public User(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }
}
