package com.xuf.www.gobang.interator;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.peak.salut.SalutDevice;
import com.xuf.www.gobang.bean.Message;
import com.xuf.www.gobang.presenter.INetInteratorCallback;
import com.xuf.www.gobang.util.BlueToothWrapper;

import java.util.List;

/**
 * Created by lenov0 on 2016/2/21.
 */
public class BlueToothInteractor extends NetInteractor {

    private static final String TAG = "BlueToothInteractor";

    private Context mContext;

    private BlueToothWrapper mBlueToothWrapper;
    private INetInteratorCallback mCallback;

    private BlueToothWrapper.DeviceConnectListener mDeviceConnectListener;
    private BlueToothWrapper.DataListener mDataListener;

    public BlueToothInteractor(Context context, INetInteratorCallback callback) {
        mBlueToothWrapper = new BlueToothWrapper(context);
        mCallback = callback;
        mContext = context;
    }

    public boolean init() {
        boolean success = mBlueToothWrapper.init();
        setListener();
        return success;
    }

    public void unInit() {
        mBlueToothWrapper.unInit();
    }

    private void setListener() {
        mDeviceConnectListener = new BlueToothWrapper.DeviceConnectListener() {
            @Override
            public void onConnectResult(boolean success) {
                if (success) {
                    mCallback.onBlueToothDeviceConnected();
                } else {
                    mCallback.onBlueToothDeviceConnectFailed();
                }
            }
        };
        mDataListener = new BlueToothWrapper.DataListener() {
            @Override
            public void onDataReceived(Object data) {
                mCallback.onDataReceived(data);
            }
        };
        mBlueToothWrapper.setListener(mDeviceConnectListener, mDataListener);
    }

    public void startNetService() {
        mBlueToothWrapper.setDiscoverable();
    }

    public void stopNetService() {
        mBlueToothWrapper.stopBlueToothService();
    }

    public void findPeers() {
        final List<BluetoothDevice> pairedDevices = mBlueToothWrapper.getPairedDevices();
        mBlueToothWrapper.discoveryDevices(new BlueToothWrapper.DeviceDiscoveryListener() {
            @Override
            public void onDeviceFounded(BluetoothDevice device) {
                if (!pairedDevices.contains(device)) {
                    pairedDevices.add(device);
                }
            }

            @Override
            public void onDiscoveryFinished() {
                mCallback.onFindBlueToothPeers(pairedDevices);
            }
        });
    }

    public void connectToHost(SalutDevice salutHost, BluetoothDevice blueToothHost) {
        mBlueToothWrapper.connectToDevice(blueToothHost);
    }

    public void sendToDevice(Message message, boolean isHost) {
        mBlueToothWrapper.sendData(message);
    }
}