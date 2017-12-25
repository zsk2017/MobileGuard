package cn.edu.gdmec.android.mobileguard.m4appmanager.utils;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.edu.gdmec.android.mobileguard.m4appmanager.entity.AppInfo;
public class AppInfoParser {
    /**
     * 获取手机里面的所有的应用程序
     * @param context 上下文
     * @return
     */
    public static List<AppInfo> getAppInfos(Context context){
        //获取包管理器
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
        List<AppInfo> appInfos = new ArrayList<AppInfo>();

        for (PackageInfo packInfo:packageInfos) {
            AppInfo appinfo = new AppInfo();
            String packname = packInfo.packageName;
            appinfo.packageName = packname;
            Drawable icon = packInfo.applicationInfo.loadIcon(pm);
            appinfo.icon=icon;
            String appname = packInfo.applicationInfo.loadLabel(pm).toString();
            appinfo.appName = appname;
            //应用程序apk包的路径
            String apkpath = packInfo.applicationInfo.sourceDir;
            appinfo.apkPath = apkpath;
            File file = new File(apkpath);
            long appSize = file.length();
            appinfo.appSize = appSize;
            //应用的版本号
            String version = packInfo.versionName;
            appinfo.version = version;
            //应用的安装时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            appinfo.installTime = sdf.format(new Date(packInfo.firstInstallTime));
            //应用的签名
            try {
                PackageInfo packinfo = pm.getPackageInfo(packname, PackageManager.GET_SIGNATURES);
                byte[] ss = packinfo.signatures[0].toByteArray();
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                X509Certificate cert = (X509Certificate) cf.generateCertificate(
                        new ByteArrayInputStream(ss));
                if (cert!=null){
                    appinfo.certifi=cert.getIssuerDN().toString();
                }

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            }
            //应用的权限申请信息

                PackageInfo packinfo2 = null;
            try {
                packinfo2 = pm.getPackageInfo(packname, PackageManager.GET_PERMISSIONS);
                if (packinfo2.requestedPermissions!=null){
                    for (String pio : packinfo2.requestedPermissions){
                        appinfo.permisstion= appinfo.permisstion+pio+"\n";
                    }
                }
//                String[] p = packinfo2.requestedPermissions;
//                for (String s : p) {
//                    appinfo.permisstion+=s;
//                }

            } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
            }
            //应用activityinfo
            PackageInfo packinfo3;
            try {
                packinfo3 = pm.getPackageInfo(packname,PackageManager.GET_ACTIVITIES);
                ActivityInfo[] activityInfos =  packinfo3.activities;
                if (activityInfos != null) {
                        for (ActivityInfo info : activityInfos) {
                            appinfo.activityInfo = appinfo.activityInfo + info.name + "\n";
                        }
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }


            //应用程序安装的位置
            int flags = packInfo.applicationInfo.flags;//二进制映射 大bit-map
            if ((ApplicationInfo.FLAG_EXTERNAL_STORAGE & flags)!=0){
                //外部存储
                appinfo.isInRoom = false;
            }else {
                //手机存储
                appinfo.isInRoom = true;
            }
            if ((ApplicationInfo.FLAG_SYSTEM&flags)!=0){
                //系统应用
                appinfo.isUserApp = false;

            }else {
                //用户应用
                appinfo.isUserApp = true;

            }
            appInfos.add(appinfo);
//            appinfo = null;

        }
        return appInfos;
    }
}
