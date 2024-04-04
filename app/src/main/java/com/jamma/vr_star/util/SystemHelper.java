package com.jamma.vr_star.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import java.util.ArrayList;
import java.util.List;

public class SystemHelper {

    /**
     * 检查指定包名的应用程序是否已安装
     * @param context 上下文
     * @param packageName 包名
     * @return 如果应用程序已安装，则返回 true；否则返回 false。
     */
    public static boolean appIsExist(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
        List<String> packageNames = new ArrayList<String>();
        if (packageInfoList != null) {
            for (int i = 0; i < packageInfoList.size(); i++) {
                String packName = packageInfoList.get(i).packageName;
                packageNames.add(packName);
            }
        }
        return packageNames.contains(packageName);
    }
}