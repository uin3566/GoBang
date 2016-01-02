package com.xuf.www.gobang;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by lenov0 on 2015/12/28.
 */
public class DialogManager implements
        LineWayDialog.ButtonClickListener
       ,CompositionDialog.ButtonClickListener
       ,PeersDialog.PeerConnectListener{

    private FragmentManager mFragmentManager;

    private LineWayDialog mLineWayDialog;
    private CompositionDialog mCompositionDialog;
    private PeersDialog mPeersDialog;
    private WaitingPlayerDialog mWaitingDialog;

    private DialogButtonClickListener mListener;

    public DialogManager(FragmentManager manager, DialogButtonClickListener listener){
        mFragmentManager = manager;
        mListener = listener;
        mLineWayDialog = new LineWayDialog();
        mLineWayDialog.setLister(this);
        mCompositionDialog = new CompositionDialog();
        mCompositionDialog.setLister(this);
        mPeersDialog = new PeersDialog();
        mPeersDialog.setListener(this);
        mWaitingDialog = new WaitingPlayerDialog();
    }

    @Override
    public void onPeerConnect(WifiP2pDevice device) {
        if (mListener != null){
        }
    }

    @Override
    public void onLineWayButtonClick(int button) {
        if (mListener != null){
            mListener.onLineWayButtonClick(button);
        }
    }

    @Override
    public void onCompositionButtonClick(int button) {
        if (mListener != null){
            mListener.onCompositionButtonClick(button);
        }
    }

    public void showWaitingDialog(){
        mWaitingDialog.show(preShowDialog(WaitingPlayerDialog.TAG), WaitingPlayerDialog.TAG);
    }

    public void showPeersDialog(){
        mPeersDialog.show(preShowDialog(PeersDialog.TAG), PeersDialog.TAG);
    }

    public void showLineWayDialog(){
        mLineWayDialog.show(preShowDialog(LineWayDialog.TAG), LineWayDialog.TAG);
    }

    public void showCompositionDialog(){
        mCompositionDialog.show(preShowDialog(CompositionDialog.TAG), CompositionDialog.TAG);
    }

    private FragmentTransaction preShowDialog(String tag){
        FragmentTransaction fr = mFragmentManager.beginTransaction();
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        if (fragment != null){
            fr.remove(fragment);
        }
        fr.addToBackStack(null);

        return fr;
    }

    public void dismissCompositionDialog(){
        mCompositionDialog.dismiss();
    }

    public interface DialogButtonClickListener{
        public void onLineWayButtonClick(int button);
        public void onCompositionButtonClick(int button);
    }
}
