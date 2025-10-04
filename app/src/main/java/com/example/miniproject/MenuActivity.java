package com.example.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    private Button btnToLogin, btnToRegister, btnQuit;

    private void goToLogin() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    private void goToRegister() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnToLogin = findViewById(R.id.btnToLogin);
        btnToRegister = findViewById(R.id.btnToRegister);
        btnQuit = findViewById(R.id.btnQuit);

        btnToLogin.setOnClickListener(v -> goToLogin());
        btnToRegister.setOnClickListener(v -> goToRegister());
        btnQuit.setOnClickListener(v -> goToRegister());
    }
}
