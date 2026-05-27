package com.example.chords;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 应用的主界面 Activity
 * 功能：
 * 1. 初始化界面控件与动画资源
 * 2. 给按钮添加按压动画反馈
 * 3. 实现主界面到 maj/min 子页面的跳转
 * 4. 给文本控件添加点击提示
 */
public class MainActivity extends AppCompatActivity {

    // 界面控件声明
    private TextView home_chord; // 主界面文本控件
    private Button h_maj;         // 大调按钮
    private Button h_min;         // 小调按钮

    // 动画资源声明
    private Animation a_up; // 按下时的动画（放大/上浮）
    private Animation a_dw; // 抬起时的动画（缩小/回落）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 绑定主界面布局文件
        setContentView(R.layout.activity_main);

        // 1. 绑定界面控件（通过 id 关联布局文件中的控件）
        initViews();

        // 2. 加载动画资源
        initAnimations();

        // 3. 设置控件的点击/触摸事件
        setupListeners();
    }

    /**
     * 初始化界面控件
     */
    private void initViews() {
        home_chord = findViewById(R.id.chords);
        h_maj = findViewById(R.id.h_major);
        h_min = findViewById(R.id.h_minor);
    }

    /**
     * 加载动画资源
     */
    private void initAnimations() {
        // 从 res/anim/ 文件夹加载动画文件
        a_up = AnimationUtils.loadAnimation(this, R.anim.anim);
        a_dw = AnimationUtils.loadAnimation(this, R.anim.anim2);
    }

    /**
     * 设置所有控件的事件监听
     */
    private void setupListeners() {
        // --- 文本控件 home_chord 点击事件 ---
        home_chord.setOnClickListener(v -> {
            // 弹出吐司提示，显示开发者信息
            Toast.makeText(MainActivity.this, "Made by 2heb Developer", Toast.LENGTH_SHORT).show();
        });

        // --- 大调按钮 h_maj 触摸事件（动画反馈）---
        h_maj.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // 手指按下时，播放 a_up 动画
                h_maj.startAnimation(a_up);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                // 手指抬起时，播放 a_dw 动画
                h_maj.startAnimation(a_dw);
            }
            // 返回 false，让事件继续传递，不影响后续的点击事件
            return false;
        });

        // --- 大调按钮 h_maj 点击事件（页面跳转）---
        h_maj.setOnClickListener(v -> {
            // 创建 Intent，从当前 MainActivity 跳转到 maj_intent 页面
            Intent intent = new Intent(MainActivity.this, maj_intent.class);
            startActivity(intent);
        });

        // --- 小调按钮 h_min 触摸事件（动画反馈）---
        h_min.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                h_min.startAnimation(a_up);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                h_min.startAnimation(a_dw);
            }
            return false;
        });

        // --- 小调按钮 h_min 点击事件（页面跳转）---
        h_min.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, min_intent.class);
            startActivity(intent);
        });
    }
}