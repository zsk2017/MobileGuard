package cn.edu.gdmec.android.mobileguard.m9advancedtools.utils;

/**
 * Created by Jack on 2017/12/3.
 */
import android.app.Activity;
import android.widget.Toast;

public class UIUtils {

    public static void showToast(final Activity context,final String msg){
        if("main".equals(Thread.currentThread().getName())){
            Toast.makeText(context, msg, 1).show();
        }else{
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, msg, 1).show();
                }
            });
        }
    }
}
