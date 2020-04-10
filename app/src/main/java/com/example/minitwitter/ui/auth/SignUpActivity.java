package com.example.minitwitter.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.minitwitter.R;
import com.example.minitwitter.common.Constants;
import com.example.minitwitter.common.SharedPreferencesManager;
import com.example.minitwitter.retrofit.MiniTwitterClient;
import com.example.minitwitter.retrofit.MiniTwitterService;
import com.example.minitwitter.retrofit.request.RequestSignUp;
import com.example.minitwitter.retrofit.response.ResponseAuth;
import com.example.minitwitter.ui.DashboardActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private EditText etUsername, etEmail;
    private EditText etPassword;
    private Button btnSignUp;
    private TextView tvGoLogin;
    private MiniTwitterClient miniTwitterClient;
    private MiniTwitterService miniTwitterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        retrofitInit();
        findViews();
        events();
    }

    private void retrofitInit() {
        miniTwitterClient = MiniTwitterClient.getInstance();
        miniTwitterService = miniTwitterClient.getMiniTwitterService();
    }

    private void findViews() {
        etUsername = findViewById(R.id.editTextUsername);
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
        btnSignUp = findViewById(R.id.buttonSignUp);
        tvGoLogin = findViewById(R.id.textViewGoLogin);
    }
    
    private void events() {
        btnSignUp.setOnClickListener(v -> signUp());

        tvGoLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    private void signUp() {
        String username = etUsername.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        if (username.isEmpty())
            etUsername.setError(etUsername.getHint() + " is required");

        else if (email.isEmpty())
            etEmail.setError(etEmail.getHint() + " is required");

        else if (password.isEmpty())
            etPassword.setError(etPassword.getHint() + " is required");

        else if (password.length() < 4)
            etPassword.setError(etPassword.getHint() + " minimum 4 characters");

        else {
            String code = "UDEMYANDROID";
            RequestSignUp requestSignUp = new RequestSignUp(username, email, password, code);
            Call<ResponseAuth> call = miniTwitterService.signUp(requestSignUp);

            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if (response.isSuccessful()) {
                        SharedPreferencesManager.writeStringValue(Constants.PREF_TOKEN, response.body().getToke());
                        SharedPreferencesManager.writeStringValue(Constants.PREF_USERNAME, response.body().getUsername());
                        SharedPreferencesManager.writeStringValue(Constants.PREF_EMAIL, response.body().getEmail());
                        SharedPreferencesManager.writeStringValue(Constants.PREF_PHOTO_URL, response.body().getPhotoUrl());
                        SharedPreferencesManager.writeStringValue(Constants.PREF_CREATED, response.body().getCreated());
                        SharedPreferencesManager.writeBooleanValue(Constants.PREF_ACTIVE, response.body().isActive());
                        startActivity(new Intent(SignUpActivity.this, DashboardActivity.class));
                        finish();
                    }
                    else
                        Toast.makeText(SignUpActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(SignUpActivity.this, "Conection problems. Please try again", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
