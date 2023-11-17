package com.ssuandroid.my_parttime;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

public class User {

    // 유저의 id
    private long id;

    @ServerTimestamp
    private Timestamp timestamp;

    public User(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }
}
