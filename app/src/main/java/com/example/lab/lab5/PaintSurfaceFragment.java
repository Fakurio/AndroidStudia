package com.example.lab.lab5;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.lab.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PaintSurfaceFragment extends Fragment {
    private SurfaceView surfaceViewLayout;
    private PaintSurfaceView paintSurfaceView;
    private AppCompatButton redButton, blueButton, yellowButton, greenButton;
    private Button clearButton;
    public PaintSurfaceFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_paint_surface, container, false);
        surfaceViewLayout = view.findViewById(R.id.lab5SurfaceView);
        paintSurfaceView = new PaintSurfaceView(getContext(), surfaceViewLayout);

        redButton = view.findViewById(R.id.redButton);
        blueButton = view.findViewById(R.id.blueButton);
        yellowButton = view.findViewById(R.id.yellowButton);
        greenButton = view.findViewById(R.id.greenButton);
        clearButton = view.findViewById(R.id.clearSurfaceButton);
        setChangeColorButton();
        setClearButton();

        return view;
    }

    private void setChangeColorButton() {
        Context applicationContext = requireContext().getApplicationContext();
        redButton.setOnClickListener(v -> {
            paintSurfaceView.changeDrawingColor(ContextCompat.getColor(applicationContext, R.color.red));
        });
        blueButton.setOnClickListener(v -> {
            paintSurfaceView.changeDrawingColor(ContextCompat.getColor(applicationContext, R.color.blue));
        });
        yellowButton.setOnClickListener(v -> {
            paintSurfaceView.changeDrawingColor(ContextCompat.getColor(applicationContext, R.color.yellow));
        });
        greenButton.setOnClickListener(v -> {
            paintSurfaceView.changeDrawingColor(ContextCompat.getColor(applicationContext, R.color.green));
        });
    }

    public void saveSurfaceView(String fileName) {
        Bitmap bitmap = paintSurfaceView.getMyBitmap();
        try {
            File outputFile = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "Pictures/" + fileName + ".png");
            if(outputFile.exists()) {
                Toast.makeText(getContext(), "Obrazek z podaną nazwą istnieje", Toast.LENGTH_LONG).show();
                return;
            }
            FileOutputStream outputStream = new FileOutputStream(outputFile.getPath());
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Toast.makeText(getContext(), "Obrazek zapisany pomyślnie", Toast.LENGTH_SHORT).show();
        }

    }

    private void setClearButton() {
        clearButton.setOnClickListener(v -> {
            paintSurfaceView.clearSurface();
        });
    }
}