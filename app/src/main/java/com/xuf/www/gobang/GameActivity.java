package com.xuf.www.gobang;

import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2015/12/8.
 */
public class GameActivity extends BaseActivity {

    @Override
    protected Fragment createFragment() {
        int gameMode = getIntent().getIntExtra(Constants.GAME_MODE, Constants.INVALID_MODE);

        return GameFragment.getInstance(gameMode);
    }
}
