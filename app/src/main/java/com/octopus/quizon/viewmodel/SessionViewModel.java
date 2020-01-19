package com.octopus.quizon.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.octopus.quizon.data.AnswerData;
import com.octopus.quizon.data.ResponseQuestion;
import com.octopus.quizon.data.ResponseQuestionFifthRound;
import com.octopus.quizon.data.ResponseResult;
import com.octopus.quizon.data.ResponseSave;
import com.octopus.quizon.data.ResultFromUser;
import com.octopus.quizon.data.TableResultGame;
import com.octopus.quizon.repos.RepoRequestState;
import com.octopus.quizon.repos.SessionRepo;

public class SessionViewModel extends AndroidViewModel {

    private SessionRepo mRepo;

    private LiveData<RepoRequestState> mRepoRequestStateLiveData;

    public SessionViewModel(@NonNull Application application) {
        super(application);
        mRepo = new SessionRepo(application);
    }

    public void postAnswer(ResultFromUser data) {
        mRepo.postAnswer(data);
    }

    public LiveData<ResponseQuestionFifthRound> getQuestion(Long idGame, int idRound, int idQuestion) {
        return mRepo.getQuestion(idGame, idRound, idQuestion);
    }

    public LiveData<TableResultGame> getResults(Long gameId)
    {
        return mRepo.getResults(gameId);
    }

    public boolean saveResults(TableResultGame results)
    {
        mRepo.saveResults(results);
        return true;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public LiveData<RepoRequestState> getRepoRequestState()
    {
        mRepoRequestStateLiveData = mRepo.getRepoRequestState();
        return mRepoRequestStateLiveData;
    }
}
