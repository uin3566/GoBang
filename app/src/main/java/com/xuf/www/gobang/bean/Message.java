package com.xuf.www.gobang.bean;

import android.os.Build;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by Xuf on 2016/1/10.
 */
@JsonObject
public class Message {
    public static final int MSG_TYPE_HOST_BEGIN = 0;//房主开始游戏
    public static final int MSG_TYPE_BEGIN_ACK = 1;//玩家收到房主开始游戏的确认消息
    public static final int MSG_TYPE_GAME_DATA = 2;//游戏数据消息，包括下子位置
    public static final int MSG_TYPE_GAME_END = 3;
    public static final int MSG_TYPE_GAME_RESTART_REQ = 4;//重新开始游戏请求
    public static final int MSG_TYPE_GAME_RESTART_RESP = 5;//重新开始游戏应答
    public static final int MSG_TYPE_EXIT = 6;
    public static final int MSG_TYPE_MOVE_BACK_REQ = 7;//悔棋请求
    public static final int MSG_TYPE_MOVE_BACK_RESP = 8;//悔棋应答

    @JsonField
    public int mMessageType;

    @JsonField
    public boolean mIsWhite;

    @JsonField
    public Point mGameData;

    @JsonField
    public String mMessage;

    @JsonField
    public boolean mAgreeRestart;

    @JsonField
    public boolean mAgreeMoveBack;

    //使用@JsonObject必须提供public默认构造函数
    public Message() {

    }

    private Message(Builder builder) {
        this.mMessageType = builder.mMessageType;
        this.mIsWhite = builder.mIsWhite;
        this.mGameData = builder.mGameData;
        this.mMessage = builder.mMessage;
        this.mAgreeRestart = builder.mAgreeRestart;
        this.mAgreeMoveBack = builder.mAgreeMoveBack;
    }

    //组装消息时建议使用Builder
    public static class Builder {
        private final int mMessageType;
        private boolean mIsWhite;
        private Point mGameData;
        private String mMessage;
        private boolean mAgreeRestart;
        private boolean mAgreeMoveBack;

        public Builder(int messageType) {
            mMessageType = messageType;
        }

        public Builder isWhite(boolean isWhite) {
            mIsWhite = isWhite;
            return this;
        }

        public Builder gameData(Point gameData) {
            mGameData = gameData;
            return this;
        }

        public Builder message(String message) {
            mMessage = message;
            return this;
        }

        public Builder agreeRestart(boolean agreeRestart) {
            mAgreeRestart = agreeRestart;
            return this;
        }

        public Builder agreeMoveBack(boolean agreeMoveBack) {
            mAgreeMoveBack = agreeMoveBack;
            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }
}
