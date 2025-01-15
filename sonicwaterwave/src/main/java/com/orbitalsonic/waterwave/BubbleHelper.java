package com.orbitalsonic.waterwave;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BubbleHelper {

    public static final int DEFAULT_BUBBLE_MAX_RADIUS = 30;
    public static final int DEFAULT_BUBBLE_MIN_RADIUS = 5;
    public static final int DEFAULT_BUBBLE_MAX_SIZE = 30;
    public static final int DEFAULT_BUBBLE_MAX_SPEED_Y = 5;
    public static final int DEFAULT_BUBBLE_ALPHA = 128;

    private Point screenSize;
    private ArrayList<Bubble> mBubbles = new ArrayList<>();

    private Random random = new Random();
    private Paint mBubblePaint;                 // 气泡画笔
    private int mBubbleMaxRadius = 30;          // 气泡最大半径 px
    private int mBubbleMinRadius = 5;           // 气泡最小半径 px
    private int mBubbleMaxSize = 30;            // 气泡数量
    private int mBubbleMaxSpeedY = 5;           // 气泡速度
    private int mBubbleAlpha = 128;             // 气泡画笔
    private int mBubbleColor = Color.WHITE;     // 气泡颜色
    private boolean mIsShowBubble = true;       // 是否显示气泡

    public void setScreenSize(Point screenSize) {
        this.screenSize = screenSize;
    }

    BubbleHelper(Point screenSize, int bubbleMaxRadius, int bubbleMinRadius, int bubbleMaxSize, int bubbleMaxSpeedY, int bubbleAlpha, int bubbleColor, boolean isShowBubble) {
        this.screenSize = screenSize;
        this.mBubbleMaxRadius = bubbleMaxRadius;
        this.mBubbleMinRadius = bubbleMinRadius;
        this.mBubbleMaxSize = bubbleMaxSize;
        this.mBubbleMaxSpeedY = bubbleMaxSpeedY;
        this.mBubbleAlpha = bubbleAlpha;
        this.mBubbleColor = bubbleColor;
        this.mIsShowBubble = isShowBubble;

        mBubblePaint = new Paint();
        mBubblePaint.setColor(mBubbleColor);
        mBubblePaint.setAlpha(mBubbleAlpha);
    }


    private class Bubble {
        int radius;     // 气泡半径
        float speedY;   // 上升速度
        float speedX;   // 平移速度
        float x;        // 气泡x坐标
        float y;        // 气泡y坐标
    }


    public void refresh() {
        tryCreateBubble();
        refreshBubbles();
    }


    // 尝试创建气泡
    private void tryCreateBubble() {
        if (!mIsShowBubble) {
            return;
        }
        RectF mWaterRectF = new RectF(0, 0, screenSize.x, screenSize.y);
        if (mBubbles.size() >= mBubbleMaxSize) {
            return;
        }
        if (random.nextFloat() < 0.95) {
            return;
        }
        Bubble bubble = new Bubble();
        int radius = random.nextInt(mBubbleMaxRadius - mBubbleMinRadius);
        radius += mBubbleMinRadius;
        float speedY = random.nextFloat() * mBubbleMaxSpeedY;
        while (speedY < 1) {
            speedY = random.nextFloat() * mBubbleMaxSpeedY;
        }
        bubble.radius = radius;
        bubble.speedY = speedY;
        bubble.x = mWaterRectF.centerX();
        bubble.y = mWaterRectF.bottom - radius;// - mBottleBorder / 2;
        float speedX = random.nextFloat() - 0.5f;
        while (speedX == 0) {
            speedX = random.nextFloat() - 0.5f;
        }
        bubble.speedX = speedX * 2;
        mBubbles.add(bubble);
    }



    // 刷新气泡位置，对于超出区域的气泡进行移除
    private void refreshBubbles() {
        if (!mIsShowBubble) {
            return;
        }
        RectF mWaterRectF = new RectF(0, 0, screenSize.x, screenSize.y);
        List<Bubble> list = new ArrayList<>(mBubbles);
        for (Bubble bubble : list) {
            if (bubble.y - bubble.speedY <= mWaterRectF.top + bubble.radius) {
                mBubbles.remove(bubble);
            } else {
                int i = mBubbles.indexOf(bubble);
                if (bubble.x + bubble.speedX <= mWaterRectF.left + bubble.radius) {
                    bubble.x = mWaterRectF.left + bubble.radius;
                } else if (bubble.x + bubble.speedX >= mWaterRectF.right - bubble.radius) {
                    bubble.x = mWaterRectF.right - bubble.radius;
                } else {
                    bubble.x = bubble.x + bubble.speedX;
                }
                bubble.y = bubble.y - bubble.speedY;
                mBubbles.set(i, bubble);
            }
        }
    }

    // 绘制气泡
    void drawBubble(Canvas canvas) {
        if (!mIsShowBubble) {
            return;
        }
        List<Bubble> list = new ArrayList<>(mBubbles);
        for (Bubble bubble : list) {
            if (null == bubble) continue;
            canvas.drawCircle(bubble.x, bubble.y,
                    bubble.radius, mBubblePaint);
        }
    }
}
