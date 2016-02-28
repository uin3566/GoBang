package com.xuf.www.gobang.presenter;

import android.bluetooth.BluetoothDevice;
import android.net.wifi.p2p.WifiP2pDevice;

import com.peak.salut.SalutDevice;

import java.util.List;

/**
 * Created by Xuf on 2016/1/23.
 */
public interface INetInteratorCallback {

    void onMobileNotSupportDevice();

    void onWifiDeviceConnected(SalutDevice device);

    void onBlueToothDeviceConnected();

    void onBlueToothDeviceConnectFailed();

    void onStartWifiServiceFailed();

    void onFindWifiPeers(List<SalutDevice> deviceList);

    void onFindP2pPeers(List<WifiP2pDevice> deviceList);

    void onFindBlueToothPeers(List<BluetoothDevice> deviceList);

    void onPeersNotFound();

    void onDataReceived(Object o);

    void onSendMessageFailed();
}
