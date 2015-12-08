package com.xuf.www.gobang;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2015/12/8.
 */
public class GoBangBoard extends View {

    private static final int LINE_COUNT = 15;
    private static final int BOARD_MARGIN = 30;

    private Paint mLinePaint;
    private Paint mPointPaint;
    private int mLineCount;
    private float[] mHorizontalLinePoints;
    private float[] mVerticalLinePoints;
    private float[] mBlackPoints;

    public GoBangBoard(Context context) {
        super(context);
        init();
    }

    public GoBangBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GoBangBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(Color.BLACK);

        mPointPaint = new Paint();
        mPointPaint.setStrokeWidth(10);

        mLineCount = LINE_COUNT;
    }

    private void calcLinePoints(){
        mHorizontalLinePoints = new float[mLineCount * 4];
        mVerticalLinePoints = new float[mLineCount * 4];

        float boardWidth = getWidth() - BOARD_MARGIN * 2;
        float boardHeight = getHeight() - BOARD_MARGIN * 2;

        float gridWidth = boardWidth / (mLineCount - 1);
        for (int i = 0; i < mLineCount * 4; i += 4){
            mVerticalLinePoints[i] = i * gridWidth / 4 + BOARD_MARGIN;
            mVerticalLinePoints[i + 1] = BOARD_MARGIN;
            mVerticalLinePoints[i + 2] = i * gridWidth / 4 + BOARD_MARGIN;
            mVerticalLinePoints[i + 3] = boardHeight + BOARD_MARGIN;
        }

        float gridHeight = boardHeight / (mLineCount - 1);
        for (int i = 0; i < mLineCount * 4; i += 4){
            mHorizontalLinePoints[i] = BOARD_MARGIN;
            mHorizontalLinePoints[i + 1] = i * gridHeight / 4 + BOARD_MARGIN;
            mHorizontalLinePoints[i + 2] = boardWidth + BOARD_MARGIN;
            mHorizontalLinePoints[i + 3] = i * gridHeight / 4 + BOARD_MARGIN;
        }

        mBlackPoints = new float[]{3 * gridWidth + BOARD_MARGIN, 3 * gridHeight + BOARD_MARGIN,
                                   11 * gridWidth + BOARD_MARGIN, 3 * gridHeight + BOARD_MARGIN,
                                   7 * gridWidth + BOARD_MARGIN, 7 * gridHeight + BOARD_MARGIN,
                                   3 * gridWidth + BOARD_MARGIN, 11 * gridHeight + BOARD_MARGIN,
                                   11 * gridWidth + BOARD_MARGIN, 11 * gridHeight + BOARD_MARGIN};
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calcLinePoints();
        drawLines(canvas);
        drawBlackPoints(canvas);
        drawChess(canvas);
    }

    private void drawLines(Canvas canvas){
        canvas.drawLines(mHorizontalLinePoints, mLinePaint);
        canvas.drawLines(mVerticalLinePoints, mLinePaint);
    }

    private void drawBlackPoints(Canvas canvas){
        canvas.drawPoints(mBlackPoints, mPointPaint);
    }

    private void drawChess(Canvas canvas){

    }
}
