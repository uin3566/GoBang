package com.xuf.www.gobang.presenter.wifi;

import android.content.Context;

import com.peak.salut.Salut;
import com.peak.salut.SalutDevice;
import com.xuf.www.gobang.bean.Message;
import com.xuf.www.gobang.bean.Point;
import com.xuf.www.gobang.interator.wifi.WifiInteractor;

import java.util.List;

/**
 * Created by Xuf on 2016/1/23.
 */
public class WifiPresenter implements IWifiInteratorCallback {
    private IWifiView mWifiView;
    private WifiInteractor mWifiInterator;

    public WifiPresenter(Context context, IWifiView wifiView) {
        mWifiView = wifiView;
        mWifiInterator = new WifiInteractor(context, this);
    }

    public void initWifiNet() {
        mWifiInterator.initWifiNet();
    }

    public void unInitWifiNet(boolean isHost) {
        mWifiInterator.unInitWifiNet(isHost);
    }

    public void startWifiService() {
        mWifiInterator.startWifiService();
    }

    public void sendMessage(Message message) {
        mWifiInterator.sendMessage(message);
    }

    public void findPeers() {
        mWifiInterator.findPeers();
    }

    public void connectToHost(SalutDevice host) {
        mWifiInterator.connectToHost(host);
    }

    @Override
    public void onDeviceConnected(SalutDevice device) {
        mWifiView.onDeviceConnected(device);
    }

    @Override
    public void onStartWifiServiceFailed() {
        mWifiView.onStartWifiServiceFailed();
    }

    @Override
    public void onFindPeers(List<SalutDevice> deviceList) {
        mWifiView.onFindPeers(deviceList);
    }

    @Override
    public void onPeersNotFound() {
        mWifiView.onPeersNotFound();
    }

    @Override
    public void onDataReceived(Object o) {
        mWifiView.onDataReceived(o);
    }

    @Override
    public void onSendMessageFailed() {
        mWifiView.onSendMessageFailed();
    }

    @Override
    public void onMobileNotSupportDevice() {
        String message = "抱歉，您的设备不支持wifi直连";
        mWifiView.onWifiInitFailed(message);
    }
}
