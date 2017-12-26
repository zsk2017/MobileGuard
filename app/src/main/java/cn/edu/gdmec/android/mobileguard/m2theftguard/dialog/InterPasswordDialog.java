package cn.edu.gdmec.android.mobileguard.m2theftguard.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.edu.gdmec.android.mobileguard.R;


public class InterPasswordDialog extends Dialog implements View.OnClickListener {
    //    对话框标题
    private TextView mTitleTV;
    //   输入密码文本框
    public EditText mInterET;
    //    确认按钮
    public Button mOKBtn;
    //    取消按钮
    public Button mCancleBtn;
//    回调接口
    private MyCallBack myCallBack;
    private Context context;

    public InterPasswordDialog(@NonNull Context context){
        super(context, R.style.dialog_custom);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.inter_password_dialog);
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView(){
        mTitleTV = (TextView) findViewById(R.id.tv_interpwd_title);
        mInterET = (EditText) findViewById(R.id.et_inter_passwoed);
        mOKBtn = (Button) findViewById(R.id.btn_comfirm);
        mCancleBtn = (Button) findViewById(R.id.btn_dismiss);
        mOKBtn.setOnClickListener(this);
        mCancleBtn.setOnClickListener(this);
    }


//    设置对话框标题
    public void setTitle(String title){
        if(!TextUtils.isEmpty(title)){
            mTitleTV.setText(title);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_comfirm:
                myCallBack.confirm();
                break;
            case R.id.btn_dismiss:
                myCallBack.cancle();
                break;
        }
    }

    public String getPassword(){
        return mInterET.getText().toString();
    }
    public void setMyCallBack(MyCallBack myCallBack){
        this.myCallBack = myCallBack;
    }

//    回调接口
    public interface MyCallBack{
        void confirm();
        void cancle();


}
}

