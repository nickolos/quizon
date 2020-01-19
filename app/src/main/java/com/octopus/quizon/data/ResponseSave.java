package com.octopus.quizon.data;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ResponseSave {

    @SerializedName("saved")
    @Expose
    private final String saved = "null";

    public String getAnswer()
    {
        return saved;
    }
}