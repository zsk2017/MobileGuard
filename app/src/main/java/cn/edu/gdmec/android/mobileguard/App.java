package cn.edu.gdmec.android.mobileguard;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by Administrator on 2017/10/20.
 */

public class App extends Application {
    //添加变量
    public static final String APPLOCK_ACTION = "cn.edu.gdmec.android.mobileguard.m9advancedtools.applock";
    public static final String APPLOCK_CONTENT_URI = "content://cn.edu.gdmec.android.mobileguard.m9advancedtools.applock";
    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        correctSIM();
    }

    public void correctSIM() {
        //检查sim卡是否发生变化
        SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        //获取防盗保护的状态
        boolean protecting = sp.getBoolean("protecting",true);
        if (protecting){
            //得到绑定的sim卡串号
            String bindsim = sp.getString("sim","");
            //得到手机现在的sim卡串号
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            //为了测试在手机序列号上的data 已模拟sim卡被更换的情况
            String realsim = tm.getSimSerialNumber();
//            //测试用
//            realsim="999";
            if (bindsim.equals(realsim)){
                Log.i("", "correctSIM: sim卡未发生变化，还是您的手机");
            }else{
                Log.d("", "correctSIM: SIM卡变化了");
                //由于系统版本的问题，这里的发短信可能与其他手机版本不兼容
//                String safenumber = sp.getString("safephone","");
//                if (!TextUtils.isEmpty(safenumber)){
//                    SmsManager smsManager = SmsManager.getDefault();
//                    smsManager.sendTextMessage(safenumber,null,"你的亲友手机的SIM卡已经被更换！",null,null);
//
//                }


            }

        }
    }
}
