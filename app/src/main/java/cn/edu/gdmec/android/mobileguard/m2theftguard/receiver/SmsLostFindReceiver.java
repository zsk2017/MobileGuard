package cn.edu.gdmec.android.mobileguard.m2theftguard.receiver;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import cn.edu.gdmec.android.mobileguard.R;
import cn.edu.gdmec.android.mobileguard.m2theftguard.service.GPSLocationService;

/**
 * Created by lenovo on 2017/10/23.
 */

public class SmsLostFindReceiver extends BootCompleteReceiver{
    private static final String TAG = SmsLostFindReceiver.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    private ComponentName componentName;
    @Override
    public void onReceive(Context context, Intent intent){
        sharedPreferences = context.getSharedPreferences("config",
                Activity.MODE_PRIVATE);
        boolean protecting = sharedPreferences.getBoolean("protecting",true);
        if(protecting){
            //获取超级管理员
            DevicePolicyManager dpm =  (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            //获取短信数据
            Object[] objs = (Object[]) intent.getExtras().get("pdus");
            for(Object obj : objs){
                SmsMessage smsMessage= SmsMessage.createFromPdu((byte[]) obj);
                //获取来信号码
                String sender = smsMessage.getOriginatingAddress();
                if(sender.startsWith("+86")){
                    sender= sender.substring(3,sender.length());
                }
                String body = smsMessage.getMessageBody();
                String safephone = sharedPreferences.getString("safephone",null);
                //如果该短信是安全号码发送的
                if(!TextUtils.isEmpty(safephone) & sender.equals(safephone)){
                    if("#*location*#".equals(body)){
                        Log.i(TAG,"返回位置信息.");
                        //获取位置 放在服务里面去实现
                        Intent service = new Intent(context, GPSLocationService.class);
                        context.startService(service);
                        abortBroadcast();
                    }else if("#*alarm*#".equals(body)){
                        Log.i(TAG,"播放报警音乐.");
                        MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
                        player.setVolume(1.0f,1.0f);
                        player.start();
                        abortBroadcast();
                    }else if("#*wipedata*#".equals(body)){
                        Log.i(TAG,"远程清除数据.");
                        dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
                        abortBroadcast();
                    }else if("#*lockScreen*#".equals(body)){
                        Log.i(TAG,"远程锁屏.");
                        dpm.resetPassword("123456",0);
                        dpm.lockNow();
                        abortBroadcast();
                    }
                }

            }
        }

    }

}
