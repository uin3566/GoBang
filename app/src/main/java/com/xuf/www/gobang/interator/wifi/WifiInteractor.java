package com.xuf.www.gobang.interator.wifi;

import android.app.Activity;
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
import com.xuf.www.gobang.presenter.wifi.IWifiInteratorCallback;

/**
 * Created by Xuf on 2016/1/23.
 */
public class WifiInteractor implements SalutDataCallback {
    private static final String TAG = "WifiInteractor";

    private IWifiInteratorCallback mCallback;

    private Context mContext;

    private Salut mSalut;

    private SalutDevice mSendToDevice;

    public WifiInteractor(Context context, IWifiInteratorCallback callback) {
        mCallback = callback;
        mContext = context;
    }

    public void initWifiNet() {
        SalutDataReceiver mDataReceiver = new SalutDataReceiver((Activity) mContext, this);
        SalutServiceData mServiceData = new SalutServiceData("server", 50489, "instance");
        mSalut = new Salut(mDataReceiver, mServiceData, new SalutCallback() {
            @Override
            public void call() {
                mCallback.onMobileNotSupportDevice();
            }
        });
    }

    public void unInitWifiNet(boolean isHost) {
        if (isHost) {
            mSalut.stopNetworkService(false);
        } else {
            mSalut.unregisterClient(false);
        }
    }

    public void startWifiService() {
        mSalut.startNetworkService(new SalutDeviceCallback() {
            @Override
            public void call(SalutDevice salutDevice) {
                Log.i(TAG, "startNetworkService, onDeviceConnected, device:" + salutDevice.deviceName);
                mSendToDevice = salutDevice;
                mCallback.onDeviceConnected(salutDevice);
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

    public void findPeers() {
        mSalut.discoverWithTimeout(new SalutCallback() {
            @Override
            public void call() {
                Log.i(TAG, "discoverWithTimeout, onDeviceFound" + mSalut.foundDevices.toString());
                mCallback.onFindPeers(mSalut.foundDevices);
            }
        }, new SalutCallback() {
            @Override
            public void call() {
                Log.i(TAG, "discoverWithTimeout, onDeviceNotFound");
                mCallback.onPeersNotFound();
            }
        }, 6000);
    }

    public void connectToHost(SalutDevice host) {
        mSendToDevice = host;
        mSalut.registerWithHost(host, new SalutCallback() {
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

    public void sendMessage(Message message) {
        if (mSendToDevice != null){
            mSalut.sendToDevice(mSendToDevice, message, new SalutCallback() {
                @Override
                public void call() {
                    Log.i(TAG, "sendMessage, send data failed");
                    mCallback.onSendMessageFailed();
                }
            });
        }
    }

    @Override
    public void onDataReceived(Object o) {
        mCallback.onDataReceived(o);
    }
}
