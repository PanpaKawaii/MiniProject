package com.example.miniproject;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    private Button btnToLogin, btnToRegister, btnQuit;
    private ImageButton btnGuide;

    private void goToLogin() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    private void goToRegister() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void showGuidePopup() {
        // Inflate layout popup_guide.xml
        View popupView = getLayoutInflater().inflate(R.layout.popup_guide, null);

        // Find close buttons
        Button btnCloseGuide = popupView.findViewById(R.id.btnCloseGuide);
        ImageButton btnCloseGuideX = popupView.findViewById(R.id.btnCloseGuideX);

        // Build dialog with transparent background
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(popupView)
                .setCancelable(true)
                .create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.show();

        // Handle close button clicks
        btnCloseGuide.setOnClickListener(v -> dialog.dismiss());
        btnCloseGuideX.setOnClickListener(v -> dialog.dismiss());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnToLogin = findViewById(R.id.btnToLogin);
        btnToRegister = findViewById(R.id.btnToRegister);
        btnQuit = findViewById(R.id.btnQuit);
        btnGuide = findViewById(R.id.btnGuide);

        btnToLogin.setOnClickListener(v -> goToLogin());
        btnToRegister.setOnClickListener(v -> goToRegister());
        btnQuit.setOnClickListener(v -> finish());
        btnGuide.setOnClickListener(v -> showGuidePopup());
    }
}