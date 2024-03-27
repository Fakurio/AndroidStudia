package com.example.lab.lab3;

import static android.provider.CalendarContract.CalendarCache.URI;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lab.R;
import com.example.lab.addToolbar;
import com.example.lab.lab1.Lab1Activity;

import java.util.Optional;

public class Lab3PhoneForm extends AppCompatActivity implements addToolbar {
    private Toolbar toolbar;
    private EditText manufacturerField, modelField, versionField, websiteField;
    private Button websiteButton, saveButton, cancelButton;
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

    private void setPhoneFormField(EditText field, String errorMessage) {
        field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().isEmpty()) {
                    field.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    field.setError(errorMessage);
                } else {
                    field.setError(null);
                    field.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setWebsiteFormField(EditText field, String errorMessage) {
        field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String content = field.getText().toString();
                if (!content.startsWith("http://") && !content.startsWith("https://")) {
                    field.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    field.setError(errorMessage);
                } else {
                    field.setError(null);
                    field.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    private void setCancelButton() {
        cancelButton.setOnClickListener(v -> {
            finish();
        });
    }

    private boolean checkFormFields() {
        return manufacturerField.getError() == null && modelField.getError() == null
                && versionField.getError() == null && websiteField.getError() == null
                && !manufacturerField.getText().toString().isEmpty()
                && !modelField.getText().toString().isEmpty()
                && !versionField.getText().toString().isEmpty()
                && !websiteField.getText().toString().isEmpty();
    }
    private void setSaveButton() {
        saveButton.setOnClickListener(v -> {
            if(checkFormFields()) {
                Bundle phoneBundle = new Bundle();
                //w przypdaku aktualizacji telefonu z listy
                Bundle phoneToUpdateData = getIntent().getExtras();
                if(phoneToUpdateData != null) {
                    String id = phoneToUpdateData.getString("ID");
                    if(id != null) {
                        phoneBundle.putString("ID", getIntent().getExtras().getString("ID"));
                    }
                }
                phoneBundle.putString("manufacturer", manufacturerField.getText().toString());
                phoneBundle.putString("model", modelField.getText().toString());
                phoneBundle.putString("version", versionField.getText().toString());
                phoneBundle.putString("website", websiteField.getText().toString());
                setResult(RESULT_OK, new Intent().putExtras(phoneBundle));
                finish();
            } else {
                Toast.makeText(this, "Wszystkie pola formularza są wymagane", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setWebsiteButton() {
        websiteButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(websiteField.getText().toString()));
            startActivity(intent);
        });
    }

    private void fillFormField() {
        Bundle phoneBundle = getIntent().getExtras();
        if(phoneBundle != null) {
            manufacturerField.setText(phoneBundle.getString("manufacturer"));
            modelField.setText(phoneBundle.getString("model"));
            versionField.setText(phoneBundle.getString("version"));
            websiteField.setText(phoneBundle.getString("website"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab3_phone_form);

        toolbar = findViewById(R.id.lab3_phone_form_toolbar);
        setToolbar(toolbar, Optional.of("Phone DB"));

        manufacturerField = findViewById(R.id.manufacturerField);
        modelField = findViewById(R.id.modelField);
        versionField = findViewById(R.id.versionField);
        websiteField = findViewById(R.id.websiteField);
        setPhoneFormField(manufacturerField, "Producent nie może być pusty");
        setPhoneFormField(modelField, "Model nie może być pusty");
        setPhoneFormField(versionField, "Wersja androida nie może być pusta");
        setWebsiteFormField(websiteField, "Nieprawidłowy adres strony");

        websiteButton = findViewById(R.id.phoneFormWebsiteButton);
        saveButton = findViewById(R.id.phoneFormSaveButton);
        cancelButton = findViewById(R.id.phoneFormCancelButton);
        setCancelButton();
        setSaveButton();
        setWebsiteButton();

        fillFormField();
    }
}