package com.xuf.www.gobang.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Looper;
import android.util.Log;

/**
 * Created by lenov0 on 2016/2/27.
 */
public class WifiWrapper {
    private static final String TAG = "WifiWrapper";

    private Context mContext;

    private WifiP2pManager mP2pManager;
    private WifiP2pManager.Channel mChannel;
    private BroadcastReceiver mReceiver;

    private IntentFilter mIntentFilter;
    private WifiInterface mWifiInterface;

    public WifiWrapper(Context context, WifiInterface wifiInterface) {
        mContext = context;
        mWifiInterface = wifiInterface;
    }

    public void init() {
        mP2pManager = (WifiP2pManager) mContext.getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mP2pManager.initialize(mContext, Looper.getMainLooper(), null);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        mReceiver = new WifiP2pBroadcastReceiver();
        registerReceiver();
    }

    public void uninit() {
        unregisterReceiver();
    }

    public void discoveryPeers() {
        mP2pManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "discovery process success");
            }

            @Override
            public void onFailure(int i) {
                Log.i(TAG, "discovery process fail");
            }
        });
    }

    public void connectToDevice(WifiP2pDevice wifiP2pDevice) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = wifiP2pDevice.deviceAddress;
        mP2pManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "connect process success");
            }

            @Override
            public void onFailure(int i) {
                Log.i(TAG, "connect process fail");
            }
        });
    }

    private void registerReceiver() {
        mContext.registerReceiver(mReceiver, mIntentFilter);
    }

    private void unregisterReceiver() {
        mContext.unregisterReceiver(mReceiver);
    }

    private class WifiP2pBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION:
                    Log.i(TAG, "WIFI_P2P_STATE_CHANGED_ACTION");
                    break;
                case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION:
                    Log.i(TAG, "WIFI_P2P_PEERS_CHANGED_ACTION");
                    mP2pManager.requestPeers(mChannel, new WifiP2pManager.PeerListListener() {
                        @Override
                        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
                            mWifiInterface.onPeersFound(wifiP2pDeviceList);
                        }
                    });
                    break;
                case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION:
                    Log.i(TAG, "WIFI_P2P_CONNECTION_CHANGED_ACTION");
                    NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                    if (networkInfo.isConnected()) {
                        mP2pManager.requestConnectionInfo(mChannel, new WifiP2pManager.ConnectionInfoListener() {
                            @Override
                            public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {

                            }
                        });
                    }
                    break;
                case WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION:
                    Log.i(TAG, "WIFI_P2P_THIS_DEVICE_CHANGED_ACTION");
                    break;
            }
        }
    }

    public interface WifiInterface {
        void onPeersFound(WifiP2pDeviceList p2pDeviceList);
    }
}
