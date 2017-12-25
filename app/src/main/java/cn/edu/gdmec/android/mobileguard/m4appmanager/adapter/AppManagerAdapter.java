package cn.edu.gdmec.android.mobileguard.m4appmanager.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.edu.gdmec.android.mobileguard.R;
import cn.edu.gdmec.android.mobileguard.m4appmanager.entity.AppInfo;
import cn.edu.gdmec.android.mobileguard.m4appmanager.utils.DensityUtil;
import cn.edu.gdmec.android.mobileguard.m4appmanager.utils.EngineUtils;

/**
 * Created by Administrator on 2017/11/5.
 */

public class AppManagerAdapter extends BaseAdapter {
    private List<AppInfo> UserAppInfos;
    private List<AppInfo> SystemAppInfos;
    private Context context;

    public AppManagerAdapter( List<AppInfo> UserAppInfos, List<AppInfo> SystemAppInfos,Context context){
        super();
        this.UserAppInfos = UserAppInfos;
        this.SystemAppInfos = SystemAppInfos;
        this.context = context;

    }
    @Override
    public int getCount() {
        //因为有两个条目需要用于显示用户进程 系统进程因此需要加2
        return UserAppInfos.size()+SystemAppInfos.size()+2;
    }

    @Override
    public Object getItem(int i) {
        if(i ==0){
            //第0个位置显示的应该是 用户程序的个数的标签
            return null;
        }else if (i == (UserAppInfos.size()+1)){
            return null;
        }
        AppInfo appInfo;
        if (i < (UserAppInfos.size()+1)){
            //用户程序
            appInfo = UserAppInfos.get(i-1);//多了个textview的标签 位置需要-1
        }else {
            //系统程序
            int location = i - UserAppInfos.size()-2;
            appInfo = SystemAppInfos.get(location);
        }
        return appInfo;
    }

     @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        if (i == 0){
            TextView tv = getTextView();
            tv.setText("用户程序："+UserAppInfos.size()+"个");
            return tv;
        }else if (i == (UserAppInfos.size()+1)){
            TextView tv = getTextView();
            tv.setText("系统程序："+SystemAppInfos.size()+"个");
            return tv;
        }
        AppInfo appInfo;
        if (i < (UserAppInfos.size()+1)){
            appInfo = UserAppInfos.get(i-1);
        }else {
            appInfo  = SystemAppInfos.get(i-UserAppInfos.size() - 2);

        }
        ViewHolder viewHolder = null;
        if (view != null && view instanceof  LinearLayout){
            viewHolder = (ViewHolder) view.getTag();
        }else {
            viewHolder = new ViewHolder();
            view =View.inflate(context,R.layout.item_appmanager_list,null);
            viewHolder.mAbouticonTV = (TextView) view.findViewById(R.id.tv_abouticon_app);
            viewHolder.mAppIconImgv = (ImageView) view.findViewById(R.id.imgv_appicon);
            viewHolder.mAppLocationTV = (TextView) view.findViewById(R.id.tv_appisroom);
            viewHolder.mAppSizeTV = (TextView) view.findViewById(R.id.tv_appsize);
            viewHolder.mAppNameTV = (TextView) view.findViewById(R.id.tv_appname);
            viewHolder.mLuanchAppTV = (TextView) view.findViewById(R.id.tv_launch_app);
            viewHolder.mSettingAppTV = (TextView) view.findViewById(R.id.tv_setting_app);
            viewHolder.mShareAppTV = (TextView) view.findViewById(R.id.tv_share_app);
            viewHolder.mUninstallTV = (TextView) view.findViewById(R.id.tv_uninstall_app);
            viewHolder.mAppOptionLL = (LinearLayout) view.findViewById(R.id.ll_option_app);
            viewHolder.mActivityInfo = (TextView) view.findViewById(R.id.tv_activityicon_app);
            view.setTag(viewHolder);
        }
        if (appInfo != null){
            viewHolder.mAppLocationTV.setText(appInfo.getAppLocation(appInfo.isInRoom));
            viewHolder.mAppIconImgv.setImageDrawable(appInfo.icon);
            Log.d("tag", "getView: -----------------------" +appInfo.appSize);
            viewHolder.mAppSizeTV.setText(Formatter.formatFileSize(context,appInfo.appSize));
            Log.d("tag", "getView: -----------------------" +Formatter.formatFileSize(context,appInfo.appSize));
            viewHolder.mAppNameTV.setText(appInfo.appName);
            if (appInfo.isSelected){
                viewHolder.mAppOptionLL.setVisibility(View.VISIBLE);
            }else {
                viewHolder.mAppOptionLL.setVisibility(View.GONE);
            }
            MyClickListener listener = new MyClickListener(appInfo);
            viewHolder.mLuanchAppTV.setOnClickListener(listener);
            viewHolder.mSettingAppTV.setOnClickListener(listener);
            viewHolder.mShareAppTV.setOnClickListener(listener);
            viewHolder.mUninstallTV.setOnClickListener(listener);
            viewHolder.mAbouticonTV.setOnClickListener(listener);
            viewHolder.mActivityInfo.setOnClickListener(listener);
        }
        return view;
    }

    private TextView getTextView(){
        TextView tv = new TextView(context);
        tv.setBackgroundColor(ContextCompat.getColor(context, R.color.graye5));
        tv.setPadding(DensityUtil.dip2px(context,5),
                DensityUtil.dip2px(context,5),
                DensityUtil.dip2px(context,5),
                DensityUtil.dip2px(context,5));
        tv.setTextColor(ContextCompat.getColor(context,R.color.black));
        return tv;
    }
    static class ViewHolder {
        TextView mAbouticonTV;
        TextView mLuanchAppTV;
        TextView mUninstallTV;
        TextView mShareAppTV;
        TextView mSettingAppTV;
        ImageView mAppIconImgv;
        TextView mAppLocationTV;
        TextView mAppSizeTV;
        TextView mAppNameTV;
        TextView mActivityInfo;
        LinearLayout mAppOptionLL;
    }
    class MyClickListener implements View.OnClickListener{
        private AppInfo appInfo;
        public MyClickListener(AppInfo appInfo){
            super();
            this.appInfo = appInfo;
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_launch_app:
                    EngineUtils.startApplication(context,appInfo);
                    break;
                case R.id.tv_share_app:
                    EngineUtils.shareApplication(context,appInfo);
                    break;
                case R.id.tv_setting_app:
                    EngineUtils.SettingAppDetail(context,appInfo);
                    break;
                case R.id.tv_uninstall_app:
                    //卸载应用 需要注册广播接受者
                    if (appInfo.packageName.equals(context.getPackageName())){
                        Toast.makeText(context, "您没有权限卸载此应用! ", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    EngineUtils.uninstallApplication(context,appInfo);
                    break;
                case R.id.tv_abouticon_app:
                    EngineUtils.AbouticonAppDetail(context,appInfo);
                    break;
                case R.id.tv_activityicon_app:
                    EngineUtils.ActivityInfoDetail(context,appInfo);
                    break;
            }
        }
    }
}
