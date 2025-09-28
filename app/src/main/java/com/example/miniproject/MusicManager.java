package com.example.miniproject;

import android.content.Context;
import android.media.MediaPlayer;

/** Quản lý nhạc nền và hiệu ứng trong toàn bộ app */
public class MusicManager {

    private static MediaPlayer bgMusic;     // Nhạc nền
    private static MediaPlayer soundEffect; // Hiệu ứng ngắn

    /** Phát nhạc nền (loop) */
    public static void playBackground(Context context, int resId) {
        stopBackground(); // dừng nhạc cũ nếu có
        bgMusic = MediaPlayer.create(context, resId);
        bgMusic.setLooping(true);
        bgMusic.start();
    }

    /** Dừng nhạc nền */
    public static void stopBackground() {
        if (bgMusic != null) {
            if (bgMusic.isPlaying()) {
                bgMusic.stop();
            }
            bgMusic.release();
            bgMusic = null;
        }
    }

    /** Phát sound effect (chỉ một lần) */
    public static void playEffect(Context context, int resId) {
        // Dừng effect cũ nếu đang chạy
        if (soundEffect != null) {
            soundEffect.release();
        }
        soundEffect = MediaPlayer.create(context, resId);
        soundEffect.setOnCompletionListener(mp -> {
            mp.release();  // giải phóng sau khi phát xong
        });
        soundEffect.start();
    }

    /** Dừng sound effect ngay lập tức */
    public static void stopEffect() {
        if (soundEffect != null) {
            if (soundEffect.isPlaying()) {
                soundEffect.stop();
            }
            soundEffect.release();
            soundEffect = null;
        }
    }
}
