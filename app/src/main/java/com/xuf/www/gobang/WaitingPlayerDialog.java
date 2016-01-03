package com.xuf.www.gobang;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

/**
 * Created by lenov0 on 2015/12/28.
 */
public class WaitingPlayerDialog extends DialogFragment {
    public static final String TAG = "WaitingPlayerDialog";

    private Button mBeginButton;
    private Button mCancelButton;

    private WaitingDialogCallback mCallback;

    public void setListener(WaitingDialogCallback callback){
        mCallback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.dialog_waiting_player, container, false);
        mBeginButton = (Button)view.findViewById(R.id.btn_begin);
        mBeginButton.setClickable(false);
        mCancelButton = (Button)view.findViewById(R.id.btn_cancel);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onWaitingCancel();
            }
        });

        return view;
    }

    public void setBeginEnable(){
        mBeginButton.setClickable(true);
        mBeginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onWaitingBegin();
            }
        });
    }

    public interface WaitingDialogCallback{
        void onWaitingBegin();
        void onWaitingCancel();
    }
}
