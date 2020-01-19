package com.octopus.quizon.data;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ResponseGameId {

    @SerializedName("id")
    @Expose
    private Long mId;

    @SerializedName("timeArrive")
    @Expose
    private Long mWaitingTime;

    public ResponseGameId(Long id, Long time)
    {
        mId = id;
        mWaitingTime = time;
    }

    public Long getId() {
        return mId;
    }

    public Long getWaitingTime() {
        return mWaitingTime;
    }

}
