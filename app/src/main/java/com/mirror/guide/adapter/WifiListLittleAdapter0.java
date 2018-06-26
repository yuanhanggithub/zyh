package com.mirror.guide.adapter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mirror.R;

import java.util.List;

public class WifiListLittleAdapter0 extends BaseAdapter {

    private List<ScanResult> datas;
    private Context context;
    // 取得WifiManager对象
    private WifiManager mWifiManager;
    private ConnectivityManager cm;

    public void setDatas(List<ScanResult> datas) {
        this.datas = datas;
    }

    public WifiListLittleAdapter0(Context context, List<ScanResult> datas) {
        super();
        this.datas = datas;
        this.context = context;
        mWifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    public int getCount() {
        if (datas == null) {
            return 0;
        }
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.wifi_child_child, null);
            viewHolder = new Holder();
            viewHolder.txtWifiName = (TextView) convertView.findViewById(R.id.txtWifiName);
            viewHolder.imgWifiLevelIco = (ImageView) convertView.findViewById(R.id.imgWifiLevelIco);
            viewHolder.img_wifi_lock = (ImageView) convertView.findViewById(R.id.img_wifi_lock);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (Holder) convertView.getTag();
        }
        // 设置数据
        String desc = "";
        String descOri = datas.get(position).capabilities;
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
        updateWifiImg(level, viewHolder.imgWifiLevelIco);
        updateLock(desc, viewHolder.img_wifi_lock);
        viewHolder.txtWifiName.setTextColor(0xff5cabfa);
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
        viewHolder.txtWifiName.setText(datas.get(position).SSID);
        return convertView;
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

    public static class Holder {
        public TextView txtWifiName;
        public ImageView imgWifiLevelIco;
        ImageView img_wifi_lock;
    }
}
