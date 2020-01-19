package com.octopus.quizon.data;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
public class ResultFromUser {

    @SerializedName("id_game")
    @Expose
    private Long mGameId;

    @SerializedName("number")
    @Expose
    private int mNumber;

    @SerializedName("uid")
    @Expose
    private String mUid;

    @SerializedName("name")
    @Expose
    private String mName;

    @SerializedName("answer_user")
    @Expose
    private String mAnswer;

    public ResultFromUser(Long id_game, int number, String uid, String name, String answer) {
        mGameId = id_game;
        mNumber = number;
        mUid = uid;
        mName = name;
        mAnswer = answer;
    }
}
