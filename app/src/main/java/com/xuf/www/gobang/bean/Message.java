package com.xuf.www.gobang.bean;

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
    public static final int MSG_TYPE_GAME_RESTART_REQ = 4;
    public static final int MSG_TYPE_GAME_RESTART_RESP = 5;
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
}
