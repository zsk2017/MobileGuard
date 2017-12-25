package cn.edu.gdmec.android.mobileguard.m9advancedtools.adapter;
/**
 * Created by Jack on 2017/12/3.
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.edu.gdmec.android.mobileguard.R;
import cn.edu.gdmec.android.mobileguard.m9advancedtools.entity.AppInfo;

/**此类可复用，未加锁和已加锁都可以用此Adapter**/
public class AppLockAdapter extends BaseAdapter {

    private List<AppInfo> appInfos;
    private Context context;

    public AppLockAdapter(List<AppInfo> appInfos, Context context) {
        super();
        this.appInfos = appInfos;
        this.context = context;
    }

    @Override
    public int getCount() {

        return appInfos.size();
    }

    @Override
    public Object getItem(int position) {

        return appInfos.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView !=null && convertView instanceof RelativeLayout){
            holder = (ViewHolder) convertView.getTag();
        }else{
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_list_applock, null);
            holder.mAppIconImgv = (ImageView) convertView.findViewById( R.id.imgv_appicon);
            holder.mAppNameTV = (TextView) convertView.findViewById(R.id.tv_appname);
            holder.mLockIcon = (ImageView) convertView.findViewById(R.id.imgv_lock);
            convertView.setTag(holder);
        }

        final AppInfo appInfo = appInfos.get(position);
        holder.mAppIconImgv.setImageDrawable(appInfo.icon);
        holder.mAppNameTV.setText(appInfo.appName);
        if(appInfo.isLock){
            //表示当前应用已经加锁
            holder.mLockIcon.setBackgroundResource(R.drawable.applock_icon);
        }else{
            //当前应用未加锁
            holder.mLockIcon.setBackgroundResource(R.drawable.appunlock_icon);
        }
        return convertView;
    }

    static class ViewHolder{
        TextView mAppNameTV;
        ImageView mAppIconImgv;
        /**控制图片显示加锁还是不加锁*/
        ImageView mLockIcon;
    }
}

