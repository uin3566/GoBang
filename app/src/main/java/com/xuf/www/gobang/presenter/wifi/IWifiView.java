package com.xuf.www.gobang.presenter.wifi;

import com.peak.salut.SalutDevice;

import java.util.List;

/**
 * Created by Xuf on 2016/1/23.
 */
public interface IWifiView {
    void onWifiInitFailed(String message);

    void onDeviceConnected(SalutDevice device);

    void onStartWifiServiceFailed();

    void onFindPeers(List<SalutDevice> deviceList);

    void onPeersNotFound();
}
