package com.example.chords;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class maj_a extends AppCompatActivity {

    private SoundPool soundPool;
    private int sound1, sound2, sound3, sound4, sound5, sound6, sound7;
    private Animation a_up, a_dw;

    private Button button1, button2, button3, button4, button5, button6, button7;
    private LinearLayout pianoLayout;
    private PianoView pianoView;
    private Button btnSwitch;
    private Button pianoModeBtn;
    private TextView titleLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maj_a);

        button1 = findViewById(R.id.a);
        button2 = findViewById(R.id.b);
        button3 = findViewById(R.id.cs);
        button4 = findViewById(R.id.d);
        button5 = findViewById(R.id.e);
        button6 = findViewById(R.id.fs);
        button7 = findViewById(R.id.gs);
        pianoLayout = findViewById(R.id.pianoLayout);
        pianoView = findViewById(R.id.pianoView);
        btnSwitch = findViewById(R.id.btn_switch_piano);
        pianoModeBtn = findViewById(R.id.piano_mode_btn);
        titleLabel = findViewById(R.id.maj_a_label);

        // 点击标题返回上一页
        titleLabel.setOnClickListener(v -> {
            startActivity(new Intent(maj_a.this, maj_intent.class));
            finish();
        });

        // 初始化音效池
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder().setMaxStreams(7).build();
        } else {
            soundPool = new SoundPool(7, AudioManager.STREAM_MUSIC, 0);
        }
        sound1 = soundPool.load(this, R.raw.maj_a_a, 1);
        sound2 = soundPool.load(this, R.raw.maj_a_b, 1);
        sound3 = soundPool.load(this, R.raw.maj_a_cs, 1);
        sound4 = soundPool.load(this, R.raw.maj_a_d, 1);
        sound5 = soundPool.load(this, R.raw.maj_a_e, 1);
        sound6 = soundPool.load(this, R.raw.maj_a_fs, 1);
        sound7 = soundPool.load(this, R.raw.maj_a_gs, 1);

        a_up = AnimationUtils.loadAnimation(this, R.anim.anim);
        a_dw = AnimationUtils.loadAnimation(this, R.anim.anim2);

        setupButton(button1, sound1);
        setupButton(button2, sound2);
        setupButton(button3, sound3);
        setupButton(button4, sound4);
        setupButton(button5, sound5);
        setupButton(button6, sound6);
        setupButton(button7, sound7);

        pianoView.setKeyListener(new PianoView.OnKeyListener() {
            @Override
            public void onKeyDown(int keyIndex, boolean isBlack) {
                int soundId = -1;
                if (!isBlack) {
                    switch (keyIndex) {
                        case 5: soundId = sound1; break;
                        case 6: soundId = sound2; break;
                        case 1: soundId = sound4; break;
                        case 2: soundId = sound5; break;
                    }
                } else {
                    switch (keyIndex) {
                        case 0: soundId = sound3; break;
                        case 2: soundId = sound6; break;
                        case 3: soundId = sound7; break;
                    }
                }
                if (soundId != -1) soundPool.play(soundId, 1, 1, 0, 0, 1);
            }
            @Override public void onKeyUp(int keyIndex, boolean isBlack) {}
        });

        btnSwitch.setOnClickListener(v -> {
            setButtonModeVisibility(false);
            pianoLayout.setVisibility(View.VISIBLE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            pianoView.postDelayed(pianoView::requestLayout, 100);
        });

        pianoModeBtn.setOnClickListener(v -> {
            pianoLayout.setVisibility(View.GONE);
            setButtonModeVisibility(true);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        });
    }

    private void setButtonModeVisibility(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        titleLabel.setVisibility(visibility);
        btnSwitch.setVisibility(visibility);
        button1.setVisibility(visibility);
        button2.setVisibility(visibility);
        button3.setVisibility(visibility);
        button4.setVisibility(visibility);
        button5.setVisibility(visibility);
        button6.setVisibility(visibility);
        button7.setVisibility(visibility);
    }

    private void setupButton(final Button btn, final int soundId) {
        btn.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                btn.startAnimation(a_up);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                btn.startAnimation(a_dw);
                soundPool.play(soundId, 1, 1, 0, 0, 1);
            }
            return false;   // 必须返回 false，让按钮自己管理 pressed 状态
        });
        // 不要设置 setOnClickListener
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundPool != null) soundPool.release();
    }
}