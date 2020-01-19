package com.octopus.quizon.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.octopus.quizon.R;
import com.octopus.quizon.data.ResponseGameId;
import com.octopus.quizon.data.ResponseMoreResult;
import com.octopus.quizon.data.ResponseUser;
import com.octopus.quizon.data.TableResultGame;
import com.octopus.quizon.viewmodel.MenuViewModel;
import com.octopus.quizon.viewmodel.SessionViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


public class ResultFragment extends Fragment {

    public interface MainGoing {
        void toMain();
    }

    private ResponseGameId mGameId;
    private ResponseUser mUser;

    private SessionViewModel mViewModel;

    private Button mToMainActivityButton;

    private ResultDataAdapter mResultDataAdapter;

    private LiveData<TableResultGame> mResultTableLiveData;

    private int mTime;

    public static ResultFragment newInstance() {
        return new ResultFragment();
    }

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View resultFragment = inflater.inflate(R.layout.result_fragment, container, false);
        mViewModel = ViewModelProviders.of(this).get(SessionViewModel.class);
        mToMainActivityButton = resultFragment.findViewById(R.id.to_main_activity_button);
        mToMainActivityButton.setOnClickListener(v -> {
            ((MainGoing)getActivity()).toMain();
        });

        Bundle args = getArguments();
        if (args != null) {
            mGameId = new ResponseGameId(args.getLong("game_id"), args.getLong("game_arrive_time"));
            Log.i("QInfo", "GameID: " + mGameId.getId());
            mUser = new ResponseUser(args.getString("user_id"), args.getString("user_name"), args.getString("user_email"));
            mTime = args.getInt("request_time") + 1;
        }

        return resultFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        final RecyclerView recyclerView = view.findViewById(R.id.list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        mResultTableLiveData = mViewModel.getResults(mGameId.getId());
        mResultTableLiveData.observe(getViewLifecycleOwner(), (data) -> {
            Log.i("QInfo", data.toString());
            if (data != null) {
                mResultDataAdapter = new ResultDataAdapter(data);
                mViewModel.saveResults(data);
                recyclerView.setAdapter(mResultDataAdapter);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putAll(savedInstanceState);
    }


    class ResultDataAdapter extends RecyclerView.Adapter<ResultFragment.MyViewHolder> {
        private TableResultGame mData;

        ResultDataAdapter(TableResultGame data) {
            mData = data;
        }

        @NonNull
        @Override
        public ResultFragment.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_list_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ResultFragment.MyViewHolder holder, int position) {
            holder.textPlace.setText(String.valueOf(mData.getTable().get(position).getPlace()));
            holder.textName.setText(String.valueOf(mData.getTable().get(position).getName()));
            holder.textScore.setText(String.valueOf(mData.getTable().get(position).getScore()));
        }

        @Override
        public int getItemCount() {
            return mData.getTable().size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView textPlace;
        private final TextView textName;
        private final TextView textScore;

        private MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textPlace = itemView.findViewById(R.id.place);
            textName = itemView.findViewById(R.id.name);
            textScore = itemView.findViewById(R.id.score);
        }
    }


}
