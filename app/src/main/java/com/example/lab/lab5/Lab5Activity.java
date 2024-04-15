package com.example.lab.lab5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.lab.R;
import com.example.lab.addToolbar;
import com.example.lab.lab4.HttpConnectionService;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Optional;

public class Lab5Activity extends AppCompatActivity implements addToolbar, SaveImageFragmentListener,
    GalleryFragmentListener{
    private Toolbar toolbar;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 5;
    private String fileName;
    @Override
    public void setToolbar(Toolbar toolbar, Optional<String> title) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title.orElse(null));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            } else {
               finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab5);

        toolbar = findViewById(R.id.toolbar_lab5);
        setToolbar(toolbar, Optional.of("Lab 5"));

        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer,
                new PaintSurfaceFragment(), "paint_surface_fragment").commit();
    }

    @Override
    public void onSaveImageButtonClick(String fileName) {
        PaintSurfaceFragment fragment = (PaintSurfaceFragment) getSupportFragmentManager()
                .findFragmentByTag("paint_surface_fragment");
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            assert fragment != null;
            fragment.saveSurfaceView(fileName);

        } else {
            this.fileName = fileName;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onImageNameClick(String imageName) {
        String path = Environment.getExternalStorageDirectory() + File.separator + "Pictures/" + imageName;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new ImagePreviewFragment(path))
                .addToBackStack(null).commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                PaintSurfaceFragment fragment = (PaintSurfaceFragment) getSupportFragmentManager()
                        .findFragmentByTag("paint_surface_fragment");
                assert fragment != null;
                fragment.saveSurfaceView(this.fileName);
            } else {
                Toast.makeText(this, "Brak uprawnień do zapisania pliku", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lab5_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.saveImageOption) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new SaveImageFragment(this))
                    .addToBackStack(null).commit();
        }
        if(id == R.id.galleryOption) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new GalleryFragment(this))
                    .addToBackStack(null).commit();
        }
        if(id == R.id.paintOption) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new PaintSurfaceFragment()).addToBackStack(null).commit();
        }
        return super.onOptionsItemSelected(item);
    }
}