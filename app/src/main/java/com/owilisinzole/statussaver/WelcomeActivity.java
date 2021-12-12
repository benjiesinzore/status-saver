package com.owilisinzole.statussaver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.owilisinzole.statussaver.R;

import static android.content.ContentValues.TAG;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //TransitionAnimation From WelcomeSplash To MainActivity
        final Intent welcomeToMainActivity = new Intent(WelcomeActivity.this, MainActivity.class);
        Thread timer = new Thread(){
            public void run() {
                try {
                    sleep(6000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally{
                    startActivity(welcomeToMainActivity);
                    finish();
                }
            }
        };
        timer.start();
        //TransitionAnimation From WelcomeSplash To MainActivity
    }
}