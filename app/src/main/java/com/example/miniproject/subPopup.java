package com.example.miniproject;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class subPopup extends AppCompatActivity {

    private EditText etA, etB, etC, etD;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Lưu ý: layout file phải là lowercase: res/layout/activity_subpopup.xml
        setContentView(R.layout.activity_subpopup);

        etA = findViewById(R.id.etA);
        etB = findViewById(R.id.etB);
        etC = findViewById(R.id.etC);
        etD = findViewById(R.id.etD);

        // Chỉ cho nhập chữ số và dấu chấm (không cho -, +, e, E)
        DigitsKeyListener digitsOnly = DigitsKeyListener.getInstance("0123456789.");
        etA.setKeyListener(digitsOnly);
        etB.setKeyListener(digitsOnly);
        etC.setKeyListener(digitsOnly);
        etD.setKeyListener(digitsOnly);

        // Không cho nhập khoảng trắng
        InputFilter noSpaces = (source, start, end, dest, dstart, dend) ->
                (source != null && source.toString().contains(" ")) ? "" : null;

        etA.setFilters(new InputFilter[]{noSpaces});
        etB.setFilters(new InputFilter[]{noSpaces});
        etC.setFilters(new InputFilter[]{noSpaces});
        etD.setFilters(new InputFilter[]{noSpaces});

        findViewById(R.id.btnShowResult).setOnClickListener(v -> showResultPopup());
    }

    private void showResultPopup() {

        Double a = getPositiveNumberOrError(etA, "A");
        Double b = getPositiveNumberOrError(etB, "B");
        Double c = getPositiveNumberOrError(etC, "C");
        Double d = getPositiveNumberOrError(etD, "D");
        if (a == null || b == null || c == null || d == null) return;

        double win = a - b - c;
        double total = win + d;

        // Inflate layout popup_result.xml
        View popupView = getLayoutInflater().inflate(R.layout.popup_result, null);

        // Find views & set data
        TextView tvA = popupView.findViewById(R.id.tvA);
        TextView tvB = popupView.findViewById(R.id.tvB);
        TextView tvC = popupView.findViewById(R.id.tvC);
        TextView tvWin = popupView.findViewById(R.id.tvWin);
        TextView tvTotal = popupView.findViewById(R.id.tvTotal);
        Button btnClose = popupView.findViewById(R.id.btnClose);

        String fa = fmt(a), fb = fmt(b), fc = fmt(c), fd = fmt(d);
        tvA.setText("A = " + fa);
        tvB.setText("B = " + fb);
        tvC.setText("C = " + fc);
        String winLabel;
        final double EPS = 1e-9;
        double winDisplayValue;
        if (win < -EPS) {
            winLabel = "Lose";
            winDisplayValue = Math.abs(win);
        } else if (Math.abs(win) <= EPS) {
            winLabel = "Draw";
            winDisplayValue = 0d;
        } else {
            winLabel = "Win";
            winDisplayValue = win;
        }

// Hiển thị theo nhãn đã chọn
        tvWin.setText(winLabel + ": " + fmt(winDisplayValue));
        tvTotal.setText("Total: " + fmt(total));

        // Build dialog + transparent background (để popup nhìn như thẻ nổi)
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(popupView)
                .setCancelable(true)
                .create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.show();

        btnClose.setOnClickListener(v -> dialog.dismiss());
    }

    /** Đọc & validate số dương; lỗi thì setError ngay EditText */
    private @Nullable Double getPositiveNumberOrError(EditText et, String label) {
        String raw = et.getText().toString().trim();
        if (raw.isEmpty()) {
            et.setError(label + " không được trống");
            et.requestFocus();
            return null;
        }
        try {
            double val = Double.parseDouble(raw);
            if (val <= 0) {
                et.setError(label + " phải là số dương (> 0)");
                et.requestFocus();
                return null;
            }
            return val;
        } catch (NumberFormatException e) {
            et.setError(label + " không hợp lệ");
            et.requestFocus();
            return null;
        }
    }

    /** Định dạng số gọn: bỏ .0 thừa, giữ tối đa 4 chữ số thập phân */
    private String fmt(double v) {
        if (Math.floor(v) == v) return String.valueOf((long) v);
        return new java.text.DecimalFormat("#.####").format(v);
    }
}
