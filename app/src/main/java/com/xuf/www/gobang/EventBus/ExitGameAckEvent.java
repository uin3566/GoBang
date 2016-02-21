package com.xuf.www.gobang.eventbus;

/**
 * Created by Administrator on 2016/1/27.
 */
public class ExitGameAckEvent {

    public boolean mExit;

    public ExitGameAckEvent(boolean exit) {
        mExit = exit;
    }
}
