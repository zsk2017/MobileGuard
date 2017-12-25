package cn.edu.gdmec.android.mobileguard.m1home.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Administrator on 2017/9/17.
 */

public class MyUtils {
    /**
     * 获取本地版本号
     * @param context
     * @return  返回本地版本号
     */
    public static String getVersion(Context context){
//        PackageManager 可以获取清单文件中的所有信息
        PackageManager packageManager = context.getPackageManager();
        try {
            //getPackageName()获取到当前程序的包名
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(),0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }


    }
}
