package com.xuf.www.gobang.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.xuf.www.gobang.R;
import com.xuf.www.gobang.bean.Point;
import com.xuf.www.gobang.util.Constants;
import com.xuf.www.gobang.util.DimenUtil;

/**
 * Created by Administrator on 2015/12/8.
 */
public class GoBangBoard extends View {

    private static final int LINE_COUNT = 15;
    private static final int BOARD_MARGIN = 40;
    private static final int HALF_CHESS_SIZE = 35;

    private static final int BOARD_SIZE = LINE_COUNT;
    private static final float BOARD_LINE_WIDTH_DP = 0.7f;//棋盘线宽度
    private static final float BOARD_FRAME_WIDTH_DP = 1;//棋盘框的线宽度
    private static final float BOARD_POINT_RADIUS_DP = 2;//棋盘五个圆点的半径宽度

    private int[][] mBoard = new int[BOARD_SIZE][BOARD_SIZE];
    private int mLastPutX;
    private int mLastPutY;

    private Context mContext;

    private Bitmap mWhiteChessBitmap;
    private Bitmap mBlackChessBitmap;

    private Paint mPointPaint;//画圆点
    private Paint mLinePaint;//画线

    private float[] mBoardFramePoints;//棋盘边框
    private float[] mVerticalLinePoints;//棋盘竖线
    private float[] mHorizontalLinePoints;//棋盘横线
    private float[] mBlackPoints;//棋盘黑点

    private boolean mShouldDrawRedFlag = false;
    private int mLineCount;
    private float mGridWidth;
    private float mGridHeight;

    private PutChessListener mPutChessListener;

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

    private void init(Context context) {
        mContext = context;

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(Color.BLACK);

        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);

        mLineCount = LINE_COUNT;

        mBlackChessBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.chess_black);
        mWhiteChessBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.chess_white);
    }

    public void setPutChessListener(PutChessListener listener) {
        mPutChessListener = listener;
    }

    public void clearBoard() {
        for (int row = 0; row < LINE_COUNT; row++) {
            for (int col = 0; col < LINE_COUNT; col++) {
                mBoard[col][row] = Constants.CHESS_NONE;
            }
        }
        mShouldDrawRedFlag = false;
        invalidate();
    }

    private void calcLinePoints() {
        mHorizontalLinePoints = new float[mLineCount * 4];
        mVerticalLinePoints = new float[mLineCount * 4];

        float boardWidth = getMeasuredWidth() - BOARD_MARGIN * 2;
        float boardHeight = getMeasuredHeight() - BOARD_MARGIN * 2;

        mGridWidth = boardWidth / (mLineCount - 1);
        for (int i = 0; i < mLineCount * 4; i += 4) {
            mVerticalLinePoints[i] = i * mGridWidth / 4 + BOARD_MARGIN;
            mVerticalLinePoints[i + 1] = BOARD_MARGIN;
            mVerticalLinePoints[i + 2] = i * mGridWidth / 4 + BOARD_MARGIN;
            mVerticalLinePoints[i + 3] = boardHeight + BOARD_MARGIN;
        }

        mGridHeight = boardHeight / (mLineCount - 1);
        for (int i = 0; i < mLineCount * 4; i += 4) {
            mHorizontalLinePoints[i] = BOARD_MARGIN;
            mHorizontalLinePoints[i + 1] = i * mGridHeight / 4 + BOARD_MARGIN;
            mHorizontalLinePoints[i + 2] = boardWidth + BOARD_MARGIN;
            mHorizontalLinePoints[i + 3] = i * mGridHeight / 4 + BOARD_MARGIN;
        }

        float frameMargin = BOARD_MARGIN * 0.8f;
        mBoardFramePoints = new float[]{frameMargin, frameMargin, getMeasuredWidth() - frameMargin, frameMargin,//上横
                frameMargin, getMeasuredHeight() - frameMargin, getMeasuredWidth() - frameMargin, getMeasuredHeight() - frameMargin,//下横
                frameMargin, frameMargin, frameMargin, getMeasuredHeight() - frameMargin,//左竖
                getMeasuredWidth() - frameMargin, frameMargin, getMeasuredWidth() - frameMargin, getMeasuredHeight() - frameMargin};//右竖

        mBlackPoints = new float[]{3 * mGridWidth + BOARD_MARGIN, 3 * mGridHeight + BOARD_MARGIN,
                11 * mGridWidth + BOARD_MARGIN, 3 * mGridHeight + BOARD_MARGIN,
                7 * mGridWidth + BOARD_MARGIN, 7 * mGridHeight + BOARD_MARGIN,
                3 * mGridWidth + BOARD_MARGIN, 11 * mGridHeight + BOARD_MARGIN,
                11 * mGridWidth + BOARD_MARGIN, 11 * mGridHeight + BOARD_MARGIN};
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, width);
        calcLinePoints();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLines(canvas);
        drawBlackPoints(canvas);
        drawChess(canvas);
        drawRedFlag(canvas);
    }

    public Point convertPoint(float x, float y) {
        int i = (int) (Math.rint((x - BOARD_MARGIN) / mGridWidth));
        int j = (int) (Math.rint((y - BOARD_MARGIN) / mGridHeight));
        Point point = new Point();
        point.setXY(i, j);
        return point;
    }

    public void moveBack(Point currentPoint) {
        mBoard[mLastPutX][mLastPutY] = Constants.CHESS_NONE;
        if (currentPoint != null){
            mLastPutX = currentPoint.x;
            mLastPutY = currentPoint.y;
            mShouldDrawRedFlag = true;
        } else {
            mLastPutX = 0;
            mLastPutY = 0;
            mShouldDrawRedFlag = false;
        }
        invalidate();
    }

    public boolean putChess(boolean isWhite, int x, int y) {
        if (mBoard[x][y] != Constants.CHESS_NONE) {
            return false;
        }

        if (isWhite) {
            mBoard[x][y] = Constants.CHESS_WHITE;
        } else {
            mBoard[x][y] = Constants.CHESS_BLACK;
        }
        mLastPutX = x;
        mLastPutY = y;
        mShouldDrawRedFlag = true;

        mPutChessListener.onPutChess(mBoard, x, y);
        invalidate();
        return true;
    }

    private void drawRedFlag(Canvas canvas) {
        if (mShouldDrawRedFlag) {
            float coordinateX = BOARD_MARGIN + mLastPutX * mGridWidth;
            float coordinateY = BOARD_MARGIN + mLastPutY * mGridHeight;
            mPointPaint.setColor(Color.RED);
            canvas.drawCircle(coordinateX, coordinateY, DimenUtil.dp2px(mContext, BOARD_POINT_RADIUS_DP), mPointPaint);
        }
    }

    private void drawLines(Canvas canvas) {
        mLinePaint.setStrokeWidth(DimenUtil.dp2px(mContext, BOARD_LINE_WIDTH_DP));
        canvas.drawLines(mHorizontalLinePoints, mLinePaint);
        canvas.drawLines(mVerticalLinePoints, mLinePaint);
        mLinePaint.setStrokeWidth(DimenUtil.dp2px(mContext, BOARD_FRAME_WIDTH_DP));
        canvas.drawLines(mBoardFramePoints, mLinePaint);
    }

    private void drawBlackPoints(Canvas canvas) {
        mPointPaint.setColor(Color.BLACK);
        for (int i = 0; i < mBlackPoints.length; i += 2) {
            canvas.drawCircle(mBlackPoints[i], mBlackPoints[i + 1], DimenUtil.dp2px(mContext, BOARD_POINT_RADIUS_DP), mPointPaint);
        }
    }

    private void drawChess(Canvas canvas) {
        for (int row = 0; row < LINE_COUNT; row++) {
            for (int col = 0; col < LINE_COUNT; col++) {
                float x = BOARD_MARGIN + col * mGridWidth;
                float y = BOARD_MARGIN + row * mGridHeight;
                RectF rectF = new RectF(x - HALF_CHESS_SIZE, y - HALF_CHESS_SIZE, x + HALF_CHESS_SIZE, y + HALF_CHESS_SIZE);
                if (mBoard[col][row] == Constants.CHESS_WHITE) {
                    canvas.drawBitmap(mWhiteChessBitmap, null, rectF, null);
                } else if (mBoard[col][row] == Constants.CHESS_BLACK) {
                    canvas.drawBitmap(mBlackChessBitmap, null, rectF, null);
                }
            }
        }
    }

    public interface PutChessListener {
        void onPutChess(int[][] board, int x, int y);
    }
}
