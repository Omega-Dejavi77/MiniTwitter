package com.example.minitwitter.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestCreateTweet {

    @SerializedName("mensaje")
    @Expose
    private String message;

    public RequestCreateTweet() {}

    public RequestCreateTweet(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
