package com.kyser.demosuite.view.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.kyser.demosuite.R;
import com.kyser.demosuite.database.DataManager;
import com.kyser.demosuite.database.NoteKeeperHelper;
import com.kyser.demosuite.service.preferences.HistoryService;
import com.kyser.demosuite.service.streamservice.StreamService;

public class Splash extends AppCompatActivity implements Animation.AnimationListener, View.OnClickListener {

    private int mAnimationIndex = 0;
    private int RC_SIGN_IN=100;
    private Animation animFadeIn;
    private Animation animFadeOut;
    private ImageView splash_image1;
    private boolean serviceReady = false;
    private boolean __SINGINFLAG =false;
    private GoogleSignInClient mGoogleSignInClient;
    private String TAG="SplashFLOW";
    private GoogleSignInAccount mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        callServiceTest();
        startSplashAnimation();
        HistoryService.getInstance().getHistory(this);
        NoteKeeperHelper np = new NoteKeeperHelper(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        __SINGINFLAG = false;
                    }
                });
        findViewById(R.id.sign_in_button).setOnClickListener(this);
//        DataManager.getInstance().demo(np);
    }

    private void callServiceTest() {
        StreamService.getInstance().testService(response -> {
            if(!response.equals("ERROR"))
                serviceReady = true;
            else{
                new Handler().postDelayed(() -> callServiceTest(),10*1000);
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavBars();
    }

    private void startSplashAnimation() {
        splash_image1= findViewById(R.id.splash_icon1);
        animFadeIn = AnimationUtils.loadAnimation(this,   R.anim.fade_in);
        animFadeOut = AnimationUtils.loadAnimation(this,   R.anim.fade_out);
        animFadeIn.setAnimationListener(this);
        animFadeOut.setAnimationListener(this);
        new Handler().postDelayed(() -> {
             splash_image1.startAnimation(animFadeIn);
        },1000);
    }

    @Override
    public void onAnimationStart(Animation animation) { }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == animFadeIn ) {
            splash_image1.startAnimation(animFadeOut);
        }else {
            mAnimationIndex++;
            Drawable icon =((mAnimationIndex%4==0)?getResources().getDrawable(R.drawable.flim):
                    ((mAnimationIndex%4==1)?getResources().getDrawable(R.drawable.audio):
                            ((mAnimationIndex%4==2)?getResources().getDrawable(R.drawable.watching_tv):
                            getResources().getDrawable(R.drawable.radio))));
            splash_image1.setImageDrawable(icon);

            if( !(serviceReady && mAnimationIndex>=4 && __SINGINFLAG ) ){
                splash_image1.startAnimation(animFadeIn);
            }else{
                Intent intent = new Intent(this,Featured.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(getString(R.string.account_name), mAccount.getGivenName());
                intent.putExtra(getString(R.string.account_face), mAccount.getPhotoUrl().toString());
                intent.putExtra(getString(R.string.account_mail), mAccount.getEmail());
                intent.putExtra(getString(R.string.account_given_name), mAccount.getGivenName());
                intent.putExtra(getString(R.string.account_family_name), mAccount.getFamilyName());
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                finish();
            }
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) { }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach  a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if(account != null) {
          //  ((TextView) findViewById(R.id.status_text)).setText(new StringBuilder().append("Signed in as :").append(account.getGivenName()).toString());
            __SINGINFLAG = true;
            Log.v(TAG,account.getPhotoUrl()+"");
            SharedPreferences sharedPref =this.getPreferences(Context.MODE_PRIVATE);
            mAccount = account;
            ((TextView)findViewById(R.id.sign_in_status)).setText(new StringBuilder().append("Welcome ").append(account.getGivenName()).append("!\nFetching Recommendations...").toString());
            ((TextView)findViewById(R.id.sign_in_status)).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    protected void hideNavBars() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}
