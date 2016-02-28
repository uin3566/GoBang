package com.xuf.www.gobang.util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.bluelinelabs.logansquare.LoganSquare;
import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by lenov0 on 2016/2/16.
 */
public class BlueToothWrapper {
    private static final String TAG = "BlueToothWrapper";

    private Context mContext;
    private BluetoothAdapter mAdapter;

    private static final UUID MY_UUID = UUID.fromString("b2a770c2-529e-4e80-933b-99dc372d3e65");
    private static final String APP_NAME = "GoBang";
    private DeviceDiscoveryListener mDeviceDiscoveryListener;
    private DeviceConnectListener mDeviceConnectListener;
    private DataListener mDataListener;

    private AcceptThread mAcceptThread = null;
    private ConnectThread mConnectThread = null;
    private DataTransferThread mDataTransferThread = null;

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

    public interface DeviceConnectListener {
        void onConnectResult(boolean success);
    }

    public interface DataListener {
        void onDataReceived(Object data);
    }

    public BlueToothWrapper(Context context) {
        mContext = context;
    }

    public boolean init() {
        if (initBlueTooth()) {
            registerReceiver();
            return true;
        }
        return false;
    }

    public void unInit() {
        unregisterReceiver();
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }
        if (mDataTransferThread != null) {
            mDataTransferThread.cancel();
            mDataTransferThread = null;
        }
    }

    public void setListener(DeviceConnectListener connectListener, DataListener dataListener) {
        mDeviceConnectListener = connectListener;
        mDataListener = dataListener;
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        mContext.registerReceiver(mReceiver, filter);
    }

    private void unregisterReceiver() {
        mContext.unregisterReceiver(mReceiver);
    }

    private boolean initBlueTooth() {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mAdapter != null) {
            if (!mAdapter.isEnabled()) {
                Intent enableBlueToothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                mContext.startActivity(enableBlueToothIntent);
            }
        }
        return mAdapter != null;
    }

    public void setDiscoverable() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        mContext.startActivity(discoverableIntent);
        startBlueToothService();
    }

    private void startBlueToothService() {
        if (mAdapter.isDiscovering()) {
            mAdapter.cancelDiscovery();
        }
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mDataTransferThread != null) {
            mDataTransferThread.cancel();
            mDataTransferThread = null;
        }

        if (mAcceptThread == null) {
            mAcceptThread = new AcceptThread();
            mAcceptThread.start();
        }
    }

    public void stopBlueToothService() {
        if (mDataTransferThread != null) {
            mDataTransferThread.cancel();
            mDataTransferThread = null;
        }

        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }
    }

    public void connectToDevice(BluetoothDevice device) {
        if (mAdapter.isDiscovering()) {
            mAdapter.cancelDiscovery();
        }
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (mDataTransferThread != null) {
            mDataTransferThread.cancel();
            mDataTransferThread = null;
        }
        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }

        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
    }

    public List<BluetoothDevice> getPairedDevices() {
        Set<BluetoothDevice> bondedDevices = mAdapter.getBondedDevices();
        return new ArrayList<>(bondedDevices);
    }

    public boolean discoveryDevices(DeviceDiscoveryListener discoveryListener) {
        if (mAdapter.isDiscovering()) {
            mAdapter.cancelDiscovery();
        }
        mDeviceDiscoveryListener = discoveryListener;
        return mAdapter.startDiscovery();
    }

    private void manageConnectedSocket(BluetoothSocket socket) {
        if (mDataTransferThread != null) {
            mDataTransferThread.cancel();
            mDataTransferThread = null;
        }

        if (mDeviceConnectListener != null) {
            if (mContext instanceof Activity) {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDeviceConnectListener.onConnectResult(true);
                    }
                });
            }
        }
        mDataTransferThread = new DataTransferThread(socket);
        mDataTransferThread.start();
    }

    public void sendData(Object data) {
        if (mDataTransferThread != null) {
            mDataTransferThread.sendData(data);
        }
    }

    private class AcceptThread extends Thread {
        private BluetoothServerSocket mServerSocket = null;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            try {
                tmp = mAdapter.listenUsingRfcommWithServiceRecord(APP_NAME, MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mServerSocket = tmp;
        }

        @Override
        public void run() {
            if (mServerSocket == null) {
                return;
            }
            BluetoothSocket socket = null;
            try {
                socket = mServerSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (socket != null) {
                manageConnectedSocket(socket);
                try {
                    mServerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void cancel() {
            if (mServerSocket != null) {
                try {
                    mServerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class ConnectThread extends Thread {
        private BluetoothSocket mSocket = null;

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mSocket = tmp;
        }

        @Override
        public void run() {
            if (mSocket == null) {
                return;
            }
            if (mAdapter.isDiscovering()) {
                mAdapter.cancelDiscovery();
            }
            try {
                mSocket.connect();
            } catch (IOException e1) {
                e1.printStackTrace();
                if (mDeviceConnectListener != null) {
                    if (mContext instanceof Activity) {
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDeviceConnectListener.onConnectResult(false);
                            }
                        });
                    }
                }
                try {
                    mSocket.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                return;
            }
            manageConnectedSocket(mSocket);
        }

        public void cancel() {
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class DataTransferThread extends Thread {
        private BluetoothSocket mSocket;
        private InputStream mInputStream;
        private OutputStream mOutputStream;

        public DataTransferThread(BluetoothSocket socket) {
            mSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = mSocket.getInputStream();
                tmpOut = mSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mInputStream = tmpIn;
            mOutputStream = tmpOut;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            while (true) {
                try {
                    bytes = mInputStream.read(buffer);
                    byte[] tmp = Arrays.copyOf(buffer, bytes);
                    final String data = new String(tmp, Charsets.UTF_8);
                    Log.i(TAG, "res:" + data);
                    if (!data.isEmpty()) {
                        if (mContext instanceof Activity) {
                            ((Activity) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mDataListener.onDataReceived(data);
                                }
                            });
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        public void sendData(Object data) {
            try {
                String strData = LoganSquare.serialize(data);
                byte[] buffer;
                buffer = strData.getBytes(Charsets.UTF_8);
                mOutputStream.write(buffer);
                Log.i(TAG, strData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void cancel() {
            if (mSocket != null) {
                try {
                    mSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}