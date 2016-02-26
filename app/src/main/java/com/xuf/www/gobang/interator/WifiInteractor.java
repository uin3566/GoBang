package com.xuf.www.gobang.interator;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Callbacks.SalutDataCallback;
import com.peak.salut.Callbacks.SalutDeviceCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutDevice;
import com.peak.salut.SalutServiceData;
import com.xuf.www.gobang.bean.Message;
import com.xuf.www.gobang.presenter.INetInteratorCallback;

/**
 * Created by Xuf on 2016/1/23.
 */
public class WifiInteractor extends NetInteractor implements SalutDataCallback {
    private static final String TAG = "WifiInteractor";

    private INetInteratorCallback mCallback;

    private Context mContext;

    private Salut mSalut;

    private SalutDevice mSendToDevice;

    public WifiInteractor(Context context, INetInteratorCallback callback) {
        mCallback = callback;
        mContext = context;
    }

    public boolean init() {
        if (!Salut.isWiFiEnabled(mContext)) {
            Salut.enableWiFi(mContext);
        }
        SalutDataReceiver mDataReceiver = new SalutDataReceiver((Activity) mContext, this);
        SalutServiceData mServiceData = new SalutServiceData("server", 5432, "xuf");
        mSalut = new Salut(mDataReceiver, mServiceData, new SalutCallback() {
            @Override
            public void call() {
                mCallback.onMobileNotSupportDevice();
            }
        });

        return true;
    }

    public void unInit() {
        if (mSalut.isRunningAsHost) {
            mSalut.stopNetworkService(false);
        } else {
            mSalut.unregisterClient(false);
        }
    }

    public void startNetService() {
        mSalut.startNetworkService(new SalutDeviceCallback() {
            @Override
            public void call(SalutDevice salutDevice) {
                Log.i(TAG, "startNetworkService, onWifiDeviceConnected, device:" + salutDevice.deviceName);
                mSendToDevice = salutDevice;
                mCallback.onWifiDeviceConnected(salutDevice);
            }
        }, new SalutCallback() {
            @Override
            public void call() {
                Log.i(TAG, "startNetworkService, init success");
            }
        }, new SalutCallback() {
            @Override
            public void call() {
                Log.i(TAG, "startNetworkService, init failed");
                mCallback.onStartWifiServiceFailed();
            }
        });
    }

    public void stopNetService(){
        if (mSalut.isRunningAsHost){
            mSalut.stopNetworkService(false);
        } else {
            mSalut.unregisterClient(false);
        }
    }

    public void findPeers() {
        mSalut.discoverWithTimeout(new SalutCallback() {
            @Override
            public void call() {
                Log.i(TAG, "discoverWithTimeout, onDeviceFound" + mSalut.foundDevices.toString());
                mCallback.onFindWifiPeers(mSalut.foundDevices);
            }
        }, new SalutCallback() {
            @Override
            public void call() {
                Log.i(TAG, "discoverWithTimeout, onDeviceNotFound");
                mCallback.onPeersNotFound();
            }
        }, 6000);
    }

    public void connectToHost(SalutDevice salutHost, BluetoothDevice blueToothHost) {
        mSendToDevice = salutHost;
        mSalut.registerWithHost(salutHost, new SalutCallback() {
            @Override
            public void call() {
                Log.i(TAG, "registerWithHost, registered success");
            }
        }, new SalutCallback() {
            @Override
            public void call() {
                Log.i(TAG, "registerWithHost, registered failed");
            }
        });
    }

    public void sendToDevice(Message message, boolean isHost) {
        if (isHost) {
            if (mSendToDevice != null) {
                mSalut.sendToDevice(mSendToDevice, message, new SalutCallback() {
                    @Override
                    public void call() {
                        Log.i(TAG, "sendToDevice, send data failed");
                        mCallback.onSendMessageFailed();
                    }
                });
            }
        } else {
            mSalut.sendToHost(message, new SalutCallback() {
                @Override
                public void call() {
                    Log.i(TAG, "sendToHost, send data failed");
                }
            });
        }
    }

    @Override
    public void onDataReceived(Object o) {
        mCallback.onDataReceived(o);
    }
}
