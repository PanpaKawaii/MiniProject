package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class RacingActivity extends AppCompatActivity {
    ProgressBar pb1, pb2, pb3;
    TextView tvResult, tvMoney;
    Button btnStart;
    ImageView imgDuck1, imgDuck2, imgDuck3;
    EditText edtBetDuck1, edtBetDuck2, edtBetDuck3;

    Handler handler = new Handler();
    Random random = new Random();
    private static final int UPDATE_INTERVAL_MS = 50;
    private static final int WIN_THRESHOLD = 96;
    private static final int MAX_PROGRESS = 100;

    int val1 = 0, val2 = 0, val3 = 0;
    boolean isRunning = false;
    boolean winnerDetermined = false;
    int userMoney = 1000; // Hardcoded money for now
    int betDuck1 = 0, betDuck2 = 0, betDuck3 = 0;
    int totalBetAmount = 0;

    // Toggle để đổi ảnh tạo hiệu ứng chạy
    boolean toggle1 = false, toggle2 = false, toggle3 = false;

    private void startRace() {
        // Lấy số tiền cược cho từng vịt
        try {
            betDuck1 = getBetAmount(edtBetDuck1);
            betDuck2 = getBetAmount(edtBetDuck2);
            betDuck3 = getBetAmount(edtBetDuck3);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Vui lòng nhập số tiền hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tính tổng tiền cược
        totalBetAmount = betDuck1 + betDuck2 + betDuck3;

        // Kiểm tra validation
        if (totalBetAmount <= 0) {
            Toast.makeText(this, "Vui lòng đặt cược ít nhất một vịt!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (totalBetAmount > userMoney) {
            Toast.makeText(this, "Tổng tiền cược không được vượt quá số dư!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Reset race state
        val1 = val2 = val3 = 0;
        pb1.setProgress(0);
        pb2.setProgress(0);
        pb3.setProgress(0);
        tvResult.setText("Đang chuẩn bị...");
        isRunning = false;
        winnerDetermined = false;

        // Disable betting during race
        setBettingEnabled(false);

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

            // Hiển thị thông tin cược
            String betInfo = "Đang chạy... - Cược: ";
            if (betDuck1 > 0) betInfo += "V1:" + betDuck1 + " ";
            if (betDuck2 > 0) betInfo += "V2:" + betDuck2 + " ";
            if (betDuck3 > 0) betInfo += "V3:" + betDuck3;
            tvResult.setText(betInfo);
        }, 5000);

        // Reset vị trí vịt
        imgDuck1.setX(0);
        imgDuck2.setX(0);
        imgDuck3.setX(0);
    }

    private int getBetAmount(EditText editText) {
        String text = editText.getText().toString().trim();
        if (text.isEmpty()) {
            return 0;
        }
        int amount = Integer.parseInt(text);
        if (amount < 0) {
            Toast.makeText(this, "Số tiền cược không được âm!", Toast.LENGTH_SHORT).show();
            throw new NumberFormatException();
        }
        return amount;
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

        int winner;
        // Nếu chỉ có 1 người chiến thắng
        if (potentialWinners.size() == 1) {
            winner = potentialWinners.get(0);
        } else {
            // Nếu có nhiều người cùng đạt ngưỡng, chọn ngẫu nhiên 1 người chiến thắng
            int randomIndex = random.nextInt(potentialWinners.size());
            winner = potentialWinners.get(randomIndex);
        }

        setWinnerProgress(winner);
        processBetResult(winner);

        // Enable betting for next race
        setBettingEnabled(true);
    }

    private void processBetResult(int winner) {
        int profit = 0;
        int winningBet = 0;

        // Tính toán kết quả
        switch (winner) {
            case 1:
                winningBet = betDuck1;
                profit = winningBet; // Thắng bằng đúng số tiền cược vào vịt 1
                break;
            case 2:
                winningBet = betDuck2;
                profit = winningBet; // Thắng bằng đúng số tiền cược vào vịt 2
                break;
            case 3:
                winningBet = betDuck3;
                profit = winningBet; // Thắng bằng đúng số tiền cược vào vịt 3
                break;
        }

        // Tính tổng thua (tổng cược trừ đi số cược vào vịt thắng)
        int loseAmount = totalBetAmount - winningBet;

        // Cập nhật tiền: tiền hiện tại + tiền thắng - tiền thua
        userMoney = userMoney + profit - loseAmount;

        // Hiển thị kết quả chi tiết
        String result = "Vịt " + winner + " THẮNG!\n";
        result += "Thắng: +" + profit + " | Thua: -" + loseAmount + "\n";
        result += "Tổng: " + (profit - loseAmount) + " | Số dư: " + userMoney;

        tvResult.setText(result);

        // Update money display
        tvMoney.setText("Số dư: " + userMoney + " $");

        // Reset bet amounts for next race
        resetBets();

        // Hiển thị toast thông báo
        if (profit > loseAmount) {
            Toast.makeText(this, "Chúc mừng! Bạn lời " + (profit - loseAmount), Toast.LENGTH_LONG).show();
        } else if (profit < loseAmount) {
            Toast.makeText(this, "Tiếc quá! Bạn lỗ " + (loseAmount - profit), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Hòa vốn!", Toast.LENGTH_LONG).show();
        }
    }

    private void resetBets() {
        betDuck1 = betDuck2 = betDuck3 = 0;
        totalBetAmount = 0;
        edtBetDuck1.setText("");
        edtBetDuck2.setText("");
        edtBetDuck3.setText("");
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
            processBetResult(winner);
            setBettingEnabled(true);
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

    private void setBettingEnabled(boolean enabled) {
        edtBetDuck1.setEnabled(enabled);
        edtBetDuck2.setEnabled(enabled);
        edtBetDuck3.setEnabled(enabled);
        btnStart.setEnabled(enabled);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MusicManager.playBackground(this, R.raw.backgoundwait);

        setContentView(R.layout.activity_racing);

        // Initialize views
        pb1 = findViewById(R.id.progressBar1);
        pb2 = findViewById(R.id.progressBar2);
        pb3 = findViewById(R.id.progressBar3);
        tvResult = findViewById(R.id.tvResult);
        tvMoney = findViewById(R.id.tvMoney);
        btnStart = findViewById(R.id.btnStart);
        edtBetDuck1 = findViewById(R.id.edtBetDuck1);
        edtBetDuck2 = findViewById(R.id.edtBetDuck2);
        edtBetDuck3 = findViewById(R.id.edtBetDuck3);
        imgDuck1 = findViewById(R.id.imgDuck1);
        imgDuck2 = findViewById(R.id.imgDuck2);
        imgDuck3 = findViewById(R.id.imgDuck3);

        // Initialize money display
        tvMoney.setText("Số dư: " + userMoney + " $");

        // Start race button
        btnStart.setOnClickListener(v -> startRace());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateRunnable);
        MusicManager.stopBackground();
    }
}