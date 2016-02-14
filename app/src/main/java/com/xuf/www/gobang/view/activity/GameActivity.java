package com.xuf.www.gobang.view.activity;

import android.support.v4.app.Fragment;

import com.xuf.www.gobang.util.Constants;
import com.xuf.www.gobang.view.fragment.CoupleGameFragment;
import com.xuf.www.gobang.view.fragment.WifiDirectGameFragment;

/**
 * Created by Administrator on 2015/12/8.
 */
public class GameActivity extends BaseActivity {

    @Override
    protected Fragment createFragment() {
        int gameMode = getIntent().getIntExtra(Constants.GAME_MODE, Constants.INVALID_MODE);
        Fragment fragment = null;

        switch (gameMode){
            case Constants.INVALID_MODE:
                break;
            case Constants.COUPE_MODE:
                fragment = new CoupleGameFragment();
                break;
            case Constants.WIFI_MODE:
                fragment = new WifiDirectGameFragment();
                break;
            case Constants.BLUE_TOOTH_MODE:
                break;
        }

        return fragment;
    }

    @Override
    public void onBackPressed() {

    }
}
