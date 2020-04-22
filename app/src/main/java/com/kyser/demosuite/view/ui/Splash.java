package com.kyser.demosuite.view.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.kyser.demosuite.R;
import com.kyser.demosuite.service.preferences.HistoryService;
import com.kyser.demosuite.service.streamservice.StreamService;

public class Splash extends AppCompatActivity implements Animation.AnimationListener {

    private int mAnimationIndex = 0;
    private Animation animFadeIn;
    private Animation animFadeOut;
    private ImageView splash_image1;
    private boolean serviceReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        callServiceTest();
        startSplashAnimation();
        HistoryService.getInstance().getHistory(this);
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
            if(getResources().getString(R.string.mode).equals("dev") && serviceReady) {
                Intent intent = new Intent(this,Featured.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                finish();
            }else if(!serviceReady||mAnimationIndex<4)
                splash_image1.startAnimation(animFadeIn);
            else{
                Intent intent = new Intent(this,Featured.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                finish();
            }
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) { }

    protected void hideNavBars() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}
