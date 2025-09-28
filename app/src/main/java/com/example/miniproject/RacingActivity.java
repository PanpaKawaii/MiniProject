package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.Random;

public class RacingActivity extends AppCompatActivity {
    ProgressBar pb1, pb2, pb3;
    TextView tvResult;
    Button btnStart;
    ImageView imgDuck1, imgDuck2, imgDuck3;

    Handler handler = new Handler();
    Random random = new Random();
    private static final int UPDATE_INTERVAL_MS = 50;

    int val1 = 0, val2 = 0, val3 = 0;
    boolean isRunning = false;

    // Toggle để đổi ảnh tạo hiệu ứng chạy
    boolean toggle1 = false, toggle2 = false, toggle3 = false;

    private void startRace() {
        // Reset
        val1 = val2 = val3 = 0;
        pb1.setProgress(0);
        pb2.setProgress(0);
        pb3.setProgress(0);
        tvResult.setText("Đang chuẩn bị...");
        isRunning = false;

        // Reset vị trí vịt
        imgDuck1.setX(0);
        imgDuck2.setX(0);
        imgDuck3.setX(0);

        // Dừng nhạc nền chờ
        MusicManager.stopBackground();

        // Phát sound effect "Ready"
        MusicManager.playEffect(this, R.raw.readytorace);

        // Delay 2s trước khi bắt đầu cuộc đua
        handler.postDelayed(() -> {
            // Phát nhạc nền race
            MusicManager.playBackground(this, R.raw.backgoundrace);

            // Bắt đầu chạy
            isRunning = true;
            tvResult.setText("Đang chạy...");
            handler.postDelayed(updateRunnable, UPDATE_INTERVAL_MS);
        }, 5000); // 5 giây
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

            // Di chuyển và đổi ảnh vịt
            moveDuck(imgDuck1, pb1, val1, 1);
            moveDuck(imgDuck2, pb2, val2, 2);
            moveDuck(imgDuck3, pb3, val3, 3);

            // Kiểm tra điều kiện dừng
            int max1 = pb1.getMax();
            int max2 = pb2.getMax();
            int max3 = pb3.getMax();

            if (val1 >= max1 || val2 >= max2 || val3 >= max3) {
                isRunning = false;
                String winner = "";
                if (val1 >= max1) winner += "Thanh 1 ";
                if (val2 >= max2) winner += "Thanh 2 ";
                if (val3 >= max3) winner += "Thanh 3 ";
                tvResult.setText("Hoàn thành: " + winner);
            } else {
                handler.postDelayed(this, UPDATE_INTERVAL_MS);
            }
        }
    };

    // Di chuyển vịt và đổi ảnh để tạo animation
    private void moveDuck(ImageView duck, ProgressBar pb, int progress, int duckIndex) {
        int pbWidth = pb.getWidth();
        int duckWidth = duck.getWidth();
        float ratio = progress / (float) pb.getMax();
        float newX = ratio * (pbWidth - duckWidth);
        duck.setX(newX);

        // Luân phiên đổi ảnh duck1 <-> duck2
        switch (duckIndex) {
            case 1:
                toggle1 = !toggle1;
                duck.setImageResource(toggle1 ? R.drawable.duck1 : R.drawable.duck2);
                break;
            case 2:
                toggle2 = !toggle2;
                duck.setImageResource(toggle2 ? R.drawable.duck1 : R.drawable.duck2);
                break;
            case 3:
                toggle3 = !toggle3;
                duck.setImageResource(toggle3 ? R.drawable.duck1 : R.drawable.duck2);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MusicManager.playBackground(this, R.raw.backgoundwait);

        setContentView(R.layout.activity_racing);

        // ProgressBar
        pb1 = findViewById(R.id.progressBar1);
        pb2 = findViewById(R.id.progressBar2);
        pb3 = findViewById(R.id.progressBar3);

        // TextView + Button
        tvResult = findViewById(R.id.tvResult);
        btnStart = findViewById(R.id.btnStart);

        // ImageView vịt
        imgDuck1 = findViewById(R.id.imgDuck1);
        imgDuck2 = findViewById(R.id.imgDuck2);
        imgDuck3 = findViewById(R.id.imgDuck3);

        btnStart.setOnClickListener(v -> startRace());
    }
}
