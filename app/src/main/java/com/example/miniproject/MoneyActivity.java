package com.example.miniproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MoneyActivity extends AppCompatActivity {

    private TextView tvBalance;
    private EditText etBankAccount, etAmount;
    private Button btnSubmit;
    private AccountManager accountManager;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);

        MusicManager.stopBackground();

        // Init views
        tvBalance = findViewById(R.id.textViewBalance);
        etBankAccount = findViewById(R.id.editTextBankAccount);
        etAmount = findViewById(R.id.editTextAmount);
        btnSubmit = findViewById(R.id.buttonSubmit);
        btnBack = findViewById(R.id.btnBack);



        // Enable nút back trên Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Xử lý sự kiện back
        btnBack.setOnClickListener(v -> finish());


        accountManager = AccountManager.getInstance(this);
        String username = getIntent().getStringExtra("username");

        // Lấy balance trực tiếp từ AccountManager
        int balance = accountManager.getMoney(username);
        tvBalance.setText("$" + balance);

        btnSubmit.setOnClickListener(v -> {
            String bankAccount = etBankAccount.getText().toString().trim();
            String amountStr = etAmount.getText().toString().trim();

            if (bankAccount.isEmpty() || amountStr.isEmpty()) return;

            int amount = Integer.parseInt(amountStr);
            int newBalance = accountManager.getMoney(username) + amount;
            MusicManager.playEffect(MoneyActivity.this, R.raw.upmoney);
            tvBalance.setText("$" + accountManager.updateBalance(username, newBalance));
        });
    }
}
