package com.xuf.www.gobang.view.activity;

import android.support.v4.app.Fragment;

import com.xuf.www.gobang.util.ToastUtil;
import com.xuf.www.gobang.view.fragment.MainFragment;

/**
 * Created by Administrator on 2015/12/9.
 */
public class MainActivity extends BaseActivity {

    long firstBackTime = 0;

    @Override
    protected Fragment createFragment() {
        return new MainFragment();
    }

    @Override
    public void onBackPressed() {
        long secondBackTime = System.currentTimeMillis();
        if (secondBackTime - firstBackTime > 2000) {
            ToastUtil.showShort(this, "再按一次退出程序");
            firstBackTime = secondBackTime;
        } else {
            finish();
        }
    }
}
