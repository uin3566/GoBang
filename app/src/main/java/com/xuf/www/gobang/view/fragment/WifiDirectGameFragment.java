package com.xuf.www.gobang.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.bluelinelabs.logansquare.LoganSquare;
import com.peak.salut.SalutDevice;
import com.squareup.otto.Subscribe;
import com.xuf.www.gobang.R;
import com.xuf.www.gobang.bean.Message;
import com.xuf.www.gobang.bean.Point;
import com.xuf.www.gobang.presenter.wifi.IWifiView;
import com.xuf.www.gobang.presenter.wifi.WifiPresenter;
import com.xuf.www.gobang.util.EventBus.WifiBeginWaitingEvent;
import com.xuf.www.gobang.util.EventBus.WifiCancelCompositionEvent;
import com.xuf.www.gobang.util.EventBus.WifiCancelWaitingEvent;
import com.xuf.www.gobang.util.EventBus.WifiConnectPeerEvent;
import com.xuf.www.gobang.util.EventBus.WifiCreateGameEvent;
import com.xuf.www.gobang.util.EventBus.WifiJoinGameEvent;
import com.xuf.www.gobang.util.EventBus.WifiCancelPeerEvent;
import com.xuf.www.gobang.util.MessageWrapper;
import com.xuf.www.gobang.util.ToastUtil;
import com.xuf.www.gobang.view.dialog.DialogCenter;
import com.xuf.www.gobang.widget.GoBangBoard;

import java.io.IOException;
import java.util.List;

/**
 * Created by Xuf on 2016/1/23.
 */
public class WifiDirectGameFragment extends BaseGameFragment implements IWifiView, View.OnTouchListener {

    private boolean mIsHost;
    private boolean mIsMePlay = false;

    private WifiPresenter mWifiPresenter;

    private DialogCenter mDialogCenter;
    private GoBangBoard mBoard;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unInit();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mBoard = (GoBangBoard) view.findViewById(R.id.go_bang_board);
        mBoard.setOnTouchListener(this);

        return view;
    }

    private void init() {
        mDialogCenter = new DialogCenter(getActivity());
        mDialogCenter.showCompositionDialog();
        mWifiPresenter = new WifiPresenter(getActivity(), this);
        mWifiPresenter.initWifiNet();
    }

    private void unInit() {
        mWifiPresenter.unInitWifiNet(mIsHost);
    }

    @Override
    public void onWifiInitFailed(String message) {
        ToastUtil.showShort(getActivity(), message);
        getActivity().finish();
    }

    @Override
    public void onDeviceConnected(SalutDevice device) {
        ToastUtil.showShort(getActivity(), "onDeviceConnected");
        mDialogCenter.enableWaitingPlayerDialogsBegin();
    }

    @Override
    public void onStartWifiServiceFailed() {
        ToastUtil.showShort(getActivity(), "onStartWifiServiceFailed");
    }

    @Override
    public void onFindPeers(List<SalutDevice> deviceList) {
        mDialogCenter.updatePeers(deviceList);
    }

    @Override
    public void onPeersNotFound() {
        ToastUtil.showShort(getActivity(), "found no peers");
    }

    @Override
    public void onDataReceived(Object o) {
        String str = (String) o;
        try {
            Message message = LoganSquare.parse(str, Message.class);
            int type = message.mMessageType;
            switch (type) {
                case Message.MSG_TYPE_HOST_BEGIN:
                    //joiner
                    mDialogCenter.dismissPeersAndComposition();
                    Message ack = MessageWrapper.getHostBeginAckMessage();
                    mWifiPresenter.sendToHost(ack);
                    ToastUtil.showShort(getActivity(), "游戏开始");
                    break;
                case Message.MSG_TYPE_BEGIN_ACK:
                    //host
                    mDialogCenter.dismissWaitingAndComposition();
                    mIsMePlay = true;
                    break;
                case Message.MSG_TYPE_GAME_DATA:
                    mBoard.putChess(message.mIsWhite, message.mGameData.x, message.mGameData.y);
                    mIsMePlay = true;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSendMessageFailed() {
        ToastUtil.showShort(getActivity(), "send message failed");
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mIsMePlay) {
                    float x = motionEvent.getX();
                    float y = motionEvent.getY();
                    Point point = mBoard.convertPoint(x, y);
                    mBoard.putChess(mIsHost, point.x, point.y);
                    Message data = MessageWrapper.getSendDataMessage(point, mIsHost);
                    if (mIsHost) {
                        mWifiPresenter.sendToDevice(data);
                    } else {
                        mWifiPresenter.sendToHost(data);
                    }
                    mIsMePlay = false;
                }
                break;
        }
        return false;
    }

    @Subscribe
    public void onCreateGame(WifiCreateGameEvent event) {
        mIsHost = true;
        mWifiPresenter.startWifiService();
        mDialogCenter.showWaitingPlayerDialog();
    }

    @Subscribe
    public void onJoinGame(WifiJoinGameEvent event) {
        mIsHost = false;
        mWifiPresenter.findPeers();
        mDialogCenter.showPeersDialog();
    }

    @Subscribe
    public void onCancelCompositionDialog(WifiCancelCompositionEvent event) {
        getActivity().finish();
    }

    @Subscribe
    public void onConnectPeer(WifiConnectPeerEvent event) {
        mWifiPresenter.connectToHost(event.mDevice);
    }

    @Subscribe
    public void onCancelConnectPeer(WifiCancelPeerEvent event) {
        mDialogCenter.dismissPeersDialog();
    }

    @Subscribe
    public void onBeginGame(WifiBeginWaitingEvent event) {
        Message begin = MessageWrapper.getHostBeginMessage();
        mWifiPresenter.sendToDevice(begin);
    }

    @Subscribe
    public void onCancelWaitingDialog(WifiCancelWaitingEvent event) {
        mDialogCenter.dismissWaitingPlayerDialog();
    }
}