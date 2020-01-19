package com.octopus.quizon.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.octopus.quizon.R;
import com.octopus.quizon.data.ResponseGameId;
import com.octopus.quizon.data.ResponseUser;
import com.octopus.quizon.data.ResultFromUser;
import com.octopus.quizon.viewmodel.MenuViewModel;

import java.util.Timer;
import java.util.TimerTask;

public class WaitingFragment extends Fragment implements OnBackListener{

    @FunctionalInterface
    public interface OnWaitingOverListener {
        void onWaitingOver(Long gameId);
    }

    private MenuViewModel mViewModel;

    private Handler mTimerHandler;

    private Timer mTimer;
    private int mTimeValue;
    private TextView mWaitingTimeTextView;

    private ResponseGameId mGame;

    public static WaitingFragment newInstance() {
        return new WaitingFragment();
    }

    @SuppressLint("HandlerLeak")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.waiting_fragment, container, false);

        Bundle args = getArguments();
        if (args != null) {
            mGame = new ResponseGameId(args.getLong("game_id"), args.getLong("waiting_time"));
        }
        mTimeValue = mGame.getWaitingTime().intValue() / 2000;

        mWaitingTimeTextView = view.findViewById(R.id.waiting_time_text);

        mTimerHandler = new Handler(){
            @Override
            public void handleMessage(Message message) {
                super.handleMessage(message);
                switch (message.what) {
                    case 0:
                        mWaitingTimeTextView.setText(getString(R.string.waiting_time) + " " + mTimeValue);
                        break;
                    case 1:
                        ((OnWaitingOverListener)getActivity()).onWaitingOver(mGame.getId());
                        break;
                }
            }
        };

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MenuViewModel.class);

        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(mTimeValue <= 0)
                {
                    cancel();
                    ((OnWaitingOverListener)getActivity()).onWaitingOver(mGame.getId());
                }
                else
                {
                    mTimeValue -= 1;
                    mTimerHandler.obtainMessage(0).sendToTarget();
                }
            }
        }, 0, 1000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTimer.cancel();
    }

    @Override
    public void onPause() {
        super.onPause();
        mTimer.cancel();
    }

    @Override
    public void onBackClicked() {
        mTimer.cancel();
    }
}
