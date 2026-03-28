package com.example.android_os_finalreport;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TouchView extends View {

    private Paint fillPaint;  // 用于填充颜色
    private Paint borderPaint;  // 用于绘制边框

    private int squareSize = 50;  // 正方形的大小
    private boolean[][] touched;  // 用于记录每个正方形是否被滑动过
    // 用于记录上次触摸的位置
    private float lastTouchX = -1;
    private float lastTouchY = -1;

    public TouchView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 初始化填充 Paint，用于填充黄色和白色
        fillPaint = new Paint();
        fillPaint.setColor(Color.YELLOW);  // 默认填充颜色为黄色
        fillPaint.setAntiAlias(true);  // 防止锯齿
        fillPaint.setStyle(Paint.Style.FILL);  // 填充正方形

        // 初始化边框 Paint，用于绘制正方形的边
        borderPaint = new Paint();
        borderPaint.setColor(Color.YELLOW);  // 边框颜色为黄色
        borderPaint.setAntiAlias(true);  // 防止锯齿
        borderPaint.setStrokeWidth(5);  // 设置边框的宽度
        borderPaint.setStyle(Paint.Style.STROKE);  // 只绘制边框

        // 初始化触摸状态数组，确定正方形的数量
        touched = new boolean[100][100];  // 假设最多100x100个正方形
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int screenWidth = getWidth();  // 获取屏幕宽度
        int screenHeight = getHeight();  // 获取屏幕高度

        // 计算每行显示多少个正方形
        int cols = screenWidth / squareSize;
        int rows = screenHeight / squareSize;

        // 绘制所有的正方形
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                float left = i * squareSize;
                float top = j * squareSize;
                float right = left + squareSize;
                float bottom = top + squareSize;

                // 如果当前正方形被触摸过，将其填充为白色
                if (touched[i][j]) {
                    fillPaint.setColor(Color.WHITE);  // 已触摸的正方形变为白色
                } else {
                    fillPaint.setColor(Color.YELLOW);  // 默认黄色
                }

                // 绘制填充的正方形
                canvas.drawRect(left, top, right, bottom, fillPaint);

                // 绘制黄色边框
                canvas.drawRect(left, top, right, bottom, borderPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 获取触摸位置
        float touchX = event.getX();
        float touchY = event.getY();

        // 四舍五入计算触摸位置对应的正方形索引
        int col = Math.round(touchX / squareSize);
        int row = Math.round(touchY / squareSize);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 第一次触摸，标记当前位置
                markTouched(col, row);
                break;
            case MotionEvent.ACTION_MOVE:
                // 处理滑动路径上的多个正方形
                if (lastTouchX != -1 && lastTouchY != -1) {
                    // 计算触摸方向，逐步标记路径上的正方形
                    markTouchedPath(Math.round(lastTouchX / squareSize), Math.round(lastTouchY / squareSize), col, row);
                }
                markTouched(col, row);  // 更新当前正方形为白色
                break;
        }

        // 更新上次触摸的位置
        lastTouchX = touchX;
        lastTouchY = touchY;

        // 重新绘制视图
        invalidate();
        return true;  // 返回true表示事件已处理
    }

    // 标记正方形为触摸过
    private void markTouched(int col, int row) {
        if (col >= 0 && col < touched.length && row >= 0 && row < touched[0].length) {
            touched[col][row] = true;
        }
    }

    // 通过路径标记所有经过的正方形
    private void markTouchedPath(int startCol, int startRow, int endCol, int endRow) {
        int dx = Math.abs(endCol - startCol);
        int dy = Math.abs(endRow - startRow);
        int stepX = (startCol < endCol) ? 1 : -1;
        int stepY = (startRow < endRow) ? 1 : -1;
        int err = dx - dy;

        int x = startCol;
        int y = startRow;
        while (true) {
            markTouched(x, y);
            if (x == endCol && y == endRow) break;
            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x += stepX;
            }
            if (e2 < dx) {
                err += dx;
                y += stepY;
            }
        }
    }
}
