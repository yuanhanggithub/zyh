package com.mirror.guide.adapter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mirror.R;
import com.mirror.wifi.ObjectAdapterClickListener;

import java.util.List;

public class WifiListLittleAdapter extends RecyclerView.Adapter<WifiListLittleAdapter.ViewHolder> {

    public List<ScanResult> datas;
    private Context context;
    // 取得WifiManager对象
    private WifiManager mWifiManager;
    private ConnectivityManager cm;

    public void setDatas(List<ScanResult> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public WifiListLittleAdapter(Context context, List<ScanResult> datas) {
        super();
        this.datas = datas;
        this.context = context;
        mWifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemView.setFocusable(true);
        changeViewState(false, holder.itemView);
        ScanResult scanResult = datas.get(position);
        String descOri = datas.get(position).capabilities;
        String desc = "";
        if (descOri.toUpperCase().contains("WPA-PSK")) {
            desc = "WPA";
        }
        if (descOri.toUpperCase().contains("WPA2-PSK")) {
            desc = "WPA2";
        }
        if (descOri.toUpperCase().contains("WPA-PSK")
                && descOri.toUpperCase().contains("WPA2-PSK")) {
            desc = "WPA/WPA2";
        }
        int level = datas.get(position).level;
        updateWifiImg(level, holder.imgWifiLevelIco);
        updateLock(desc, holder.img_wifi_lock);
        holder.txtWifiName.setTextColor(0xff5cabfa);
        NetworkInfo.State wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (wifi == NetworkInfo.State.CONNECTING) {
            desc = "连接中...";
        } else if (wifi == NetworkInfo.State.DISCONNECTED) {
            desc = "网络切换中";
        } else if (wifi == NetworkInfo.State.DISCONNECTING) {
            desc = "网络切换中";
        } else if (wifi == NetworkInfo.State.SUSPENDED) {
            desc = "连接中...";
        } else if (wifi == NetworkInfo.State.CONNECTED) {
            desc = "已连接";
        } else if (wifi == NetworkInfo.State.UNKNOWN) {
            desc = "网络异常,请在高级设置，进入高级系统设置";
        }
        holder.txtWifiName.setText(scanResult.SSID);

        //=========================焦点事件===============================================
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.clickItem(datas.get(position), position);
                }
            }
        });

        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                changeViewState(b, view);
            }
        });

        holder.itemView.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View view, MotionEvent motionEvent) {
                int what = motionEvent.getAction();
                switch (what) {
                    case MotionEvent.ACTION_HOVER_ENTER:  //鼠标进入view
                        changeViewState(true, view);
                        break;
                    case MotionEvent.ACTION_HOVER_EXIT:  //鼠标离开view
                        changeViewState(false, view);
                        break;
                }
                return false;
            }
        });
    }

    public void changeViewState(boolean isChooice, View view) {
        if (isChooice) {
            view.setBackgroundResource(R.drawable.rect_circle_app);
        } else {
            view.setBackgroundResource(R.drawable.rect_circle_trans);
        }
    }


    /***
     * 更新是否需要密碼的圖標
     * @param isLock
     * @param imgLock
     */
    public void updateLock(String isLock, ImageView imgLock) {
        if (TextUtils.isEmpty(isLock)) {
            imgLock.setVisibility(View.GONE);
        } else {
            imgLock.setVisibility(View.VISIBLE);
        }
    }

    /***
     * 更新wifi图标
     * @param level
     * @param wifiImage
     */
    public void updateWifiImg(int level, ImageView wifiImage) {
        int imgId = R.drawable.wifi_4;
        if (Math.abs(level) > 100) {
            imgId = R.drawable.wifi_1;
        } else if (Math.abs(level) > 80) {
            imgId = R.drawable.wifi_2;
        } else if (Math.abs(level) > 70) {
            imgId = R.drawable.wifi_3;
        } else if (Math.abs(level) > 60) {
            imgId = R.drawable.wifi_3;
        } else if (Math.abs(level) > 50) {
            imgId = R.drawable.wifi_4;
        } else {
            imgId = R.drawable.wifi_4;
        }
        wifiImage.setImageResource(imgId);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        viewGroup = (ViewGroup) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wifi_child_child, viewGroup, false);
        context = viewGroup.getContext();
        return new WifiListLittleAdapter.ViewHolder(viewGroup);
    }


    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtWifiName;
        public ImageView imgWifiLevelIco;
        ImageView img_wifi_lock;

        public ViewHolder(View paramView) {
            super(paramView);
            txtWifiName = (TextView) paramView.findViewById(R.id.txtWifiName);
            imgWifiLevelIco = (ImageView) paramView.findViewById(R.id.imgWifiLevelIco);
            img_wifi_lock = (ImageView) paramView.findViewById(R.id.img_wifi_lock);
        }
    }


    ObjectAdapterClickListener listener;

    public void setOnAdapterClickListener(ObjectAdapterClickListener listener) {
        this.listener = listener;
    }
}
