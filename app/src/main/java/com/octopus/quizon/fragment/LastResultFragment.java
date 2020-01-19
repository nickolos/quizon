package com.octopus.quizon.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.octopus.quizon.R;
import com.octopus.quizon.data.ResponseMoreResult;
import com.octopus.quizon.data.TableResultGame;
import com.octopus.quizon.repos.RepoRequestState;
import com.octopus.quizon.viewmodel.MenuViewModel;

import java.util.ArrayList;
import java.util.List;

public class LastResultFragment extends Fragment {

    private ResultDataAdapter mResultDataAdapter;
    private LiveData<TableResultGame> mResultListLiveData;
    private MenuViewModel mViewModel;

    private TextView mLastResultLoadingFailTextView;
    private RecyclerView mRecyclerView;

    private LiveData<RepoRequestState> mRepoRequestState;

    public static LastResultFragment newInstance() {
        return new LastResultFragment();
    }

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.last_result_fragment, container, false);

        mViewModel = ViewModelProviders.of(this).get(MenuViewModel.class);

        mRecyclerView = view.findViewById(R.id.list);
        mRecyclerView.setVisibility(View.GONE);
        mLastResultLoadingFailTextView = view.findViewById(R.id.loading_fail_textview);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mResultListLiveData = mViewModel.getLastGamesResults();
        mResultListLiveData.observe(getViewLifecycleOwner(), (data) -> {
            if(data != null) {
                mResultDataAdapter = new ResultDataAdapter(data);
                mRecyclerView.setAdapter(mResultDataAdapter);
                mLastResultLoadingFailTextView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                Log.i("QInfo", "Loading...");
            }
        });
        mRepoRequestState = mViewModel.getRepoRequestState();
        mRepoRequestState.observe(getViewLifecycleOwner(), (data)-> {
            if(data != null)
            {
                switch (data.getStatus())
                {
                    case ERROR:
                        Log.e("QError","No results found");
                        break;
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putAll(savedInstanceState);
    }


    class ResultDataAdapter extends RecyclerView.Adapter<ResultsViewHolder> {
        private TableResultGame mData;

        ResultDataAdapter(TableResultGame data) {
            mData = data;
        }

        @NonNull
        @Override
        public ResultsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_list_item, parent, false);
            return new ResultsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ResultsViewHolder holder, int position) {
            holder.textPlace.setText(String.valueOf(position + 1));
            holder.textName.setText(String.valueOf(mData.getTable().get(position).getName()));
            holder.textScore.setText(String.valueOf(mData.getTable().get(position).getScore()));
            //holder.textInfo.setText(mData.getTable().get(position).ge());
        }

        @Override
        public int getItemCount() {
            return mData.getTable().size();
        }
    }

    class ResultsViewHolder extends RecyclerView.ViewHolder {
        private final TextView textPlace;
        private final TextView textName;
        private final TextView textScore;
        //private final TextView textInfo;

        private ResultsViewHolder(@NonNull View itemView) {
            super(itemView);
            textPlace = itemView.findViewById(R.id.place);
            textName = itemView.findViewById(R.id.name);
            textScore = itemView.findViewById(R.id.score);
            //textInfo = itemView.findViewById(R.id.info);
        }
    }
}
