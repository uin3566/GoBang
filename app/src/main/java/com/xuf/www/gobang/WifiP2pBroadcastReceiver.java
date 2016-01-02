package com.xuf.www.gobang;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;

/**
 * Created by lenov0 on 2015/12/27.
 */
public class WifiP2pBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private GameFragment mFragment;

    public WifiP2pBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel, GameFragment fragment) {
        super();
        mManager = manager;
        mChannel = channel;
        mFragment = fragment;
    }

    @TargetApi(14)
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION:
                break;

            case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION:
                if (mManager == null) {
                    return;
                }
                NetworkInfo networkInfo = (NetworkInfo)intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                if (networkInfo.isConnected()){
                    mManager.requestConnectionInfo(mChannel, mFragment.getConnectionListener());
                }
                break;

            case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION:
                if (mManager == null) {
                    return;
                }
                mManager.requestPeers(mChannel, new WifiP2pManager.PeerListListener() {
                    @Override
                    public void onPeersAvailable(WifiP2pDeviceList peers) {
                        mFragment.onReceivePeerLists(peers);
                    }
                });
                break;

            case WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION:
                break;
        }
    }
}
