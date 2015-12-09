package com.xuf.www.gobang;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Comparator;

/**
 * Created by Administrator on 2015/12/8.
 */
public class GoBangBoard extends View implements GameController.ReDrawCallback {

    private static final int LINE_COUNT = GameController.BOARD_SIZE;
    private static final int BOARD_MARGIN = 40;
    private static final int HALF_CHESS_SIZE = 35;

    private static final int POINT_IDLE = GameController.CHESS_NONE;
    private static final int POINT_WHITE = GameController.CHESS_WHITE;
    private static final int POINT_BLACK = GameController.CHESS_BLACK;

    private GameController mController;

    private Bitmap mWhiteChessBitmap;
    private Bitmap mBlackChessBitmap;

    private Paint mLinePaint;
    private Paint mPointPaint;

    private float[] mVerticalLinePoints;
    private float[] mHorizontalLinePoints;
    private float[] mBlackPoints;

    private int mLineCount;
    private float mGridWidth;
    private float mGridHeight;

    public GoBangBoard(Context context) {
        super(context);
        init(context);
    }

    public GoBangBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GoBangBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mController = new GameController();
        mController.setCallback(this);

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(Color.BLACK);

        mPointPaint = new Paint();
        mPointPaint.setStrokeWidth(10);

        mLineCount = LINE_COUNT;

        mBlackChessBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.chess_black);
        mWhiteChessBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.chess_white);
    }

    private void calcLinePoints(){
        mHorizontalLinePoints = new float[mLineCount * 4];
        mVerticalLinePoints = new float[mLineCount * 4];

        float boardWidth = getWidth() - BOARD_MARGIN * 2;
        float boardHeight = getHeight() - BOARD_MARGIN * 2;

        mGridWidth = boardWidth / (mLineCount - 1);
        for (int i = 0; i < mLineCount * 4; i += 4){
            mVerticalLinePoints[i] = i * mGridWidth / 4 + BOARD_MARGIN;
            mVerticalLinePoints[i + 1] = BOARD_MARGIN;
            mVerticalLinePoints[i + 2] = i * mGridWidth / 4 + BOARD_MARGIN;
            mVerticalLinePoints[i + 3] = boardHeight + BOARD_MARGIN;
        }

        mGridHeight = boardHeight / (mLineCount - 1);
        for (int i = 0; i < mLineCount * 4; i += 4){
            mHorizontalLinePoints[i] = BOARD_MARGIN;
            mHorizontalLinePoints[i + 1] = i * mGridHeight / 4 + BOARD_MARGIN;
            mHorizontalLinePoints[i + 2] = boardWidth + BOARD_MARGIN;
            mHorizontalLinePoints[i + 3] = i * mGridHeight / 4 + BOARD_MARGIN;
        }

        mBlackPoints = new float[]{3 * mGridWidth + BOARD_MARGIN, 3 * mGridHeight + BOARD_MARGIN,
                                   11 * mGridWidth + BOARD_MARGIN, 3 * mGridHeight + BOARD_MARGIN,
                                   7 * mGridWidth + BOARD_MARGIN, 7 * mGridHeight + BOARD_MARGIN,
                                   3 * mGridWidth + BOARD_MARGIN, 11 * mGridHeight + BOARD_MARGIN,
                                   11 * mGridWidth + BOARD_MARGIN, 11 * mGridHeight + BOARD_MARGIN};
    }

    public void setGameMode(int gameMode){
        mController.setGameMode(gameMode);
    }

    @Override
    public void onRedraw() {
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
                int i = (int)(Math.rint((x - BOARD_MARGIN) / mGridWidth));
                int j = (int)(Math.rint((y - BOARD_MARGIN) / mGridHeight));
                mController.putChess(i, j);
                break;
        }
        return super.onTouchEvent(event);
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
        int board[][] = mController.getBoardData();
        for (int row = 0; row < LINE_COUNT; row++){
            for (int col = 0; col < LINE_COUNT; col++){
                float x = BOARD_MARGIN + col * mGridWidth;
                float y = BOARD_MARGIN + row * mGridHeight;
                RectF rectF = new RectF(x - HALF_CHESS_SIZE, y - HALF_CHESS_SIZE, x + HALF_CHESS_SIZE, y + HALF_CHESS_SIZE);
                if (board[col][row] == POINT_WHITE){
                    canvas.drawBitmap(mWhiteChessBitmap, null, rectF, null);
                } else if (board[col][row] == POINT_BLACK){
                    canvas.drawBitmap(mBlackChessBitmap, null, rectF, null);
                }
            }
        }
    }
}
