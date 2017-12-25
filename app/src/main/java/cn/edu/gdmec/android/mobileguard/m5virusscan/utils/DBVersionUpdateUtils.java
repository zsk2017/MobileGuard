package cn.edu.gdmec.android.mobileguard.m5virusscan.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.edu.gdmec.android.mobileguard.R;
import cn.edu.gdmec.android.mobileguard.m5virusscan.entity.DbVersionEntity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2017/11/16.
 */

public class DBVersionUpdateUtils {
    private static final int MESSAGE_IO_ERROR = 102;//网络错误代号
    private static final int MESSAGE_JSON_ERROR = 103;//JSON错误代号
    private static final int MESSAGE_SHOW = 104;//SHOW

    private String myDBversion;//本地版本号
    private DbVersionEntity dbVersionEntity;
    private Activity context;
    private Handler parentHandler;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_IO_ERROR:
                    Toast.makeText(context, "网络错误，无法获取最新病毒库", Toast.LENGTH_LONG).show();
                    break;
                case MESSAGE_JSON_ERROR:
                    Toast.makeText(context, "JSON解析错误", Toast.LENGTH_LONG).show();
                    break;
                case MESSAGE_SHOW:
                    showUpdateDialog(dbVersionEntity);
                    break;
            }
        }
    };
    public DBVersionUpdateUtils(String myDBversion, Activity context,Handler parentHandler) {
        this.myDBversion = myDBversion;
        this.context = context;
        this.parentHandler = parentHandler;
    }
    //获取网络版本
    public void getCloudVersion(){
        try {
            HttpClient httpClient = new DefaultHttpClient();
         //设置超时
            HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),5000);
            HttpConnectionParams.setSoTimeout(httpClient.getParams(),5000);
            //请求链接
            HttpGet httpGet = new HttpGet("http://android2017.duapp.com/virusupdateinfo.html");
            //执行

            HttpResponse execute = httpClient.execute(httpGet);
            if (execute.getStatusLine().getStatusCode() == 200){
                HttpEntity httpEntity = execute.getEntity();
                String result = EntityUtils.toString(httpEntity,"utf-8");
                JSONObject jsonObject = new JSONObject(result);
                dbVersionEntity = new DbVersionEntity();
                dbVersionEntity.versionDb = jsonObject.getString("code");
                dbVersionEntity.description = jsonObject.getString("des");
                dbVersionEntity.dburl = jsonObject.getString("apkurl");
                Log.d("Tag", "本地数据库版本为: "+myDBversion);
                Log.d("Tag", "网络数据库版本为: "+dbVersionEntity.versionDb);
                if (!myDBversion.equals(dbVersionEntity.versionDb)){
                    handler.sendEmptyMessage(MESSAGE_SHOW);
                }
            }
        } catch (IOException e) {
           handler.sendEmptyMessage(MESSAGE_IO_ERROR);
            e.printStackTrace();
        } catch (JSONException e){

        }
    }
    /**
     * 选择是否升级的对话框
     * @param dbVersionEntity 网络版本号
     */
    private void showUpdateDialog(final DbVersionEntity dbVersionEntity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("病毒库检查到有新版本：" + dbVersionEntity.versionDb);
        builder.setMessage(dbVersionEntity.description);
        builder.setCancelable(false);//设置不能被忽视
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setPositiveButton("立刻升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downloadNewApk(dbVersionEntity.dburl);
            }
        });
        builder.setNegativeButton("暂不升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    private void downloadNewApk(String apkurl){
        DownloadDbUtils downloadUtils = new DownloadDbUtils(context.getApplicationContext(), new DownloadDbUtils.DownloadCallback() {
            @Override
            public void afterDownload(final String filename) {
                Log.d("Tag", "run: ---------线程开启---"+new File("/download/"+filename).getPath());
                new Thread(){
                    @Override
                    public void run() {
                        try {
//                            FileInputStream fis = new FileInputStream(new File("/download/"+filename));
//                            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),filename);
//                            if (file.exists()&&file.length()>0){
//                                Log.i("Tag", "run----------: 找到"+filename+"大小为"+file.length()+"路径为："+file.getPath());
//                            }else{
//                                Log.d("Tag", "run: 文件不存在"+file.getPath());
//                            }
                            FileInputStream fis = new FileInputStream(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),filename));
//                            FileInputStream fis = context.openFileInput("/download/"+filename);
                            Log.d("Tag", "run: ---------更新文件---"+new File("/download/"+filename).getPath());
                            FileOutputStream fos = context.openFileOutput(filename,MODE_PRIVATE);
                            byte[] buffer = new byte[1024];
                            int len = 0;
                            while((len = fis.read(buffer))!=-1){
                                fos.write(buffer,0,len);
                            }
                            fis.close();
                            fos.close();
                            Log.d("Tag", "run: ---------更新成功---");
                            parentHandler.sendEmptyMessage(100);
                        } catch (IOException e) {
                            Log.d("Tag", "run: IOException---------");
                            e.printStackTrace();
                        }
                    };
                }.start();
            }
        });
        downloadUtils.downloadDB(apkurl,"antivirus.db",context);
    }
}
