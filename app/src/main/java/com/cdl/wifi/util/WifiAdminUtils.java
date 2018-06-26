package com.cdl.wifi.util;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.mirror.view.MyToastView;
import com.mirror.wifi.ApMgr;

import java.net.Inet4Address;
import java.util.List;

/**
 * Function:Wifi连接管理工具类
 * Created by Xiho on 2016/2/1.
 */
public class WifiAdminUtils {


    // 定义一个WifiManager对象
    private WifiManager mWifiManager;
    // 定义一个WifiInfo对象
    private WifiInfo mWifiInfo;
    // 扫描出的网络连接列表
    private List<ScanResult> mScanWifiList;
    // 网络连接列表
    Context mContext;

    public WifiAdminUtils(Context mContext) {
        this.mContext = mContext;
        //取得WifiManager对象
        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        //取得WifiInfo对象
        mWifiInfo = mWifiManager.getConnectionInfo();
    }

    public void startScan() {
        try{
            boolean isApOPen = ApMgr.isApOn(mContext);
            if (isApOPen){
                ApMgr.disableAp(mContext);
            }
            boolean isOpen = openWifi();
            Log.i("ServiceWifi", "打開wifi的開關===" + isOpen);
            mWifiManager.startScan();
            mScanWifiList = mWifiManager.getScanResults();
        }catch (Exception e){
        }
    }

    // 得到网络列表
    public List<ScanResult> getWifiList() {
        return mScanWifiList;
    }


    // 断开指定ID的网络
    public boolean disConnectionWifi(int netId) {
        boolean isBack = false;
        boolean isDisable = mWifiManager.disableNetwork(netId);
        if (isDisable) {
            boolean isDisConnect = mWifiManager.disconnect();
            if (isDisConnect) {
                isBack = true;
            } else {
                MyToastView.getInstance().Toast(mContext, "断开连接失败");
                isBack = false;
            }
        } else {
            isBack = false;
            MyToastView.getInstance().Toast(mContext, "移除配置信息失败");
        }
        return isBack;
    }

    /**
     * Function: 打开wifi功能<br>
     *
     * @return true:打开成功；false:打开失败<br>
     */
    public boolean openWifi() {
        boolean bRet = false;
        bRet = mWifiManager.setWifiEnabled(true);
        return bRet;
    }


    public void ScanWifiList() {
        mWifiManager.startScan();
        mScanWifiList = mWifiManager.getScanResults();
    }

    /**
     * 给外部提供一个借口，连接无线网络
     *
     * @param SSID
     * @param Password
     * @param Type
     * @return true:连接成功；false:连接失败<br>
     */
    public boolean connect(String SSID, String Password, WifiConnectUtils.WifiCipherType Type) {
        if (!this.openWifi()) {
            return false;
        }
        // 状态变成WIFI_STATE_ENABLED的时候才能执行下面的语句
        while (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
            try {
                // 为了避免程序一直while循环，让它睡个100毫秒在检测……
                Thread.currentThread();
                Thread.sleep(100);
            } catch (Exception ie) {
            }
        }
        if (SSID == null || Password == null || SSID.equals("")) {
            Log.e(this.getClass().getName(),
                    "addNetwork() ## nullpointer error!");
            return false;
        }
        WifiConfiguration wifiConfig = createWifiInfo(SSID, Password, Type);
        // wifi的配置信息
        if (wifiConfig == null) {
            return false;
        }
        // 查看以前是否也配置过这个网络
        WifiConfiguration tempConfig = this.isExsits(SSID);
        if (tempConfig != null) {

            mWifiManager.removeNetwork(tempConfig.networkId);
        }
        // 添加一个新的网络描述为一组配置的网络。
        int netID = mWifiManager.addNetwork(wifiConfig);
        Log.d("WifiListActivity", "wifi的netID为：" + netID);
        // 断开连接
        mWifiManager.disconnect();
        // 重新连接
        Log.d("WifiListActivity", "Wifi的重新连接netID为：" + netID);
        // 设置为true,使其他的连接断开
        boolean mConnectConfig = mWifiManager.enableNetwork(netID, true);
        mWifiManager.reconnect();
        return mConnectConfig;
    }

