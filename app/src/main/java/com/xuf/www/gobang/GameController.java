package com.xuf.www.gobang;

/**
 * Created by lenov0 on 2015/12/8.
 */
public class GameController {

    public static final int BOARD_SIZE = 15;
    public static final int CHESS_NONE = 0;
    public static final int CHESS_WHITE = 1;
    public static final int CHESS_BLACK = 2;

    private int mGameMode = Constants.INVALID_MODE;
    private boolean mIsWhiteChess = false;
    private ReDrawCallback mCallback;
    private int[][] mBoard = new int[BOARD_SIZE][BOARD_SIZE];

    public GameController() {

    }

    public void setCallback(ReDrawCallback callback){
        mCallback = callback;
    }

    public void setGameMode(int gameMode){
        mGameMode = gameMode;
    }

    public int[][] getBoardData(){
        return mBoard;
    }

    public void putChess(int x, int y){
        if (mBoard[x][y] != CHESS_NONE){
            return;
        }

        if (mIsWhiteChess){
            mBoard[x][y] = CHESS_WHITE;
        } else {
            mBoard[x][y] = CHESS_BLACK;
        }
        if (mGameMode == Constants.COUPE_MODE) {
            mIsWhiteChess = !mIsWhiteChess;
        }

        if (mCallback != null){
            mCallback.onRedraw();
        }
    }

    public interface ReDrawCallback{
        void onRedraw();
    }
}
