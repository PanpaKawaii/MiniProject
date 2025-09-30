package com.example.miniproject;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class MoneyActivity extends AppCompatActivity {

    private TextView tvBalance;
    private EditText etBankAccount, etAmount;
    private Button btnSubmit;
    private AccountManager accountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);

        tvBalance = findViewById(R.id.textViewBalance);
        etBankAccount = findViewById(R.id.editTextBankAccount);
        etAmount = findViewById(R.id.editTextAmount);
        btnSubmit = findViewById(R.id.buttonSubmit);

        accountManager = AccountManager.getInstance(this);

        String username = getIntent().getStringExtra("username");
        int balance = getIntent().getIntExtra("balance", 0);
        tvBalance.setText("$" + balance);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bankAccount = etBankAccount.getText().toString().trim();
                String amountStr = etAmount.getText().toString().trim();

                if (bankAccount.isEmpty() || amountStr.isEmpty()) {
                    return;
                }

                int amount = Integer.parseInt(amountStr);
                int newBalance = accountManager.getMoney(username) + amount;
                tvBalance.setText("$" + accountManager.updateBalance(username, newBalance));
            }
        });
    }
}
