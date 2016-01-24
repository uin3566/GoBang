package com.xuf.www.gobang.view.dialog;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.squareup.otto.Produce;

/**
 * Created by lenov0 on 2015/12/28.
 */
public class DialogCenter {

    private static DialogCenter mInstance = null;

    private static FragmentManager mFragmentManager;

    private CompositionDialog mCompositionDialog;
    private PeersDialog mPeersDialog;
    private WaitingPlayerDialog mWaitingDialog;

    private DialogCenter(FragmentManager manager) {
        mFragmentManager = manager;
        mCompositionDialog = new CompositionDialog();
        mCompositionDialog.setCancelable(false);
        mPeersDialog = new PeersDialog();
        mPeersDialog.setCancelable(false);
        mWaitingDialog = new WaitingPlayerDialog();
        mWaitingDialog.setCancelable(false);
    }

    public static DialogCenter getInstance(FragmentManager fragmentManager) {
        if (mInstance == null) {
            mFragmentManager = fragmentManager;
            mInstance = new DialogCenter(mFragmentManager);
        }
        return mInstance;
    }

    public void showCompositionDialog() {
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

//    @Override
//    public void onPeerConnect(SalutDevice device) {
//        if (mListener != null){
//            mListener.onPeerConnect(device);
//        }
//    }
//
//    @Override
//    public void onPeerCancel() {
//        if (mListener != null){
//            mListener.onPeerClickCancel();
//        }
//    }
//
//    @Override
//    public void onWaitingBegin() {
//        if (mListener != null){
//            mListener.onWaitingBegin();
//        }
//    }
//
//    @Override
//    public void onWaitingCancel() {
//        if (mListener != null){
//            mListener.onWaitingCancel();
//        }
//    }
//
//    @Override
//    public void onCompositionButtonClick(int button) {
//        if (mListener != null){
//            mListener.onCompositionButtonClick(button);
//        }
//    }
//
//    public void updatePeers(List<SalutDevice> data){
//        mPeersDialog.updatePeers(data);
//    }
//
//    public void showWaitingDialog(){
//        mWaitingDialog.show(preShowDialog(WaitingPlayerDialog.TAG), WaitingPlayerDialog.TAG);
//    }
//
//    public void dismissWaitingDialog(){
//        mWaitingDialog.dismiss();
//    }
//
//    public void enableWaitingBegin(){
//        mWaitingDialog.setBeginEnable();
//    }
//
//    public void showPeersDialog(){
//        mPeersDialog.show(preShowDialog(PeersDialog.TAG), PeersDialog.TAG);
//    }
//
//    public void showCompositionDialog(){
//        mCompositionDialog.show(preShowDialog(CompositionDialog.TAG), CompositionDialog.TAG);
//    }
//
//    private FragmentTransaction preShowDialog(String tag){
//        FragmentTransaction fr = mFragmentManager.beginTransaction();
//        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
//        if (fragment != null){
//            fr.remove(fragment);
//        }
//        fr.addToBackStack(null);
//
//        return fr;
//    }
//
//    public void dismissCompositionDialog(){
//        mCompositionDialog.dismiss();
//    }
//
//    public void dismissPeersDialog(){
//        mPeersDialog.dismiss();
//    }
//
//    public void dismissAll(){
//        mWaitingDialog.dismiss();
//        mCompositionDialog.dismiss();
//    }
//
//    public interface DialogsCallback {
//        public void onLineWayButtonClick(int button);
//        public void onCompositionButtonClick(int button);
//        public void onPeerConnect(SalutDevice device);
//        public void onPeerClickCancel();
//        public void onWaitingBegin();
//        public void onWaitingCancel();
//    }
}
