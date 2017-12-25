package cn.edu.gdmec.android.mobileguard.m5virusscan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.edu.gdmec.android.mobileguard.R;
import cn.edu.gdmec.android.mobileguard.m5virusscan.dao.AntiVirusDao;
import cn.edu.gdmec.android.mobileguard.m5virusscan.utils.DBVersionUpdateUtils;

public class VirusScanActivity extends AppCompatActivity implements View.OnClickListener{
    public static final int UPDATE = 100;
    public static final int UPDATE_VERSION = 101;
    private TextView mLastTimeTV;
    private TextView myversionTV;
    private SharedPreferences mSP;
    private AntiVirusDao antivirusdao;
    private DBVersionUpdateUtils dbv;
    private String localVersion = "";
    private boolean flag ;//本地数据库是否存在
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE:
                    update();
                    break;
                case UPDATE_VERSION:
                    Log.d("Tag", "handleMessage:-------------- "+antivirusdao);
//                    myversionTV.setText("病毒数据库版本:"+antivirusdao.getVersion());
                    myversionTV.setText("病毒数据库版本:"+localVersion);
                    break;

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virus_scan);
        mSP = getSharedPreferences("config",MODE_PRIVATE);
        antivirusdao = new AntiVirusDao(this.getApplicationContext());
        initView();
    }
    private void update(){
        flag = mSP.getBoolean("updateFlag",false);
        //得到本地版本
        if (!flag){
            return;
        }

        String myDbversion = antivirusdao.getVersion();
        localVersion = myDbversion;
        Log.d("Tag", "本地数据库版本为: "+myDbversion);
        handler.sendEmptyMessage(UPDATE_VERSION);
        if (myDbversion != null){
                Log.d("Tag", "传过去的版本为：------------"+myDbversion);
                dbv = new DBVersionUpdateUtils(myDbversion,this,handler);

            new Thread(){
                @Override
                public void run() {
                    dbv.getCloudVersion();
                }
            }.start();


        }
    }

    @Override
    protected void onResume() {
        String string = mSP.getString("lastVirusScan","您还没有查杀病毒");
        mLastTimeTV.setText(string);
        copyDB("antivirus.db");
        super.onResume();
    }

    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.blue));
        ImageView mLeftImagv = (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView)findViewById(R.id.tv_title)).setText("病毒查杀");
        mLeftImagv.setOnClickListener(this);
        mLeftImagv.setImageResource(R.drawable.back);
        mLastTimeTV = (TextView) findViewById(R.id.tv_lastscantime);
        myversionTV = (TextView) findViewById(R.id.tv_version);
        findViewById(R.id.rl_allscanvirus).setOnClickListener(this);
        findViewById(R.id.rl_cloudscanvirus).setOnClickListener(this);

    }

    /**
     * 拷贝病毒数据库
     * dbname="/data/data/"+context.getPackageName()+"/files/antivirus.db";
     * 将assets下的数据库拷入files中
     * @param dbname
     */
    private void copyDB(final String dbname) {
        //大文件的拷贝复制一定要用线程，否则很容易出现ANR
        new Thread(){
            @Override
            public void run() {
                try {
                    File file = new File(getFilesDir(),dbname);
                    if (file.exists()&&file.length()>0){
                        Log.i("Tag", "VirusScanActivity: 数据库已存在！");
                        Log.i("Tag", "run: "+getFilesDir());
                        handler.sendEmptyMessage(UPDATE);
                        return;
                    }
                    InputStream is = getAssets().open(dbname);
                    Log.d("Tag", " InputStream is = getAssets().open(dbname);: "+getAssets().toString());
                    FileOutputStream fos = openFileOutput(dbname,MODE_PRIVATE);
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while((len = is.read(buffer))!=-1){
                        fos.write(buffer,0,len);
                    }
                    is.close();
                    fos.close();
                    SharedPreferences.Editor editor = mSP.edit();
                    editor.putBoolean("updateFlag",true);
                    editor.commit();

                    handler.sendEmptyMessage(UPDATE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.rl_allscanvirus:
                startActivity(new Intent(this,VirusScanSpeedActivity.class));
                break;
            case R.id.rl_cloudscanvirus:
                System.out.println("asdfasdfadfasdf");
                Intent intent = new Intent(this,VirusScanSpeedActivity.class);
                intent.putExtra("cloud",true);
                startActivity(intent);
                break;

        }
    }
}
