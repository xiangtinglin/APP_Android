package com.example.android_os_finalreport;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class EKGView extends View {

    private Paint linePaint;
    private List<Float> ekgPoints;
    private static final int MAX_AMPLITUDE = 20;  // 最大震幅對應的數值範圍
    private static final float SCALE_FACTOR = 0.1f; // 縮放因子

    public EKGView(Context context, AttributeSet attrs) {
        super(context, attrs);

        linePaint = new Paint();
        linePaint.setColor(Color.GREEN);
        linePaint.setStrokeWidth(5);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);

        ekgPoints = new ArrayList<>();
        initEKGPoints();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width = getWidth();
        float height = getHeight();
        float startX = 0;
        float startY = height * 3 / 4;  // 調整基線位置為視圖高度的3/4

        if (ekgPoints.isEmpty()) {
            // 當沒有數據時，繪製一條橫跨整個視圖的橫線
            canvas.drawLine(startX, startY, width, startY, linePaint);
        } else {
            for (int i = 0; i < ekgPoints.size() - 1; i++) {
                float x = startX + (i / (float) ekgPoints.size()) * width;
                float y = startY - (ekgPoints.get(i) * SCALE_FACTOR * height / 2);  // 使用縮放因子調整震幅

                float nextX = startX + ((i + 1) / (float) ekgPoints.size()) * width;
                float nextY = startY - (ekgPoints.get(i + 1) * SCALE_FACTOR * height / 2);  // 使用縮放因子調整震幅

                canvas.drawLine(x, y, nextX, nextY, linePaint);
            }
        }
    }

    private void initEKGPoints() {
        for (int i = 0; i < 100; i++) {
            ekgPoints.add(0f);
        }
    }

    public void updateEKGPoints(float amplitude) {
        if (ekgPoints.size() > 100) {
            ekgPoints.remove(0);
        }
        ekgPoints.add(amplitude);
        invalidate();
    }
}
