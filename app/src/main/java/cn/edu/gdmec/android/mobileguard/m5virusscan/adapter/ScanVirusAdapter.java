package cn.edu.gdmec.android.mobileguard.m5virusscan.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.edu.gdmec.android.mobileguard.R;
import cn.edu.gdmec.android.mobileguard.m5virusscan.entity.ScanAppInfo;

/**
 * Created by Administrator on 2017/11/16.
 */

public class ScanVirusAdapter extends BaseAdapter {
    private List<ScanAppInfo> mScanAppInfos;
    private Context context;
    public ScanVirusAdapter(List<ScanAppInfo> scanAppInfos,Context context){
        super();
        mScanAppInfos = scanAppInfos;
        this.context = context;
    }
    static class ViewHolder{
        ImageView mAppIconImgv;
        TextView mAppNameTV;
        ImageView mScanIconImgv;
    }
    @Override
    public int getCount() {
        return mScanAppInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mScanAppInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null){
            view = View.inflate(context, R.layout.item_list_applock,null);
            holder = new ViewHolder();
            holder.mAppIconImgv = (ImageView) view.findViewById(R.id.imgv_appicon);
            holder.mAppNameTV = (TextView) view.findViewById(R.id.tv_appname);
            holder.mScanIconImgv = (ImageView) view.findViewById(R.id.imgv_lock);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        ScanAppInfo scanAppinfo = mScanAppInfos.get(position);
        if (!scanAppinfo.isVirus){
            holder.mScanIconImgv.setImageResource(R.drawable.blue_right_icon);
            holder.mAppNameTV.setTextColor(context.getResources().getColor(R.color.black));
            holder.mAppNameTV.setText(scanAppinfo.appName);

        }else {
            holder.mAppNameTV.setTextColor(context.getResources().getColor(R.color.bright_red));
            holder.mAppNameTV.setText(scanAppinfo.appName+"("+scanAppinfo.description+")");
        }
        holder.mAppIconImgv.setImageDrawable(scanAppinfo.appicon);
        return view;
    }
}
