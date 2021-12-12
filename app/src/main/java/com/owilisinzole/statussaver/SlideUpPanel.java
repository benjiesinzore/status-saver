package com.owilisinzole.statussaver;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.owilisinzole.statussaver.R;

public class SlideUpPanel extends AppCompatActivity {
    Dialog popAddPost;
    Button PostProduct, PostService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_up_panel);

    }
}
