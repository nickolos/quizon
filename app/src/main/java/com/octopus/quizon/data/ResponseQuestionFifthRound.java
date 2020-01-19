package com.octopus.quizon.data;


import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseQuestionFifthRound implements ResponseQuestion {

    @SerializedName("number")
    @Expose
    private int mID;

    @SerializedName("question")
    @Expose
    private String mText;

    @SerializedName("id_game")
    @Expose
    private Long mIdGame;

    @SerializedName("file")
    @Expose
    private String mFilePath;

    private Bitmap mImage;

    private final int mIdRound = 5;
    private final int mTime = 60000;

    public int getID() {
        return mID;
    }

    public String getText() {
        return mText;
    }

    public String getFilePath() {
        return mFilePath;
    }

    public Bitmap getImage() {
        return mImage;
    }

    public void setImage(Bitmap image) {
        this.mImage = image;
    }

    @Override
    public int getTime() {
        return mTime;
    }

    @Override
    public int getIdRound() {
        return mIdRound;
    }

    @Override
    public Long getIdGame() {
        return mIdGame;
    }
}
