package com.example.lab;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.lab.lab1.Lab1Activity;
import com.example.lab.lab3.Lab3Activity;
import com.example.lab.lab4.Lab4Activity;
import com.example.lab.lab5.Lab5Activity;

public class MainActivity extends AppCompatActivity {

    Button lab1Button, lab3Button, lab4Button, lab5Button;
    private static final int LAB1_CODE = 10;
    private void setButtonListener() {
        lab1Button.setOnClickListener(v -> {
            startActivityForResult(new Intent(this, Lab1Activity.class), LAB1_CODE);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lab1Button = findViewById(R.id.lab1Button);
        setButtonListener();

        lab3Button = findViewById(R.id.lab3Button);
        lab3Button.setOnClickListener(v -> {
            startActivity(new Intent(this, Lab3Activity.class));
        });

        lab4Button = findViewById(R.id.lab4Button);
        lab4Button.setOnClickListener(v -> {
            startActivity(new Intent(this, Lab4Activity.class));
        });

        lab5Button = findViewById(R.id.lab5Button);
        lab5Button.setOnClickListener(v -> {
            startActivity(new Intent(this, Lab5Activity.class));
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == LAB1_CODE) {
            if(resultCode == RESULT_OK) {
                if(data != null) {
                    Toast.makeText(this, data.getStringExtra("lab1_result"),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}