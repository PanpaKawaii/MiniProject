package com.example.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast; // Keep Toast for registration success message

import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    // Views
    private EditText etUsername;
    private EditText etPassword;
    private TextView tvNotAccountYet;
    private Button btnSignIn;
    private TextView tvUsernameErrorSignIn;
    private TextView tvPasswordErrorSignIn;
    private TextView tvSignInError;

    // Notify
    private final String REQUIRE = "This field is required";
    private final String INVALID_CREDENTIALS = "Invalid username or password";
    private AccountManager accountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Reference from layout
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        tvNotAccountYet = findViewById(R.id.tvNotAccountYet);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvUsernameErrorSignIn = findViewById(R.id.tvUsernameErrorSignIn);
        tvPasswordErrorSignIn = findViewById(R.id.tvPasswordErrorSignIn);
        tvSignInError = findViewById(R.id.tvSignInError);

        // Initialize AccountManager
        accountManager = AccountManager.getInstance(this);

        // Register event
        tvNotAccountYet.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);

        // Check for registration success message
        if (getIntent().getBooleanExtra("REGISTRATION_SUCCESSFUL", false)) {
            Toast.makeText(this, "Registration successful! Please sign in.", Toast.LENGTH_LONG).show();
        }
    }

    private void clearErrorMessages() {
        tvUsernameErrorSignIn.setText(""); // Clear text before making it invisible
        tvUsernameErrorSignIn.setVisibility(View.INVISIBLE);
        tvPasswordErrorSignIn.setText(""); // Clear text
        tvPasswordErrorSignIn.setVisibility(View.INVISIBLE);
        tvSignInError.setText(""); // Clear text
        tvSignInError.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnSignIn) {
            signIn();
        } else if (id == R.id.tvNotAccountYet) {
            signUpForm();
        }
    }

    private boolean checkInput() {
        clearErrorMessages();
        boolean isValid = true;

        // Username
        if (TextUtils.isEmpty(etUsername.getText().toString())) {
            tvUsernameErrorSignIn.setText(REQUIRE);
            tvUsernameErrorSignIn.setVisibility(View.VISIBLE);
            isValid = false;
        }

        // Password
        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            tvPasswordErrorSignIn.setText(REQUIRE);
            tvPasswordErrorSignIn.setVisibility(View.VISIBLE);
            isValid = false;
        }

        return isValid;
    }

    private void signIn() {
        if (!checkInput()) {
            MusicManager.playEffect(this, R.raw.errorlogin);
            return;
        }

        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if (accountManager.isValidUser(username, password)) {
            int balance = accountManager.getMoney(username);
            MusicManager.playEffect(this, R.raw.login);
            // Toast.makeText(this, "Sign In Successful!", Toast.LENGTH_SHORT).show(); // Optional: keep for success
            // Start RacingActivity
            Intent intent = new Intent(this, RacingActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("balance", balance);
            startActivity(intent);
            finish();
        } else {
            MusicManager.playEffect(this, R.raw.errorlogin);
            tvSignInError.setText(INVALID_CREDENTIALS);
            tvSignInError.setVisibility(View.VISIBLE);
        }
    }

    private void signUpForm() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }
}
