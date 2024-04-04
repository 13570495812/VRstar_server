package com.jamma.vr_star.SocketMSG;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.os.Bundle;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtil {
    public static String TAG="Log日志";

    public static void info(String info){
        Log.i(TAG,info);
    }
    public static void debug(String info){
        Log.d(TAG,info);
    }
}
