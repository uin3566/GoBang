package com.xuf.www.gobang;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2015/12/9.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        initView(view);

        return view;
    }

    private void initView(View root){
        TextView aiModeTextView = (TextView) root.findViewById(R.id.tv_ai_mode);
        TextView coupeTextView = (TextView) root.findViewById(R.id.tv_coupe_mode);
        TextView onlineTextView = (TextView) root.findViewById(R.id.tv_online_mode);

        aiModeTextView.setOnClickListener(this);
        coupeTextView.setOnClickListener(this);
        onlineTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), GameActivity.class);
        switch (v.getId()){
            case R.id.tv_ai_mode:
                intent.putExtra(Constants.GAME_MODE, Constants.AI_MODE);
                break;
            case R.id.tv_coupe_mode:
                intent.putExtra(Constants.GAME_MODE, Constants.COUPE_MODE);
                break;
            case R.id.tv_online_mode:
                intent.putExtra(Constants.GAME_MODE, Constants.ONLINE_MODE);
                break;
        }
        startActivity(intent);
    }
}
