package com.xuf.www.gobang.EventBus;

import android.bluetooth.BluetoothDevice;
import android.net.wifi.p2p.WifiP2pDevice;

import com.peak.salut.SalutDevice;

/**
 * Created by Administrator on 2016/1/25.
 */
public class ConnectPeerEvent {

    public SalutDevice mSalutDevice;
    public BluetoothDevice mBlueToothDevice;
    public WifiP2pDevice mP2pDevice;

    public ConnectPeerEvent(WifiP2pDevice p2pDevice, SalutDevice device, BluetoothDevice bluetoothDevice) {
        mP2pDevice = p2pDevice;
        mSalutDevice = device;
        mBlueToothDevice = bluetoothDevice;
    }
}