    // 查看以前是否也配置过这个网络
    public WifiConfiguration isExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = mWifiManager
                .getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }

    private WifiConfiguration createWifiInfo(String SSID, String Password,
                                             WifiConnectUtils.WifiCipherType Type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
        if (Type == WifiConnectUtils.WifiCipherType.WIFICIPHER_NOPASS) {
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (Type == WifiConnectUtils.WifiCipherType.WIFICIPHER_WEP) {
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (Type == WifiConnectUtils.WifiCipherType.WIFICIPHER_WPA) {
            // 修改之后配置
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP);
            // config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.CCMP);

        } else {
            return null;
        }
        return config;
    }

    /**
     * Function:判断扫描结果是否连接上<br>
     *
     * @param result
     * @return<br>
     */
    public boolean isConnect(ScanResult result) {
        if (result == null) {
            return false;
        }

        mWifiInfo = mWifiManager.getConnectionInfo();
        String g2 = "\"" + result.SSID + "\"";
        if (mWifiInfo.getSSID() != null && mWifiInfo.getSSID().endsWith(g2)) {
            return true;
        }
        return false;
    }

    /**
     * Function: 将int类型的IP转换成字符串形式的IP<br>
     *
     * @param ip
     * @return<br>
     */
    public String ipIntToString(int ip) {
        try {
            byte[] bytes = new byte[4];
            bytes[0] = (byte) (0xff & ip);
            bytes[1] = (byte) ((0xff00 & ip) >> 8);
            bytes[2] = (byte) ((0xff0000 & ip) >> 16);
            bytes[3] = (byte) ((0xff000000 & ip) >> 24);
            return Inet4Address.getByAddress(bytes).getHostAddress();
        } catch (Exception e) {
            return "";
        }
    }

    public int getConnNetId() {
        // result.SSID;
        mWifiInfo = mWifiManager.getConnectionInfo();
        return mWifiInfo.getNetworkId();
    }

    /**
     * Function:信号强度转换为字符串
     *
     * @param level
     * @author Xiho
     */
    public static String singlLevToStr(int level) {

        String resuString = "无信号";

        if (Math.abs(level) > 100) {
        } else if (Math.abs(level) > 80) {
            resuString = "弱";
        } else if (Math.abs(level) > 70) {
            resuString = "强";
        } else if (Math.abs(level) > 60) {
            resuString = "强";
        } else if (Math.abs(level) > 50) {
            resuString = "较强";
        } else {
            resuString = "极强";
        }
        return resuString;
    }

    /**
     * 添加到网络
     *
     * @param wcg
     * @author Xiho
     */
    public boolean addNetwork(WifiConfiguration wcg) {
        if (wcg == null) {
            return false;
        }
        int wcgID = mWifiManager.addNetwork(wcg);
        boolean b = mWifiManager.enableNetwork(wcgID, true);
        mWifiManager.saveConfiguration();
        System.out.println(b);
        return b;
    }

    public boolean connectSpecificAP(ScanResult scan) {
        List<WifiConfiguration> list = mWifiManager.getConfiguredNetworks();
        boolean networkInSupplicant = false;
        boolean connectResult = false;
        // 重新连接指定AP
        mWifiManager.disconnect();
        for (WifiConfiguration w : list) {
            // 将指定AP 名字转化
            // String str = convertToQuotedString(info.ssid);
            if (w.BSSID != null && w.BSSID.equals(scan.BSSID)) {
                connectResult = mWifiManager.enableNetwork(w.networkId, true);
                // mWifiManager.saveConfiguration();
                networkInSupplicant = true;
                break;
            }
        }
        if (!networkInSupplicant) {
            WifiConfiguration config = CreateWifiInfo(scan, "");
            connectResult = addNetwork(config);
        }

        return connectResult;
    }


    // 然后是一个实际应用方法，只验证过没有密码的情况：
    public WifiConfiguration CreateWifiInfo(ScanResult scan, String Password) {
        WifiConfiguration config = new WifiConfiguration();
        config.hiddenSSID = false;
        config.status = WifiConfiguration.Status.ENABLED;

        if (scan.capabilities.contains("WEP")) {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.WEP104);

            config.SSID = "\"" + scan.SSID + "\"";

            config.wepTxKeyIndex = 0;
            config.wepKeys[0] = Password;
            // config.preSharedKey = "\"" + SHARED_KEY + "\"";
        } else if (scan.capabilities.contains("PSK")) {
            //
            config.SSID = "\"" + scan.SSID + "\"";
            config.preSharedKey = "\"" + Password + "\"";
        } else if (scan.capabilities.contains("EAP")) {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.SSID = "\"" + scan.SSID + "\"";
            config.preSharedKey = "\"" + Password + "\"";
        } else {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.SSID = "\"" + scan.SSID + "\"";
            config.preSharedKey = null;
        }
        return config;
    }


}

