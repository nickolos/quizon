package com.octopus.quizon.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.octopus.quizon.R;
import com.octopus.quizon.data.ResponseGameId;
import com.octopus.quizon.data.ResponseQuestionFifthRound;
import com.octopus.quizon.data.ResponseUser;
import com.octopus.quizon.data.ResultFromUser;
import com.octopus.quizon.viewmodel.SessionViewModel;

import java.util.Timer;
import java.util.TimerTask;

public class QuestionFragment extends Fragment {

    @FunctionalInterface
    public interface ResultListener {
        void toResult();
    }

    private ResponseGameId mGameId;
    private ResponseUser mUser;

    private SessionViewModel mViewModel;

    private TextView mQuestionTimeView;
    private TextView mQuestionTextView;
    private ImageView mQuestionImageView;
//    private Button mPostButton;
    private EditText mAnswerEdit;
    private ProgressBar mWaitingBar;

    private Handler mTimerHandler;
    private int mTime = 1000;
    private boolean mReady = false;

    private Timer mTimer;

    private final int TEST_QUESTIONS_NUMBER = 6;
    private int mQuestionId = 0;

    private boolean mAnswered = false;

    public static QuestionFragment newInstance() {
        return new QuestionFragment();
    }

    @SuppressLint("HandlerLeak")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View inflated = inflater.inflate(R.layout.question_fragment, container, false);

        mViewModel = ViewModelProviders.of(this).get(SessionViewModel.class);

        mQuestionImageView = inflated.findViewById(R.id.question_image_view);
        mQuestionTextView = inflated.findViewById(R.id.question_text_view);
        mQuestionTimeView = inflated.findViewById(R.id.question_time_view);
//        mPostButton = inflated.findViewById(R.id.post_button);
        mAnswerEdit = inflated.findViewById(R.id.answer_edit_view);
        mWaitingBar = inflated.findViewById(R.id.waiting_bar);

        Bundle args = getArguments();
        if (args != null) {
            mGameId = new ResponseGameId(args.getLong("game_id"), args.getLong("game_arrive_time"));
            mUser = new ResponseUser(args.getString("user_id"), args.getString("user_name"), args.getString("user_email"));
        }

        mTimer = new Timer();
        mTimerHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                super.handleMessage(message);
                switch (message.what) {
                    case 0:
                        mQuestionTimeView.setText(String.valueOf(mTime));
                        break;
                    case 1:
                        postAnswer(new ResultFromUser(mGameId.getId(),
                                mQuestionId,
                                mUser.getId(),
                                mUser.getName(),
                                mAnswerEdit.getText().toString()));
                        mAnswerEdit.setText("");
                }
            }
        };

        return inflated;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onWait();
        getQuestion();
    }

    public void onLoaded() {
        mQuestionTimeView.setVisibility(View.VISIBLE);
        mQuestionTextView.setVisibility(View.VISIBLE);
        mQuestionImageView.setVisibility(View.VISIBLE);
//        mPostButton.setVisibility(View.VISIBLE);
        mAnswerEdit.setVisibility(View.VISIBLE);
        mWaitingBar.setVisibility(View.GONE);
    }

    public void onWait() {
        mQuestionTimeView.setVisibility(View.GONE);
        mQuestionTextView.setVisibility(View.GONE);
        mQuestionImageView.setVisibility(View.GONE);
//        mPostButton.setVisibility(View.GONE);
        mAnswerEdit.setVisibility(View.GONE);
        mWaitingBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SessionViewModel.class);

//        mPostButton.setOnClickListener(v -> {
//            postAnswer(new ResultFromUser(mGameId.getId(),
//                    mQuestionId,
//                    mUser.getId(),
//                    mUser.getName(),
//                    mAnswerEdit.getText().toString()));
//        });

        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (mTime <= 0) {
                    mTimerHandler.obtainMessage(1).sendToTarget();
                } else {
                    mTime -= 1;
                    mTimerHandler.obtainMessage(0).sendToTarget();
                }
            }
        }, 0, 1000);
    }

    public void getQuestion() {
        LiveData<ResponseQuestionFifthRound> res = mViewModel.getQuestion(mGameId.getId(), 5, mQuestionId);
        Log.i("QInfo", "Get Question");
        res.observe(this, data -> {
            if (data != null) {
                switch (data.getIdRound()) {
                    case 5:
                        ResponseQuestionFifthRound question = data;
                        mQuestionTextView.setText(question.getText());
                        mQuestionImageView.setImageBitmap(question.getImage());
                        mTime = question.getTime() / 1000;
                        mQuestionTimeView.setText(String.valueOf(mTime));
                        mAnswered = false;
//                        mPostButton.setEnabled(true);
                        onLoaded();
                        break;
                    default:
                        Log.e("QError", "Unknown round id: " + data.getIdRound());
                        break;
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTimer.cancel();
    }

    public void postAnswer(ResultFromUser answerData) {
        if (!mAnswered) {
            mViewModel.postAnswer(answerData);
            mAnswered = true;
//            mPostButton.setEnabled(false);
            Log.i("QInfo", "QuestionID: " + mQuestionId);
            if (++mQuestionId >= TEST_QUESTIONS_NUMBER) {
                mTimer.cancel();
                ((ResultListener) getActivity()).toResult();
            }
            else
                getQuestion();
        }
    }
}
