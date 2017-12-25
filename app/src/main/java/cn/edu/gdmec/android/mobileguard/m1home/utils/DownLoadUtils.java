package cn.edu.gdmec.android.mobileguard.m1home.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import cn.edu.gdmec.android.mobileguard.m5virusscan.utils.DownloadDbUtils;

/**
 * Created by Administrator on 2017/9/17.
 */
//下载的工具类
public class DownloadUtils {
    private Context context;
    private DownloadUtils.DownloadCallback callbacl;
    private BroadcastReceiver broadcastReceiver;
    public DownloadUtils(Context context, DownloadUtils.DownloadCallback callbacl){
        this.context = context;
        this.callbacl = callbacl;

    }
    /**
     * 下载akp的方法
     * @param url
     * @param targetFile
     * @param context
     */

    public void downloadApk(String url,String targetFile,Context context){
        //通过url获得系统下载管理   DownloadManager.Request用来请求一个下载
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //setAllowedOverRoaming用于设置漫游状态下是否可以下载
        request.setAllowedOverRoaming(false);
        //MimeTypeMap作用是告诉Android系统本Activity可以处理的文件的类型。
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimsString = mimeTypeMap.getMimeTypeFromExtension(mimeTypeMap.getFileExtensionFromUrl(url));
        request.setMimeType(mimsString);//设置下载的文件类型因为下载管理Ui中点击某个已下载完成文件及下载完成点击通知栏提示都会根据mimeType去打开文件，所以我们可以利用这个属性。
        //用于设置下载时时候在状态栏显示通知信息
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setVisibleInDownloadsUi(true);
//        用于设置下载文件的存放路径
        request.setDestinationInExternalPublicDir("/download",targetFile);

        //获得系统的下载服务
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        long mTaskid = downloadManager.enqueue(request);
        listener(mTaskid,targetFile);
    }
    private void listener(final long mTaskid, final String targetFile) {
        IntentFilter intf = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long ID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1);
                if (ID == mTaskid){
                    Toast.makeText(context.getApplicationContext(), "下载编号："+mTaskid+"的"+targetFile+"下载完成", Toast.LENGTH_LONG).show();
                    callbacl.afterDownload(targetFile);
                }
                context.unregisterReceiver(broadcastReceiver);

            }
        };
        context.registerReceiver(broadcastReceiver,intf);

    }

    public interface DownloadCallback{
        void afterDownload(final String filename);
    }
}
