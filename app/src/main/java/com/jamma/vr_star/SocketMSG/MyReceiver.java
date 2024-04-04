package com.jamma.vr_star.SocketMSG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.util.Log;

import com.jamma.vr_star.MainActivity;
import com.jamma.vr_star.util.Util;
import com.jamma.vr_star.util.WifiConnect;

/**
 * Created by Zain-PC on 2018/4/18 0018.
 */

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {

        Util.restartApp(MainActivity.M_this);

        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {// 这个监听wifi的打开与关闭，与wifi的连接无关
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            Log.e("H3c", "wifiState" + wifiState);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    MainActivity.M_this.isShutdown = false;
                    break;
                //
            }
        }

        // 这个监听wifi的连接状态即是否连上了一个有效无线路由，当上边广播的状态是WifiManager.WIFI_STATE_DISABLING，和WIFI_STATE_DISABLED的时候，根本不会接到这个广播。
        // 在上边广播接到广播是WifiManager.WIFI_STATE_ENABLED状态的同时也会接到这个广播，当然刚打开wifi肯定还没有连接到有效的无线
        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (null != parcelableExtra) {
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                NetworkInfo.State state = networkInfo.getState();
                boolean isConnected = state == NetworkInfo.State.CONNECTED;// 当然，这边可以更精确的确定状态

                if (new WifiConnect(context).isWifiConnect()) {
                    MainActivity.M_this.isShutdown = false;
                    Util.restartApp(context);
                } else {
                    if (MainActivity.M_this != null) {
                        if (MainActivity.M_this.isShutdown == false) {
                            MainActivity.M_this.TimeOutShutdown();
                        }
                    }
                }
            }
        }


        switch (intent.getAction()) {
            case "com.picovr.wing.player.sendStatus":
                break;
            case "android.intent.action.BOOT_COMPLETED":
                new WifiConnect(context).OpenWifi_ConnectSave();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent mainActivityIntent = new Intent(context, MainActivity.class);  // 要启动的Activity
                mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(mainActivityIntent);
                break;
            case "android.intent.action.ACTION_SHUTDOWN":
                break;
            case "android.intent.action.SCREEN_ON":
//                jamma_start.jamma_start.socketConnect.SendToServer((byte)0x55);
                break;
            case "android.intent.action.SCREEN_OFF":
//                jamma_start.jamma_start.socketConnect.SendToServer((byte)0xAA);
                break;

        }
        //jamma_start.StopVideo(msg.obj.toString());


    }


    }



