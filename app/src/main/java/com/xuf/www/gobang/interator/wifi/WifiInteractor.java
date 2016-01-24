package com.xuf.www.gobang.interator.wifi;

import android.app.Activity;
import android.content.Context;

import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Callbacks.SalutDataCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutDevice;
import com.peak.salut.SalutServiceData;
import com.xuf.www.gobang.presenter.wifi.IWifiInteratorCallback;

/**
 * Created by Xuf on 2016/1/23.
 */
public class WifiInteractor implements SalutDataCallback {
    private IWifiInteratorCallback mCallback;

    private Context mContext;

    private Salut mSalut;

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

    @Override
    public void onDataReceived(Object o) {

    }
}
