package Additional;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.owilisinzole.statussaver.DocumentActivity;
import com.owilisinzole.statussaver.GalleryView;
import com.owilisinzole.statussaver.R;
import com.owilisinzole.statussaver.VideoView_Activity;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {

    private Context context;
    private ArrayList<Object> filesList;

    public StoryAdapter(Context context, ArrayList<Object> filesList) {
        this.context = context;
        this.filesList = filesList;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.card_row, null, false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {

        final StoryModule files = (StoryModule) filesList.get(position);

        if (files.getUri().toString().endsWith(".mp4")){
            holder.playIcon.setVisibility(View.VISIBLE);
        }else {
            holder.playIcon.setVisibility(View.INVISIBLE);
        }

        Glide.with(context)
                .load(files.getUri())
                .into(holder.saveImage);

        holder.saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (files.getUri().toString().endsWith(".mp4")){
                    final StoryModule pathImage = (StoryModule) filesList.get(position);
                    context.startActivity(new Intent(context, VideoView_Activity.class)
                            .putExtra("path", String.valueOf(pathImage)));

                }else {
                    final String fileVideo = ((StoryModule) filesList.get(position)).getPath();
                    context.startActivity(new Intent(context, DocumentActivity.class)
                            .putExtra("path", String.valueOf(fileVideo)));
                }
            }
        });

        holder.moreOptionsID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFolder();

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(context).inflate(
                        R.layout.activity_slide_up_panel, (LinearLayout) bottomSheetDialog.findViewById(R.id.slideupContainer)
                );
                bottomSheetView.findViewById(R.id.toGallery).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent shareIntent = new Intent(context, GalleryView.class);
                        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(shareIntent);
                    }
                });
                bottomSheetView.findViewById(R.id.shareFile).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final String pathImage = ((StoryModule) filesList.get(position)).getPath();
                        //final File fileToShare = new File(pathImage);

                        context.startActivity(new Intent(context, DocumentActivity.class)
                                .putExtra("path", pathImage));

                        /*Intent shareFileIntent = new Intent();
                        shareFileIntent.setAction(Intent.ACTION_SEND);
                        shareFileIntent.putExtra(Intent.EXTRA_STREAM, fileToShare);
                        shareFileIntent.setType("image/jpg");
                        context.startActivities(new Intent[]{shareFileIntent});
                        context.startActivities(new Intent[]{Intent.createChooser(shareFileIntent, "Share Image Via")});*/
                    }
                });
                bottomSheetView.findViewById(R.id.downloadFile).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final String path = ((StoryModule) filesList.get(position)).getPath();
                        final File file = new File(path);

                        String destPath = Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.SAVE_FOLDER_NAME;
                        File destFile = new File(destPath);

                        try {
                            FileUtils.copyFileToDirectory(file, destFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        MediaScannerConnection.scanFile(
                                context,
                                new String[]{destPath + files.getFilename()},
                                new String[]{"*/*"},
                                new MediaScannerConnection.MediaScannerConnectionClient() {
                                    @Override
                                    public void onMediaScannerConnected() {

                                    }

                                    @Override
                                    public void onScanCompleted(String s, Uri uri) {

                                    }
                                }
                        );
                        Toast.makeText(context, "Saved to:"+destPath+files.getFilename(), Toast.LENGTH_SHORT).show();
                    }
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

    }

    private void checkFolder() {

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.SAVE_FOLDER_NAME;
        File dir = new File(path);

        boolean isDirectoryCreated = dir.exists();

        if (!isDirectoryCreated){
            isDirectoryCreated = dir.mkdir();
        }

        if (isDirectoryCreated){
            Log.d("Folder", "Already Created");
        }

    }

    @Override
    public int getItemCount() {
        return filesList.size();
    }

    public class StoryViewHolder extends RecyclerView.ViewHolder {

        ImageView playIcon, moreOptionsID, saveImage;

        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);

            saveImage = itemView.findViewById(R.id.mainImageView);
            playIcon = itemView.findViewById(R.id.playButtonImage);
            moreOptionsID = itemView.findViewById(R.id.moreOptionsID);
        }
    }
}
