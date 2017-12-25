package cn.edu.gdmec.android.mobileguard.m9advancedtools;

/**
 * Created by Jack on 2017/12/3.
 */

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import cn.edu.gdmec.android.mobileguard.R;
import cn.edu.gdmec.android.mobileguard.m9advancedtools.utils.SmsBackUpUtils;
import cn.edu.gdmec.android.mobileguard.m9advancedtools.utils.UIUtils;
import cn.edu.gdmec.android.mobileguard.m9advancedtools.widget.MyCircleProgress;

public class SMSBackupActivity extends Activity implements OnClickListener{

    private MyCircleProgress mProgressButton;
    /**标识符，用来标识备份状态的*/
    private boolean flag = false;
    private SmsBackUpUtils smsBackUpUtils;
    private static final int CHANGE_BUTTON_TEXT = 100;
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case CHANGE_BUTTON_TEXT:
                    mProgressButton.setText("一键备份");
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView( R.layout.activity_smsbackup);
        smsBackUpUtils = new SmsBackUpUtils();
        initView();
    }

    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(
                getResources().getColor(R.color.bright_red));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView) findViewById(R.id.tv_title)).setText("短信备份");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);

        mProgressButton = (MyCircleProgress) findViewById(R.id.mcp_smsbackup);
        mProgressButton.setOnClickListener(this);
    }


    @Override
    protected void onDestroy() {
        flag = false;
        smsBackUpUtils.setFlag(flag);
        super.onDestroy();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.mcp_smsbackup:
                if(flag){
                    flag = false;
                    mProgressButton.setText("一键备份");
                }else{
                    flag = true;
                    mProgressButton.setText("取消备份");
                }
                smsBackUpUtils.setFlag(flag);
                new Thread(){

                    public void run() {
                        try {
                            boolean backUpSms = smsBackUpUtils.backUpSms(SMSBackupActivity.this, new SmsBackUpUtils.BackupStatusCallback () {



                                @Override
                                public void onSmsBackup(int process) {
                                    mProgressButton.setProcess(process);
                                }

                                @Override
                                public void beforeSmsBackup(int size) {
                                    if(size <= 0){
                                        flag = false;
                                        smsBackUpUtils.setFlag(flag);
                                        UIUtils.showToast(SMSBackupActivity.this, "您还没有短信！");
                                        handler.sendEmptyMessage(CHANGE_BUTTON_TEXT);
                                    }else{
                                        mProgressButton.setMax(size);
                                    }
                                }
                            });
                            if(backUpSms){
                                UIUtils.showToast(SMSBackupActivity.this, "备份成功");
                            }else{
                                UIUtils.showToast(SMSBackupActivity.this, "备份失败");
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            UIUtils.showToast(SMSBackupActivity.this, "文件生成失败");
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                            UIUtils.showToast(SMSBackupActivity.this, "SD卡不可用或SD卡内存不足");
                        } catch (IOException e) {
                            e.printStackTrace();
                            UIUtils.showToast(SMSBackupActivity.this, "读写错误");
                        }
                    };
                }.start();
                break;
        }
    }
}

