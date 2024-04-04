package com.jamma.vr_star.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.jamma.vr_star.MainActivity;

import java.util.List;

public class WifiConnect {
    WifiManager wifiManager;

    //定义几种加密方式，一种是WEP，一种是WPA，还有没有密码的情况
    public enum WifiCipherType {
        WIFICIPHER_WEP, WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID
    }

    //构造函数
    public WifiConnect(Context context) {
        this.wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    //打开wifi功能
    private boolean OpenWifi() {
        boolean bRet = true;
        if (!wifiManager.isWifiEnabled()) {
            bRet = wifiManager.setWifiEnabled(true);
        }
        return bRet;
    }

    //wifi是否开启
    public boolean isWifiEnabled() {
        return wifiManager.isWifiEnabled();
    }

    //提供一个外部接口，传入要连接的无线网
    public boolean Connect(String SSID, String Password, WifiCipherType Type) {
        if (!this.OpenWifi()) {
            return false;
        }

        while (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
            try {
                //为了避免程序一直while循环，让它睡个100毫秒在检测……
                Thread.currentThread();
                Thread.sleep(100);
            } catch (InterruptedException ie) {
            }
        }

        WifiConfiguration wifiConfig = this.CreateWifiInfo(SSID, Password, Type);
        //
        if (wifiConfig == null) {
            return false;
        }

        WifiConfiguration tempConfig = this.IsExsits(SSID);

        if (tempConfig != null) {
            wifiManager.removeNetwork(tempConfig.networkId);
        }

        int netID = wifiManager.addNetwork(wifiConfig);
        boolean bRet = wifiManager.enableNetwork(netID, false);
        return bRet;
    }

    //查看以前是否也配置过这个网络
    private WifiConfiguration IsExsits(String SSID) {
//        List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
//        for (WifiConfiguration existingConfig : existingConfigs) {
//            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
//                return existingConfig;
//            }
//        }
        return null;
    }

    public boolean isWifiConnect() {
        ConnectivityManager connManager = (ConnectivityManager) MainActivity.M_this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifiInfo.isConnected();
    }

    public boolean checkWifiState() {
        boolean isConnect = false;
        if (isWifiConnect()) {
            WifiManager mWifiManager = (WifiManager) MainActivity.M_this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
            int wifi = mWifiInfo.getRssi();//获取wifi信号强度
            if (wifi > -50 && wifi < 0) {//最强
            } else if (wifi > -70 && wifi < -50) {//较强
            } else if (wifi > -80 && wifi < -70) {//较弱
            } else if (wifi > -100 && wifi < -80) {//微弱
            }
            isConnect = true;
        } else {
            //无连接
            //  mImageView.setImageResource(R.drawable.wifi_null);
            isConnect = false;
        }
        return isConnect;
    }

    private WifiConfiguration CreateWifiInfo(String SSID, String Password, WifiCipherType Type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
        if (Type == WifiCipherType.WIFICIPHER_NOPASS) {
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (Type == WifiCipherType.WIFICIPHER_WEP) {
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (Type == WifiCipherType.WIFICIPHER_WPA) {
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.status = WifiConfiguration.Status.ENABLED;
        } else {
            return null;
        }
        return config;
    }

    private List<WifiConfiguration> GetWifiList() {

//        List<WifiConfiguration> configs = wifiManager.getConfiguredNetworks();
//        return configs;
        return null;
    }

    //提供一个外部接口，传入要连接的无线网
    public boolean OpenWifi_ConnectSave() {
        if (!this.OpenWifi()) {
            return false;
        }

        while (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
            try {
                //为了避免程序一直while循环，让它睡个100毫秒在检测……
                Thread.currentThread();
                Thread.sleep(100);
            } catch (InterruptedException ie) {
            }
        }
        boolean isConnect = false;
        while (!isConnect) {

            isConnect = wifiManager.enableNetwork(GetWifiList().get(0).networkId, true);
        }
        return isConnect;
    }

    //提供一个外部接口，传入要连接的无线网
    public boolean ConnectSave() {
        boolean isConnect = false;
        while (!isConnect) {

            isConnect = wifiManager.enableNetwork(GetWifiList().get(0).networkId, true);
        }
        return isConnect;
    }


    //本地IP
    public String getLocalIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
        return ipAddress;
    }

    //转IPstr
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    /**
     * 判断WIFI是否连接成功
     *
     * @param context
     * @return
     */
    public boolean isWifiContected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (info != null && info.isConnected()) {
            Log.i("tag", "Wifi网络连接成功");
            return true;
        }
        Log.i("tag", "Wifi网络连接失败");
        return false;
    }
}
