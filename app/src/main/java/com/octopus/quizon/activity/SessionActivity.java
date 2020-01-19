package com.octopus.quizon.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.octopus.quizon.R;
import com.octopus.quizon.data.ResponseGameId;
import com.octopus.quizon.data.ResponseUser;
import com.octopus.quizon.fragment.QuestionFragment;
import com.octopus.quizon.fragment.ResultFragment;
import com.octopus.quizon.repos.SessionRepo;

public class SessionActivity extends AppCompatActivity implements QuestionFragment.ResultListener, ResultFragment.MainGoing {

    private QuestionFragment mQuestionFragment;
    private ResultFragment mResultFragment;

    private ResponseGameId mGameId;
    private ResponseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar bar = getSupportActionBar();
        if (bar != null) bar.hide();

        mQuestionFragment = QuestionFragment.newInstance();
        mResultFragment = ResultFragment.newInstance();

        Bundle args = getIntent().getExtras();
        if (args != null) {
            mGameId = new ResponseGameId(args.getLong("game_id"), 0L);
            mUser = new ResponseUser(args.getString("user_id"), args.getString("user_name"), args.getString("user_email"));
        }
        mQuestionFragment.setArguments(args);
        mResultFragment.setArguments(args);

        setContentView(R.layout.activity_session);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container_session, mQuestionFragment)
                .commit();

    }

    @Override
    public void toResult() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(mQuestionFragment)
                .commit();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container_session, mResultFragment)
                .commit();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void toMain() {
        finish();
    }
}
