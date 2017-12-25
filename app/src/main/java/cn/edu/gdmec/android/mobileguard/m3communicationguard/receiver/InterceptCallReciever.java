package cn.edu.gdmec.android.mobileguard.m3communicationguard.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

import cn.edu.gdmec.android.mobileguard.m3communicationguard.db.dao.BlackNumberDao;

public class InterceptCallReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences mSP = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        boolean BlackNumStatus = mSP.getBoolean("BlackNumStatus",true);
        if (!BlackNumStatus){
            //黑名单拦截关闭
            return;
        }
        BlackNumberDao dao = new BlackNumberDao(context);
        if (!intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){
            String mIncomingNumber = "";
            //如果是来电
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            switch (telephonyManager.getCallState()){
                case TelephonyManager.CALL_STATE_RINGING://震铃状态
                    mIncomingNumber = intent.getStringExtra("incoming_number");
                    if (mIncomingNumber==null){
                        return;
                    }
                    //根据号码查询黑名单信息
                    int blackContactMode = dao.getBlackContactMode(mIncomingNumber);
                    if (blackContactMode == 1 || blackContactMode == 3){
                        Uri uri = Uri.parse("content://call_log/calls");
                        context.getContentResolver().registerContentObserver(uri,true,new CallLogObserver(new Handler(),mIncomingNumber,context));
                        endCall(context);
                    }
                    break;
            }

        }
    }

    private void endCall(Context context) {
        try {
            Class clazz = context.getClassLoader().loadClass("android.os.ServiceManager");
            Method method = clazz.getDeclaredMethod("getService",String.class);
            IBinder iBinder = (IBinder) method.invoke(null,Context.TELECOM_SERVICE);
            ITelephony iteltphony = ITelephony.Stub.asInterface(iBinder);
            iteltphony.endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class CallLogObserver extends ContentObserver {
        private String incomingNumber;
        private Context context;
        public CallLogObserver(Handler handler, String mIncomingNumber, Context context) {
            super(handler);
            this.incomingNumber = mIncomingNumber;
            this.context = context;
        }
//观察数据库内容变化调用的方法
        @Override
        public void onChange(boolean selfChange) {
            Log.i("CallLogObserver", "呼叫记录数据库的内容变化了");
            context.getContentResolver().unregisterContentObserver(this);
            deleteCallLog(incomingNumber,context);
            super.onChange(selfChange);
        }
    }
    public void deleteCallLog(String incomingNumber, Context context){
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://call_log/calls");
        Cursor cursor = resolver.query(uri,new String[]{"_id"},"number=?",new String[]{incomingNumber},"_id desc limit 1");
        if (cursor.moveToNext()){
            String id = cursor.getString(0);
            resolver.delete(uri,"_id=?",new String[]{id});
        }
    }
}
