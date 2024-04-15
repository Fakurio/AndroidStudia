package com.example.lab.lab5;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.lab.R;

import java.io.File;

public class ImagePreviewFragment extends Fragment {
    private String imagePath;
    private ImageView imagePreview;
    public ImagePreviewFragment(String imagePath) {
        this.imagePath = imagePath;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_preview, container, false);
        imagePreview = view.findViewById(R.id.imagePreview);
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        imagePreview.setImageBitmap(bitmap);
        return view;
    }
}