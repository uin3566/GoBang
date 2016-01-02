package com.xuf.www.gobang;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.InetAddress;

/**
 * Created by Administrator on 2015/12/8.
 */
public class GameFragment extends Fragment implements
       DialogManager.DialogButtonClickListener{

    public static final String TAG = "GameFragment";

    private int mGameMode = Constants.INVALID_MODE;
    private DialogManager mDialogManager;

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private BroadcastReceiver mReceiver;

    private MyConnectionListener mConnectionListener;

    public static GameFragment getInstance(int gameMode){
        Bundle args = new Bundle();
        args.putInt(Constants.GAME_MODE, gameMode);

        GameFragment fragment = new GameFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null){
            mGameMode = args.getInt(Constants.GAME_MODE);
        }

        if (mGameMode == Constants.ONLINE_MODE){
            mConnectionListener = new MyConnectionListener();
            mDialogManager = new DialogManager(getChildFragmentManager(), this);
            showLineWayDialog();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        GoBangBoard goBangBoard = (GoBangBoard)view.findViewById(R.id.go_bang_board);
        goBangBoard.setGameMode(mGameMode);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null){
            getActivity().unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    @TargetApi(14)
    private class MyConnectionListener implements WifiP2pManager.ConnectionInfoListener{
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {
            String log = String.format("connection info available, info.groupFormed:%b, info.isGroupOwner:%b", info.groupFormed, info.isGroupOwner);
            Log.d(Constants.CON_TAG, log);
            if (info.groupFormed && info.isGroupOwner){
            } else if (info.groupFormed){

            }
        }
    }

    public MyConnectionListener getConnectionListener(){
        return mConnectionListener;
    }

    @TargetApi(14)
    @Override
    public void onPeerConnect(WifiP2pDevice device) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                String log = "connect init success";
                Log.d(Constants.CON_TAG, log);
            }

            @Override
            public void onFailure(int reason) {
                String log = "connect init fail";
                Log.d(Constants.CON_TAG, log);
                ToastUtil.showShort(getActivity(), "连接失败");
            }
        });
    }

    @Override
    public void onLineWayButtonClick(int button) {
        switch (button){
            case BaseButtonDialog.BTN_1:
                break;
            case BaseButtonDialog.BTN_2:
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    mManager = (WifiP2pManager)getActivity().getSystemService(Context.WIFI_P2P_SERVICE);
                    mChannel = mManager.initialize(getActivity(), Looper.getMainLooper(), null);
                    mReceiver = new WifiP2pBroadcastReceiver(mManager, mChannel, this);
                    IntentFilter filter = new IntentFilter();
                    filter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
                    filter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
                    filter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
                    filter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
                    getActivity().registerReceiver(mReceiver, filter);
                    showCompositionDialog();
                } else {
                    ToastUtil.showShort(getActivity(), "您当前的系统版本不支持Wifi直连，请选择蓝牙连接");
                }
                break;
            case BaseButtonDialog.BTN_3:
                break;
        }
    }

    @TargetApi(14)
    @Override
    public void onCompositionButtonClick(int button) {
        switch (button){
            case BaseButtonDialog.BTN_1:
                //todo:创建棋局
                showWaitingDialog();
                break;
            case BaseButtonDialog.BTN_2:
                //todo:加入棋局
                mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        String log = "discoverPeers init success";
                        Log.d(Constants.CON_TAG, log);
                    }

                    @Override
                    public void onFailure(int reason) {
                        String log = "discoverPeers init fail";
                        Log.d(Constants.CON_TAG, log);
                        ToastUtil.showShort(getActivity(), "搜索Wifi对象失败");
                    }
                });
                showPeersDialog();
                break;
            case BaseButtonDialog.BTN_3:
                if (mReceiver != null){
                    getActivity().unregisterReceiver(mReceiver);
                    mReceiver = null;
                }
                dismissCompositionDialog();
                break;
        }
    }

    public void showWaitingDialog(){
        mDialogManager.showWaitingDialog();
    }

    public void onReceivePeerLists(WifiP2pDeviceList peers){
        mDialogManager.onReceivePeerLists(peers);
    }

    private void showPeersDialog(){
        mDialogManager.showPeersDialog();
    }

    private void showLineWayDialog(){
        mDialogManager.showLineWayDialog();
    }

    private void showCompositionDialog(){
        mDialogManager.showCompositionDialog();
    }

    private void dismissCompositionDialog(){
        mDialogManager.dismissCompositionDialog();
    }
}
