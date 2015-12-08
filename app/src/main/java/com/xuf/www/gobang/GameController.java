package com.xuf.www.gobang;

/**
 * Created by lenov0 on 2015/12/8.
 */
public class GameController {

    public static final int BOARD_SIZE = 15;
    public static final int CHESS_NONE = 0;
    public static final int CHESS_WHITE = 1;
    public static final int CHESS_BLACK = 2;

    private ReDrawCallback mCallback;
    private int[][] mBoard = new int[BOARD_SIZE][BOARD_SIZE];

    public GameController() {

    }

    public void setCallback(ReDrawCallback callback){
        mCallback = callback;
    }

    public int[][] getBoardData(){
        return mBoard;
    }

    public void putChess(int x, int y, boolean whiteChess){
        if (whiteChess){
            mBoard[x][y] = CHESS_WHITE;
        } else {
            mBoard[x][y] = CHESS_BLACK;
        }
        if (mCallback != null){
            mCallback.onRedraw();
        }
    }

    public interface ReDrawCallback{
        void onRedraw();
    }
}
