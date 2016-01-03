package com.xuf.www.gobang;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Callbacks.SalutDataCallback;
import com.peak.salut.Callbacks.SalutDeviceCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutDevice;
import com.peak.salut.SalutServiceData;

/**
 * Created by Administrator on 2015/12/8.
 */
public class GameFragment extends Fragment implements
        DialogManager.DialogsCallback
      ,SalutDataCallback{

    public static final String TAG = "GameFragment";

    private int mGameMode = Constants.INVALID_MODE;
    private DialogManager mDialogManager;

    private SalutServiceData mServiceData;
    private SalutDataReceiver mDataReceiver;
    private Salut mSalut;
    private SalutDevice mPeerDevice;

    private boolean mIsHost;
    private Context mContext;

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
            mContext = getActivity();
            mDataReceiver = new SalutDataReceiver(getActivity(), this);
            mServiceData = new SalutServiceData("server", 50489, "instance");
            mSalut = new Salut(mDataReceiver, mServiceData, new SalutCallback() {
                @Override
                public void call() {
                    Log.e(TAG, "Sorry, but this device does not support WiFi Direct.");
                    if (mContext != null){
                        ToastUtil.showShort(mContext, "抱歉，您的设备不支持wifi直连");
                    }
                    getActivity().finish();
                }
            });
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
        mContext = null;
        if (mGameMode != Constants.ONLINE_MODE){
            return;
        }
        if (mIsHost){
            mSalut.stopNetworkService(true);
        } else {
            mSalut.unregisterClient(true);
        }
    }

    @Override
    public void onLineWayButtonClick(int button) {
        switch (button){
            //蓝牙连接
            case BaseButtonDialog.BTN_1:
                break;
            //wifi连接
            case BaseButtonDialog.BTN_2:
                showCompositionDialog();
                break;
            //返回
            case BaseButtonDialog.BTN_3:
                getActivity().finish();
                break;
        }
    }

    @TargetApi(14)
    @Override
    public void onCompositionButtonClick(int button) {
        switch (button){
            case BaseButtonDialog.BTN_1:
                //todo:创建棋局
                mIsHost = true;
                mSalut.startNetworkService(new SalutDeviceCallback() {
                    @Override
                    public void call(SalutDevice device) {
                        Log.d(TAG, device.readableName + " has connected!");
                        if (mContext != null) {
                            ToastUtil.showShort(mContext, "对方已连接成功");
                        }
                        enableWaitingBegin();
                        mPeerDevice = device;
                    }
                });
                showWaitingDialog();
                break;
            case BaseButtonDialog.BTN_2:
                //todo:加入棋局
                mIsHost = false;
                mSalut.discoverWithTimeout(new SalutCallback() {
                    @Override
                    public void call() {
                        Log.d(TAG, "Look at all these devices! " + mSalut.foundDevices.toString());
                        mDialogManager.updatePeers(mSalut.foundDevices);
                    }
                }, new SalutCallback() {
                    @Override
                    public void call() {
                        Log.d(TAG, "Bummer, we didn't find anyone. ");
                        if (mContext != null){
                            ToastUtil.showShort(mContext, "查询超时，请重试");
                        }
                    }
                }, 5000);
                showPeersDialog();
                break;
            case BaseButtonDialog.BTN_3:
                dismissCompositionDialog();
                break;
        }
    }

    @Override
    public void onWaitingBegin() {
        dismissAll();
        mSalut.sendToDevice(mPeerDevice, "abc", new SalutCallback() {
            @Override
            public void call() {
                if (mContext != null){
                    ToastUtil.showShort(mContext, "数据发送失败");
                }
            }
        });
    }

    @Override
    public void onWaitingCancel() {
        dismissWaitingDialog();
        mSalut.stopNetworkService(true);
    }

    @Override
    public void onPeerClickCancel() {
        dismissPeersDialog();
    }

    @Override
    public void onPeerConnect(final SalutDevice device) {
        mSalut.registerWithHost(device, new SalutCallback() {
            @Override
            public void call() {
                Log.d(TAG, "We're now registered.");
                mPeerDevice = device;
            }
        }, new SalutCallback() {
            @Override
            public void call() {
                Log.d(TAG, "We failed to register.");
            }
        });
    }

    @Override
    public void onDataReceived(Object o) {
        if (!mIsHost){
            dismissPeersDialog();
            String str = (String)o;
            ToastUtil.showShort(mContext, str);
        }
    }

    public void showWaitingDialog(){
        mDialogManager.showWaitingDialog();
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

    private void dismissPeersDialog(){
        mDialogManager.dismissPeersDialog();
    }

    private void dismissWaitingDialog(){
        mDialogManager.dismissWaitingDialog();
    }

    private void enableWaitingBegin(){
        mDialogManager.enableWaitingBegin();
    }

    private void dismissAll(){
        mDialogManager.dismissAll();
    }
}
