package com.xuf.www.gobang.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;
import com.xuf.www.gobang.presenter.wifi.IWifiView;
import com.xuf.www.gobang.presenter.wifi.WifiPresenter;
import com.xuf.www.gobang.util.EventBus.WifiCancelEvent;
import com.xuf.www.gobang.util.EventBus.WifiCreateGameEvent;
import com.xuf.www.gobang.util.EventBus.WifiJoinGameEvent;
import com.xuf.www.gobang.util.ToastUtil;
import com.xuf.www.gobang.view.dialog.DialogCenter;

/**
 * Created by Xuf on 2016/1/23.
 */
public class WifiDirectGameFragment extends BaseGameFragment implements IWifiView {

    private WifiPresenter mWifiPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void init() {
        mWifiPresenter = new WifiPresenter(getActivity(), this);
        mWifiPresenter.initWifiNet();
    }

    @Override
    public void onResume() {
        super.onResume();
        //DialogCenter.getInstance(getActivity().getSupportFragmentManager()).showCompositionDialog();
    }

    @Override
    public void onWifiInitFailed(String message) {
        ToastUtil.showShort(getActivity(), message);
        getActivity().finish();
    }

    @Subscribe
    public void onCreateGame(WifiCreateGameEvent event) {
        ToastUtil.showShort(getActivity(), "onCreateGame");
    }

    @Subscribe
    public void onJoinGame(WifiJoinGameEvent event) {
        ToastUtil.showShort(getActivity(), "onJoinGame");
    }

    @Subscribe
    public void onCancel(WifiCancelEvent event){
        //getActivity().finish();
    }
}
