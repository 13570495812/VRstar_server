package com.jamma.vr_star.util;

import static android.content.Context.ACTIVITY_SERVICE;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Build;
import android.util.Log;

import java.util.List;
import java.util.logging.Logger;

public class TgSystem {

    public static void startService(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //android8.0以上通过startForegroundService启动service
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    /**
     * 判断本地是否已经安装好了指定的应用程序包
     *
     * @param packageNameTarget ：待判断的 App 包名，如 微博 com.sina.weibo
     * @return 已安装时返回 true,不存在时返回 false
     */
    public static boolean appIsExist(Context context, String packageNameTarget) {
        if (packageNameTarget == null || packageNameTarget.isEmpty()) {
            return false;
        }

        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(PackageManager.MATCH_UNINSTALLED_PACKAGES);
        for (PackageInfo packageInfo : packageInfoList) {
            if (packageInfo.packageName.equals(packageNameTarget)) {
                return true;
            }
        }

        return false;
    }

    //当本应用位于后台时，则将它切换到最前端
    public static void setTopApp(Context context) {
        Log.e("22222222","12121212");
//        if (isRunningForeground(context)) {
//            return;
//        }
        //获取ActivityManager
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);

        //获得当前运行的task(任务)
        List<ActivityManager.RunningTaskInfo> taskInfoList = activityManager.getRunningTasks(100);
//        Log.e("11111",name);
        for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
            Log.e("22222222",taskInfo.topActivity.getPackageName());
            Log.e("333333", String.valueOf(taskInfo.id));
            //找到本应用的 task，并将它切换到前台
            if (taskInfo.topActivity.getPackageName().equals(taskInfo.topActivity.getPackageName())) {
                Log.e("我知道VR是他让 APP了", String.valueOf(taskInfo.id));
                activityManager.moveTaskToFront(taskInfo.id, 0);
                break;
            }
        }
    }

    //判断本应用是否已经位于最前端：已经位于最前端时，返回 true；否则返回 false
    public static boolean isRunningForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = activityManager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfoList) {
            if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    && appProcessInfo.processName.equals(context.getApplicationInfo().processName)) {
                return true;
            }
        }
        return false;
    }
    /**/
    public static boolean isAppRunning(Context context, String packageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        if (runningAppProcesses == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcesses) {
            if (processInfo.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

}
