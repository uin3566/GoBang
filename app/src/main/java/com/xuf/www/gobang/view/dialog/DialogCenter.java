package com.xuf.www.gobang.view.dialog;

import android.bluetooth.BluetoothDevice;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.peak.salut.SalutDevice;

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
    private ExitAckDialog mExitAckDialog;
    private MoveBackAckDialog mMoveBackAckDialog;
    private MoveBackWaitingDialog mMoveBackWaitingDialog;

    public DialogCenter(FragmentActivity activity) {
        mFragmentManager = activity.getSupportFragmentManager();
        mCompositionDialog = new CompositionDialog();
        mCompositionDialog.setCancelable(false);
        mPeersDialog = new PeersDialog();
        mPeersDialog.setCancelable(false);
        mWaitingDialog = new WaitingPlayerDialog();
        mWaitingDialog.setCancelable(false);
        mRestartWaitingDialog = new RestartWaitingDialog();
        mRestartAckDialog = new RestartAckDialog();
        mRestartAckDialog.setCancelable(false);
        mExitAckDialog = new ExitAckDialog();
        mExitAckDialog.setCancelable(false);
        mMoveBackAckDialog = new MoveBackAckDialog();
        mMoveBackAckDialog.setCancelable(false);
        mMoveBackWaitingDialog = new MoveBackWaitingDialog();
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

    public void updateWifiPeers(List<SalutDevice> data) {
        mPeersDialog.updateWifiPeers(data);
    }

    public void updateBlueToothPeers(List<BluetoothDevice> data, boolean append) {
        mPeersDialog.updateBlueToothPeers(data, append);
    }

    public void showRestartWaitingDialog() {
        mRestartWaitingDialog.show(preShowDialog(RestartWaitingDialog.TAG), RestartWaitingDialog.TAG);
    }

    public void dismissRestartWaitingDialog() {
        mRestartWaitingDialog.dismiss();
    }

    public void showMoveBackWaitingDialog() {
        mMoveBackWaitingDialog.show(preShowDialog(MoveBackWaitingDialog.TAG), MoveBackWaitingDialog.TAG);
    }

    public void dismissMoveBackWaitingDialog() {
        mMoveBackWaitingDialog.dismiss();
    }

    public void showRestartAckDialog() {
        mRestartAckDialog.show(preShowDialog(RestartAckDialog.TAG), RestartAckDialog.TAG);
    }

    public void dismissRestartAckDialog() {
        mRestartAckDialog.dismiss();
    }

    public void showExitAckDialog() {
        mExitAckDialog.show(preShowDialog(ExitAckDialog.TAG), ExitAckDialog.TAG);
    }

    public void dismissExitAckDialog() {
        mExitAckDialog.dismiss();
    }

    public void showMoveBackAckDialog() {
        mMoveBackAckDialog.show(preShowDialog(MoveBackAckDialog.TAG), MoveBackAckDialog.TAG);
    }

    public void dismissMoveBackAckDialog() {
        mMoveBackAckDialog.dismiss();
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
}
