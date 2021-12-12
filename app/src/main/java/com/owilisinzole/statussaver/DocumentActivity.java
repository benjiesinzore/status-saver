package com.owilisinzole.statussaver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.owilisinzole.statussaver.R;

import java.io.File;

public class DocumentActivity extends AppCompatActivity {
    private String filePath = "";
    private ImageView imageView, imageViewPlayIcon;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imageView = findViewById(R.id.viewImageView);
        imageViewPlayIcon = findViewById(R.id.playIconImageView);
        textView = findViewById(R.id.videoPlayerText);

        //PDFView pdfView = findViewById(R.id.pdfView);
        filePath = getIntent().getStringExtra("path");
        final File fileToShow = new File(filePath);

        if (filePath.endsWith(".mp4")){
            imageViewPlayIcon.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }else {
            imageViewPlayIcon.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
        }

        Glide.with(this)
                .load(fileToShow)
                .into(imageView);

       /* File file = new File(filePath);
        Uri path = Uri.fromFile(file);
        pdfView.fromUri(path).load();*/


    }
}