package com.example.lab.lab5;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lab.R;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private RecyclerView galleryRecyclewView;
    private GalleryAdapter galleryAdapter;
    private List<String> imageNames;
    private GalleryFragmentListener listener;
    public GalleryFragment(GalleryFragmentListener listener) {
        this.listener = listener;
    }

    private void loadImageNames() {
        imageNames = new ArrayList<>();
        File[] files = new File(Environment
                .getExternalStorageDirectory() + File.separator + "Pictures/").listFiles();
        for(File f : files) {
            if(!f.isHidden()) {
                imageNames.add(f.getName());
            }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        galleryRecyclewView = view.findViewById(R.id.gallery);
        loadImageNames();
        galleryAdapter = new GalleryAdapter(imageNames, this.listener);
        galleryRecyclewView.setAdapter(galleryAdapter);
        galleryRecyclewView.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;
    }
}