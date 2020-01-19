package com.octopus.quizon.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.octopus.quizon.data.ResponseUser;
import com.octopus.quizon.repos.AuthRepo;

public class AuthViewModel extends AndroidViewModel {

    private LiveData<ResponseUser> mResponseUserLiveData;

    private AuthRepo mAuthRepo;

    public AuthViewModel(@NonNull Application application) {
        super(application);

        mAuthRepo = new AuthRepo(application);
    }

//    public LiveData<ResponseUser> getResponseUserLiveData()
//    {
//        mResponseUserLiveData = mAuthRepo;
//        return mResponseUserLiveData;
//    }
}
