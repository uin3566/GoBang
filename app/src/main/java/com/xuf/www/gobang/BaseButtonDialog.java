package com.xuf.www.gobang;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

/**
 * Created by lenov0 on 2015/12/26.
 */
public abstract class BaseButtonDialog extends DialogFragment implements View.OnClickListener{

    public static final int BTN_1 = 1;
    public static final int BTN_2 = 2;
    public static final int BTN_3 = 3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_base_button, container, false);
        initView(view);

        return view;
    }

    private void initView(View view){
        Button btn1 = (Button)view.findViewById(R.id.btn_1);
        Button btn2 = (Button)view.findViewById(R.id.btn_2);
        Button btn3 = (Button)view.findViewById(R.id.btn_3);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn1.setText(getButtonText(BTN_1));
        btn2.setText(getButtonText(BTN_2));
        btn3.setText(getButtonText(BTN_3));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_1:
                onButtonClick(BTN_1);
                break;
            case R.id.btn_2:
                onButtonClick(BTN_2);
                break;
            case R.id.btn_3:
                onButtonClick(BTN_3);
                break;
        }
    }

    public abstract String getButtonText(int button);

    public abstract void onButtonClick(int button);
}
