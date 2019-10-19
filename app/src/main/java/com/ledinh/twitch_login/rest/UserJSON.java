package com.ledinh.twitch_login.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserJSON {

    @SerializedName("data")
    @Expose
    private List<UserJSONDatum> data = null;

    public List<UserJSONDatum> getData() {
        return data;
    }

    public void setData(List<UserJSONDatum> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "UserJSON{" +
                "data=" + data +
                '}';
    }
}