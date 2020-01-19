
package com.octopus.quizon.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.octopus.quizon.R;
import com.octopus.quizon.data.NetworkService;
import com.octopus.quizon.data.ResponseGameId;
import com.octopus.quizon.data.ResponseUser;
import com.octopus.quizon.fragment.LastResultFragment;
import com.octopus.quizon.fragment.ListOfGamesFragment;
import com.octopus.quizon.fragment.MenuFragment;
import com.octopus.quizon.fragment.OnBackListener;
import com.octopus.quizon.fragment.ProfileFragment;
import com.octopus.quizon.fragment.SettingsFragment;
import com.octopus.quizon.fragment.WaitingFragment;
import com.octopus.quizon.repos.RepoRequestState;
import com.octopus.quizon.viewmodel.MenuViewModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.octopus.quizon.R.id.fragment_holder_layout;

public class MainActivity extends AppCompatActivity implements WaitingFragment.OnWaitingOverListener, MenuFragment.MenuListener, ProfileFragment.ToMain, ListOfGamesFragment.OnConnectListener {

    private ProfileFragment mProfileFragment;
    private SettingsFragment mSettingsFragment;
    private WaitingFragment mWaitingFragment;
    private MenuFragment mMenuFragment;
    private LastResultFragment mLastResultFragment;
    private ListOfGamesFragment mListOfGamesFragment;

    private LiveData<RepoRequestState> mRequestState;

    private ResponseUser mUser;

    private Fragment mActiveFragment;

    private MenuViewModel mViewModel;

    private ArrayList<OnBackListener> mBackListeners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActionBar bar = getSupportActionBar();
        if (bar != null) bar.hide();

        setContentView(R.layout.activity_main);

        mProfileFragment = ProfileFragment.newInstance();
        mSettingsFragment = SettingsFragment.newInstance();
        mWaitingFragment = WaitingFragment.newInstance();
        mMenuFragment = MenuFragment.newInstance();
        mLastResultFragment = LastResultFragment.newInstance();
        mListOfGamesFragment = ListOfGamesFragment.newInstance();

        mViewModel = ViewModelProviders.of(this).get(MenuViewModel.class);
        mRequestState = mViewModel.getRepoRequestState();
        mRequestState.observe(this, (data)->{

        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(fragment_holder_layout, mMenuFragment)
                .commit();

        Bundle args = getIntent().getExtras();
        if (args != null) {
            mUser = new ResponseUser(args.getString("user_id"), args.getString("user_name"), args.getString("user_email"));
        }

        mActiveFragment = mMenuFragment;
        mBackListeners = new ArrayList();
        mBackListeners.add(mWaitingFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(fragment_holder_layout, mActiveFragment == null ? mMenuFragment : mActiveFragment)
                .commit();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onPlay() {
        String backStateName = this.getClass().getName();

        Bundle bundle = new Bundle();
        bundle.putString("user_id", mUser.getId());
        bundle.putString("user_name", mUser.getName());
        bundle.putString("user_email", mUser.getEmail());
        mListOfGamesFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(fragment_holder_layout, mListOfGamesFragment)
                .addToBackStack(backStateName)
                .commit();
        mActiveFragment = mListOfGamesFragment;
    }

    @Override
    public void onSettings() {
        String backStateName = this.getClass().getName();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(fragment_holder_layout, mSettingsFragment)
                .addToBackStack(backStateName)
                .commit();
        mActiveFragment = mSettingsFragment;
    }

    @Override
    public void onProfile() {
        String backStateName = this.getClass().getName();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(fragment_holder_layout, mProfileFragment)
                .addToBackStack(backStateName)
                .commit();
        mActiveFragment = mProfileFragment;
    }

    @Override
    public void clickLastResult() {
        String backStateName = this.getClass().getName();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(fragment_holder_layout, mLastResultFragment)
                .addToBackStack(backStateName)
                .commit();
        mActiveFragment = mLastResultFragment;
    }

    @Override
    public void onConnect(ResponseGameId game) {

        Bundle bundle = new Bundle();
        bundle.putLong("game_id", game.getId());
        bundle.putLong("waiting_time", game.getWaitingTime());
        mWaitingFragment.setArguments(bundle);

        if (mWaitingFragment != null)
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(fragment_holder_layout, mWaitingFragment)
                    .commit();

        mActiveFragment = mWaitingFragment;
    }

    @Override
    public void onBackPressed() {
        if(mActiveFragment != mMenuFragment)
            super.onBackPressed();
        for(OnBackListener listener : mBackListeners) listener.onBackClicked();
    }

    @Override
    public void onWaitingOver(Long gameId) {
        Call<ResponseGameId> call = NetworkService.getInstance().getAPI().joinToGame(gameId, mUser);
        call.enqueue(new Callback<ResponseGameId>() {
            @Override
            public void onResponse(Call<ResponseGameId> call, Response<ResponseGameId> response) {
                if(response.body() != null) {
                    if (mWaitingFragment != null)
                        getSupportFragmentManager()
                                .beginTransaction()
                                .remove(mWaitingFragment)
                                .commit();

                    mActiveFragment = null;

                    Intent intent = new Intent(MainActivity.this, SessionActivity.class);
                    intent.putExtra("user_id", mUser.getId());
                    intent.putExtra("user_name", mUser.getName());
                    intent.putExtra("user_email", mUser.getEmail());
                    intent.putExtra("game_id", response.body().getId());
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), getString(R.string.game_connection_failed), Toast.LENGTH_SHORT);
                    Log.e("QError", "onChoose->onResponse");
                }
            }

            @Override
            public void onFailure(Call<ResponseGameId> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getString(R.string.game_connection_failed), Toast.LENGTH_SHORT);
                Log.e("QError", t.getMessage());
            }
        });
    }
}