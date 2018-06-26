package com.cdl.wifi.adapter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdl.wifi.view.WifiUpdateView;
import com.mirror.R;
import com.mirror.wifi.ObjectAdapterClickListener;

import java.util.List;

public class WifiListRecycleAdapter extends RecyclerView.Adapter<WifiListRecycleAdapter.Holder> {

    private List<ScanResult> datas;
    private Context context;
    // 取得WifiManager对象
    private ConnectivityManager cm;
    LayoutInflater inflater;
    ObjectAdapterClickListener listener;
    private WifiManager mWifiManager;


    public void setOnItemClickListener(ObjectAdapterClickListener listener) {
        this.listener = listener;
    }

    public void setDatas(List<ScanResult> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public WifiListRecycleAdapter(Context context, List<ScanResult> datas) {
        super();
        this.datas = datas;
        this.context = context;
        inflater = LayoutInflater.from(context);
        cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.wifi_child_recycle, null);
        return new WifiListRecycleAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        holder.itemView.setFocusable(true);
        changeView(false, holder.itemView);
        ScanResult scanResult = datas.get(position);
        int level = datas.get(position).level;
        WifiUpdateView.updateWifiImg(level, holder.imgWifiLevelIco);
        WifiUpdateView.updateLock(scanResult, holder.img_wifi_lock);
        holder.txtWifiDesc.setTextColor(context.getResources().getColor(R.color.white));
        holder.txtWifiName.setTextColor(context.getResources().getColor(R.color.white));
        NetworkInfo.State wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        holder.txtWifiName.setText(datas.get(position).SSID);

        //===========================================================
        String wifiState = "";
        String descOri = datas.get(position).capabilities;
        if (descOri.toUpperCase().contains("WPA-PSK")) {
            wifiState = "WPA";
        }
        if (descOri.toUpperCase().contains("WPA2-PSK")) {
            wifiState = "WPA2";
        }
        if (descOri.toUpperCase().contains("WPA-PSK")
                && descOri.toUpperCase().contains("WPA2-PSK")) {
            wifiState = "WPA/WPA2";
        }
        NetworkInfo.State state = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        String g1 = wifiInfo.getSSID();
        String g2 = "\"" + datas.get(position).SSID + "\"";
        if (state == NetworkInfo.State.CONNECTING) {
            if (g2.endsWith(g1)) {
                wifiState = "连接中...";
            }
        } else if (wifi == NetworkInfo.State.CONNECTED) {
            if (g2.endsWith(g1)) {
                wifiState = "已连接";
                holder.txtWifiDesc.setTextColor(context.getResources().getColor(R.color.blue));
                holder.txtWifiName.setTextColor(context.getResources().getColor(R.color.blue));
            }
        }
        holder.txtWifiDesc.setText(wifiState);
        //=================================================================================

        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (listener != null) {
                    listener.onSelectionItem(position);
                }
                changeView(b, view);
            }
        });

        holder.itemView.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View view, MotionEvent motionEvent) {
                int what = motionEvent.getAction();
                switch (what) {
                    case MotionEvent.ACTION_HOVER_ENTER:  //鼠标进入view
                        changeView(true, view);
                        break;
                    case MotionEvent.ACTION_HOVER_EXIT:  //鼠标离开view
                        changeView(false, view);
                        break;
                }
                return false;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.clickItem(datas.get(position), position);
                }
            }
        });
    }

    public void changeView(boolean paramBoolean, View view) {
        if (paramBoolean) {
            view.setBackgroundResource(R.drawable.rect_circle_app);
        } else {
            view.setBackgroundResource(R.drawable.rect_circle_trans);
        }
    }


    @Override
    public int getItemCount() {
        if (datas == null) {
            return 0;
        }
        return datas.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        public TextView txtWifiName;
        public TextView txtWifiDesc;
        public ImageView imgWifiLevelIco;
        ImageView img_wifi_lock;

        public Holder(View convertView) {
            super(convertView);
            txtWifiName = (TextView) convertView.findViewById(R.id.txt_wifi_name);
            txtWifiDesc = (TextView) convertView.findViewById(R.id.txt_wifi_desc);
            imgWifiLevelIco = (ImageView) convertView.findViewById(R.id.img_wifi_level_ico);
            img_wifi_lock = (ImageView) convertView.findViewById(R.id.img_wifi_lock);
        }
    }
}
