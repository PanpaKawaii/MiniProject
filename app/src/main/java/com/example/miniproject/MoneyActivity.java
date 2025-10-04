package com.example.miniproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

            // Kiểm tra input rỗng
            if (bankAccount.isEmpty() || amountStr.isEmpty()) {
                Toast.makeText(MoneyActivity.this, "Please enter complete information", Toast.LENGTH_SHORT).show();
                MusicManager.playEffect(MoneyActivity.this, R.raw.errordeposit);
                return;
            }

            try {
                int amount = Integer.parseInt(amountStr);
                int currentBalance = accountManager.getMoney(username);
                long newBalance = (long) currentBalance + amount; // Sử dụng long để tránh tràn số

                // Kiểm tra số tiền vượt quá giới hạn
                if (newBalance > 999999999) {
                    Toast.makeText(MoneyActivity.this, "Deposit failed: Balance exceeds 999,999,999", Toast.LENGTH_SHORT).show();
                    MusicManager.playEffect(MoneyActivity.this, R.raw.errordeposit);
                    return;
                }

                // Kiểm tra số tiền âm hoặc bằng 0
                if (amount <= 0) {
                    Toast.makeText(MoneyActivity.this, "Deposit failed: Amount must be greater than 0", Toast.LENGTH_SHORT).show();
                    MusicManager.playEffect(MoneyActivity.this, R.raw.errordeposit);
                    return;
                }

                // Cập nhật số dư nếu hợp lệ
                MusicManager.playEffect(MoneyActivity.this, R.raw.upmoney);
                tvBalance.setText("$" + accountManager.updateBalance(username, (int) newBalance));
            } catch (NumberFormatException e) {
                // Xử lý khi nhập không phải số
                Toast.makeText(MoneyActivity.this, "Deposit failed", Toast.LENGTH_SHORT).show();
                MusicManager.playEffect(MoneyActivity.this, R.raw.errordeposit);
            }
        });
    }
}