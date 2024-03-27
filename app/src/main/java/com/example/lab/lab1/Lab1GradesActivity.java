package com.example.lab.lab1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;

import com.example.lab.R;
import com.example.lab.addToolbar;

import java.util.ArrayList;
import java.util.Optional;

public class Lab1GradesActivity extends AppCompatActivity implements addToolbar {

    Toolbar toolbar;
    ArrayList<Grade> gradesList;
    RecyclerView gradesListView;
    Button calculateMeanButton;

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

    private void setGradesList() {
        String gradeCount = getIntent().getStringExtra("gradeCount");
        String[] subjects = getResources().getStringArray(R.array.lab1_subjects);
        if(gradeCount != null && !gradeCount.isEmpty()) {
            for(int i = 0; i < Integer.parseInt(gradeCount); i++) {
                this.gradesList.add(new Grade(subjects[i], 2));
            }
        } else {
            for(String subject : subjects) {
                this.gradesList.add(new Grade(subject, 2));
            }
        }
    }

    private void setAdapter() {
        RecyclerAdapter adapter = new RecyclerAdapter(this.gradesList);
        gradesListView.setAdapter(adapter);
        gradesListView.setLayoutManager(new LinearLayoutManager(this));
    }

    private String getMeanFromGrades() {
        float sum = 0;
        for(Grade grade : this.gradesList) {
            sum += grade.getGrade();
        }
        float mean = sum / this.gradesList.size();
        return String.format("%.2f", mean);
    }

    private void setCalculateMeanButtonListener() {
        calculateMeanButton.setOnClickListener(v -> {
            setResult(RESULT_OK, new Intent().putExtra("Mean", getMeanFromGrades()));
            finish();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_lab1_grades);
        }
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_lab1_grades_horizontal);
        }


        toolbar = findViewById(R.id.grades_toolbar);
        setToolbar(toolbar, Optional.empty());

        gradesListView = findViewById(R.id.subjectsList);
        gradesList = new ArrayList<>();
        setGradesList();
        setAdapter();

        calculateMeanButton = findViewById(R.id.calculateMeanButton);
        setCalculateMeanButtonListener();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }
}