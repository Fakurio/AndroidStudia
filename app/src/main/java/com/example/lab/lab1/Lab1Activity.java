package com.example.lab.lab1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab.R;
import com.example.lab.addToolbar;

import java.util.Optional;

public class Lab1Activity extends AppCompatActivity implements addToolbar {
    EditText name, surname, gradeCount;
    TextView meanGrade;
    Button gradeButton, returnToMenuButton;
    Toolbar toolbar;
    private static final int GET_MEAN_GRADE_REQUEST = 20;

    private void setTextInputListener(EditText input, String errorMessage) {
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().isEmpty()) {
                    input.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    input.setError(errorMessage);
                    Toast.makeText(Lab1Activity.this, errorMessage, Toast.LENGTH_SHORT).show();
                } else {
                    input.setError(null);
                    input.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                }
                gradesButtonToggle();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void setGradeInputListener(EditText input) {
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String content = charSequence.toString().trim();
                if(!content.isEmpty()) {
                    try {
                        int count = Integer.parseInt(content);
                        if (count < 5 || count > 15) {
                            input.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                            input.setError("Nieprawidłowa liczba ocen");
                            Toast.makeText(Lab1Activity.this, "Nieprawidłowa liczba ocen",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            input.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                            input.setError(null);
                        }
                    } catch (NumberFormatException e) {
                        input.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        input.setError("Nieprawidłowa wartość");
                        Toast.makeText(Lab1Activity.this, "Nieprawidłowa wartość",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    input.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                    input.setError(null);
                }
                gradesButtonToggle();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void setGradesButtonListener(Button button) {
        button.setOnClickListener(v -> {
            startActivityForResult(new Intent(this, Lab1GradesActivity.class)
                    .putExtra("gradeCount", gradeCount.getText().toString()), GET_MEAN_GRADE_REQUEST);
        });
    }
    private void gradesButtonToggle() {
        if (name.getError() == null && !name.getText().toString().isEmpty()
                && surname.getError() == null && !surname.getText().toString().isEmpty()
                && gradeCount.getError() == null) {
            gradeButton.setVisibility(View.VISIBLE);
        } else {
            gradeButton.setVisibility(View.GONE);
        }
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
        setContentView(R.layout.activity_lab1);

        toolbar = findViewById(R.id.toolbar);
        name = findViewById(R.id.nameInput);
        surname = findViewById(R.id.surnameInput);
        gradeCount = findViewById(R.id.gradesInput);
        gradeButton = findViewById(R.id.gradesButton);
        meanGrade = findViewById(R.id.meanGrade);
        returnToMenuButton = findViewById(R.id.returnToMenu);

        setToolbar(toolbar, Optional.of("Lab 1"));
        setTextInputListener(name, "Imie nie może być puste");
        setTextInputListener(surname, "Nazwisko nie może być puste");
        setGradeInputListener(gradeCount);
        gradesButtonToggle();
        setGradesButtonListener(gradeButton);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_MEAN_GRADE_REQUEST) {
            if (resultCode == RESULT_OK) {
                if(data != null) {
                    String meanGradeRawValue = data.getStringExtra("Mean");

                    if(meanGradeRawValue != null) {
                        float meanGradeValue = Float.parseFloat(meanGradeRawValue);
                        if(meanGradeValue < 3.0) {
                            returnToMenuButton.setText(getResources()
                                    .getString(R.string.lab1_mean_grade_result_bad));
                        } else {
                            returnToMenuButton.setText(getResources()
                                    .getString(R.string.lab1_mean_grade_result_good));
                        }

                        returnToMenuButton.setOnClickListener(v -> {
                            if(meanGradeValue < 3.0) {
                                setResult(RESULT_OK, new Intent().putExtra("lab1_result",
                                        getResources().getString(R.string.lab1_result_bad)));
                            } else {
                                setResult(RESULT_OK, new Intent().putExtra("lab1_result",
                                        getResources().getString(R.string.lab1_result_good)));
                            }
                            finish();
                        });
                    }
                    meanGrade.setText("Twoja średnia: " + meanGradeRawValue);

                    meanGrade.setVisibility(View.VISIBLE);
                    returnToMenuButton.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}