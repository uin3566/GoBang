package com.xuf.www.gobang;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2015/12/8.
 */
public class GameFragment extends Fragment implements
       DialogManager.DialogButtonClickListener{

    public static final String TAG = "GameFragment";

    private int mGameMode = Constants.INVALID_MODE;
    private DialogManager mDialogManager;

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
    }

    @Override
    public void onLineWayButtonClick(int button) {
        switch (button){
            //返回
            case BaseButtonDialog.BTN_1:
                break;
            //wifi连接
            case BaseButtonDialog.BTN_2:
                showCompositionDialog();
                break;
            //蓝牙连接
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
                showPeersDialog();
                break;
            case BaseButtonDialog.BTN_3:
                dismissCompositionDialog();
                break;
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
}
