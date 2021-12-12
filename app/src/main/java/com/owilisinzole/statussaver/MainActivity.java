package com.owilisinzole.statussaver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.owilisinzole.statussaver.R;

import java.io.File;
import java.util.ArrayList;

import Additional.Constant;
import Additional.StoryAdapter;
import Additional.StoryModule;

public class MainActivity extends AppCompatActivity /*implements OnPdfSelectListener*/ {

    private RecyclerView recyclerView;

    private AdView mAdView;

    //Permission
    private int STORAGE_PERMISSION_CODE = 1;

    ArrayList<Object> filesList = new ArrayList<>();
    private StoryAdapter storyAdapter;
    private Permision_Lib permision_lib;
    //File[] files;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adViewMainActivity);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRecyclerView);
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
                        Toast.makeText(MainActivity.this, "Refreshed", Toast.LENGTH_SHORT).show();
                    }
                }, 1500);
            }
        });

        runTimePermission();
    }

    private void runTimePermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            setUpRefreshLayout();

        }else {
            requestStoragePermission();

        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission Required")
                    .setMessage("This permission is need to access file from the internal storage")
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .create().show();

        }else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setUpRefreshLayout();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setUpRefreshLayout() {
        filesList.clear();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        storyAdapter = new StoryAdapter(MainActivity.this, getData());
        recyclerView.setAdapter(storyAdapter);
        storyAdapter.notifyDataSetChanged();
    }

    private ArrayList<Object> getData() {
        StoryModule f;
        String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.FOLDER_NAME+"Media/.Statuses";
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

    /*public ArrayList<Object> getData() {
        StoryModule f;
        String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.FOLDER_NAME+"Media/.Statuses";

        File targetDirector = new File(targetPath);
        files = targetDirector.listFiles();

        for (int i=0; i<files.length;i++){

            File file = files[i];
            /*f = new StoryModule();
            f.setUri(Uri.fromFile(file));
            f.setPath(files[i].getAbsolutePath());
            f.setFilename(file.getName());

            if (!f.getUri().toString().endsWith(".nomedia")){
                filesList.add(f);
            }
        }
        return filesList;
    }*/

    /*public ArrayList<File> findPdf (File file){

        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();

        for (File singleFile: files){

            if (singleFile.isDirectory() && !singleFile.isHidden()){
                arrayList.addAll(findPdf(singleFile));
            }else {
                if (singleFile.getName().endsWith(".pdf")){
                    arrayList.add(singleFile);
                }
            }
        }

        return arrayList;
    }

    private void displayPdf(){

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        pdfList = new ArrayList<>();
        pdfList.addAll(findPdf(Environment.getExternalStorageDirectory()));
        pdfAdapter = new PdfAdapter(this, pdfList, this);
        recyclerView.setAdapter(pdfAdapter);
    }

    @Override
    public void onPdfSelected(File file) {
        startActivity(new Intent(MainActivity.this, DocumentActivity.class)
        .putExtra("path", file.getAbsolutePath()));
    }*/
}