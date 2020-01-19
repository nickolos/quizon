package com.octopus.quizon.repos;

import android.app.Application;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.octopus.quizon.data.AnswerData;
import com.octopus.quizon.data.NetworkService;
import com.octopus.quizon.data.ResponseMoreResult;
import com.octopus.quizon.data.ResponseQuestion;
import com.octopus.quizon.data.ResponseQuestionFifthRound;
import com.octopus.quizon.data.ResponseResult;
import com.octopus.quizon.data.ResponseSave;
import com.octopus.quizon.data.ResultFromUser;
import com.octopus.quizon.data.TableResultGame;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SessionRepo {

    private Application mApplication;

    private MutableLiveData<ResponseQuestionFifthRound> mQuestionData;
    private MutableLiveData<TableResultGame> mResultData;

    private MutableLiveData<RepoRequestState> mRepoRequestState;

    public SessionRepo(Application app) {
        super();
        mApplication = app;

        mQuestionData = new MutableLiveData<>();
        mResultData = new MutableLiveData<>();

        mRepoRequestState = new MutableLiveData<>();
    }

    public void postAnswer(ResultFromUser data) {
        NetworkService.getInstance().getAPI().save(data).enqueue(new Callback<ResponseSave>() {
            @Override
            public void onResponse(Call<ResponseSave> call, Response<ResponseSave> response) {

            }

            @Override
            public void onFailure(Call<ResponseSave> call, Throwable t) {

            }
        });
    }

    public boolean saveResults(TableResultGame results)
    {
        new WriteResultTask().execute(results);
        return true;
    }

    public MutableLiveData<ResponseQuestionFifthRound> getQuestion(Long idGame, int idRound, int idQuestion) {
        NetworkService.getInstance()
                .getAPI()
                .getQuestion(idGame, idRound, idQuestion)
                .enqueue(new Callback<ResponseQuestionFifthRound>() {
                    @Override
                    public void onResponse(Call<ResponseQuestionFifthRound> call, Response<ResponseQuestionFifthRound> response) {
                        if (response.body() != null)
                        {
                            new LoadingQuestionImageTask(mQuestionData).execute(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseQuestionFifthRound> call, Throwable t) {
                        Log.e("QError", "SessionRepo.getQuestion: " + t.getMessage());
                    }
                });
        return mQuestionData;
    }

    private class LoadingQuestionImageTask extends AsyncTask<ResponseQuestionFifthRound, Void, Void> {

        MutableLiveData<ResponseQuestionFifthRound> questionLiveData;

        LoadingQuestionImageTask(MutableLiveData<ResponseQuestionFifthRound> question)
        {
            questionLiveData = question;
        }

        @Override
        protected Void doInBackground(ResponseQuestionFifthRound... res) {
            InputStream inputStream = null;
            try {
                    String img = res[0].getFilePath().replaceAll("/home/nsilakov/","");
                    inputStream = new URL(NetworkService.BASE_URL + NetworkService.PIC_PORT + "/img/"+img).openStream();
                //inputStream = new URL(NetworkService.BASE_URL + NetworkService.PIC_PORT + "/img/img1.jpg").openStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            res[0].setImage(BitmapFactory.decodeStream(inputStream));
            questionLiveData.postValue(res[0]);
            return null;
        }
    }

    public LiveData<TableResultGame> getResults(Long idGame) {
        Log.i("QInfo", "Result idGame: " + idGame);
        NetworkService
                .getInstance()
                .getAPI()
                .tableresult(idGame)
                .enqueue(new Callback<TableResultGame>() {
                    @Override
                    public void onResponse(Call<TableResultGame> call, Response<TableResultGame> response) {
                        if (response.body() != null) {
                            mResultData.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<TableResultGame> call, Throwable t) {
                        Log.e("QError", "SessionRepo: " + t.getMessage());
                    }
                });
        return mResultData;
    }

    class WriteResultTask extends AsyncTask<TableResultGame, Void, Void> {

        @Override
        protected Void doInBackground(TableResultGame... results) {
            Gson gson = new Gson();
            String json = gson.toJson(results[0]);
            Log.i("QInfo", "Storage: " + mApplication.getFilesDir().getPath());
            try (FileOutputStream file = mApplication.openFileOutput("results.json", Context.MODE_PRIVATE)) {
                file.write(json.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public LiveData<RepoRequestState> getRepoRequestState()
    {
        return mRepoRequestState;
    }
}