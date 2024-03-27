package com.example.lab.lab4;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.widget.Toast;

import com.example.lab.R;
import com.example.lab.addToolbar;

import java.util.Optional;

public class Lab4Activity extends AppCompatActivity implements addToolbar, HttpConnectionListener {
    private Toolbar toolbar;
    private EditText websiteUrlField;
    private TextView fileSizeValue, fileTypeValue;
    private Button downloadInfoButton, downloadFileButton;
    private HttpConnectionTask http;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 4;


    private void setDownloadInfoButton() {
        downloadInfoButton.setOnClickListener(v -> {
            http = new HttpConnectionTask(this);
            http.execute(websiteUrlField.getText().toString());
        });
    }

    private void setDownloadFileButton() {
        downloadFileButton.setOnClickListener(v -> {
            if(ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                HttpConnectionService.startService(this, websiteUrlField.getText().toString());
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        });
    }

    @Override
    public void setToolbar(Toolbar toolbar, Optional<String> title) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title.orElse(null));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab4);

        toolbar = findViewById(R.id.toolbar_lab4);
        setToolbar(toolbar, Optional.of("Lab 4"));

        websiteUrlField = findViewById(R.id.websiteUrlField);
        fileSizeValue = findViewById(R.id.fileSizeValue);
        fileTypeValue = findViewById(R.id.fileTypeValue);

        downloadInfoButton = findViewById(R.id.downloadInfoButton);
        setDownloadInfoButton();
        downloadFileButton = findViewById(R.id.downloadFileButton);
        setDownloadFileButton();

    }

    @Override
    public void beforeDownloadInfoStart() {
        fileSizeValue.setText("Loading...");
        fileTypeValue.setText("Loading...");
    }

    @Override
    public void onDownloadInfoComplete(FileInfo result) {
        fileTypeValue.setText(result.getType());
        int size = (result.getSize() / 1024) / 1024;
        fileSizeValue.setText(size + "MB");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                HttpConnectionService.startService(this, websiteUrlField.getText().toString());
            } else {
                Toast.makeText(this, "Brak uprawnień do pobrania pliku", Toast.LENGTH_LONG).show();
            }
        }
    }
}