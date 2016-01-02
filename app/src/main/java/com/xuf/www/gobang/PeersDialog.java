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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by lenov0 on 2015/12/27.
 */
public class PeersDialog extends DialogFragment {

    public static final String TAG = "PeersDialog";

    private PeerConnectListener mListener;

    private ListView mListView;
    private DeviceAdapter mAdapter;
    private List<WifiP2pDevice> mData = new ArrayList<>();

    public void setListener(PeerConnectListener listener){
        mListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_peer_list, container, false);

        mListView = (ListView)view.findViewById(R.id.lv_peers);
        mAdapter = new DeviceAdapter();
        mListView.setAdapter(mAdapter);

        return view;
    }

    private class DeviceAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null){
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_device, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }

            String device = mData.get(position).deviceName;
            holder = (ViewHolder)convertView.getTag();
            holder.device.setText(device);
            holder.device.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onPeerConnect(mData.get(position));
                }
            });

            return convertView;
        }

        private class ViewHolder{
            public TextView device;

            public ViewHolder(View view){
                device = (TextView)view.findViewById(R.id.tv_device);
            }
        }
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
            mData.add((WifiP2pDevice)device);
        }
        mAdapter.notifyDataSetChanged();
    }

    public interface PeerConnectListener{
        void onPeerConnect(WifiP2pDevice device);
    }
}
