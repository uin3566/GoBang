package com.xuf.www.gobang.view.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.xuf.www.gobang.R;
import com.xuf.www.gobang.util.EventBus.BusProvider;
import com.xuf.www.gobang.util.EventBus.ExitGameAckEvent;
import com.xuf.www.gobang.util.EventBus.RestartGameAckEvent;

/**
 * Created by Administrator on 2016/1/27.
 */
public class ExitAckDialog extends BaseDialog{
    public static final String TAG = "ExitAckDialog";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_ack_exit, container, false);

        Button exit = (Button) view.findViewById(R.id.btn_exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BusProvider.getInstance().post(new ExitGameAckEvent(true));
            }
        });

        Button cancel = (Button) view.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BusProvider.getInstance().post(new ExitGameAckEvent(false));
            }
        });

        return view;
    }
}
