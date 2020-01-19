package com.octopus.quizon.data;

public class AnswerData {

    private String mAnswer;

    public AnswerData(String answer) {
        mAnswer = answer;
    }

    @Override
    public String toString()
    {
        return mAnswer;
    }
}
