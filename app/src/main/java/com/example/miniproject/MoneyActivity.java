package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MoneyActivity extends AppCompatActivity {

    // Khai báo các view
    private TextView tvBalance, tvResult;
    private EditText etAccount, etName, etAmount;
    private Button btnSubmit;

    private int balance = 100; // số tiền gốc

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money); // gắn layout (đặt tên file xml là activity_money.xml)

        // Ánh xạ view từ XML
        tvBalance = findViewById(R.id.textViewBalance);
        etAccount = findViewById(R.id.EditTextAccount);
        etName = findViewById(R.id.EditTextName);
        etAmount = findViewById(R.id.EditTextAmount);
        btnSubmit = findViewById(R.id.buttonSubmit);
        tvResult = findViewById(R.id.tvResult);

        // Set số tiền gốc ban đầu
        tvBalance.setText("Số tiền gốc: $" + balance);

        // Xử lý khi bấm nút
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = etAccount.getText().toString().trim();
                String name = etName.getText().toString().trim();
                String amountStr = etAmount.getText().toString().trim();

                if (account.isEmpty() || name.isEmpty() || amountStr.isEmpty()) {
                    tvResult.setText("Vui lòng nhập đầy đủ thông tin!");
                    return;
                }

                int amount = Integer.parseInt(amountStr);


                    balance += amount;
                    tvBalance.setText("Số tiền gốc: $" + balance);
                    tvResult.setText("Chuyển $" + amount + " cho " + name + " (TK: " + account + ") thành công!");

            }
        });
    }
}
