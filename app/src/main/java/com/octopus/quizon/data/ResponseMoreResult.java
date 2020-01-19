package com.octopus.quizon.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ResponseMoreResult {

    @SerializedName("score")
    @Expose
    private int mScore;

    @SerializedName("name")
    @Expose
    private String mName;

    @SerializedName("info")
    @Expose
    private String mInfo;


    public int getScore() {
        return mScore;
    }

    public String getName() {
        return mName;
    }

    public String getInfo() {
        return mInfo;
    }
}
