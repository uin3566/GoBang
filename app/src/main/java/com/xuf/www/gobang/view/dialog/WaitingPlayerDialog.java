package com.xuf.www.gobang.view.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.xuf.www.gobang.R;
import com.xuf.www.gobang.util.EventBus.BusProvider;
import com.xuf.www.gobang.util.EventBus.WifiBeginWaitingEvent;
import com.xuf.www.gobang.util.EventBus.WifiCancelWaitingEvent;

/**
 * Created by lenov0 on 2015/12/28.
 */
public class WaitingPlayerDialog extends BaseDialog {
    public static final String TAG = "WaitingPlayerDialog";

    private Button mBeginButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.dialog_waiting_player, container, false);
        mBeginButton = (Button)view.findViewById(R.id.btn_begin);
        mBeginButton.setClickable(false);
        Button CancelButton = (Button)view.findViewById(R.id.btn_cancel);
        CancelButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 BusProvider.getInstance().post(new WifiCancelWaitingEvent());
             }
         });

        return view;
    }

    public void setBeginEnable(){
        mBeginButton.setClickable(true);
        mBeginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BusProvider.getInstance().post(new WifiBeginWaitingEvent());
            }
        });
    }
}
