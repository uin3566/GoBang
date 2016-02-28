package com.xuf.www.gobang.presenter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.peak.salut.SalutDevice;
import com.xuf.www.gobang.bean.Message;
import com.xuf.www.gobang.interator.NetInteractor;
import com.xuf.www.gobang.interator.BlueToothInteractor;
import com.xuf.www.gobang.interator.WifiInteractor;
import com.xuf.www.gobang.util.Constants;

import java.util.List;

/**
 * Created by Xuf on 2016/1/23.
 */
public class NetPresenter implements INetInteratorCallback {
    private INetView mNetView;
    private NetInteractor mNetInteractor;

    private int mGameMode;

    public NetPresenter(Context context, INetView netView, int gameMode) {
        mNetView = netView;
        mGameMode = gameMode;
        if (isWifiMode()) {
            mNetInteractor = new WifiInteractor(context, this);
        } else {
            mNetInteractor = new BlueToothInteractor(context, this);
        }
    }

    private boolean isWifiMode() {
        return mGameMode == Constants.WIFI_MODE;
    }

    public void init() {
        mNetInteractor.init();
    }

    public void unInit() {
        mNetInteractor.unInit();
    }

    public void startService() {
        mNetInteractor.startNetService();
    }

    public void stopService() {
        mNetInteractor.stopNetService();
    }

    public void sendToDevice(Message message, boolean isHost) {
        mNetInteractor.sendToDevice(message, isHost);
    }

    public void findPeers() {
        mNetInteractor.findPeers();
    }

    public void connectToHost(SalutDevice salutHost, BluetoothDevice blueToothHost) {
        mNetInteractor.connectToHost(salutHost, blueToothHost);
    }

    @Override
    public void onWifiDeviceConnected(SalutDevice device) {
        mNetView.onWifiDeviceConnected(device);
    }

    @Override
    public void onBlueToothDeviceConnected() {
        mNetView.onBlueToothDeviceConnected();
    }

    @Override
    public void onBlueToothDeviceConnectFailed() {
        mNetView.onBlueToothDeviceConnectFailed();
    }

    @Override
    public void onStartWifiServiceFailed() {
        mNetView.onStartWifiServiceFailed();
    }

    @Override
    public void onFindWifiPeers(List<SalutDevice> deviceList) {
        mNetView.onFindWifiPeers(deviceList);
    }

    @Override
    public void onGetPairedToothPeers(List<BluetoothDevice> deviceList) {
        mNetView.onGetPairedToothPeers(deviceList);
    }

    @Override
    public void onFindBlueToothPeers(List<BluetoothDevice> deviceList) {
        mNetView.onFindBlueToothPeers(deviceList);
    }

    @Override
    public void onPeersNotFound() {
        mNetView.onPeersNotFound();
    }

    @Override
    public void onDataReceived(Object o) {
        mNetView.onDataReceived(o);
    }

    @Override
    public void onSendMessageFailed() {
        mNetView.onSendMessageFailed();
    }

    @Override
    public void onMobileNotSupportDevice() {
        String message = "抱歉，您的设备不支持wifi直连";
        mNetView.onWifiInitFailed(message);
    }
}
