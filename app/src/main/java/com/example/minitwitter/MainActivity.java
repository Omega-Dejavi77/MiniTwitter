package com.example.minitwitter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    /**Main Activity is the Login Activity**/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        Button btnLogin = findViewById(R.id.buttonLogin);
        btnLogin.setOnClickListener(v -> {

        });

        TextView tvGoSignUp = findViewById(R.id.textViewGoSignUp);
        tvGoSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
