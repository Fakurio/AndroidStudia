package com.example.lab.lab5;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab.R;

public class SaveImageFragment extends Fragment {
    private TextView imageNameField;
    private Button saveImageButton;
    private SaveImageFragmentListener listener;

    private void setSaveImageButton() {
        saveImageButton.setOnClickListener(v -> {
            String fileName = imageNameField.getText().toString();
            if(fileName.isEmpty()) {
                Toast.makeText(getContext(), "Podaj nazwę obrazka", Toast.LENGTH_SHORT).show();
                return;
            }
            listener.onSaveImageButtonClick(imageNameField.getText().toString());
        });
    }
    public SaveImageFragment(SaveImageFragmentListener listener) {
        this.listener = listener;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save_image, container, false);
        imageNameField = view.findViewById(R.id.imageNameField);
        saveImageButton = view.findViewById(R.id.saveImageButton);
        setSaveImageButton();
        return view;
    }
}