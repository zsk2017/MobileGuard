package cn.edu.gdmec.android.mobileguard.m9advancedtools.utils;

/**
 * Created by Jack on 2017/12/3.
 */

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

import cn.edu.gdmec.android.mobileguard.m9advancedtools.entity.AppInfo;


public class AppInfoParser{
    /**
     * 获取手机里面的所有的应用程序
     */
    public static List<AppInfo> getAppInfos(Context context){
        //得到一个java保证的 包管理器。
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packInfos = pm.getInstalledPackages(0);
        List<AppInfo> appinfos = new ArrayList<AppInfo>();
        for(PackageInfo packInfo:packInfos){
            AppInfo appinfo = new AppInfo();
            String packname = packInfo.packageName;
            appinfo.packageName = packname;
            Drawable icon = packInfo.applicationInfo.loadIcon(pm);
            appinfo. icon = icon;
            String appname = packInfo.applicationInfo.loadLabel(pm).toString();
            appinfo.appName = appname;
            //应用程序apk包的路径
            String apkpath = packInfo.applicationInfo.sourceDir;
            appinfo.apkPath = apkpath;
            appinfos.add(appinfo);
            appinfo = null;
        }
        return appinfos;
    }
}
