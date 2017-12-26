package cn.edu.gdmec.android.mobileguard.m2theftguard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Toast;

import cn.edu.gdmec.android.mobileguard.R;

public abstract class BaseSetupActivity extends AppCompatActivity {
    public SharedPreferences sp;
    //手势识别类
    private GestureDetector mGestureDetector;

    //抽象方法 显示前一屏的activity
    public abstract void showNext();

    //抽象方法 显示后一
    public abstract void showPre();

    //用手势识别器去识别触控事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //分析手势事件
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /*
    * 开启新的activity并且关闭自己
    * */
    public void startActivityAndFinishSelf(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        //把设置布局注释掉，布局由具体的子类加载，抽象类不加载
//        setContentView(R.layout.activity_base_setup);

        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (Math.abs(velocityX) < 200) {
                    Toast.makeText(getApplicationContext(), "无效动作，移动太慢", Toast.LENGTH_SHORT).show();
                    return true;
                }
                if ((e2.getRawX() - e1.getRawX()) > 200){
                    //从左向右滑动屏幕，显示上一个界面
                    showPre();
                    overridePendingTransition(R.anim.pre_in,R.anim.pre_out);
                    return true;
                }
                if ((e1.getRawX() - e2.getRawX()) > 200){
                    //从右向左滑动屏幕，显示下一个界面
                    showNext();
                    overridePendingTransition(R.anim.next_in,R.anim.next_out);
                    return true;
                }

                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

    }
}
