package com.xuf.www.gobang;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.peak.salut.SalutDevice;

import java.util.List;

/**
 * Created by lenov0 on 2015/12/28.
 */
public class DialogManager implements
        LineWayDialog.ButtonClickListener
       ,CompositionDialog.ButtonClickListener
       ,PeersDialog.PeerDialogCallback {

    private FragmentManager mFragmentManager;

    private LineWayDialog mLineWayDialog;
    private CompositionDialog mCompositionDialog;
    private PeersDialog mPeersDialog;
    private WaitingPlayerDialog mWaitingDialog;

    private DialogsCallback mListener;

    public DialogManager(FragmentManager manager, DialogsCallback listener){
        mFragmentManager = manager;
        mListener = listener;
        mLineWayDialog = new LineWayDialog();
        mLineWayDialog.setLister(this);
        mLineWayDialog.setCancelable(false);
        mCompositionDialog = new CompositionDialog();
        mCompositionDialog.setLister(this);
        mCompositionDialog.setCancelable(false);
        mPeersDialog = new PeersDialog();
        mPeersDialog.setListener(this);
        mPeersDialog.setCancelable(false);
        mWaitingDialog = new WaitingPlayerDialog();
        mWaitingDialog.setCancelable(false);
    }

    @Override
    public void onPeerConnect(SalutDevice device) {
        if (mListener != null){
            mListener.onPeerConnect(device);
        }
    }

    @Override
    public void onCancelButtonClicked() {
        if (mListener != null){
            mListener.onPeerClickCancel();
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

    public void updatePeers(List<SalutDevice> data){
        mPeersDialog.updatePeers(data);
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

    public void dismissPeersDialog(){
        mPeersDialog.dismiss();
    }

    public interface DialogsCallback {
        public void onLineWayButtonClick(int button);
        public void onCompositionButtonClick(int button);
        public void onPeerConnect(SalutDevice device);
        public void onPeerClickCancel();
    }
}
