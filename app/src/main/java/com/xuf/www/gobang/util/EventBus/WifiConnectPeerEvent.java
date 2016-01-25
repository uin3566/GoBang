package com.xuf.www.gobang.util.EventBus;

import com.peak.salut.SalutDevice;

/**
 * Created by Administrator on 2016/1/25.
 */
public class WifiConnectPeerEvent {

    public SalutDevice mDevice;

    public WifiConnectPeerEvent(SalutDevice device) {
        mDevice = device;
    }
}
