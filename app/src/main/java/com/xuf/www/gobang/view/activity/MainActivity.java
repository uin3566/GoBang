package com.xuf.www.gobang.view.activity;

import android.support.v4.app.Fragment;

import com.xuf.www.gobang.view.fragment.MainFragment;

/**
 * Created by Administrator on 2015/12/9.
 */
public class MainActivity extends BaseActivity {

    @Override
    protected Fragment createFragment() {
        return new MainFragment();
    }
}
