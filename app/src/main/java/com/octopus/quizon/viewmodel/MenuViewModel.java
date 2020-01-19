package com.octopus.quizon.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.octopus.quizon.data.ListActiveGame;
import com.octopus.quizon.data.ResponseGameId;
import com.octopus.quizon.data.ResponseMoreResult;
import com.octopus.quizon.data.ResponseUser;
import com.octopus.quizon.data.TableResultGame;
import com.octopus.quizon.repos.MenuRepo;
import com.octopus.quizon.repos.RepoRequestState;

import java.util.List;

public class MenuViewModel extends AndroidViewModel {

    private MenuRepo mMenuRepo;

    private LiveData<TableResultGame> mLastGamesResultsLiveData;
    private LiveData<ListActiveGame> mActiveGamesIdsLiveData;
    private LiveData<ResponseGameId> mResponseCreateGameLiveData;
    private LiveData<ResponseGameId> mResponseJoinGameLiveData;
    private LiveData<ResponseUser> mResponseUserLiveData;

    private LiveData<RepoRequestState> mRepoRequestStateLiveData;

    public MenuViewModel(@NonNull Application application) {
        super(application);
        mMenuRepo = new MenuRepo(application);
    }

    public LiveData<TableResultGame> getLastGamesResults() {
        mLastGamesResultsLiveData = mMenuRepo.getResult();
        return mLastGamesResultsLiveData;
    }

    public LiveData<ListActiveGame> getActiveGames() {
        mActiveGamesIdsLiveData = mMenuRepo.getActiveGames();
        return mActiveGamesIdsLiveData;
    }

    public LiveData<RepoRequestState> getRepoRequestState() {
        mRepoRequestStateLiveData = mMenuRepo.getRepoRequestState();
        return mRepoRequestStateLiveData;
    }

    public LiveData<ResponseGameId> createGame(ResponseUser user) {
        mResponseCreateGameLiveData = mMenuRepo.createGame(user);
        return mResponseCreateGameLiveData;
    }

    public LiveData<ResponseGameId> joinGame(Long idGame, ResponseUser user) {
        mResponseJoinGameLiveData = mMenuRepo.joinGame(idGame, user);
        return mResponseJoinGameLiveData;
    }
}
