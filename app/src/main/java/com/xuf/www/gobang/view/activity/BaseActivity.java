package com.xuf.www.gobang.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.xuf.www.gobang.R;

/**
 * Created by Administrator on 2015/12/8.
 */
public abstract class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fl_fragment_container);

        if (fragment == null){
            fragment = createFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fl_fragment_container, fragment);
            ft.commit();
        }
    }

    protected abstract Fragment createFragment();
}
