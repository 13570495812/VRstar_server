package com.jamma.vr_star.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

//import com.google.android.gms.common.util.IOUtils;


import com.jamma.vr_star.MainActivity;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * 静态工具工厂
 *
 * @author user
 */
public class Util {

    //获取机器序列号
    public static String getSerialNumber() {

        String serial = null;
        try {

            Class<?> c = Class.forName("android.os.SystemProperties");

            Method get = c.getMethod("get", String.class);

            serial = (String) get.invoke(c, "ro.serialno");

        } catch (Exception e) {

            e.printStackTrace();

        }

        return serial;

    }

    /**
     * 依靠包名启动应用
     */
    public static boolean startApp(Context context, String pkgName) {
        try {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(pkgName);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 依靠包名关闭应用
     */
    public static void stopApp(Context context, String pkgName) {
        Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        context.startActivity(mHomeIntent);
        try {
            Thread.sleep(1500);
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            manager.killBackgroundProcesses(pkgName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断程序是否在前台运行
     *
     * @param packageName
     * @return
     */
    public static boolean isOpen(Context context, String packageName) {
        if (packageName.equals("") | packageName == null)
            return false;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningAppProcesses = manager.getRunningAppProcesses();
        for (RunningAppProcessInfo runinfo : runningAppProcesses) {
            String pn = runinfo.processName;
            if (pn.equals(packageName) && runinfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
                return true;
        }
        return false;
    }

    //重启APP
    public static void restartApp(Context context) {
        if (!MainActivity.M_this.isGameing) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());  //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
        }
    }

    public static void forceStopPackage(Context context){
        Log.d("", "forceStopPackage: " +context.getPackageName());
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    //httpPost请求
    public static boolean doHttpPost(String URL, String Json) {
        boolean isUp = false;
        System.out.println("发起的数据:" + Json);
        byte[] jsonData = Json.getBytes();
        InputStream instr = null;
        try {
            URL url = new URL(URL);
            URLConnection urlCon = url.openConnection();
            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);
            urlCon.setUseCaches(false);
            urlCon.setRequestProperty("content-Type", "application/json");
            urlCon.setRequestProperty("charset", "utf-8");
            urlCon.setRequestProperty("Content-length", String.valueOf(jsonData.length));
            System.out.println(String.valueOf(jsonData.length));

            DataOutputStream printout = new DataOutputStream(urlCon.getOutputStream());
            printout.write(jsonData);
            printout.flush();
            printout.close();
            instr = urlCon.getInputStream();

//            byte[] bis = IOUtils.toByteArray(instr);
//            String ResponseString = new String(bis, "UTF-8");
//            if ((ResponseString == null) || ("".equals(ResponseString.trim()))) {
//                System.out.println("返回空");
//            }
//            System.out.println("返回数据为:" + ResponseString);
//            JSONObject jsb = new JSONObject(ResponseString.toString());
//            switch (jsb.getString("code")) {
//                case "1":
//                    isUp = true;
//                    break;
//            }
            return isUp;

        } catch (Exception e) {
            e.printStackTrace();
            return isUp;
        } finally {
            try {
                instr.close();

            } catch (Exception ex) {
                return isUp;
            }
        }
    }

}
