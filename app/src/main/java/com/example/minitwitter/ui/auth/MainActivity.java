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
import com.example.minitwitter.retrofit.request.RequestLogin;
import com.example.minitwitter.retrofit.response.ResponseAuth;
import com.example.minitwitter.ui.DashboardActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    /**Main Activity is the Login Activity**/
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvGoSignUp;

    private MiniTwitterClient miniTwitterClient;
    private MiniTwitterService miniTwitterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.buttonLogin);
        tvGoSignUp = findViewById(R.id.textViewGoSignUp);
    }

    private void events() {
        btnLogin.setOnClickListener(v -> login());

        tvGoSignUp.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUpActivity.class));
            finish();
        });
    }

    private void login() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        if (email.isEmpty())
            etEmail.setError("Email is required");

        else if (password.isEmpty())
            etPassword.setError("Password is required");

        else {
            RequestLogin requestLogin = new RequestLogin(email, password);
            Call<ResponseAuth> call = miniTwitterService.login(requestLogin);
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
                        startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                        finish();
                    }
                    else
                        Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Conection problems. Please try again", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
