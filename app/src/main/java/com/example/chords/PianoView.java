package com.example.chords;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

import java.util.Locale;

public class PianoView extends HorizontalScrollView {

    public interface OnKeyListener {
        void onKeyDown(int keyIndex, boolean isBlack);
        void onKeyUp(int keyIndex, boolean isBlack);
    }

    private OnKeyListener keyListener;
    private final PianoKeyboardView keyboardView;

    public PianoView(Context context) {
        this(context, null);
    }

    public PianoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PianoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setHorizontalScrollBarEnabled(false);
        setFillViewport(true);

        keyboardView = new PianoKeyboardView(context);
        addView(keyboardView, new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT
        ));

        keyboardView.setOnInternalKeyListener(new PianoKeyboardView.InternalKeyListener() {
            @Override
            public void onInternalKeyDown(int keyIndex, boolean isBlack) {
                if (keyListener != null) {
                    keyListener.onKeyDown(keyIndex, isBlack);
                }
            }

            @Override
            public void onInternalKeyUp(int keyIndex, boolean isBlack) {
                if (keyListener != null) {
                    keyListener.onKeyUp(keyIndex, isBlack);
                }
            }
        });
    }

    public void setKeyListener(OnKeyListener listener) {
        this.keyListener = listener;
    }

    public void highlightNotes(boolean[] whiteHighlights, boolean[] blackHighlights) {
        keyboardView.setHighlights(whiteHighlights, blackHighlights);
    }

    private static class PianoKeyboardView extends View {

        interface InternalKeyListener {
            void onInternalKeyDown(int keyIndex, boolean isBlack);
            void onInternalKeyUp(int keyIndex, boolean isBlack);
        }

        private InternalKeyListener internalKeyListener;

        private final Paint whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint blackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint whiteTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint blackTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint highlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint pressedOverlayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        private final RectF[] whiteRects = new RectF[21];
        private final RectF[] blackRects = new RectF[15];

        private boolean[] whiteHighlights = new boolean[21];
        private boolean[] blackHighlights = new boolean[15];

        private final int[] whiteNoteMap = {
                0, 2, 4, 5, 7, 9, 11,
                12, 14, 16, 17, 19, 21, 23,
                24, 26, 28, 29, 31, 33, 35
        };

        private final int[] blackNoteMap = {
                1, 3, 6, 8, 10,
                13, 15, 18, 20, 22,
                25, 27, 30, 32, 34
        };

        private int activeWhiteIndex = -1;
        private int activeBlackIndex = -1;

        private SoundPool soundPool;
        private final int[] noteSounds = new int[36];

        public PianoKeyboardView(Context context) {
            super(context);
            init(context);
        }

        private void init(Context context) {
            whitePaint.setStyle(Paint.Style.FILL);
            blackPaint.setStyle(Paint.Style.FILL);

            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(2f);
            borderPaint.setColor(Color.parseColor("#D0D0D0"));

            whiteTextPaint.setColor(Color.parseColor("#556070"));
            whiteTextPaint.setTextAlign(Paint.Align.CENTER);
            whiteTextPaint.setTextSize(34f);

            blackTextPaint.setColor(Color.WHITE);
            blackTextPaint.setTextAlign(Paint.Align.CENTER);
            blackTextPaint.setTextSize(28f);

            highlightPaint.setStyle(Paint.Style.FILL);
            highlightPaint.setColor(Color.parseColor("#FFF1A8"));

            pressedOverlayPaint.setStyle(Paint.Style.FILL);
            pressedOverlayPaint.setColor(Color.parseColor("#55FFD54F"));

            for (int i = 0; i < whiteRects.length; i++) {
                whiteRects[i] = new RectF();
            }
            for (int i = 0; i < blackRects.length; i++) {
                blackRects[i] = new RectF();
            }

            setBackgroundColor(Color.WHITE);

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(8)
                    .setAudioAttributes(audioAttributes)
                    .build();

            // 自动加载 res/raw/n00.ogg ~ n35.ogg
            for (int i = 0; i < 36; i++) {
                String rawName = String.format(Locale.US, "n%02d", i);
                int resId = context.getResources().getIdentifier(
                        rawName, "raw", context.getPackageName()
                );
                if (resId != 0) {
                    noteSounds[i] = soundPool.load(context, resId, 1);
                }
            }
        }

        void setOnInternalKeyListener(InternalKeyListener listener) {
            this.internalKeyListener = listener;
        }

        void setHighlights(boolean[] white, boolean[] black) {
            if (white != null && white.length == whiteHighlights.length) {
                whiteHighlights = white.clone();
            }
            if (black != null && black.length == blackHighlights.length) {
                blackHighlights = black.clone();
            }
            invalidate();
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int height = MeasureSpec.getSize(heightMeasureSpec);
            if (height <= 0) {
                height = dp(260);
            }
            int whiteKeyWidth = dp(52);
            int desiredWidth = whiteKeyWidth * whiteRects.length;
            setMeasuredDimension(desiredWidth, height);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            float whiteW = (float) w / whiteRects.length;
            float blackW = whiteW * 0.62f;
            float blackH = h * 0.62f;

            for (int i = 0; i < whiteRects.length; i++) {
                float left = i * whiteW;
                whiteRects[i].set(left, 0, left + whiteW, h);
            }

            int[] blackAnchor = {
                    0, 1, 3, 4, 5,
                    7, 8, 10, 11, 12,
                    14, 15, 17, 18, 19
            };

            for (int i = 0; i < blackRects.length; i++) {
                float centerX = (blackAnchor[i] + 1) * whiteW;
                float left = centerX - blackW / 2f;
                blackRects[i].set(left, 0, left + blackW, blackH);
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            for (int i = 0; i < whiteRects.length; i++) {
                RectF rect = whiteRects[i];

                whitePaint.setShader(new LinearGradient(
                        rect.left, rect.top, rect.left, rect.bottom,
                        Color.parseColor("#FFFFFF"),
                        Color.parseColor("#EDEDED"),
                        Shader.TileMode.CLAMP
                ));
                canvas.drawRoundRect(rect, 0f, 0f, whitePaint);

                if (whiteHighlights[i]) {
                    canvas.drawRoundRect(rect, 0f, 0f, highlightPaint);
                }

                if (i == activeWhiteIndex) {
                    canvas.drawRoundRect(rect, 0f, 0f, pressedOverlayPaint);
                }

                canvas.drawRect(rect, borderPaint);
                drawWhiteLabel(canvas, i);
            }

            for (int i = 0; i < blackRects.length; i++) {
                RectF rect = blackRects[i];

                blackPaint.setShader(new LinearGradient(
                        rect.left, rect.top, rect.left, rect.bottom,
                        Color.parseColor("#34324A"),
                        Color.parseColor("#0F0F17"),
                        Shader.TileMode.CLAMP
                ));
                canvas.drawRoundRect(rect, 12f, 12f, blackPaint);

                if (blackHighlights[i]) {
                    canvas.drawRoundRect(rect, 12f, 12f, highlightPaint);
                }

                if (i == activeBlackIndex) {
                    canvas.drawRoundRect(rect, 12f, 12f, pressedOverlayPaint);
                }

                drawBlackLabel(canvas, i);
            }
        }

        private void drawWhiteLabel(Canvas canvas, int index) {
            String note = getWhiteNoteName(index);
            float cx = whiteRects[index].centerX();
            float cy = whiteRects[index].bottom - dp(18);
            canvas.drawText(note, cx, cy, whiteTextPaint);
        }

        private void drawBlackLabel(Canvas canvas, int index) {
            String note = getBlackNoteName(index);
            float cx = blackRects[index].centerX();
            float cy = blackRects[index].bottom - dp(10);
            canvas.drawText(note, cx, cy, blackTextPaint);
        }

        private String getWhiteNoteName(int index) {
            switch (index % 7) {
                case 0: return "C";
                case 1: return "D";
                case 2: return "E";
                case 3: return "F";
                case 4: return "G";
                case 5: return "A";
                default: return "B";
            }
        }

        private String getBlackNoteName(int index) {
            switch (index % 5) {
                case 0: return "C#";
                case 1: return "D#";
                case 2: return "F#";
                case 3: return "G#";
                default: return "A#";
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    handleTouch(x, y);
                    return true;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    releaseActiveKey();
                    return true;
            }
            return super.onTouchEvent(event);
        }

        private void handleTouch(float x, float y) {
            int blackIndex = findBlackKey(x, y);
            if (blackIndex != -1) {
                if (activeBlackIndex != blackIndex || activeWhiteIndex != -1) {
                    releaseActiveKey();
                    activeBlackIndex = blackIndex;
                    playNote(true, blackIndex);

                    if (internalKeyListener != null) {
                        internalKeyListener.onInternalKeyDown(blackIndex, true);
                    }
                    invalidate();
                }
                return;
            }

            int whiteIndex = findWhiteKey(x, y);
            if (whiteIndex != -1) {
                if (activeWhiteIndex != whiteIndex || activeBlackIndex != -1) {
                    releaseActiveKey();
                    activeWhiteIndex = whiteIndex;
                    playNote(false, whiteIndex);

                    if (internalKeyListener != null) {
                        internalKeyListener.onInternalKeyDown(whiteIndex, false);
                    }
                    invalidate();
                }
            }
        }

        private void playNote(boolean isBlack, int keyIndex) {
            int noteIndex = isBlack ? blackNoteMap[keyIndex] : whiteNoteMap[keyIndex];
            if (noteIndex >= 0 && noteIndex < noteSounds.length) {
                int soundId = noteSounds[noteIndex];
                if (soundId != 0 && soundPool != null) {
                    soundPool.play(soundId, 1f, 1f, 1, 0, 1f);
                }
            }
        }

        private void releaseActiveKey() {
            if (activeBlackIndex != -1 && internalKeyListener != null) {
                internalKeyListener.onInternalKeyUp(activeBlackIndex, true);
            }
            if (activeWhiteIndex != -1 && internalKeyListener != null) {
                internalKeyListener.onInternalKeyUp(activeWhiteIndex, false);
            }
            activeBlackIndex = -1;
            activeWhiteIndex = -1;
            invalidate();
        }

        private int findBlackKey(float x, float y) {
            for (int i = 0; i < blackRects.length; i++) {
                if (blackRects[i].contains(x, y)) {
                    return i;
                }
            }
            return -1;
        }

        private int findWhiteKey(float x, float y) {
            for (int i = 0; i < whiteRects.length; i++) {
                if (whiteRects[i].contains(x, y)) {
                    return i;
                }
            }
            return -1;
        }

        private int dp(int value) {
            return (int) (value * getResources().getDisplayMetrics().density);
        }

        @Override
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            if (soundPool != null) {
                soundPool.release();
                soundPool = null;
            }
        }
    }
}