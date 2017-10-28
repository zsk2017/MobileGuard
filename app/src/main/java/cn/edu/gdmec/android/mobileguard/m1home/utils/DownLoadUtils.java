package cn.edu.gdmec.android.mobileguard.m1home.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

/**
 * Created by lenovo on 2017/9/19.
 */

public class DownLoadUtils {
    public static DownLoadUtils downloadutils;

    public static void downloadApk(String url, String targetFile, Context context){
        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(url));
        request.setAllowedOverRoaming(false);
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString=mimeTypeMap.getMimeTypeFromExtension(mimeTypeMap.getFileExtensionFromUrl(url));
        request.setMimeType(mimeString);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setVisibleInDownloadsUi(true);

        request.setDestinationInExternalPublicDir("/download",targetFile);
        DownloadManager downloadManager =(DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        long mTaskid=downloadManager.enqueue(request);
    }
}
