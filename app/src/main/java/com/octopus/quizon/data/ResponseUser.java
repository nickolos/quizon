package com.octopus.quizon.data;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
public class ResponseUser {
    @SerializedName("uid")
    @Expose
    private String mId;
    @SerializedName("name")
    @Expose
    private String mName;
    @SerializedName("email")
    @Expose
    private String mEmail;

    public ResponseUser(String id, String name, String email) {
        mId = id;
        mName = name;
        mEmail = email;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getEmail() {
        return mEmail;
    }

}
