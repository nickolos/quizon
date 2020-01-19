package com.octopus.quizon.repos;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.octopus.quizon.data.ListActiveGame;
import com.octopus.quizon.data.NetworkService;
import com.octopus.quizon.data.ResponseGameId;
import com.octopus.quizon.data.ResponseMoreResult;
import com.octopus.quizon.data.ResponseUser;
import com.octopus.quizon.data.TableResultGame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuRepo {

    private MutableLiveData<TableResultGame> mResultsLiveData;
    private MutableLiveData<ListActiveGame> mActiveGamesListLiveData;
    private MutableLiveData<ResponseGameId> mResponseCreateGameIdLiveData;
    private MutableLiveData<ResponseGameId> mResponseJoinGameIdLiveData;

    private MutableLiveData<RepoRequestState> mRepoRequestStateLiveData;

    private Application mApplication;


    public MenuRepo(Application app) {
        mResultsLiveData = new MutableLiveData<>();
        mActiveGamesListLiveData = new MutableLiveData<>();
        mResponseCreateGameIdLiveData = new MutableLiveData<>();
        mResponseJoinGameIdLiveData = new MutableLiveData<>();

        mRepoRequestStateLiveData = new MutableLiveData<>();

        mApplication = app;
    }

    public LiveData<RepoRequestState> getRepoRequestState() {
        return mRepoRequestStateLiveData;
    }

    /**
     * ListOfGamesFragment section
     */

    public MutableLiveData<ListActiveGame> getActiveGames() {
        NetworkService
                .getInstance()
                .getAPI()
                .getListActiveGames()
                .enqueue(new Callback<ListActiveGame>() {
                    @Override
                    public void onResponse(Call<ListActiveGame> call, Response<ListActiveGame> response) {
                        if (response.body() != null) {
                            mActiveGamesListLiveData.postValue(response.body());
                            mRepoRequestStateLiveData.postValue(new RepoRequestState(RepoRequestState.Status.OK, "List refreshed successful"));
                        } else {
                            mRepoRequestStateLiveData.postValue(new RepoRequestState(RepoRequestState.Status.ERROR, "List refreshed failed"));
                        }
                    }

                    @Override
                    public void onFailure(Call<ListActiveGame> call, Throwable t) {
                        mRepoRequestStateLiveData.postValue(new RepoRequestState(RepoRequestState.Status.ERROR, "List refreshed failed: " + t.getMessage()));
                    }
                });
        return mActiveGamesListLiveData;
    }

    public LiveData<ResponseGameId> createGame(ResponseUser user) {
        NetworkService
                .getInstance()
                .getAPI()
                .create(user)
                .enqueue(new Callback<ResponseGameId>() {
                    @Override
                    public void onResponse(Call<ResponseGameId> call, Response<ResponseGameId> response) {
                        if (response.body() != null) {
                            mResponseCreateGameIdLiveData.postValue(response.body());
                            mRepoRequestStateLiveData.postValue(new RepoRequestState(RepoRequestState.Status.OK, "Game created successful"));
                        }
                        else
                            mRepoRequestStateLiveData.postValue(new RepoRequestState(RepoRequestState.Status.ERROR, "Game create failed"));
                    }

                    @Override
                    public void onFailure(Call<ResponseGameId> call, Throwable t) {
                        mRepoRequestStateLiveData.postValue(new RepoRequestState(RepoRequestState.Status.ERROR, "Game create failed: " + t.getMessage()));
                    }
                });

        return mResponseCreateGameIdLiveData;
    }

    public LiveData<ResponseGameId> joinGame(Long gameId, ResponseUser user) {
        Log.i("QInfo", "User: " + user.getName() + " : " + user.getId() + "; GameID: " + gameId);
        NetworkService
                .getInstance()
                .getAPI()
                .joinToGame(gameId, user)
                .enqueue(new Callback<ResponseGameId>() {
                    @Override
                    public void onResponse(Call<ResponseGameId> call, Response<ResponseGameId> response) {
                        if (response.body() != null) {
                            mResponseJoinGameIdLiveData.postValue(response.body());
                            mRepoRequestStateLiveData.postValue(new RepoRequestState(RepoRequestState.Status.OK, "Joining to game was successful"));
                        }
                        else
                            mRepoRequestStateLiveData.postValue(new RepoRequestState(RepoRequestState.Status.ERROR, "Joining to game was failed"));
                    }

                    @Override
                    public void onFailure(Call<ResponseGameId> call, Throwable t) {
                        mRepoRequestStateLiveData.postValue(new RepoRequestState(RepoRequestState.Status.ERROR, "Game create failed: " + t.getMessage()));
                    }
                });

        return mResponseJoinGameIdLiveData;
    }

    /**
     * LastResultFragment section
     */


    private File getStorageFile(String fileName) {
        return new File(mApplication.getFilesDir().getPath(), fileName);
    }

    public MutableLiveData<TableResultGame> getResult() {
        new ReadResultTask(mResultsLiveData).execute();
        return mResultsLiveData;
    }

    class ReadResultTask extends AsyncTask<Void, Void, Void> {

        private MutableLiveData<TableResultGame> responseLiveData;

        public ReadResultTask(MutableLiveData<TableResultGame> responseLiveData) {
            this.responseLiveData = responseLiveData;
        }

        @Override
        protected Void doInBackground(Void... v) {
            try (FileInputStream file = mApplication.openFileInput("results.json")) {
                InputStreamReader reader = new InputStreamReader(file);
                Gson gson = new Gson();
                TableResultGame results = gson.fromJson(reader, TableResultGame.class);
                responseLiveData.postValue(results);
            } catch (IOException e) {
                mRepoRequestStateLiveData.postValue(new RepoRequestState(RepoRequestState.Status.ERROR, "No results found"));
                e.printStackTrace();
            }
            return null;
        }
    }
}
