package cn.edu.gdmec.android.mobileguard.m2theftguard;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import cn.edu.gdmec.android.mobileguard.R;
public class LostFindActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView mSafePhonrTV;
    private RelativeLayout mInterSetupRL;
    private SharedPreferences msharePreferences;
    private ToggleButton mToggleButton;
    private TextView mProtectStatusTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_find);
        msharePreferences = getSharedPreferences("config",MODE_PRIVATE);
        if (!isSetUp()){
            //如果没有进入设置向导，则进入
            startSetUpActivity();
        }
        initView();
    }
    private boolean isSetUp(){
        return msharePreferences.getBoolean("isSetUp",false);
    }
    public void initView(){
        TextView mTitleTV = (TextView)findViewById(R.id.tv_title);
        mTitleTV.setText("手机防盗");
        ImageView mLeftImgv = (ImageView)findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.purple));
        mSafePhonrTV = (TextView)findViewById(R.id.tv_safephone);
        mSafePhonrTV.setText(msharePreferences.getString("safephone",""));
        mToggleButton = (ToggleButton)findViewById(R.id.togglebtn_lostfind);
        mInterSetupRL = (RelativeLayout)findViewById(R.id.rl_inter_setup_wizard);
        mInterSetupRL.setOnClickListener(this);
        mProtectStatusTV = (TextView)findViewById(R.id.tv_lostfind_protectstauts);
        //查询手机防盗是否开启，默认为开启
        boolean protecting = msharePreferences.getBoolean("protecting",true);
        if (protecting){
            mProtectStatusTV.setText("防盗保护已经开启");
            mToggleButton.setChecked(true);
        }else{
            mProtectStatusTV.setText("防盗保护没有开启");
            mToggleButton.setChecked(false);
        }
        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    mProtectStatusTV.setText("防盗保护已经开启");
                }else{
                    mProtectStatusTV.setText("防盗保护没有开启");
                }
                SharedPreferences.Editor editor = msharePreferences.edit();
                editor.putBoolean("protecting",isChecked);
                editor.commit();
            }
        });
    }
    private void startSetUpActivity(){
        Intent intent = new Intent(LostFindActivity.this,Setup1Activty.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_inter_setup_wizard:

                //重新进入设置向导
                startSetUpActivity();
            case R.id.imgv_leftbtn:
                //重新进入设置向导
                finish();
                break;
        }
    }
}
