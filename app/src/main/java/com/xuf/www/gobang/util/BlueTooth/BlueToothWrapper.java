package com.xuf.www.gobang.util.BlueTooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.Set;

/**
 * Created by lenov0 on 2016/2/16.
 */
public class BlueToothWrapper {

    private Context mContext;
    private BluetoothAdapter mAdapter;

    private DeviceDiscoveryListener mDeviceDiscoveryListener;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (mDeviceDiscoveryListener != null) {
                    mDeviceDiscoveryListener.onDeviceFounded(device);
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (mDeviceDiscoveryListener != null) {
                    mDeviceDiscoveryListener.onDiscoveryFinished();
                }
            }
        }
    };

    public interface DeviceDiscoveryListener {
        void onDeviceFounded(BluetoothDevice device);

        void onDiscoveryFinished();
    }

    public BlueToothWrapper(Context context) {
        mContext = context;
    }

    public void setDeviceFoundListener(DeviceDiscoveryListener deviceDiscoveryListener) {
        mDeviceDiscoveryListener = deviceDiscoveryListener;
    }

    public void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        mContext.registerReceiver(mReceiver, filter);
    }

    public void unregisterReceiver() {
        mContext.unregisterReceiver(mReceiver);
    }

    public boolean initBlueTooth() {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        return mAdapter != null;
    }

    public void setDiscoverable() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        mContext.startActivity(discoverableIntent);
    }

    public Set<BluetoothDevice> getPairedDevices() {
        return mAdapter.getBondedDevices();
    }

    public boolean discoveryDevices() {
        if (mAdapter.isDiscovering()) {
            mAdapter.cancelDiscovery();
        }
        return mAdapter.startDiscovery();
    }
}
