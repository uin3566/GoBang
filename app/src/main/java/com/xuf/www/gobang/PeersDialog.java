package com.xuf.www.gobang;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.peak.salut.SalutDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenov0 on 2015/12/27.
 */
public class PeersDialog extends DialogFragment {

    public static final String TAG = "PeersDialog";

    private PeerDialogCallback mListener;

    private ListView mListView;
    private DeviceAdapter mAdapter;
    private List<SalutDevice> mData = new ArrayList<>();

    public void setListener(PeerDialogCallback listener){
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

        Button cancel = (Button)view.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPeerCancel();
            }
        });

        return view;
    }

    public void updatePeers(List<SalutDevice> data){
        mAdapter.setData(data);
    }

    private class DeviceAdapter extends BaseAdapter{
        public void setData(List<SalutDevice> data){
            mData.clear();
            for (SalutDevice device : data){
                mData.add(device);
            }
            notifyDataSetChanged();
        }

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

    public interface PeerDialogCallback {
        void onPeerConnect(SalutDevice device);
        void onPeerCancel();
    }
}
