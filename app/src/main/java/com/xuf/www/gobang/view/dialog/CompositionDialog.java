package com.xuf.www.gobang.view.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.xuf.www.gobang.R;
import com.xuf.www.gobang.eventbus.BusProvider;
import com.xuf.www.gobang.eventbus.WifiCancelCompositionEvent;
import com.xuf.www.gobang.eventbus.WifiCreateGameEvent;
import com.xuf.www.gobang.eventbus.WifiJoinGameEvent;

/**
 * Created by lenov0 on 2015/12/26.
 */
public class CompositionDialog extends BaseDialog implements View.OnClickListener {

    public static final String TAG = "CompositionDialog";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_composition, container, false);

        Button createGame = (Button) view.findViewById(R.id.btn_create_game);
        Button joinGame = (Button) view.findViewById(R.id.btn_join_game);
        Button cancel = (Button) view.findViewById(R.id.btn_cancel);

        createGame.setOnClickListener(this);
        joinGame.setOnClickListener(this);
        cancel.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create_game:
                BusProvider.getInstance().post(new WifiCreateGameEvent());
                break;
            case R.id.btn_join_game:
                BusProvider.getInstance().post(new WifiJoinGameEvent());
                break;
            case R.id.btn_cancel:
                BusProvider.getInstance().post(new WifiCancelCompositionEvent());
                break;
        }
    }
}
