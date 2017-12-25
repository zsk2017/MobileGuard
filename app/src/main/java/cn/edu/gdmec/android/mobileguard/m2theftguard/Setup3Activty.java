package cn.edu.gdmec.android.mobileguard.m2theftguard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import cn.edu.gdmec.android.mobileguard.R;

public class Setup3Activty extends BaseSetupActivity implements View.OnClickListener{
    private EditText mInputPhone;
    @Override
    public void showNext() {
        //判断文本输入框中是否有电话号码
        String safePhone = mInputPhone.getText().toString().trim();
        if (TextUtils.isEmpty(safePhone)){
            Toast.makeText(this, "请输入安全密码", Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("safephone",safePhone);
        editor.commit();
        startActivityAndFinishSelf(Setup4Activty.class);
    }

    @Override
    public void showPre() {
        startActivityAndFinishSelf(Setup2Activty.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3_activty);
        //设置第三个小圆点的颜色
        ((RadioButton)findViewById(R.id.rb_third)).setChecked(true);

        findViewById(R.id.btn_addcontact).setOnClickListener(this);
        mInputPhone = (EditText) findViewById(R.id.et_inputphone);
        String safephone = sp.getString("safephone",null);
        if (!TextUtils.isEmpty(safephone)){
            mInputPhone.setText(safephone);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_addcontact:
                //启动联系人选择activity并获取返回值
                startActivityForResult(new Intent(this,ContactSelectActivity.class),0);
                break;
        }
    }
    //获取被调用的activity的返回值

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            String phone = data.getStringExtra("phone");
            mInputPhone.setText(phone);
        }
    }
}
