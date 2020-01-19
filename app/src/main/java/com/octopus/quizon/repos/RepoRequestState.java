package com.octopus.quizon.repos;

public class RepoRequestState {

    public enum Status
    {
        OK,
        ERROR
    }

    private String mMessage;
    private Status mStatus;

    public RepoRequestState(Status status, String message) {
        mMessage = message;
        mStatus = status;
    }

    public String getMessage() {
        return mMessage;
    }

    public Status getStatus() {
        return mStatus;
    }
}
