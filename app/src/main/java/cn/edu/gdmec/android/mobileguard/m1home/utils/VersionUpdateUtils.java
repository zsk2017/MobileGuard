package cn.edu.gdmec.android.mobileguard.m1home.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import cn.edu.gdmec.android.mobileguard.R;
import cn.edu.gdmec.android.mobileguard.m1home.HomeActivity;
import cn.edu.gdmec.android.mobileguard.m1home.entity.VersionEntity;
import cn.edu.gdmec.android.mobileguard.m5virusscan.utils.DownloadDbUtils;

/**
 * Created by Administrator on 2017/9/17.
 */
//获取版本号  对比版本号  下载更新
public class VersionUpdateUtils {
    private static final int MESSAGE_IO_ERROR = 102;//网络错误代号
    private static final int MESSAGE_JSON_ERROR = 103;//JSON错误代号
    private static final int MESSAGE_SHOW_ERROR = 104;//SHOW错误代号
    private static final int MESSAGE_ENTERHOME = 105;//HOME错误代号
    private String mVersion;
    private Activity context;
    private VersionEntity versionEntity;
    //handler
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_IO_ERROR:
                    Toast.makeText(context, "IO错误", Toast.LENGTH_LONG).show();
                    //测试用 网络错误也进入主界面
                    Intent intent1 = new Intent(context, HomeActivity.class);
                    context.startActivity(intent1);
                    context.finish();

                    break;
                case MESSAGE_JSON_ERROR:
                    Toast.makeText(context, "JSON解析错误", Toast.LENGTH_LONG).show();
                    break;
                case MESSAGE_SHOW_ERROR:
                    showUpdateDialog(versionEntity);
                    break;
                case MESSAGE_ENTERHOME:
                    Intent intent = new Intent(context, HomeActivity.class);
                    context.startActivity(intent);
                    context.finish();
                    break;

            }
        }
    };

    /**
     *
     * @param mVersion
     * @param context
     */
    public VersionUpdateUtils(String mVersion, Activity context) {
        this.mVersion = mVersion;
        this.context = context;
    }

    public void getCloudVersion() {
        try {
            HttpClient httpClient = new DefaultHttpClient();
            //设置超时
            HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 5000);
            HttpConnectionParams.setSoTimeout(httpClient.getParams(), 5000);
//            请求链接
            HttpGet httpGet = new HttpGet("http://android2017.duapp.com/updateinfo.html");
//            执行
            HttpResponse execute = httpClient.execute(httpGet);
//            比对返回码 200 为成功
            if (execute.getStatusLine().getStatusCode() == 200) {
//                获取服务器返回的内容并处理
                HttpEntity httpEntity = execute.getEntity();
                String result = EntityUtils.toString(httpEntity, "utf-8");
                JSONObject jsonObject = new JSONObject(result);
                versionEntity = new VersionEntity();
                versionEntity.versioncode = jsonObject.getString("code");
                versionEntity.description = jsonObject.getString("des");
                versionEntity.apkurl = jsonObject.getString("apkurl");
                //Log.d("Tag", "getCloudVersion 本地版本为: " + mVersion);
                if (!mVersion.equals(versionEntity.versioncode)) {
                    //版本不同 需升级
//                    Toast.makeText(context, versionEntity.description, Toast.LENGTH_SHORT).show();
                    handler.sendEmptyMessage(MESSAGE_SHOW_ERROR);
//                    handler.sendEmptyMessage(MESSAGE_ENTERHOME);
                }
            }
        } catch (IOException e) {
            handler.sendEmptyMessage(MESSAGE_IO_ERROR);
            e.printStackTrace();
        } catch (JSONException e) {
            handler.sendEmptyMessage(MESSAGE_JSON_ERROR);
            e.printStackTrace();
        }


    }

    /**
     * 选择是否升级的对话框
     * @param versionEntity 网络版本号
     */
    private void showUpdateDialog(final VersionEntity versionEntity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("检查到有新版本：" + versionEntity.versioncode);
        builder.setMessage(versionEntity.description);
        builder.setCancelable(false);//设置不能被忽视
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setPositiveButton("立即升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               /* Log.d("Tag", "getCloudVersion 网络版本为: " + versionEntity.versioncode);
                DownloadUtils downloadUtils = new DownloadUtils();
                downloadUtils.downloadApk(versionEntity.apkurl, "mobileguard.apk", context);
                Log.d("Tag", "下载成功");*/
               downloadNewApk(versionEntity.apkurl);
            }
        });
        builder.setNegativeButton("暂不升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                enterHome();
            }
        });
        builder.show();
    }

    /**
     * 通过handler的message进入主界面
     */
    private void enterHome() {
        handler.sendEmptyMessage(MESSAGE_ENTERHOME);
    }
    private void downloadNewApk(String apkurl){
        DownloadUtils downloadUtils = new DownloadUtils(context, new DownloadUtils.DownloadCallback() {
            @Override
            public void afterDownload(String filename) {
                installApk(context,filename);
            }
        });
        downloadUtils.downloadApk(apkurl,"app2.apk",context);
    }

    private void installApk(Activity context, String filename) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(Uri.fromFile(
                new File(
                        Environment.getExternalStoragePublicDirectory("/download/").getPath()
                                +"/"+filename)
        ),"application/vnd.android.package-archive");
        context.startActivityForResult(intent,0);
//        enterHome();
    }

}
