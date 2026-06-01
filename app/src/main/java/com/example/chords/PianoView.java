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
    private static final int PRESS_DOWN_SHIFT_DP = 4;
    private static final float BLACK_KEY_OFFSET_FACTOR = 0.5f;

    private float pressDownShiftPx;
    private final List<RectF> whiteKeyRects = new ArrayList<>();
    private final List<RectF> blackKeyRects = new ArrayList<>();

    private final Paint whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint blackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint gapFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaintWhite = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaintBlack = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final int[] whiteKeyColors = new int[WHITE_KEYS];
    private final int[] blackKeyColors = new int[BLACK_KEYS];

    private int pressedKeyIndex = -1;
    private boolean pressedIsBlack = false;

    private OnKeyListener keyListener;

    // 预分配临时 RectF 对象，避免在 onDraw 中创建新对象
    private final RectF tmpRect = new RectF();

    private final String[] whiteKeyNames = {"C", "D", "E", "F", "G", "A", "B"};
    private final String[] blackKeyNames = {"C#", "D#", "F#", "G#", "A#"};

    private final boolean[] whiteKeyShowText = {false, true, true, false, false, true, true};
    private final boolean[] blackKeyShowText = {true, false, true, true, false};

    public interface OnKeyListener {
        void onKeyDown(int keyIndex, boolean isBlack);
        void onKeyUp(int keyIndex, boolean isBlack);
    }

    public PianoView(Context context) {
        super(context);
        init(context);
    }

    public PianoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PianoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Arrays.fill(whiteKeyColors, Color.WHITE);
        Arrays.fill(blackKeyColors, Color.BLACK);

        gapFillPaint.setColor(Color.BLACK);
        gapFillPaint.setStyle(Paint.Style.FILL);

        textPaintWhite.setColor(Color.BLACK);
        textPaintWhite.setTextSize(32f);
        textPaintWhite.setTextAlign(Paint.Align.CENTER);

        textPaintBlack.setColor(Color.WHITE);
        textPaintBlack.setTextSize(28f);
        textPaintBlack.setTextAlign(Paint.Align.CENTER);

        pressDownShiftPx = PRESS_DOWN_SHIFT_DP * context.getResources().getDisplayMetrics().density;
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

        for (int i = 0; i < WHITE_KEYS; i++) {
            float left = i * whiteKeyWidth;
            float right = left + whiteKeyWidth;
            whiteKeyRects.add(new RectF(left, 0, right, height));
        }

        int[] blackKeyPositions = {1, 2, 4, 5, 6};
        for (int i = 0; i < BLACK_KEYS; i++) {
            int whiteIndex = blackKeyPositions[i];
            float left = (whiteIndex + BLACK_KEY_OFFSET_FACTOR) * whiteKeyWidth - blackKeyWidth / 2;
            float right = left + blackKeyWidth;
            blackKeyRects.add(new RectF(left, 0, right, blackKeyHeight));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();

        // 绘制白键
        for (int i = 0; i < whiteKeyRects.size(); i++) {
            RectF original = whiteKeyRects.get(i);
            float offsetY = (pressedKeyIndex == i && !pressedIsBlack) ? pressDownShiftPx : 0;
            // 复用 tmpRect，避免分配新对象
            tmpRect.set(original.left, original.top + offsetY,
                    original.right, original.bottom + offsetY);
            whitePaint.setColor(whiteKeyColors[i]);
            canvas.drawRect(tmpRect, whitePaint);
            // 边框
            whitePaint.setColor(Color.BLACK);
            whitePaint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(tmpRect, whitePaint);
            whitePaint.setStyle(Paint.Style.FILL);

            if (whiteKeyShowText[i]) {
                float x = tmpRect.centerX();
                float y = tmpRect.bottom - 20;
                canvas.drawText(whiteKeyNames[i], x, y, textPaintWhite);
            }
        }

        // 绘制黑键
        for (int i = 0; i < blackKeyRects.size(); i++) {
            RectF original = blackKeyRects.get(i);
            boolean isPressed = (pressedKeyIndex == i && pressedIsBlack);
            float offsetY = isPressed ? pressDownShiftPx : 0;

            // 填充缝隙（复用 tmpRect）
            if (isPressed && offsetY > 0) {
                tmpRect.set(original.left, original.top,
                        original.right, original.top + offsetY);
                canvas.drawRect(tmpRect, gapFillPaint);
            }

            tmpRect.set(original.left, original.top + offsetY,
                    original.right, original.bottom + offsetY);
            blackPaint.setColor(blackKeyColors[i]);
            canvas.drawRect(tmpRect, blackPaint);

            if (blackKeyShowText[i]) {
                float x = tmpRect.centerX();
                float y = tmpRect.bottom - 12;
                canvas.drawText(blackKeyNames[i], x, y, textPaintBlack);
            }
        }

        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int action = event.getActionMasked();

        int foundIndex = -1;
        boolean foundBlack = false;
        // 触摸检测使用原始矩形（未偏移）
        for (int i = 0; i < blackKeyRects.size(); i++) {
            if (blackKeyRects.get(i).contains(x, y)) {
                foundIndex = i;
                foundBlack = true;
                break;
            }
        }
        if (foundIndex == -1) {
            for (int i = 0; i < whiteKeyRects.size(); i++) {
                if (whiteKeyRects.get(i).contains(x, y)) {
                    foundIndex = i;
                    foundBlack = false;
                    break;
                }
            }
        }

        if (action == MotionEvent.ACTION_DOWN) {
            if (foundIndex != -1) {
                pressedKeyIndex = foundIndex;
                pressedIsBlack = foundBlack;
                invalidate();
                if (keyListener != null) {
                    keyListener.onKeyDown(foundIndex, foundBlack);
                }
                // 调用 performClick 以满足无障碍要求
                performClick();
                return true;
            }
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            if (pressedKeyIndex != -1) {
                int idx = pressedKeyIndex;
                boolean blk = pressedIsBlack;
                pressedKeyIndex = -1;
                invalidate();
                if (keyListener != null) {
                    keyListener.onKeyUp(idx, blk);
                }
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
}