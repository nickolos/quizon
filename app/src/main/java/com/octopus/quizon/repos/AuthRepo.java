package com.octopus.quizon.repos;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.octopus.quizon.data.ResponseUser;

public class AuthRepo {


    private Application mApplication;

    public AuthRepo(Application app)
    {
        mApplication = app;

    }
}
