package com.example.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    // Views
    private EditText etUsername;
    private EditText etPassword;
    private TextView tvNotAccountYet;
    private Button btnSignIn;

    // Notify
    private final String REQUIRE = "Require";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Reference from layout
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        tvNotAccountYet = findViewById(R.id.tvNotAccountYet);
        btnSignIn = findViewById(R.id.btnSignIn);

        // Register event
        tvNotAccountYet.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnSignIn) {
            // Sign In action
            signIn();
        } else if (id == R.id.tvNotAccountYet) {
            signUpForm();
        }
    }

    private boolean checkInput() {
        // Username
        if (TextUtils.isEmpty(etUsername.getText().toString())) {
            etUsername.setError(REQUIRE);
            return false;
        }

        // Password
        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            etPassword.setError(REQUIRE);
            return false;
        }

        // Valid
        return true;
    }

    private void signIn() {
        // Invalid
        if (!checkInput()) {
            // Play error sound
            MusicManager.playEffect(this, R.raw.errorlogin);
            return;
        }

        // Start MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // Close current activity
    }

    private void signUpForm() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }
}