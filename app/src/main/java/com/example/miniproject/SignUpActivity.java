package com.example.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
// Removed Toast import as it's no longer used for validation errors

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    // Views
    private EditText etUsername;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private TextView tvAlreadyAccount;
    private Button btnSignUp;
    private ImageButton btnBack;
    private TextView tvUsernameError;
    private TextView tvPasswordError;
    private TextView tvConfirmPasswordError;
    private TextView tvSignUpError;

    // Notify
    private final String REQUIRE = "This field is required";
    private final String PASSWORDS_DO_NOT_MATCH = "Passwords do not match";
    private final String USERNAME_EXISTS = "Username already exists";

    private AccountManager accountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Reference from Layout
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        tvAlreadyAccount = findViewById(R.id.tvAlreadyAccount);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvUsernameError = findViewById(R.id.tvUsernameError);
        tvPasswordError = findViewById(R.id.tvPasswordError);
        tvConfirmPasswordError = findViewById(R.id.tvConfirmPasswordError);
        tvSignUpError = findViewById(R.id.tvSignUpError);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        // Initialize AccountManager
        accountManager = AccountManager.getInstance(this);

        // Register event
        tvAlreadyAccount.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    private void clearErrorMessages() {
        tvUsernameError.setText(""); // Clear text before making it invisible
        tvUsernameError.setVisibility(View.INVISIBLE);
        tvPasswordError.setText(""); // Clear text
        tvPasswordError.setVisibility(View.INVISIBLE);
        tvConfirmPasswordError.setText(""); // Clear text
        tvConfirmPasswordError.setVisibility(View.INVISIBLE);
        tvSignUpError.setText(""); // Clear text
        tvSignUpError.setVisibility(View.INVISIBLE);
    }

    private boolean checkInput() {
        clearErrorMessages();
        boolean isValid = true;

        // Username validation
        if (TextUtils.isEmpty(etUsername.getText().toString())) {
            tvUsernameError.setText(REQUIRE);
            tvUsernameError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        // Password validation
        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            tvPasswordError.setText(REQUIRE);
            tvPasswordError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        // Confirm password validation
        if (TextUtils.isEmpty(etConfirmPassword.getText().toString())) {
            tvConfirmPasswordError.setText(REQUIRE);
            tvConfirmPasswordError.setVisibility(View.VISIBLE);
            isValid = false;
        } else if (!TextUtils.equals(etPassword.getText().toString(),
                etConfirmPassword.getText().toString())) {
            tvConfirmPasswordError.setText(PASSWORDS_DO_NOT_MATCH);
            tvConfirmPasswordError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        return isValid;
    }

    private void signUp() {
        if (!checkInput()) {
            MusicManager.playEffect(this, R.raw.errorlogin);
            return;
        }

        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if (accountManager.addUser(username, password)) {
            MusicManager.playEffect(this, R.raw.login);
            // Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show(); // Optional: keep for success
            // Go to Sign In screen after successful registration
            Intent intent = new Intent(this, SignInActivity.class);
            intent.putExtra("REGISTRATION_SUCCESSFUL", true); // Pass a flag for optional success message on SignIn screen
            startActivity(intent);
            finish();
        } else {
            MusicManager.playEffect(this, R.raw.errorlogin);
            tvSignUpError.setText(USERNAME_EXISTS);
            tvSignUpError.setVisibility(View.VISIBLE);
        }
    }

    private void signInForm() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnSignUp) {
            signUp();
        } else if (id == R.id.tvAlreadyAccount) {
            signInForm();
        }
    }
}
