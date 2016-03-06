package com.xuf.www.gobang.util;

import com.xuf.www.gobang.bean.Message;
import com.xuf.www.gobang.bean.Point;

/**
 * Created by lenov0 on 2016/1/25.
 */
public class MessageWrapper {

    public static Message getHostBeginMessage() {
        return new Message.Builder(Message.MSG_TYPE_HOST_BEGIN).build();
    }

    public static Message getHostBeginAckMessage() {
        return new Message.Builder(Message.MSG_TYPE_BEGIN_ACK).build();
    }

    public static Message getSendDataMessage(Point point, boolean isWhite) {
        return new Message.Builder(Message.MSG_TYPE_GAME_DATA).gameData(point).isWhite(isWhite).build();
    }

    public static Message getGameEndMessage(String endMessage) {
        return new Message.Builder(Message.MSG_TYPE_GAME_END).message(endMessage).build();
    }

    public static Message getGameRestartReqMessage() {
        return new Message.Builder(Message.MSG_TYPE_GAME_RESTART_REQ).build();
    }

    public static Message getGameRestartRespMessage(boolean agreeRestart) {
        return new Message.Builder(Message.MSG_TYPE_GAME_RESTART_RESP).agreeRestart(agreeRestart).build();
    }

    public static Message getGameExitMessage() {
        return new Message.Builder(Message.MSG_TYPE_EXIT).build();
    }

    public static Message getGameMoveBackReqMessage() {
        return new Message.Builder(Message.MSG_TYPE_MOVE_BACK_REQ).build();
    }

    public static Message getGameMoveBackRespMessage(boolean agreeMoveBack) {
        return new Message.Builder(Message.MSG_TYPE_MOVE_BACK_RESP).agreeMoveBack(agreeMoveBack).build();
    }
}
