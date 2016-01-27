package com.xuf.www.gobang.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.peak.salut.SalutDevice;
import com.squareup.otto.Produce;

import java.util.List;

/**
 * Created by lenov0 on 2015/12/28.
 */
public class DialogCenter {
    private FragmentManager mFragmentManager;

    private CompositionDialog mCompositionDialog;
    private PeersDialog mPeersDialog;
    private WaitingPlayerDialog mWaitingDialog;
    private RestartWaitingDialog mRestartWaitingDialog;
    private RestartAckDialog mRestartAckDialog;

    public DialogCenter(FragmentActivity activity) {
        mFragmentManager = activity.getSupportFragmentManager();
        mCompositionDialog = new CompositionDialog();
        mCompositionDialog.setCancelable(false);
        mPeersDialog = new PeersDialog();
        mPeersDialog.setCancelable(false);
        mWaitingDialog = new WaitingPlayerDialog();
        mWaitingDialog.setCancelable(false);
        mRestartWaitingDialog = new RestartWaitingDialog();
        mRestartWaitingDialog.setCancelable(false);
        mRestartAckDialog = new RestartAckDialog();
        mRestartAckDialog.setCancelable(false);
    }

    public void showCompositionDialog() {
        mCompositionDialog.show(preShowDialog(CompositionDialog.TAG), CompositionDialog.TAG);
    }

    public void showWaitingPlayerDialog() {
        mWaitingDialog.show(preShowDialog(WaitingPlayerDialog.TAG), WaitingPlayerDialog.TAG);
    }

    public void enableWaitingPlayerDialogsBegin() {
        mWaitingDialog.setBeginEnable();
    }

    public void dismissWaitingPlayerDialog() {
        mWaitingDialog.dismiss();
    }

    public void showPeersDialog() {
        mPeersDialog.show(preShowDialog(PeersDialog.TAG), PeersDialog.TAG);
    }

    public void dismissPeersDialog() {
        mPeersDialog.dismiss();
    }

    public void updatePeers(List<SalutDevice> data) {
        mPeersDialog.updatePeers(data);
    }

    public void showRestartWaitingDialog() {
        mRestartWaitingDialog.show(preShowDialog(RestartWaitingDialog.TAG), RestartWaitingDialog.TAG);
    }

    public void dismissRestartWaitingDialog() {
        mRestartWaitingDialog.dismiss();
    }

    public void showRestartAckDialog() {
        mRestartAckDialog.show(preShowDialog(RestartAckDialog.TAG), RestartAckDialog.TAG);
    }

    public void dismissRestartAckDialog() {
        mRestartAckDialog.dismiss();
    }

    public void dismissWaitingAndComposition() {
        mWaitingDialog.dismiss();
        mCompositionDialog.dismiss();
    }

    public void dismissPeersAndComposition() {
        mPeersDialog.dismiss();
        mCompositionDialog.dismiss();
    }

    private FragmentTransaction preShowDialog(String tag) {
        FragmentTransaction fr = mFragmentManager.beginTransaction();
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
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
