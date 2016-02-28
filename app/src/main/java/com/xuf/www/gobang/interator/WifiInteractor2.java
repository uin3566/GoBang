package com.xuf.www.gobang.interator;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;

import com.peak.salut.SalutDevice;
import com.xuf.www.gobang.bean.Message;
import com.xuf.www.gobang.presenter.INetInteratorCallback;
import com.xuf.www.gobang.util.WifiWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenov0 on 2016/2/27.
 */
public class WifiInteractor2 extends NetInteractor implements WifiWrapper.WifiInterface{
    private WifiWrapper mWifiWrapper;
    private INetInteratorCallback mCallback;

    public WifiInteractor2(Context context, INetInteratorCallback callback) {
        mWifiWrapper = new WifiWrapper(context, this);
        mCallback = callback;
    }

    @Override
    public boolean init() {
        mWifiWrapper.init();
        return true;
    }

    @Override
    public void unInit() {
        super.unInit();
        mWifiWrapper.uninit();
    }

    @Override
    public void startNetService() {
        super.startNetService();
        mWifiWrapper.discoveryPeers();
    }

    @Override
    public void stopNetService() {
        super.stopNetService();
    }

    @Override
    public void sendToDevice(Message message, boolean isHost) {
        super.sendToDevice(message, isHost);
    }

    @Override
    public void findPeers() {
        super.findPeers();
        mWifiWrapper.discoveryPeers();
    }

    @Override
    public void connectToHost(WifiP2pDevice wifiP2pDevice, SalutDevice salutHost, BluetoothDevice blueToothHost) {
        super.connectToHost(wifiP2pDevice, salutHost, blueToothHost);
        mWifiWrapper.connectToDevice(wifiP2pDevice);
    }

    @Override
    public void onPeersFound(WifiP2pDeviceList p2pDeviceList) {
        List<WifiP2pDevice> list = new ArrayList<>();
        list.addAll(p2pDeviceList.getDeviceList());
        mCallback.onFindP2pPeers(list);
    }
}
