package com.xuf.www.gobang.bean;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by Xuf on 2016/1/10.
 */
@JsonObject
public class Message {
    public static final int MSG_TYPE_HOST_BEGIN = 0;
    public static final int MSG_TYPE_BEGIN_ACK = 1;
    public static final int MSG_TYPE_GAME_DATA = 2;
    public static final int MSG_TYPE_GAME_END = 3;
    public static final int MSG_TYPE_GAME_RESTART_REQ = 4;
    public static final int MSG_TYPE_GAME_RESTART_RESP = 5;

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
}
