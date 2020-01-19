package com.octopus.quizon.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.Status;
import com.octopus.quizon.R;
import com.octopus.quizon.data.ResponseUser;

public class AuthActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int SIGNED_IN = 0;
    private static final int STATE_SIGNING_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;
    private static final int RC_SIGN_IN = 0;


    private GoogleApiClient mGoogleApiClient;
    private PendingIntent mSignInIntent;
    private Status mConnectionResult;
    private int mSignInProgress;

    private SignInButton mSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_fragment);

        findViewById(R.id.icon_imageview).setVisibility(View.VISIBLE);
        mSignInButton = findViewById(R.id.google_signin_button);
        mSignInButton.setEnabled(true);
        mSignInButton.setOnClickListener(v -> {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

        mGoogleApiClient = buildGoogleApiClient();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mSignInProgress = SIGNED_IN;
        onSignedIn();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (mSignInProgress != STATE_IN_PROGRESS) {
            mSignInIntent = result.getResolution();
            if (mSignInProgress == STATE_SIGNING_IN) {
                resolveSignInError();
            }
        }
        onSignedOut();
    }

    private void resolveSignInError() {
        if (mSignInIntent != null) {
            try {
                mSignInProgress = STATE_IN_PROGRESS;
                mConnectionResult.startResolutionForResult(this, STATE_IN_PROGRESS);
            } catch (IntentSender.SendIntentException e) {
                mSignInProgress = STATE_SIGNING_IN;
                mGoogleApiClient.connect();
            }
        } else {
        }
    }

    private GoogleApiClient buildGoogleApiClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        return new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_SIGN_IN:
                if (resultCode == RESULT_OK) {
                    mSignInProgress = STATE_SIGNING_IN;

                } else {
                    mSignInProgress = SIGNED_IN;
                }

                if (!mGoogleApiClient.isConnecting()) {
                    mGoogleApiClient.connect();
                    onSignedIn();
                }
                break;
        }
    }

    public void onSignedIn()
    {
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        opr.setResultCallback(result -> {
            if (result.isSuccess()) {
                try {
                    GoogleSignInAccount account = result.getSignInAccount();
                    ResponseUser user = new ResponseUser(account.getId(), account.getDisplayName(), account.getEmail());
                    Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                    intent.putExtra("user_id", user.getId());
                    intent.putExtra("user_name", user.getName());
                    intent.putExtra("user_email", user.getEmail());
                    startActivity(intent);
                } catch (Exception ex) {
                    String exception = ex.getLocalizedMessage();
                    Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, exception, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void onSignedOut() {
        if(mGoogleApiClient.isConnected())
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
    }
}
