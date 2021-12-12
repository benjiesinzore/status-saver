package com.owilisinzole.statussaver;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class VideoView_Activity extends AppCompatActivity {

    private String filePath = "";
    private ImageView imageViewPlayIcon;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        filePath = getIntent().getStringExtra("path");
        final File fileToShow = new File(filePath);

        imageViewPlayIcon = findViewById(R.id.playIconImageView);
        videoView = (VideoView) findViewById(R.id.viewVideoView);

        if (videoView != null){
            MediaController mc = new MediaController(this);
            mc.setAnchorView(videoView);
            mc.setMediaPlayer((MediaController.MediaPlayerControl) videoView);
            Uri uri = Uri.parse(filePath);
            videoView.setMediaController(mc);
            videoView.setVideoURI(uri);
            videoView.start();

        } else {
            Toast.makeText(this, "Video not Found", Toast.LENGTH_SHORT).show();
        }

        imageViewPlayIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                videoView.start();
                if (videoView.isPlaying()){
                    imageViewPlayIcon.setVisibility(view.INVISIBLE);
                }else {
                    imageViewPlayIcon.setVisibility(view.VISIBLE);
                }
                if (videoView.isPressed()){
                    videoView.pause();
                    imageViewPlayIcon.setVisibility(view.VISIBLE);
                }
            }
        });

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                videoView.start();
                if (videoView.isPlaying()){
                    imageViewPlayIcon.setVisibility(view.INVISIBLE);
                }else {
                    imageViewPlayIcon.setVisibility(view.VISIBLE);
                }
                if (videoView.isPressed()){
                    videoView.pause();
                    imageViewPlayIcon.setVisibility(view.VISIBLE);
                }
            }
        });

    }
}