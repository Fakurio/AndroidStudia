package com.example.lab.lab5;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab.R;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {
    private List<String> imageNames;
    private GalleryFragmentListener listener;

    public GalleryAdapter(List<String> imageNames, GalleryFragmentListener listener) {
        this.imageNames = imageNames;
        this.listener = listener;
    }

    public static class GalleryViewHolder extends RecyclerView.ViewHolder {
        private TextView imageName;
        private GalleryFragmentListener listener;
        public GalleryViewHolder(@NonNull View itemView, GalleryFragmentListener listener) {
            super(itemView);
            imageName = itemView.findViewById(R.id.imageName);
            this.listener = listener;
            imageName.setOnClickListener(v -> listener.onImageNameClick(imageName.getText().toString()));
        }
    }

    @NonNull
    @Override
    public GalleryAdapter.GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.gallery_item, parent, false);
        return new GalleryAdapter.GalleryViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryAdapter.GalleryViewHolder holder, int position) {
        holder.imageName.setText(imageNames.get(position));
    }

    @Override
    public int getItemCount() {
        return imageNames.size();
    }


}
