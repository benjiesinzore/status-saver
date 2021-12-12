package com.owilisinzole.statussaver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.owilisinzole.statussaver.R;

import java.io.File;
import java.util.List;

public class PdfAdapter extends RecyclerView.Adapter<PdfViewHolder> {

    private Context context;
    private List<File> pdfFiles;
    private OnPdfSelectListener listener;

    public PdfAdapter(Context context, List<File> pdfFiles, OnPdfSelectListener listener) {
        this.context = context;
        this.pdfFiles = pdfFiles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PdfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PdfViewHolder(LayoutInflater.from(context).inflate(R.layout.element_holder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PdfViewHolder holder, int position) {

        holder.textViewName.setText(pdfFiles.get(position).getName());
        holder.textViewName.setSelected(true);

        holder.cardViewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPdfSelected(pdfFiles.get(position));
                /*File filePdfAdapter = new File(pdfFiles.get(position).getPath());
                try {
                    if (filePdfAdapter.exists()){
                        Uri path = Uri.fromFile(filePdfAdapter);
                        Intent objIntent = new Intent(Intent.ACTION_WEB_SEARCH);
                        objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(objIntent);

                    }else {
                        Toast.makeText(context, "File does not exist.", Toast.LENGTH_SHORT).show();

                    }
                }catch (ActivityNotFoundException e){
                    Toast.makeText(context, "No Viewer Application Found.", Toast.LENGTH_SHORT).show();

                }catch (Exception e){
                    e.printStackTrace();
                }*/

            }
        });
    }

    @Override
    public int getItemCount() {
        return pdfFiles.size();
    }
}
