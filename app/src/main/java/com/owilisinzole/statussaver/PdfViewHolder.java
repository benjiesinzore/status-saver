package com.owilisinzole.statussaver;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.owilisinzole.statussaver.R;

public class PdfViewHolder extends RecyclerView.ViewHolder {

    public TextView textViewName;
    public CardView cardViewContainer;

    public PdfViewHolder(@NonNull View itemView) {
        super(itemView);

        textViewName = itemView.findViewById(R.id.textViewPDFName);
        cardViewContainer = itemView.findViewById(R.id.container);
    }
}
