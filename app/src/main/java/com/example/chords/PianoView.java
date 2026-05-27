package com.example.chords;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PianoView extends View {

    private static final int WHITE_KEYS = 7;
    private static final int BLACK_KEYS = 5;

    private final List<RectF> whiteKeyRects = new ArrayList<>();
    private final List<RectF> blackKeyRects = new ArrayList<>();

    private final Paint whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint blackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaintWhite = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaintBlack = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final int[] whiteKeyColors = new int[WHITE_KEYS];
    private final int[] blackKeyColors = new int[BLACK_KEYS];

    private OnKeyListener keyListener;

    // 白键对应的音名（索引 0~6 对应 C, D, E, F, G, A, B）
    private final String[] whiteKeyNames = {"C", "D", "E", "F", "G", "A", "B"};
    // 黑键对应的音名（索引 0~4 对应 C#, D#, F#, G#, A#）
    private final String[] blackKeyNames = {"C#", "D#", "F#", "G#", "A#"};

    // 控制哪些键显示文字（根据 A 大调的发声键）
    // 白键中 A(5)、B(6)、D(1)、E(2) 发声；C(0)、F(3)、G(4) 不发声
    private final boolean[] whiteKeyShowText = {false, true, true, false, false, true, true};
    // 黑键中 C#(0)、F#(2)、G#(3) 发声；D#(1)、A#(4) 不发声
    private final boolean[] blackKeyShowText = {true, false, true, true, false};

    public interface OnKeyListener {
        void onKeyDown(int keyIndex, boolean isBlack);
        void onKeyUp(int keyIndex, boolean isBlack);
    }

    public PianoView(Context context) {
        super(context);
        init();
    }

    public PianoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PianoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Arrays.fill(whiteKeyColors, Color.WHITE);
        Arrays.fill(blackKeyColors, Color.BLACK);

        // 白键文字样式：黑色、小字、底部居中
        textPaintWhite.setColor(Color.BLACK);
        textPaintWhite.setTextSize(32f); // 单位 px，可根据屏幕密度调整，或者使用 sp 转换
        textPaintWhite.setTextAlign(Paint.Align.CENTER);

        // 黑键文字样式：白色、小字、底部居中
        textPaintBlack.setColor(Color.WHITE);
        textPaintBlack.setTextSize(28f);
        textPaintBlack.setTextAlign(Paint.Align.CENTER);
    }

    public void setKeyListener(OnKeyListener listener) {
        this.keyListener = listener;
    }

    public void highlightNotes(boolean[] whiteHighlights, boolean[] blackHighlights) {
        for (int i = 0; i < WHITE_KEYS; i++) {
            whiteKeyColors[i] = whiteHighlights[i] ? Color.YELLOW : Color.WHITE;
        }
        for (int i = 0; i < BLACK_KEYS; i++) {
            blackKeyColors[i] = blackHighlights[i] ? Color.parseColor("#FFD700") : Color.BLACK;
        }
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        calculateKeyRects(w, h);
    }

    private void calculateKeyRects(int width, int height) {
        whiteKeyRects.clear();
        blackKeyRects.clear();

        float whiteKeyWidth = (float) width / WHITE_KEYS;
        float blackKeyWidth = whiteKeyWidth * 0.6f;
        float blackKeyHeight = height * 0.6f;

        // 白键
        for (int i = 0; i < WHITE_KEYS; i++) {
            float left = i * whiteKeyWidth;
            float right = left + whiteKeyWidth;
            whiteKeyRects.add(new RectF(left, 0, right, height));
        }

        // 黑键位置（标准钢琴：C#(1), D#(2), F#(4), G#(5), A#(6) 对应的白键索引）
        int[] blackKeyPositions = {1, 2, 4, 5, 6};
        for (int i = 0; i < BLACK_KEYS; i++) {
            int whiteIndex = blackKeyPositions[i];
            float left = (whiteIndex + 0.5f) * whiteKeyWidth - blackKeyWidth / 2;
            float right = left + blackKeyWidth;
            blackKeyRects.add(new RectF(left, 0, right, blackKeyHeight));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 绘制白键
        for (int i = 0; i < whiteKeyRects.size(); i++) {
            RectF rect = whiteKeyRects.get(i);
            whitePaint.setColor(whiteKeyColors[i]);
            canvas.drawRect(rect, whitePaint);
            // 边框
            whitePaint.setColor(Color.BLACK);
            whitePaint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(rect, whitePaint);
            whitePaint.setStyle(Paint.Style.FILL);

            // 绘制白键文字（需要显示的键）
            if (whiteKeyShowText[i]) {
                String label = whiteKeyNames[i];
                float x = rect.centerX();
                float y = rect.bottom - 20; // 距离底部 20px，可根据需要调整
                canvas.drawText(label, x, y, textPaintWhite);
            }
        }

        // 绘制黑键
        for (int i = 0; i < blackKeyRects.size(); i++) {
            RectF rect = blackKeyRects.get(i);
            blackPaint.setColor(blackKeyColors[i]);
            canvas.drawRect(rect, blackPaint);

            // 绘制黑键文字（需要显示的键）
            if (blackKeyShowText[i]) {
                String label = blackKeyNames[i];
                float x = rect.centerX();
                float y = rect.bottom - 12; // 距离底部 12px
                canvas.drawText(label, x, y, textPaintBlack);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int action = event.getActionMasked();

        // 先检测黑键
        for (int i = 0; i < blackKeyRects.size(); i++) {
            if (blackKeyRects.get(i).contains(x, y)) {
                handleKeyEvent(i, true, action);
                return true;
            }
        }
        // 再检测白键
        for (int i = 0; i < whiteKeyRects.size(); i++) {
            if (whiteKeyRects.get(i).contains(x, y)) {
                handleKeyEvent(i, false, action);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    private void handleKeyEvent(int index, boolean isBlack, int action) {
        if (keyListener == null) return;
        if (action == MotionEvent.ACTION_DOWN) {
            keyListener.onKeyDown(index, isBlack);
        } else if (action == MotionEvent.ACTION_UP) {
            keyListener.onKeyUp(index, isBlack);
        }
    }
}