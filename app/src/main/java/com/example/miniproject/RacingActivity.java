package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.Random;

public class RacingActivity extends AppCompatActivity {
    ProgressBar pb1, pb2, pb3;
    TextView tvResult;
    Button btnStart;
    Handler handler = new Handler();
    Random random = new Random();
    int delayInterval = 50;

    int val1 = 0, val2 = 0, val3 = 0;
    boolean isRunning = false;

    private void startRace() {
        // Reset
        val1 = val2 = val3 = 0;
        pb1.setProgress(0);
        pb2.setProgress(0);
        pb3.setProgress(0);
        tvResult.setText("Đang chạy...");
        isRunning = true;

        handler.postDelayed(updateRunnable, delayInterval);
    }

    private Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isRunning) return;

            // Random tăng 1-3
            val1 += random.nextInt(3) + 1;
            val2 += random.nextInt(3) + 1;
            val3 += random.nextInt(3) + 1;

            pb1.setProgress(val1);
            pb2.setProgress(val2);
            pb3.setProgress(val3);

            // Kiểm tra điều kiện dừng
            if (val1 >= 100 || val2 >= 100 || val3 >= 100) {
                isRunning = false;
                String winner = "";
                if (val1 >= 100) winner += "Thanh 1 ";
                if (val2 >= 100) winner += "Thanh 2 ";
                if (val3 >= 100) winner += "Thanh 3 ";
                tvResult.setText("Hoàn thành: " + winner);
            } else {
                handler.postDelayed(this, delayInterval); // lặp lại sau 1 giây
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_racing);

        pb1 = findViewById(R.id.progressBar1);
        pb2 = findViewById(R.id.progressBar2);
        pb3 = findViewById(R.id.progressBar3);
        tvResult = findViewById(R.id.tvResult);
        btnStart = findViewById(R.id.btnStart);

        btnStart.setOnClickListener(v -> startRace());
    }
}
