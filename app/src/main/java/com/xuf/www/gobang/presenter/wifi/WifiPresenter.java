package com.xuf.www.gobang.presenter.wifi;

import android.content.Context;

import com.xuf.www.gobang.interator.wifi.WifiInteractor;

/**
 * Created by Xuf on 2016/1/23.
 */
public class WifiPresenter implements IWifiInteratorCallback{
    private IWifiView mWifiView;
    private WifiInteractor mWifiInterator;

    public WifiPresenter(Context context, IWifiView wifiView){
        mWifiView = wifiView;
        mWifiInterator = new WifiInteractor(context, this);
    }

    public void initWifiNet(){
        mWifiInterator.initWifiNet();
    }

    @Override
    public void onMobileNotSupportDevice() {
        String message = "抱歉，您的设备不支持wifi直连";
        mWifiView.onWifiInitFailed(message);
    }
}
