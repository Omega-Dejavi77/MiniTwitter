package com.example.minitwitter.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseAuth {

    @SerializedName("token")
    @Expose
    private String toke;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("photoUrl")
    @Expose
    private String photoUrl;

    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("active")
    @Expose
    private boolean active;

    public String getToke() {
        return toke;
    }

    public void setToke(String toke) {
        this.toke = toke;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
