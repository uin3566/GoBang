package com.xuf.www.gobang.presenter;

import android.bluetooth.BluetoothDevice;

import com.peak.salut.SalutDevice;

import java.util.List;

/**
 * Created by Xuf on 2016/1/23.
 */
public interface INetView {
    void onWifiInitFailed(String message);

    void onWifiDeviceConnected(SalutDevice device);

    void onBlueToothDeviceConnected();

    void onBlueToothDeviceConnectFailed();

    void onStartWifiServiceFailed();

    void onFindWifiPeers(List<SalutDevice> deviceList);

    void onGetPairedToothPeers(List<BluetoothDevice> deviceList);

    void onFindBlueToothPeers(List<BluetoothDevice> deviceList);

    void onPeersNotFound();

    void onDataReceived(Object o);

    void onSendMessageFailed();
}
