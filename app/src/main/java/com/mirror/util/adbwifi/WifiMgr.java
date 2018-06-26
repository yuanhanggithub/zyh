package com.mirror.util.adbwifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

public class WifiMgr {
    public static final int WIFICIPHER_NOPASS = 1;
    public static final int WIFICIPHER_WEP = 2;
    public static final int WIFICIPHER_WPA = 3;
    private static WifiMgr mWifiMgr;
    private Context mContext;
    List<ScanResult> mScanResultList;
    List<WifiConfiguration> mWifiConfigurations;
    WifiInfo mWifiInfo;
    private WifiManager mWifiManager;

    private WifiMgr(Context paramContext) {
        this.mContext = paramContext;
        this.mWifiManager = ((WifiManager) paramContext.getSystemService(Context.WIFI_SERVICE));
    }

    public static WifiMgr getInstance(Context paramContext) {
        if (mWifiMgr == null) {
        }
        try {
            if (mWifiMgr == null) {
                mWifiMgr = new WifiMgr(paramContext);
            }
            return mWifiMgr;
        } finally {
        }
    }

    public boolean addNetwork(WifiConfiguration paramWifiConfiguration) {
        disconnectCurrentNetwork();
        int i = this.mWifiManager.addNetwork(paramWifiConfiguration);
        return this.mWifiManager.enableNetwork(i, true);
    }

    public void closeWifi() {
        if (this.mWifiManager.isWifiEnabled()) {
            this.mWifiManager.setWifiEnabled(false);
        }
    }

    public void disableWifi() {
        if (this.mWifiManager != null) {
            this.mWifiManager.setWifiEnabled(false);
        }
    }

    public boolean disconnectCurrentNetwork() {
        if ((this.mWifiManager != null) && (this.mWifiManager.isWifiEnabled())) {
            int i = this.mWifiManager.getConnectionInfo().getNetworkId();
            this.mWifiManager.disableNetwork(i);
            return this.mWifiManager.disconnect();
        }
        return false;
    }


    /***
     *
     * @return
     */
    public static String getLocalNetIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {

        }
        return null;
    }


    /***
     * 获取WIFI下的ip地址
     * @return
     */
    public String getCurrentIpAddress() {
        int i = this.mWifiManager.getDhcpInfo().ipAddress;
        return (i & 0xFF) + "." + (i >> 8 & 0xFF) + "." + (i >> 16 & 0xFF) + "." + (i >> 24 & 0xFF);
    }

    public String getHotspotLocalIpAddress() {
        int i = this.mWifiManager.getDhcpInfo().serverAddress;
        return (i & 0xFF) + "." + (i >> 8 & 0xFF) + "." + (i >> 16 & 0xFF) + "." + (i >> 24 & 0xFF);
    }

    public String getIpAddressFromHotspot() {
        int i = this.mWifiManager.getDhcpInfo().gateway;
        return (i & 0xFF) + "." + (i >> 8 & 0xFF) + "." + (i >> 16 & 0xFF) + "." + (i >> 24 & 0xFF);
    }

    public List<ScanResult> getScanResultList() {
        return this.mScanResultList;
    }

    public List<WifiConfiguration> getWifiConfigurations() {
        return this.mWifiConfigurations;
    }

    public WifiInfo getWifiInfo() {
        this.mWifiInfo = this.mWifiManager.getConnectionInfo();
        return this.mWifiInfo;
    }

    public boolean isWifiEnable() {
        if (this.mWifiManager == null) {
            return false;
        }
        return this.mWifiManager.isWifiEnabled();
    }

    public void openWifi() {
        if (!this.mWifiManager.isWifiEnabled()) {
            this.mWifiManager.setWifiEnabled(true);
        }
    }

    public void startScan() {
        this.mWifiManager.startScan();
        this.mScanResultList = this.mWifiManager.getScanResults();
        this.mWifiConfigurations = this.mWifiManager.getConfiguredNetworks();
    }
}
