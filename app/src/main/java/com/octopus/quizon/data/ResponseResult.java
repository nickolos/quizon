package com.octopus.quizon.data;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ResponseResult {

    @SerializedName("place")
    @Expose
    private int mPlace;
    @SerializedName("score")
    @Expose
    private int mScore;
    @SerializedName("name")
    @Expose
    private String mName;

    public int getPlace() {
        return mPlace;
    }

    public int getScore() {
        return mScore;
    }

    public String getName() {
        return mName;
    }
}
