package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RacingActivity extends AppCompatActivity {
    ProgressBar pb1, pb2, pb3;
    TextView tvResult;
    Button btnStart;
    ImageView imgDuck1, imgDuck2, imgDuck3;

    Handler handler = new Handler();
    Random random = new Random();
    private static final int UPDATE_INTERVAL_MS = 50;
    private static final int WIN_THRESHOLD = 96; // Ngưỡng chiến thắng
    private static final int MAX_PROGRESS = 100;

    int val1 = 0, val2 = 0, val3 = 0;
    boolean isRunning = false;
    boolean winnerDetermined = false;

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
        winnerDetermined = false;

        handler.postDelayed(updateRunnable, UPDATE_INTERVAL_MS);
        // 1. Dừng nhạc nền chờ
        MusicManager.stopBackground();

        // 2. Phát sound effect "Ready"
        MusicManager.playEffect(this, R.raw.readytorace);

        // 3. Delay 5 giây rồi mới bắt đầu nhạc race + chạy thanh progress
        handler.postDelayed(() -> {
            // 3.1 Phát nhạc nền cuộc đua
            MusicManager.playBackground(this, R.raw.backgoundrace);

            // 3.2 Bắt đầu chạy
            isRunning = true;
            handler.postDelayed(updateRunnable, UPDATE_INTERVAL_MS);
            tvResult.setText("Đang chạy...");
        }, 5000);

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
            if (!isRunning || winnerDetermined) return;

            // Random tăng 1-3
            val1 += random.nextInt(3) + 1;
            val2 += random.nextInt(3) + 1;
            val3 += random.nextInt(3) + 1;

            // Đảm bảo không vượt quá 100
            val1 = Math.min(val1, MAX_PROGRESS);
            val2 = Math.min(val2, MAX_PROGRESS);
            val3 = Math.min(val3, MAX_PROGRESS);

            pb1.setProgress(val1);
            pb2.setProgress(val2);
            pb3.setProgress(val3);

            // Kiểm tra xem có ai đạt ngưỡng chiến thắng chưa
            checkForWinner();
            // Di chuyển và đổi ảnh vịt
            moveDuck(imgDuck1, pb1, val1, 1);
            moveDuck(imgDuck2, pb2, val2, 2);
            moveDuck(imgDuck3, pb3, val3, 3);

            // Kiểm tra điều kiện dừng
            int max1 = pb1.getMax();
            int max2 = pb2.getMax();
            int max3 = pb3.getMax();

            if (!winnerDetermined) {
                handler.postDelayed(this, UPDATE_INTERVAL_MS);
            }
        }
    };

    private void checkForWinner() {
        // Kiểm tra nếu đã có người chiến thắng
        if (winnerDetermined) return;

        List<Integer> potentialWinners = new ArrayList<>();

        // Kiểm tra các đối tượng đạt >= 96 điểm
        if (val1 >= WIN_THRESHOLD) potentialWinners.add(1);
        if (val2 >= WIN_THRESHOLD) potentialWinners.add(2);
        if (val3 >= WIN_THRESHOLD) potentialWinners.add(3);

        // Nếu có ít nhất 1 đối tượng đạt ngưỡng
        if (!potentialWinners.isEmpty()) {
            determineFinalWinner(potentialWinners);
        } else {
            // Kiểm tra điều kiện chiến thắng thông thường (về đích 100 điểm)
            checkNormalWinCondition();
        }
    }

    private void determineFinalWinner(List<Integer> potentialWinners) {
        winnerDetermined = true;
        isRunning = false;

        // Nếu chỉ có 1 người chiến thắng
        if (potentialWinners.size() == 1) {
            int winner = potentialWinners.get(0);
            setWinnerProgress(winner);
            tvResult.setText("Hoàn thành: Thanh " + winner);
        } else {
            // Nếu có nhiều người cùng đạt ngưỡng, chọn ngẫu nhiên 1 người chiến thắng
            int randomIndex = random.nextInt(potentialWinners.size());
            int winner = potentialWinners.get(randomIndex);
            setWinnerProgress(winner);

            // Hiển thị thông báo về các ứng viên
//            StringBuilder candidates = new StringBuilder();
//            for (int i = 0; i < potentialWinners.size(); i++) {
//                if (i > 0) candidates.append(", ");
//                candidates.append("Thanh ").append(potentialWinners.get(i));
//            }
//            tvResult.setText("Hoàn thành: Thanh " + winner + " (Các ứng viên: " + candidates + ")");
//
        }

        // Phát âm thanh chiến thắng
        //MusicManager.playEffect(this, R.raw.winner_sound);
    }

    private void setWinnerProgress(int winner) {
        // Đặt người chiến thắng lên 100%
        switch (winner) {
            case 1:
                val1 = MAX_PROGRESS;
                pb1.setProgress(MAX_PROGRESS);
                break;
            case 2:
                val2 = MAX_PROGRESS;
                pb2.setProgress(MAX_PROGRESS);
                break;
            case 3:
                val3 = MAX_PROGRESS;
                pb3.setProgress(MAX_PROGRESS);
        }
    }
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

    private void checkNormalWinCondition() {
        // Kiểm tra điều kiện chiến thắng thông thường (về đích 100 điểm)
        boolean hasWinner = false;
        int winner = 0;

        if (val1 >= MAX_PROGRESS) {
            hasWinner = true;
            winner = 1;
        } else if (val2 >= MAX_PROGRESS) {
            hasWinner = true;
            winner = 2;
        } else if (val3 >= MAX_PROGRESS) {
            hasWinner = true;
            winner = 3;
        }

        if (hasWinner) {
            winnerDetermined = true;
            isRunning = false;
            tvResult.setText("Hoàn thành: Thanh " + winner);
            //MusicManager.playEffect(this, R.raw.winner_sound);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Dọn dẹp handler để tránh memory leaks
        handler.removeCallbacks(updateRunnable);
        MusicManager.stopBackground();
    }
}