package com.octopus.quizon.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.octopus.quizon.R;
import com.octopus.quizon.data.ListActiveGame;
import com.octopus.quizon.data.NetworkService;
import com.octopus.quizon.data.ResponseGameId;
import com.octopus.quizon.data.ResponseUser;
import com.octopus.quizon.repos.RepoRequestState;
import com.octopus.quizon.viewmodel.MenuViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListOfGamesFragment extends Fragment {

    private MenuViewModel mViewModel;

    private ResponseUser mUser;

    private ActiveGamesListDataAdapter mDataAdapter;

    private LiveData<ListActiveGame> mActiveGamesLiveData;
    private LiveData<RepoRequestState> mRepoRequestStateLiveData;

    private LiveData<ResponseGameId> mResponseCreateGameLiveData;
    private LiveData<ResponseGameId> mResponseJoinGameLiveData;

    private Button mCreateButton;
    private Button mRefreshButton;
    private boolean mIsButtonClicked = false;

    public static ListOfGamesFragment newInstance() {
        return new ListOfGamesFragment();
    }

    public interface OnConnectListener {
        void onConnect(ResponseGameId game);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_of_games, container, false);

        mViewModel = ViewModelProviders.of(this).get(MenuViewModel.class);

        Bundle args = getArguments();
        if (args != null) {
            mUser = new ResponseUser(args.getString("user_id"), args.getString("user_name"), args.getString("user_email"));
        }

        mRepoRequestStateLiveData = mViewModel.getRepoRequestState();
        mRepoRequestStateLiveData.observe(getViewLifecycleOwner(), data -> {
            switch (data.getStatus()) {
                case OK:
                    Log.i("QInfo", data.getMessage());
                    break;
                case ERROR:
                    Log.e("QError", data.getMessage());
                    Toast.makeText(getContext(), data.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
            mIsButtonClicked = false;
        });

        onRefreshGamesList();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mCreateButton = view.findViewById(R.id.create_game);
        mCreateButton.setOnClickListener(v -> onCreateGame());

        mRefreshButton = view.findViewById(R.id.refresh_button);
        mRefreshButton.setOnClickListener(v -> onRefreshGamesList());

        final RecyclerView recyclerView = view.findViewById(R.id.list_game);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        mDataAdapter = new ActiveGamesListDataAdapter();
        recyclerView.setAdapter(mDataAdapter);
    }

    public void onRefreshGamesList() {
        if (!mIsButtonClicked) {
            mIsButtonClicked = true;
            mActiveGamesLiveData = mViewModel.getActiveGames();
            mActiveGamesLiveData.observe(getViewLifecycleOwner(), data -> {
                mDataAdapter.setData(data);
                mIsButtonClicked = false;
            });
        }
    }

    public void onCreateGame() {
        if (!mIsButtonClicked) {
            mIsButtonClicked = true;
            mResponseCreateGameLiveData = mViewModel.createGame(mUser);
            mResponseCreateGameLiveData.observe(getViewLifecycleOwner(), data -> {
                ((OnConnectListener) getActivity()).onConnect(data);
                mIsButtonClicked = false;
            });
        }
    }

    public void onJoinGame(Long gameId) {
        //if (!mIsButtonClicked) {
            mIsButtonClicked = true;
            mResponseJoinGameLiveData = mViewModel.joinGame(gameId, mUser);
            mResponseJoinGameLiveData.observe(getViewLifecycleOwner(), data -> {
                if (data != null)
                    ((OnConnectListener) getActivity()).onConnect(data);
                else {
                    Log.e("QError", "Game join failed");
                    Toast.makeText(getContext(), getString(R.string.game_connection_failed), Toast.LENGTH_SHORT).show();
                    onRefreshGamesList();
                }
                mIsButtonClicked = false;
            });
        //}
    }

    class ActiveGamesListDataAdapter extends RecyclerView.Adapter<ListOfGamesFragment.ActiveGamesListViewHolder> {

        private ListActiveGame list;

        public void setData(ListActiveGame list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ListOfGamesFragment.ActiveGamesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_of_games_item, parent, false);
            return new ListOfGamesFragment.ActiveGamesListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListOfGamesFragment.ActiveGamesListViewHolder holder, int position) {
            if (list != null) {
                holder.mStartGameButton.setText(getString(R.string.game) + " " + list.getActiveGamesList().get(position).toString());
                holder.mStartGameButton.setOnClickListener(v -> {
                    if (!mIsButtonClicked) {
                        mIsButtonClicked = true;
                        onJoinGame(list.getActiveGamesList().get(position));
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            if (list != null)
                return list.getActiveGamesList().size();
            else
                return 0;
        }


    }

    class ActiveGamesListViewHolder extends RecyclerView.ViewHolder {
        private Button mStartGameButton;

        public ActiveGamesListViewHolder(@NonNull View itemView) {
            super(itemView);
            mStartGameButton = itemView.findViewById(R.id.number_game);
        }
    }
}
