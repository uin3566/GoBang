package com.xuf.www.gobang.util.BlueTooth;

import android.bluetooth.BluetoothAdapter;

/**
 * Created by lenov0 on 2016/2/16.
 */
public class BlueToothWrapper {

    public boolean initBlueTooth() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        return adapter != null;
    }


}
