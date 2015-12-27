package com.xuf.www.gobang;

import android.annotation.TargetApi;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by lenov0 on 2015/12/27.
 */
public class PeersDialog extends DialogFragment {

    public static final String TAG = "PeersDialog";

    private ListView mListView;
    private ArrayAdapter mAdapter;
    private List<String> mData = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_peer_list, container, false);

        mListView = (ListView)view.findViewById(R.id.lv_peers);
        mAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, mData);
        mListView.setAdapter(mAdapter);

        return view;
    }

    @TargetApi(14)
    public void onReceivePeerLists(WifiP2pDeviceList peers){
        if (peers == null){
            return;
        }
        Collection collection = peers.getDeviceList();
        if (collection.isEmpty()){
            return;
        }
        mData.clear();
        for (Object device : collection){
            mData.add(((WifiP2pDevice)device).deviceName);
        }
        mAdapter.notifyDataSetChanged();
    }
}
