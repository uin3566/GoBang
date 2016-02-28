package com.xuf.www.gobang.util;

import android.text.BoringLayout;

/**
 * Created by Administrator on 2016/1/27.
 */
public class GameJudger {

    private static enum DIRECT {
        VERTICAL,
        HORIZONTAL,
        LEFT_TILT,
        RIGHT_TILT
    }

    public static boolean isGameEnd(int[][] board, int x, int y) {
        if (getLinkCount(board, x, y, DIRECT.VERTICAL) == 5 || getLinkCount(board, x, y, DIRECT.HORIZONTAL) == 5
                || getLinkCount(board, x, y, DIRECT.LEFT_TILT) == 5 || getLinkCount(board, x, y, DIRECT.RIGHT_TILT) == 5) {
            return true;
        }

        return false;
    }

    private static int getLinkCount(final int[][] board, final int x, final int y, final DIRECT direct) {
        int linkCount = 0;
        int tmpX = x;
        int tmpY = y;

        switch (direct) {
            case VERTICAL:
                //上
                while (tmpY >= 0 && board[tmpX][tmpY] == board[x][y]) {
                    linkCount++;
                    tmpY--;
                    if (linkCount == 5) {
                        return linkCount;
                    }
                }
                tmpY = y;
                //下
                while (tmpY < board.length - 1 && board[tmpX][tmpY + 1] == board[x][y]) {
                    linkCount++;
                    tmpY++;
                    if (linkCount == 5) {
                        return linkCount;
                    }
                }
                break;
            case HORIZONTAL:
                //左
                while (tmpX >= 0 && board[tmpX][tmpY] == board[x][y]) {
                    linkCount++;
                    tmpX--;
                    if (linkCount == 5) {
                        return linkCount;
                    }
                }
                tmpX = x;
                //右
                while (tmpX < board.length - 1 && board[tmpX + 1][tmpY] == board[x][y]) {
                    linkCount++;
                    tmpX++;
                    if (linkCount == 5) {
                        return linkCount;
                    }
                }
                break;
            case LEFT_TILT:
                //左上
                while (tmpX >= 0 && tmpY >= 0 && board[tmpX][tmpY] == board[x][y]) {
                    linkCount++;
                    tmpX--;
                    tmpY--;
                    if (linkCount == 5) {
                        return linkCount;
                    }
                }
                tmpX = x;
                tmpY = y;
                //右下
                while (tmpX < board.length - 1 && tmpY < board.length - 1 && board[tmpX + 1][tmpY + 1] == board[x][y]) {
                    linkCount++;
                    tmpX++;
                    tmpY++;
                    if (linkCount == 5) {
                        return linkCount;
                    }
                }
                break;
            case RIGHT_TILT:
                //右上
                while (tmpX < board.length && tmpY >= 0 && board[tmpX][tmpY] == board[x][y]) {
                    linkCount++;
                    tmpX++;
                    tmpY--;
                    if (linkCount == 5) {
                        return linkCount;
                    }
                }
                tmpX = x;
                tmpY = y;
                //左下
                while (tmpX > 0 && tmpY < board.length - 1 && board[tmpX - 1][tmpY + 1] == board[x][y]) {
                    linkCount++;
                    tmpX--;
                    tmpY++;
                    if (linkCount == 5) {
                        return linkCount;
                    }
                }
                break;
        }

        return linkCount;
    }
}
