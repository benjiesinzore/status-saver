package com.owilisinzole.statussaver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.owilisinzole.statussaver.R;

import java.io.File;
import java.util.ArrayList;

import Additional.Constant;
import Additional.StoryAdapter;
import Additional.StoryModule;

import static android.content.ContentValues.TAG;

public class GalleryView extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    private AdView mAdView;
    private InterstitialAd mInterstitialOnClickAboutDev;

    ArrayList<Object> filesList = new ArrayList<>();
    private StoryAdapter storyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_view);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        //AfroAds
        AdRequest adRequestAfroAds = new AdRequest.Builder().build();
        InterstitialAd.load(this, "ca-app-pub-9730894483622376/8070074161", adRequestAfroAds, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialOnClickAboutDev = interstitialAd;
                Log.i(TAG, "onAdLoaded");

                mInterstitialOnClickAboutDev.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        Log.d("TAG", "Ad failed to show");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        mInterstitialOnClickAboutDev = null;
                        Log.d("TAG", "Ad shown");
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        final Intent welcomeToMainActivity = new Intent(GalleryView.this, MainActivity.class);
                        startActivity(welcomeToMainActivity);
                        finish();
                        Log.d("TAG", "Ad dismissed");
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.i(TAG, loadAdError.getMessage());
                mInterstitialOnClickAboutDev = null;
            }
        });
        mAdView = findViewById(R.id.adViewGallery);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        recyclerView = findViewById(R.id.recyclerViewGallery);
        swipeRefreshLayout = findViewById(R.id.swipeRecyclerViewGallery);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeRefreshLayout.setRefreshing(true);
                setUpRefreshLayout();
                (
                        new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(GalleryView.this, "Refreshed", Toast.LENGTH_SHORT).show();
                    }
                }, 1500);
            }
        });

        setUpRefreshLayout();
    }

    private void setUpRefreshLayout() {
        filesList.clear();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        storyAdapter = new StoryAdapter(GalleryView.this, getData());
        recyclerView.setAdapter(storyAdapter);
        storyAdapter.notifyDataSetChanged();
    }

    private ArrayList<Object> getData() {
        StoryModule f;
        String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.SAVE_FOLDER_NAME+"";
        Log.d("Files", "Path: " + targetPath);
        File targetDirector = new File(targetPath);
        File files[] = targetDirector.listFiles();
        Log.d("Files", "Size: "+ files.length);
        for (int i=0; i<files.length; i++){
            Log.d("Files", "FileName: " +files[i].getName());

            File file = files[i];
            f = new StoryModule();
            f.setUri(Uri.fromFile(file));
            f.setPath(files[i].getAbsolutePath());
            f.setFilename(file.getName());

            if (!f.getUri().toString().endsWith(".nomedia")){
                filesList.add(f);
            }
        }
        return filesList;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (mInterstitialOnClickAboutDev != null){
            mInterstitialOnClickAboutDev.show(GalleryView.this);
        }else {
            Log.d("TAG", "Ad not available");
            Intent galleryIntent = new Intent(GalleryView.this, MainActivity.class);
            galleryIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(galleryIntent);
            finish();
        }
    }
}