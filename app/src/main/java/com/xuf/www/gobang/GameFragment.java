package com.xuf.www.gobang;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2015/12/8.
 */
public class GameFragment extends Fragment implements
        LineWayDialog.ButtonClickListener
       ,CompositionDialog.ButtonClickListener{

    private int mGameMode = Constants.INVALID_MODE;
    private LineWayDialog mLineWayDialog;
    private CompositionDialog mCompositionDialog;
    private PeersDialog mPeersDialog;

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private BroadcastReceiver mReceiver;

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
            mLineWayDialog = new LineWayDialog();
            mLineWayDialog.setLister(this);
            mCompositionDialog = new CompositionDialog();
            mCompositionDialog.setLister(this);
            mPeersDialog = new PeersDialog();
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
        if (mGameMode == Constants.ONLINE_MODE){
            showLineWayDialog();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null){
            getActivity().unregisterReceiver(mReceiver);
        }
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
                break;
            case BaseButtonDialog.BTN_2:
                //todo:加入棋局
                mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(int reason) {
                        ToastUtil.showShort(getActivity(), "搜索Wifi对象失败");
                    }
                });
                mPeersDialog.show(getChildFragmentManager(), PeersDialog.TAG);
                break;
            case BaseButtonDialog.BTN_3:
                if (mReceiver != null){
                    getActivity().unregisterReceiver(mReceiver);
                }
                dismissCompositionDialog();
                break;
        }
    }

    public void onReceivePeerLists(WifiP2pDeviceList peers){
        if (mPeersDialog != null){
            mPeersDialog.onReceivePeerLists(peers);
        }
    }

    private void showLineWayDialog(){
        mLineWayDialog.show(getChildFragmentManager(), LineWayDialog.TAG);
    }

    private void showCompositionDialog(){
        mCompositionDialog.show(getChildFragmentManager(), CompositionDialog.TAG);
    }

    private void dismissCompositionDialog(){
        mCompositionDialog.dismiss();
    }
}
